package com.mtsoft.olaassistant.matcher;

import java.util.HashMap;

/**
 * Created by manhhung on 5/8/19.
 */

public class VideoMatcher {
    public static float getScoreVideo(String sentence) {
        HashMap<String, Float> dicFeatureWords = new HashMap<String, Float>();

        sentence = sentence.toLowerCase().trim();
        dicFeatureWords.put("tìm kiếm", 0.9f);
        dicFeatureWords.put("xem", 0.9f);
        dicFeatureWords.put("video", 0.9f);
        dicFeatureWords.put("youtube", 0.9f);
        dicFeatureWords.put("trên youtube", 1.8f);
        dicFeatureWords.put("của", 0.9f);
        dicFeatureWords.put("mở", 0.9f);
        dicFeatureWords.put("bật", 0.9f);
        dicFeatureWords.put("mv", 0.9f);
        float score = 0;
        for (String key : dicFeatureWords.keySet()) {
            if (sentence.contains(key)) {
                score += dicFeatureWords.get(key);
            }
        }
        return score;
    }


    public static String getVideo(String sentence) {
        if (getScoreVideo(sentence) > 1.7999) {
            sentence.replaceAll("youtube", "");
            sentence.replaceAll("trên youtube", "");
            sentence = sentence.toLowerCase().trim();

            String featureSongs[] = {"video", "mv", "mở", "bật", "xem"};
            for (int i = 0; i < featureSongs.length; i++) {
                if (sentence.contains(featureSongs[i])) {
                    return sentence.substring(sentence.indexOf(featureSongs[i]) + featureSongs[i].length()).trim();
                }
            }
        }
        return null;
    }
}
