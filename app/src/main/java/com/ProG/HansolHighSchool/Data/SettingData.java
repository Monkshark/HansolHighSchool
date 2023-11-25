package com.ProG.HansolHighSchool.Data;

import android.content.Context;
import android.content.SharedPreferences;

public class SettingData {
    private static final String PREFERENCES_NAME = "com.ProG.HansolHighSchool.Data";
    private static final int DEFAULT_VALUE_INT = 0;

    private static SharedPreferences getPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE);
    }

    public static void setSpinnerGrade(Context context, int input) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("spinnerGrade", input);
        editor.apply();
    }

    public static int getSpinnerGrade(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getInt("spinnerGrade", DEFAULT_VALUE_INT);
    }

    public static void setSpinnerClass(Context context, int input) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("spinnerClass", input);
        editor.apply();
    }

    public static int getSpinnerClass(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getInt("spinnerClass", DEFAULT_VALUE_INT);
    }

    public static void setSpinnerMealScCode(Context context, int input) {
        SharedPreferences prefs = getPreferences(context);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putInt("spinnerMealScCode", input);
        editor.apply();
    }

    public static int getSpinnerMealScCode(Context context) {
        SharedPreferences prefs = getPreferences(context);
        return prefs.getInt("spinnerMealScCode", DEFAULT_VALUE_INT);
    }
}
