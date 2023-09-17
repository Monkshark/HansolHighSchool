package com.ProG.HansolHighSchool.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.ProG.HansolHighSchool.R;

public class RegisterActivity extends Activity {

    String schoolNum, name, password, pwdDoubleCheck;
    boolean teacher;
    EditText et_schoolNum, et_name, et_password, et_pwdDoubleCheck;
    Button btn_numberCheck;
    CheckBox cb_registerStudent, cb_registerTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_register);

        this.setFinishOnTouchOutside(true);
        overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.85);
        int height = (int) (dm.heightPixels * 0.85);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        cb_registerStudent = findViewById(R.id.cb_registerStudent);
        cb_registerTeacher = findViewById(R.id.cb_registerTeacher);
        et_schoolNum = findViewById(R.id.et_schoolNum);
        et_name = findViewById(R.id.et_name);
        et_password = findViewById(R.id.et_password);
        et_pwdDoubleCheck = findViewById(R.id.et_pwdDoubleCheck);
        btn_numberCheck = findViewById(R.id.btn_numberCheck);

        et_schoolNum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});

        cb_registerStudent.setOnClickListener(v -> {
            cb_registerTeacher.setChecked(false);
            et_schoolNum.setInputType(InputType.TYPE_CLASS_NUMBER);
            et_schoolNum.setHint("학번을 입력해주세요");
            teacher = false;
            et_schoolNum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(5)});
        });

        cb_registerTeacher.setOnClickListener(v -> {
            cb_registerStudent.setChecked(false);
            et_schoolNum.setInputType(InputType.TYPE_CLASS_TEXT);
            et_schoolNum.setHint("아이디를 입력해주세요");
            teacher = true;
            et_schoolNum.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        });

        btn_numberCheck.setOnClickListener(v -> {

                schoolNum = et_schoolNum.getText().toString();
                name = et_name.getText().toString();
                password = et_password.getText().toString();
                pwdDoubleCheck = et_pwdDoubleCheck.getText().toString();

            if (cb_registerStudent.isChecked() || cb_registerTeacher.isChecked()) {
                if (schoolNum.isEmpty() || name.isEmpty() || password.isEmpty() || pwdDoubleCheck.isEmpty()) {
                    Toast.makeText(RegisterActivity.this, "빈 칸을 모두 채워주세요", Toast.LENGTH_SHORT).show();
                } else {
                    if (cb_registerStudent.isChecked() && schoolNum.length() == 5) {
                        if (password.equals(pwdDoubleCheck)) {
                            Intent intentActivity = new Intent(RegisterActivity.this, NumberCheckActivity.class);

                            intentActivity.putExtra("schoolNum", schoolNum);
                            intentActivity.putExtra("name", name);
                            intentActivity.putExtra("password", password);
                            intentActivity.putExtra("teacher", teacher);
                            startActivity(intentActivity);
                            finish();
                        } else {
                            Toast.makeText(RegisterActivity.this, "비밀번호를 확인해주세요", Toast.LENGTH_SHORT).show();
                        }
                    } else if (cb_registerStudent.isChecked()) {
                        Toast.makeText(RegisterActivity.this, "학번은 5글자를 입력해주세요", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                Toast.makeText(RegisterActivity.this, "체크박스를 선택해주세요", Toast.LENGTH_SHORT).show();
            }
        });
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
        Intent intentActivity = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intentActivity);
        finish();
    }
}
