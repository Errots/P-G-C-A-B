/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Display;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

/**
 *
 * @author Errot
 */
public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        
        BorderPane root = new BorderPane();
        try{
        Scene scene = new Scene(root, 640, 480);
        scene.getStylesheets().add(getClass().getResource("/Display/FXMLs/Aplicacion.css").toExternalForm());        
        primaryStage.setScene(scene);
        primaryStage.show();
        }catch(Exception e){e.printStackTrace();}
        root.setCenter(new RootLayout());
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
}
