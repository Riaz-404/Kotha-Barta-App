package com.kuet.kothabarta.Adapter;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kuet.kothabarta.R;

public class UsersViewHolder extends RecyclerView.ViewHolder {
    ImageView profilePicImageView;
    TextView userNameTextView, lastMessageTextView;

    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);

        profilePicImageView = itemView.findViewById(R.id.profile_image);
        userNameTextView = itemView.findViewById(R.id.userNameTextView);
//        lastMessageTextView = itemView.findViewById(R.id.lastMessageTextView);
    }
}
