package com.ProG.HansolHighSchool.Activity.Auth;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ProG.HansolHighSchool.Data.LoginData;
import com.ProG.HansolHighSchool.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends Activity {

    private EditText et_schoolNum, et_password;
    private Button btn_login, btn_register;

    private FirebaseDatabase firebaseRead;
    LoginData loginData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);
        overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit);
        loginData = LoginData.getInstance(this);

        initializeViews();
        initializeFirebase();
        setWindowSize();

        btn_login.setOnClickListener(v -> {
            String schoolNum = String.valueOf(et_schoolNum.getText());
            String password = String.valueOf(et_password.getText());

            if (validateInputs(schoolNum, password)) {
                authenticateUser(schoolNum, password);
            } else {
                Toast.makeText(LoginActivity.this, "학번과 비밀번호를 입력해주세요.", Toast.LENGTH_SHORT).show();
            }
        });

        btn_register.setOnClickListener(v -> {
            Intent intentActivity = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intentActivity);
            finish();
        });
    }

    private void initializeViews() {
        et_schoolNum = findViewById(R.id.et_phoneNum);
        et_password = findViewById(R.id.et_name);
        btn_login = findViewById(R.id.btn_login);
        btn_register = findViewById(R.id.btn_register);
    }

    private void initializeFirebase() {
        firebaseRead = FirebaseDatabase.getInstance();
    }

    private void setWindowSize() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.85);
        int height = (int) (dm.heightPixels * 0.85);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
    }

    private boolean validateInputs(String schoolNum, String password) {
        return !(schoolNum.isEmpty() && !password.isEmpty());
    }

    private void authenticateUser(String schoolNum, String password) {
        DatabaseReference usersRef = firebaseRead.getReference("users").child(schoolNum);
        usersRef.child("password").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String tryPassword = dataSnapshot.getValue(String.class);
                if (password.equals(tryPassword)) {
                    loginData.setIsLogin(true);
                    loginData.setSchoolNum(schoolNum);

                    usersRef.child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String name = dataSnapshot.getValue(String.class);
                            loginData.setName(name);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Log.e("LoginActivity", "Failed to read name.", databaseError.toException());
                            Toast.makeText(LoginActivity.this, "Failed to read name.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    });

                    loginData.setGrade(schoolNum.substring(0, 1));
                    loginData.setClassNum(schoolNum.substring(2, 3));

                    Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("LoginActivity", "Failed to read password.", databaseError.toException());
                Toast.makeText(LoginActivity.this, "Failed to read password.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}
