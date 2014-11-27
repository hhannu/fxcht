/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fxcht;

import java.util.Collections;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javax.swing.JOptionPane;

/**
 *
 * @author hth
 */
public class UI extends VBox implements EventHandler{
    
    private final boolean serverMode;
    private String userName;
    private int selectedIndex;
    private String selectedUser;
    private final HBox chatBox = new HBox(5);
    private final HBox msgBox = new HBox(5);
    private final VBox userBox = new VBox(5);
    private final TextArea chat = new TextArea();
    private final TextField message = new TextField();
    private final Label msgLabel = new Label("Message:");
    private ListView lw = new ListView();
    private MenuBar mBar;
    private Menu fileMenu, helpMenu;
    private MenuItem closeItem, clearItem, aboutItem;    
    
    private Button closeButton;        
    private Button sendButton;       
    private Button dcButton;    
    
    // Names to show on ListView
    ObservableList<String> names = FXCollections.observableArrayList();
    
    public UI(boolean serverMode) {
        this.serverMode = serverMode;
        userName = "user";
        selectedIndex = 0;
        selectedUser = "";
        chat.setMinWidth(320);
        chat.setMaxWidth(320);
        chat.setEditable(false);
        chat.setWrapText(true);
        lw.setMinWidth(160);
        lw.setMaxWidth(160);
        lw.setEditable(false);
        if(this.serverMode) {
            userBox.getChildren().add(lw);        
            dcButton = new Button("Disconnect"); 
            dcButton.setOnAction(this);
            userBox.getChildren().add(dcButton);         
            chatBox.getChildren().add(userBox);
        }
        else
            chatBox.getChildren().add(lw);              
        chatBox.getChildren().add(chat);        
        chatBox.setPadding(new Insets(5, 5, 5, 5));  
        this.setupMenu();        
        this.getChildren().add(mBar);
        this.getChildren().add(chatBox);
        names.add(0, "");
        if(names.size() > 1)
            Collections.sort(names);
        lw.setItems(names);
        // Add a listener to ListView to track the selected name.
        lw.getSelectionModel().selectedItemProperty().addListener(
            new ChangeListener<String>() {
                @Override
                public void changed(ObservableValue<? extends String> ov, 
                    String old_val, String new_val) {
                    selectedIndex = lw.getSelectionModel().getSelectedIndex();
                    selectedUser = names.get(selectedIndex);
            }
        });
        msgBox.setPadding(new Insets(10, 10, 10, 10));  
        msgBox.getChildren().add(msgLabel);
        message.setMinWidth(320);
        message.setMaxWidth(320);
        message.setOnAction(this);
        msgBox.getChildren().add(message);
        sendButton = new Button("Send");
        sendButton.setOnAction(this);
        msgBox.getChildren().add(sendButton);
        msgBox.setAlignment(Pos.CENTER);
        this.getChildren().add(msgBox);
    }  
    
    private void setupMenu() {
        mBar = new MenuBar();
        fileMenu = new Menu("File");
        helpMenu = new Menu("Help");
        closeItem = new MenuItem("Close");
        closeItem.setOnAction(this);
        clearItem = new MenuItem("Clear chat");
        clearItem.setOnAction(this);
        aboutItem = new MenuItem("About");
        aboutItem.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent e) {
                JOptionPane.showMessageDialog(null, "JavaFX exercise for the Software development course 2014.\n\n" +
                "Java version: " + System.getProperty("java.version") + "\nOS: " +
                System.getProperty("os.name") + " " + System.getProperty("os.version") + " " + System.getProperty("os.arch"), 
                "About FXAddressBook", JOptionPane.INFORMATION_MESSAGE);
            }
        });
        fileMenu.getItems().add(clearItem);
        fileMenu.getItems().add(closeItem);
        helpMenu.getItems().add(aboutItem);
        mBar.getMenus().addAll(fileMenu);
        mBar.getMenus().addAll(helpMenu);
    }  

    @Override
    public void handle(Event event) {
        if(event.getSource().equals(sendButton) || event.getSource().equals(message)) {
            String msg = message.getText();
            if(!msg.equals(""))
                sendMessage(msg);
        }
        else if(event.getSource().equals(closeItem)) {
            Platform.exit();
        }
        else if(event.getSource().equals(clearItem)) {
            chat.setText("");
        }
        else if(event.getSource().equals(dcButton)) {
            if(selectedIndex != 0) {
                names.remove(selectedIndex);
                selectedIndex = 0;
                selectedUser = "";
                lw.getSelectionModel().select(selectedIndex);
            }
            // TODO: disconnect user
        }
    }
    
    private void sendMessage(String msg) {
        message.setText("");
        chat.appendText("\n" + userName + (selectedUser.equals("") ? ": " : "->" + selectedUser + ": ") + msg);
        // TODO: send message        
    }
}
