package com.ProG.HansolHighSchool.Activity.Auth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.ProG.HansolHighSchool.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class EmailCheckActivity extends Activity {

    String schoolNum, name, password, email, emailCheck;
    boolean isTeacher;
    private Button btn_sendCode, btn_sendCheckEmail, btn_register;
    private EditText et_email,et_emailCheck , et_checkCode;
    private TextView tv_authValue;
    private final FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
    private final FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private final DatabaseReference databaseReference = firebaseDatabase.getReference();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_check);

        initializeViews();
        setWindowSize();
        getIntentValue();

        btn_sendCheckEmail.setOnClickListener(v -> {
            email = String.valueOf(et_email.getText());
            emailCheck = String.valueOf(et_emailCheck.getText());

            if (!email.equals(emailCheck)) {
                tv_authValue.setText("이메일이 일치하지 않습니다.");
                tv_authValue.setTextColor(getResources().getColor(R.color.Red));
                return;
            }
            tv_authValue.setText("이메일을 확인해주세요");
            tv_authValue.setTextColor(Color.YELLOW);

            firebaseAuth.createUserWithEmailAndPassword(email, "TempPassword1234")
                    .addOnCompleteListener(EmailCheckActivity.this, tempTask -> {
                        if (tempTask.isSuccessful()) {
                            FirebaseUser tempUser = firebaseAuth.getCurrentUser();
                            if(tempUser != null) return;
                            tempUser.sendEmailVerification()
                                    .addOnCompleteListener(verifyTask -> {
                                        if (!verifyTask.isSuccessful()) {
                                            Toast.makeText(EmailCheckActivity.this, "인증 이메일 발송 실패: " + verifyTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                            return;
                                        }
                                        tempUser.delete()
                                                .addOnCompleteListener(deleteTask -> {
                                                    if (!deleteTask.isSuccessful()) return;
                                                    firebaseAuth.createUserWithEmailAndPassword(email, password)
                                                            .addOnCompleteListener(EmailCheckActivity.this, task -> {
                                                                if (!task.isSuccessful()) return;
                                                                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                                                                FirebaseDatabase database = FirebaseDatabase.getInstance();
                                                                DatabaseReference myRef = database.getReference("users").child(tempUser.getUid());
                                                                HashMap<String, String> userData = new HashMap<>();
                                                                userData.put("이메일", email);
                                                                userData.put("비밀번호", password);
                                                                userData.put("학번", schoolNum);
                                                                userData.put("이름", name);
                                                                userData.put("선생님", String.valueOf(isTeacher));

                                                                myRef.setValue(userData);
                                                                Toast.makeText(EmailCheckActivity.this, "인증 이메일을 확인해 주세요.", Toast.LENGTH_SHORT).show();
                                                            });
                                                });
                                    });
                        }
                    });
        });
    }

    private void initializeViews() {
        btn_sendCheckEmail = findViewById(R.id.btn_sendCheckEmail);
        btn_register = findViewById(R.id.btn_register);
        et_email = findViewById(R.id.et_email);
        et_emailCheck = findViewById(R.id.et_emailCheck);
        tv_authValue = findViewById(R.id.tv_authValue);
    }

    private void setWindowSize() {
        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.85);
        int height = (int) (dm.heightPixels * 0.85);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
    }

    private void getIntentValue() {
        Intent intent = getIntent();
        schoolNum = intent.getStringExtra("schoolNum");
        name = intent.getStringExtra("name");
        password = intent.getStringExtra("password");
        isTeacher = intent.getBooleanExtra("teacher", false);
    }
}
