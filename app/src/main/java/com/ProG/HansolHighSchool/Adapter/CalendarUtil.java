package com.ProG.HansolHighSchool.Adapter;

import android.icu.util.Calendar;

public class CalendarUtil {
    public static Calendar selectedDate;

    public static boolean isWeekends() {
        return java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SATURDAY
                || java.util.Calendar.getInstance().get(java.util.Calendar.DAY_OF_WEEK) == java.util.Calendar.SUNDAY;
    }


}
