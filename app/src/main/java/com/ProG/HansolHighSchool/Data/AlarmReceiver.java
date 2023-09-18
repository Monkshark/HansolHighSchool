package com.ProG.HansolHighSchool.Data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.ProG.HansolHighSchool.Adapter.NotificationUtil;

import java.util.Objects;

public class AlarmReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        String 분류 = intent.getStringExtra("분류");
        String 메뉴 = intent.getStringExtra("메뉴");

        switch (Objects.requireNonNull(분류)) {
            case "조식" -> NotificationUtil.sendNotification(context, "조식정보", 메뉴);
            case "중식" -> NotificationUtil.sendNotification(context, "중식정보", 메뉴);
            case "석식" -> NotificationUtil.sendNotification(context, "석식정보", 메뉴);
        }


    }
}
