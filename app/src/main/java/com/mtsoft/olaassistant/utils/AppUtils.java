package com.mtsoft.olaassistant.utils;

import android.content.pm.PackageManager;

/**
 * Created by manhhung on 3/29/19.
 */

public class AppUtils {
    public static String URL_ZING = "https://ac.global.mp3.zing.vn/complete/mobile?type=song&num=5&query=";
    public static String URL_PLAYER_ZING = "https://zingmp3.vn/bai-hat/";
    public static String KEY_YOUTUBE = "AIzaSyC7fmWRvQ666Tv33KLoAyonh_BViNm02rc";
    public static String WEBVIEW_YOUTUBE = "http://www.youtube.com/watch?v=";
    public static String KEY_GOOGLE_MAP = "AIzaSyCRwjuTAzHRMmS7a9nQrwG_XaCZpGzRzjU";
    public static String KEY_WEATHER = "17fdbe391ebf3056d60ae06d4620093f";

    public static String URL_SERVER = "https://a9c70d78.ngrok.io/segmention";

    /*TODO: set URL for get lat & lng location
    * @location: name address
    * */
    public static String setUrlGetLatLng(String location) {
        location = location.trim().replaceAll(" ", "+");
        return "https://maps.googleapis.com/maps/api/geocode/json?address=" + location + "&key=" + KEY_GOOGLE_MAP;
    }

    public static String setUrlGetWeather(String latlng, Long timestamp, String lang) {
        return "https://api.darksky.net/forecast/" + KEY_WEATHER + "/" + latlng + "," + timestamp + "?lang=" + lang;
    }

    public static String setUrlForGetAddress(String latlng){
        return "https://maps.googleapis.com/maps/api/geocode/json?latlng=" +latlng + "&sensor=true&key=" + KEY_GOOGLE_MAP;
    }

    public static String setUrlQueryYoutube(String q) {
        q = q.replaceAll(" ", "+");
        return "https://www.googleapis.com/youtube/v3/search?part=id&q=" + q + "&type=video&key=" + KEY_YOUTUBE;
    }

    /*TODO: check pakage installed*/
    public static boolean isPackageInstalled(String packageName, PackageManager packageManager) {
        boolean found = true;
        try {
            packageManager.getPackageInfo(packageName, 0);
        } catch (PackageManager.NameNotFoundException e) {
            found = false;
        }
        return found;
    }

    public static String getTimeMillis() {
        Long tsLong = System.currentTimeMillis() / 1000;
        return tsLong.toString();
    }


}
