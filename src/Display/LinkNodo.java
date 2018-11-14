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
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.shape.CubicCurve;

public class LinkNodo extends AnchorPane {

@FXML CubicCurve node_link;

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
    Circle SourceCircle = null;
    Circle TargetCircle = null;
    
    Node nodeOut = source.getChildren().get(0);
    if(nodeOut instanceof VBox){
        for(Node nodeIn:((VBox)nodeOut).getChildren()){
            if(nodeIn instanceof GridPane){
                for(Node node:((GridPane)nodeIn).getChildren()){
                    if(node instanceof Circle){
                        SourceCircle = (Circle) node;
                    }
                }
            }
        }
    } 
    Node nodeOut2 = target.getChildren().get(0);
    if(nodeOut2 instanceof VBox){
        for(Node nodeIn:((VBox)nodeOut2).getChildren()){
            if(nodeIn instanceof HBox){
                for(Node node:((HBox)nodeIn).getChildren()){
                    if(node instanceof Circle){
                        TargetCircle = (Circle) node;
                    }
                }
            }
        }
    } 
    
    Point2D LocalCords = getParent().sceneToLocal(SourceCircle.getLayoutX(), SourceCircle.getLayoutX());
    
    System.out.println(source.layoutYProperty());
    System.out.println(source.layoutXProperty());
    System.out.println(target.layoutYProperty());
    System.out.println(target.layoutXProperty());
    System.out.println(SourceCircle.layoutYProperty());
    System.out.println(SourceCircle.layoutXProperty());
    System.out.println(TargetCircle.layoutYProperty());
    System.out.println(TargetCircle.layoutXProperty());
    System.out.println(SourceCircle.getBoundsInParent());
    System.out.println(TargetCircle.layoutXProperty().subtract(target.layoutYProperty()));
    System.out.println(SourceCircle.layoutYProperty().subtract(source.layoutYProperty()));
    System.out.println(SourceCircle.centerYProperty());
    System.out.println(SourceCircle.centerXProperty().get());
    System.out.println(TargetCircle.centerYProperty().get());
    System.out.println(TargetCircle.centerXProperty());
    
    node_link.startXProperty().bind(
        Bindings.add(SourceCircle.layoutXProperty(), (SourceCircle.getRadius()/ 2.0)));
        
    node_link.startYProperty().bind(
        Bindings.add(SourceCircle.layoutYProperty(), (SourceCircle.getRadius() / 2.0)));
        
    node_link.endXProperty().bind(
        Bindings.add(TargetCircle.layoutXProperty(), (TargetCircle.getRadius() / 2.0)));
        
    node_link.endYProperty().bind(
        Bindings.add(TargetCircle.layoutXProperty(), (TargetCircle.getRadius() / 2.0)));
    
    source.registerLink (getId());
    target.registerLink (getId());
}
	
}



