package com.example.fssupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

public class Home extends AppCompatActivity {

    ImageButton map,contact,profile,history,sos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        AnhXa();
        setEvent();
    }
    
    public void setEvent(){
        map.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this, "Map ne", Toast.LENGTH_SHORT).show();
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Home.this, "Contact ne", Toast.LENGTH_SHORT).show();
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Home.this,Personal.class);
                startActivity(intent);
            }
        });
    }


    public void AnhXa(){
        map = (ImageButton)findViewById(R.id.btn_map);
        contact = (ImageButton)findViewById(R.id.btn_contact);
        profile = (ImageButton)findViewById(R.id.btn_profile);
        history = (ImageButton)findViewById(R.id.btn_history);
        sos = (ImageButton)findViewById(R.id.btn_sos);
    }
}