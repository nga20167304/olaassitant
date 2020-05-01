package com.mtsoft.olaassistant.models;

/**
 * Created by manhhung on 3/31/19.
 */

public class WeatherDaily extends Weather {
    private Double temperatureHigh;
    private Long temperatureHighTime;

    private Double temperatureLow;
    private Long temperatureLowTime;


    private Double temperatureMin;
    private Long temperatureMinTime;

    private Double temperatureMax;
    private Long temperatureMaxTime;


    public WeatherDaily(Long time, String summary, String icon, String windSpeed,
                        Double humidity, Double temperatureHigh, Long temperatureHighTime,
                        Double temperatureLow, Long temperatureLowTime, Double temperatureMin,
                        Long temperatureMinTime, Double temperatureMax, Long temperatureMaxTime) {
        super(time, summary, icon, windSpeed, humidity);
        this.temperatureHigh = temperatureHigh;
        this.temperatureHighTime = temperatureHighTime;
        this.temperatureLow = temperatureLow;
        this.temperatureLowTime = temperatureLowTime;
        this.temperatureMin = temperatureMin;
        this.temperatureMinTime = temperatureMinTime;
        this.temperatureMax = temperatureMax;
        this.temperatureMaxTime = temperatureMaxTime;
    }

    public Double getTemperatureHigh() {
        return temperatureHigh;
    }

    public void setTemperatureHigh(Double temperatureHigh) {
        this.temperatureHigh = temperatureHigh;
    }

    public Long getTemperatureHighTime() {
        return temperatureHighTime;
    }

    public void setTemperatureHighTime(Long temperatureHighTime) {
        this.temperatureHighTime = temperatureHighTime;
    }

    public Double getTemperatureLow() {
        return temperatureLow;
    }

    public void setTemperatureLow(Double temperatureLow) {
        this.temperatureLow = temperatureLow;
    }

    public Long getTemperatureLowTime() {
        return temperatureLowTime;
    }

    public void setTemperatureLowTime(Long temperatureLowTime) {
        this.temperatureLowTime = temperatureLowTime;
    }

    public Double getTemperatureMin() {
        return temperatureMin;
    }

    public void setTemperatureMin(Double temperatureMin) {
        this.temperatureMin = temperatureMin;
    }

    public Long getTemperatureMinTime() {
        return temperatureMinTime;
    }

    public void setTemperatureMinTime(Long temperatureMinTime) {
        this.temperatureMinTime = temperatureMinTime;
    }

    public Double getTemperatureMax() {
        return temperatureMax;
    }

    public void setTemperatureMax(Double temperatureMax) {
        this.temperatureMax = temperatureMax;
    }

    public Long getTemperatureMaxTime() {
        return temperatureMaxTime;
    }

    public void setTemperatureMaxTime(Long temperatureMaxTime) {
        this.temperatureMaxTime = temperatureMaxTime;
    }
}
