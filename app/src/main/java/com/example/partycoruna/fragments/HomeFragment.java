package com.example.partycoruna.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.partycoruna.R;


import android.graphics.Color;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.partycoruna.adapters.EventsAdapter;
import com.example.partycoruna.helpers.AuthManager;
import com.example.partycoruna.models.Evento;
import com.example.partycoruna.network.ApiClient;
import com.example.partycoruna.network.ApiService;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private TextView tvUserName;
    private TextView tabDestacados, tabParaTi;
    private RecyclerView rvEvents;
    private EventsAdapter adapter;

    // Data
    private List<Evento> allEvents = new ArrayList<>();
    private String currentCategory = "TODOS";
    private String currentSearchQuery = "";
    private EditText etSearch;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUserName = view.findViewById(R.id.tvUserName);
        tabDestacados = view.findViewById(R.id.tabDestacados);
        tabParaTi = view.findViewById(R.id.tabParaTi);
        rvEvents = view.findViewById(R.id.rvEvents);
        etSearch = view.findViewById(R.id.etSearch);
        
        // Search Listener
        if (etSearch != null) {
            etSearch.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    currentSearchQuery = s.toString();
                    applyFilters();
                }

                @Override
                public void afterTextChanged(Editable s) {}
            });
        }
        
        // Menu Button Logic
        View btnMenu = view.findViewById(R.id.btnMenu);
        if (btnMenu != null) {
            btnMenu.setOnClickListener(v -> {
                if (getActivity() instanceof com.example.partycoruna.MainActivity) {
                    ((com.example.partycoruna.MainActivity) getActivity()).openDrawer();
                }
            });
        }

        rvEvents.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set User Name
        String name = AuthManager.getUsername(requireContext());
        if (name != null && !name.isEmpty()) {
            tvUserName.setText(name);
        }

        // Fetch real user profile to get connection name
        fetchUserProfile();

        // Tabs Logic
        tabDestacados.setOnClickListener(v -> selectTab(true));
        tabParaTi.setOnClickListener(v -> selectTab(false));

        // Initial Load
        selectTab(true);
    }

    private void fetchUserProfile() {
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getMyProfile().enqueue(new Callback<com.example.partycoruna.models.UserProfileResponse>() {
            @Override
            public void onResponse(Call<com.example.partycoruna.models.UserProfileResponse> call, Response<com.example.partycoruna.models.UserProfileResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update cache and UI
                    AuthManager.saveUsername(requireContext(), response.body().fullName);
                    tvUserName.setText(response.body().fullName);
                }
            }
            @Override
            public void onFailure(Call<com.example.partycoruna.models.UserProfileResponse> call, Throwable t) { }
        });
    }

    private void selectTab(boolean isDestacados) {
        if (isDestacados) {
            // Style Destacados Active
            tabDestacados.setBackgroundResource(R.drawable.btn_primary);
            tabDestacados.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

            // Style Para Ti Inactive
            tabParaTi.setBackgroundColor(Color.TRANSPARENT);
            tabParaTi.setTextColor(Color.parseColor("#AAAAAA"));

        } else {
            // Style Para Ti Active
            tabParaTi.setBackgroundResource(R.drawable.btn_primary);
            tabParaTi.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));

            // Style Destacados Inactive
            tabDestacados.setBackgroundColor(Color.TRANSPARENT);
            tabDestacados.setTextColor(Color.parseColor("#AAAAAA"));
        }

        loadEvents(isDestacados);

        loadEvents(isDestacados);
        
        // Reset filter when switching tabs
        currentCategory = "TODOS";
        currentSearchQuery = "";
        if (etSearch != null) etSearch.setText("");
    }

    private void loadEvents(boolean isDestacados) {
        String filter = isDestacados ? "featured" : "recommendations";

        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.getEvents(filter).enqueue(new Callback<List<Evento>>() {
            public void onResponse(Call<List<Evento>> call, Response<List<Evento>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allEvents = response.body();
                    filterEvents(currentCategory); // Apply current filter (usually TODOS on load)

                    // adapter creation moved below to use filtered list or initial list
                    List<Evento> eventsToShow = new ArrayList<>(allEvents);

                    adapter = new EventsAdapter(eventsToShow,
                            evento -> {
                                // Click en evento
                                Toast.makeText(getContext(), "Click en " + evento.getNombre(), Toast.LENGTH_SHORT).show();
                            },
                            evento -> {
                                // Click en favorito
                                toggleFavorite(evento);
                            },
                            category -> {
                                // Click en categoria
                                filterEvents(category);
                            }
                    );
                    rvEvents.setAdapter(adapter);
                    // Re-apply filter just in case logic needs it, triggers update
                    applyFilters();
                } else {
                    // If API fails or empty, show Mock Data
                    loadMockData();
                }
            }

            @Override
            public void onFailure(Call<List<Evento>> call, Throwable t) {
                loadMockData();
            }
        });
    }

    private void toggleFavorite(Evento evento) {
        // La UI ya se actualizó optimísticamente en el Adapter.
        // Aquí hacemos la llamada de fondo.
        ApiService api = ApiClient.getClient().create(ApiService.class);
        api.toggleFavorite(evento.getId()).enqueue(new Callback<com.example.partycoruna.models.ToggleFavoriteResponse>() {
            @Override
            public void onResponse(Call<com.example.partycoruna.models.ToggleFavoriteResponse> call, Response<com.example.partycoruna.models.ToggleFavoriteResponse> response) {
                if (!response.isSuccessful()) {
                    // Si falla, revertir cambio visual (opcional, por ahora solo toast)
                    Toast.makeText(getContext(), "Error al actualizar favorito", Toast.LENGTH_SHORT).show();
                } else {
                    // Confirmado por backend
                    // response.body().isFavorite nos dice el estado real
                }
            }

            @Override
            public void onFailure(Call<com.example.partycoruna.models.ToggleFavoriteResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error de red", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMockData() {
        List<Evento> mockEventos = new ArrayList<>();
        mockEventos.add(new Evento(1, "Neon Nights Festival", "Vie, 24 Nov • 22:00", "Club Supernova", "https://images.unsplash.com/photo-1545128485-c400e7702796?q=80&w=2070&auto=format&fit=crop", "ELECTRÓNICA"));
        mockEventos.add(new Evento(2, "Sunset Rooftop Party", "Sáb, 25 Nov • 18:00", "Sky Bar Coruña", "https://images.unsplash.com/photo-1514525253440-b393452e3383?q=80&w=2666&auto=format&fit=crop", "CHILL OUT"));
        mockEventos.add(new Evento(3, "Rock Legends Live", "Dom, 26 Nov • 21:00", "Sala Mardigras", "https://images.unsplash.com/photo-1459749411177-0473ef71607b?q=80&w=2070&auto=format&fit=crop", "ROCK"));
        mockEventos.add(new Evento(4, "Jazz & Wine Night", "Jue, 30 Nov • 20:30", "Jazz Filloa", "https://images.unsplash.com/photo-1415201364774-f6f0bb35f28f?q=80&w=2070&auto=format&fit=crop", "JAZZ"));
        mockEventos.add(new Evento(5, "Reggaeton Beach", "Vie, 01 Dic • 23:00", "Sala Pelícano", "https://images.unsplash.com/photo-1574390353291-111c42251863?q=80&w=2083&auto=format&fit=crop", "URBANO"));
        mockEventos.add(new Evento(6, "Indie Codes", "Sáb, 02 Dic • 21:30", "Playa Club", "https://images.unsplash.com/photo-1501612780327-45045538702b?q=80&w=2070&auto=format&fit=crop", "INDIE"));
        mockEventos.add(new Evento(7, "Salsa & Bachata", "Dom, 03 Dic • 19:00", "Latin Steps", "https://images.unsplash.com/photo-1533174072545-e8d4aa97edf9?q=80&w=2070&auto=format&fit=crop", "LATINO"));
        mockEventos.add(new Evento(8, "Techno Bunker", "Vie, 08 Dic • 01:00", "O Tunel", "https://images.unsplash.com/photo-1571266028243-3716f02d2d2e?q=80&w=2072&auto=format&fit=crop", "TECHNO"));

        allEvents = mockEventos;

        adapter = new EventsAdapter(new ArrayList<>(allEvents),
                evento -> Toast.makeText(getContext(), evento.getNombre(), Toast.LENGTH_SHORT).show(),
                evento -> Toast.makeText(getContext(), "Like en Demo: " + evento.getNombre(), Toast.LENGTH_SHORT).show(),
                category -> filterEvents(category)
        );
        rvEvents.setAdapter(adapter);
        applyFilters(); // Apply filter
    }




    private void filterEvents(String category) {
        // Toggle logic: if clicking same category, reset to TODOS
        if (this.currentCategory.equalsIgnoreCase(category)) {
            category = "TODOS";
        }
        this.currentCategory = category;
        applyFilters();
    }

    private void applyFilters() {
        List<Evento> filtered = new ArrayList<>();
        
        for (Evento e : allEvents) {
            boolean matchesCategory = currentCategory.equals("TODOS") || 
                                      (e.getCategoria() != null && e.getCategoria().equalsIgnoreCase(currentCategory));
            
            boolean matchesSearch = currentSearchQuery.isEmpty() || 
                                    (e.getNombre() != null && e.getNombre().toLowerCase().contains(currentSearchQuery.toLowerCase()));

            if (matchesCategory && matchesSearch) {
                filtered.add(e);
            }
        }
        
        if (adapter != null) {
            adapter.updateEvents(filtered);
        }
    }
}

