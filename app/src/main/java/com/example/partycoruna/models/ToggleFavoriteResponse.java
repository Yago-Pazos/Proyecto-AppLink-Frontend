package com.example.partycoruna.models;

import com.google.gson.annotations.SerializedName;

public class ToggleFavoriteResponse {
    @SerializedName("eventId")
    public int eventId;

    @SerializedName("isFavorite")
    public boolean isFavorite;
}
