package com.example.partycoruna;

import android.os.Bundle;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;

import com.example.partycoruna.fragments.preferences.MusicFragment;

public class PreferencesActivity extends AppCompatActivity {

    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 1. Asegúrate de que este sea el nombre de tu XML de la Activity
        setContentView(R.layout.activity_preferences);

        // 2. Inicializar la barra de progreso
        progressBar = findViewById(R.id.progressBar);

        // 3. Cargar el primer fragmento (Música) al iniciar
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, new MusicFragment())
                    .commit();
        }
    }

    /**
     * Metodo público para que los fragmentos actualicen el progreso.
     * Si sale error aquí, revisa que el ID en tu XML sea "progressBar".
     */
    public void updateProgress(int progress) {
        if (progressBar != null) {
            progressBar.setProgress(progress);
        }
    }
}
