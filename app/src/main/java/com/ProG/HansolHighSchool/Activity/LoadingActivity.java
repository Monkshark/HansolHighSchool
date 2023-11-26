package com.ProG.HansolHighSchool.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;

import com.ProG.HansolHighSchool.R;

public class LoadingActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        startLoading();
    }
    private void startLoading() {
        Handler handler = new Handler();
        handler.postDelayed(this :: finish, 500);
    }

}