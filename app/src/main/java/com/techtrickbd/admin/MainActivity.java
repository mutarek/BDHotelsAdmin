package com.techtrickbd.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {


    private ImageView selectImage;
    private TextView selectiontext;
    private StorageReference storageRef;
    private FirebaseApp app;
    private FirebaseStorage storage;
    ArrayList<Uri> imagelist = new ArrayList<Uri>();
    private Uri imageuri;
    private FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private String currentusr = firebaseAuth.getCurrentUser().getUid();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference Hotels = db.collection(currentusr);
    private static final int PICK_IMAGE = 71;
    private int uploadCount = 0;
    private ProgressDialog progressDialog;
    private StorageReference hotels = FirebaseStorage.getInstance().getReference("hotelsImages");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = FirebaseApp.getInstance();
        storage = FirebaseStorage.getInstance(app);
        selectImage = findViewById(R.id.select_image);
        selectiontext = findViewById(R.id.selection_text);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading");
        selectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImages(v);
            }
        });
    }

    public void uploadImages(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int totalimage = data.getClipData().getItemCount();

                int currentImageSelected = 0;
                while (currentImageSelected < totalimage) {
                    imageuri = data.getClipData().getItemAt(currentImageSelected).getUri();
                    imagelist.add(imageuri);
                    currentImageSelected = currentImageSelected + 1;
                }

                selectImage.setVisibility(View.GONE);
                selectiontext.setVisibility(View.VISIBLE);
                selectiontext.setText("Selected " + "" + imagelist.size() + " " + "Images");
            } else {
                Toast.makeText(this, "Please select more than one image", Toast.LENGTH_SHORT).show();
            }

        }

    }

    public void UploadImages(View view) {
        progressDialog.show();
        for (uploadCount = 0; uploadCount < imagelist.size(); uploadCount++) {
            Uri eachImage = imagelist.get(uploadCount);
            final StorageReference ImageName = hotels.child("Image" + eachImage.getLastPathSegment());
            /*ImageName.putFile(eachImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    ImageName.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Toast.makeText(MainActivity.this, "success", Toast.LENGTH_SHORT).show();
                            String url = uri.toString();
                            uploadtodb(url);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });*/
            final StorageReference ref = ImageName.child("images/" + UUID.randomUUID().toString());
            ref.putFile(eachImage)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = String.valueOf(uri);
                                    uploadtodb(url);
                                }
                            });
                            /*progressDialog.dismiss();
                            Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!urlTask.isSuccessful()) ;
                            result = urlTask.getResult();
                            completeProfile();*/
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }


    private void uploadtodb(String url) {
        Hotels.add(url).addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
            @Override
            public void onComplete(@NonNull Task<DocumentReference> task) {
                if (task.isSuccessful()) {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
