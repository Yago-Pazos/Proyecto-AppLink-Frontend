package com.example.partycoruna.models;

public class RegisterRequest {

    private String fullName;
    private String email;
    private String password;
    private String confirmPassword;

    public RegisterRequest(String fullName, String email, String password, String confirmPassword) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }
}
