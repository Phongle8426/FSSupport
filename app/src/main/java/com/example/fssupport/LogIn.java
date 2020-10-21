package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    EditText email,password;
    CheckBox rememberMe;
    TextView error,goRegis,forgotPass;
    ImageButton login;
    ProgressBar progress;
    FirebaseAuth mAuth;
    public static final String MyPREFERENCES = "MyPrefs";
    public static final String USERNAME = "userNameKey";
    public static final String PASS = "passKey";
    public static final String REMEMBER = "remember";
    SharedPreferences sharedpreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth =FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        AnhXa();
        loadData();
        setEvent();
    }

    public void Login(){
        final String Email = email.getText().toString();
        final String Password = password.getText().toString();
        if (Email.isEmpty() || Password.isEmpty()){
            error.setText("Something is empty!");
            if (Email.isEmpty())
                email.setFocusable(true);
            password.setFocusable(true);

        }else{
            mAuth.signInWithEmailAndPassword(Email, Password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                if (rememberMe.isChecked())
                                    saveData(Email,Password);
                                else
                                    clearData();
                                Intent intent = new Intent(LogIn.this,Home.class);
                                startActivity(intent);
                            }else {
                                error.setText("Something was wrong!");
                                email.setFocusable(true);
                            }
                        }
                    });
        }


    }
    public void setEvent(){
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
                progress.setVisibility(View.VISIBLE);
            }
        });

        goRegis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogIn.this,Register.class);
                startActivity(intent);
            }
        });

    }

    public void saveData(String username, String Pass){
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(USERNAME, username);
        editor.putString(PASS, Pass);
        editor.putBoolean(REMEMBER,rememberMe.isChecked());
        editor.commit();
    }
    private void clearData() {
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.clear();
        editor.commit();
    }

    private void loadData() {
        if(sharedpreferences.getBoolean(REMEMBER,false)) {
            email.setText(sharedpreferences.getString(USERNAME, ""));
            password.setText(sharedpreferences.getString(PASS, ""));
            rememberMe.setChecked(true);
            Login();
        }
        else
            rememberMe.setChecked(false);

    }
    public void AnhXa(){
        email = (EditText)findViewById(R.id.txt_email);
        password = (EditText)findViewById(R.id.txt_password);
        goRegis = (TextView) findViewById(R.id.btn_goto_regis);
        login = (ImageButton)findViewById(R.id.btn_login);
        error = (TextView) findViewById(R.id.txt_error);
        rememberMe = (CheckBox)findViewById(R.id.cb_remember);
        forgotPass = (TextView)findViewById(R.id.btn_forgot);
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);
    }
}