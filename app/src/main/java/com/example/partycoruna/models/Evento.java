package com.example.partycoruna.models;

import com.google.gson.annotations.SerializedName;

public class Evento {
    
    @SerializedName("eventId") // Backend sends "eventId"
    private int id;

    @SerializedName("title") // Backend sends "title"
    private String nombre;

    @SerializedName("date") // Backend sends "date"
    private String fecha;

    @SerializedName("location") // Backend sends "location"
    private String lugar;

    @SerializedName("imageUrl") // Backend sends "imageUrl"
    private String imagenUrl;

    @SerializedName("category") // Backend sends "category"
    private String categoria;

    @SerializedName("isFavorite") // Backend sends "isFavorite"
    private boolean isFavorite;

    // Constructor
    public Evento(int id, String nombre, String fecha, String lugar, String imagenUrl, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.imagenUrl = imagenUrl;
        this.categoria = categoria;
        this.isFavorite = false;
    }

    // Getters
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getFecha() { return fecha; }
    public String getLugar() { return lugar; }
    public String getImagenUrl() { return imagenUrl; }
    public String getCategoria() { return categoria; }
    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}
