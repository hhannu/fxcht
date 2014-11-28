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
import javafx.application.Platform;

/**
 *
 * @author hth
 */
public class ChatClient implements Runnable {

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
                System.out.println(m.getMessage());
                
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {                                   
                        if(m instanceof StatusMessage) {
                            // TODO: handle statusmessage
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