package utils;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by manhhung on 2/19/19.
 */

public class ApplicationsName {

    /*
    * Name Applications
    * */
    public static final String FACEBOOK = "facebook";
    public static final String YOUTUBE = "yotube";
    public static final String GOOGLE_CHORME = "google";
    public static final String TWITTER = "twitter";
    public static final String ZALO = "zalo";
    public static final String TELEGRAM = "telegram";
    public static final String MESSENGER = "messenger";
    public static final String GMAIL = "gmail";
    public static final String ZINGMP3 = "zing_mp3";
    public static final String SPOTIFY = "spotify";
    public static final String TIKI = "tiki";
    public static final String LAZADA = "lazada";
    public static final String SHOPEE = "shopee";
    public static final String GOOGLE_TRANSLATE = "google_dich";
    public static final String GOOGLE_MAPS = "google_map";
    public static final String INSTAGRAM = "instagram";
    public static final String GOVIET = "goviet";
    public static final String FASTGO = "fastgo";
    public static final String GRAB = "grab";
    public static final String BE = "be";
    // setting app on phone
    public static final String WIFI = "wifi";
    public static final String BLUETOOTH = "bluetooth";
    public static final String INTERNET_3G = "3g";
    public static final String LOCATION_SETTING = "location";
    public static final String FLIGHT_MODE= "flight_mode";
    public static final String SCREEN_OFF = "screen_off";
    public static final String FLASH = "flash";
    public static final String SETTING_SOUNDS = "setting_sounds";
    public static final String SILENT_MODE = "silent_mode";
    public static final String NORMAL_MODE = "normal_mode";
    public static final String Vibrate_MODE = "vibrate_mode";
    public static final String CAMERA_VIDEO = "camera_video";
    public static final String IMAGE_VIDEO = "image_video";
    public static final String CONTACT = "contact";
    public static final String INBOX_MESSAGE = "inbox_message";
    public static final String SETTING_APPLICATION = "setting_application";
    public static final String CALENDAR = "calendar";




    /*
    * domain classification
    * */
    public static final String AIR_CONDITION = "air_condition";
    public static final String ALARM = "alarm";
    public static final String APPLICATION = "application";
    public static final String CALL = "call";
    public static final String LOCATION_DOMAIN = "location";
    public static final String SETTING = "setting";
    public static final String WEATHER = "weather";
    public static final String WELCOME = "welcome";
    public static final String EVENT = "event";
    public static final String SITE = "site";
    public static final String QUESTION_EVENT = "question_event";

    /*
    * pakagename
    * */
    public static final String PAKAGENAME_FACEBOOK = "com.facebook.katana";
    public static final String PAKAGENAME_YOUTUBE = "com.google.android.youtube";
    public static final String PAKAGENAME_GOOGLE_CHORME = "com.android.chrome";
    public static final String PAKAGENAME_TWITTER = "com.twitter.android";
    public static final String PAKAGENAME_ZALO = "com.zing.zalo";
    public static final String PAKAGENAME_TELEGRAM = "org.telegram.messenger";
    public static final String PAKAGENAME_MESSENGER = "com.facebook.orca";
    public static final String PAKAGENAME_GMAIL = "com.google.android.gm";
    public static final String PAKAGENAME_ZINGMP3 = "com.zing.mp3";
    public static final String PAKAGENAME_SPOTIFY = "com.spotify.music";
    public static final String PAKAGENAME_TIKI = "vn.tiki.app.tikiandroid";
    public static final String PAKAGENAME_LAZADA = "com.lazada.android";
    public static final String PAKAGENAME_SHOPEE = "com.shopee.vn";
    public static final String PAKAGENAME_GOOGLE_TRANSLATE = "com.google.android.apps.translate";
    public static final String PAKAGENAME_GOOGLE_MAPS = "com.google.android.apps.maps";
    public static final String PAKAGENAME_INSTAGRAM = "com.instagram.android";
    public static final String PAKAGENAME_GOVIET = "com.goviet.app";
    public static final String PAKAGENAME_FASTGO = "vn.mpos.fastgo";
    public static final String PAKAGENAME_GRAB = "com.grabtaxi.passenger";
    public static final String PAKAGENAME_BE = "xyz.be.customer";

    public static final Map<String, String> application = new HashMap<String, String>() {
        {
            put(FACEBOOK, PAKAGENAME_FACEBOOK);
            put(YOUTUBE, PAKAGENAME_YOUTUBE);
            put(GOOGLE_CHORME, PAKAGENAME_GOOGLE_CHORME);
            put(TWITTER, PAKAGENAME_TWITTER);
            put(ZALO, PAKAGENAME_ZALO);
            put(TELEGRAM, PAKAGENAME_TELEGRAM);
            put(MESSENGER, PAKAGENAME_MESSENGER);
            put(GMAIL, PAKAGENAME_GMAIL);
            put(ZINGMP3, PAKAGENAME_ZINGMP3);
            put(SPOTIFY, PAKAGENAME_SPOTIFY);
            put(TIKI, PAKAGENAME_TIKI);
            put(LAZADA, PAKAGENAME_LAZADA);
            put(SHOPEE, PAKAGENAME_SHOPEE);
            put(GOOGLE_TRANSLATE, PAKAGENAME_GOOGLE_TRANSLATE);
            put(GOOGLE_MAPS, PAKAGENAME_GOOGLE_MAPS);
            put(INSTAGRAM, PAKAGENAME_INSTAGRAM);
            put(GOVIET, PAKAGENAME_GOVIET);
            put(FASTGO, PAKAGENAME_FASTGO);
            put(GRAB, PAKAGENAME_GRAB);
            put(BE, PAKAGENAME_BE);
        }
    };

    public static String getApplication(String nameApp) {
        if (application.containsKey(nameApp)) {
            return application.get(nameApp);
        }
        return "Ola không tìm thấy ứng dụng cho bạn!";
    }

}

