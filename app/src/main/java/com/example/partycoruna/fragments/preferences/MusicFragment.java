package com.example.partycoruna.fragments.preferences;

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
import com.example.partycoruna.PreferencesActivity;
import com.example.partycoruna.viewmodels.PreferencesViewModel;

import java.util.Arrays;
import java.util.List;

public class MusicFragment extends Fragment implements OptionAdapter.OnSelectionChangeListener {

    private PreferencesViewModel viewModel;
    private OptionAdapter adapter;
    private Button btnAction;
    private boolean isItemSelected = false;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_music, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getActivity() instanceof PreferencesActivity) {
            ((PreferencesActivity) getActivity()).updateProgress(33);
        }

        viewModel = new ViewModelProvider(requireActivity()).get(PreferencesViewModel.class);

        RecyclerView rvMusic = view.findViewById(R.id.rvMusic);
        btnAction = view.findViewById(R.id.btnNext);

        List<String> musicOptions = Arrays.asList(
                "Reggaeton", "Techno", "Rock", "Pop",
                "Salsa", "Hip Hop", "Indie", "Jazz", "Infantil"
        );

        adapter = new OptionAdapter(musicOptions, this);
        rvMusic.setLayoutManager(new GridLayoutManager(getContext(), 2));
        rvMusic.setAdapter(adapter);

        btnAction.setOnClickListener(v -> {
            if (!adapter.getSelectedItems().isEmpty()) {
                String musicElegida = adapter.getSelectedItems().get(0);
                viewModel.setMusic(musicElegida);
                navigateToAmbiance();
            } else {
                goToHome();
            }
        });
    }

    @Override
    public void onSelectionChanged(int count) {
        // count solo será 0 o 1 ahora
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

    private void navigateToAmbiance() {
        if (getActivity() instanceof PreferencesActivity) {
            ((PreferencesActivity) getActivity()).updateProgress(66);
        }

        getParentFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new AmbianceFragment())
                .addToBackStack(null)
                .commit();
    }

    private void goToHome() {
        Intent intent = new Intent(getActivity(), MainActivity.class);
        startActivity(intent);
        if (getActivity() != null) getActivity().finish();
    }
}