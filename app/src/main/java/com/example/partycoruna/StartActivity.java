package com.example.partycoruna;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private Button btnLogin, btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 🔐 AUTO-LOGIN (Activado)
        if (com.example.partycoruna.helpers.AuthManager.isLoggedIn(this)) {
            // Ya hay sesión → ir directo a Main
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        // Si no hay token, mostramos la pantalla Start
        setContentView(R.layout.activity_start);

        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        btnLogin.setOnClickListener(v ->
                startActivity(new Intent(StartActivity.this, LoginActivity.class))
        );

        btnRegister.setOnClickListener(v ->
                startActivity(new Intent(StartActivity.this, RegisterActivity.class))
        );
    }
}
