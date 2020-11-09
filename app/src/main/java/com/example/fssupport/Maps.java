package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.w3c.dom.Text;

public class Maps extends AppCompatActivity implements Animation.AnimationListener {

    TextView hospital,police,fire,gotoGGMap;
    Animation animFadein;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_map);
        animFadein = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_down);
        animFadein.setAnimationListener((Animation.AnimationListener) this);
        AnhXa();
        setEvent();
    }

    public void setEvent(){
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigate);
        bottomNavigationView.setSelectedItemId(R.id.map);
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
                    case R.id.contact:
                        startActivity(new Intent(getApplicationContext(),Contact.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        return true;
                }
                return false;
            }
        });
        gotoGGMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openMap("");
                police.clearAnimation();
                fire.clearAnimation();
                hospital.clearAnimation();
            }
        });
        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hospital.startAnimation(animFadein);
                police.clearAnimation();
                fire.clearAnimation();
                openMap("bệnh viện");
            }
        });

        police.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                police.startAnimation(animFadein);
                fire.clearAnimation();
                hospital.clearAnimation();
                openMap("đồn cảnh sát");
            }
        });

        fire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fire.startAnimation(animFadein);
                police.clearAnimation();
                hospital.clearAnimation();
                openMap("Cảnh sát chữa cháy");
            }
        });
    }

    public void openMap(String center){
        Uri uri = Uri.parse("geo:0,0?q="+center);
        Intent intent = new Intent(Intent.ACTION_VIEW,uri);
        intent.setPackage("com.google.android.apps.maps");
        startActivity(intent);
    }
    public void AnhXa(){
        hospital = (TextView)findViewById(R.id.txt_hospital);
        police =(TextView)findViewById(R.id.txt_police);
        fire = (TextView)findViewById(R.id.txt_fire);
        gotoGGMap = (TextView)findViewById(R.id.txt_gotoGGMap);
    }
    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {

    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }
}