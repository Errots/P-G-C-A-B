package Display;

import Display.RootLayout;
import CGenerator.*;
import Configurations.ConfigurationData;
import java.io.IOException;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.UUID;
import java.util.Timer;
import java.util.TimerTask;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
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
    @FXML private GridPane TopGrid_Handle;
    
    private LinkNodo mDragLink = null;
    private AnchorPane right_pane = null;

    private EventHandler <MouseEvent> mLeftLinkHandleDragDetected;
    private EventHandler <MouseEvent> mRightLinkHandleDragDetected;
    private EventHandler <MouseEvent> mVarNameEventHandleMouseExit;
    private EventHandler <MouseEvent> mVarValueEventHandleMouseExit;
    private EventHandler <MouseEvent> mAddBtnEventHandleMouseClicked;
    private EventHandler <DragEvent> mLeftLinkHandleDragDropped;
    private EventHandler <DragEvent> mRightLinkHandleDragDropped;
    private EventHandler <DragEvent> mContextLinkDragOver;  
    private EventHandler <DragEvent> mContextLinkDragDropped;		
    private EventHandler <DragEvent> mContextDragOver;
    private EventHandler <DragEvent> mContextDragDropped;   
    Timer timer = new Timer();

    private final List<String> mLinkIds = new ArrayList <String> ();
    private final List<String> ChildIds = new ArrayList <String> ();
    
    private DataCollector ColeccionDatos = new DataCollector();
    
    private TiposdeIconos mType = null;
    
    private Point2D mDragOffset = new Point2D(0.0, 0.0);
        
    private RootLayout Root;
    
    private final IconDrag self;
    boolean NameX = false;
    boolean ValX = false;
    public IconDrag(ResourceBundle resourceBundle) {
    
    FXMLLoader fxmlLoader = new FXMLLoader(
    getClass().getResource("/Display/FXMLs/IconoMovible.fxml"),resourceBundle
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
    addBtn_Handle.setOnMouseClicked(mAddBtnEventHandleMouseClicked);
    endLink_Handle.setOnDragDetected(mLeftLinkHandleDragDetected);
    startLink_Handle.setOnDragDetected(mRightLinkHandleDragDetected);
    endLink_Handle.setOnDragDropped(mLeftLinkHandleDragDropped);
    startLink_Handle.setOnDragDropped(mRightLinkHandleDragDropped);
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
    
    
timer.scheduleAtFixedRate(new TimerTask() {
  @Override
  public void run() {
    String nombre = varName_Handle.getText();
            if(!nombre.matches("[a-z,A-Z].*")|| nombre.isEmpty())
                {
                    
                    switch(mType)
                    {
                    case Mostrar:
                        NameX = true;
                    break;
                        
                    default:ColeccionDatos.noValido = false;
                    Tooltip nota = new Tooltip();
                    nota.setText("Solo se aceptan letras");
                    root_pane.setStyle("-fx-background-color:FF0000;");         
                    NameX = false;
                        break;
                    }
                    
                }
                else 
                {
                    NameX = true;
                }
            if (!ColeccionDatos.NombreItem.equals(nombre)){           
            ColeccionDatos.NombreItem = varName_Handle.getText();
            }
  }
}, 3000, 3000);    
    
timer.scheduleAtFixedRate(new TimerTask() {
  @Override
  public void run() {
     String valor = varValue_Handle.getText();
            switch(mType)
            {
            case Entero:
                if(!valor.matches("[0-9]*[^.]")|| valor.isEmpty())
                {
                    Tooltip nota = new Tooltip();
                    nota.setText("Solo se aceptan numeros enteros");
                    root_pane.setStyle("-fx-background-color:red;");
                    ColeccionDatos.noValido = false;
                    ValX = false;
                }
                else {ValX = true;}
            break;
            
            case Flotante:
                if(!valor.matches("[0-9]+\\.[0-9]+")|| valor.isEmpty())
                {
                    Tooltip nota = new Tooltip();
                    nota.setText("Solo se aceptan numeros decimales");
                    root_pane.setStyle("-fx-background-color:red;");
                    ColeccionDatos.noValido = false;
                    ValX = false;
                }
                else {   ValX = true;}
            break;
            
            case Doble:
                if(!valor.matches("[0-9]+\\.[0-9]+")|| valor.isEmpty())
                {
                    Tooltip nota = new Tooltip();
                    nota.setText("Solo se aceptan numeros decimales");
                    root_pane.setStyle("-fx-background-color:red;");
                    ColeccionDatos.noValido = false;
                    ValX = false;
                }
                else {ValX = true;}
            break;
            
            case Texto:
                if(!valor.matches("[a-zA-Z]*[-]?[0-9]*\\.?")|| valor.isEmpty())
                {
                    Tooltip nota = new Tooltip();
                    nota.setText("Solo se aceptan letras");
                    root_pane.setStyle("-fx-background-color:red;");
                    ColeccionDatos.noValido = false;
                    ValX = false;
                }
                else {ValX = true;}
            break;
        
            case Mostrar:
                if(!valor.matches("[0-9]*[a-zA-Z]*[.]*")|| valor.isEmpty())
                {
                    Tooltip nota = new Tooltip();
                    nota.setText("Solo se aceptan letras");
                    root_pane.setStyle("-fx-background-color:red;");
                    ColeccionDatos.noValido = false;
                    ValX = false;
                }
                else {ValX = true;}
            break;
            }
            if (!ColeccionDatos.ValorItem.equals(varValue_Handle.getText())){           
            ColeccionDatos.ValorItem = varValue_Handle.getText();
            }if (ValX && NameX) {
        switch(mType){
        
                    case Entero:
                    root_pane.setStyle("-fx-background-color: lightgrey;"); 
                    break;
            
                    case Flotante:
                    root_pane.setStyle("-fx-background-color: lightgrey;"); 
                    break;
            
                    case Doble:
                    root_pane.setStyle("-fx-background-color: lightgrey;"); 
                    break;
            
                    case Texto:
                    root_pane.setStyle("-fx-background-color: lightgrey;"); 
                    break;
        
                    case Leer:
                    root_pane.setStyle("-fx-background-color: lightgrey;"); 
                    break;
        
                    case Mostrar:
                    root_pane.setStyle("-fx-background-color: lightgrey;"); 
                    break;
                }
                    ColeccionDatos.noValido = true;
                    ColeccionDatos.positionX = getLayoutX();
                    ColeccionDatos.positionY = getLayoutY();
    }
    }
}, 3000, 3000);

    
}

public void registerLink(String linkId) {
    mLinkIds.add(linkId);
}

public void AddChild(String Id) {
    ChildIds.add(Id);
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

public void ValueDisable(boolean disable){varValue_Handle.setDisable(disable); }
public void setDataCollector(DataCollector data)
{
    if (!data.NombreItem.isEmpty()&&data.positionX !=0.00 && data.positionY != 0.00) {
        setLayoutX(data.positionX);
        setLayoutY(data.positionY);
        ColeccionDatos.NombreItem = data.NombreItem;
        varName_Handle.setText(data.NombreItem);
        ColeccionDatos.Id = data.Id;
        ColeccionDatos.Id2 = data.Id2;
        ColeccionDatos.Textos = data.Textos;
        ChildIds.addAll(data.ChildIds);
        mLinkIds.addAll(data.mLinkIds);
        
    }
    ColeccionDatos.noValido = data.noValido;
    ColeccionDatos.ValorItem = data.ValorItem;
    varValue_Handle.setText(data.ValorItem);
    
}
public DataCollector getDataCollector(){return ColeccionDatos;}
    
public void setType(TiposdeIconos type,ResourceBundle resourceBundle){
    mType = type;
    getStyleClass().clear();
    getStyleClass().add("dragicon");
    switch(mType){
        
        case Entero:
        ColeccionDatos.TipoItem = "Entero";
        ((VBox) addBtn_Handle.getParent()).getChildren().remove(addBtn_Handle);
        title_bar.setText(resourceBundle.getString("Entero"));
        TopGrid_Handle.getStyleClass().add("icon-entero");
        break;
            
        case Flotante:
        ColeccionDatos.TipoItem = "Flotante";
        ((VBox) addBtn_Handle.getParent()).getChildren().remove(addBtn_Handle);
        title_bar.setText(resourceBundle.getString("Flotante"));
        TopGrid_Handle.getStyleClass().add("icon-flotante");
        break;
            
        case Doble:
        ColeccionDatos.TipoItem = "Doble";        
        ((VBox) addBtn_Handle.getParent()).getChildren().remove(addBtn_Handle);
        title_bar.setText(resourceBundle.getString("Doble"));
        TopGrid_Handle.getStyleClass().add("icon-doble");
        break;
            
        case Texto:
        ColeccionDatos.TipoItem = "Texto";
        addBtn_Handle.setVisible(true);
        title_bar.setText(resourceBundle.getString("Texto"));
        TopGrid_Handle.getStyleClass().add("icon-texto");
        break;
        
        case Leer:
        ColeccionDatos.TipoItem = "Leer";
        ((GridPane) endLink_Handle.getParent()).getChildren().remove(endLink_Handle);
        ((GridPane) varValue_Handle.getParent()).getChildren().remove(varValue_Handle);
        title_bar.setText(resourceBundle.getString("Leer"));
        TopGrid_Handle.getStyleClass().add("icon-leer");
        break;
        
        case Mostrar:
        ColeccionDatos.TipoItem = "Mostrar";
        ((VBox) varName_Handle.getParent()).getChildren().remove(varName_Handle);
        title_bar.setText(resourceBundle.getString("Mostrar"));
        TopGrid_Handle.getStyleClass().add("icon-mostrar");
        break;
    }
}
public void buildNodeEventHandlers()
{
 
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
     
    
    ChildIds.stream().forEach((id) -> {
        Root.SetNodeEnable(id);
    });
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
        ValueDisable(false);
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
			
mRightLinkHandleDragDetected = new EventHandler <MouseEvent> () {

    @Override
    public void handle(MouseEvent event) {
                    
    getParent().setOnDragOver(null);
    getParent().setOnDragDropped(null);
                    
    getParent().setOnDragOver(mContextLinkDragOver);
    getParent().setOnDragDropped(mContextLinkDragDropped);
                    
    //Set up user-draggable link
    right_pane.getChildren().add(0,mDragLink);                  
                    
    mDragLink.setVisible(false);

    Circle CSource = null;
    VBox SBox= (VBox) getChildren().get(0);
    GridPane Pane = (GridPane) SBox.getChildren().get(0);
    CSource = (Circle) Pane.getChildren().get(0);
    
    Point2D p = new Point2D(
    getLayoutX() + CSource.getLayoutX(),
    getLayoutY() + CSource.getLayoutY()
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
mLeftLinkHandleDragDetected = new EventHandler <MouseEvent> () {

    @Override
    public void handle(MouseEvent event) {
                    
    getParent().setOnDragOver(null);
    getParent().setOnDragDropped(null);
                    
    getParent().setOnDragOver(mContextLinkDragOver);
    getParent().setOnDragDropped(mContextLinkDragDropped);
                    
    //Set up user-draggable link
    right_pane.getChildren().add(0,mDragLink);                  
                    
    mDragLink.setVisible(false);

    Circle CSource = null;
    VBox SBox= (VBox) getChildren().get(0);
    VBox TVbox = (VBox) SBox.getChildren().get(1);
    GridPane TPane= null;
    for (Node n : TVbox.getChildren()) {
        if (n instanceof GridPane) {
            TPane = (GridPane) n;
        }
    }
    if (TPane != null) {
        CSource = (Circle) TPane.getChildren().get(0);
    }
    
    Point2D p = new Point2D(
    getLayoutX() + CSource.getLayoutX(),
    getLayoutY() + (TPane.getLayoutY()+40)
    );

    mDragLink.setStart(p);                  
                    
    //Drag content code
    ClipboardContent content = new ClipboardContent();
    Contenedor container = new Contenedor ();
                    
    container.addData("target", getId());
    content.put(Contenedor.AddLink, container);
                
    startDragAndDrop (TransferMode.ANY).setContent(content);    

    event.consume();
    }
};

mRightLinkHandleDragDropped = new EventHandler <DragEvent> () {

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
	container.addData("source", getId());
					
	content.put(Contenedor.AddLink, container);
					
	event.getDragboard().setContent(content);
	event.setDropCompleted(true);
	event.consume();				
	}
	};

mLeftLinkHandleDragDropped = new EventHandler <DragEvent> () {

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
