/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcht;

import java.util.Scanner;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

/**
 *
 * @author hth
 */
public class FxCht extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        StackPane root = new StackPane();
        boolean servermode = true;
        String userName = "BOFH";
        
        // TODO: dialog to ask server/client mode, username, address, ...
        
//        System.out.print("UserName: ");
//        
//        Scanner sc = new Scanner(System.in);
//        userName = sc.nextLine();
        
        UI ui = new UI(userName, servermode);
        // Start Server
        if(servermode) {
            ChatServer server = new ChatServer(ui, 3010);
            server.setUserName(userName);
            ui.setServer(server);
            Thread serverThread = new Thread(server);
            serverThread.setDaemon(true);
            serverThread.start();
        }
        // Start Client
        else {
            ChatClient client = new ChatClient(ui);
            client.connect("127.0.0.1", 3010);
            ui.setClient(client);
            Thread clientThread = new Thread(client);
            clientThread.setDaemon(true);
            clientThread.start();
            
            StatusMessage sm = new StatusMessage();
            sm.setLogInMessage(true);
            sm.setSenderName(userName);
            client.sendMessage(sm);
        }
        
        root.getChildren().add(ui);
        
        Scene scene = new Scene(root);
        
        primaryStage.setTitle("FxCht");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
