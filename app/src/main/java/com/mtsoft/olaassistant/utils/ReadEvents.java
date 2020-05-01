package com.mtsoft.olaassistant.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.provider.BaseColumns;
import android.provider.CalendarContract;
import androidx.core.app.ActivityCompat;
import android.util.Log;

import com.mtsoft.olaassistant.models.Event;

import java.util.ArrayList;

/**
 * Created by manhhung on 4/14/19.
 */

public class ReadEvents {
    private static final int MY_CAL_REQ = 105;
    static Cursor cursor;
    public Context context;
    Activity activity;

    public ReadEvents(Activity activity, Context context) {
        this.activity = activity;
        this.context = context;
    }


    public ArrayList<Event> getEvents(String sentence) {

        ArrayList<Event> events = new ArrayList<>();

        Long startTime = CalendarUtils.getTimestamp(sentence);
        Long endTime = startTime + 24 * 3600;
        startTime *= 1000;
        endTime *= 1000;

        Long next = CalendarUtils.getNextNumber(sentence);
        if (next != 0) {
            System.out.println("Next: " + next);
            endTime = (next + startTime);
        }

        System.out.println("Start time: " + startTime);
        System.out.println("End time: " + endTime);


        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_CALENDAR}, MY_CAL_REQ);
            Log.e("request", "per");
        } else {
//            Uri CALENDAR_URI = null;
//            String uri = "";
//            if (Build.VERSION.SDK_INT >= 8 || Build.VERSION.SDK_INT <= 13) {
//
//                uri = "content://com.android.calendar/events";
//                CALENDAR_URI = Uri.parse(uri);
//
//            } else if (Build.VERSION.SDK_INT >= 14) {
//                CALENDAR_URI = CalendarContract.Events.CONTENT_URI;
//
//            } else {
//                uri = "content://calendar/events";
//                CALENDAR_URI = Uri.parse(uri);
//
//            }

            String[] projection = new String[]{BaseColumns._ID, CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART, CalendarContract.Events.EVENT_LOCATION};
            String selection = CalendarContract.Events.DTSTART + " >= ? AND " + CalendarContract.Events.DTSTART + "<= ?";
            String[] selectionArgs = new String[]{Long.toString(startTime), Long.toString(endTime)};
            Cursor cur = context.getContentResolver().query(CalendarContract.Events.CONTENT_URI, projection, selection, selectionArgs, null);


            long _id = 0l;
            String title = null;
            String location = null;
            String time = null;
            while (cur.moveToNext()) {
                _id = cur.getLong(0);
                title = cur.getString(1);
                time = cur.getString(2);
                location = cur.getString(3);

                Log.v("log_tag", "_id     : " + _id);
                Log.v("log_tag", "name    : " + title);
                Log.v("log_tag", "start time   : " + CalendarUtils.convertMilliseconds2DateFormat(Long.parseLong(time)));
                Log.v("log_tag", "location: " + location);
                time = CalendarUtils.convertMilliseconds2DateFormat(Long.parseLong(time));

                events.add(new Event(_id, title, location, time));

            }

        }

        return events;


    }

//    public static void main(String[] args) {
//        String s = "ngày 16 tháng 4 có sự kiện gì không";
//        System.out.println(s);
//        getEvents(s);
//        System.out.println("--------");
//
//        s = "9:20 ngày 16 tháng 4 có sự kiện gì không";
//        System.out.println(s);
//        getEvents(s);
//        System.out.println("--------");
//        s = "8 ngày 16 tháng 4 có sự kiện gì không";
//        System.out.println(s);
//
//        getEvents(s);
//        System.out.println("--------");
//        s = "lịch trình 3 ngày tới";
//        System.out.println(s);
//
//        getEvents(s);
//        System.out.println("--------");
//        s = "lịch trình tuần tới";
//        System.out.println(s);
//
//        getEvents(s);
//        System.out.println("--------");
//        s = "lịch trình 10 ngày nữa";
//        System.out.println(s);
//
//        getEvents(s);
//        System.out.println("--------");
//        s = "lịch trình tuần sau";
//        System.out.println(s);
//
//        getEvents(s);
//        System.out.println("--------");
//        s = "lịch trình 2 tuần tới";
//        System.out.println(s);
//
//        getEvents(s);
//    }


}


