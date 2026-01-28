package com.example.partycoruna.network;


import com.example.partycoruna.models.PreferencesRequest;
import com.example.partycoruna.models.LoginRequest;
import com.example.partycoruna.models.LoginResponse;

import com.example.partycoruna.models.RegisterRequest;
import com.example.partycoruna.models.RegisterResponse;
import com.example.partycoruna.models.UserProfileResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;

/* ApiService
 - Define endpoints Retrofit (POST auth/register, GET auth/me).
 - Ejemplo: ApiService api = ApiClient.getClient().create(ApiService.class);
   Call<RegisterResponse> c = api.register(req);
 */
public interface ApiService {

    @POST("auth/register")
    Call<RegisterResponse> register(@Body RegisterRequest request);

    @POST("users/preferences")
    Call<Void> savePreferences(@Body PreferencesRequest request);

    @POST("auth/login")
    Call<LoginResponse> loginUser(@Body LoginRequest request);

    @GET("users/me")
    Call<UserProfileResponse> getMyProfile();

}
