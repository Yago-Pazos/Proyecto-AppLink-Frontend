package com.example.partycoruna.viewmodels;

import androidx.lifecycle.ViewModel;
import com.example.partycoruna.models.PreferencesRequest;

public class PreferencesViewModel extends ViewModel {

    // Almacenamos selecciones únicas como String
    private String selectedMusic = "";
    private String selectedAmbiance = "";
    private String selectedAge = "";

    // Métodos para Música
    public void setMusic(String music) { this.selectedMusic = music; }
    public String getMusic() { return selectedMusic; }

    // Métodos para Ambiente
    public void setAmbiance(String ambiance) { this.selectedAmbiance = ambiance; }
    public String getAmbiance() { return selectedAmbiance; }

    // Métodos para Edad
    public void setAge(String age) { this.selectedAge = age; }
    public String getAge() { return selectedAge; }

    // Genera el objeto final para el POST /users/preferences.

    public PreferencesRequest getFinalRequest() {
        return new PreferencesRequest(selectedMusic, selectedAmbiance, selectedAge);
    }
}