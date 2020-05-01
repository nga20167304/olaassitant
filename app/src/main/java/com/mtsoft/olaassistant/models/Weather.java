package com.mtsoft.olaassistant.models;

/**
 * Created by manhhung on 3/31/19.
 */

public class Weather {
    private Long time;
    private String summary;
    private String icon;
    private String windSpeed;
    private Double humidity;

    public Weather(Long time, String summary, String icon, String windSpeed, Double humidity) {
        this.time = time;
        this.summary = summary;
        this.icon = icon;
        this.windSpeed = windSpeed;
        this.humidity = humidity;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public Double getHumidity() {
        return humidity;
    }

    public void setHumidity(Double humidity) {
        this.humidity = humidity;
    }


    @Override
    public String toString() {
        return "Weather{" +
                "time=" + time +
                ", summary='" + summary + '\'' +
                ", icon='" + icon + '\'' +
                ", windSpeed='" + windSpeed + '\'' +
                ", humidity=" + humidity +
                '}';
    }
}
