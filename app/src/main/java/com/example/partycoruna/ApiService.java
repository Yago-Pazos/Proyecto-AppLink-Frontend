package com.example.partycoruna;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface ApiService {
    @POST("auth/login") // Tu ruta de backend
    Call<LoginResponse> loginUser(@Body LoginRequest request);
}
