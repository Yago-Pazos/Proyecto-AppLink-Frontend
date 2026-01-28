package com.example.partycoruna.ui.preferences;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

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

import java.util.Arrays;
import java.util.List;

public class AmbianceFragment extends Fragment implements OptionAdapter.OnSelectionChangeListener {

    private PreferencesViewModel viewModel;
    private OptionAdapter adapter;
    private Button btnAction;
    private boolean isItemSelected = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ambiance, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof PreferencesActivity) {
            ((PreferencesActivity) getActivity()).updateProgress(66);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(PreferencesViewModel.class);

        RecyclerView rvAmbiance = view.findViewById(R.id.rvAmbiance);
        btnAction = view.findViewById(R.id.btnNext);

        // Opciones basadas en el diseño
        List<String> ambianceOptions = Arrays.asList(
                "Discoteca", "Pub", "Bar", "Exterior",
                "Privado", "Rooftop", "Parque", "Salón de Fiestas"
        );

        adapter = new OptionAdapter(ambianceOptions, this);
        rvAmbiance.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvAmbiance.setAdapter(adapter);

        btnAction.setOnClickListener(v -> {
            if (!adapter.getSelectedItems().isEmpty()) {
                String musicElegida = adapter.getSelectedItems().get(0);
                viewModel.setMusic(musicElegida);
                navigateToAge();
            } else {
                goToHome();
            }
        });
    }

    @Override
    public void onSelectionChanged(int count) {
        if (count > 0) {
            isItemSelected = true;
            btnAction.setText("Siguiente");
            btnAction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#00A3FF")));
        } else {
            isItemSelected = false;
            btnAction.setText("Saltar");
            btnAction.setBackgroundTintList(ColorStateList.valueOf(Color.parseColor("#1A1A1A")));
        }
    }

    private void navigateToAge() {
        // Actualizar barra de progreso al 100% (o al valor que prefieras para el paso final)
        if (getActivity() instanceof PreferencesActivity) {
            ((PreferencesActivity) getActivity()).updateProgress(100);
        }

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AgeFragment())
                .addToBackStack(null)
                .commit();
    }

    private void goToHome() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        if (getActivity() != null) getActivity().finish();
    }
}
