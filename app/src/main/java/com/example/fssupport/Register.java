package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;


public class Register extends AppCompatActivity{

    ImageButton register;
    TextView error;
    EditText email,password,repassword,phonenumber;
    public String errorMessage;
    public static final String emailValue = "EMAILVALUE";
    public static final String passValue = "PASSLVALUE";
    public static final String phoneValue = "PHONEVALUE";
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        AnhXa();
        setAction();
    }
    public void AnhXa(){
        register = (ImageButton) findViewById(R.id.btn_register);
        email = (EditText)findViewById(R.id.etxt_email);
        password = (EditText)findViewById(R.id.etxt_password);
        repassword = (EditText)findViewById(R.id.etxt_repass);
        phonenumber = (EditText)findViewById(R.id.etxt_phone);
        error = (TextView)findViewById(R.id.txt_error);
    }

    public boolean showError(){
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Repassword = repassword.getText().toString();
        String Phonenumber = phonenumber.getText().toString();
        if(Email.isEmpty()||Password.isEmpty()||Repassword.isEmpty()||Phonenumber.isEmpty()){
            errorMessage = "Something is empty!";
            return true;
        }
        if (Phonenumber.length()!=9){
            errorMessage="Phone number invalid!";
            return true;
        }
        if(!Password.equals(Repassword)){
            errorMessage="Repassword not true!";
            return true;
        }
        return false;
    }

    public void setAction(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone_number ="+84" + phonenumber.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if(showError()){
                    error.setText(errorMessage);
                    error.setVisibility(View.VISIBLE);
                }
                else {
                    Intent intent = new Intent(Register.this, Verify.class);
                    intent.putExtra(phoneValue,phone_number);
                    intent.putExtra(emailValue,Email);
                    intent.putExtra(passValue,Password);
                    startActivity(intent);
                }
            }
        });
    }
}