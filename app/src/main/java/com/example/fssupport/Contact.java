package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.fssupport.Dialog.DialogAddContact;
import com.example.fssupport.Object.ContactAdapter;
import com.example.fssupport.Object.ContactAdapterRecyclerView;
import com.example.fssupport.Object.ObjectContact;
import com.example.fssupport.Object.RecyclerViewClickInterface;
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
                contactList.clear();
                for(DataSnapshot data : snapshot.getChildren()){
                    ObjectContact ds = data.getValue(ObjectContact.class);
                    contactList.add(ds);
                }
                newContactAdapterRecyclerView();
                recyclerView.setAdapter(adapterRecyclerView);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void deleteContact(String contact){
        mDatabase.child(contact).child("name_contact").setValue(null);
        mDatabase.child(contact).child("phone_number").setValue(null);
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
    public void onLongItemClick( final int position) {
        final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle("Delete Contact!");
        dialog.setMessage("Do you want to delete this contact?");
        dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String name = contactList.get(position).getName_contact();
                contactList.remove(position);
                adapterRecyclerView.notifyItemRemoved(position);
                deleteContact(name);
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
}