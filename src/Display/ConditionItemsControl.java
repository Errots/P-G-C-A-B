package Display;

import CGenerator.DataCollector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.ResourceBundle;
import javafx.application.Application;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PopupControl;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Popup;
import javafx.stage.Stage;
import org.controlsfx.control.PopOver;

public class ConditionItemsControl extends AnchorPane {
        @FXML public AnchorPane Extraright_pane;
        @FXML public ScrollPane ExtraDropzone_handle;
         
        public EventHandler mExtraIconDragDropped=null;
        
        public PopOver popup;
        
        public String ActualId;
        
    public ConditionItemsControl() {
        FXMLLoader fxmlLoader = new FXMLLoader(
    getClass().getResource("/Display/FXMLs/ConditionItemsView.fxml"));
        
    fxmlLoader.setController(this); 
    try {
        popup = new PopOver(fxmlLoader.load());
    } catch (IOException exception) {
    throw new RuntimeException(exception);
    }
    }
}
