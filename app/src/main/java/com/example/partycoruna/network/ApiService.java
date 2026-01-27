package com.example.partycoruna.network;

import com.example.partycoruna.models.RegisterRequest;
import com.example.partycoruna.models.RegisterResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiService {

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @GET("users/me/favorites")
    Call<List<Evento>> getFavoritos(@Header("Authorization") String token);
}
