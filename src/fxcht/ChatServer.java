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

    private ArrayList<ServerClient> clients = new ArrayList();
    private String userName;
    private int port;
    private UI ui;
    
    public ChatServer(UI ui) {
        this.ui = ui;
    }
    
    public ChatServer(UI ui, int port) {
        this.ui = ui;
        this.port = port;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }   

    @Override
    public void run() {
        try {
            int i = 1;
            //Start the server to listen port 3010
            ServerSocket server = new ServerSocket(port);
            
            ServerClient srv = new ServerClient(0, null, null);
            srv.setServer(null);
            srv.setUserName(userName);
            clients.add(srv);
                
            //Start to listen and wait connections
            while(true) {
                Socket temp = server.accept();
                ServerClient client = new ServerClient(i++, temp, ui);
                client.setServer(this);
                clients.add(client);
                Thread t = new Thread(client);
                t.setDaemon(true);
                t.start();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }   
    
    public void broadcastMessage(ChatMessage cm, int excludeId) {
        for(ServerClient temp : clients) {
            if(temp.getId() != excludeId && temp.getId() != 0)
                temp.sendMessage(cm);
        }
    }
    
    public void sendUserNames(int clientId) {
        ArrayList<String> users = new ArrayList();
        ServerClient client = null;
        for(ServerClient temp : clients) {
            users.add(temp.getUserName());
            if(temp.getId() == clientId)
                client = temp;
        }
        StatusMessage sm = new StatusMessage();
        sm.setLogInReply(true);
        sm.setLogInStatus(true);
        sm.setData(users);        
        client.sendMessage(sm);
    }
    
    public void sendTo(ChatMessage m) {
        String receiver = m.getReceiverName();
        if(receiver.equals(userName)) {
            return;
        }
        System.out.println("sendTo " + receiver);
        for(ServerClient temp : clients) {
            if(temp.getUserName().equals(receiver)) {
                temp.sendMessage(m);
                return;
            }
        }
    }
    
    public void removeClient(int clientId) {        
        for(ServerClient temp : clients) {
            if(temp.getId() == clientId) {                              
                StatusMessage sm = new StatusMessage();
                sm.setUserLeft(true);
                sm.setData((String)temp.getUserName());
                clients.remove(temp);    
                broadcastMessage(sm, 0);
                return;
            }
        }
    } 
}
