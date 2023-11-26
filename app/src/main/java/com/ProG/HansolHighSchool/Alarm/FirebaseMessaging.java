package com.ProG.HansolHighSchool.Alarm;

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

    @SuppressLint({"NewApi", "ScheduleExactAlarm"})
    public void setAlarms(@NonNull Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar currentCalendar = Calendar.getInstance();
        int dayOfWeek = currentCalendar.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            return;
        }

        PendingIntent pendingIntent1 = createPendingIntent(context, 1, "조식", 6, 30);
        PendingIntent pendingIntent2 = createPendingIntent(context, 2, "중식", 12, 0);
        PendingIntent pendingIntent3 = createPendingIntent(context, 3, "석식", 17, 0);

        Calendar calendar1 = getAlarmTime(6, 30);
        Calendar calendar2 = getAlarmTime(12, 0);
        Calendar calendar3 = getAlarmTime(17, 0);

        if (alarmManager != null) {
            Log.e(TAG, "조식정보 알림 발송");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar1.getTimeInMillis(), pendingIntent1);
            Log.e(TAG, "중식정보 알림 발송");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar2.getTimeInMillis(), pendingIntent2);
            Log.e(TAG, "석식정보 알림 발송");
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar3.getTimeInMillis(), pendingIntent3);
        }
    }

    private Calendar getAlarmTime(int hourOfDay, int minute) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return calendar;
    }



    private PendingIntent createPendingIntent(Context context, int requestCode, String category, int hourOfDay, int minute) {
        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("분류", category);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        return PendingIntent.getBroadcast(context, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
    }

    private long getAlarmTimeInMillis(Calendar calendar) {
        return calendar.getTimeInMillis();
    }
}
