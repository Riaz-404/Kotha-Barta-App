package com.kuet.kothabarta;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ActivityOptions;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kuet.kothabarta.Adapter.ChatAdapter;
import com.kuet.kothabarta.Model.Message;
import com.kuet.kothabarta.Model.User;
import com.kuet.kothabarta.databinding.ActivityChatBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Map;

public class ChatActivity extends AppCompatActivity {
    ActivityChatBinding binding;

    String senderId, receiverId, receiverName, receiverProfileImage;
    ArrayList<Message> messagesArray;

    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        senderId = auth.getUid();
        receiverId = getIntent().getStringExtra("uid");
        receiverName = getIntent().getStringExtra("userName");
        receiverProfileImage = getIntent().getStringExtra("profileImage");

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        binding.chatMessageRecyclerView.setLayoutManager(linearLayoutManager);

        messagesArray = new ArrayList<>();
        ChatAdapter chatAdapter = new ChatAdapter(messagesArray, this, receiverId);
        binding.chatMessageRecyclerView.setAdapter(chatAdapter);

        binding.backArrowImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChatActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(ChatActivity.this, R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
                startActivity(intent, options.toBundle());
            }
        });

        Glide.with(this).load(receiverProfileImage).placeholder(R.drawable.avatar).into(binding.profileImageView);

        binding.userNameTextView.setText(receiverName);


        final String senderRoom = senderId + receiverId;
        final String receiverRoom = receiverId + senderId;

        database.getReference().child("Chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messagesArray.clear();

                for(DataSnapshot snapshot1: snapshot.getChildren()){
                    Message model = snapshot1.getValue(Message.class);
                    model.setId(snapshot1.getKey());
                    messagesArray.add(model);
                }
                chatAdapter.notifyDataSetChanged();
                binding.chatMessageRecyclerView.scrollToPosition(messagesArray.size()-1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = binding.enterMessageEditText.getText().toString();
                String timeStamp = new SimpleDateFormat("hh:mm a", Locale.getDefault()).format(new Date());
                final Message model = new Message(senderId, message, timeStamp);

                database.getReference().child("Chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        database.getReference().child("Chats").child(receiverRoom).push().setValue(model);
                        User user = new User();
                        user.setLastMessage(message);
                        Map<String, Object> lastMessage = user.toMap();
                        database.getReference().child("Users").child(senderId).updateChildren(lastMessage, new DatabaseReference.CompletionListener() {
                            @Override
                            public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                                database.getReference().child("Users").child(receiverId).updateChildren(lastMessage);
                            }
                        });
                        binding.enterMessageEditText.setText("");
                    }
                });
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
    }

}

