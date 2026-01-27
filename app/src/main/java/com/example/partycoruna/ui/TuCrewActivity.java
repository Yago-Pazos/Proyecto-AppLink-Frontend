package com.example.partycoruna.ui;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycoruna.R;
import com.example.partycoruna.adapters.AmigosAdapter;
import com.example.partycoruna.helpers.AuthManager;
import com.example.partycoruna.models.Friend;
import com.example.partycoruna.network.ApiClient;
import com.example.partycoruna.network.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TuCrewActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private AmigosAdapter adapter;
    private ApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tu_crew);

        recyclerView = findViewById(R.id.recyclerFriends);
        progressBar = findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new AmigosAdapter(null, (friend, position) -> deleteFriend(friend, position));
        recyclerView.setAdapter(adapter);

        apiService = ApiClient.getClient().create(ApiService.class);
        fetchFriends();
    }

    private void fetchFriends() {
        progressBar.setVisibility(View.VISIBLE);
        String token = AuthManager.getToken(this);
        String authHeader = token != null && !token.isEmpty()
                ? (token.startsWith("Bearer ") ? token : "Bearer " + token)
                : null;
        Call<List<Friend>> call = apiService.getMyFriends(authHeader);
        call.enqueue(new Callback<List<Friend>>() {
            @Override
            public void onResponse(Call<List<Friend>> call, Response<List<Friend>> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    adapter.updateList(response.body());
                } else {
                    Toast.makeText(TuCrewActivity.this, "Error cargando amigos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Friend>> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(TuCrewActivity.this, "Fallo de red: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void deleteFriend(Friend friend, int position) {
        if (friend == null) return;
        String token = AuthManager.getToken(this);
        String authHeader = token != null && !token.isEmpty()
                ? (token.startsWith("Bearer ") ? token : "Bearer " + token)
                : null;
        Call<Void> call = apiService.deleteFriend(authHeader, friend.getId());
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    adapter.removeAt(position);
                    Toast.makeText(TuCrewActivity.this, "Amigo eliminado", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(TuCrewActivity.this, "No se pudo eliminar", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(TuCrewActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
