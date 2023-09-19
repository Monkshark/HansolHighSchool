package com.ProG.HansolHighSchool.Data;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ProG.HansolHighSchool.API.getMealData;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    Date currentDate = new Date();
    String spDate = sf.format(currentDate);

    @Override
    public void onReceive(Context context, Intent intent) {

        String 분류 = intent.getStringExtra("분류");

        assert 분류 != null;
        String 메뉴 = switch (분류) {
                case "조식" -> getMealData.getMeal(spDate, "1", "메뉴");
                case "중식" -> getMealData.getMeal(spDate, "2", "메뉴");
                case "석식" -> getMealData.getMeal(spDate, "3", "메뉴");
            };

    }
}
