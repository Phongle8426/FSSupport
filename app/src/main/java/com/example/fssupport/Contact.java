package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.example.fssupport.Dialog.DialogAddContact;
import com.example.fssupport.Object.ContactAdapter;
import com.example.fssupport.Object.ContactAdapterRecyclerView;
import com.example.fssupport.Object.ObjectContact;
import com.example.fssupport.Object.RecyclerViewClickInterface;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Contact extends AppCompatActivity implements RecyclerViewClickInterface {
    //RecyclerViewClickInterface recyclerViewClickInterface;
    List<ObjectContact> contactList;
    RecyclerView recyclerView;
    ContactAdapterRecyclerView adapterRecyclerView;
    Button addContact;
    LottieAnimationView family;
    TextView text_family,text;
    public String uid,nameContact,phoneContact,allChildPost;
    public static final String nameValue = "NAME";
    public static final String phoneValue = "PHONE";
    private DatabaseReference mDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        AnhXa();
        setEvent();
        getIDCanhan();
        getListContact();
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                String phone = contactList.get(viewHolder.getLayoutPosition()).getPhone_number();
                deleteContact(phone);
                contactList.remove(viewHolder.getLayoutPosition());
                adapterRecyclerView.notifyItemRemoved(viewHolder.getLayoutPosition());
            }
        }).attachToRecyclerView(recyclerView);
    }


    public void getIDCanhan(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            uid = user.getUid();
        }else{
            Toast.makeText(this, "Loi lay UID", Toast.LENGTH_SHORT).show();
        }
    }
    public void newContactAdapterRecyclerView(){
        adapterRecyclerView = new ContactAdapterRecyclerView(contactList,this);
    }

    public void getListContact(){
        mDatabase = mDatabase.child("ContactUser").child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    text_family.setVisibility(View.INVISIBLE);
                    family.setVisibility(View.INVISIBLE);
                    text.setVisibility(View.VISIBLE);
                    contactList.clear();
                    for(DataSnapshot data : snapshot.getChildren()){
                        ObjectContact ds = data.getValue(ObjectContact.class);
                        contactList.add(ds);
                    }
                    newContactAdapterRecyclerView();
                    recyclerView.setAdapter(adapterRecyclerView);
                    if (contactList.size()==5){
                        addContact.setEnabled(false);
                        addContact.setBackgroundResource(R.drawable.shape_round_dis);
                    }else{
                        addContact.setBackgroundResource(R.drawable.shape_round);
                        addContact.setEnabled(true);
                    }

                }else{
                    contactList.clear();
                    text_family.setVisibility(View.VISIBLE);
                    family.setVisibility(View.VISIBLE);
                    text.setVisibility(View.INVISIBLE);
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteContact(String contact){
        //mDatabase.child(contact).child("name_contact").setValue(null);
        //mDatabase.child(contact).child("phone_number").setValue(null);
        mDatabase.child(contact).setValue(null);
    }

    public void activeItemBottomNavigation(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigate);
        bottomNavigationView.setSelectedItemId(R.id.contact);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    //1 cho cho history
                    case R.id.personal:
                        startActivity(new Intent(getApplicationContext(),ViewPersonal.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(),Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(),Maps.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.history:
                        startActivity(new Intent(getApplicationContext(),History.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.contact:
                        return true;
                }
                return false;
            }
        });
    }
    public void setEvent(){
       activeItemBottomNavigation();
       addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialogAddContact();
            }
        });
    }

    public void openDialogAddContact(){
        DialogAddContact dialogAddContact = new DialogAddContact();
        dialogAddContact.show(getSupportFragmentManager(),"add contact");

    }
    public void AnhXa(){
        addContact = (Button)findViewById(R.id.btnAddContact);
        recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        text = findViewById(R.id.text);
        text_family = findViewById(R.id.text_family);
        family = findViewById(R.id.family);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();

    }

    @Override
    public void onItemClick(int position) {
        String name = contactList.get(position).getName_contact();
        String phone = contactList.get(position).getPhone_number();
        Bundle bundle = new Bundle(); //Bundle containing data you are passing to the dialog
        bundle.putString(nameValue, name);
        bundle.putString(phoneValue, phone);
        DialogAddContact dialogAddContact = new DialogAddContact();
        dialogAddContact.show(getSupportFragmentManager(),"add contact");
        dialogAddContact.setArguments(bundle);
    }

    @Override
    public void onBackPressed(){

    }
}