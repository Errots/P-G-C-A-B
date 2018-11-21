package Display;


import CGenerator.*;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DragEvent;

import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

import javafx.scene.shape.Circle;

public class IconDrag extends AnchorPane{
    
    @FXML private AnchorPane root_pane;
    @FXML private Circle startLink_Handle;
    @FXML private Circle endLink_Handle;
    @FXML private Label title_bar;
    @FXML private Label close_button;
    @FXML private TextField varValue_Handle;
    @FXML private TextField varName_Handle;
    @FXML private ImageView addBtn_Handle;
    @FXML private GridPane Inputs_Handle;
    
    private LinkNodo mDragLink = null;
    private AnchorPane right_pane = null;

    private EventHandler <MouseEvent> mLinkHandleDragDetected;
    private EventHandler <MouseEvent> mVarNameEventHandleMouseExit;
    private EventHandler <MouseEvent> mVarValueEventHandleMouseExit;
    private EventHandler <MouseEvent> mAddBtnEventHandleMouseClicked;
    private EventHandler <DragEvent> mLinkHandleDragDropped;
    private EventHandler <DragEvent> mContextLinkDragOver;  
    private EventHandler <DragEvent> mContextLinkDragDropped;		
    private EventHandler <DragEvent> mContextDragOver;
    private EventHandler <DragEvent> mContextDragDropped;                                

    private final List <String> mLinkIds = new ArrayList <String> ();
    
    private DataCollector ColeccionDatos = new DataCollector();
    
    private TiposdeIconos mType = null;
    
    private Point2D mDragOffset = new Point2D(0.0, 0.0);
        
    private final IconDrag self;
    
    public IconDrag() {
    
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
{   buildNodeEventHandlers();
    buildNodeDragHandlers();
    buildLinkDragHandlers();
    varValue_Handle.setOnMouseExited(mVarValueEventHandleMouseExit);
    varName_Handle.setOnMouseExited(mVarNameEventHandleMouseExit);
    addBtn_Handle.setOnMouseClicked(mAddBtnEventHandleMouseClicked);
    endLink_Handle.setOnDragDetected(mLinkHandleDragDetected);
    startLink_Handle.setOnDragDetected(mLinkHandleDragDetected);
    endLink_Handle.setOnDragDropped(mLinkHandleDragDropped);
    startLink_Handle.setOnDragDropped(mLinkHandleDragDropped);
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

    
    Circle SourceCircle = null;
    Circle TargetCircle = null;
    //Recoloca el objeto a un punto que fue convertido a cordenada de la scena
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

public TiposdeIconos getType(){return mType;}

public DataCollector getDataCollector(){return ColeccionDatos;}
    
public void setType(TiposdeIconos type){
    mType = type;
    getStyleClass().clear();
    getStyleClass().add("dragicon");
    switch(mType){
        
        case Entero:
        ColeccionDatos.TipoItem = "Entero";
        ((VBox) addBtn_Handle.getParent()).getChildren().remove(addBtn_Handle);
        title_bar.setText("Entero");
        getStyleClass().add("icon-blue");
        break;
            
        case Flotante:
        ColeccionDatos.TipoItem = "Flotante";
        ((VBox) addBtn_Handle.getParent()).getChildren().remove(addBtn_Handle);
        title_bar.setText("Flotante");
        getStyleClass().add("icon-red");
        break;
            
        case Doble:
        ColeccionDatos.TipoItem = "Doble";        
        ((VBox) addBtn_Handle.getParent()).getChildren().remove(addBtn_Handle);
        title_bar.setText("Doble");
        getStyleClass().add("icon-grey");
        break;
            
        case Texto:
        ColeccionDatos.TipoItem = "Texto";
        addBtn_Handle.setVisible(true);
        title_bar.setText("Texto");
        getStyleClass().add("icon-yellow");
        break;
        
        case Leer:
        ColeccionDatos.TipoItem = "Leer";
        ((GridPane) endLink_Handle.getParent()).getChildren().remove(endLink_Handle);
        ((GridPane) varValue_Handle.getParent()).getChildren().remove(varValue_Handle);
        title_bar.setText("Leer");
        getStyleClass().add("icon-green");
        break;
        
        case Mostrar:
        ColeccionDatos.TipoItem = "Mostrar";
        ((VBox) varName_Handle.getParent()).getChildren().remove(varName_Handle);
        title_bar.setText("Mostrar");
        getStyleClass().add("icon-purple");
        break;
    }
}
public void buildNodeEventHandlers()
{
    mVarValueEventHandleMouseExit = new EventHandler <MouseEvent>()
    {
        @Override
        public void handle(MouseEvent event)
        {
            String valor = varValue_Handle.getText();
            if (!ColeccionDatos.ValorItem.equals(varValue_Handle.getText())){           
            ColeccionDatos.ValorItem = varValue_Handle.getText();
            }
            event.consume();
        }
    };
    
    mVarNameEventHandleMouseExit = new EventHandler <MouseEvent>()
    {
        @Override
        public void handle(MouseEvent event)
        {
            if (!ColeccionDatos.NombreItem.equals(varName_Handle.getText())){           
            ColeccionDatos.NombreItem = varName_Handle.getText();
            }
            event.consume();
        }
    };
    
//    mAddBtnEventHandleMouseClicked = new EventHandler <MouseEvent>()
//    {
//        @Override
//        public void handle(MouseEvent event)
//        {
//            
//            ObservableList<RowConstraints> Rows =Inputs_Handle.getRowConstraints();
//            int NRows = Rows.size();
//            Circle crl = new Circle(10);
//        }
//    };
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
					
	Circle link_handle = (Circle) event.getSource();
					
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
