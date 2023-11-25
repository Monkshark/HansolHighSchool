package com.ProG.HansolHighSchool.Fragment;

import static com.ProG.HansolHighSchool.Adapter.CalendarUtil.isWeekends;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.ProG.HansolHighSchool.API.GetMealData;
import com.ProG.HansolHighSchool.Activity.MealInfoActivity;
import com.ProG.HansolHighSchool.Data.NetworkStatus;
import com.ProG.HansolHighSchool.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class MealFragment extends Fragment {

    TextView tv_breakfast, tv_lunch, tv_dinner, tv_naljja, tv_breakfastKcal, tv_lunchKcal, tv_dinnerKcal;
    ImageButton btn_pre, btn_next;
    Date currentDate = new Date();
    int oneDayInMs = 24 * 60 * 60 * 1000;
    // 24시간 * 60분 * 60초 * 1000밀리초 = 86400000밀리초 = 1일

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

        btn_pre = view.findViewById(R.id.btn_pre);
        btn_next = view.findViewById(R.id.btn_next);

        tv_breakfast.setMovementMethod(new ScrollingMovementMethod());
        tv_lunch.setMovementMethod(new ScrollingMovementMethod());
        tv_dinner.setMovementMethod(new ScrollingMovementMethod());


        Intent intent = new Intent(getActivity(), MealInfoActivity.class);

        tv_breakfast.setOnClickListener(v ->
                GetMealData.getMeal(dateFormat.format(currentDate), "1", "영양정보").thenAccept(mealInfo -> {
            intent.putExtra("mealInfo", mealInfo);
            startActivity(intent);
        }));

        tv_lunch.setOnClickListener(v ->
                GetMealData.getMeal(dateFormat.format(currentDate), "2", "영양정보").thenAccept(mealInfo -> {
            intent.putExtra("mealInfo", mealInfo);
            startActivity(intent);
        }));

        tv_dinner.setOnClickListener(v ->
                GetMealData.getMeal(dateFormat.format(currentDate), "3", "영양정보").thenAccept(mealInfo -> {
            intent.putExtra("mealInfo", mealInfo);
            startActivity(intent);
        }));

        btn_pre.setOnClickListener(v ->
                getPastMeal().thenRun(() -> updateMealDate(currentDate)));
        btn_next.setOnClickListener(v ->
                getFutureMeal().thenRun(() -> updateMealDate(currentDate)));

        if (isWeekends(currentDate)) {
            tv_breakfast.setText("정보 없음");
            tv_lunch.setText("정보 없음");
            tv_dinner.setText("정보 없음");
            tv_breakfastKcal.setText("");
            tv_lunchKcal.setText("");
            tv_dinnerKcal.setText("");
            getFutureMeal();
        } else {
            isAllMealsEmpty(dateFormat.format(currentDate)).thenAccept(isEmpty -> {
                if (isEmpty) {
                    tv_breakfast.setText("정보 없음");
                    tv_lunch.setText("정보 없음");
                    tv_dinner.setText("정보 없음");
                    tv_breakfastKcal.setText("");
                    tv_lunchKcal.setText("");
                    tv_dinnerKcal.setText("");
                    getFutureMeal();
                }
            });
        }

        updateMealDate(currentDate);

        return view;
    }

    @SuppressLint("SetTextI18n")
    private void updateMealDate(Date date) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        String formattedDate = dateFormat.format(date);
        String dateToShow = formattedDate.substring(0, 4)
                + "-" + formattedDate.substring(4, 6)
                + "-" + formattedDate.substring(6, 8);
        tv_naljja.setText(dateToShow);

        if (NetworkStatus.isConnected(requireContext())) {
            getMealTask(formattedDate);
        } else {
            tv_breakfast.setText("네트워크 연결을 확인해주세요");
            tv_lunch.setText("네트워크 연결을 확인해주세요");
            tv_dinner.setText("네트워크 연결을 확인해주세요");
            tv_breakfastKcal.setText("");
            tv_lunchKcal.setText("");
            tv_dinnerKcal.setText("");
        }
    }

    public void getMealTask(String date) {
        tv_breakfast.setText("정보 불러오는중...");
        tv_lunch.setText("정보 불러오는중...");
        tv_dinner.setText("정보 불러오는중...");
        tv_breakfastKcal.setText("");
        tv_lunchKcal.setText("");
        tv_dinnerKcal.setText("");
        tv_breakfast.requestLayout();
        tv_lunch.requestLayout();
        tv_dinner.requestLayout();

        GetMealData.getMeal(date, "1", "메뉴").thenAcceptAsync(breakfast -> {
            tv_breakfast.setText(breakfast);
            tv_breakfast.requestLayout();
        }, mainThreadExecutor());

        GetMealData.getMeal(date, "1", "칼로리").thenAcceptAsync(breakfastKcal -> {
            tv_breakfastKcal.setText(breakfastKcal);
            tv_breakfastKcal.requestLayout();
        }, mainThreadExecutor());

        GetMealData.getMeal(date, "2", "메뉴").thenAcceptAsync(lunch -> {
            tv_lunch.setText(lunch);
            tv_lunch.requestLayout();
        }, mainThreadExecutor());

        GetMealData.getMeal(date, "2", "칼로리").thenAcceptAsync(lunchKcal -> {
            tv_lunchKcal.setText(lunchKcal);
            tv_lunchKcal.requestLayout();
        }, mainThreadExecutor());

        GetMealData.getMeal(date, "3", "메뉴").thenAcceptAsync(dinner -> {
            if (dinner != null && !dinner.contains("null")) {
                tv_dinner.setText(dinner);
            } else {
                tv_dinner.setText("급식 정보 없음");
            }
            tv_dinner.requestLayout();
        }, mainThreadExecutor());

        GetMealData.getMeal(date, "3", "칼로리").thenAcceptAsync(dinnerKcal -> {
            if (dinnerKcal != null && !dinnerKcal.contains("null")) {
                tv_dinnerKcal.setText(dinnerKcal);
            } else {
                tv_dinnerKcal.setText("");
            }
            tv_dinnerKcal.requestLayout();
        }, mainThreadExecutor());

    }

    public CompletableFuture<Boolean> isAllMealsEmpty(String date) {
        CompletableFuture<String> breakfast = GetMealData.getMeal(date, "1", "메뉴");
        CompletableFuture<String> lunch = GetMealData.getMeal(date, "2", "메뉴");
        CompletableFuture<String> dinner = GetMealData.getMeal(date, "3", "메뉴");

        return CompletableFuture.allOf(breakfast, lunch, dinner).thenApply(v ->
                (breakfast.join() == null || breakfast.join().contains("null")) &&
                        (lunch.join() == null || lunch.join().contains("null")) &&
                        (dinner.join() == null || dinner.join().contains("null"))
        );
    }

    private CompletableFuture<Void> getFutureMeal() {
        int attemptCount = 0;
        Date originalDate = currentDate;

        while (true) {
            currentDate = new Date(currentDate.getTime() + oneDayInMs);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            while (isWeekends(currentDate)) {
                currentDate = new Date(currentDate.getTime() + oneDayInMs);
                calendar.setTime(currentDate);
            }

            attemptCount++;

            if (attemptCount > 5) {
                currentDate = originalDate;
                break;
            }

            try {
                boolean isEmpty = isAllMealsEmpty(dateFormat.format(currentDate)).get();
                if (!isEmpty) {
                    requireActivity().runOnUiThread(() -> updateMealDate(currentDate));
                    break;
                }
            } catch (Exception e) {
                currentDate = originalDate;
                break;
            }
        }

        return CompletableFuture.completedFuture(null);
    }


    private CompletableFuture<Void> getPastMeal() {
        int attemptCount = 0;
        Date originalDate = currentDate;

        while (true) {
            currentDate = new Date(currentDate.getTime() - oneDayInMs);
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(currentDate);

            while (isWeekends(currentDate)) {
                currentDate = new Date(currentDate.getTime() - oneDayInMs);
                calendar.setTime(currentDate);
            }

            attemptCount++;

            if (attemptCount > 5) {
                currentDate = originalDate;
                break;
            }

            try {
                boolean isEmpty = isAllMealsEmpty(dateFormat.format(currentDate)).get();
                if (!isEmpty) {
                    requireActivity().runOnUiThread(() -> updateMealDate(currentDate));
                    break;
                }
            } catch (Exception e) {
                currentDate = originalDate;
                break;
            }
        }

        return CompletableFuture.completedFuture(null);
    }

    public Executor mainThreadExecutor() {
        return command -> new Handler(Looper.getMainLooper()).post(command);
    }

}