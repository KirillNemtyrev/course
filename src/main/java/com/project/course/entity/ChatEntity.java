package com.project.course.entity;

import java.util.Arrays;

public class ChatEntity {
    private String username;
    private String pathToPhoto;
    private MessageEntity[] messages;

    public String getUsername() {
        return username;
    }

    public String getPathToPhoto() {
        return pathToPhoto;
    }

    public MessageEntity[] getMessages() {
        return messages;
    }

    public MessageEntity getLastMessage(){
        return messages.length != 0 ? messages[messages.length - 1] : null;
    }

    public void addMessage(MessageEntity messageEntity) {
        MessageEntity[] entities = Arrays.copyOf(messages, messages.length + 1);
        entities[messages.length] = messageEntity;
        messages = Arrays.copyOf(entities, entities.length);

    }
}
