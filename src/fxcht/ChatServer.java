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
public class ChatServer {

    static ArrayList<ChatClient> clients = new ArrayList();
    
    public static void main(String[] args) {
        try {
            //Start the server to listen port 3010
            ServerSocket server = new ServerSocket(3010);
            
            //Start to listen and wait connections
            while(true) {
                Socket temp = server.accept();
                ChatClient client = new ChatClient(temp);
                clients.add(client);
                Thread t = new Thread(client);
                t.setDaemon(true);
                t.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
    
    public static void broadcastMessage(ChatMessage cm) {
        for(ChatClient temp : clients) {
            temp.sendMessage(cm);
        }
    }
    
}
