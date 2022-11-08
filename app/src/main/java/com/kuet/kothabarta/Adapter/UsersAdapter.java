package com.kuet.kothabarta.Adapter;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.kuet.kothabarta.ChatActivity;
import com.kuet.kothabarta.Model.User;
import com.kuet.kothabarta.R;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersViewHolder> {
    ArrayList<User> users;
    Context context;

    public UsersAdapter(ArrayList<User> users, Context context) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_show_users, parent, false);
        return new UsersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        User user = users.get(position);

        Glide.with(context).load(user.getProfileImage()).placeholder(R.drawable.avatar3).into(holder.profilePicImageView);
        holder.userNameTextView.setText(user.getUserName());
//        if(user.getLastMessage()!=null) {
//            holder.lastMessageTextView.setText(user.getLastMessage());
//        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, ChatActivity.class);
                intent.putExtra("uid", user.getUid());
                intent.putExtra("userName", user.getUserName());
                intent.putExtra("profileImage", user.getProfileImage());
                ActivityOptions options = ActivityOptions.makeCustomAnimation(context, R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);
                context.startActivity(intent, options.toBundle());
            }
        });
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
