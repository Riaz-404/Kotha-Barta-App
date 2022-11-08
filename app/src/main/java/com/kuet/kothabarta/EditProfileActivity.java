package com.kuet.kothabarta;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityOptions;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kuet.kothabarta.databinding.ActivityEditProfileBinding;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {
    ActivityEditProfileBinding binding;

    String profileImage;

    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    Uri profileImageUri;

    ProgressDialog progressDialog;

    ActivityResultLauncher<String> mGetContent = registerForActivityResult(new ActivityResultContracts.GetContent(),
            new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri uri) {
                    profileImageUri = uri;
                    binding.profileImageView.setImageURI(profileImageUri);
                }
            });


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            mGetContent.launch("image/*");
        }
        else{
            Toast.makeText(this, "Permission required", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        profileImage = getIntent().getStringExtra("profileImage");
        profileImageUri = null;

        Glide.with(EditProfileActivity.this).load(profileImage).placeholder(R.drawable.avatar3).into(binding.profileImageView);

        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Please wait..");
        progressDialog.setMessage("Updating profile...");

        binding.tvChangeProfileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(ActivityCompat.checkSelfPermission(EditProfileActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(EditProfileActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 101);
                }
                else{
                    mGetContent.launch("image/*");
                }
            }
        });

        binding.ivBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditProfileActivity.this, SettingsActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP| Intent.FLAG_ACTIVITY_NEW_TASK);
                ActivityOptions options = ActivityOptions.makeCustomAnimation(EditProfileActivity.this, R.anim.slide_in_left_to_right, R.anim.slide_out_left_to_right);
                startActivity(intent, options.toBundle());
            }
        });

        binding.updateProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();
                String newEmail = binding.etEmail.getText().toString();
                String newUserName = binding.etUserName.getText().toString();

                if(newEmail.matches("") && newUserName.matches("") && profileImageUri == null){
                    progressDialog.cancel();
                    Toast.makeText(EditProfileActivity.this, "No Change Available", Toast.LENGTH_SHORT).show();
                }
                else {
                    StorageReference reference = storage.getReference().child("Profile Image").child(auth.getUid());

                    reference.putFile(profileImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    Map<String, Object> postValues = new HashMap<>();
                                    if (!newEmail.matches("")) {
                                        postValues.put("userName", newEmail);
                                    }
                                    if (!newUserName.matches("")) {
                                        String finalNewUserName = newUserName.substring(0, 1).toUpperCase(Locale.ROOT) + newUserName.substring(1).toLowerCase(Locale.ROOT);
                                        postValues.put("email", newEmail);
                                    }

                                    postValues.put("profileImage", uri.toString());


                                    database.getReference().child("Users").child(auth.getUid()).updateChildren(postValues).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void unused) {
                                            progressDialog.cancel();
                                            Toast.makeText(EditProfileActivity.this, "Update Successful", Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });
    }

}