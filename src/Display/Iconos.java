package Display;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class Iconos extends AnchorPane{
    @FXML AnchorPane root_pane;
    @FXML Label iconoTitle_bar;
    private TiposdeIconos mType;
    
    public Iconos()
    {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Display/FXMLs/IconoPrueba.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        
        try
        {
            fxmlLoader.load();
        }catch(IOException ex){throw new RuntimeException(ex);}
    }
    
    @FXML
private void initialize() {}

public void relocateToPoint (Point2D p) {

    Point2D localCoords = getParent().sceneToLocal(p);

    relocate (
        (int) (localCoords.getX() - (getBoundsInLocal().getWidth() / 2)),
        (int) (localCoords.getY() - (getBoundsInLocal().getHeight() / 2))
    );
}
    
    public TiposdeIconos getType(){return mType;}
    
    public void setType(TiposdeIconos type){
        
        mType = type;
        
        getStyleClass().clear();
        getStyleClass().add("dragicon");
        switch(mType){
        
        case Entero:
        iconoTitle_bar.setText("Entero");
        getStyleClass().add("icon-blue");
        break;
            
        case Flotante:
        iconoTitle_bar.setText("Flotante");
        getStyleClass().add("icon-red");
        break;
            
        case Doble:
        iconoTitle_bar.setText("Doble");
        getStyleClass().add("icon-grey");
        break;
            
        case Texto:
        iconoTitle_bar.setText("Texto");
        getStyleClass().add("icon-yellow");
        break;
        
        case Leer:
        iconoTitle_bar.setText("Leer");
        getStyleClass().add("icon-green");
        break;
        
        case Mostrar:
        iconoTitle_bar.setText("Mostrar");
        getStyleClass().add("icon-purple");
        break;
        }
    }
}
