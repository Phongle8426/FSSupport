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
    EditText code,phone;
    TextView error;
    Button reSend,recieve;
    ImageButton verify;
    private String mAuthVerificationId;
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        mAuth = FirebaseAuth.getInstance();
        mCurrentUser = mAuth.getCurrentUser();
  //      mAuthVerificationId = getIntent().getStringExtra("AuthCredentials");
        AnhXa();
        setEvent();
    }

    public void AnhXa(){
        code = (EditText) findViewById(R.id.etxt_code);
        phone = (EditText)findViewById(R.id.etxt_phone);
        reSend = (Button) findViewById(R.id.btn_resendCode);
        recieve = (Button) findViewById(R.id.btn_recieve);
        verify = (ImageButton)findViewById(R.id.btn_verify);
        error = (TextView)findViewById(R.id.txt_error);
    }

    public void setEvent() {
        recieve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumber = phone.getText().toString();
                if (phoneNumber.isEmpty())
                    Toast.makeText(Verify.this, "Enter your phone number", Toast.LENGTH_SHORT).show();
                else {
                    //verify phone number
                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+84" + phoneNumber, 60, TimeUnit.SECONDS, Verify.this,
                            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                                @Override
                                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                                    signInUser(phoneAuthCredential);
                                }

                                @Override
                                public void onVerificationFailed(@NonNull FirebaseException e) {
                                    error.setText("failed.");
                                }

                                @Override
                                public void onCodeSent(final String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                    super.onCodeSent(verificationId, forceResendingToken);
                                    verify.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            String verificationCode = code.getText().toString();
                                            if (verificationId.isEmpty()) return;
                                            //create a credential
                                            PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, verificationCode);
                                            signInUser(credential);
                                        }
                                    });

                                }
                            });
                }
            }
        });
    }

        private void signInUser (PhoneAuthCredential credential){
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(Verify.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                getValue();
                            } else {
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    error.setVisibility(View.VISIBLE);
                                    error.setText("There was an error verifying OTP");
                                }
                            }
                        }
                    });
        }
        public void getValue () {
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

    }
