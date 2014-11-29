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
public class ServerClient implements Runnable {

    private int id;
    private String userName;
    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private UI ui;
    private ChatServer server;
    
    public ServerClient(UI u) {
        ui = u;
    }
    
    public ServerClient(int i, Socket sock, UI u) {    
        id = i;
        socket = sock;
        ui = u;
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

    public ChatServer getServer() {
        return server;
    }

    public void setServer(ChatServer server) {
        this.server = server;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }
    
    @Override
    public void run() {
        try {            
            output = new ObjectOutputStream(socket.getOutputStream()); 
            input = new ObjectInputStream(socket.getInputStream());
            
            while(true) {
                ChatMessage m = (ChatMessage)input.readObject();
                //System.out.println(m.getMessage());               

                // Handle server messages
                if(m instanceof StatusMessage) {
                    StatusMessage sm = (StatusMessage)m;
                    if(sm.isLogOutMessage()) {
                        return;
                    }
                    if(sm.isLogInMessage()) {
                        System.out.println("loginmessage from " + sm.getSenderName());
                        userName = sm.getSenderName();                        
                        //TODO: check username on server
                        server.sendUserNames(id); 
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {  
                                ui.addUserName(userName); 
                            }
                        }); 

                        // send other clients the new username
                        StatusMessage rm = new StatusMessage();
                        rm.setUserJoined(true);
                        rm.setData(userName);
                        server.broadcastMessage(rm, id);
                    }
                }
                else {
                    System.out.println(m.getSenderName() +
                    (m.getReceiverName().equals("") ? "" : "->" + m.getReceiverName()) +
                    ": " + m.getMessage()); 
                    
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {                                  
                            ui.setMessage(m);
                        }
                    });
                    if(m.getReceiverName().equals("")) {
                        System.out.println("serverclient broadcastMessage");
                        server.broadcastMessage(m, 0);  
                    }
                    else
                        server.sendTo(m);
                }
            }
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();  
            server.removeClient(id);
            Platform.runLater(new Runnable() {
                @Override
                public void run() {   
                    ui.removeUserName(userName);
                }
            });         
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