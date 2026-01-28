package com.example.partycoruna.ui.preferences;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycoruna.MainActivity;
import com.example.partycoruna.R;
import com.example.partycoruna.adapters.OptionAdapter;
import com.example.partycoruna.viewmodels.PreferencesViewModel;
import com.example.partycoruna.network.ApiClient;
import com.example.partycoruna.network.ApiService;

import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AgeFragment extends Fragment implements OptionAdapter.OnSelectionChangeListener {

    private PreferencesViewModel viewModel;
    private OptionAdapter adapter;
    private Button btnSave;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_age, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Barra de progreso al 100%
        if (getActivity() instanceof PreferencesActivity) {
            ((PreferencesActivity) getActivity()).updateProgress(100);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(PreferencesViewModel.class);
        btnSave = view.findViewById(R.id.btnSave);
        RecyclerView rvAge = view.findViewById(R.id.rvAge);

        // Opciones basadas en el diseño
        List<String> ageOptions = Arrays.asList("Infantil", "14-17", "18-21", "22-25", "26-30", "30+");

        adapter = new OptionAdapter(ageOptions, this);
        rvAge.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvAge.setAdapter(adapter);

        btnSave.setOnClickListener(v -> saveAllPreferences());
    }

    @Override
    public void onSelectionChanged(int count) {
        // En el último paso, el botón siempre dice "Guardar"
        // pero le damos un poco de brillo si hay algo seleccionado
        if (count > 0) {
            btnSave.setAlpha(1.0f);
        } else {
            btnSave.setAlpha(0.5f);
        }
    }

    private void saveAllPreferences() {
        // Guardar la edad en el ViewModel
        if (!adapter.getSelectedItems().isEmpty()) {
            viewModel.setAge(adapter.getSelectedItems().get(0));
        }

        // Llamada a la API
        ApiService apiService = ApiClient.getClient().create(ApiService.class);
        apiService.savePreferences(viewModel.getFinalRequest()).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Toast.makeText(getContext(), "Preferencias guardadas", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getActivity(), MainActivity.class);
                    startActivity(intent);
                    getActivity().finish();
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Toast.makeText(getContext(), "Error al conectar con el servidor", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
