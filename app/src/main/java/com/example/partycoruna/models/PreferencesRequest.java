package com.example.partycoruna.models;

import com.google.gson.annotations.SerializedName;

public class PreferencesRequest {
    @SerializedName("music")
    private String music;

    @SerializedName("ambiance")
    private String ambiance;

    @SerializedName("age")
    private String age;

    // Constructor
    public PreferencesRequest(String music, String ambiance, String age) {
        this.music = music;
        this.ambiance = ambiance;
        this.age = age;
    }

    // Getters y Setters
    public String getMusic() { return music; }
    public void setMusic(String music) { this.music = music; }

    public String getAmbiance() { return ambiance; }
    public void setAmbiance(String ambiance) { this.ambiance = ambiance; }

    public String getAge() { return age; }
    public void setAge(String age) { this.age = age; }
}
