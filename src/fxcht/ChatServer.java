/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcht;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 *
 * @author hth
 */
public class ChatServer implements Runnable {

    private static ArrayList<ChatClient> clients = new ArrayList();
    
    private int port;
    private UI ui;
    
    public ChatServer(UI ui) {
        this.ui = ui;
    }
    
    public ChatServer(UI ui, int port) {
        this.ui = ui;
        this.port = port;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }       
    
    public static void broadcastMessage(ChatMessage cm) {
        for(ChatClient temp : clients) {
            temp.sendMessage(cm);
        }
    }

    @Override
    public void run() {
        try {
            //Start the server to listen port 3010
            ServerSocket server = new ServerSocket(port);
            
            //Start to listen and wait connections
            while(true) {
                Socket temp = server.accept();
                ChatClient client = new ChatClient(ui, temp);
                clients.add(client);
                Thread t = new Thread(client);
                t.setDaemon(true);
                t.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }    
}
