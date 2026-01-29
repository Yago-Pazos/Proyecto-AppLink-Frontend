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
import com.example.partycoruna.adapters.FavoritosAdapter;
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

        // Llamada a la API (comentada temporalmente para ver el diseño con ejemplos)
        apiService.getFavoritos().enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Evento> eventos = response.body();
                    adapter = new FavoritosAdapter(eventos, position -> {
                         // borrar (toggle favorite logic)
                         Evento eventToDelete = eventos.get(position);
                         apiService.toggleFavorite(eventToDelete.getId()).enqueue(new Callback<com.example.partycoruna.models.ToggleFavoriteResponse>() {
                             @Override
                             public void onResponse(Call<com.example.partycoruna.models.ToggleFavoriteResponse> call, Response<com.example.partycoruna.models.ToggleFavoriteResponse> response) {
                                 if (response.isSuccessful()) {
                                     // Remove from list
                                     eventos.remove(position);
                                     adapter.notifyItemRemoved(position);
                                     adapter.notifyItemRangeChanged(position, eventos.size());
                                     Toast.makeText(getContext(), "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
                                 } else {
                                     Toast.makeText(getContext(), "Error al eliminar", Toast.LENGTH_SHORT).show();
                                 }
                             }

                             @Override
                             public void onFailure(Call<com.example.partycoruna.models.ToggleFavoriteResponse> call, Throwable t) {
                                 Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
                             }
                         });
                    });
                    recyclerView.setAdapter(adapter);
                } else {
                     // Empty or Error
                     Toast.makeText(getContext(), "No tienes favoritos aún", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) { 
                Toast.makeText(getContext(), "Error de red al cargar favoritos", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
