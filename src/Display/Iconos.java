package Display;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;

public class Iconos extends AnchorPane{
    Tooltip nota = new Tooltip();
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
private void initialize() {    
    iconoTitle_bar.setTooltip(nota);
}

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
        nota.setText("\"Integer\" este tipo de datos solo acepta numeros enteros.  ");
        break;
            
        case Flotante:
        iconoTitle_bar.setText("Flotante");
        getStyleClass().add("icon-red");
        nota.setText("\"Integer\" este tipo de datos solo acepta numeros decimales y es impreciso.  ");
        break;
            
        case Doble:
        iconoTitle_bar.setText("Doble");
        getStyleClass().add("icon-grey");
        nota.setText("\"Double\" este tipo de datos solo acepta numeros decimales.  ");
        break;
            
        case Texto:
        iconoTitle_bar.setText("Texto");
        getStyleClass().add("icon-yellow");
        nota.setText("\"String\" este tipo de datos solo acepta lineas de caracteres o texto.  ");
        break;
        
        case Leer:
        iconoTitle_bar.setText("Leer");
        getStyleClass().add("icon-green");
        nota.setText("\"Scanner\" recoje y almacena el valor escrito desde la barra de comandos.  ");
        break;
        
        case Mostrar:
        iconoTitle_bar.setText("Mostrar");
        getStyleClass().add("icon-purple");
        nota.setText("\"System.out.print\" imprime cualquier tipo de dato que se le introdusca.  ");
        break;
        }
    }
}
