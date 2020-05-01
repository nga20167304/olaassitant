package com.mtsoft.olaassistant.models;

/**
 * Created by manhhung on 3/31/19.
 */

public class WeatherHourly extends Weather{
    private Double temperature;

    public WeatherHourly(Long time, String summary, String icon, String windSpeed, Double humidity) {
        super(time, summary, icon, windSpeed, humidity);
    }

    public WeatherHourly(Long time, String summary, String icon, String windSpeed, Double humidity, Double temperature) {
        super(time, summary, icon, windSpeed, humidity);
        this.temperature = temperature;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }


}
