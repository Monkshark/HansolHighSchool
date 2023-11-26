package com.ProG.HansolHighSchool.Data;

import android.content.Context;
import android.content.SharedPreferences;

public class LoginData {
    private static final String PREF_NAME = "LoginData";
    private final SharedPreferences sharedPref;

    private LoginData(Context context) {
        sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    public static LoginData getInstance(Context context) {
        return new LoginData(context);
    }

    public void setSchoolNum(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("schoolNum", value);
        editor.apply();
    }

    public String getSchoolNum() {
        return sharedPref.getString("schoolNum", "");
    }

    public void setName(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("name", value);
        editor.apply();
    }

    public String getName() {
        return sharedPref.getString("name", "");
    }

    public void setPassword(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("password", value);
        editor.apply();
    }

    public String getPassword() {
        return sharedPref.getString("password", "");
    }

    public void setIsLogin(boolean value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("isLogin", value);
        editor.apply();
    }

    public boolean isLogin() {
        return sharedPref.getBoolean("isLogin", false);
    }

    public void setGrade(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("grade", value);
        editor.apply();
    }

    public String getGrade() {
        return sharedPref.getString("grade", "");
    }

    public void setClassNum(String value) {
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("classNum", value);
        editor.apply();
    }

    public String getClassNum() {
        return sharedPref.getString("classNum", "");
    }
}
