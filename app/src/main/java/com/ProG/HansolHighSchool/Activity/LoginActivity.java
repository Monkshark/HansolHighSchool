package com.ProG.HansolHighSchool.Activity;

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

import java.util.Objects;

public class LoginActivity extends Activity {

    String schoolNum, password, tryPassword, name;
    EditText et_schoolNum, et_password;
    Button btn_login, btn_register;

    FirebaseDatabase FBRead;
    DatabaseReference FBWrite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        FBRead = FirebaseDatabase.getInstance();
        FBWrite = FirebaseDatabase.getInstance().getReference();

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_login);

        overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.85);
        int height = (int) (dm.heightPixels * 0.85);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        /*
        FBRead.getReference("users").child("11111").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull
                DataSnapshot dataSnapshot) {
                String a = dataSnapshot.getValue(String.class);
                if (Objects.equals(a, "a")) {
                    Toast.makeText(LoginActivity.this, "데이터 가져오기 성공 " + a, Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(LoginActivity.this,"데이터 가져오기 실패 " + a, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e( "LoginActivity" , "Failed to read value.", databaseError.toException());
            }
        });*/

        /* FBWrite.child("users").child("11111").setValue("a"); /* 데이터 쓰기 */


        {
            et_schoolNum = findViewById(R.id.et_phoneNum);
            et_password = findViewById(R.id.et_name);
            btn_login = findViewById(R.id.btn_login);
            btn_register = findViewById(R.id.btn_register);

            schoolNum = et_schoolNum.getText().toString();
            password = et_password.getText().toString();
        }


        btn_login.setOnClickListener(v -> {

            try {
                schoolNum = et_schoolNum.getText().toString();
                password = et_password.getText().toString();

                FBRead.getReference("users").child(schoolNum).child("password").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull
                        DataSnapshot dataSnapshot) {
                        tryPassword = dataSnapshot.getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e( "LoginActivity" , "Failed to read value.", databaseError.toException());
                        Toast.makeText(LoginActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
            } catch (Exception e) {
                Log.e("LoginActivity", "Error: " + e.getMessage());
                Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }


            if (Objects.equals(password, tryPassword)) {
                LoginData.isLogin = true;
                LoginData.schoolNum = schoolNum;
                LoginData.password = password;
                FBRead.getReference("users").child(schoolNum).child("name").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull
                        DataSnapshot dataSnapshot) {
                        name = dataSnapshot.getValue(String.class);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e( "LoginActivity" , "Failed to read value.", databaseError.toException());
                        Toast.makeText(LoginActivity.this, "Failed to read value.", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });

                LoginData.name = name;
                LoginData.grade = schoolNum.substring(0, 1);
                LoginData.classNum = schoolNum.substring(2, 3);

                Toast.makeText(LoginActivity.this, "로그인 성공", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "로그인 실패", Toast.LENGTH_SHORT).show();
            }
        });

        btn_register.setOnClickListener(v -> {
            Intent intentActivity = new Intent(LoginActivity.this, RegisterActivity.class);
            startActivity(intentActivity);
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
    }
}
