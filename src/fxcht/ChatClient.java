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

/**
 *
 * @author hth
 */
public class ChatClient implements Runnable {

    private Socket socket;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    
    public ChatClient() {}
    
    public ChatClient(Socket sock) {
        socket = sock;
    }
    
    public boolean connect(String address, int port) {
        try {
            socket = new Socket(address, port);
            
        } catch (IOException ex) {
            ex.printStackTrace();
            return false;
        }
        return true;
    }
    
    @Override
    public void run() {
        try {            
            output = new ObjectOutputStream(socket.getOutputStream());
            input = new ObjectInputStream(socket.getInputStream());
            
            while(true) {
                    ChatMessage m = (ChatMessage)input.readObject();
                    System.out.println(m.getMessage());
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