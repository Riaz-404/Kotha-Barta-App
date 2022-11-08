package com.kuet.kothabarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.kuet.kothabarta.Model.User;
import com.kuet.kothabarta.databinding.ActivitySettingsBinding;

import org.json.JSONObject;

public class SettingsActivity extends AppCompatActivity {
    ActivitySettingsBinding binding;

    String userName, profileImage;

    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        database.getReference().child("Users").child(auth.getUid()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    User user = task.getResult().getValue(User.class);
                    user.setUid(task.getResult().getKey());
                    userName = user.getUserName();
                    profileImage = user.getProfileImage();
                    binding.tvUserName.setText(userName);
                    Glide.with(SettingsActivity.this).load(profileImage).placeholder(R.drawable.avatar3).into(binding.profileImageView);
                }
            }
        });

        binding.ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        binding.clEditMyProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
                intent.putExtra("profileImage", profileImage);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(SettingsActivity.this, R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);
                startActivity(intent, options.toBundle());
            }
        });

        binding.clChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, ChangePasswordActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(SettingsActivity.this, R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);
                startActivity(intent, options.toBundle());
            }
        });

        binding.clDeleteProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SettingsActivity.this, DeleteAccountActivity.class);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(SettingsActivity.this, R.anim.slide_in_right_to_left, R.anim.slide_out_right_to_left);
                startActivity(intent, options.toBundle());
            }
        });
    }
}