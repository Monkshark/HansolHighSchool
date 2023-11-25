package com.ProG.HansolHighSchool.Data;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginData {
    private static SharedPreferences sharedPref;
    private static final String PREF_NAME = "LoginData";

    public static void init(Context context) {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static void setSchoolNum(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("schoolNum", value);
        editor.apply();
    }

    public static String getSchoolNum() {
        return sharedPref.getString("schoolNum", "");
    }

    public static void setName(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", value);
        editor.apply();
    }

    public static String getName() {
        return sharedPref.getString("name", "");
    }

    public static void setPassword(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("password", value);
        editor.apply();
    }

    public static String getPassword() {
        return sharedPref.getString("password", "");
    }

    public static void setIsLogin(boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isLogin", value);
        editor.apply();
    }

    public static boolean isLogin() {
        return sharedPref.getBoolean("isLogin", false);
    }

    public static void setGrade(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("grade", value);
        editor.apply();
    }

    public static String getGrade() {
        return sharedPref.getString("grade", "");
    }

    public static void setClassNum(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("classNum", value);
        editor.apply();
    }

    public static String getClassNum() {
        return sharedPref.getString("classNum", "");
    }
}
