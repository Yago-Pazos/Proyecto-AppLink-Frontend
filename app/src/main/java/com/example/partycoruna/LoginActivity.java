package com.example.partycoruna;

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

    // Declaramos la variable del binding
    private ActivityLoginBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inicializamos el binding
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Manejamos el clic del botón
        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.etEmail.getText().toString().trim();
                String password = binding.etPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Rellena todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    realizarLogin(email, password);
                }
            }
        });
    }

    private void realizarLogin(String email, String password) {
        // Creamos el objeto con los datos (Request)
        LoginRequest loginRequest = new LoginRequest(email, password);

        // Preparamos la llamada usando nuestra interfaz com.example.partycoruna.ApiService
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        Call<LoginResponse> call = apiService.loginUser(loginRequest);

        // Ejecutamos la llamada de forma asíncrona (en segundo plano)
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // ÉXITO: El backend devolvió un 200 OK
                    String token = response.body().getToken();
                    Toast.makeText(LoginActivity.this, "¡Bienvenido! Token: " + token, Toast.LENGTH_LONG).show();

                    // Aquí podrías pasar a la siguiente pantalla (Home)
                    // Intent intent = new Intent(com.example.partycoruna.LoginActivity.this, HomeActivity.class);
                    // startActivity(intent);
                } else {
                    // ERROR: El backend devolvió 401, 404, 500, etc.
                    Toast.makeText(LoginActivity.this, "Error: Usuario o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                // ERROR DE RED: No hay internet o el servidor está apagado
                Log.e("API_ERROR", t.getMessage());
                Toast.makeText(LoginActivity.this, "Error de conexión con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
        // Aquí conectaremos con Retrofit
        Toast.makeText(this, "Conectando con el servidor...", Toast.LENGTH_SHORT).show();
    }
}
