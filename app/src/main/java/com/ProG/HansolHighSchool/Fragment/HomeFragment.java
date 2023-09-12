package com.ProG.HansolHighSchool.Fragment;

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
import com.ProG.HansolHighSchool.Data.LoginData;
import com.ProG.HansolHighSchool.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class HomeFragment extends Fragment {

    Button btn_hansolhs, btn_riroschool, btn_account;
    TextView tv_meal, tv_timetable;
    Date currentDate = new Date();
    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        btn_hansolhs = view.findViewById(R.id.btn_hansolhs);
        btn_riroschool = view.findViewById(R.id.btn_riroschool);
        btn_account = view.findViewById(R.id.btn_account);
        tv_meal = view.findViewById(R.id.tv_meal);
        tv_timetable = view.findViewById(R.id.tv_timetable);

        String crdate = dateFormat.format(currentDate);

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

        tv_meal.setText(crdate.substring(0, 4) + "년 " +
                crdate.substring(4, 6) + "월 " +
                crdate.substring(6, 8) + "일\n" +
                "급식정보\n\n" + getMealData.getMeal(crdate, "2", "메뉴"));
        tv_timetable.setText(getTimetableData.getTimeTable(crdate, "1", "6"));

        return view;
    }

}
