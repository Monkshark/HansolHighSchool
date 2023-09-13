package com.ProG.HansolHighSchool.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ProG.HansolHighSchool.Data.SpinnerData;
import com.ProG.HansolHighSchool.Fragment.HomeFragment;
import com.ProG.HansolHighSchool.R;

public class SettingsActivity extends Activity {

    Spinner sp_grade, sp_className;
    Button btn_save;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);

        this.setFinishOnTouchOutside(true);
        overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.80);
        int height = (int) (dm.heightPixels * 0.85);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        sp_grade = findViewById(R.id.sp_grade);
        sp_className = findViewById(R.id.sp_className);
        btn_save = findViewById(R.id.btn_save);

        ArrayAdapter<CharSequence> grade = ArrayAdapter.createFromResource(this, R.array.grade, android.R.layout.simple_spinner_dropdown_item);
        grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_grade.setAdapter(grade);

        ArrayAdapter<CharSequence> className = ArrayAdapter.createFromResource(this, R.array.className, android.R.layout.simple_spinner_dropdown_item);
        grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_className.setAdapter(className);

        sp_grade.setSelection(SpinnerData.spinnerGrade);
        sp_className.setSelection(SpinnerData.spinnerClass);

        btn_save.setOnClickListener(v -> {
            SpinnerData.spinnerGrade = (int) sp_grade.getSelectedItemId();
            SpinnerData.spinnerClass = (int) sp_className.getSelectedItemId();
            Log.e("spinner", SpinnerData.spinnerGrade + " " + SpinnerData.spinnerClass);
            HomeFragment.setTimetable();
            finish();
        });


    }
}