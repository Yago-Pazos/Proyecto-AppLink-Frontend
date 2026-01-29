package com.example.partycoruna.adapters;

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

public class EventsAdapter extends RecyclerView.Adapter<EventsAdapter.ViewHolder> {

    private List<Evento> events;
    private OnItemClickListener listener;
    private OnFavoriteClickListener favListener;

    public interface OnItemClickListener {
        void onItemClick(Evento evento);
    }
    
    public interface OnFavoriteClickListener {
        void onFavoriteClick(Evento evento);
    }

    public EventsAdapter(List<Evento> events, OnItemClickListener listener, OnFavoriteClickListener favListener) {
        this.events = events;
        this.listener = listener;
        this.favListener = favListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_event_home, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Evento event = events.get(position);
        holder.bind(event, listener, favListener);
    }

    @Override
    public int getItemCount() {
        return events != null ? events.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgEvent, imgLikeIcon;
        TextView tvCategory, tvTitle, tvDate, tvLocation;
        View btnLike;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgEvent = itemView.findViewById(R.id.imgEvent);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            btnLike = itemView.findViewById(R.id.btnLike);
            
            // Assuming btnLike contains the ImageView. Let's find it.
            // In item_event_home.xml, btnLike is a FrameLayout containing an ImageView.
            // We need to access that inner ImageView to change its resource/tint.
            if (btnLike instanceof android.view.ViewGroup) {
                 imgLikeIcon = (ImageView) ((android.view.ViewGroup) btnLike).getChildAt(0);
            }
        }

        public void bind(final Evento event, final OnItemClickListener listener, final OnFavoriteClickListener favListener) {
            tvTitle.setText(event.getNombre());
            tvDate.setText(event.getFecha());
            tvLocation.setText(event.getLugar());
            
            if(event.getCategoria() != null) {
                tvCategory.setText(event.getCategoria().toUpperCase());
            } else {
                tvCategory.setText("GLOW"); 
            }

            Glide.with(itemView.getContext())
                    .load(event.getImagenUrl())
                    .centerCrop()
                    .placeholder(R.drawable.ic_launcher_background)
                    .into(imgEvent);

            itemView.setOnClickListener(v -> listener.onItemClick(event));
            
            // Favorite Logic
            updateFavoriteIcon(event.isFavorite());

            btnLike.setOnClickListener(v -> {
                favListener.onFavoriteClick(event);
                // Optimistic visual update (will be confirmed by API, but instant feedback is better)
                event.setFavorite(!event.isFavorite());
                updateFavoriteIcon(event.isFavorite());
            });
        }
        
        private void updateFavoriteIcon(boolean isFavorite) {
            if (imgLikeIcon != null && btnLike != null) {
                if (isFavorite) {
                    // Active: Blue Circle + White Filled Heart
                    btnLike.setBackgroundResource(R.drawable.circle_bg_blue);
                    imgLikeIcon.setImageResource(R.drawable.ic_heart_filled);
                    imgLikeIcon.setColorFilter(android.graphics.Color.WHITE);
                } else {
                    // Inactive: Translucent Circle + White Outline Heart
                    btnLike.setBackgroundResource(R.drawable.circle_bg_translucent);
                    imgLikeIcon.setImageResource(R.drawable.ic_heart);
                    imgLikeIcon.setColorFilter(android.graphics.Color.WHITE); 
                }
            }
        }
    }
}
