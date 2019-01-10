package babble.babble.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

import babble.babble.R;
import de.hdodenhof.circleimageview.CircleImageView;
import repository.UserRepo;

public class SettingActivity extends AppCompatActivity {
    private CircleImageView circleImageView;
    private EditText userNameInput;
    private EditText descriptionInput;
    private TextView updateButton;
    private FirebaseAuth mAuth;
    private UserRepo userRepo;
    private DatabaseReference databaseReference;
    private static final int galleryPick = 1;
    private StorageReference storageReference;
    private String currentUserID;
    private String image;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        //Init variables
        circleImageView = findViewById(R.id.profile_activity_icon);
        userNameInput = findViewById(R.id.profile_activity_username);
        descriptionInput = findViewById(R.id.profile_activity_description);
        updateButton = findViewById(R.id.updateButton);
        userRepo = new UserRepo(SettingActivity.this);
        mAuth = FirebaseAuth.getInstance();
        currentUserID = mAuth.getCurrentUser().getUid();
        //Stores the image
        storageReference = FirebaseStorage.getInstance().getReference().child("Profile Images");
        databaseReference = FirebaseDatabase.getInstance().getReference();
        getUserData();

        circleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, galleryPick);
            }
        });


        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((!TextUtils.isEmpty(userNameInput.getText().toString()) && !TextUtils.isEmpty(descriptionInput.getText().toString()))) {
                    updateUser();
                    sendToMain();
                } else {
                    Toast.makeText(SettingActivity.this, R.string.empty_fields, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateUser() {
        HashMap<String, String> userProfileMap = new HashMap<>();
        userProfileMap.put("uid", mAuth.getCurrentUser().getUid());
        userProfileMap.put("username", userNameInput.getText().toString());
        userProfileMap.put("description", descriptionInput.getText().toString());
        userProfileMap.put("image", image);
        userRepo.updateUser(userProfileMap);
    }

    private void sendToMain() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }

    public void getUserData() {
        databaseReference.child("Users").child(currentUserID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists() && (dataSnapshot.hasChild("username")) && (dataSnapshot.hasChild("image"))) {
                    userNameInput.setText(dataSnapshot.child("username").getValue().toString());
                    descriptionInput.setText(dataSnapshot.child("description").getValue().toString());
                    image = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(image).into(circleImageView);
                } else if (dataSnapshot.exists() && (dataSnapshot.hasChild("username"))) {
                    userNameInput.setText(dataSnapshot.child("username").getValue().toString());
                    descriptionInput.setText(dataSnapshot.child("description").getValue().toString());
                } else if (dataSnapshot.exists() && (dataSnapshot.hasChild("image"))) {
                    image = dataSnapshot.child("image").getValue().toString();
                    Picasso.get().load(image).into(circleImageView);
                } else {
                    Toast.makeText(SettingActivity.this, "Update your Profile information", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.w("tag", "Failed to read value.", databaseError.toException());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == galleryPick && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();

            CropImage.activity()
                    .setGuidelines(CropImageView.Guidelines.ON)
                    .setAspectRatio(1, 1)
                    .start(this);

        }
        //Retrieves the image chosen from Gallery
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                final Uri resultUri = result.getUri();
                final StorageReference filePath = storageReference.child(currentUserID + ".jpg");
                filePath.putFile(resultUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                final String downloadUrl = uri.toString();
                                databaseReference.child("Users").child(currentUserID).child("image").setValue(downloadUrl)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(SettingActivity.this, "Profile image stored to firebase database successfully.", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    String message = task.getException().getMessage();
                                                    Toast.makeText(SettingActivity.this, "Error Occurred..." + message, Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        });
                    }
                });
            }
        }
    }
}
