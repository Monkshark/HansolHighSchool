package com.ProG.HansolHighSchool.Activity;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
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
    private Spinner sp_grade, sp_className, sp_mealScCode;
    private Button btn_save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_settings);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        setWindowProperties();

        sp_grade = findViewById(R.id.sp_grade);
        sp_className = findViewById(R.id.sp_className);
        sp_mealScCode = findViewById(R.id.sp_mealScCode);
        btn_save = findViewById(R.id.btn_save);

        setupSpinners();

        btn_save.setOnClickListener(v -> {
            saveSpinnerSettings();
            updateHomeFragmentData();
            finish();
        });
    }

    private void setWindowProperties() {
        this.setFinishOnTouchOutside(true);
        overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.80);
        int height = (int) (dm.heightPixels * 0.85);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;
    }

    private void setupSpinners() {
        ArrayAdapter<CharSequence> gradeAdapter = ArrayAdapter.createFromResource(this, R.array.grade, android.R.layout.simple_spinner_dropdown_item);
        gradeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_grade.setAdapter(gradeAdapter);

        ArrayAdapter<CharSequence> classNameAdapter = ArrayAdapter.createFromResource(this, R.array.className, android.R.layout.simple_spinner_dropdown_item);
        classNameAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_className.setAdapter(classNameAdapter);

        ArrayAdapter<CharSequence> mealScCodeAdapter = ArrayAdapter.createFromResource(this, R.array.mealScCode, android.R.layout.simple_spinner_dropdown_item);
        mealScCodeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_mealScCode.setAdapter(mealScCodeAdapter);

        sp_grade.setSelection(SettingData.getSpinnerGrade(this));
        sp_className.setSelection(SettingData.getSpinnerClass(this));
        sp_mealScCode.setSelection(SettingData.getSpinnerMealScCode(this));
    }

    private void saveSpinnerSettings() {
        SettingData.setSpinnerGrade(this, (int) sp_grade.getSelectedItemId());
        SettingData.setSpinnerClass(this, (int) sp_className.getSelectedItemId());
        SettingData.setSpinnerMealScCode(this, (int) sp_mealScCode.getSelectedItemId());
        Log.e("spinner", SettingData.getSpinnerGrade(this) + " " + SettingData.getSpinnerClass(this));
    }

    private void updateHomeFragmentData() {
        HomeFragment homeFragment = MainActivity.getHomeFragment();
        if (homeFragment.isAdded()) {
            homeFragment.setMainData();
        } else {
            Log.e("SettingsActivity", "HomeFragment is not attached to Activity");
        }
    }
}
