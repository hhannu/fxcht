/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcht;

import java.io.Serializable;

/**
 *
 * @author hth
 */
public class StatusMessage extends ChatMessage implements Serializable {
    
    private boolean logInMessage;
    private boolean logInReply;
    private boolean logInStatus;
    private boolean logOutMessage;
    private boolean userJoined;
    private boolean userLeft;
    private String passWord;

    public boolean isLogInMessage() {
        return logInMessage;
    }

    public void setLogInMessage(boolean logInMessage) {
        this.logInMessage = logInMessage;
    }

    public boolean isLogInReply() {
        return logInReply;
    }

    public void setLogInReply(boolean logInReply) {
        this.logInReply = logInReply;
    }

    public boolean getLogInStatus() {
        return logInStatus;
    }

    public void setLogInStatus(boolean logInStatus) {
        this.logInStatus = logInStatus;
    }

    public boolean isLogOutMessage() {
        return logOutMessage;
    }

    public void setLogOutMessage(boolean logOutMessage) {
        this.logOutMessage = logOutMessage;
    }

    public boolean isUserJoined() {
        return userJoined;
    }

    public void setUserJoined(boolean userJoined) {
        this.userJoined = userJoined;
    }

    public boolean isUserLeft() {
        return userLeft;
    }

    public void setUserLeft(boolean userLeft) {
        this.userLeft = userLeft;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

}