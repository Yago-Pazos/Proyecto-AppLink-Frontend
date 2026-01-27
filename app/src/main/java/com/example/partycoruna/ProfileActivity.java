package com.example.partycoruna;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.partycoruna.helpers.AuthManager;
import com.example.partycoruna.models.UserProfileResponse;
import com.example.partycoruna.network.ApiClient;
import com.example.partycoruna.network.ApiService;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* ProfileActivity
 - Muestra el perfil del usuario. Usa `user.handle` si el backend lo devuelve; si no, usa el username guardado en AuthManager.
 - IDs del layout: tvName, tvHandle, tvEvents, tvFriends, imgAvatar, btnEditAvatar, btnInstagram, btnTwitter, btnMenu
 */

public class ProfileActivity extends AppCompatActivity {

    // UI
    private TextView tvName;
    private TextView tvHandle;
    private TextView tvEvents;
    private TextView tvFriends;
    private ImageView imgAvatar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Referencias UI
        tvName = findViewById(R.id.tvName);
        tvHandle = findViewById(R.id.tvHandle);
        tvEvents = findViewById(R.id.tvEvents);
        tvFriends = findViewById(R.id.tvFriends);
        imgAvatar = findViewById(R.id.imgAvatar);

        loadUserProfile();
    }

    private void loadUserProfile() {

        // Obtener token guardado
        String token = AuthManager.getToken(this);

        if (token == null) {
            // Redirigir al login si no hay token
            return;
        }

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        Call<UserProfileResponse> call =
                apiService.getMyProfile("Bearer " + token);

        call.enqueue(new Callback<UserProfileResponse>() {
            @Override
            public void onResponse(Call<UserProfileResponse> call,
                                   Response<UserProfileResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    UserProfileResponse user = response.body();

                    // Nombre y handle
                    tvName.setText(user.fullName);
                    tvHandle.setText(user.handle);

                    // Estadísticas
                    if (user.stats != null) {
                        tvEvents.setText(String.valueOf(user.stats.eventos));
                        tvFriends.setText(String.valueOf(user.stats.amigos));
                    }

                    // Avatar
                    Glide.with(ProfileActivity.this)
                            .load(user.avatarUrl)
                            .placeholder(R.drawable.avatar_placeholder)
                            .into(imgAvatar);
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}

