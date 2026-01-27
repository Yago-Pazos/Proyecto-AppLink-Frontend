package com.example.partycoruna.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.os.Bundle;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycoruna.R;
import com.example.partycoruna.RegisterActivity;
import com.example.partycoruna.adapters.AmigosAdapter;
import com.example.partycoruna.helpers.AuthManager;
import com.example.partycoruna.models.Friend;
import com.example.partycoruna.network.ApiClient;
import com.example.partycoruna.network.ApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AmigosFragment extends Fragment {

    private List<Friend> amigosList = new ArrayList<>();
    private AmigosAdapter adapter;
    private boolean isDebuggable = false;
    private ProgressBar progressBar;
    private TextView emptyText;
    private View btnAddFriend;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_amigos, container, false);

        Context ctx = requireContext();
        isDebuggable = (ctx.getApplicationInfo().flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;

        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewAmigos);
        progressBar = view.findViewById(R.id.progressBarAmigos);
        emptyText = view.findViewById(R.id.emptyTextAmigos);
        btnAddFriend = view.findViewById(R.id.btnAddFriend);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AmigosAdapter(amigosList, (friend, position) -> confirmDelete(friend, position));
        recyclerView.setAdapter(adapter);

        if (btnAddFriend != null) {
            btnAddFriend.setOnClickListener(v -> Toast.makeText(requireContext(), "Añadir amigo (pendiente)", Toast.LENGTH_SHORT).show());
        }

        // Intentamos cargar inicialmente
        loadAmigos(adapter);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Recargar al volver al fragment (p. ej. después de registrar/iniciar sesión)
        if (adapter != null) {
            loadAmigos(adapter);
        }
    }

    private void promptLogin() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle("Autenticación requerida")
                .setMessage("Necesitas iniciar sesión para ver tus amigos. ¿Quieres ir a Registrar/Iniciar sesión ahora?")
                .setPositiveButton("Ir", (d, w) -> {
                    Intent i = new Intent(requireContext(), RegisterActivity.class);
                    startActivity(i);
                })
                .setNegativeButton("Cancelar", (d, w) -> d.dismiss());

        if (isDebuggable) {
            builder.setNeutralButton("Pegar token", (d, w) -> showPasteTokenDialog());
        }

        builder.show();
    }

    private void showPasteTokenDialog() {
        EditText input = new EditText(requireContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        new AlertDialog.Builder(requireContext())
                .setTitle("Pegar token (debug)")
                .setMessage("Pega aquí el token para pruebas y se guardará en SharedPreferences")
                .setView(input)
                .setPositiveButton("Guardar", (d, w) -> {
                    String t = input.getText().toString().trim();
                    if (!t.isEmpty()) {
                        AuthManager.saveToken(requireContext(), t);
                        Toast.makeText(requireContext(), "Token guardado (debug)", Toast.LENGTH_SHORT).show();
                        // recargar
                        if (adapter != null) loadAmigos(adapter);
                    } else {
                        Toast.makeText(requireContext(), "Token vacío", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancelar", (d, w) -> d.dismiss())
                .show();
    }

    private void loadAmigos(AmigosAdapter adapter) {
        // Obtener token usando el AuthManager centralizado (usa PREFS_NAME y KEY_TOKEN)
        Context ctx = requireContext();
        String token = AuthManager.getToken(ctx);


        if (token == null || token.isEmpty()) {
            // Intentar migración desde implementaciones previas
            SharedPreferences legacy = ctx.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            String legacyToken = legacy.getString("token", null);
            if (legacyToken != null && !legacyToken.isEmpty()) {
                // Guardar con AuthManager para estandarizar el uso
                AuthManager.saveToken(ctx, legacyToken);
                token = legacyToken;
            } else {
                // Si no lo encontramos en la clave "token", inspeccionamos el archivo "MyAppPrefs" (AuthManager usa ese nombre)
                SharedPreferences appPrefs = ctx.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE);
                Map<String, ?> all = appPrefs.getAll();
                if (!all.isEmpty() && all.containsKey("auth_token")) {
                    Object v = all.get("auth_token");
                    if (v instanceof String) {
                        token = (String) v;
                    }
                }
            }
        }

        if (token == null || token.isEmpty()) {
            Toast.makeText(getContext(), "No hay token de autenticación", Toast.LENGTH_SHORT).show();
            promptLogin();
            return;
        }

        // Normalizar token: quitar comillas y evitar "Bearer Bearer"
        String raw = token.trim();
        if (raw.startsWith("\"") && raw.endsWith("\"")) {
            raw = raw.substring(1, raw.length() - 1);
        }
        String authHeader = raw.startsWith("Bearer ") ? raw : ("Bearer " + raw);

        setLoading(true);
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<List<Friend>> call = apiService.getMyFriends(authHeader);
        call.enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                setLoading(false);
                if (response.isSuccessful() && response.body() != null) {
                    amigosList.clear();
                    amigosList.addAll(response.body());
                    adapter.notifyDataSetChanged();
                    updateEmptyState();
                } else {
                    Toast.makeText(getContext(), "Error al cargar amigos", Toast.LENGTH_SHORT).show();
                    updateEmptyState();
                }
            }

            @Override
            public void onFailure(Call<List<Friend>> call, Throwable t) {
                setLoading(false);
                Toast.makeText(getContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                updateEmptyState();
            }
        });
    }

    private void confirmDelete(Friend friend, int position) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Eliminar amigo")
                .setMessage("¿Quieres eliminar a " + friend.getName() + " de tu crew?")
                .setPositiveButton("Eliminar", (d, w) -> deleteFriend(friend, position))
                .setNegativeButton("Cancelar", (d, w) -> d.dismiss())
                .show();
    }

    private void deleteFriend(Friend friend, int position) {
        String token = AuthManager.getToken(requireContext());
        if (token == null || token.isEmpty()) {
            Toast.makeText(requireContext(), "No hay token", Toast.LENGTH_SHORT).show();
            promptLogin();
            return;
        }
        String raw = token.trim();
        if (raw.startsWith("\"") && raw.endsWith("\"")) {
            raw = raw.substring(1, raw.length() - 1);
        }
        String authHeader = raw.startsWith("Bearer ") ? raw : ("Bearer " + raw);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        setLoading(true);
        apiService.deleteFriend(authHeader, friend.getId()).enqueue(new Callback<>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                setLoading(false);
                if (response.isSuccessful()) {
                    amigosList.remove(position);
                    adapter.notifyItemRemoved(position);
                    updateEmptyState();
                } else {
                    Toast.makeText(requireContext(), "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                setLoading(false);
                Toast.makeText(requireContext(), "Error de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setLoading(boolean loading) {
        if (progressBar != null) {
            progressBar.setVisibility(loading ? View.VISIBLE : View.GONE);
        }
    }

    private void updateEmptyState() {
        if (emptyText == null) return;
        emptyText.setVisibility(amigosList.isEmpty() ? View.VISIBLE : View.GONE);
    }
}
