package com.kuet.kothabarta.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kuet.kothabarta.Model.Message;
import com.kuet.kothabarta.R;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter {
    ArrayList<Message> messagesArray;
    Context context;
    String receiverId;

    int SENDER_VIEW_TYPE = 1;
    int RECEIVER_VIEW_TYPE = 2;



    public ChatAdapter(ArrayList<Message> messagesArray, Context context, String receiverId) {
        this.messagesArray = messagesArray;
        this.context = context;
        this.receiverId = receiverId;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == SENDER_VIEW_TYPE){
            View view = LayoutInflater.from(context).inflate(R.layout.sample_sender_chat, parent, false);
            return new SenderViewHolder(view);
        }
        else{
            View view = LayoutInflater.from(context).inflate(R.layout.sample_receiver_chat, parent, false);
            return new ReceiverViewHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(messagesArray.get(position).getSenderId().equals(receiverId)){
            return RECEIVER_VIEW_TYPE;
        }
        else{
            return SENDER_VIEW_TYPE;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Message message = messagesArray.get(position);

        if(holder.getClass() == SenderViewHolder.class){
            ((SenderViewHolder)holder).senderMessage.setText(message.getMessage());
            ((SenderViewHolder)holder).senderTimeStamp.setText(message.getTimeStamp());
        }
        else{
            ((ReceiverViewHolder)holder).receiverMessage.setText(message.getMessage());
            ((ReceiverViewHolder)holder).receiverTimeStamp.setText(message.getTimeStamp());
        }
    }

    @Override
    public int getItemCount() {
        return messagesArray.size();
    }

    public class ReceiverViewHolder extends RecyclerView.ViewHolder {
        TextView receiverMessage, receiverTimeStamp;
        public ReceiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMessage = itemView.findViewById(R.id.receiverText);
            receiverTimeStamp = itemView.findViewById(R.id.receiverTime);
        }
    }

    public class SenderViewHolder extends RecyclerView.ViewHolder {
        TextView senderMessage, senderTimeStamp;
        public SenderViewHolder(@NonNull View itemView) {
            super(itemView);
            senderMessage = itemView.findViewById(R.id.senderText);
            senderTimeStamp = itemView.findViewById(R.id.senderTime);
        }
    }
}
