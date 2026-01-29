package com.example.partycoruna;

import android.app.Application;
import com.example.partycoruna.network.ApiClient;

public class PartyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Inicializar ApiClient con el contexto de la aplicación
        ApiClient.init(this);
    }
}
