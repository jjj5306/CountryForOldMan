package com.cbnusoftandriod.countryforoldman.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GeocoderHelper {
    private static final String TAG = "GeocoderHelper";

    // 주소를 위도와 경도로 변환하는 메서드
    public static double[] getCoordinatesFromAddress(Context context, String address) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocationName(address, 5);
            if (addresses.size() != 0) {
                Address location = addresses.get(0);
                if (location.hasLatitude() && location.hasLongitude() && isValidAddress(location)) {
                    Log.d(TAG, "유효한 주소: " + location);
                    return new double[]{location.getLatitude(), location.getLongitude()};
                } else {
                    Log.d(TAG, "위도와 경도가 없는 주소: " + location);
                }
            } else {
                Log.d(TAG, "주소를 찾을 수 없음: " + address);
            }
            return null; // 유효하지 않은 주소일 경우 null 반환
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Geocoder IOException: " + e.getMessage());
            return null;
        }
    }

    // 주소가 유효한지 확인하는 메서드
    private static boolean isValidAddress(Address address) {
        return address.getMaxAddressLineIndex() >= 0 && address.getCountryName() != null;
    }

    // 위도와 경도를 받아 주소를 반환하는 메서드
    public static String getAddressFromLocation(Context context, double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 5);
            if (addresses != null && !addresses.isEmpty()) {
                return addresses.get(0).getAddressLine(0);
            } else {
                return null; // 주소를 찾지 못했을 경우 null 반환
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.e(TAG, "Geocoder IOException: " + e.getMessage());
            return null;
        }
    }
}
