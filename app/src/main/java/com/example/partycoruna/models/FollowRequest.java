package com.example.partycoruna.models;

import com.google.gson.annotations.SerializedName;

public class FollowRequest {
    @SerializedName("targetUserId")
    private String userId;

    public FollowRequest(int id) {
        this.userId = "u_" + id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
