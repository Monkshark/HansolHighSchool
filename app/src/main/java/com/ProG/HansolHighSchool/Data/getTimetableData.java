package com.ProG.HansolHighSchool.Data;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class getTimetableData {

    private static final String TAG = "getTimetableData";
    public static String getTimeTable(String date, int grade, int classNum) {
        niesAPI niesAPI = new niesAPI();

        String requestURL;
        StringBuilder resultBuilder = new StringBuilder();

        resultBuilder
                .append("\n")
                .append(date.substring(0, 4)).append("년 ")
                .append(date.substring(4, 6)).append("월 ")
                .append(date.substring(6, 8)).append("일\n")
                .append(grade).append("학년 ")
                .append(classNum).append("반 시간표").append("\n\n");

        requestURL = "https://open.neis.go.kr/hub/hisTimetable?" +
                "KEY=" + niesAPI.KEY +
                "&Type=" + "json" +
                "&ATPT_OFCDC_SC_CODE=" + niesAPI.ATPT_OFCDC_SC_CODE +
                "&SD_SCHUL_CODE=" + niesAPI.SD_SCHUL_CODE +
                "&ALL_TI_YMD="  + date +
                "&GRADE=" + grade +
                "&CLASS_NM=" + classNum;

        Log.e(TAG, "requestURL : \n" + requestURL);

        Future<String> futureResult = Executors.newSingleThreadExecutor().submit(() -> {
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

                JSONObject responseJson = new JSONObject(stringBuilder.toString());
                JSONArray timetableArray = responseJson.getJSONArray("hisTimetable");
                timetableArray = timetableArray.getJSONObject(1).getJSONArray("row");

                for (int i = 0; i < timetableArray.length(); i++) {
                    JSONObject itemObject = timetableArray.getJSONObject(i);
                    String PERIO = itemObject.getString("PERIO");
                    String ITRT_CNTNT = itemObject.getString("ITRT_CNTNT");

                    if (i == 1 && PERIO.equals("2")) {
                        resultBuilder
                                .append("1교시: 자율" + "\n");
                    }

                    resultBuilder
                            .append(PERIO)
                            .append("교시: ")
                            .append(ITRT_CNTNT)
                            .append("\n");

                    Log.e(TAG, "parsing : " + resultBuilder);
                }

                return resultBuilder.toString();
        });

        try {
            String finalResult = futureResult.get();
            Log.e(TAG,"return : " + finalResult);
            return finalResult;
        } catch (Exception e) {
            Log.e(TAG, "return Error" + e.toString());
            return "리턴에러다 수정해라";
        }

    }
}