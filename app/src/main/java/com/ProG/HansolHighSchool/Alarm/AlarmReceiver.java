package com.ProG.HansolHighSchool.Alarm;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ProG.HansolHighSchool.API.GetMealData;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class AlarmReceiver extends BroadcastReceiver {
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
    Date currentDate = new Date();
    String spDate = sf.format(currentDate);

    @Override
    public void onReceive(Context context, Intent intent) {
        String 분류 = intent.getStringExtra("분류");
        final String[] 메뉴 = new String[1];
        메뉴[0] = getMenu(분류, spDate).join();
    }

    public CompletableFuture<String> getMenu(String 분류, String spDate) {
        return switch (Objects.requireNonNull(분류)) {
            case "조식" -> GetMealData.getMeal(spDate, "1", "메뉴");
            case "중식" -> GetMealData.getMeal(spDate, "2", "메뉴");
            case "석식" -> GetMealData.getMeal(spDate, "3", "메뉴");
            default -> CompletableFuture.completedFuture("");
        };
    }
}
