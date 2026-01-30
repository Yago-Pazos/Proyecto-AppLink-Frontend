package com.example.partycoruna.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.partycoruna.R;
import com.example.partycoruna.models.Friend;

import java.util.ArrayList;
import java.util.List;

/* AmigosAdapter
 - Adapter para RecyclerView de amigos.
 */
public class AmigosAdapter extends RecyclerView.Adapter<AmigosAdapter.AmigoViewHolder> {

    private List<Friend> amigosList;
    private OnFriendActionListener listener;

    // Listener para acciones sobre un amigo (ej. eliminar)
    public interface OnFriendActionListener {
        void onDelete(Friend friend, int position);
    }

    // Constructor recibe listener (puede ser null)
    public AmigosAdapter(List<Friend> amigosList, OnFriendActionListener listener) {
        this.amigosList = amigosList != null ? amigosList : new ArrayList<>();
        this.listener = listener;
    }

    // Constructor sobrecargado para compatibilidad
    public AmigosAdapter(List<Friend> amigosList) {
        this(amigosList, null);
    }

    @NonNull
    @Override
    public AmigoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_amigo, parent, false);
        return new AmigoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AmigoViewHolder holder, int position) {
        Friend amigo = amigosList.get(position);
        holder.nameTextView.setText(amigo.getName());
        
        Glide.with(holder.itemView.getContext())
                .load(amigo.getAvatarUrl())
                .placeholder(R.drawable.ic_launcher_foreground) // Placeholder simple
                .error(R.drawable.ic_launcher_foreground)
                .circleCrop() // Asegura que sea redondo
                .into(holder.avatarImageView);

        holder.deleteButton.setOnClickListener(v -> {
            int pos = holder.getBindingAdapterPosition();
            if (pos == RecyclerView.NO_POSITION) return;
            if (listener != null) {
                listener.onDelete(amigo, pos);
            }
        });
    }

    @Override
    public int getItemCount() {
        return amigosList != null ? amigosList.size() : 0;
    }

    public static class AmigoViewHolder extends RecyclerView.ViewHolder {
        ImageView avatarImageView;
        TextView nameTextView;
        ImageButton deleteButton;

        public AmigoViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarImageView = itemView.findViewById(R.id.avatarImageView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            deleteButton = itemView.findViewById(R.id.deleteButton);
        }
    }
}
