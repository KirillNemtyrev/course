package com.project.course.entity;

import java.util.Date;

public class MessageEntity {
    private boolean bot;
    private String message;
    private Date date;

    public MessageEntity(boolean bot, String message, Date date) {
        this.bot = bot;
        this.message = message;
        this.date = date;
    }

    public boolean isBot() {
        return bot;
    }

    public String getMessage() {
        return message;
    }

    public Date getDate() {
        return date;
    }
}
