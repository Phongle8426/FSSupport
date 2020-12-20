package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
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
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

public class Verify extends AppCompatActivity {
    EditText code;
    TextView reSend;
    Button verify;
    ProgressDialog loadingProcess;
    private String phonenumber;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        mAuth = FirebaseAuth.getInstance(); // bien authentication
        mDatabase = FirebaseDatabase.getInstance().getReference();
        AnhXa();
        phonenumber = getIntent().getStringExtra(Register.phoneValue); // lay phone tu register
        verifyPhoneNumber(); // goi ham xu ly xac thuc phone
        setEvent(); // goi ham xu ly RESEND CODE
    }

    public void AnhXa(){
        code = (EditText) findViewById(R.id.etxt_code);
        reSend = (TextView) findViewById(R.id.txt_resendCode);
        verify = findViewById(R.id.btn_verify);
        loadingProcess = new ProgressDialog(this);
    }
    // Show Progress Dialog Verification
    public void contentOfLoading(){
        loadingProcess.setTitle("Phone Verification");
        loadingProcess.setMessage("Please wait...while we are authenticating your phone number..");
        loadingProcess.setCanceledOnTouchOutside(false);
        loadingProcess.show();
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
                        Toast.makeText(Verify.this, "This phone number exits!", Toast.LENGTH_LONG).show();
                    }

                    // khi ma nhap dung code thi thuc hien dang ki email
                    @Override
                    public void onCodeSent(final String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                        super.onCodeSent(verificationId, forceResendingToken);
                        verify.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                contentOfLoading();
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
                                pushPhoneNumber(phonenumber);
                                loadingProcess.dismiss();
                                signUpEmail();

                            } else {
                                if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                    Toast.makeText(Verify.this, "There was an error verify OTP", Toast.LENGTH_LONG).show();
                                }
                            }
                        }
                    });
        }

     // ham luu sdt duoc dang ki vao DB
     public void pushPhoneNumber(String phonenumber){
        mDatabase.child("PhoneNumber").push().child("phone").setValue(phonenumber);
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
