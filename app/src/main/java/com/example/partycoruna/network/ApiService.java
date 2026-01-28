package com.example.partycoruna.network;

import com.example.partycoruna.models.PreferencesRequest;
import com.example.partycoruna.models.RegisterRequest;
import com.example.partycoruna.models.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
    @POST("users/preferences")
    Call<Void> savePreferences(@Body PreferencesRequest request);
}
