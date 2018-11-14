/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CodigosdePrueba;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseDragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.stage.Stage;

/**
 *
 * @author Errot
 */
public class PruebaNodes extends Application {
    
    public static Circle createCircle(double x, double y) {
    return new Circle(x, y, 20, Color.BLACK.deriveColor(0, 1, 1, 0.5));
}    

@Override
public void start(Stage primaryStage) {

    Node[] circles = new Node[]{
        createCircle(40, 40),
        createCircle(240, 40),
        createCircle(40, 240),
        createCircle(240, 240)
    };
    Pane root = new Pane(circles);

    class DragStartHandler implements EventHandler<MouseEvent> {

        public Line line;

        @Override
        public void handle(MouseEvent event) {
            if (line == null) {
                Node sourceNode = (Node) event.getSource();
                line = new Line();
                Bounds bounds = sourceNode.getBoundsInParent();

                // start line at center of node
                line.setStartX((bounds.getMinX() + bounds.getMaxX()) / 2);
                line.setStartY((bounds.getMinY() + bounds.getMaxY()) / 2);
                line.setEndX(line.getStartX());
                line.setEndY(line.getStartY());
                sourceNode.startFullDrag();
                root.getChildren().add(0, line);
            }
        }
    }

    DragStartHandler startHandler = new DragStartHandler();
    EventHandler<MouseDragEvent> dragReleaseHandler = evt -> {
        if (evt.getGestureSource() == evt.getSource()) {
            // remove line, if it starts and ends in the same node
            root.getChildren().remove(startHandler.line);
        }
        evt.consume();
        startHandler.line = null;
    };
    EventHandler<MouseEvent> dragEnteredHandler = evt -> {
        if (startHandler.line != null) {
            // snap line end to node center
            Node node = (Node) evt.getSource();
            Bounds bounds = node.getBoundsInParent();
            startHandler.line.setEndX((bounds.getMinX() + bounds.getMaxX()) / 2);
            startHandler.line.setEndY((bounds.getMinY() + bounds.getMaxY()) / 2);
        }
    };

    for (Node n : circles) {
        // register handlers
        n.setOnDragDetected(startHandler);
        n.setOnMouseDragReleased(dragReleaseHandler);
        n.setOnMouseDragEntered(dragEnteredHandler);

        // add info allowing to identify this node as drag source/target
        n.setUserData(Boolean.TRUE);
    }

    root.setOnMouseReleased(evt -> {
        // mouse released outside of a target -> remove line
        root.getChildren().remove(startHandler.line);
        startHandler.line = null;
    });
    root.setOnMouseDragged(evt -> {
        if (startHandler.line != null) {
            Node pickResult = evt.getPickResult().getIntersectedNode();
            if (pickResult == null || pickResult.getUserData() != Boolean.TRUE) {
                // mouse outside of target -> set line end to mouse position
                startHandler.line.setEndX(evt.getX());
                startHandler.line.setEndY(evt.getY());
            }
        }
    });

    Scene scene = new Scene(root, 280, 280);

    primaryStage.setScene(scene);
    primaryStage.show();
}
    public static void main(String[] args){
        launch(args);
    }
}
