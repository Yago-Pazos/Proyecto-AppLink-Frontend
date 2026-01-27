package com.example.partycoruna;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.partycoruna.helpers.AuthManager;
import com.example.partycoruna.models.RegisterRequest;
import com.example.partycoruna.models.RegisterResponse;
import com.example.partycoruna.network.ApiClient;
import com.example.partycoruna.network.ApiService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPassword, etRepeat;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        // Si el usuario ya está logueado, ir directamente a MainActivity
        if (AuthManager.isLoggedIn(this)) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            return;
        }
        
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        etRepeat = findViewById(R.id.etRepeat);
        btnRegister = findViewById(R.id.btnRegister);

        btnRegister.setOnClickListener(v -> register());
    }

    private void register() {

        String name = etName.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String password = etPassword.getText().toString().trim();
        String repeat = etRepeat.getText().toString().trim();

        if (name.isEmpty() || email.isEmpty() || password.isEmpty() || repeat.isEmpty()) {
            Toast.makeText(this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        RegisterRequest request =
                new RegisterRequest(name, email, password, repeat);

        ApiService apiService = ApiClient.getClient().create(ApiService.class);

        apiService.register(request).enqueue(new Callback<RegisterResponse>() {
            @Override
            public void onResponse(Call<RegisterResponse> call, Response<RegisterResponse> response) {

                if (response.isSuccessful() && response.body() != null) {

                    String token = response.body().getToken();
                    if (token != null && !token.isEmpty()) {
                        // Guardar el token
                        AuthManager.saveToken(RegisterActivity.this, token);
                        
                        Toast.makeText(RegisterActivity.this,
                                "Registro correcto", Toast.LENGTH_SHORT).show();
                        
                        // Navegar a MainActivity
                        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this,
                                "Error: Token no recibido", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    try {
                        String error = response.errorBody().string();
                        Toast.makeText(
                                RegisterActivity.this,
                                error,
                                Toast.LENGTH_LONG
                        ).show();
                    } catch (Exception e) {
                        Toast.makeText(
                                RegisterActivity.this,
                                "Error desconocido",
                                Toast.LENGTH_SHORT
                        ).show();
                    }
                }

            }

            @Override
            public void onFailure(Call<RegisterResponse> call, Throwable t) {
                Toast.makeText(RegisterActivity.this,
                        "Error de conexión", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
