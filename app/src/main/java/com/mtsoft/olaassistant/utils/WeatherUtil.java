package com.mtsoft.olaassistant.utils;

/**
 * Created by manhhung on 3/31/19.
 */

public class WeatherUtil {
    public static Double convertF2C(Double f) {
        Double t = (f - 32) * 5 / 9;
        return (double) Math.round(t * 10) / 10 ;
    }
}
