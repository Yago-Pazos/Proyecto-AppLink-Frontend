package com.example.partycoruna;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.partycoruna.databinding.ActivityLoginBinding;
import com.example.partycoruna.models.LoginRequest;
import com.example.partycoruna.models.LoginResponse;
import com.example.partycoruna.network.ApiClient;
import com.example.partycoruna.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.btnLogin.setOnClickListener(v -> {
            String email = binding.etEmail.getText().toString().trim();
            String password = binding.etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            } else {
                realizarLogin(email, password);
            }
        });

        // Ir a Register
        String text = "¿No tienes cuenta? Regístrate";
        android.text.SpannableString ss = new android.text.SpannableString(text);
        int start = text.indexOf("Regístrate");
        int end = start + "Regístrate".length();

        // Color azul
        ss.setSpan(new android.text.style.ForegroundColorSpan(getResources().getColor(R.color.party_light_blue)), start, end, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        // Negrita
        ss.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), start, end, android.text.Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        binding.tvRegister.setText(ss);
        binding.tvRegister.setOnClickListener(v ->
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class))
        );

        // Botón volver
        if (binding.btnBack != null) {
            binding.btnBack.setOnClickListener(v -> finish());
        }
    }

    private void realizarLogin(String email, String password) {
        LoginRequest loginRequest = new LoginRequest(email, password);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginUser(loginRequest);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {

                    String token = response.body().getToken();

                    // 💾 Guardar token usando AuthManager
                    com.example.partycoruna.helpers.AuthManager.saveToken(LoginActivity.this, token);

                    // 🚀 Ir a MainActivity (Limpiando pila de actividades)
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();

                } else {
                    Toast.makeText(LoginActivity.this,
                            "Usuario o contraseña incorrectos",
                            Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LOGIN_ERROR", t.getMessage());
                Toast.makeText(LoginActivity.this,
                        "Error de conexión con el servidor",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
}
