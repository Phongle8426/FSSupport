package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.fssupport.Modules.DirectionFinder;
import com.example.fssupport.Modules.DirectionFinderListener;
import com.example.fssupport.Modules.Route;
import com.example.fssupport.Object.ObjectContact;
import com.example.fssupport.Object.ObjectDistanceCenter;
import com.example.fssupport.Object.ObjectHistory;
import com.example.fssupport.Object.ObjectProfileCenter;
import com.example.fssupport.Object.ObjectSupportCenter;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.PublicKey;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import static com.example.fssupport.LogIn.MyPREFERENCES;


public class Home extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, OnMapReadyCallback, DirectionFinderListener {

    Button popup;
    LottieAnimationView sos;
    SharedPreferences sharedpreferences;
    FusedLocationProviderClient fusedLocationProviderClient;
    public int currentCenterIndex;
    public String message;
    public String uid,longitudeUser,latitudeUser,cityUser,finalIndex,nameCenter,typeCenter;
    public boolean trans = false;
    //boolean check = false;
    public List<ObjectSupportCenter> centerList;
    public List<ObjectContact> contact;
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth =FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        AnhXa();
        getUid();
        setEvent();
    }

    //lấy UID
    public void getUid(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        uid = user.getUid();
    }

    // Hiênj dialog setting
    public void dialogLogOut(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Log Out!");
        dialog.setMessage("Do you want to exit?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                sigOut();
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
    // hàm mở dialog thông báo request đã thành công
    public void dialogNotificateRequestSuccess(String center){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Request Successful!");
        dialog.setMessage("Your Request had accepted by "+ center);
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog al = dialog.create();
        al.show();
    }

    public void dialogNotificateRequestFail(){
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Request Fail!");
        dialog.setMessage("Have no center nearby here !");
        dialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog al = dialog.create();
        al.show();
    }
    //chức năng Logout
    public void sigOut(){
        mAuth.signOut();
        clearData();
        Intent intent_toLogin = new Intent(Home.this,LogIn.class);
        intent_toLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_toLogin);
    }
    // xóa key-value shareReference ghi nhớ tài khoản
    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }
    public void activeItemBottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigate);
        bottomNavigationView.setSelectedItemId(R.id.home);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.personal:
                        startActivity(new Intent(getApplicationContext(),ViewPersonal.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),Maps.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.contact:
                        startActivity(new Intent(getApplicationContext(),Contact.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(),History.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        return true;
                }
                return false;
            }
        });
    }
    // bắt các sự kiện
    public void setEvent(){
       activeItemBottomNavigation();
        sos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogSelectCenter();
            }
        });
    }

    public Double CalculationByDistance(double x, double y) {
        double distance;
        Location locationA = new Location("");
        locationA.setLatitude(Double.valueOf(latitudeUser));
        locationA.setLongitude(Double.valueOf(longitudeUser));
        Location locationB = new Location("");
        locationB.setLatitude(x);
        locationB.setLongitude(y);
        distance = locationA.distanceTo(locationB)/1000;
        return distance;
    }

    // Tạo nội dung tin nhắn với nội dung đầy đủ
    public String createContentSMS(){
        final String[] name_user = new String[1];
        mDatabase.child("InfoUser").child(uid).child("Information").child("name").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                name_user[0] = snapshot.getValue().toString();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return "I am " + name_user[0] +", I want to let you know I'm having trouble stay in  "+ latitudeUser +","+longitudeUser
                +".Copy this number and search on map you will be known where am I, please!";
    }

    // Gửi tin nhắn cho tất cả các liên hệ trong Contact
    public void sendSMS(){
        // lấy tất cả các số điện thoại có trong Contact
            mDatabase = mDatabase.child("ContactUser").child(uid);
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    contact.clear();
                    for(DataSnapshot data : snapshot.getChildren()){
                        ObjectContact ds = data.getValue(ObjectContact.class);
                        contact.add(ds);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            //thực hiện kiểm tra cho phép sử dụng tin nhắn của điện thoại
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS)!= PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)){

            }else{
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, MY_PERMISSIONS_REQUEST_SEND_SMS);
            }
        }
        // thực hiện gửi tin nhắn cho tất cả số điện thoại
            for(ObjectContact data : contact){
                send(data.getPhone_number(),createContentSMS());
            }
        Toast.makeText(this, "The SMS already send !", Toast.LENGTH_SHORT).show();
    }

    // Hàm gửi tin nhắn cho 1 số điện thoại
    private void send(String phoneNumber, String message) {
        SmsManager sms = SmsManager.getDefault();
        sms.sendTextMessage(phoneNumber, null, message, null, null);
    }

    // Hàm thực hiện ghép latitude và longitude thành 1 chuỗi
    public String mergLatLg(String latitude, String longitude){
        return latitude + "," + longitude;
    }

    // thực hiện mở dialog để chọn type Center
    public void openDialogSelectCenter(){
      //  final String[] hichic = new String[1];
        final String[] items = {"Police", "Hospital", "Fire"};
        AlertDialog.Builder b = new AlertDialog.Builder(this);
        b.setTitle("Please Select a Type of Center.");
        b.setSingleChoiceItems(items, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                typeCenter = items[i];
                dialogInterface.dismiss();
                if (typeCenter != null){
                     findCurentLocationUser();
                    sendSMS();
                }
            }
        });
        AlertDialog mDialog = b.create();
        mDialog.show();

    }
    // Hàm thực hiện tìm vị trí hiện tại của User.
    public void findCurentLocationUser(){
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if (location != null) {
                        try {
                            Geocoder geocoder = new Geocoder(Home.this, Locale.getDefault());
                            List<Address> userLocation = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 10);
                            cityUser= userLocation.get(0).getAdminArea()+"";
                            latitudeUser = userLocation.get(0).getLatitude()+"";
                            longitudeUser =  userLocation.get(0).getLongitude()+"";
                            getListCenter(typeCenter,cityUser);
                           // Toast.makeText(Home.this, latitudeUser, Toast.LENGTH_SHORT).show();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });
        } else {
            ActivityCompat.requestPermissions(Home.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 44);
        }
    }

    // thêm vào room chờ của center thông tin ở SubTransaction, xử lý trả lời của center trả lời của center ở MessageReceive
    public void setTransaction(final String Center_id){
      //  Toast.makeText(this, "chạy trans", Toast.LENGTH_SHORT).show();
        mDatabase.child("Requests").child(Center_id).child("tran_status").setValue(false);
        mDatabase.child("Requests").child(Center_id).child("user_id").setValue(uid);
        mDatabase.child("Requests").child(Center_id).child("latitude_user").setValue(latitudeUser);
        mDatabase.child("Requests").child(Center_id).child("longitude_user").setValue(longitudeUser);
         final Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            boolean check = false;
            int i =1;
            @Override
            public void run() {
                Log.d("AAAAAAAAAAAAAAAA"+i,"run");
                if (!check) {
                    listenRespond(Center_id);
                     message = String.valueOf(message);
                    if (message.compareTo("true")==0){
                        check = true;
                        storeHistory(nameCenter,typeCenter,Center_id,latitudeUser,longitudeUser);
                        mDatabase.child("Requests").child(Center_id).child("message_toUser").setValue("null");
                        mDatabase.child("Requests").child(Center_id).child("latitude_user").setValue("");
                        mDatabase.child("Requests").child(Center_id).child("longitude_user").setValue("");
                        mDatabase.child("Requests").child(Center_id).child("user_id").setValue("");
                        mDatabase.child("Requests").child(Center_id).child("tran_status").setValue(true);
                        dialogNotificateRequestSuccess(nameCenter);
                    }else if(message.compareTo("false")==0){
                      //  Toast.makeText(Home.this, "Khong nhan may", Toast.LENGTH_SHORT).show();
                        check = true;
                        // chuyển hướng tìm một center khác, loại center hiện tại ra khỏi listcenter
                      //  deleteCenterCurrent(currentCenterIndex);
                        mDatabase.child("Requests").child(Center_id).child("message_toUser").setValue("null");
                        mDatabase.child("Requests").child(Center_id).child("latitude_user").setValue("");
                        mDatabase.child("Requests").child(Center_id).child("longitude_user").setValue("");
                        mDatabase.child("Requests").child(Center_id).child("user_id").setValue("");
                        mDatabase.child("Requests").child(Center_id).child("tran_status").setValue(true);
                    }else{
                        Toast.makeText(Home.this, "wait", Toast.LENGTH_SHORT).show();
                        i++;
                    }
                    mHandler.postDelayed(this, 3000);
                    if (i==10){
                        check = true;
                        mDatabase.child("Requests").child(Center_id).child("latitude_user").setValue("");
                        mDatabase.child("Requests").child(Center_id).child("longitude_user").setValue("");
                        mDatabase.child("Requests").child(Center_id).child("user_id").setValue("");
                        mDatabase.child("Requests").child(Center_id).child("tran_status").setValue(true);
                    }
                }
            }
        },3000);
    }

    //Lắng nghe phản hồi từ center
    public void listenRespond(final String idcenter){
        mDatabase.child("Requests").child(idcenter).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if(snapshot.exists()){
                        message = snapshot.getValue().toString();
                        Log.d("MESSAGE", message);
                }else
                {
                    Toast.makeText(Home.this, "Have no data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    // Hàm ghi vào lịch sử của user
    public void storeHistory(String centerName,String typeCenter,String idCenter,String lat,String longi){
        DateFormat df = new SimpleDateFormat("HH'h'mm'-'dd/MM/yy");
        String date = df.format(Calendar.getInstance().getTime());
        String cityCenter = cityUser;
        ObjectHistory historyUser = new ObjectHistory(centerName,typeCenter,cityCenter,date,idCenter,lat,longi);
        mDatabase.child("InfoUser").child(uid).child("HistoryUser").push().setValue(historyUser);
    }
    // Hàm thực hiện tìm center gần nhất với user
 /*   public void findCenterNearest1(){
        sendRequest(mergLatLg(latitudeUser,longitudeUser),mergLatLg(distanceList.get(0).getLatitude(),distanceList.get(0).getLongitude()));
      //  minDistance = checkDistance;
        for(ObjectDistanceCenter emplement : distanceList){
            sendRequest(mergLatLg(latitudeUser,longitudeUser),mergLatLg(emplement.getLatitude(),emplement.getLongitude()));
         //   if (checkDistance < minDistance){
           //     minDistance = checkDistance;
                latitudeCenter = emplement.getLatitude();
                longitudeCenter = emplement.getLongitude();
                nameCenter = emplement.getNamecenter();
                finalIndex = emplement.getCenterid();
            //}
        }
    }*/

 // Tìm center có khoảng cách ngắn nhất tới user.. trả về id và name
    public void findCenterNearest(List<ObjectSupportCenter> center){
        double checkDistance,minDistance;
        int indexOfNeareast = 0;
        minDistance = CalculationByDistance(Double.valueOf(center.get(0).getCenter_latitude()),
                Double.valueOf(center.get(0).getCenter_longitude()));
        for(int i = 0 ; i< center.size() ; i++) {
            checkDistance = CalculationByDistance(Double.valueOf(center.get(i).getCenter_latitude()),
                    Double.valueOf(center.get(i).getCenter_longitude()));
            if (checkDistance < minDistance) {
                minDistance = checkDistance;
                indexOfNeareast = i;
            }
        }
        finalIndex = centerList.get(indexOfNeareast).getCenter_id();
        currentCenterIndex = indexOfNeareast;
        nameCenter = centerList.get(indexOfNeareast).getCenter_name();
        Toast.makeText(this, finalIndex, Toast.LENGTH_SHORT).show();
        setTransaction(finalIndex);
    }

    // Hàm thực hiện xóa center đã từ chối request của user ra khỏi list center và nếu list > 0 thì  chạy lại hàm tìm kiếm center gần nhất
    // nếu không thì thông báo không có center nào gần đây.
//    public void deleteCenterCurrent(int index){
//        centerList.remove(index);
//        if (centerList.size()>0){
//            findCenterNearest(centerList);
//        }else{
//            dialogNotificateRequestFail();
//        }
//    }

    // Hàm thực hiện lấy thông tin vị trí của các Center đang sẵn sàng phù hợp gần User
    public void getListCenter(String type_center,String city){
        mDatabase.child("SupportCenter").child(type_center).child(city).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                for(DataSnapshot snap : snapshot.getChildren()) {
                    ObjectSupportCenter ds = snap.getValue(ObjectSupportCenter.class);
                    if (ds.getCenter_status().equals("true"))
                    centerList.add(ds);
                }
                if (centerList.size() > 0)
                    findCenterNearest(centerList);
                else
                    dialogNotificateRequestFail();
                }else  dialogNotificateRequestFail();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    //Hàm thực hiện gửi request tìm khoảng cách ngắn nhất
    private void sendRequest(String origin, String destination ) {
        if (origin.isEmpty()) {
            return;
        }
        if (destination.isEmpty()) {
            return;
        }
        try {
            new DirectionFinder(this, origin, destination).execute();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    // Hàm thực hiện mở setting
    public void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }


    @Override
    protected void onResume() {
        super.onResume();
       // if (trans == true){
            //Toast.makeText(this, ""+finalIndex, Toast.LENGTH_SHORT).show();
            //listenRespond(finalIndex);
       // }
    }

    public void AnhXa(){
        popup = (Button)findViewById(R.id.btn_option);
        centerList = new ArrayList<>();
        contact = new ArrayList<>();
        sos = findViewById(R.id.sos);
    }

    // Hàm thực hiện các chức năng có trong setting
    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.item1:
                dialogLogOut();
                return true;
            case R.id.item2:
                Toast.makeText(this, "dang xuat", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.item3:
                Toast.makeText(this, "dang xuat", Toast.LENGTH_SHORT).show();
                return true;
            default: return false;
        }
    }

    @Override
    public void onDirectionFinderStart() {

    }

    // Hàm trả về khoảng cách.
    @Override
    public void onDirectionFinderSuccess(List<Route> route) {
        //checkDistance = route.get(0).distance.value;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

    }
    @Override
    public void onBackPressed(){

    }
}