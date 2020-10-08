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
    EditText email,password,repassword;
    public static final String emailValue = "EMAILVALUE";
    public static final String passValue = "PASSLVALUE";
   // public static final String phoneValue = "PHONEVALUE";
    private FirebaseAuth mAuth;
    private FirebaseUser mCurrentUser;
 //   private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


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
      //  phonenumber = (EditText)findViewById(R.id.etxt_phone);
        error = (TextView)findViewById(R.id.txt_error);
    }

    public boolean showError(){
        String Email = email.getText().toString();
        String Password = password.getText().toString();
        String Repassword = repassword.getText().toString();
       // String Phonenumber = phonenumber.getText().toString();
        if(Email.isEmpty()||Password.isEmpty()||Repassword.isEmpty())
            return true;
        return false;
    }
    public boolean showWrongRepass(){
        String Password = password.getText().toString();
        String Repassword = repassword.getText().toString();
        if(!Password.equals(Repassword))
            return true;
        return false;
    }
    public void setAction(){
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // String phone_number ="+84" + phonenumber.getText().toString();
                String Email = email.getText().toString();
                String Password = password.getText().toString();
                if(showError())
                error.setText("Something is empty!");
                if (showWrongRepass())
                    error.setText("Repassword not true!");
                else {
                    Intent intent = new Intent(Register.this, Verify.class);
                  //  intent.putExtra(phoneValue,phone_number);
                    intent.putExtra(emailValue,Email);
                    intent.putExtra(passValue,Password);
                    startActivity(intent);
                }
            }
        });
    }
  /*  private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            sendUserToHome();
                            // ...
                        } else {
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                mLoginFeedbackText.setVisibility(View.VISIBLE);
                                mLoginFeedbackText.setText("There was an error verifying OTP");
                            }
                        }
                        mLoginProgress.setVisibility(View.INVISIBLE);
                        mGenerateBtn.setEnabled(true);
                    }
                });
    }*/
}