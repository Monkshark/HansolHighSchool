package com.ProG.HansolHighSchool.API;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.CompletableFuture;

public class GetMealData {
    private static final String TAG = "getMealData";
    static String result = null;

    public static CompletableFuture<String> getMeal(String date, String mealScCode, String type) {

        niesAPI niesAPI = new niesAPI();
        String requestURL;

        requestURL =
                "https://open.neis.go.kr/hub/mealServiceDietInfo?" +
//                        "KEY=" + niesAPI.KEY +
                        "&Type=json" +
                        "&MMEAL_SC_CODE=" + mealScCode +
                        "&ATPT_OFCDC_SC_CODE=" + niesAPI.ATPT_OFCDC_SC_CODE +
                        "&SD_SCHUL_CODE=" + niesAPI.SD_SCHUL_CODE +
                        "&MLSV_YMD=" + date;

        return CompletableFuture.supplyAsync(() -> {
            try {
                Log.d(TAG, "start parse \n" + requestURL);

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

                while ((data = inputStreamReader.read()) != - 1) {
                    stringBuilder.append((char) data);
                }

                inputStreamReader.close();
                connection.disconnect();

                JSONObject responseJson = new JSONObject(String.valueOf(stringBuilder));
                JSONArray timetableArray = responseJson.getJSONArray("mealServiceDietInfo");
                timetableArray = timetableArray.getJSONObject(1).getJSONArray("row");

                for (int i = 0; i < timetableArray.length(); i++) {
                    JSONObject itemObject = timetableArray.getJSONObject(i);

                    String 메뉴 = itemObject.getString("DDISH_NM");
                    String 칼로리 = itemObject.getString("CAL_INFO");
                    String 영양정보 = itemObject.getString("NTR_INFO");

                    switch (type) {
                        case "메뉴" -> result = 메뉴;
                        case "칼로리" -> result = 칼로리;
                        case "영양정보" -> result = 영양정보;
                    }

                }
            } catch (Exception e) {
                Log.e(TAG, "return Error" + e);
                return null;
            }
            return result.replace("<br/>", "\n");
        });
    }
}
