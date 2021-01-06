package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.auth.SignInMethodQueryResult;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.concurrent.TimeUnit;


public class Register extends AppCompatActivity {

    Button register;
    TextInputEditText email, password, repassword, phonenumber;
    int i = 0;
    public static final String emailValue = "EMAILVALUE";
    public static final String passValue = "PASSLVALUE";
    public static final String phoneValue = "PHONEVALUE";
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        AnhXa();
        setAction();
    }

    public void AnhXa() {
        register = findViewById(R.id.btn_register);
        email = findViewById(R.id.etxt_email);
        password = findViewById(R.id.etxt_password);
        repassword = findViewById(R.id.etxt_repass);
        phonenumber = findViewById(R.id.etxt_phone);
    }

    public boolean showError() {
        final String emailPattern = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Repassword = repassword.getText().toString();
        String Phonenumber = phonenumber.getText().toString();
        if (Email.isEmpty() || Password.isEmpty() || Repassword.isEmpty() || Phonenumber.isEmpty()) {
            Toast.makeText(this, "Something is empty!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (Phonenumber.length() != 9) {
            Toast.makeText(this, "Phone number invalid!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!Password.equals(Repassword)) {
            Toast.makeText(this, "Re-Password is wrong!", Toast.LENGTH_SHORT).show();
            return true;
        }
        if (!Email.matches(emailPattern)) {
            Toast.makeText(getApplicationContext(), "Invalid email", Toast.LENGTH_SHORT).show();
            return true;
        }

//        if (!checkEmail(Email)){
//            Toast.makeText(this, "This email already exists!", Toast.LENGTH_SHORT).show();
//            return true;
//        }
        return false;
    }

    public void checkEmail(String email) {
        mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {

            @Override
            public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                boolean check = task.getResult().getSignInMethods().isEmpty(); // khong co email
                if (check) {
                    //Toast.makeText(Register.this, "khong co", Toast.LENGTH_SHORT).show();
                    i = 0; //k co email
                }
            }
        });
        if (i != 0) // neu co email
            Toast.makeText(this, "co email", Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "k co email", Toast.LENGTH_SHORT).show();
//            return false; // tra ve co email
//        return true; // tra ve k co email
    }

    public void setAction() {
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Email = email.getText().toString();
                mAuth.fetchSignInMethodsForEmail(Email).addOnCompleteListener(new OnCompleteListener<SignInMethodQueryResult>() {
                    @Override
                    public void onComplete(@NonNull Task<SignInMethodQueryResult> task) {
                        boolean check = task.getResult().getSignInMethods().isEmpty(); // khong co email
                        if (check) {
                            final String phone_number = "+84" + phonenumber.getText().toString();
                            final String Email = email.getText().toString();
                            final String Password = password.getText().toString();
                            if (!showError()) {
                                mDatabase.child("PhoneNumber").orderByChild("phone").equalTo(phone_number).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if (snapshot.exists()) {
                                            Toast.makeText(Register.this, "This phone number already exists!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Intent intent = new Intent(Register.this, Verify.class);
                                            intent.putExtra(phoneValue, phone_number);
                                            intent.putExtra(emailValue, Email);
                                            intent.putExtra(passValue, Password);
                                            startActivity(intent);
                                        }
                                        mDatabase.child("PhoneNumber").orderByChild("phone").equalTo(phone_number).removeEventListener(this);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }else Toast.makeText(Register.this, "This email already exists!", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
    }

    @Override
    public void onBackPressed() {

    }
}