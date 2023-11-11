package com.ProG.HansolHighSchool.Adapter;

import java.util.Calendar;
import java.util.Date;

public class CalendarUtil {
    public static android.icu.util.Calendar selectedDate;

    public static boolean isWeekends(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK);

        return dayOfWeek == Calendar.SATURDAY || dayOfWeek == Calendar.SUNDAY;
    }
}
