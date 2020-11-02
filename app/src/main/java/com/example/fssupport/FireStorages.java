package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class FireStorages extends AppCompatActivity {

    private FirebaseStorage storage;
    private DatabaseReference mDatabase;
    Button openCam,save;
    LottieAnimationView sos;
    ImageView imgCam;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fire_storages);
        storage = FirebaseStorage.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        AnhXa();
        openCam();
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                upImgID();
            }
        });
    }

    // Lay du lieu anh tu camera
    public void openCam(){
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               myMethod(view);
            }
        });
    }
    public void myMethod(View v){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }
    public void AnhXa(){
        sos = findViewById(R.id.sos);
        openCam = (Button)findViewById(R.id.btn_openCAm);
        imgCam = (ImageView)findViewById(R.id.img_cam);
        save = (Button)findViewById(R.id.btn_save);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        //Lay du lieu anh tu camera
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imgCam.setImageBitmap(bitmap);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    // push anh len storage firebase
    public void upImgID(){
        Calendar calendar = Calendar.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child("image"+calendar.getTimeInMillis()+".png");
        // Get the data from an ImageView as bytes
        imgCam.setDrawingCacheEnabled(true);
        imgCam.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imgCam.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(FireStorages.this, "Error!!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                  taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                      @Override
                      public void onSuccess(Uri uri) {
                          Toast.makeText(FireStorages.this, "thanh cong!", Toast.LENGTH_LONG).show();
                          mDatabase.child("Anh").setValue(uri.toString());
                      }
                  });
            }
        });
    }
}