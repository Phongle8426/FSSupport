package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.fssupport.Object.ContactAdapterRecyclerView;
import com.example.fssupport.Object.HistoryAdapterRecyclerView;
import com.example.fssupport.Object.HistoryRecyclerViewClickInterface;
import com.example.fssupport.Object.ObjectContact;
import com.example.fssupport.Object.ObjectHistory;
import com.example.fssupport.Object.RecyclerViewClickInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class History extends AppCompatActivity implements HistoryRecyclerViewClickInterface {

    List<ObjectHistory> historyList;
    RecyclerView recyclerView;
    public String uid;
    private DatabaseReference mDatabase;
    HistoryAdapterRecyclerView adapterRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        Anhxa();
        getIDCanhan();
        setEvent();
        getListHistory();

    }

    public void getIDCanhan(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            uid = user.getUid();
        }else{
            Toast.makeText(this, "Loi lay UID", Toast.LENGTH_SHORT).show();
        }
    }
    public void newHistoryAdapterRecyclerView(){
        adapterRecyclerView = new HistoryAdapterRecyclerView(historyList,this);
    }
    public void getListHistory(){
        mDatabase = mDatabase.child("InfoUser").child(uid).child("HistoryUser");
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                historyList.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    ObjectHistory ds = data.getValue(ObjectHistory.class);
                    historyList.add(ds);
                }
                newHistoryAdapterRecyclerView();
                recyclerView.setAdapter(adapterRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
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
                        return true;
                }
                return false;
            }
        });
    }
    public void setEvent(){
        activeItemBottomNavigation();
    }
    public void Anhxa(){
        recyclerView = (RecyclerView)findViewById(R.id.recycler_history_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        historyList = new ArrayList<>();
    }

    @Override
    public void onItemClick(int position) {
        String idCenter = historyList.get(position).getCenterID();
        String latitudeUser = historyList.get(position).getLatitude();
        String longitudeUser = historyList.get(position).getLongitude();
        String time = historyList.get(position).getDayTime();
        String namecenter = historyList.get(position).getNameCenter();
        String typecenter = historyList.get(position).getTypeCenter();
        Intent intent = new Intent(getApplicationContext(),DetailHistory.class);
        intent.putExtra("IDUSER",idCenter);
        intent.putExtra("LATITUDEUSER",latitudeUser);
        intent.putExtra("LONGITUDEUSER",longitudeUser);
        intent.putExtra("TIMEUSER",time);
        intent.putExtra("NAMCENTER",namecenter);
        intent.putExtra("TYPECENTER",typecenter);
        startActivity(intent);
    }

    @Override
    public void onLongItemClick(int position) {

    }
}