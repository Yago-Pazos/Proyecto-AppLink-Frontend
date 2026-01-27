package com.example.partycoruna.models;

/* UserProfileResponse
 - Modelo para `auth/me`: fullName, handle, avatarUrl, stats.{eventos,amigos}
 */
public class UserProfileResponse {

    public String id;
    public String fullName;
    public String handle;
    public String avatarUrl;
    public Stats stats;

    public static class Stats {
        public int eventos;
        public int amigos;
    }
}

