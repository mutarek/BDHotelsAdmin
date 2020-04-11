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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
    private EditText hotelname,hoteldesc;
    private RadioGroup roomleftgroup,internetgroup,petgroup,parkinggroup,couplegroup,shuttlegroup,lunchgroup,shopgroup,bridalgroup
            ,sharedlouge,liftgroup,airfortsuttlegrop,roomsservicegroup,airconditiongroup,viproomgroup,newspapergroup,carhiregroup,
    ownresturengroup,smokenggroup,frontdeskgroup,atmgroup,exchangegroup,lockersgroup,luggagegroup,tourdeskgroup,fitnessgroup,kidsclubgroup,
    tabletennisgroup,poolgroup,sunumbrellagroup,checkoutgroup;
    private RadioButton room1,room0,internet1,internet0,pet1,pet0,parking0,parking1,couple0,couple1,shuttle0,shuttle1,lunch0,lunch1
            ,shop1,shop0,bridal0,bridal1,sharedlounge0,sharedlounge1,lift1,lift0,airfort1,airfort0,roomservice1,roomservice0,ac0,ac1
            ,viproom1,viproom0,newspaper0,newspaper1,carhire0,carhire1,rest0,rest1,smoke0,smoke1,frontdesk0,front1,atm0,atm1,exchange0,exchange1
            ,lockers0,locker1,luggage0,luiggage1,tourdesk0,tourdeesk1,fitness0,fitness1,kidsclub0,kidsclub1,tabletennis0,tabletennis1
            ,pool0,pool1,sunumbrella0,sunumbrellla1,checkout1,checkout2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        casting();
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

    private void casting() {
        checkoutgroup=findViewById(R.id.checkoutgroup);
        checkout1=findViewById(R.id.check1);
        checkout2=findViewById(R.id.check2);
        sunumbrellagroup=findViewById(R.id.sunumbrellagroup);
        sunumbrella0=findViewById(R.id.sunumbrellagroup0);
        sunumbrellla1=findViewById(R.id.sunumbrellagroup1);
        tabletennisgroup=findViewById(R.id.tabletennisgroup);
        tabletennis0=findViewById(R.id.tebletennis0);
        tabletennis1=findViewById(R.id.tabletennis1);
        kidsclubgroup=findViewById(R.id.kidsclubgroup);
        kidsclub0=findViewById(R.id.kidsclub0);
        kidsclub1=findViewById(R.id.kidsclub1);
        fitnessgroup=findViewById(R.id.fitnessgroup);
        fitness0=findViewById(R.id.fitness0);
        fitness1=findViewById(R.id.fitness1);
        tourdeskgroup=findViewById(R.id.tourdeskgroup);
        tourdeesk1=findViewById(R.id.tourdesk1);
        tourdesk0=findViewById(R.id.tourdesk0);
        luggagegroup =findViewById(R.id.luggagegroup);
        luggage0 = findViewById(R.id.luggage0);
        luiggage1=findViewById(R.id.luggage1);
        lockersgroup=findViewById(R.id.lockersgroup);
        locker1=findViewById(R.id.locker1);
        lockers0=findViewById(R.id.lockers0);
        exchangegroup=findViewById(R.id.exchangegroup);
        exchange0=findViewById(R.id.exchange0);
        exchange1=findViewById(R.id.exchange1);
        atmgroup=findViewById(R.id.atmgroup);
        atm0=findViewById(R.id.atm0);
        atm1=findViewById(R.id.atm1);
        frontdeskgroup=findViewById(R.id.frontdeskgroup);
        front1=findViewById(R.id.frontdesk1);
        frontdesk0=findViewById(R.id.frontdesk0);
        smokenggroup=findViewById(R.id.smokinggroup);
        smoke0=findViewById(R.id.smoking0);
        smoke1=findViewById(R.id.smoking1);
        ownresturengroup = findViewById(R.id.ownrestrantgroup);
        rest0 = findViewById(R.id.ownresturant0);
        rest1=findViewById(R.id.ownresturant1);
        carhiregroup = findViewById(R.id.car_hiregroup);
        carhire0 = findViewById(R.id.cardhire0);
        carhire1=findViewById(R.id.carhire1);
        newspapergroup = findViewById(R.id.newspapergroup);
        newspaper0 = findViewById(R.id.newspaper0);
        newspaper1 = findViewById(R.id.newspaper1);
        viproomgroup = findViewById(R.id.viproomgroup);
        viproom0 = findViewById(R.id.viproom0);
        viproom1 = findViewById(R.id.viproom1);
        airconditiongroup = findViewById(R.id.acgroup);
        roomsservicegroup = findViewById(R.id.roomservicegroup);
        roomservice1 = findViewById(R.id.roomservice1);
        roomservice0 = findViewById(R.id.roomserviec0);
        airfortsuttlegrop = findViewById(R.id.airfortshuttlegroup);
        airfort0  = findViewById(R.id.airfortshuttle0);
        airfort1 = findViewById(R.id.airfprtshuttle1);
        liftgroup = findViewById(R.id.lift_group);
        lift0 = findViewById(R.id.lift0);
        lift1 = findViewById(R.id.lift1);
        sharedlouge = findViewById(R.id.sharedlungh);
        sharedlounge0 = findViewById(R.id.sharedlungs0);
        sharedlounge1 = findViewById(R.id.sharedlung1);
        bridalgroup = findViewById(R.id.bridal_group);
        bridal0 = findViewById(R.id.bridal0);
        bridal1 = findViewById(R.id.bridal1);
        shopgroup=findViewById(R.id.shop_group);
        shop0=findViewById(R.id.shop0);
        shop1 =findViewById(R.id.shop1);
        lunch0=findViewById(R.id.lunch0);
        lunch1 =findViewById(R.id.lunch1);
        shuttlegroup = findViewById(R.id.shuttle_group);
        shuttle0 =findViewById(R.id.shuttle0);
        shuttle1 =findViewById(R.id.shuttle1);
        lunchgroup = findViewById(R.id.lunch_group);
        couplegroup=findViewById(R.id.couple_group);
        couple0=findViewById(R.id.couple0);
        couple1 = findViewById(R.id.couple1);
        parking1 =findViewById(R.id.parking1);
        parking0=findViewById(R.id.parking0);
        parkinggroup=findViewById(R.id.parking_group);
        hotelname = findViewById(R.id.hotelnameET);
        hoteldesc = findViewById(R.id.hoteldescET);
        roomleftgroup=findViewById(R.id.room_left_group);
        room1 = findViewById(R.id.room1);
        room0 = findViewById(R.id.room0);
        internet0=findViewById(R.id.internet0);
        internet1 = findViewById(R.id.internet1);
        internetgroup =findViewById(R.id.internet_group);
        pet0=findViewById(R.id.pet0);
        pet1 =findViewById(R.id.pet1);
        petgroup=findViewById(R.id.pet_group);
    }

    public void uploadImages(View view) {
        /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(intent, PICK_IMAGE);*/
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

    public void submit(View view) {
        checknullvalue(view);
    }
    private void checknullvalue(View view) {
        String shotelName = hotelname.getText().toString();
        String sdesc = hoteldesc.getText().toString();
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.room0:
                if (checked)
                    Toast.makeText(MainActivity.this,"yes",Toast.LENGTH_LONG).show();
                break;
            case R.id.room1:
                if (checked)
                    Toast.makeText(MainActivity.this,"no",Toast.LENGTH_LONG).show();
                break;
        }
    }


    public void OnHotelRoomClicked(View view) {
        boolean checked = ((RadioButton) view).isChecked();
        // Check which RadioButton was clicked
        switch(view.getId()) {
            case R.id.room0:
                if (checked)
                {
                    Toast.makeText(this, "Room Found", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.room1:
                if (checked)
                {
                    Toast.makeText(this, "Room not found", Toast.LENGTH_SHORT).show();
                }
            break;
            case R.id.internet0:
                if (checked)
                {
                    Toast.makeText(this, "Have Internet", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.internet1:
                if (checked)
                {
                    Toast.makeText(this, "No Internet", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
