package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class Verify extends AppCompatActivity {
    EditText code;
    TextView error,reSend;
    ImageButton verify;
    private String phonenumber;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        mAuth = FirebaseAuth.getInstance(); // bien authentication
        AnhXa();
        phonenumber = getIntent().getStringExtra(Register.phoneValue); // lay phone tu register
        verifyPhoneNumber(); // goi ham xu ly xac thuc phone
        setEvent(); // goi ham xu ly RESEND CODE
    }

    public void AnhXa(){
        code = (EditText) findViewById(R.id.etxt_code);
        reSend = (TextView) findViewById(R.id.txt_resendCode);
        verify = (ImageButton)findViewById(R.id.btn_verify);
        error = (TextView)findViewById(R.id.txt_error);
    }

    // ham xu ly gui yeu cau tao code va nhan code
    public void verifyPhoneNumber(){
        String phoneNumber = phonenumber;
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                 phoneNumber, 60, TimeUnit.SECONDS, Verify.this,
                new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                    @Override
                    public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

                    }

                    //neu khong gui duoc yeu cau
                    @Override
                    public void onVerificationFailed(@NonNull FirebaseException e) {
                        error.setText("failed!");
                    }

                    // khi ma nhap dung code thi thuc hien dang ki email
                    @Override
                    public void onCodeSent(final String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        verify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String verificationCode = code.getText().toString();
                                if (verificationId.isEmpty()) return;
                                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
                                signInUser(credential);
                            }
                        });
                    }
                });
    }

    // ham xu li dang ki bang phone, goi ham dk bang email
        private void signInUser (PhoneAuthCredential credential){
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(Verify.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                signUpEmail();
                            } else {
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    error.setVisibility(View.VISIBLE);
                                    error.setText("There was an error verifying OTP");
                                }
                            }
                        }
                    });
        }

    // ham dang ki bang email
        public void signUpEmail() {
            String email1 = getIntent().getStringExtra(Register.emailValue);
            String pass = getIntent().getStringExtra(Register.passValue);
            mAuth.createUserWithEmailAndPassword(email1, pass)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(Verify.this, "Dang ky thanh cong!!", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Verify.this, LogIn.class);
                                startActivity(intent);
                            } else {
                                Toast.makeText(Verify.this, "Loi!!!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

        public void setEvent(){
            reSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    verifyPhoneNumber();
                }
            });
        }
    }
