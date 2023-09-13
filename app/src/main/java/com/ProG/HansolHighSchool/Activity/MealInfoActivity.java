package com.ProG.HansolHighSchool.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ProG.HansolHighSchool.R;

public class MealInfoActivity extends Activity {

    TextView tv_mealInfo;
    Button btn_close;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_meal_info);

        this.setFinishOnTouchOutside(true);
        overridePendingTransition(R.anim.popup_enter, R.anim.popup_exit);

        DisplayMetrics dm = getApplicationContext().getResources().getDisplayMetrics();
        int width = (int) (dm.widthPixels * 0.80);
        int height = (int) (dm.heightPixels * 0.85);
        getWindow().getAttributes().width = width;
        getWindow().getAttributes().height = height;

        tv_mealInfo = findViewById(R.id.tv_mealInfo);
        btn_close = findViewById(R.id.btn_close);

        btn_close.setOnClickListener(this :: onClick);

//        tv_mealInfo.setText("씨발");
        Intent intent = getIntent();
        String mealInfo = intent.getStringExtra("mealInfo");
        tv_mealInfo.setText(mealInfo);

    }

    private void onClick(View v) {
        finish();
    }
}