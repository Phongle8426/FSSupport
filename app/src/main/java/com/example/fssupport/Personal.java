package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;

public class Personal extends AppCompatActivity {

    EditText name,email,phone,address,idcard,blood,note;
    ImageButton imageIDcard,save;
    ImageView imageID;
    TextView error;
    public String uid, name2, email2, phone2, address2, idcard2, blood2, note2;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();

        AnhXa();
        getIDCanhan();
      //  getInfoUser();
        setEvent();
    }

    public void getIDCanhan(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            uid = user.getUid();
        }else{
            Toast.makeText(this, "Loi lay UID", Toast.LENGTH_SHORT).show();
        }
    }

    public void getValueOnField(){
        name2 = name.getText().toString();
        email2 = email.getText().toString();
        phone2 = phone.getText().toString();
        address2 = address.getText().toString();
        idcard2 = idcard.getText().toString();
        blood2 = blood.getText().toString();
        note2 = note.getText().toString();
    }

    public void pushInfo(){
        getValueOnField();
        ObjectInfoUser infoUser = new ObjectInfoUser(name2, email2, phone2, address2, idcard2, blood2, note2);
        mDatabase.child("InfoUser").child(uid).setValue(infoUser);
    }

    public void setEvent(){
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushInfo();
             //   upImgID();
            }
        });

        imageIDcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            imageID.setImageBitmap(bitmap);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void upImgID(){
        Calendar calendar = Calendar.getInstance();
        StorageReference storageRef = storage.getReference();
        StorageReference mountainsRef = storageRef.child("image"+calendar.getTimeInMillis()+".png");
        // Get the data from an ImageView as bytes
        imageID.setDrawingCacheEnabled(true);
        imageID.buildDrawingCache();
        Bitmap bitmap = ((BitmapDrawable) imageID.getDrawable()).getBitmap();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
                Toast.makeText(Personal.this, "Error!!!", Toast.LENGTH_SHORT).show();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, etc.
                // ...
              //  Uri download = taskSnapshot.getDownLoadUrl();
                Toast.makeText(Personal.this, "thanh cong!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void getInfoUser(){
        mDatabase.child("InfoUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
               showInfoUser(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void showInfoUser(DataSnapshot snapshot){
        ObjectInfoUser infoUser =new ObjectInfoUser();
        infoUser.setName(snapshot.child(uid).child("name").getValue(String.class));
        infoUser.setEmail(snapshot.child(uid).child("email").getValue(String.class));
        infoUser.setPhone(snapshot.child(uid).child("phone").getValue(String.class));
        infoUser.setAddress(snapshot.child(uid).child("address").getValue(String.class));
        infoUser.setIdcard(snapshot.child(uid).child("idcard").getValue(String.class));
        infoUser.setBlood(snapshot.child(uid).child("blood").getValue(String.class));
        infoUser.setNote(snapshot.child(uid).child("note").getValue(String.class));

        name.setText(infoUser.getName());
        email.setText(infoUser.getEmail());
        phone.setText(infoUser.getPhone());
        address.setText(infoUser.getAddress());
        idcard.setText(infoUser.getIdcard());
        blood.setText(infoUser.getBlood());
        note.setText(infoUser.getNote());

    }

    public boolean checkError(){
        getValueOnField();
        if(name2.isEmpty()||email2.isEmpty()||phone2.isEmpty()||address2.isEmpty()||idcard2.isEmpty()||blood2.isEmpty()||note2.isEmpty()){
            error.setText("Something is empty!");
            return true;
        }
        return false;
    }

    public void AnhXa(){
        name = (EditText)findViewById(R.id.etxt_name);
        email = (EditText)findViewById(R.id.etxt_email);
        phone = (EditText)findViewById(R.id.etxt_phoneNumber);
        address = (EditText)findViewById(R.id.etxt_address);
        idcard = (EditText)findViewById(R.id.etxt_IDcard);
        imageID = (ImageView)findViewById(R.id.imgId);
        blood = (EditText)findViewById(R.id.etxt_blood);
        note = (EditText)findViewById(R.id.etxt_note);
        imageIDcard = (ImageButton)findViewById(R.id.btn_imageID);
        save = (ImageButton)findViewById(R.id.btn_save);
        error = (TextView)findViewById(R.id.txt_error);
    }
}