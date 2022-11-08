package com.kuet.kothabarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.kuet.kothabarta.databinding.ActivityChangePasswordBinding;

public class ChangePasswordActivity extends AppCompatActivity {
    ActivityChangePasswordBinding binding;

    FirebaseAuth auth;
    FirebaseUser currentUser;

    FirebaseDatabase database;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Updating password...");

        binding.ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChangePasswordActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(ChangePasswordActivity.this, R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
                startActivity(intent, options.toBundle());
            }
        });

        binding.updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String oldPassword = binding.etOldPassword.getText().toString();
                String newPassword = binding.etNewPassword.getText().toString();
                String confirmNewPassword = binding.etConfirmPassword.getText().toString();

                currentUser = auth.getCurrentUser();

                if(!newPassword.equals(confirmNewPassword)){
                    Toast.makeText(ChangePasswordActivity.this, "New password not matched", Toast.LENGTH_SHORT).show();
                }
                else{
                    String email = currentUser.getEmail();

                    progressDialog.show();
                    auth.signInWithEmailAndPassword(email, oldPassword)
                            .addOnCompleteListener(ChangePasswordActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        currentUser = auth.getCurrentUser();

                                        currentUser.updatePassword(newPassword)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()) {
                                                            database.getReference().child("Users").child(currentUser.getUid()).child("password").setValue(newPassword).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void unused) {
                                                                    auth.signOut();
                                                                    progressDialog.cancel();

                                                                    Toast.makeText(ChangePasswordActivity.this, "Password Updated. Please Log In again...", Toast.LENGTH_SHORT).show();

                                                                    Intent intent = new Intent(ChangePasswordActivity.this, LogInActivity.class);
                                                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                                                    startActivity(intent);
                                                                }
                                                            });
                                                        }
                                                    }
                                                });

                                    } else {
                                        progressDialog.cancel();
                                        Toast.makeText(ChangePasswordActivity.this, "Authentication failed.",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}