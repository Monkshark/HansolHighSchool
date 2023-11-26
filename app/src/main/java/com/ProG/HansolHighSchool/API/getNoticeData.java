package com.ProG.HansolHighSchool.API;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class GetNoticeData {
    static String requestURL;

    private static final String TAG = "getNoticeData";

    public static CompletableFuture<String> getNotice(String date) {
        niesAPI niesAPI = new niesAPI();
        Log.e(TAG, "getNotice: " + date);
        requestURL =
                "https://open.neis.go.kr/hub/SchoolSchedule?" +
//                        "KEY=" + niesAPI.KEY +
                        "&Type=json" +
                        "&ATPT_OFCDC_SC_CODE=" + niesAPI.ATPT_OFCDC_SC_CODE +
                        "&SD_SCHUL_CODE=" + niesAPI.SD_SCHUL_CODE +
                        "&AA_YMD=" + date;

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

                JSONObject responseJson = new JSONObject(stringBuilder.toString());
                JSONArray schoolScheduleArray = responseJson.getJSONArray("SchoolSchedule");

                for (int i = 0; i < schoolScheduleArray.length(); i++) {
                    JSONObject schedule = schoolScheduleArray.getJSONObject(i);
                    if (schedule.has("row")) {
                        JSONArray rowArray = schedule.getJSONArray("row");
                        for (int j = 0; j < rowArray.length(); j++) {
                            JSONObject row = rowArray.getJSONObject(j);
                            if (row.has("EVENT_NM")) {
                                return row.getString("EVENT_NM");
                            }
                        }
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            return "학사일정 없음";
        });
    }
}
