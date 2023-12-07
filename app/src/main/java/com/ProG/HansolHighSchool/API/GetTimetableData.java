package com.ProG.HansolHighSchool.API;

import android.annotation.SuppressLint;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

public class GetTimetableData {
    private static final String TAG = "getTimetableData";

    @SuppressLint("SimpleDateFormat")
    public static CompletableFuture<String> getTimeTable(String date, String grade, String classNum) {
        niesAPI niesAPI = new niesAPI();

        String requestURL;
        StringBuilder resultBuilder = new StringBuilder();
        requestURL =
                "https://open.neis.go.kr/hub/hisTimetable?" +
                        // "KEY=" + niesAPI.KEY +
                        "&Type=" + "json" +
                        "&ATPT_OFCDC_SC_CODE=" + niesAPI.ATPT_OFCDC_SC_CODE +
                        "&SD_SCHUL_CODE=" + niesAPI.SD_SCHUL_CODE +
                        "&ALL_TI_YMD=" + date +
                        "&GRADE=" + grade +
                        "&CLASS_NM=" + classNum;

        Log.e(TAG, "requestURL :\n" + requestURL);

        return CompletableFuture.supplyAsync(() -> {
            try {
                URL url = new URL(requestURL);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                connection.setRequestProperty("Content-Type", "application/json");
                connection.setDoInput(true);
                connection.connect();

                InputStream inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                StringBuilder stringBuilder = new StringBuilder();
                int data;

                while ((data = inputStreamReader.read()) != -1) {
                    stringBuilder.append((char) data);
                }

                inputStreamReader.close();
                connection.disconnect();

                JSONObject responseJson = new JSONObject(String.valueOf(stringBuilder));
                JSONArray timetableArray = responseJson.getJSONArray("hisTimetable");
                timetableArray = timetableArray.getJSONObject(1).getJSONArray("row");

                Calendar calendar = Calendar.getInstance();
                calendar.setTime(Objects.requireNonNull(new SimpleDateFormat("yyyyMMdd").parse(date)));
                if (calendar.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
                    resultBuilder.append("자율\n");
                }

                for (int i = 0; i < timetableArray.length(); i++) {
                    JSONObject itemObject = timetableArray.getJSONObject(i);
                    String ITRT_CNTNT = itemObject.getString("ITRT_CNTNT");

                    resultBuilder
                            .append(ITRT_CNTNT)
                            .append("\n");
                    Log.d(TAG, "parsing : " + resultBuilder);
                }

                return String.valueOf(resultBuilder);
            } catch (Exception e) {
                Log.e(TAG, "return Error" + e);
                return null;
            }
        });
    }
}
