package com.example.partycoruna;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycoruna.adapters.UserSearchAdapter;
import com.example.partycoruna.helpers.AuthManager;
import com.example.partycoruna.models.FollowRequest;
import com.example.partycoruna.models.Friend;
import com.example.partycoruna.network.ApiClient;
import com.example.partycoruna.network.ApiService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchUsersActivity extends AppCompatActivity {

    private EditText etSearch;
    private RecyclerView recyclerResults;
    private LinearLayout layoutEmptyState;
    private ProgressBar progressBar;
    private UserSearchAdapter adapter;
    private List<Friend> userList = new ArrayList<>();
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_users);

        etSearch = findViewById(R.id.etSearch);
        recyclerResults = findViewById(R.id.recyclerResults);
        layoutEmptyState = findViewById(R.id.layoutEmptyState);
        progressBar = findViewById(R.id.progressBar);
        View btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> finish());

        apiService = ApiClient.getClient().create(ApiService.class);

        adapter = new UserSearchAdapter(userList, this::followUser);
        recyclerResults.setLayoutManager(new LinearLayoutManager(this));
        recyclerResults.setAdapter(adapter);

        etSearch.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                performSearch(etSearch.getText().toString());
                return true;
            }
            return false;
        });
    }

    private void performSearch(String query) {
        if (query.isEmpty()) return;

        progressBar.setVisibility(View.VISIBLE);
        layoutEmptyState.setVisibility(View.GONE);
        recyclerResults.setVisibility(View.GONE);

        String token = AuthManager.getToken(this);
        String authHeader = "Bearer " + token;

        apiService.searchUsers(authHeader, query).enqueue(new Callback<com.example.partycoruna.models.UserSearchResponse>() {
            @Override
            public void onResponse(Call<com.example.partycoruna.models.UserSearchResponse> call, Response<com.example.partycoruna.models.UserSearchResponse> response) {
                progressBar.setVisibility(View.GONE);
                if (response.isSuccessful() && response.body() != null) {
                    userList.clear();
                    userList.addAll(response.body().getUsers());
                    adapter.notifyDataSetChanged();

                    if (userList.isEmpty()) {
                        Toast.makeText(SearchUsersActivity.this, R.string.no_found_users, Toast.LENGTH_SHORT).show();
                        layoutEmptyState.setVisibility(View.VISIBLE);
                    } else {
                        recyclerResults.setVisibility(View.VISIBLE);
                    }
                } else {
                    Toast.makeText(SearchUsersActivity.this, R.string.search_error, Toast.LENGTH_SHORT).show();
                    layoutEmptyState.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<com.example.partycoruna.models.UserSearchResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(SearchUsersActivity.this, getString(R.string.failure_prefix, t.getMessage()), Toast.LENGTH_LONG).show();
                layoutEmptyState.setVisibility(View.VISIBLE);
            }
        });
    }

    private void followUser(Friend user, android.widget.Button btnFollow) {
        String token = AuthManager.getToken(this);
        String authHeader = "Bearer " + token;
        FollowRequest req = new FollowRequest(user.getId());

        // Deshabilitar temporalmente para evitar doble click
        btnFollow.setEnabled(false);
        btnFollow.setText("...");

        apiService.followUser(authHeader, req).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(SearchUsersActivity.this, getString(R.string.now_following, user.getName()), Toast.LENGTH_SHORT).show();
                    // Feedback visual permanente
                    btnFollow.setText(R.string.seguidor);
                    btnFollow.setBackgroundColor(android.graphics.Color.parseColor("#4CAF50")); // Verde éxito
                    btnFollow.setTextColor(android.graphics.Color.WHITE);
                    // No habilitamos de nuevo porque ya "está seguido" (si se quisiera toggle, habría que cambiar lógica)
                } else {
                    btnFollow.setEnabled(true);
                    btnFollow.setText(R.string.seguir); // Restaurar
                    ResponseBody errorBody = response.errorBody();
                    if (errorBody != null) {
                        try (ResponseBody body = errorBody) {
                            String errorStr = body.string();
                            Toast.makeText(SearchUsersActivity.this, "Error " + response.code() + ": " + errorStr, Toast.LENGTH_LONG).show();
                        } catch (IOException e) {
                            Toast.makeText(SearchUsersActivity.this, "Error " + response.code(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(SearchUsersActivity.this, "Error " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                btnFollow.setEnabled(true);
                btnFollow.setText(R.string.seguir);
                Toast.makeText(SearchUsersActivity.this, getString(R.string.network_failure, t.getMessage()), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
