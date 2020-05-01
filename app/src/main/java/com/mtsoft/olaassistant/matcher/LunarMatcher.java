package com.mtsoft.olaassistant.matcher;

/**
 * Created by manhhung on 5/8/19.
 */

public class LunarMatcher {
    public static boolean isQuestionLunar(String sentence) {
        sentence = sentence.toLowerCase();

        if (sentence.contains("âm lịch") || sentence.contains("ngày âm") ||
                (sentence.contains("âm")) && sentence.contains("nhiêu")) {
            return true;
        }

        return false;
    }
}
