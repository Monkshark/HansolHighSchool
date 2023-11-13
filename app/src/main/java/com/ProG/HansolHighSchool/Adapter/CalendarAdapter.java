package com.ProG.HansolHighSchool.Adapter;

import static com.ProG.HansolHighSchool.Fragment.NoticeFragment.tv_flDate;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ProG.HansolHighSchool.API.getNoticeData;
import com.ProG.HansolHighSchool.R;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.calendarViewHolder> {

    ArrayList<Date> dayList;
    private View previousView = null;

    public CalendarAdapter(ArrayList<Date> dayList) {
        this.dayList = dayList;

    }

    @NonNull
    @Override
    public calendarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        View view = inflater.inflate(R.layout.calendar_cell, parent, false);

        return new calendarViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull calendarViewHolder holder, int position) {

        Date monthDate = dayList.get(position);

        Calendar dateCalendar = Calendar.getInstance();

        dateCalendar.setTime(monthDate);

        int currentDay = CalendarUtil.selectedDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = CalendarUtil.selectedDate.get(Calendar.MONTH) + 1;
        int currentYear = CalendarUtil.selectedDate.get(Calendar.YEAR);

        int displayDay = dateCalendar.get(Calendar.DAY_OF_MONTH);
        int displayMonth = dateCalendar.get(Calendar.MONTH) + 1;
        int displayYear = dateCalendar.get(Calendar.YEAR);

        if (displayMonth == currentMonth && displayYear == currentYear) {
            holder.tv_day.setTextColor(Color.parseColor("#ffffff"));
            holder.tv_day.setAlpha((float)0.9);
            /* 투명도 1.0(불투명) ~ 0.0(투명) */

        } else {
            holder.tv_day.setTextColor(Color.parseColor("#BEBEBE"));
            holder.tv_day.setAlpha((float)0.2);
        }

        int dayNo = dateCalendar.get(Calendar.DAY_OF_MONTH);

        holder.tv_day.setText(String.valueOf(dayNo));


        if ((position + 1) % 7 == 0) {
            holder.tv_day.setTextColor(Color.BLUE);
        } else if (position % 7 == 0) {

            holder.tv_day.setTextColor(Color.RED);
        }

        holder.itemView.setOnClickListener(v -> {

            String selectedYMD = displayYear + "-" + displayMonth + "-" + displayDay;
            String Date = "" + displayYear + displayMonth+ displayDay;
            String NoticeData = getNoticeData.getNotice(Date);

            tv_flDate.setText(selectedYMD + "\n" + NoticeData);

            if (previousView != null) {
                previousView.setBackgroundResource(0);
            }
            holder.parentView.setBackgroundResource(R.drawable.calendar_tdr);
            previousView = holder.parentView;
        });

    }


    @Override
    public int getItemCount() {
        return dayList.size();
    }

    static class calendarViewHolder extends RecyclerView.ViewHolder {

        TextView tv_day;
        View parentView;

        public calendarViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_day = itemView.findViewById(R.id.tv_day);
            parentView = itemView.findViewById(R.id.parentView);

        }
    }


}