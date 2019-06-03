/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Display;

import java.io.IOException;
import java.util.UUID;
import javafx.beans.binding.Binding;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.When;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;

public class LinkNodo extends AnchorPane {

@FXML CubicCurve node_link;

private String StartNodeId = "";
private String EndNodeId = "";

private EventHandler mLinkNodeClickEvent;

private final DoubleProperty mControlOffsetX = new SimpleDoubleProperty();
private final DoubleProperty mControlOffsetY = new SimpleDoubleProperty();
private final DoubleProperty mControlDirectionX1 = new SimpleDoubleProperty();
private final DoubleProperty mControlDirectionY1 = new SimpleDoubleProperty();
private final DoubleProperty mControlDirectionX2 = new SimpleDoubleProperty();
private final DoubleProperty mControlDirectionY2 = new SimpleDoubleProperty();

public LinkNodo() {
		
    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/Display/FXMLs/LinkNodo.fxml")
    );
		
    fxmlLoader.setRoot(this); 
    fxmlLoader.setController(this);

    try { 
    fxmlLoader.load();
    } catch (IOException exception) {
     throw new RuntimeException(exception);
    }
    //provide a universally unique identifier for this object
    setId(UUID.randomUUID().toString());
    }	
	

@FXML
private void initialize() {
        
    mControlOffsetX.set(100.0);
    mControlOffsetY.set(50.0);

    mControlDirectionX1.bind(new When (
    node_link.startXProperty().greaterThan(node_link.endXProperty()))
        .then(-1.0).otherwise(1.0));
        
    mControlDirectionX2.bind(new When (
    node_link.startXProperty().greaterThan(node_link.endXProperty()))
        .then(1.0).otherwise(-1.0));

        
    node_link.controlX1Property().bind(
        Bindings.add(
            node_link.startXProperty(),
            mControlOffsetX.multiply(mControlDirectionX1)
        )
    );
        
    node_link.controlX2Property().bind(
        Bindings.add(
            node_link.endXProperty(),
            mControlOffsetX.multiply(mControlDirectionX2)
        )
    );
        
    node_link.controlY1Property().bind(
        Bindings.add(
            node_link.startYProperty(),
            mControlOffsetY.multiply(mControlDirectionY1)
        )
    );
        
    node_link.controlY2Property().bind(
        Bindings.add(
            node_link.endYProperty(),
            mControlOffsetY.multiply(mControlDirectionY2)
        )
    );
    
    
    mLinkNodeClickEvent = new EventHandler <MouseEvent>()
    {
        @Override
        public void handle(MouseEvent event)
        {
            ((AnchorPane) node_link.getParent()).getChildren().remove(node_link);
            event.consume();
        }
        
    };
    
    node_link.setOnMouseClicked(mLinkNodeClickEvent);
}

public void setStart(Point2D startPoint) {

    node_link.setStartX(startPoint.getX());
    node_link.setStartY(startPoint.getY());     
}
    
public void setEnd(Point2D endPoint) {
        
    node_link.setEndX(endPoint.getX());
    node_link.setEndY(endPoint.getY()); 
}

public void bindEnds (IconDrag source, IconDrag target) {
    
    Circle CSource = null;
    Circle CTarget = null;
    VBox SBox= (VBox) source.getChildren().get(0);
    GridPane Pane = (GridPane) SBox.getChildren().get(0);
    CSource = (Circle) Pane.getChildren().get(0);
    VBox TVbox = (VBox)  target.getChildren().get(0);
    VBox TVboxSecond;
    VBox TVboxChild;
    HBox THboxChild;
    GridPane TPane= null;
    switch(target.getType()){
        
            case Entero:
                 TVboxSecond = (VBox)  TVbox.getChildren().get(1);
                 TVboxChild = (VBox)  TVboxSecond.getChildren().get(0);
                for (Node n : TVboxChild.getChildren()) {
                    if (n instanceof GridPane) {
                        TPane = (GridPane) n;
                    }
                }
                
            break;
            case Flotante:
                TVboxSecond = (VBox)  TVbox.getChildren().get(1);
                TVboxChild = (VBox)  TVboxSecond.getChildren().get(0);
                for (Node n : TVboxChild.getChildren()) {
                    if (n instanceof GridPane) {
                        TPane = (GridPane) n;
                    }
                }
            break;
            case Doble:
                TVboxSecond = (VBox)  TVbox.getChildren().get(1);
                TVboxChild = (VBox)  TVboxSecond.getChildren().get(0);
                for (Node n : TVboxChild.getChildren()) {
                    if (n instanceof GridPane) {
                        TPane = (GridPane) n;
                    }
                }
            break;
            case Texto:
                TVboxSecond = (VBox)  TVbox.getChildren().get(1);
                TVboxChild = (VBox)  TVboxSecond.getChildren().get(0);
                for (Node n : TVboxChild.getChildren()) {
                    if (n instanceof GridPane) {
                        TPane = (GridPane) n;
                    }
                }
            break;
            case Leer:
                TVboxSecond = (VBox)  TVbox.getChildren().get(1);
                TVboxChild = (VBox)  TVboxSecond.getChildren().get(0);
                for (Node n : TVboxChild.getChildren()) {
                    if (n instanceof GridPane) {
                        TPane = (GridPane) n;
                    }
                }
            break;
            case Mostrar:
                TVboxSecond = (VBox)  TVbox.getChildren().get(1);
                TVboxChild = (VBox)  TVboxSecond.getChildren().get(0);
                for (Node n : TVboxChild.getChildren()) {
                    if (n instanceof GridPane) {
                        TPane = (GridPane) n;
                    }
                }
            break;
            case Mas:
                
                TVboxSecond = (VBox)  TVbox.getChildren().get(2);
                TVboxChild = (VBox)  TVboxSecond.getChildren().get(0);
                
                    if (target.secondaryValuelink) 
                        TPane = (GridPane) TVboxChild.getChildren().get(2);
                    else TPane = (GridPane) TVboxChild.getChildren().get(0);
                
            break;
            case Menos:
                TVboxSecond = (VBox)  TVbox.getChildren().get(2);
                TVboxChild = (VBox)  TVboxSecond.getChildren().get(0);
                
                    if (target.secondaryValuelink) 
                        TPane = (GridPane) TVboxChild.getChildren().get(2);
                    else TPane = (GridPane) TVboxChild.getChildren().get(0);
                

            break;
            case Entre:
                TVboxSecond = (VBox)  TVbox.getChildren().get(2);
                TVboxChild = (VBox)  TVboxSecond.getChildren().get(0);
                
                    if (target.secondaryValuelink) 
                        TPane = (GridPane) TVboxChild.getChildren().get(2);
                    else TPane = (GridPane) TVboxChild.getChildren().get(0);
                

            break;
            case Por:
                 TVboxSecond = (VBox)  TVbox.getChildren().get(2);
                TVboxChild = (VBox)  TVboxSecond.getChildren().get(0);
                   if (target.secondaryValuelink) 
                        TPane = (GridPane) TVboxChild.getChildren().get(2);
                    else TPane = (GridPane) TVboxChild.getChildren().get(0);
                

            break;
            case Diferencia:
                 TVboxSecond = (VBox)  TVbox.getChildren().get(2);
                TVboxChild = (VBox)  TVboxSecond.getChildren().get(0);
                    if (target.secondaryValuelink) 
                        TPane = (GridPane) TVboxChild.getChildren().get(2);
                    else TPane = (GridPane) TVboxChild.getChildren().get(0);
                

            break;
            case Si:
                 TVboxSecond = (VBox)  TVbox.getChildren().get(3);
                 THboxChild = (HBox)  TVboxSecond.getChildren().get(0);
                    if (target.secondaryValuelink) 
                        TPane = (GridPane) THboxChild.getChildren().get(2);
                    else TPane = (GridPane) THboxChild.getChildren().get(0);
                

            break;
            case Sino:
                TVboxSecond = (VBox)  TVbox.getChildren().get(3);
                 THboxChild = (HBox)  TVboxSecond.getChildren().get(0);
                    if (target.secondaryValuelink) 
                        TPane = (GridPane) THboxChild.getChildren().get(2);
                    else TPane = (GridPane) THboxChild.getChildren().get(0);
                
            break;
            case Cuando:
                TVboxSecond = (VBox)  TVbox.getChildren().get(3);
                 THboxChild = (HBox)  TVboxSecond.getChildren().get(0);
                    if (target.secondaryValuelink) 
                        TPane = (GridPane) THboxChild.getChildren().get(2);
                    else TPane = (GridPane) THboxChild.getChildren().get(0);
                
            break;
            case Mientras:
                TVboxSecond = (VBox)  TVbox.getChildren().get(3);
                 THboxChild = (HBox)  TVboxSecond.getChildren().get(0);
                    if (target.secondaryValuelink) 
                        TPane = (GridPane) THboxChild.getChildren().get(2);
                    else TPane = (GridPane) THboxChild.getChildren().get(0);
                
            break;
            case Mientrasque:
                TVboxSecond = (VBox)  TVbox.getChildren().get(3);
                 THboxChild = (HBox)  TVboxSecond.getChildren().get(0);
                    if (target.secondaryValuelink) 
                        TPane = (GridPane) THboxChild.getChildren().get(2);
                    else TPane = (GridPane) THboxChild.getChildren().get(0);
                
            break;
            case Porcada:
                TVboxSecond = (VBox)  TVbox.getChildren().get(4);
                TVboxChild = (VBox)  TVboxSecond.getChildren().get(0);
                for (Node n : TVboxChild.getChildren()) {
                    if (n instanceof GridPane) {
                        TPane = (GridPane) n;
                    }
                }
            break;
            case Cada:
                TVboxSecond = (VBox)  TVbox.getChildren().get(4);
                TVboxChild = (VBox)  TVboxSecond.getChildren().get(0);
                for (Node n : TVboxChild.getChildren()) {
                    if (n instanceof GridPane) {
                        TPane = (GridPane) n;
                    }
                }
            break;
             
            
    }
    
    if (TPane != null) {
        for (Node n : TPane.getChildren()) {
            if (n instanceof Circle) {
                CTarget = (Circle) n;
            }
        }
         
        node_link.startXProperty().bind(
        Bindings.add(source.layoutXProperty(), CSource.getLayoutX()));
        
        node_link.startYProperty().bind(
            Bindings.add(source.layoutYProperty(), CSource.getLayoutY() ));

        node_link.endXProperty().bind(
            Bindings.add(target.layoutXProperty(), CTarget.getLayoutX()));

        node_link.endYProperty().bind(
            Bindings.add(target.layoutYProperty(), (TPane.getLayoutY()+40)));

        source.registerLink (getId());
        target.registerLink (getId());
    }
}


public String GetEndNodeId(){
    return EndNodeId;
}

public String GetStartNodeId(){
    return StartNodeId;
}
}





