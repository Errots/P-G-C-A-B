package CGenerator;

import Display.IconDrag;
import java.io.IOException;
import java.util.ArrayList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class PreCodeDisplay extends AnchorPane {
    
    @FXML TextArea Output_Handle;
    @FXML Button SaveBtn_Handle;
    @FXML Button QuitBtn_Handle;
    
    private EventHandler mQuitBtnClicked=null;
    private EventHandler mSaveBtnClicked=null;
    
    ArrayList<String> comandos,incluidores;
    
    public PreCodeDisplay() throws IOException{
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("/Display/FXMLs/PreCodeDisplay.fxml"));
        
        
        fxmlLoader.setController(this);
        
        Scene scene = new Scene(fxmlLoader.load(), 300, 480);
        Stage stage = new Stage();
        stage.setTitle("Vista de codigo");
        stage.getIcons().add(new Image("/Display/Iconos/PGCAB.png"));
        stage.setScene(scene);
        stage.show();
        
                
    }
    
    @FXML
    private void initialize()
    {
        buildMouseEventHandle();
        Output_Handle.setText("");
        Output_Handle.setStyle("-fx-opacity: 1.0;");
        SaveBtn_Handle.setOnAction(mSaveBtnClicked);
        QuitBtn_Handle.setOnAction(mQuitBtnClicked);
        
    }
    
    public void WriteText(ArrayList<String> textos,ArrayList<String> imports)
    {
        comandos = textos;
        incluidores = imports;
        for(String text:incluidores)
        {
            Output_Handle.appendText(text+"\n");
        }
        Output_Handle.appendText("public static void main(String[] args){\npublic class Main{\n");
        for(String text:textos)
        {
            Output_Handle.appendText(text+"\n");
        }
        Output_Handle.appendText("}}");
    }
    
    private void buildMouseEventHandle()
    {
        mQuitBtnClicked = new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                Stage stage = (Stage) QuitBtn_Handle.getScene().getWindow();
                stage.close();
                event.consume();
            }
        };
        
         mSaveBtnClicked = new EventHandler<ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                GeneradorArchivo archivo = new GeneradorArchivo();
                archivo.CrearArchivo(comandos,incluidores);
                Stage stage = (Stage) SaveBtn_Handle.getScene().getWindow();
                stage.close();
                event.consume();
            }
        };
    }
    
    

    
    
}
