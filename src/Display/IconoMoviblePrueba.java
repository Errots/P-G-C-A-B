/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Display;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.shape.Circle;



/**
 *
 * @author Errot
 */
public class IconoMoviblePrueba extends AnchorPane {
    
    @FXML private AnchorPane root_pane;
    @FXML private AnchorPane left_link_handle;
    @FXML private AnchorPane right_link_handle;
    @FXML private Label title_bar;
    @FXML private Label close_button;
    
    private LinkNodo mDragLink = null;
    private AnchorPane right_pane = null;

    private EventHandler <MouseEvent> mLinkHandleDragDetected;
    private EventHandler <DragEvent> mLinkHandleDragDropped;
    private EventHandler <DragEvent> mContextLinkDragOver;  
    private EventHandler <DragEvent> mContextLinkDragDropped;		
    private EventHandler <DragEvent> mContextDragOver;
    private EventHandler <DragEvent> mContextDragDropped;                                

    private final List <String> mLinkIds = new ArrayList <String> ();

        
    private TipoItemPrueba mType = null;
    
    private Point2D mDragOffset = new Point2D(0.0, 0.0);
        
    private final IconoMoviblePrueba self;
    
    
public IconoMoviblePrueba() {
    
    FXMLLoader fxmlLoader = new FXMLLoader(
    getClass().getResource("/Display/FXMLs/IconoMovible.fxml")
    );
        
    fxmlLoader.setRoot(this); 
    fxmlLoader.setController(this);
     self = this;   
    try { 
    fxmlLoader.load();
        
    } catch (IOException exception) {
    throw new RuntimeException(exception);
    }
    //provide a universally unique identifier for this object
    setId(UUID.randomUUID().toString());  
}
    
@FXML
private void initialize() 
{
    buildNodeDragHandlers();
	buildLinkDragHandlers();
						
	left_link_handle.setOnDragDetected(mLinkHandleDragDetected);
	right_link_handle.setOnDragDetected(mLinkHandleDragDetected);

	left_link_handle.setOnDragDropped(mLinkHandleDragDropped);
	right_link_handle.setOnDragDropped(mLinkHandleDragDropped);

	mDragLink = new LinkNodo();
	mDragLink.setVisible(false);
			
	parentProperty().addListener(new ChangeListener() {

	@Override
	public void changed(ObservableValue observable,
	Object oldValue, Object newValue) 
        {
	right_pane = (AnchorPane) getParent();				
	}
				
});
}

public void registerLink(String linkId) {
    mLinkIds.add(linkId);
}

public void relocateToPoint (Point2D p) {

    //relocates the object to a point that has been converted to
    //scene coordinates
    Point2D localCoords = getParent().sceneToLocal(p);

    relocate (
        (int) (localCoords.getX() - mDragOffset.getX()),
        (int) (localCoords.getY() - mDragOffset.getY())
    );
    mDragLink = new LinkNodo();
    mDragLink.setVisible(false);
            
    parentProperty().addListener(new ChangeListener() {

    @Override
    public void changed(ObservableValue observable,
        Object oldValue, Object newValue) {
            right_pane = (AnchorPane) getParent();
        }
});
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
public void buildNodeDragHandlers() {

    mContextDragOver = new EventHandler <DragEvent>() {

	//dragover to handle node dragging in the right pane view
	@Override
	public void handle(DragEvent event) {		
			
	event.acceptTransferModes(TransferMode.ANY);				
	relocateToPoint(new Point2D( event.getSceneX(), event.getSceneY()));
	event.consume();
	}
	};
			
	//dragdrop for node dragging
	mContextDragDropped = new EventHandler <DragEvent> () {
	
	@Override
	public void handle(DragEvent event) {
				
	getParent().setOnDragOver(null);
	getParent().setOnDragDropped(null);
					
	event.setDropCompleted(true);
					
	event.consume();
	}
	};
	//close button click    
    close_button.setOnMouseClicked( new EventHandler <MouseEvent> () {

    @Override
     public void handle(MouseEvent event) {
     AnchorPane parent  = (AnchorPane) self.getParent();
     parent.getChildren().remove(self);

    
      for (ListIterator  iterId = mLinkIds.listIterator(); 
            iterId.hasNext();) {
                        
        String id = (String) iterId.next();

        for (ListIterator  iterNode = parent.getChildren().listIterator();
        iterNode.hasNext();) {
            
        Node node = (Node) iterNode.next();
                            
        if (node.getId() == null)
            continue;
                        
        if (node.getId().equals(id))
            iterNode.remove();
        }
        iterId.remove();
    }
    }
    }); 
			
	//drag detection for node dragging
	title_bar.setOnDragDetected ( new EventHandler <MouseEvent> () {

	@Override
	public void handle(MouseEvent event) {
				
	getParent().setOnDragOver(null);
	getParent().setOnDragDropped(null);

	getParent().setOnDragOver (mContextDragOver);
	getParent().setOnDragDropped (mContextDragDropped);

	//begin drag ops
        mDragOffset = new Point2D(event.getX(), event.getY());
	                
	relocateToPoint(
	new Point2D(event.getSceneX(), event.getSceneY())
	);
	                
	ClipboardContent content = new ClipboardContent();
	Contenedor container = new Contenedor();
					
	container.addData ("type", mType.toString());
	content.put(Contenedor.AddNode, container);
					
	startDragAndDrop (TransferMode.ANY).setContent(content);                
	                
	event.consume();					
	}
				
	});
          
} 
private void buildLinkDragHandlers() {
			
mLinkHandleDragDetected = new EventHandler <MouseEvent> () {

    @Override
    public void handle(MouseEvent event) {
                    
    getParent().setOnDragOver(null);
    getParent().setOnDragDropped(null);
                    
    getParent().setOnDragOver(mContextLinkDragOver);
    getParent().setOnDragDropped(mContextLinkDragDropped);
                    
    //Set up user-draggable link
    right_pane.getChildren().add(0,mDragLink);                  
                    
    mDragLink.setVisible(false);

    Point2D p = new Point2D(
    getLayoutX() + (getWidth() / 2.0),
    getLayoutY() + (getHeight() / 2.0)
    );

    mDragLink.setStart(p);                  
                    
    //Drag content code
    ClipboardContent content = new ClipboardContent();
    Contenedor container = new Contenedor ();
                    
    container.addData("source", getId());
    content.put(Contenedor.AddLink, container);
                
    startDragAndDrop (TransferMode.ANY).setContent(content);    

    event.consume();
    }
};

mLinkHandleDragDropped = new EventHandler <DragEvent> () {

	@Override
	public void handle(DragEvent event) {

	getParent().setOnDragOver(null);
	getParent().setOnDragDropped(null);
										
	//get the drag data.  If it's null, abort.  
	//This isn't the drag event we're looking for.
	Contenedor container = (Contenedor) event.getDragboard().getContent(Contenedor.AddLink);
								
	if (container == null)return;

	//hide the draggable NodeLink and remove it from the right-hand AnchorPane's children
	mDragLink.setVisible(false);
	right_pane.getChildren().remove(0);
					
	AnchorPane link_handle = (AnchorPane) event.getSource();
					
	ClipboardContent content = new ClipboardContent();
					
	//pass the UUID of the target node for later lookup
	container.addData("target", getId());
					
	content.put(Contenedor.AddLink, container);
					
	event.getDragboard().setContent(content);
	event.setDropCompleted(true);
	event.consume();				
	}
	};

	mContextLinkDragOver = new EventHandler <DragEvent> () {

	@Override
        public void handle(DragEvent event) {
        event.acceptTransferModes(TransferMode.ANY);
                    
        //Relocate user-draggable link
        if (!mDragLink.isVisible()) mDragLink.setVisible(true);
                    
        mDragLink.setEnd(new Point2D(event.getX(), event.getY()));

        event.consume();
					
	}
	};

	//drop event for link creation
	mContextLinkDragDropped = new EventHandler <DragEvent> () {

	@Override
	public void handle(DragEvent event) {
	System.out.println("context link drag dropped");
					
	getParent().setOnDragOver(null);
	getParent().setOnDragDropped(null);

	//hide the draggable NodeLink and remove it from the right-hand AnchorPane's children
	mDragLink.setVisible(false);
	right_pane.getChildren().remove(0);
					
	event.setDropCompleted(true);
	event.consume();
	}
				
	};
			
}
}
