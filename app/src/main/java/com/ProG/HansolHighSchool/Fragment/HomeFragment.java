package com.ProG.HansolHighSchool.Fragment;

import static com.ProG.HansolHighSchool.Data.URLLibrary.URL_HansolHS;
import static com.ProG.HansolHighSchool.Data.URLLibrary.URL_RiroSchool;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.ProG.HansolHighSchool.API.GetMealData;
import com.ProG.HansolHighSchool.API.GetTimetableData;
import com.ProG.HansolHighSchool.Activity.Auth.AccountInfoActivity;
import com.ProG.HansolHighSchool.Activity.Auth.LoginActivity;
import com.ProG.HansolHighSchool.Activity.MainActivity;
import com.ProG.HansolHighSchool.Activity.SettingsActivity;
import com.ProG.HansolHighSchool.Alarm.FirebaseMessaging;
import com.ProG.HansolHighSchool.Data.LoginData;
import com.ProG.HansolHighSchool.Data.NetworkStatus;
import com.ProG.HansolHighSchool.Data.SettingData;
import com.ProG.HansolHighSchool.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    ImageButton btn_hansolhs, btn_riroschool, btn_account, btn_setting;
    @SuppressLint("StaticFieldLeak")
    TextView tv_meal, tv_timetable, tv_date, tv_mealScCode, tv_className;
    Date currentDate = new Date();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
    LoginData loginData;

    @Override
    public void onResume() {
        super.onResume();
        checkNotificationPermission();
        setMainData();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        initializeViews(view);
        FirebaseMessaging fm = new FirebaseMessaging();
        fm.setAlarms(requireContext());
        checkBatteryOptimization(requireContext());

        if (NetworkStatus.isConnected(requireContext())) {
            setMainData();
        } else {
            tv_meal.setText("네트워크 연결을 확인해주세요");
            tv_timetable.setText("네트워크 연결을 확인해주세요");
        }

        return view;
    }

    private void initializeViews(View view) {
        btn_hansolhs = view.findViewById(R.id.btn_hansolhs);
        btn_riroschool = view.findViewById(R.id.btn_riroschool);
        btn_account = view.findViewById(R.id.btn_account);
        btn_setting = view.findViewById(R.id.btn_setting);
        tv_meal = view.findViewById(R.id.tv_meal);
        tv_timetable = view.findViewById(R.id.tv_timetable);
        tv_date = view.findViewById(R.id.tv_date);
        tv_className = view.findViewById(R.id.tv_className);
        tv_mealScCode = view.findViewById(R.id.tv_mealScCode);
        loginData = LoginData.getInstance(requireContext());

        btn_setting.setOnClickListener(v -> {
            Intent intentActivity = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intentActivity);
        });

        btn_hansolhs.setOnClickListener(v -> {
            Intent intentURL = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_HansolHS));
            startActivity(intentURL);
        });

        btn_riroschool.setOnClickListener(v ->
                MainActivity.openExternalApp(getActivity(), "com.riroschool.riroschool", URL_RiroSchool)
        );

        btn_account.setOnClickListener(v -> {
            Intent intentActivity;
            if (loginData.isLogin()) {
                intentActivity = new Intent(getActivity(), AccountInfoActivity.class);
            } else {
                intentActivity = new Intent(getActivity(), LoginActivity.class);
            }
            startActivity(intentActivity);
        });
    }

    @SuppressLint("SetTextI18n")
    public void setMainData() {
        Context context = getContext();
        if (context == null) {
            Log.e("HomeFragment", "Context is null");
            return;
        }

        if (!NetworkStatus.isConnected(requireContext())) {
            tv_meal.setText("네트워크 연결을 확인해주세요");
            tv_timetable.setText("네트워크 연결을 확인해주세요");
            return;
        }

        Calendar currentTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String formattedDate = sdf.format(currentTime.getTime());
        int time = Integer.parseInt(formattedDate.replace(":", ""));
        if (time > 200000) {
            currentTime.add(Calendar.DATE, 1);
            currentDate = currentTime.getTime();
        }
        String crdate = dateFormat.format(currentDate);
        currentTime.setTime(currentDate);
        int dayOfWeek = currentTime.get(Calendar.DAY_OF_WEEK);

        tv_mealScCode.setText(codeToString(SettingData.getSpinnerMealScCode(context)));
        tv_className.setText((SettingData.getSpinnerGrade(context) + 1) + "-" + (SettingData.getSpinnerClass(context) + 1) + " 시간표");
        tv_date.setText(crdate.substring(0, 4) + "년 " +
                crdate.substring(4, 6) + "월 " +
                crdate.substring(6, 8) + "일");

        if (dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            tv_meal.setText("정보 없음");
            tv_timetable.setText("정보 없음");
        } else {
            String.valueOf(GetMealData.getMeal(crdate, String.valueOf((SettingData.getSpinnerMealScCode(context) + 1)), "메뉴").thenApplyAsync(mealInfo -> {
                tv_meal.setText(deleteBracket(mealInfo));
                return null;
            }));
            GetTimetableData.getTimeTable(
                    crdate,
                    String.valueOf(SettingData.getSpinnerGrade(context) + 1),
                    String.valueOf(SettingData.getSpinnerClass(context) + 1),
                    tv_timetable);
        }
    }

    private String codeToString(int code) {
        return switch (code) {
            case 0 -> "조식";
            case 1 -> "중식";
            case 2 -> "석식";
            default -> "메뉴";
        };
    }

    public void checkNotificationPermission() {
        NotificationManager manager = (NotificationManager) requireContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (!manager.areNotificationsEnabled()) {
            Toast.makeText(requireContext(), "알림 권한을 허용해주세요.", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                    .putExtra(Settings.EXTRA_APP_PACKAGE, requireContext().getPackageName());
            startActivity(intent);
        }
    }

    @SuppressLint({"ObsoleteSdkInt", "BatteryLife"})
    private void checkBatteryOptimization(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            String packageName = context.getPackageName();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            if (!pm.isIgnoringBatteryOptimizations(packageName)) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
                intent.setData(Uri.parse("package:" + packageName));
                try {
                    context.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(context, "배터리 최적화 화면을 열 수 없습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private String deleteBracket(String msg) {
        msg = msg.replaceAll("[().1234567890]", "");
        return msg;
    }
}
