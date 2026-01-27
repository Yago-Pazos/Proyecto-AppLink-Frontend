package com.example.partycoruna.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.example.partycoruna.R;
import com.example.partycoruna.models.Evento;
import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.ViewHolder> {

    private List<Evento> eventos;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(int position);
    }

    public FavoritosAdapter(List<Evento> eventos, OnItemClickListener listener) {
        this.eventos = eventos;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_favorite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Evento ev = eventos.get(position);
        holder.tvTitle.setText(ev.getNombre());
        holder.tvCategory.setText(ev.getCategoria());
        holder.tvDetails.setText(ev.getFecha() + " • " + ev.getLugar());

        Glide.with(holder.itemView.getContext())
                .load(ev.getImagenUrl())
                .placeholder(R.drawable.ic_launcher_background)
                .into(holder.ivEvent);

        holder.btnDelete.setOnClickListener(v -> listener.onDeleteClick(position));
    }

    @Override
    public int getItemCount() { return eventos.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvCategory, tvDetails;
        ImageView ivEvent, btnDelete;
        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvEventTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvDetails = itemView.findViewById(R.id.tvEventDetails);
            ivEvent = itemView.findViewById(R.id.ivEventImage);
            btnDelete = itemView.findViewById(R.id.btnRemoveFavorite);
        }
    }
}
