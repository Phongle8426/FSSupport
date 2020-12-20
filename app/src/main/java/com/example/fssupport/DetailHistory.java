package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.example.fssupport.Object.ObjectProfileCenter;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DetailHistory extends AppCompatActivity implements OnMapReadyCallback{

    TextView time,location,name_center,center_address,center_mail,center_phone,center_type;
    String timeUser,latitudeUser,longitudeUser,idCenter,nameCenter,citycenter,addressCenter,mailCenter,phoneCenter,typeCenter;
    List<ObjectProfileCenter> infoCenterList;
    GoogleMap map;
    SupportMapFragment mapFragment;
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_history);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        AnhXa();
        mapFragment.getMapAsync( this);
        getPutExtraHistory();
        getInforUser();
        setEvent();
    }

    public void setEvent(){
        activeItemBottomNavigation();
    }
    public void getPutExtraHistory(){
        Bundle extras = getIntent().getExtras();
        citycenter = extras.getString("CITYCENTER");
        timeUser = extras.getString("TIMEUSER");
        latitudeUser = extras.getString("LATITUDEUSER");
        longitudeUser = extras.getString("LONGITUDEUSER");
        idCenter = extras.getString("IDCENTER");
        nameCenter = extras.getString("NAMCENTER");
        typeCenter = extras.getString("TYPECENTER");

    }

    public void getInforUser(){
        mDatabase.child("InfomationCenter").child(idCenter).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    ObjectProfileCenter info = new ObjectProfileCenter();
                    info.setCenter_address(snapshot.child("center_address").getValue(String.class));
                    info.setCenter_email(snapshot.child("center_email").getValue(String.class));
                    info.setCenter_phone(snapshot.child("center_phone").getValue(String.class));

                    addressCenter = info.getCenter_address();
                    mailCenter = info.getCenter_email();
                    phoneCenter = info.getCenter_phone();
                }
                setInfoHistory();
                mDatabase.child("InfomationCenter").child(idCenter).removeEventListener(this);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    public void setInfoHistory(){
        time.setText(timeUser);
        location.setText(latitudeUser+","+longitudeUser);
        name_center.setText(nameCenter);
       center_address.setText(addressCenter);
       center_phone.setText(phoneCenter);
       center_type.setText(typeCenter);
       center_mail.setText(mailCenter);
    }
    public void AnhXa(){
        time = findViewById(R.id.time);
        location = findViewById(R.id.location_user);
        name_center = findViewById(R.id.name_center);
        center_address = findViewById(R.id.address_center);
        center_mail = findViewById(R.id.mail_center);
        center_phone = findViewById(R.id.phone_center);
        center_type = findViewById(R.id.type_center);
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map_history);
        infoCenterList = new ArrayList<>();
    }
    public void activeItemBottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigate);
        bottomNavigationView.setSelectedItemId(R.id.history);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),Maps.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.personal:
                        startActivity(new Intent(getApplicationContext(),ViewPersonal.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.contact:
                        startActivity(new Intent(getApplicationContext(),Contact.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(),History.class));
                        return true;
                }
                return false;
            }
        });
    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        final LatLng latLng = new LatLng(Double.parseDouble(latitudeUser),Double.parseDouble(longitudeUser));
        map.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                map.addMarker(new MarkerOptions().title("user's location").position(latLng));
            }
        });
    }

    @Override
    public void onBackPressed(){

    }
}