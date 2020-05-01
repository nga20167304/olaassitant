package com.mtsoft.olaassistant.utils;

//import android.util.Log;

import android.util.Log;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by manhhung on 3/30/19.
 */

public class CalendarUtils {

    public static Long getTimestampCurrent() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        return date.getTime();
    }

    public static String getCurrentTimeUsingCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String formattedDate = dateFormat.format(date);
        String formattedTime = timeFormat.format(date);
//        return "Bây giờ là : " + formattedTime  + "phút ngày " + formattedDate;

        String time1 = "";

        Calendar cal1 = Calendar.getInstance();
        Date date1 = cal.getTime();
        time1 = convertMilliseconds2DateFormat(date.getTime());
        cal1.setTime(date);
        String hour = String.valueOf(cal1.get(Calendar.HOUR_OF_DAY));
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        String minute = String.valueOf(cal1.get(Calendar.MINUTE));
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        String day = String.valueOf(cal1.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(cal1.get(Calendar.MONTH) + 1);
        String year = String.valueOf(cal1.get(Calendar.YEAR));

        time1 = "Bây giờ là " + hour + " giờ " + minute + " phút, ngày " + day + " tháng " + month + " năm " + year + " thưa ngài!";
        return time1;
    }

    public static String getDateTimeCalendar() {
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy ");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm");
        String formattedDate = dateFormat.format(date);
        String formattedTime = timeFormat.format(date);
        return formattedTime + "\n" + formattedDate;
    }

    public static String convertMilliseconds2DateFormat(Long miliSec) {
        DateFormat simple = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date result = new Date(miliSec);
        return simple.format(result);
    }

    public static Long convertStringToTimestamp(String myDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd/MM/yyyy");
        Date date = null;
        try {
            date = sdf.parse(myDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long millis = date.getTime();
        return millis / 1000;
    }

    public static Long getTimestamp(String timeString) {
        timeString = timeString.toLowerCase();
        String time = "";
        Calendar cal = Calendar.getInstance();
        Date date = cal.getTime();
        time = convertMilliseconds2DateFormat(date.getTime());
        cal.setTime(date);

        String hour = String.valueOf(cal.get(Calendar.HOUR_OF_DAY));
        if (hour.length() == 1) {
            hour = "0" + hour;
        }
        String minute = String.valueOf(cal.get(Calendar.MINUTE));
        if (minute.length() == 1) {
            minute = "0" + minute;
        }
        String day = String.valueOf(cal.get(Calendar.DAY_OF_MONTH));
        String month = String.valueOf(cal.get(Calendar.MONTH) + 1);
        String year = String.valueOf(cal.get(Calendar.YEAR));

        timeString = timeString.toLowerCase();
        if (timeString.contains("nay")) {
//            Log.e("TIME", "Hôm nay");
            if (findHour(timeString) != null) {
                hour = findHour(timeString);
            }
            Long today = getTimestampOfdate(0);
            if (hour.contains(":")) {
                String h = hour.split(":")[0];
                String m = hour.split(":")[1];
                if ((timeString.contains("tối") || timeString.contains("chiều")) && Integer.parseInt(h) <= 11) {
                    return (today + (Integer.parseInt(h) + 12) * 3600 + Integer.parseInt(m) * 60);
                }
                return (today + Integer.parseInt(h) * 3600 + Integer.parseInt(m) * 60);
            }
            if ((timeString.contains("tối") || timeString.contains("chiều")) && Integer.parseInt(hour) <= 11) {
                return (today + (Integer.parseInt(hour) + 12) * 3600);
            }
            return (today + Integer.parseInt(hour) * 3600);

        } else if (timeString.contains("mai")) {
//            Log.e("TIME", "Ngày mai");
            hour = "00";
            if (findHour(timeString) != null) {
                hour = findHour(timeString);
                System.out.println("Find hour: " + hour);
//                Log.e("HOUR", hour);

            }
            Long tomorrow = getTimestampOfdate(1);
            if (hour.contains(":")) {
//                Log.e("TIME", hour);
                String h = hour.split(":")[0];
                String m = hour.split(":")[1];
                if ((timeString.contains("tối") || timeString.contains("chiều")) && Integer.parseInt(h) <= 11) {
                    return (tomorrow + (Integer.parseInt(h) + 12) * 3600 + Integer.parseInt(m) * 60);
                }
                return (tomorrow + Integer.parseInt(h) * 3600 + Integer.parseInt(m) * 60);
            }
            if ((timeString.contains("tối") || timeString.contains("chiều")) && Integer.parseInt(hour) <= 11) {
                return (tomorrow + (Integer.parseInt(hour) + 12) * 3600);
            }
            return (tomorrow + Integer.parseInt(hour) * 3600);


        } else if (timeString.contains("kia")) {
//            Log.e("TIME", "Ngày kia");
            hour = "00";
            if (findHour(timeString) != null) {
                hour = findHour(timeString);
                Long next2Day = getTimestampOfdate(2);

                if (hour.contains(":")) {
                    String h = hour.split(":")[0];
                    String m = hour.split(":")[1];
                    if ((timeString.contains("tối") || timeString.contains("chiều")) && Integer.parseInt(h) <= 11) {
                        return (next2Day + (Integer.parseInt(h) + 12) * 3600 + Integer.parseInt(m) * 60);
                    }
                    return (next2Day + Integer.parseInt(h) * 3600 + Integer.parseInt(m) * 60);
                }
                if ((timeString.contains("tối") || timeString.contains("chiều")) && Integer.parseInt(hour) <= 11) {
                    return (next2Day + (Integer.parseInt(hour) + 12) * 3600);
                }
                return (next2Day + Integer.parseInt(hour) * 3600);
            }
        } else if (findDay(timeString) != null) {
            if (findHour(timeString) != null) {
                hour = findHour(timeString);
            } else {
                hour = "00:00";
            }
            if (findDay(timeString) != null) {
                day = findDay(timeString);
            }
            if (findMonth(timeString) != null) {
                month = findMonth(timeString);
            }
            if (hour.length() == 1) {
                hour = "0" + hour;
            }
            if (!hour.contains(":")) {
                hour = hour + ":00";
            }
            if (day.length() == 1) {
                day = "0" + day;
            }
            if (month.length() == 1) {
                month = "0" + month;
            }
            String h = hour.split(":")[0];
            String m = hour.split(":")[1];
            if ((timeString.contains("tối") || timeString.contains("chiều")) && Integer.parseInt(h) <= 11) {
                hour = (Integer.parseInt(h) + 12) + ":" + m;
            }
            time = hour + " " + day + "/" + month + "/" + year;
            return convertStringToTimestamp(time);
        } else {
            if (hour.length() == 1) {
                hour = "0" + hour;
            }
            if (!hour.contains(":")) {
                hour = hour + ":" + minute;
            }

            String h = hour.split(":")[0];
            String m = hour.split(":")[1];
            if ((timeString.contains("tối") || timeString.contains("chiều")) && Integer.parseInt(h) <= 11) {
                hour = (Integer.parseInt(h) + 12) + ":" + m;
            }

            time = hour + " " + day + "/" + month + "/" + year;
            return convertStringToTimestamp(time);
        }
        return convertStringToTimestamp(time);
    }

    public static Long getTimestampOfdate(int day) {
        Date dt = new Date();
        Calendar c = Calendar.getInstance();
        c.setTime(dt);
        c.add(Calendar.DATE, day);
        dt = c.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String formattedDate = dateFormat.format(dt);

        SimpleDateFormat dateFormat1 = new SimpleDateFormat("dd/MM/yyyy");
        Date parsedDate = null;
        try {
            parsedDate = dateFormat1.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        return parsedDate.getTime() / 1000;
    }

    public static String findHour(String sentence) {
        sentence = sentence.toLowerCase();
        String h = null;
        Pattern pHour = Pattern.compile("\\d{1,2}(:)(\\d{1,2})");
        Matcher mHour = pHour.matcher(sentence);
        if (mHour.find()) {
            h = mHour.group();
            return h;
        }
        pHour = Pattern.compile("\\d{1,2}( giờ)( \\d{1,2} phút)?");
        mHour = pHour.matcher(sentence);
        if (mHour.find()) {
            pHour = Pattern.compile("\\d{1,2}");
            mHour = pHour.matcher(mHour.group());
            int i = 0;
            h = "";
            while (mHour.find()) {
                if (i == 1) {
                    h += ":";
                }
                h += mHour.group();
                i++;
            }

            return h;

        }

        pHour = Pattern.compile("(vào|lúc|khoảng|thời gian|thời điểm) \\d{1,2}( ngày)?");
        mHour = pHour.matcher(sentence);
        if (mHour.find()) {
            pHour = Pattern.compile("\\d{1,2}");
            mHour = pHour.matcher(mHour.group());
            if (mHour.find()) {
                h = mHour.group();
                return h;
            }
        }

        pHour = Pattern.compile("(lúc)?( )?\\d{1,2}( )?(ngày)?");
        mHour = pHour.matcher(sentence);
        if (mHour.find()) {
            String firsts[] = sentence.substring(0, mHour.start()).trim().split(" ");

            String first = firsts[firsts.length - 1];
            if (first.equalsIgnoreCase("ngày") || first.equalsIgnoreCase("mồng")
                    || first.equalsIgnoreCase("mùng") || first.equalsIgnoreCase("hôm")) {
                return null;
            }

            String lasts[] = sentence.substring(mHour.start()).trim().split(" ");
            if (lasts.length > 1) {
                String last = lasts[1]; //bị crash ở đây ArrayIndexOutOfBoundsException
                if (last.equalsIgnoreCase("tháng") ||
                        last.equalsIgnoreCase("ngày tới") || last.equalsIgnoreCase("hôm") ||
                        last.equalsIgnoreCase("ngày nữa") || last.equalsIgnoreCase("buổi")) {
                    return null;
                }
            }

            pHour = Pattern.compile("\\d{1,2}");
            mHour = pHour.matcher(mHour.group());
            if (mHour.find()) {
                h = mHour.group();
                return h;
            }

        }
        return h;
    }

    public static String findMonth(String timeString) {
        timeString = timeString.toLowerCase();

        Pattern pMonth = Pattern.compile("(tháng) \\d{1,2}( )?(năm)?");
        Matcher mMonth = pMonth.matcher(timeString);
        if (mMonth.find()) {
            pMonth = Pattern.compile("\\d{1,2}");
            mMonth = pMonth.matcher(mMonth.group());
            if (mMonth.find()) {
                return mMonth.group();
            }
        }
        if (timeString.contains("tháng sau") || timeString.contains("tháng tới") ||
                timeString.contains("tháng tiếp")) {
            Calendar calendar = Calendar.getInstance();
            Integer m = calendar.get(Calendar.MONTH);
            m += 2;
            if (m >= 11) {
                m = 1;
            }

            System.out.println("M: " + m);
            return "" + m;
        }
        return null;
    }

    public static String findDay(String timeString) {
        timeString = timeString.toLowerCase();
        Pattern pDay = Pattern.compile("(ngày) \\d{1,2}( tháng)?");
        Matcher mDay = pDay.matcher(timeString);
        if (mDay.find()) {
            pDay = Pattern.compile("\\d{1,2}");
            mDay = pDay.matcher(mDay.group());
            if (mDay.find()) {
                return mDay.group();
            }
        }

        pDay = Pattern.compile("(ngày )?\\d{1,2} (tháng)");
        mDay = pDay.matcher(timeString);
        if (mDay.find()) {
            pDay = Pattern.compile("\\d{1,2}");
            mDay = pDay.matcher(mDay.group());
            if (mDay.find()) {
                return mDay.group();
            }
        }

        if (timeString.contains("mai")) {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            Log.e("TAG", "" + day);
            return "" + (day + 1);

        }
        if (timeString.contains("ngày kia")) {
            Calendar cal = Calendar.getInstance();
            int day = cal.get(Calendar.DAY_OF_MONTH);
            Log.e("TAG", "" + day);
            return "" + (day + 2);
        }

        return findDayInWeek(timeString);
//        return null;
    }

    public static ArrayList<Integer> findDayOfWeeks(String sentence) {
        sentence = sentence.toLowerCase();
        ArrayList<Integer> alarmDays = new ArrayList<>();

        Map<Integer, String[]> days = new HashMap<Integer, String[]>();
        days.put(Calendar.MONDAY, new String[]{"thứ 2", "thứ hai"});
        days.put(Calendar.TUESDAY, new String[]{"thứ 3", "thứ ba"});
        days.put(Calendar.WEDNESDAY, new String[]{"thứ 4", "thứ tư", "thứ bốn"});
        days.put(Calendar.THURSDAY, new String[]{"thứ 5", "thứ năm"});
        days.put(Calendar.FRIDAY, new String[]{"thứ 6", "thứ sáu"});
        days.put(Calendar.SATURDAY, new String[]{"thứ 7", "thứ bảy"});
        days.put(Calendar.SUNDAY, new String[]{"chủ nhật"});

        for (Integer key : days.keySet()) {
            for (int i = 0; i < days.get(key).length; i++) {
                if (sentence.contains(days.get(key)[i])) {
                    alarmDays.add(key);
                }

            }
        }

        Calendar cal = Calendar.getInstance();
        Integer day = cal.get(Calendar.DAY_OF_WEEK);
        if (sentence.contains("mai")) {
            day += 1;
            alarmDays.add(day);
        }
        if (sentence.contains("kia")) {
            day += 2;
            alarmDays.add(day);
        }

        Collections.sort(alarmDays);

        if (alarmDays.size() == 2 && (sentence.contains("đến") || sentence.contains("tới"))) {
            int firt = alarmDays.get(0);
            int last = alarmDays.get(1);
            for (int i = firt + 1; i < last; i++) {
                alarmDays.add(i);
            }
        }
        Collections.sort(alarmDays);
        return alarmDays;
    }

    public static Map<String, Integer> getHourMinuteAlarm(String sentence) {
        sentence = sentence.toLowerCase();
        Map<String, Integer> dictHourMinute = new HashMap<String, Integer>();

        String hour = findHour(sentence);
        Log.e("Find: ", "----" + hour);
        Integer h = 9;
        Integer m = 0;
        if (hour.contains(":")) {
            h = Integer.parseInt(hour.split(":")[0]);
            m = Integer.parseInt(hour.split(":")[1]);
            if (sentence.contains("tối") || sentence.contains("chiều") && h <= 11) {
                h += 12;
            }
        } else {
            h = Integer.parseInt(hour);
            if (sentence.contains("tối") || sentence.contains("chiều") && h <= 11) {
                h = Integer.parseInt(hour) + 12;
            }
        }

        dictHourMinute.put("h", h);
        dictHourMinute.put("m", m);

        return dictHourMinute;
    }

    //    Ví dụ 3 ngày tới=> 3, 10 ngày nữa=>10, tuần tới=>7,
    public static Long getNextNumber(String sentence) {
        sentence = sentence.toLowerCase();
        String parseString2Num[] = {"hai", "ba", "bốn", "năm", "sáu", "bảy", "tám", "chín", "mười"};
        for (int i = 0; i < parseString2Num.length; i++) {
            if (sentence.contains(parseString2Num[i]) && sentence.indexOf(parseString2Num[i]) == 0) {
                sentence = sentence.replace(parseString2Num[i], "" + (i + 2));
            }
        }
        System.out.println(sentence);
        Long result = 0l;
        Pattern number = Pattern.compile("\\d{1,2}( )((hôm)|(buổi)|(ngày)|(tuần)|(tháng))( )((tới)|(nữa)|(tiếp))");
        Matcher mMonth = number.matcher(sentence);
        if (mMonth.find()) {
            System.out.println("có số");
            number = Pattern.compile("\\d{1,2}");
            mMonth = number.matcher(mMonth.group());
            if (mMonth.find()) {
                System.out.println("aaa: " + mMonth.group());
                result = Long.parseLong(mMonth.group());
                System.out.println("sss" + result);
            }
        }

        if (result == 0) {
            number = Pattern.compile("((hôm)|(buổi)|(ngày)|(tuần)|(tháng)|(giờ))( )((tới)|(nữa)|(tiếp)|(này)|(sau))");
            mMonth = number.matcher(sentence);
            if (mMonth.find()) {
                result = 1l;

            }
        }

        if (result != 0) {
            if (sentence.contains("tuần")) {
                result = result * 7 * 24 * 3600;
            }
            if (sentence.contains("tháng")) {
                result = result * 30 * 24 * 3600;
            }
            if (sentence.contains("giờ")) {
                result = result * 3600;
            }
            if (sentence.contains("ngày") || sentence.contains("hôm") || sentence.contains("buổi")) {
                result = result * 24 * 3600;
            }
        }
//        if (sentence.contains("tuần sau")) {
//            Calendar cal1 = Calendar.getInstance();
//            Integer hour = cal1.get(Calendar.DAY_OF_WEEK);
//        }
        return result * 1000;
    }

    //    Tìm ngày dựa vào thứ, ví dụ "Gặp cô hương vào thứ sáu tuần này"
// => đưa ra day, month của t6 tuần này
    public static String findDayInWeek(String sentence) {
        sentence = sentence.toLowerCase();
        Calendar cal = Calendar.getInstance();
        Integer dayOfWeek = cal.get(Calendar.DAY_OF_WEEK);
        System.out.println("Day of week: " + dayOfWeek);

        Map<Integer, String[]> days = new HashMap<Integer, String[]>();
        days.put(Calendar.MONDAY, new String[]{"thứ 2", "thứ hai"});
        days.put(Calendar.TUESDAY, new String[]{"thứ 3", "thứ ba"});
        days.put(Calendar.WEDNESDAY, new String[]{"thứ 4", "thứ tư", "thứ bốn"});
        days.put(Calendar.THURSDAY, new String[]{"thứ 5", "thứ năm"});
        days.put(Calendar.FRIDAY, new String[]{"thứ 6", "thứ sáu"});
        days.put(Calendar.SATURDAY, new String[]{"thứ 7", "thứ bảy"});
        days.put(8, new String[]{"chủ nhật"});

        Integer dayTarget = dayOfWeek;
        for (Integer key : days.keySet()) {
            for (int i = 0; i < days.get(key).length; i++) {
                if (sentence.contains(days.get(key)[i])) {
                    dayTarget = key;
                }

            }
        }
        Integer today = cal.get(Calendar.DAY_OF_MONTH);


        Integer delta = Math.abs(dayTarget - dayOfWeek);
        Integer tmp = delta;
        if (dayTarget < dayOfWeek) {
            delta = 7 - tmp;
        } else {
//            delta = 7 - tmp;
        }

        if ((sentence.contains("tuần sau") || sentence.contains("tuần tới"))) {
            if (dayTarget >= dayOfWeek) {
                delta = 7 + tmp;
            }
        }
        System.out.println("dayTarget: " + dayTarget);

        System.out.println("today: " + today);


        System.out.println("Find: " + (today + delta));

        return (today + delta) + "";


    }


    public static String anwserLunar(String sentence) {
        sentence = sentence.toLowerCase();
        int day = Integer.parseInt(findDay(sentence));
        Log.e("TAG", "" + day);
        Calendar cal = Calendar.getInstance();
        Integer month = cal.get(Calendar.MONTH) + 1;
        if (findMonth(sentence) != null) {
            month = Integer.parseInt(findMonth(sentence));
        }

        int year = 2019;

        VietNamCalendar vietNamCalendar = new VietNamCalendar(day, month, year, 7);
        return vietNamCalendar.getDateLunar();

    }

//    public static void main(String[] args) {
//        String sentence = "9:30 Sáng mai có mưa không";
//        System.out.println("Hour: " + findHour(sentence));
//        Long t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        String s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//        System.out.println("---------");
//
//        sentence = "3:30 chiều mai có mưa không";
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "3 chiều mai có mưa không";
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "7:30 tối mai có mưa không";
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "3:30 chiều ngày 23 tháng 4 có mưa không";
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "3:30 tối ngày 23 tháng 4 có mưa không";
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "3 tối ngày 23 tháng 4 có mưa không";
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "3 chiều nay thời tiết thế nào";
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//
//        sentence = "4 chiều ngày 23 tháng 4 thời tiết thế nào";
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        String alarm = "lịch trình 2 tuần tới";
//
////        System.out.println("Day: " + findDayOfWeeks(alarm) + "-- hour: " + findHour(alarm));
//
//        System.out.println(getNextNumber(alarm));
//
//        System.out.println("******");
//
//        sentence = "ngày 24 tháng 3 năm 2019 9 giờ sáng";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "9 giờ sáng ngày 24 tháng 3 năm 2019";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "9 giờ 15 sáng ngày 24 tháng 3 năm 2019";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "9 giờ 15 phút sáng ngày 24 tháng 3 năm 2019";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "ngày 24 tháng 3 năm 2019 9 giờ 15 phút sáng";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "ngày 24 tháng 3 năm 2019 9 sáng";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "ngày 24 tháng 3 năm 2019 lúc 9 sáng";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "ngày 24 tháng 3 năm 2019 9:25 sáng";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "sáng mai 9";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//
//        System.out.println("---------");
//        sentence = "9 ngày mai có mưa không";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "9 sáng mai có mưa không";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "Gặp cô Hương vào 9 thứ sáu tuần này";
//        findDayInWeek(sentence);
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "Gặp cô Hương vào ngày 29 thứ sáu";
//        findDayInWeek(sentence);
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "Gặp cô Hương vào ngày 29 thứ sáu tuần sau";
//        findDayInWeek(sentence);
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "Gặp cô Hương vào ngày 29 thứ tư tuần sau";
//        findDayInWeek(sentence);
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "Gặp cô Hương vào ngày 29 chủ nhật";
//        findDayInWeek(sentence);
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "Gặp cô Hương vào ngày 29 chủ nhật tuần sau";
//        findDayInWeek(sentence);
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//
//        System.out.println("---------");
//        sentence = "Gặp cô Hương vào ngày 29";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "Gặp cô Hương vào thứ hai";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//
//        System.out.println("---------");
//        sentence = "Gặp cô Hương vào thứ hai tuần sau";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "Gặp cô Hương vào chủ nhật";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "Gặp cô Hương vào chủ nhật tuần sau";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "Gặp cô Hương vào thứ sáu";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "Gặp cô Hương vào thứ sáu tuần sau";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//        System.out.println("---------");
//        sentence = "Nhắc nhở tôi sáng Thứ 6 tuần sau lúc 9 gặp cô Hương";
//        System.out.println(sentence);
//        System.out.println("Hour: " + findHour(sentence));
//        System.out.println("Day: " + findDay(sentence));
//        System.out.println("Month: " + findMonth(sentence));
//        t = getTimestamp(sentence);
//        System.out.println("Time: " + t);
//        s = convertMilliseconds2DateFormat(t * 1000);
//        System.out.println("Time string: " + s);
//
//
//
//
//        findDayInWeek(sentence);
//
//
//        getNextNumber("năm hôm tới năm tôi có sự kiện nào không");
//    }

}
