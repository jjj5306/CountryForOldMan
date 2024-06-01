package com.cbnusoftandriod.countryforoldman.util;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

public class GeocoderHelper {

    private static final String TAG = "GeocoderHelper";

    public static double[] getCoordinatesFromAddress(String address) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();

        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String urlString = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + encodedAddress;
            Log.d(TAG, "Encoded URL: " + urlString);

            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

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
                Log.d(TAG, "Response: " + result);
                return parseCoordinates(result.toString());
            } else {
                Log.e(TAG, "Error Response Code: " + responseCode);
                result.append("Error: ").append(responseCode);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error closing reader: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    public static String getRoadAddressFromAddress(String address) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        StringBuilder result = new StringBuilder();

        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8.toString());
            String urlString = "https://naveropenapi.apigw.ntruss.com/map-geocode/v2/geocode?query=" + encodedAddress;
            Log.d(TAG, "Encoded URL: " + urlString);

            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

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
                Log.d(TAG, "Response: " + result);
                return parseRoadAddress(result.toString());
            } else {
                Log.e(TAG, "Error Response Code: " + responseCode);
                result.append("Error: ").append(responseCode);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception occurred: " + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error closing reader: " + e.getMessage());
                    e.printStackTrace();
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return null;
    }

    private static double[] parseCoordinates(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray addresses = jsonObject.getJSONArray("addresses");
            Log.d(TAG, "Addresses length: " + addresses.length());
            if (addresses.length() > 0) {
                JSONObject address = addresses.getJSONObject(0);
                double x = address.getDouble("x");
                double y = address.getDouble("y");
                Log.d(TAG, "Coordinates: x=" + x + ", y=" + y);
                return new double[]{x, y};
            } else {
                Log.d(TAG, "No addresses found");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    private static String parseRoadAddress(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray addresses = jsonObject.getJSONArray("addresses");
            Log.d(TAG, "Addresses length: " + addresses.length());
            if (addresses.length() > 0) {
                JSONObject address = addresses.getJSONObject(0);
                String roadAddress = address.getString("roadAddress");
                Log.d(TAG, "Road Address: " + roadAddress);
                return roadAddress;
            } else {
                Log.d(TAG, "No addresses found");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error parsing JSON: " + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }
}
