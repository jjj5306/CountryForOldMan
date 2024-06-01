package com.cbnusoftandriod.countryforoldman.util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import org.json.JSONArray;
import org.json.JSONObject;

public class GeocoderHelper {

    private static final String TAG = "GeocoderHelper";

    public static double[] getCoordinatesFromAddress(String address) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();

        try {
            // 주소 인코딩
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String urlString = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + encodedAddress;
            Log.d(TAG, "Encoded URL: " + urlString);

            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            // 요청 헤더 설정
            urlConnection.setRequestProperty("X-NCP-APIGW-API-KEY-ID", "5m7q1kmnox");
            urlConnection.setRequestProperty("X-NCP-APIGW-API-KEY", "LfmQ92nF0Klu9aJohxjsC7tmBrb2AOZ1Ak1NcAAh");

            int responseCode = urlConnection.getResponseCode();
            Log.d(TAG, "Response Code: " + responseCode);

            if (responseCode == HttpURLConnection.HTTP_OK) {
                reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                String line;
                while ((line = reader.readLine()) != null) {
                    result.append(line);
                }
                Log.d(TAG, "Response: " + result.toString());
                return parseJSON(result.toString());
            } else {
                Log.e(TAG, "Error Response Code: " + responseCode);
                result.append("Error: ").append(responseCode);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred: " + e.getMessage());
            e.printStackTrace(); // 스택 트레이스를 출력
            return null; // 예외가 발생하면 null 반환
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error closing reader: " + e.getMessage());
                    e.printStackTrace(); // 스택 트레이스를 출력
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null; // 요청이 실패하면 null 반환
    }

    private static double[] parseJSON(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray addresses = jsonObject.getJSONArray("addresses");
            Log.d(TAG, "Addresses length: " + addresses.length());
            if (addresses.length() > 0) {
                JSONObject address = addresses.getJSONObject(0);
                double x = address.getDouble("x");
                double y = address.getDouble("y");
                Log.d(TAG, "Coordinates: x=" + x + ", y=" + y);
                return new double[]{x, y}; // 좌표 반환
            } else {
                Log.d(TAG, "No addresses found");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
            e.printStackTrace(); // 스택 트레이스를 출력
        }
        return null; // 파싱 실패 시 null 반환
    }
}
