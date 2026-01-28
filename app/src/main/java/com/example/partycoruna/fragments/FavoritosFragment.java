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
        /*
        apiService.getFavoritos().enqueue(new Callback<List<Evento>>() {
            @Override
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    adapter = new FavoritosAdapter(response.body(), position -> {
                         // borrar
                    });
                    recyclerView.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) { }
        });
        */

        // MOCK DATA PARA VISUALIZACIÓN
        List<Evento> mockEventos = new java.util.ArrayList<>();
        mockEventos.add(new Evento(1, "Neon Nights Festival", "Vie, 24 Nov • 22:00", "Club Supernova", "https://images.unsplash.com/photo-1545128485-c400e7702796?q=80&w=2070&auto=format&fit=crop", "ELECTRÓNICA"));
        mockEventos.add(new Evento(2, "Sunset Rooftop Party", "Sáb, 25 Nov • 18:00", "Sky Bar Coruña", "https://images.unsplash.com/photo-1514525253440-b393452e3383?q=80&w=2666&auto=format&fit=crop", "CHILL OUT"));
        mockEventos.add(new Evento(3, "Rock Legends Live", "Dom, 26 Nov • 21:00", "Sala Mardigras", "https://images.unsplash.com/photo-1459749411177-0473ef71607b?q=80&w=2070&auto=format&fit=crop", "ROCK"));
        mockEventos.add(new Evento(4, "Jazz & Wine Night", "Jue, 30 Nov • 20:30", "Jazz Filloa", "https://images.unsplash.com/photo-1415201364774-f6f0bb35f28f?q=80&w=2070&auto=format&fit=crop", "JAZZ"));
        mockEventos.add(new Evento(5, "Reggaeton Beach", "Vie, 01 Dic • 23:00", "Sala Pelícano", "https://images.unsplash.com/photo-1574390353291-111c42251863?q=80&w=2083&auto=format&fit=crop", "URBANO"));
        mockEventos.add(new Evento(6, "Indie Codes", "Sáb, 02 Dic • 21:30", "Playa Club", "https://images.unsplash.com/photo-1501612780327-45045538702b?q=80&w=2070&auto=format&fit=crop", "INDIE"));
        mockEventos.add(new Evento(7, "Salsa & Bachata", "Dom, 03 Dic • 19:00", "Latin Steps", "https://images.unsplash.com/photo-1533174072545-e8d4aa97edf9?q=80&w=2070&auto=format&fit=crop", "LATINO"));
        mockEventos.add(new Evento(8, "Techno Bunker", "Vie, 08 Dic • 01:00", "O Tunel", "https://images.unsplash.com/photo-1571266028243-3716f02d2d2e?q=80&w=2072&auto=format&fit=crop", "TECHNO"));

        adapter = new FavoritosAdapter(mockEventos, position -> {
            Toast.makeText(getContext(), "Eliminado de favoritos", Toast.LENGTH_SHORT).show();
        });
        recyclerView.setAdapter(adapter);
    }
}

