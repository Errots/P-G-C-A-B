/*
/////////////////////////////////////////////////////////////////////////////
/// Author: Erick Roberto Sala Noverola (Alias: Errots)                   ///
/// Fecha: 06/11/2018                                                     ///
/// Todo lo que se incluye en el paquete display                          ///
/// esta basado en el codigo de Joel Graff                                ///
///                                                                       ///
/// https://monograff76.wordpress.com/2015/02/17                          ///
///  /developing-a-drag-and-drop-ui-in-javafx-part-i-skeleton-application ///
/////////////////////////////////////////////////////////////////////////////
 */
package Display;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class Main extends Application {
    
    @Override
    public void start(Stage primaryStage) {
        
        primaryStage.setTitle("GCAB");
        primaryStage.getIcons().add(new Image("/Display/Iconos/PGCAB.png"));
        BorderPane root = new BorderPane();
        try{
        
        Scene scene = new Scene(root, 640, 480);
        scene.getStylesheets().add(getClass().getResource("/Display/FXMLs/Aplicacion.css").toExternalForm());        
        primaryStage.setScene(scene);
        primaryStage.show();
        }catch(Exception e){e.printStackTrace();}
        root.setCenter(new RootLayout());
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent event) {
                Platform.exit();
            }
        });
    }

    
    public static void main(String[] args) {
        launch(args);
    }
    
    
    
}
