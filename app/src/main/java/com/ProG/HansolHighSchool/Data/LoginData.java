package com.ProG.HansolHighSchool.Data;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginData {
    private static SharedPreferences sharedPref;
    private static final String PREF_NAME = "LoginData";

    public static void init(Context context) {
        if (sharedPref == null) {
            sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        }
    }

    private static void checkInit() {
        if (sharedPref == null) {
            throw new IllegalStateException("LoginData is not initialized, call init(Context) method first.");
        }
    }

    public static void setSchoolNum(String value) {
        checkInit();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("schoolNum", value);
        editor.apply();
    }

    public static String getSchoolNum() {
        checkInit();
        return sharedPref.getString("schoolNum", "");
    }

    public static void setName(String value) {
        checkInit();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", value);
        editor.apply();
    }

    public static String getName() {
        checkInit();
        return sharedPref.getString("name", "");
    }

    public static void setPassword(String value) {
        checkInit();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("password", value);
        editor.apply();
    }

    public static String getPassword() {
        checkInit();
        return sharedPref.getString("password", "");
    }

    public static void setIsLogin(boolean value) {
        checkInit();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isLogin", value);
        editor.apply();
    }

    public static boolean isLogin() {
        checkInit();
        return sharedPref.getBoolean("isLogin", false);
    }

    public static void setGrade(String value) {
        checkInit();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("grade", value);
        editor.apply();
    }

    public static String getGrade() {
        checkInit();
        return sharedPref.getString("grade", "");
    }

    public static void setClassNum(String value) {
        checkInit();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("classNum", value);
        editor.apply();
    }

    public static String getClassNum() {
        checkInit();
        return sharedPref.getString("classNum", "");
    }
}
