package com.example.partycoruna.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycoruna.R;
import com.example.partycoruna.Adapters.FavoritosAdapter; // Tendrás que crearlo en la carpeta principal o en una carpeta 'adapters'
import com.example.partycoruna.models.Evento;
import com.example.partycoruna.network.ApiClient;
import com.example.partycoruna.network.ApiService;

import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FavoritosFragment extends Fragment {

    private RecyclerView recyclerView;
    private FavoritosAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflamos el XML de la pantalla de favoritos
        View root = inflater.inflate(R.layout.fragment_favoritos, container, false);

        recyclerView = root.findViewById(R.id.rvFavorites);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        cargarFavoritos();

        return root;
    }

    private void cargarFavoritos() {
        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        // Aquí deberías obtener el token de tu AuthManager que veo en la carpeta helpers
        String token = "Bearer " + "TU_TOKEN_AQUÍ";

        apiService.getFavoritos(token).enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new FavoritosAdapter(response.body(), position -> {
                        // Lógica para borrar favorito
                        Toast.makeText(getContext(), "Eliminando...", Toast.LENGTH_SHORT).show();
                    });
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                Toast.makeText(getContext(), "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

