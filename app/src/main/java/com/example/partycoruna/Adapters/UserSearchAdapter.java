package com.example.partycoruna.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.partycoruna.R;
import com.example.partycoruna.models.Friend;

import java.util.ArrayList;
import java.util.List;

public class UserSearchAdapter extends RecyclerView.Adapter<UserSearchAdapter.UserViewHolder> {

    private List<Friend> userList;
    private OnUserActionListener listener;

    public interface OnUserActionListener {
        void onFollow(Friend user, Button btnFollow);
    }

    public UserSearchAdapter(List<Friend> userList, OnUserActionListener listener) {
        this.userList = userList != null ? userList : new ArrayList<>();
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_search, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        Friend user = userList.get(position);
        holder.name.setText(user.getName());
        
        Glide.with(holder.itemView.getContext())
                .load(user.getAvatarUrl())
                .placeholder(R.drawable.ic_avatar_placeholder)
                .error(R.drawable.ic_avatar_placeholder)
                .circleCrop()
                .into(holder.avatar);

        holder.btnFollow.setOnClickListener(v -> {
            if (listener != null) listener.onFollow(user, holder.btnFollow);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        ImageView avatar;
        TextView name;
        Button btnFollow;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            avatar = itemView.findViewById(R.id.imgAvatar);
            name = itemView.findViewById(R.id.tvName);
            btnFollow = itemView.findViewById(R.id.btnFollow);
        }
    }
}
