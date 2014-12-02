/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcht;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Message for the chat application
 * @author hth
 */
public class ChatMessage implements Serializable {
    
    private String SenderName;
    private String receiverName;
    private LocalDateTime timeStamp;
    private Object data;

    public String getSenderName() {
        return SenderName;
    }

    public void setSenderName(String SenderName) {
        this.SenderName = SenderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public void setReceiverName(String receiverName) {
        this.receiverName = receiverName;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }   
    
    public String getMessage() {
        return data.toString();
    }

    public void setMessage(String msg) {
        this.data = msg;
    }   
    
}
