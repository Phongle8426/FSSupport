package com.example.fssupport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

import static com.example.fssupport.LogIn.MyPREFERENCES;


public class Home extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    ImageButton map,contact,profile,history,sos;
    Button popup;
    SharedPreferences sharedpreferences;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth =FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);

        AnhXa();
        setEvent();
    }

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
    public void sigOut(){
        mAuth.signOut();
        clearData();
        Intent intent_toLogin = new Intent(Home.this,LogIn.class);
        intent_toLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent_toLogin);
    }
    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }
    public void setEvent(){
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_maps = new Intent(Home.this,Maps.class);
                startActivity(intent_maps);
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_toContact = new Intent(Home.this,Contact.class);
                startActivity(intent_toContact);
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_personal = new Intent(Home.this,ViewPersonal.class);
                startActivity(intent_personal);
            }
        });
    }

    public void showPopup(View v){
        PopupMenu popupMenu = new PopupMenu(this, v);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    public void AnhXa(){
        popup = (Button)findViewById(R.id.btn_option);
        map = (ImageButton)findViewById(R.id.btn_map);
        contact = (ImageButton)findViewById(R.id.btn_contact);
        profile = (ImageButton)findViewById(R.id.btn_profile);
        history = (ImageButton)findViewById(R.id.btn_history);
        sos = (ImageButton)findViewById(R.id.btn_sos);
    }

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
}