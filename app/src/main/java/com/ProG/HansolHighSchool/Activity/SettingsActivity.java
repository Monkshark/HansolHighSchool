package com.ProG.HansolHighSchool.Activity;


import android.app.Activity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.ProG.HansolHighSchool.Data.SettingData;
import com.ProG.HansolHighSchool.Fragment.HomeFragment;
import com.ProG.HansolHighSchool.R;

public class SettingsActivity extends Activity {
    Spinner sp_grade, sp_className, sp_mealScCode;
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
        sp_mealScCode = findViewById(R.id.sp_mealScCode);
        btn_save = findViewById(R.id.btn_save);

        ArrayAdapter<CharSequence> grade = ArrayAdapter.createFromResource(this, R.array.grade, android.R.layout.simple_spinner_dropdown_item);
        grade.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_grade.setAdapter(grade);

        ArrayAdapter<CharSequence> className = ArrayAdapter.createFromResource(this, R.array.className, android.R.layout.simple_spinner_dropdown_item);
        className.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_className.setAdapter(className);

        ArrayAdapter<CharSequence> mealScCode = ArrayAdapter.createFromResource(this, R.array.mealScCode, android.R.layout.simple_spinner_dropdown_item);
        mealScCode.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_mealScCode.setAdapter(mealScCode);

        sp_grade.setSelection(SettingData.getSpinnerGrade(this));
        sp_className.setSelection(SettingData.getSpinnerClass(this));
        sp_mealScCode.setSelection(SettingData.getSpinnerMealScCode(this));

        btn_save.setOnClickListener(v -> {
            SettingData.setSpinnerGrade(this, (int) sp_grade.getSelectedItemId());
            SettingData.setSpinnerClass(this, (int) sp_className.getSelectedItemId());
            SettingData.setSpinnerMealScCode(this, (int) sp_mealScCode.getSelectedItemId());
            Log.e("spinner", SettingData.getSpinnerGrade(this) + " " + SettingData.getSpinnerClass(this));
            HomeFragment homeFragment = MainActivity.getHomeFragment();
            if (homeFragment.isAdded()) {
                homeFragment.setTimetable();
            } else {
                Log.e("SettingsActivity", "HomeFragment is not attached to Activity");
            }
            finish();
        });

    }
}
