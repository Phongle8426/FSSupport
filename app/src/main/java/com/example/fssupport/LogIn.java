package com.example.fssupport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LogIn extends AppCompatActivity {

    EditText email,password; // field nhập email và password
    CheckBox rememberMe; // check box remember me
    TextView error,goRegis,forgotPass; // TextView hiển thị lỗi, link đến Register, link đến ForgotPassword
    Button login; // Button login
    ProgressBar progress; // hiển thị progressbar
    ImageView imgShowPass; // show/ hide password
    private boolean isShowPass = false; // ban đầu để password hide
    FirebaseAuth mAuth; // khai báo Authentication của Firebase
    public static final String MyPREFERENCES = "MyPrefs"; //biến lưu của cặp user/password
    public static final String USERNAME = "userNameKey"; // biến lưu username
    public static final String PASS = "passKey";        // biến lưu password
    public static final String REMEMBER = "remember";  // biến lưu lựa chọn remember me
    SharedPreferences sharedpreferences; // khai báo sharedpreference để lưu cặp user/passs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);
        mAuth =FirebaseAuth.getInstance();
        sharedpreferences = getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        AnhXa();
        loadData();
        setEvent();
        showPassword(isShowPass); // khi khởi động pass sẽ hide
    }

    // hàm thực hiện chức năng Login vào hệ thống
    public void Login(){
        final String Email = email.getText().toString(); // lấy mail
        final String Password = password.getText().toString(); // lấy pass
        if (Email.isEmpty() || Password.isEmpty()){        // Nếu email hay pass trống thì thực hiện
            error.setText("Something is empty!");
            progress.setVisibility(View.INVISIBLE);         // báo lỗi
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
                                progress.setVisibility(View.INVISIBLE);
                            }
                        }
                    });
        }
    }

    // Bắt sự kiện các nút bấm
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
                Intent intent_toToRegister = new Intent(LogIn.this,Register.class);
                startActivity(intent_toToRegister);
            }
        });

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent_goToForgotPassword = new Intent(LogIn.this,ForgotPassword.class);
                startActivity(intent_goToForgotPassword);
            }
        });

        imgShowPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isShowPass = !isShowPass;
                showPassword(isShowPass);
            }
        });

    }


    public void showPassword(boolean isShow){
        if(isShow){
            password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
            imgShowPass.setImageResource(R.drawable.hide);
        }else{
            password.setTransformationMethod(PasswordTransformationMethod.getInstance());
            imgShowPass.setImageResource(R.drawable.show);
        }
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
        login = (Button)findViewById(R.id.btn_login);
        error = (TextView) findViewById(R.id.txt_error);
        rememberMe = (CheckBox)findViewById(R.id.cb_remember);
        forgotPass = (TextView)findViewById(R.id.btn_forgot);
        progress = (ProgressBar) findViewById(R.id.progress);
        progress.setVisibility(View.INVISIBLE);
        imgShowPass = (ImageView)findViewById(R.id.img_password);
    }
}