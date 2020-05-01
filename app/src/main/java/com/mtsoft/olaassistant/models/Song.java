package com.mtsoft.olaassistant.models;

/**
 * Created by manhhung on 3/29/19.
 */

public class Song {
    private String artist;
    private String name;
    private String id;
    private String type;

    public Song(String artist, String name, String id, String type) {
        this.artist = artist;
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Song{" +
                "artist='" + artist + '\'' +
                ", name='" + name + '\'' +
                ", id='" + id + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
