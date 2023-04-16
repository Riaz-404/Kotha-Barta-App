package com.kuet.kothabarta.Adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kuet.kothabarta.R;

import java.util.ArrayList;

public class StatusAdapter extends RecyclerView.Adapter<StatusAdapter.StatusViewHolder> {
    ArrayList<String> status;
    Context context;

    public StatusAdapter(ArrayList<String> status, Context context) {
        this.status = status;
        this.context = context;
    }

    @NonNull
    @Override
    public StatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull StatusViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class StatusViewHolder extends RecyclerView.ViewHolder {
        ImageView statusImageView;
        TextView userName, timeStamp;

        public StatusViewHolder(@NonNull View itemView) {
            super(itemView);

            statusImageView = itemView.findViewById(com.kuet.kothabarta.R.id.status_image);
            userName = itemView.findViewById(R.id.userNameTextView);
            timeStamp = itemView.findViewById(R.id.timeStampTextView);
        }
    }
}
