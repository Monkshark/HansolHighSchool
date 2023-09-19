package com.ProG.HansolHighSchool.Data;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ProG.HansolHighSchool.Adapter.NotificationUtil;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String 분류 = intent.getStringExtra("분류");
        String 메뉴 = intent.getStringExtra("메뉴");

        Date currentDate = new Date();
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
        String spDate = sf.format(currentDate);

        if (분류 != null) switch (분류) {
            case "조식" -> NotificationUtil.sendNotification(context, spDate + " 조식정보", 메뉴);
            case "중식" -> NotificationUtil.sendNotification(context, spDate + " 중식정보", 메뉴);
            case "석식" -> NotificationUtil.sendNotification(context, spDate + " 석식정보", 메뉴);
        }

    }
}
