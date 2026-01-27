package com.example.partycoruna.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.partycoruna.ProfileActivity;
import com.example.partycoruna.R;
import com.example.partycoruna.helpers.AuthManager;
import com.example.partycoruna.models.UserProfileResponse;
import com.example.partycoruna.network.ApiClient;
import com.example.partycoruna.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PerfilFragment extends Fragment {
    private TextView tvName;
    private TextView tvHandle;
    private TextView tvEvents;
    private TextView tvFriends;
    private ImageView imgAvatar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_perfil, container, false);

        tvName = view.findViewById(R.id.tvName);
        tvHandle = view.findViewById(R.id.tvHandle);
        tvEvents = view.findViewById(R.id.tvEvents);
        tvFriends = view.findViewById(R.id.tvFriends);
        imgAvatar = view.findViewById(R.id.imgAvatar);

        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadUserProfile();
    }

    private void loadUserProfile() {

        // Obtener token guardado
        String token = AuthManager.getToken(requireActivity());

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
                    /*Glide.with(ProfileActivity.this)
                            .load(user.avatarUrl)
                            .placeholder(R.drawable.avatar_placeholder)
                            .into(imgAvatar);*/
                }
            }

            @Override
            public void onFailure(Call<UserProfileResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}



