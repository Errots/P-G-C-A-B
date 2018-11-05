/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Display;


import java.io.IOException;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;


/**
 *
 * @author Errot
 */
public class ItemPrueba extends AnchorPane {
    
    @FXML AnchorPane root_pane;
    private TipoItemPrueba mType;
    
public ItemPrueba() {
    
    FXMLLoader fxmlLoader = new FXMLLoader(
    getClass().getResource("/Display/FXMLs/IconoPrueba.fxml")
    );
        
    fxmlLoader.setRoot(this); 
    fxmlLoader.setController(this);
        
    try { 
    fxmlLoader.load();
        
    } catch (IOException exception) {
    throw new RuntimeException(exception);
    }
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

public TipoItemPrueba getType() { return mType;}

public void setType(TipoItemPrueba type) {

    mType = type;

    getStyleClass().clear();
    getStyleClass().add("dragicon");
    switch (mType) {
        
    case blue:
    getStyleClass().add("icon-blue");
    break;

    case red:
    getStyleClass().add("icon-red");            
    break;

    case green:
    getStyleClass().add("icon-green");
    break;

    case grey:
    getStyleClass().add("icon-grey");
    break;

    case purple:
    getStyleClass().add("icon-purple");
    break;

    case yellow:
    getStyleClass().add("icon-yellow");
    break;

    case black:
    getStyleClass().add("icon-black");
    break;
        
    default:
    break;
    }
}
    
}
