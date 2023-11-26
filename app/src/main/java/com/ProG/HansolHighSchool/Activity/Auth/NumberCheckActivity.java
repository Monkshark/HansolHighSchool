package com.ProG.HansolHighSchool.Activity.Auth;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.ProG.HansolHighSchool.R;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class NumberCheckActivity extends Activity {

    private EditText et_phoneNum, et_numDoubleCheck, et_checkCode;
    private Button btn_registerAfterCheck, btn_sendCode, btn_checkCode;
    private TextView tv_checkCodeView;

    private FirebaseAuth mAuth;
    private String randomSendNum;
    private String verificationId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_number_check);
        this.setFinishOnTouchOutside(true);

        overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit);

        initializeViews();
        initializeFirebase();
        setWindowSize();

        btn_sendCode.setOnClickListener(v -> {
            String phoneNum = et_phoneNum.getText().toString();
            String numDoubleCheck = et_numDoubleCheck.getText().toString();
            String checkCode = et_checkCode.getText().toString();

            if (validatePhoneNumbers(phoneNum, numDoubleCheck)) {
                String formattedNum = "+82" + phoneNum.substring(1, 10);
                runOnUiThread(() ->
                        Toast.makeText(NumberCheckActivity.this, "reCAPTCHA가 진행중입니다. 잠시만 기다려주세요", Toast.LENGTH_SHORT).show());
                startPhoneNumberVerification(formattedNum);
            } else {
                tv_checkCodeView.setText("전화번호를 확인해주세요");
                tv_checkCodeView.setTextColor(Color.parseColor("#FF0000"));
            }
        });

        btn_checkCode.setOnClickListener(v -> {
            String checkCode = et_checkCode.getText().toString();
            if (checkCode.equals(randomSendNum)) {
                tv_checkCodeView.setText("전화번호 인증에 성공하였습니다");
                tv_checkCodeView.setTextColor(Color.parseColor("#00FF00"));
            } else {
                tv_checkCodeView.setText("전화번호 인증에 실패하였습니다");
                tv_checkCodeView.setTextColor(Color.parseColor("#FF0000"));
            }
        });

        btn_registerAfterCheck.setOnClickListener(v -> {
            if (tv_checkCodeView.getCurrentTextColor() == Color.parseColor("#00FF00")) {
                // TODO: 회원가입 로직 구현 필요
            } else {
                tv_checkCodeView.setText("전화번호 인증을 진행해주세요");
                tv_checkCodeView.setTextColor(Color.parseColor("#FF0000"));
            }
        });
    }

    private void initializeViews() {
        et_phoneNum = findViewById(R.id.et_phoneNum);
        et_numDoubleCheck = findViewById(R.id.et_name);
        et_checkCode = findViewById(R.id.et_checkCode);
        btn_registerAfterCheck = findViewById(R.id.btn_registerAfterCheck);
        btn_sendCode = findViewById(R.id.btn_sendCode);
        btn_checkCode = findViewById(R.id.btn_checkCode);
        tv_checkCodeView = findViewById(R.id.tv_checkCodeView);
    }

    private void initializeFirebase() {
        mAuth = FirebaseAuth.getInstance();
    }

    private void setWindowSize() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.85);
        int height = (int) (dm.heightPixels * 0.85);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
    }

    private boolean validatePhoneNumbers(String phoneNum, String numDoubleCheck) {
        return phoneNum.equals(numDoubleCheck) && phoneNum.length() == 11;
    }

    private void startPhoneNumberVerification(String phoneNumber) {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(mAuth)
                        .setPhoneNumber(phoneNumber)
                        .setTimeout(60L, TimeUnit.SECONDS)
                        .setActivity(this)
                        .setCallbacks(mCallbacks)
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        // TODO: PhoneAuthCredential을 사용하여 인증 완료 후의 로직 구현
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentActivity = new Intent(NumberCheckActivity.this, RegisterActivity.class);
        startActivity(intentActivity);
        finish();
    }

    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }

    private final PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
            signInWithPhoneAuthCredential(phoneAuthCredential);
        }

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {
                Log.e("NumberCheckActivity", "Invalid phone number");
                Toast.makeText(NumberCheckActivity.this, "잘못된 전화번호입니다.", Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Log.e("NumberCheckActivity", "SMS is too many requests");
                Toast.makeText(NumberCheckActivity.this, "일일 인증 한도를 초과하였습니다.", Toast.LENGTH_SHORT).show();
            } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                Log.e("NumberCheckActivity", "Missing Activity for reCAPTCHA");
                Toast.makeText(NumberCheckActivity.this, "reCAPTCHA 위한 액티비티가 누락되었습니다.", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            NumberCheckActivity.this.verificationId = verificationId;
            Toast.makeText(NumberCheckActivity.this, "인증 코드가 전송되었습니다.", Toast.LENGTH_SHORT).show();
            randomSendNum = generateRandomCode();
            Toast.makeText(NumberCheckActivity.this, randomSendNum, Toast.LENGTH_LONG).show();
        }
    };
}
