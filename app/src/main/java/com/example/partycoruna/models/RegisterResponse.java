package com.example.partycoruna.models;

/* RegisterResponse
 - Modelo que representa la respuesta del servidor tras registrar.
 - Getters: getMessage(), getToken(), getUserId().
 - Uso: String token = response.body().getToken();
 */
public class RegisterResponse {

    private String message;
    private String token;
    private String userId;

    public String getMessage() {
        return message;
    }

    public String getToken() {
        return token;
    }

    public String getUserId() {
        return userId;
    }
}
