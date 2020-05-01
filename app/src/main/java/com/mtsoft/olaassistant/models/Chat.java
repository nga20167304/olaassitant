package com.mtsoft.olaassistant.models;

import java.io.Serializable;

/**
 * Created by manhhung on 5/5/17.
 */

public class Chat implements Serializable {
    private String content;
    private String person;


    public Chat(String content, String person) {
        this.content = content;
        this.person = person;
    }

    public Chat() {
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPerson() {
        return person;
    }

    public void setPerson(String person) {
        this.person = person;
    }


}
