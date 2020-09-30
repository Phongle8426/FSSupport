package com.example.fssupport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class Welcome extends AppCompatActivity {

    Button roleUser,roleCenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        Anhxa();
    }
    public void Anhxa(){
        roleUser = (Button) findViewById(R.id.btnRoleUser);
        roleCenter = (Button) findViewById(R.id.btnRoleCenter);
    }
    public void Action(){
        roleUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

}