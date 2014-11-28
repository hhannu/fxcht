/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcht;

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
        boolean servermode = false;
        UI ui = new UI(servermode);
        
        // Start Server
        if(servermode) {
            ChatServer server = new ChatServer(ui);
            ui.setServer(server);
        }
        // Start Client
        else {
            ChatClient client = new ChatClient(ui);
            client.connect("127.0.0.1", 3010);
            ui.setClient(client);
            Thread clientThread = new Thread(client);
            clientThread.start();
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
