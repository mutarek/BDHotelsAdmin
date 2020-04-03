package com.techtrickbd.admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;

import com.darsh.multipleimageselect.activities.AlbumSelectActivity;
import com.darsh.multipleimageselect.helpers.Constants;
import com.darsh.multipleimageselect.models.Image;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private StorageReference storageRef;
    private FirebaseApp app;
    private FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        app = FirebaseApp.getInstance();
        storage =FirebaseStorage.getInstance(app);
    }

    public void uploadImages(View view) {
        Intent intent = new Intent(MainActivity.this, AlbumSelectActivity.class);
        intent.putExtra(Constants.INTENT_EXTRA_LIMIT, 10);
        startActivityForResult(intent, Constants.REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Constants.REQUEST_CODE && resultCode == RESULT_OK) {
            ArrayList<Image> images = data.getParcelableArrayListExtra(Constants.INTENT_EXTRA_IMAGES);
            final Uri[] uri=new Uri[images.size()];
            for (int i =0 ; i < images.size(); i++) {
                uri[i] = Uri.parse("file://"+images.get(i).path);
                storageRef = storage.getReference("hotelPhotos");
                final StorageReference ref = storageRef.child(uri[i].getLastPathSegment());
                ref.putFile(uri[i])
                        .addOnSuccessListener(this, new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> down=taskSnapshot.getStorage().getDownloadUrl();
                                Log.d("downloadurl",uri.toString());
                                /*Uri downloadUrl = taskSnapshot.getDownloadUrl();
                                String content = downloadUrl.toString();
                                if (content.length() > 0) {
                                    editWriteMessage.setText("");
                                    Message newMessage = new Message();
                                    newMessage.text = content;
                                    newMessage.idSender = StaticConfig.UID;
                                    newMessage.idReceiver = roomId;
                                    newMessage.timestamp = System.currentTimeMillis();
                                    FirebaseDatabase.getInstance().getReference().child("message/" + roomId).push().setValue(newMessage);
                                }*/
                            }
                        });

            }
    }

    }
}
