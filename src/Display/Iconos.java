package Display;

import java.io.IOException;
import java.util.ResourceBundle;
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
    
    public void setType(TiposdeIconos type,ResourceBundle resourceBundle){
        
        mType = type;
        
        getStyleClass().clear();
        getStyleClass().add("dragicon");
        switch(mType){
        
        case Entero:
        iconoTitle_bar.setText(resourceBundle.getString("Entero"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("EnteroTex"));
        break;
            
        case Flotante:
        iconoTitle_bar.setText(resourceBundle.getString("Flotante"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("FlotanteTex"));
        break;
            
        case Doble:
        iconoTitle_bar.setText(resourceBundle.getString("Doble"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("DobleTex"));
        break;
            
        case Texto:
        iconoTitle_bar.setText(resourceBundle.getString("Texto"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("TextoTex"));
        break;
        
        case Leer:
        iconoTitle_bar.setText(resourceBundle.getString("Leer"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("LeerTex"));
        break;
        
        case Mostrar:
        iconoTitle_bar.setText(resourceBundle.getString("Mostrar"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("MostrarTex"));
        break;
            
        case Mas:
        iconoTitle_bar.setText(resourceBundle.getString("Mas"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("MasTex"));
        break;
            
        case Menos:
        iconoTitle_bar.setText(resourceBundle.getString("Menos"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("MenosTex"));
        break;
            
        case Entre:
        iconoTitle_bar.setText(resourceBundle.getString("Entre"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("EntreTex"));
        break;
        
        case Por:
        iconoTitle_bar.setText(resourceBundle.getString("Por"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("PorTex"));
        break;
            
        case Diferencia:
        iconoTitle_bar.setText(resourceBundle.getString("Diferencia"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("DiferenciaTex"));
        break;
                            
        case Si:
        iconoTitle_bar.setText(resourceBundle.getString("Si"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("SiTex"));
        break;
                                
        case Sino:
        iconoTitle_bar.setText(resourceBundle.getString("Sino"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("SinoTex"));
        break;
                                    
        case Cuando:
        iconoTitle_bar.setText(resourceBundle.getString("Cuando"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("CuandoTex"));
        break;
                                        
        case Mientras:
        iconoTitle_bar.setText(resourceBundle.getString("Mientras"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("MientrasTex"));
        break;
                                            
        case Mientrasque:
        iconoTitle_bar.setText(resourceBundle.getString("Mientrasque"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("MientrasqueTex"));
        break;
                                                
        case Porcada:
        iconoTitle_bar.setText(resourceBundle.getString("Porcada"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("PorcadaTex"));
        break;
                                                    
        case Cada:
        iconoTitle_bar.setText(resourceBundle.getString("Cada"));
        getStyleClass().add("node-overlay");
        nota.setText(resourceBundle.getString("CadaTex"));
        break;
        }
    }
}
