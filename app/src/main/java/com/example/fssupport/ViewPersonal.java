package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
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

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class ViewPersonal extends AppCompatActivity implements DatePickerDialog.OnDateSetListener{

    EditText name,birthday,address,phonenumber,email,blood,idcard,note;
    Button edit,save;
    private DatabaseReference mDatabase;
    private FirebaseStorage storage;
    public String uid, name2,birthday2, email2, phone2, address2, idcard2, blood2, note2;
    ProgressBar load;
    private boolean isExistsInformation = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_personal);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        storage = FirebaseStorage.getInstance();
        AnhXa();
        disableTextField();
        save.setVisibility(View.INVISIBLE);
        load.setVisibility(View.INVISIBLE);
        blood.setInputType(InputType.TYPE_NULL);
        birthday.setInputType(InputType.TYPE_NULL);
        getIDCanhan();
        getInfoUser();
        setEvent();
    }

    // lấy ID của user hiện tại
    public void getIDCanhan(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            uid = user.getUid();
        }else{
            Toast.makeText(this, "Loi lay UID", Toast.LENGTH_SHORT).show();
        }
    }

    //lêý giá trị được nhập vào ở các trường
    public void getValueOnField(){
        name2 = name.getText().toString();
        email2 = email.getText().toString();
        phone2 = phonenumber.getText().toString();
        address2 = address.getText().toString();
        idcard2 = idcard.getText().toString();
        blood2 = blood.getText().toString();
        note2 = note.getText().toString();
        birthday2 = birthday.getText().toString();
    }

    // KKhông cho phép nhập các trường
    public void disableTextField(){
        name.setEnabled(false);
        birthday.setEnabled(false);
        email.setEnabled(false);
        phonenumber.setEnabled(false);
        address.setEnabled(false);
        idcard.setEnabled(false);
        blood.setEnabled(false);
        note.setEnabled(false);
    }

    // Cho phép nhập các trường
    public void enableTextField(){
        if (isExistsInformation == false){  // nếu thông tin đã đuọc nhập 1 lần.
            name.setEnabled(true);
            birthday.setEnabled(true);
            idcard.setEnabled(true);
        }
        email.setEnabled(true);
        phonenumber.setEnabled(true);
        address.setEnabled(true);
        blood.setEnabled(true);
        note.setEnabled(true);
    }

    // đẩy thông tin user lên DB
    public void pushInfo(){
        getValueOnField();
        ObjectInfoUser infoUser = new ObjectInfoUser(name2,email2, phone2, address2, idcard2, blood2, note2, birthday2);
        mDatabase.child("InfoUser").child(uid).setValue(infoUser);
    }

   // Lấy thông tin của user từ DB về
    public void getInfoUser(){
        mDatabase.child("InfoUser").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists())
                    isExistsInformation = true;
                showInfoUser(snapshot);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    // Lấy và hiển thị thông tin của user lên các trường
    public void showInfoUser(DataSnapshot snapshot){
        ObjectInfoUser infoUser =new ObjectInfoUser();
        infoUser.setName(snapshot.child(uid).child("name").getValue(String.class));
        infoUser.setBirthday(snapshot.child(uid).child("birthday").getValue(String.class));
        infoUser.setEmail(snapshot.child(uid).child("email").getValue(String.class));
        infoUser.setPhone(snapshot.child(uid).child("phone").getValue(String.class));
        infoUser.setAddress(snapshot.child(uid).child("address").getValue(String.class));
        infoUser.setIdcard(snapshot.child(uid).child("idcard").getValue(String.class));
        infoUser.setBlood(snapshot.child(uid).child("blood").getValue(String.class));
        infoUser.setNote(snapshot.child(uid).child("note").getValue(String.class));

        name.setText(infoUser.getName());
        birthday.setText(infoUser.getBirthday());
        email.setText(infoUser.getEmail());
        phonenumber.setText(infoUser.getPhone());
        address.setText(infoUser.getAddress());
        idcard.setText(infoUser.getIdcard());
        blood.setText(infoUser.getBlood());
        note.setText(infoUser.getNote());

    }

    // Bắt sự kiện các nút bấm
    public void setEvent(){
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enableTextField();
                edit.setVisibility(View.INVISIBLE);
                save.setVisibility(View.VISIBLE);
            }
        });
        birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showTimeDialog1(birthday);
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogConfirm();
             //   load.setVisibility(View.VISIBLE);
            }
        });
        blood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogChooseBlood();
            }
        });
    }

    // Dialog xác nhận thay đổi thông tin
    public void dialogConfirm(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Change Personal Information!");
        dialog.setMessage("Do you want to change? Please ensure that all of the information you provide is correct," +
                " you must responsible for it!");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pushInfo();
                edit.setVisibility(View.VISIBLE);
                save.setVisibility(View.INVISIBLE);
                disableTextField();
                isExistsInformation = true;

            }
        });
        dialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog al = dialog.create();
        al.show();
    }

    //Dialog chọn nhóm máu
    public void dialogChooseBlood(){
        final String[] datas = {"A", "B","C"};
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Blood Type!");
        dialog.setSingleChoiceItems(datas, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                blood.setText(datas[i]);
            }
        });
        AlertDialog al = dialog.create();
        al.show();
    }

    //Dialog chọn ngày sinh
    public void showTimeDialog1(final EditText TimePicker1){
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
                birthday.setText(simpleDateFormat.format(calendar.getTime()));
            }
        };
        new DatePickerDialog(this, dateSetListener,calendar.get(Calendar.DAY_OF_MONTH),calendar.get(Calendar.MONTH), calendar.get(Calendar.YEAR)).show();
    }

    //Ánh xạ các đối tượng trên Resource
    public void AnhXa(){
        name = (EditText) findViewById(R.id.txt_name);
        birthday = (EditText) findViewById(R.id.txt_birthday);
        address = (EditText) findViewById(R.id.txt_address);
        phonenumber = (EditText) findViewById(R.id.txt_phone);
        email = (EditText) findViewById(R.id.txt_email);
        blood = (EditText) findViewById(R.id.txt_blood);
        idcard = (EditText) findViewById(R.id.txt_idcard);
        note = (EditText) findViewById(R.id.txt_note);
        edit = (Button)findViewById(R.id.btn_edit);
        save = (Button)findViewById(R.id.btn_save);
        load =(ProgressBar)findViewById(R.id.progressLoadInViewPersonal);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {

    }

    // Lay du lieu anh tu camera
  /*
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // chuyen tu app sang camera
        imageIDcard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent, 1);
            }
        });
        //Lay du lieu anh tu camera
        if(requestCode == 1 && resultCode == RESULT_OK && data!=null){
            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
           // imageID.setImageBitmap(bitmap);

        }
        super.onActivityResult(requestCode, resultCode, data);
    }*/

    // push anh len storage firebase
 /*   public void upImgID(){
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
    }*/
}