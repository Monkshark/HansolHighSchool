package com.ProG.HansolHighSchool.Alert;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@SuppressLint("MissingFirebaseInstanceTokenRefresh")
public class FirebaseMessaging extends FirebaseMessagingService {

    final static String TAG = "FirebaseMessaging";
    Date currentDate = new Date();
    @SuppressLint("SimpleDateFormat")
    SimpleDateFormat sf = new SimpleDateFormat("yyyyMMdd");
    String spDate = sf.format(currentDate);

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.d(TAG, "From: " + remoteMessage.getFrom());

        if (remoteMessage.getData().size() > 0) {
            Log.d(TAG, "Message data payload: " + remoteMessage.getData());
        }

        if (remoteMessage.getNotification() != null) {
            Log.d(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        }
        sendNotification(remoteMessage.getFrom(), remoteMessage.getNotification().getBody());

    }

    private void sendNotification(String from, String body) {

        new Handler(Looper.getMainLooper()).post(() ->
                Toast.makeText(getApplicationContext(), from + " -> " + body, Toast.LENGTH_LONG).show()
        );
    }

    @SuppressLint("ScheduleExactAlarm")
    public void setAlarms(@NonNull Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar currentCalendar = Calendar.getInstance();
        int dayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
        if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            return;
        }

        Intent intent1 = new Intent(context, AlertReceiver.class);
        intent1.putExtra("분류", "조식");

        Calendar calendar1 = Calendar.getInstance();
        calendar1.setTimeInMillis(System.currentTimeMillis());
        calendar1.set(Calendar.HOUR_OF_DAY, 6);
        calendar1.set(Calendar.MINUTE, 30);

        PendingIntent pendingIntent1 = PendingIntent.getBroadcast(context, 1, intent1, PendingIntent.FLAG_IMMUTABLE);
        if (calendar1.before(Calendar.getInstance())) {
            calendar1.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (alarmManager != null) {
            Log.e(TAG, "조식정보 알림 발송");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
        }

        Intent intent2 = new Intent(context, AlertReceiver.class);
        intent2.putExtra("분류", "중식");

        Calendar calendar2 = Calendar.getInstance();
        calendar2.setTimeInMillis(System.currentTimeMillis());
        calendar2.set(Calendar.HOUR_OF_DAY, 12);
        calendar2.set(Calendar.MINUTE, 0);

        PendingIntent pendingIntent2 = PendingIntent.getBroadcast(context, 2, intent2, PendingIntent.FLAG_IMMUTABLE);
        if (calendar2.before(Calendar.getInstance())) {
            calendar2.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (alarmManager != null) {
            Log.e(TAG, "중식정보 알림 발송");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);
        }

        Intent intent3 = new Intent(context, AlertReceiver.class);
        intent3.putExtra("분류", "석식");

        Calendar calendar3 = Calendar.getInstance();
        calendar3.setTimeInMillis(System.currentTimeMillis());
        calendar3.set(Calendar.HOUR_OF_DAY, 17);
        calendar3.set(Calendar.MINUTE, 0);

        PendingIntent pendingIntent3 = PendingIntent.getBroadcast(context, 3, intent3, PendingIntent.FLAG_IMMUTABLE);
        if (calendar3.before(Calendar.getInstance())) {
            calendar3.add(Calendar.DAY_OF_MONTH, 1);
        }
        if (alarmManager != null) {
            Log.e(TAG, "석식정보 알림 발송");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent3);
        }
    }
}
