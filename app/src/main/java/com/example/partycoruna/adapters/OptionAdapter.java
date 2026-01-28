package com.example.partycoruna.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.partycoruna.R;

import java.util.ArrayList;
import java.util.List;

public class OptionAdapter extends RecyclerView.Adapter<OptionAdapter.ViewHolder> {

    private final List<String> options;
    private final List<String> selectedItems = new ArrayList<>();
    private final OnSelectionChangeListener listener;

    // Interfaz para comunicar cambios al Fragment
    public interface OnSelectionChangeListener {
        void onSelectionChanged(int count);
    }

    public OptionAdapter(List<String> options, OnSelectionChangeListener listener) {
        this.options = options;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_option, parent, false);
        return new ViewHolder(view);
    }
    // Dentro de OptionAdapter.java, modifica el método onBindViewHolder y añade una variable:
    private int lastSelectedPosition = -1;

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String option = options.get(position);
        holder.tvOptionName.setText(option);

        // Verificamos si esta posición es la seleccionada
        boolean isSelected = (position == lastSelectedPosition);
        updateUI(holder, isSelected);

        holder.itemView.setOnClickListener(v -> {
            // Usamos holder.getBindingAdapterPosition()
            // para evitar el error de "variable position"
            int currentPos = holder.getBindingAdapterPosition();

            if (currentPos == RecyclerView.NO_POSITION) return;

            int previousPosition = lastSelectedPosition;

            if (lastSelectedPosition == currentPos) {
                // Deseleccionar si toca el mismo
                lastSelectedPosition = -1;
                selectedItems.clear();
            } else {
                // Seleccionar el nuevo y limpiar el anterior
                lastSelectedPosition = currentPos;
                selectedItems.clear();
                selectedItems.add(options.get(currentPos));
            }

            // Refrescar los elementos necesarios para el cambio visual
            notifyItemChanged(previousPosition);
            notifyItemChanged(lastSelectedPosition);

            if (listener != null) {
                listener.onSelectionChanged(selectedItems.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return options.size();
    }

    // Método para obtener la lista final de selecciones desde el Fragment
    public List<String> getSelectedItems() {
        return selectedItems;
    }

    private void updateUI(ViewHolder holder, boolean isSelected) {
        if (isSelected) {
            holder.itemView.setBackgroundResource(R.drawable.bg_option_selected);
        } else {
            holder.itemView.setBackgroundResource(R.drawable.bg_option_default);
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvOptionName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOptionName = itemView.findViewById(R.id.tvOptionName);
        }
    }
}
