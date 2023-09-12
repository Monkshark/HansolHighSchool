package com.ProG.HansolHighSchool.Activity;

import static android.content.ContentValues.TAG;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
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

    String phoneNum, numDoubleCheck, checkCode;
    boolean check = false;
    String verificationId;

    String randomSendNum = generateRandomCode();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_number_check);

        this.setFinishOnTouchOutside(true);

        overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.85);
        int height = (int) (dm.heightPixels * 0.85);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
    }
        /*팝업*/

        {
            EditText et_phoneNum, et_numDoubleCheck, et_checkCode;

            Button btn_registerAfterCheck, btn_sendCode, btn_checkCode;

            TextView tv_checkCodeView;

            tv_checkCodeView = findViewById(R.id.tv_checkCodeView);

            et_phoneNum = findViewById(R.id.et_phoneNum);
            et_numDoubleCheck = findViewById(R.id.et_name);
            et_checkCode = findViewById(R.id.et_checkCode);

            btn_registerAfterCheck = findViewById(R.id.btn_registerAfterCheck);
            btn_sendCode = findViewById(R.id.btn_sendCode);
            btn_checkCode = findViewById(R.id.btn_checkCode);

            Intent getInfo = getIntent();

            String schoolNum = getInfo.getStringExtra("schoolNum");
            String name = getInfo.getStringExtra("name");
            String password = getInfo.getStringExtra("password");
            String teacher = getInfo.getStringExtra("teacher");

            FirebaseAuth mAuth;

            mAuth = FirebaseAuth.getInstance();

            btn_sendCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    phoneNum = et_phoneNum.getText().toString();
                    numDoubleCheck = et_numDoubleCheck.getText().toString();
                    checkCode = et_checkCode.getText().toString();

                    if (phoneNum.equals(numDoubleCheck) && phoneNum.length() == 11) {

                        String formattedNum = "+82" + phoneNum.substring(1, 10);
                        /* 010 1234 5678  ->  +8210 1234 5678 */

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(NumberCheckActivity.this, "reCAPTCHA가 진행중입니다. 잠시만 기다려주세요", Toast.LENGTH_SHORT).show();
                            }
                        });
                        PhoneAuthOptions options =
                                PhoneAuthOptions.newBuilder(mAuth)
                                        .setPhoneNumber(formattedNum)
                                        .setTimeout(60L, TimeUnit.SECONDS)
                                        .setActivity(NumberCheckActivity.this)
                                        .setCallbacks(mCallbacks)
                                        .build();
                        PhoneAuthProvider.verifyPhoneNumber(options);

                    } else if (phoneNum.isEmpty() || numDoubleCheck.isEmpty()) {
                        tv_checkCodeView.setText("전화번호를 입력해주세요");
                        tv_checkCodeView.setTextColor(Color.parseColor("#FF0000"));
                    } else {
                        tv_checkCodeView.setText("전화번호를 확인해주세요");
                        tv_checkCodeView.setTextColor(Color.parseColor("#FF0000"));
                    }
                }
            });

            btn_checkCode.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    checkCode = et_checkCode.getText().toString();
                    if (checkCode.equals(randomSendNum)) {
                        check = true;
                        tv_checkCodeView.setText("전화번호 인증에 성공하였습니다");
                        tv_checkCodeView.setTextColor(Color.parseColor("#00FF00"));
                    } else {
                        check = false;
                        tv_checkCodeView.setText("전화번호 인증에 실패하였습니다");
                        tv_checkCodeView.setTextColor(Color.parseColor("#FF0000"));
                    }

                }
            });

            btn_registerAfterCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (check == true) {
                        /* 회원가입 로직 구현 필요 */

                    } else {
                        tv_checkCodeView.setText("전화번호 인증을 진행해주세요");
                        tv_checkCodeView.setTextColor(Color.parseColor("#FF0000"));

                    }


                }
            });
        }
        /*변수선언, 바인딩, 버튼클릭*/

    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_OUTSIDE) {
            return true;
        }
        return super.dispatchTouchEvent(ev);
    }
    /*팝업 밖 터치시 꺼짐 방지*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intentActivity = new Intent(NumberCheckActivity.this, RegisterActivity.class);
        startActivity(intentActivity);
        finish();
    }
    /*뒤로가기 방지*/

    private String generateRandomCode() {
        Random random = new Random();
        StringBuilder code = new StringBuilder();
        for (int i = 0; i < 6; i++) {
            code.append(random.nextInt(10));
        }
        return code.toString();
    }
    /*6자리 숫자 랜덤코드 생성*/

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

        }
        /* 전화인증 성공시 로직 구현 */

        @Override
        public void onVerificationFailed(@NonNull FirebaseException e) {
            if (e instanceof FirebaseAuthInvalidCredentialsException) {

                Log.e(TAG, "Invalid phone number");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NumberCheckActivity.this, "잘못된 전화번호 입니다", Toast.LENGTH_SHORT).show();
                    }
                });
            } else if (e instanceof FirebaseTooManyRequestsException) {
                Log.e(TAG, "SMS is too many requests");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NumberCheckActivity.this, "일일 인증 한도를 초과하였습니다.", Toast.LENGTH_SHORT).show();
                        Toast.makeText(NumberCheckActivity.this, randomSendNum, Toast.LENGTH_LONG).show();

                    }

                });

            } else if (e instanceof FirebaseAuthMissingActivityForRecaptchaException) {
                Log.e(TAG, "Missing Activity for reCAPTCHA");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(NumberCheckActivity.this, "Missing Activity for reCAPTCHA", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
        /*전화인증 실패시 로직 구현*/

        @Override
        public void onCodeSent(@NonNull String verificationId, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            Log.d(TAG, "onCodeSent: " + verificationId);
            NumberCheckActivity.this.verificationId = verificationId;
            Toast.makeText(NumberCheckActivity.this, "인증 코드가 전송되었습니다.", Toast.LENGTH_SHORT).show();
            Toast.makeText(NumberCheckActivity.this, randomSendNum, Toast.LENGTH_LONG).show();

        }
    };

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
    }
}
