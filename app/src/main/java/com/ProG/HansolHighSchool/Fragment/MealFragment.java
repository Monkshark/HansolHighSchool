package com.ProG.HansolHighSchool.Fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ProG.HansolHighSchool.API.getMealData;
import com.ProG.HansolHighSchool.Activity.MealInfoActivity;
import com.ProG.HansolHighSchool.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MealFragment extends Fragment {

    TextView tv_breakfast, tv_lunch, tv_dinner, tv_naljja, tv_breakfastKcal, tv_lunchKcal, tv_dinnerKcal;
    ImageButton btn_pre, btn_next;
    Date currentDate = new Date();
    private static final String TAG = "MealFragment";

    @SuppressLint("SimpleDateFormat")
    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.meal_fragment, container, false);

        tv_breakfast = view.findViewById(R.id.tv_breakfast);
        tv_lunch = view.findViewById(R.id.tv_lunch);
        tv_dinner = view.findViewById(R.id.tv_dinner);
        tv_naljja = view.findViewById(R.id.tv_naljja);
        tv_breakfastKcal = view.findViewById(R.id.tv_breakfastKcal);
        tv_lunchKcal = view.findViewById(R.id.tv_lunchKcal);
        tv_dinnerKcal = view.findViewById(R.id.tv_dinnerKcal);

        // 24시간 * 60분 * 60초 * 1000밀리초 = 1일 = 86400000밀리초
        int dayToMs = 24 * 60 * 60 * 1000;

        btn_pre = view.findViewById(R.id.btn_pre);
        btn_next = view.findViewById(R.id.btn_next);

        Log.e(TAG, "onCreateView: " + currentDate.toString());
        Intent intent = new Intent(getActivity(), MealInfoActivity.class);
        MealInfoActivity mia = new MealInfoActivity();

        tv_breakfast.setOnClickListener(v -> {
            intent.putExtra("mealInfo", getMealData.getMeal(dateFormat.format(currentDate), "1", "영양정보"));
            startActivity(intent);
        });

        tv_lunch.setOnClickListener(v -> {
            intent.putExtra("mealInfo", getMealData.getMeal(dateFormat.format(currentDate), "2", "영양정보"));
            startActivity(intent);
        });

        tv_dinner.setOnClickListener(v -> {
            intent.putExtra("mealInfo", getMealData.getMeal(dateFormat.format(currentDate), "3", "영양정보"));
            startActivity(intent);
        });

        btn_pre.setOnClickListener(v -> {

            int attemptCount = 0;
            Date originalDate = currentDate;
            currentDate = new Date(currentDate.getTime() - dayToMs);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            while ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                    || isAllMealsEmpty(dateFormat.format(currentDate))) {
                if (attemptCount >= 5) {
                    Log.e(TAG, "급식 정보 없음");
                    currentDate = originalDate;
                    break;
                }

                currentDate = new Date(currentDate.getTime() - dayToMs);
                calendar.setTime(currentDate);
                attemptCount++;
            }

            updateMealDate(currentDate);
        });


        btn_next.setOnClickListener(v -> {

            int attemptCount = 0;
            Date originalDate = currentDate;
            currentDate = new Date(currentDate.getTime() + dayToMs);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            while ((calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calendar.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                    || isAllMealsEmpty(dateFormat.format(currentDate))) {
                if (attemptCount >= 4) {
                    Log.e(TAG, "급식 정보 없음");
                    currentDate = originalDate;
                    break;
                }

                currentDate = new Date(currentDate.getTime() + dayToMs);
                calendar.setTime(currentDate);
                attemptCount++;
            }

            updateMealDate(currentDate);
        });

        updateMealDate(currentDate);

        if ((Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || Calendar.getInstance().get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY)
                || isAllMealsEmpty(dateFormat.format(currentDate))) {
            tv_breakfast.setText("정보 없음");
            tv_lunch.setText("정보 없음");
            tv_dinner.setText("정보 없음");
            tv_breakfastKcal.setText("");
            tv_lunchKcal.setText("");
            tv_dinnerKcal.setText("");
        }

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void updateMealDate(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = dateFormat.format(date);
        String dateToShow = formattedDate.substring(0,4)
                + "-" + formattedDate.substring(4,6)
                + "-" + formattedDate.substring(6,8);
        tv_naljja.setText(dateToShow + " 급식식단");

        getMealTask(formattedDate);
    }

    public void getMealTask(String date) {

        String breakfast = getMealData.getMeal(date, "1", "메뉴");
        String breakfastKcal = getMealData.getMeal(date,"1" , "칼로리");
        tv_breakfast.setText(breakfast);
        tv_breakfastKcal.setText(breakfastKcal);

        String lunch = getMealData.getMeal(date, "2", "메뉴");
        String lunchKcal = getMealData.getMeal(date, "2", "칼로리");
        tv_lunch.setText(lunch);
        tv_lunchKcal.setText(lunchKcal);

        String dinner = getMealData.getMeal(date, "3", "메뉴");
        String dinnerKcal = getMealData.getMeal(date, "3", "칼로리");
        if (!dinner.contains("null") || !dinnerKcal.contains("null")) {
            tv_dinner.setText(dinner);
            tv_dinnerKcal.setText(dinnerKcal);
        } else {
            tv_dinner.setText("급식 정보 없음");
            tv_dinnerKcal.setText("");
        }
    }

    private boolean isAllMealsEmpty(String date) {
        String breakfast = getMealData.getMeal(date, "1", "메뉴");
        String lunch = getMealData.getMeal(date, "2", "메뉴");
        String dinner = getMealData.getMeal(date, "3", "메뉴");

        return (breakfast == null || breakfast.contains("null"))
                && (lunch == null || lunch.contains("null"))
                && (dinner == null || dinner.contains("null"));

    }

}
