package com.mtsoft.olaassistant.models;

/**
 * Created by manhhung on 4/15/19.
 */

public class Event {
    private Long id;
    private String title;
    private String location;
    private String time;

    public Event(Long id, String title, String location, String time) {
        this.id = id;
        this.title = title;
        this.location = location;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
