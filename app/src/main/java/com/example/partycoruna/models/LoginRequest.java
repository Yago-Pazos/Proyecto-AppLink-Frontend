package com.example.partycoruna.models;

public class LoginRequest {
    private String email;
    private String password; // Asegúrate de que coincida con el nombre que espera tu Python

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
}