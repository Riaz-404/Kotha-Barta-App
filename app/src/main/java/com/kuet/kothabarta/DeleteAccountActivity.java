package com.kuet.kothabarta;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import com.kuet.kothabarta.R;
import com.kuet.kothabarta.databinding.ActivityDeleteAccountBinding;

public class DeleteAccountActivity extends AppCompatActivity {
    ActivityDeleteAccountBinding binding;

    FirebaseAuth auth;
    FirebaseDatabase database;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDeleteAccountBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        auth = FirebaseAuth.getInstance();
        database= FirebaseDatabase.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Deleting account...");

        binding.ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DeleteAccountActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(DeleteAccountActivity.this, R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
                startActivity(intent, options.toBundle());
            }
        });

        binding.updatePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password = binding.etPassword.getText().toString();
                String confirmPassword = binding.etConfirmPassword.getText().toString();

                if(password.matches("") || confirmPassword.matches("")){
                    Toast.makeText(DeleteAccountActivity.this, "Password field empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    if(password.equals(confirmPassword)){
                        new AlertDialog.Builder(DeleteAccountActivity.this)
                                .setTitle("Delete account")
                                .setMessage("Are you sure you want to delete the account?")
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        progressDialog.show();
                                        auth.signInWithEmailAndPassword(auth.getCurrentUser().getEmail(), password)
                                                .addOnCompleteListener(DeleteAccountActivity.this, new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {
                                                            database.getReference().child("Users").child(auth.getUid()).removeValue();

                                                            auth.getCurrentUser().delete()
                                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                                        @Override
                                                                        public void onComplete(@NonNull Task<Void> task) {
                                                                            if (task.isSuccessful()) {
                                                                                progressDialog.cancel();
                                                                                auth.signOut();
                                                                                Toast.makeText(DeleteAccountActivity.this, "Account Delete Successful",
                                                                                        Toast.LENGTH_SHORT).show();

                                                                                Intent intent = new Intent(DeleteAccountActivity.this, LogInActivity.class);
                                                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                                                startActivity(intent);
                                                                            }
                                                                        }
                                                                    });

                                                        } else {
                                                            progressDialog.cancel();
                                                            Toast.makeText(DeleteAccountActivity.this, "Authentication failed.",
                                                                    Toast.LENGTH_SHORT).show();
                                                        }
                                                    }
                                                });
                                    }
                                })
                                .setNegativeButton("No", null)
                                .setIcon(R.drawable.red_alert)
                                .show();
                    }
                    else{
                        Toast.makeText(DeleteAccountActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
}