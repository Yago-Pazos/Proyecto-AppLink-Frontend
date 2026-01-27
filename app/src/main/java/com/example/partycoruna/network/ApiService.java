package com.example.partycoruna.network;

import com.example.partycoruna.models.Friend;
import com.example.partycoruna.models.RegisterRequest;
import com.example.partycoruna.models.RegisterResponse;
import com.example.partycoruna.models.UserProfileResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

/* ApiService
 - Define endpoints Retrofit.
 - IMPORTANTE: los paths son case-sensitive en la mayoría de backends.
 */
public interface ApiService {

  @POST("auth/register")
  Call<RegisterResponse> register(@Body RegisterRequest request);

  @GET("users/me")
  Call<UserProfileResponse> getMyProfile(@Header("Authorization") String token);

  @GET("users/me/friends")
  Call<List<Friend>> getMyFriends(@Header("Authorization") String token);

  @DELETE("users/me/friends/{id}")
  Call<Void> deleteFriend(@Header("Authorization") String token, @Path("id") int friendId);
}
