package com.ch.domain;

import java.io.Serializable;

/**
 * Created by banmo.ch on 16/11/10.
 * Email: banmo.ch@alibaba-inc.com
 */
public class Message implements Serializable{
    private static final long serialVersionUID = 7618420761480379523L;
    private String message;
    private String type;

    public Message() {
    }

    public Message(String type, String message) {
        this.message = message;
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Message{" +
                "message='" + message + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
