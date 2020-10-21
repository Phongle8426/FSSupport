package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fssupport.Dialog.DialogAddContact;
import com.example.fssupport.Object.ContactAdapter;
import com.example.fssupport.Object.ContactAdapterRecyclerView;
import com.example.fssupport.Object.ObjectContact;
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

public class Contact extends AppCompatActivity {

    List<ObjectContact> contactList;
    RecyclerView recyclerView;
    ContactAdapterRecyclerView adapterRecyclerView;
    Button addContact;
    public String uid,nameContact,phoneContact,allChildPost;
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
    }

    public void getIDCanhan(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user!=null) {
            uid = user.getUid();
        }else{
            Toast.makeText(this, "Loi lay UID", Toast.LENGTH_SHORT).show();
        }
    }

    public void getListContact(){
        mDatabase = mDatabase.child("ContactUser").child(uid);
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contactList.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    ObjectContact ds = data.getValue(ObjectContact.class);
                    contactList.add(ds);
                }
                adapterRecyclerView = new ContactAdapterRecyclerView(contactList);
                recyclerView.setAdapter(adapterRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    public void setEvent(){
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        contactList = new ArrayList<>();

    }
}