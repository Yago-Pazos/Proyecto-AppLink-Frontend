package com.example.partycoruna.models;
public class Evento {
    private int id;
    private String nombre;
    private String fecha;
    private String lugar;
    private String imagenUrl;
    private String categoria;

    // Constructor
    public Evento(int id, String nombre, String fecha, String lugar, String imagenUrl, String categoria) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.lugar = lugar;
        this.imagenUrl = imagenUrl;
        this.categoria = categoria;
    }

    // Getters (necesarios para el Adapter)
    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getFecha() { return fecha; }
    public String getLugar() { return lugar; }
    public String getImagenUrl() { return imagenUrl; }
    public String getCategoria() { return categoria; }
}
