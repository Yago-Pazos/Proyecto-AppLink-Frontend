package com.example.partycoruna.models;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.util.ArrayList;

public class UserSearchResponse {
    
    @SerializedName("users")
    private List<Friend> users;

    @SerializedName("results")
    private List<Friend> results;

    @SerializedName("data")
    private List<Friend> data;

    public List<Friend> getUsers() {
        if (users != null) return users;
        if (results != null) return results;
        if (data != null) return data;
        return new ArrayList<>();
    }
}
