package com.example.groupandmessage;

public class MessageModel {
    String messageName, message, messageId;

    public MessageModel(String messageName,String message, String messageId){
        this.messageName = messageName;
        this.message = message;
        this.messageId = messageId;
    }
    public String getMessageName(){
        return messageName;
    }

    public void setMessageName(String messageName) {
        this.messageName = messageName;
    }
    public String getMessage(){
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public String getMessageId(){
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }
}
