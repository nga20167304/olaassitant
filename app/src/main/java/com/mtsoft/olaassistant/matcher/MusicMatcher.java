package com.mtsoft.olaassistant.matcher;

import java.util.HashMap;

/**
 * Created by manhhung on 5/8/19.
 */

public class MusicMatcher {
    public static float getScoreMusic(String sentence) {
        HashMap<String, Float> dicFeatureWords = new HashMap<String, Float>();

        sentence = sentence.toLowerCase().trim();
        dicFeatureWords.put("nghe", 1.0f);
        dicFeatureWords.put("tìm kiếm", 0.9f);
        dicFeatureWords.put("nghe bài", 2.0f);
        dicFeatureWords.put("bài hát", 1.8f);
        dicFeatureWords.put("bài", 0.9f);
        dicFeatureWords.put("zing mp3", 0.9f);
        dicFeatureWords.put("zing mp3", 0.9f);
        dicFeatureWords.put("trên zing", 1.8f);
        dicFeatureWords.put("trên zingmp3", 1.9f);
        dicFeatureWords.put("ca sĩ", 1.2f);
        dicFeatureWords.put("nhạc sĩ", 1.0f);
        dicFeatureWords.put("của", 0.9f);
        dicFeatureWords.put("mở", 0.9f);
        dicFeatureWords.put("bản nhạc", 1.8f);
        dicFeatureWords.put("ca khúc", 1.8f);
        dicFeatureWords.put("album", 0.9f);
        dicFeatureWords.put("nhạc", 0.9f);
        float score = 0;
        for (String key : dicFeatureWords.keySet()) {
            if (sentence.contains(key)) {
                score += dicFeatureWords.get(key);
            }
        }
        return score;
    }


    public static String getSong(String sentence) {
        if (getScoreMusic(sentence) > 1.7999) {
            sentence = sentence.toLowerCase().trim();
            String featureSongs[] = {"bài hát", "ca khúc", "bài nhạc", "bản nhạc", "bài", "mở", "nhạc"};
            for (int i = 0; i < featureSongs.length; i++) {
                if (sentence.contains(featureSongs[i])) {
                    return sentence.substring(sentence.indexOf(featureSongs[i]) + featureSongs[i].length()).trim();
                }
            }
        }
        return null;
    }
}
