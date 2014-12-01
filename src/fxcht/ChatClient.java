/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcht;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import javafx.application.Platform;

/**
 *
 * @author hth
 */
public class ChatClient implements Runnable {

    private int id;
    private String userName;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private UI ui;
    
    public ChatClient(UI u) {
        ui = u;
    }
    
    public ChatClient(UI u, Socket sock) {
        ui = u;
        socket = sock;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
    
    public boolean connect(String address, int port) {
        try {
            socket = new Socket(address, port);
            output = new ObjectOutputStream(socket.getOutputStream());            
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public void run() {
        try {            
            input = new ObjectInputStream(socket.getInputStream());
            
            while(true) {
                ChatMessage m = (ChatMessage)input.readObject();
                //System.out.println(m.getMessage());
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {                          
                        if(m instanceof StatusMessage) {
                            StatusMessage sm = (StatusMessage)m;
                            if(sm.isLogOutMessage()) {
                                ui.setStatusBarText("You have been disconnected");
                                ui.enableUi(false);
                                return;
                            }
                            if(sm.isLogInReply()) {
                                System.out.println("loginreply...");
                                ui.setUserNames((ArrayList<String>)sm.getData());
                                ui.setStatusBarText("Connected to " + socket.getInetAddress().getHostName() + 
                                        " as " + userName);
                            }
                            else if(sm.isUserJoined()) {
                                System.out.println("userjoined... new user: " + (String)sm.getData());
                                ui.addUserName((String)sm.getData());
                            }
                            else if(sm.isUserLeft()) {
                                System.out.println("userleft... user: " + (String)sm.getData());
                                ui.removeUserName((String)sm.getData());
                            }
                        }
                        else
                            ui.setMessage(m);
                    }
                });
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();            
        }        
    }    
    
    public void sendMessage(ChatMessage cm) {
        try {
            output.writeObject(cm);
            output.flush();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}