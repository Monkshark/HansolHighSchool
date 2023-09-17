package com.ProG.HansolHighSchool.Fragment;

import static com.ProG.HansolHighSchool.Data.SettingData.spinnerMealScCode;
import static com.ProG.HansolHighSchool.Data.URLLibrary.URL_HansolHS;
import static com.ProG.HansolHighSchool.Data.URLLibrary.URL_RiroSchool;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.ProG.HansolHighSchool.API.getMealData;
import com.ProG.HansolHighSchool.API.getTimetableData;
import com.ProG.HansolHighSchool.Activity.AccountInfoActivity;
import com.ProG.HansolHighSchool.Activity.LoginActivity;
import com.ProG.HansolHighSchool.Activity.SettingsActivity;
import com.ProG.HansolHighSchool.Data.LoginData;
import com.ProG.HansolHighSchool.Data.SettingData;
import com.ProG.HansolHighSchool.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class HomeFragment extends Fragment {

    Button btn_hansolhs, btn_riroschool, btn_account, btn_setting;
    @SuppressLint("StaticFieldLeak")
    static TextView tv_meal;
    @SuppressLint("StaticFieldLeak")
    static TextView tv_timetable;
    static Date currentDate = new Date();
    @SuppressLint("SimpleDateFormat")
    private static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        btn_hansolhs = view.findViewById(R.id.btn_hansolhs);
        btn_riroschool = view.findViewById(R.id.btn_riroschool);
        btn_account = view.findViewById(R.id.btn_account);
        btn_setting = view.findViewById(R.id.btn_setting);
        tv_meal = view.findViewById(R.id.tv_meal);
        tv_timetable = view.findViewById(R.id.tv_timetable);

        String crdate = dateFormat.format(currentDate);

        btn_setting.setOnClickListener(v -> {
            Intent intentActivity = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intentActivity);
        });

        btn_hansolhs.setOnClickListener(v -> {
            Intent intentURL = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_HansolHS));
            startActivity(intentURL);
        });

        btn_riroschool.setOnClickListener(v -> {
            Intent intentURL = new Intent(Intent.ACTION_VIEW, Uri.parse(URL_RiroSchool));
            startActivity(intentURL);
        });

        btn_account.setOnClickListener(v -> {

            Intent intentActivity;
            if (LoginData.isLogin) {
                intentActivity = new Intent(getActivity(), AccountInfoActivity.class);
            } else {
                intentActivity = new Intent(getActivity(), LoginActivity.class);
            }
            startActivity(intentActivity);
        });

        setTimetable();
        return view;
    }
    @SuppressLint("SetTextI18n")
    public static void setTimetable() {

        String crdate = dateFormat.format(currentDate);
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(currentDate);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        if(dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY) {
            tv_meal.setText(crdate.substring(0, 4) + "년 " +
                    crdate.substring(4, 6) + "월 " +
                    crdate.substring(6, 8) + "일\n" +
                    codeToString(spinnerMealScCode) + "정보\n\n" +
                    "정보 없음");
            tv_timetable.setText(crdate.substring(0, 4) + "년 " +
                    crdate.substring(4, 6) + "월 " +
                    crdate.substring(6, 8) + "일\n" +
                    (SettingData.spinnerGrade + 1) + "학년 " +
                    (SettingData.spinnerClass + 1) + "반 " +
                    "시간표\n\n" + "정보 없음");
        } else {
            tv_meal.setText(crdate.substring(0, 4) + "년 " +
                    crdate.substring(4, 6) + "월 " +
                    crdate.substring(6, 8) + "일\n" +
                    codeToString(spinnerMealScCode) + "정보\n\n" +
                    getMealData.getMeal(crdate, String.valueOf((spinnerMealScCode + 1)), "메뉴"));
                    tv_timetable.setText(getTimetableData.getTimeTable(crdate,
                    String.valueOf(SettingData.spinnerGrade + 1),
                    String.valueOf(SettingData.spinnerClass + 1)
            ));
        }
    }

    private static String codeToString(int code) {
        return switch (code) {
            case 0 -> "조식";
            case 1 -> "중식";
            case 2 -> "석식";
            default -> "메뉴";
        };
    }

}