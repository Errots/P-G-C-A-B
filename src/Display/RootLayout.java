package Display;

import CGenerator.Copilador;
import CGenerator.DataCollector;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


public class RootLayout extends AnchorPane {
    
    @FXML SplitPane base_pane;
    @FXML AnchorPane right_pane;
    @FXML VBox left_pane;
    @FXML MenuItem GenerateCode_Handle;
    @FXML TextArea Output_Handle;
    @FXML ImageView Generator_Handle;

    private Iconos mDragOverIcon = null;
    private EventHandler mIconDragOverRoot=null;
    private EventHandler mIconDragDropped=null;
    private EventHandler mIconDragOverRightPane=null;
    private EventHandler mMenuItemActionEvent=null;
    private EventHandler mGeneradorMouseEvent=null;
    
    private ArrayList<String> NodesIds = new ArrayList<String>();
    
    private ArrayList<DataCollector> ColeccionDatos = new ArrayList<>();
    
      
    public RootLayout(){
        FXMLLoader fxmlLoader = new FXMLLoader(
		getClass().getResource("/Display/FXMLs/MainLayout.fxml")
		);
		
		fxmlLoader.setRoot(this); 
		fxmlLoader.setController(this);
		
		try { 
			fxmlLoader.load();
        
		} catch (IOException exception) {
		    throw new RuntimeException(exception);
		}
                buildDragHandlers();
    }
    
    @FXML
    private void initialize(){
    // Agrega un icono que sera usado para el proceso drag and drop
    // el icono es agreado como hijo de root AnchorPane
    // para que pueda se visible en los dos lados
    mDragOverIcon = new Iconos();
    
    mDragOverIcon.setVisible(false);
    mDragOverIcon.setOpacity(0.65);
    getChildren().add(mDragOverIcon);
    
    buildMouseEventHandle();
    GenerateCode_Handle.setOnAction(mMenuItemActionEvent);
    Generator_Handle.setOnMouseClicked(mGeneradorMouseEvent);
    

    
    // Llenar toda la barra izquierda con iconos para pruebas
    for (int i = 0; i<TiposdeIconos.values().length;i++)
    {
        Iconos icn = new Iconos();
        
        addDragDetection(icn);
        
        icn.setType(TiposdeIconos.values()[i]);
        left_pane.getChildren().add(icn);
    }
    
    }

    
    private void addDragDetection(Iconos dragIcon) {
		
       dragIcon.setOnDragDetected (new EventHandler <MouseEvent> () {

	@Override
	public void handle(MouseEvent event) {

            // set the other drag event handles on their respective objects
            base_pane.setOnDragOver(mIconDragOverRoot);
            right_pane.setOnDragOver(mIconDragOverRightPane);
            right_pane.setOnDragDropped(mIconDragDropped);
        
            // get a reference to the clicked DragIcon object
            Iconos icn = (Iconos) event.getSource();

             //begin drag ops
            mDragOverIcon.setType(icn.getType());
            mDragOverIcon.relocateToPoint(new Point2D (event.getSceneX(), event.getSceneY()));

            ClipboardContent content = new ClipboardContent();
            Contenedor container = new Contenedor();

            container.addData ("type", mDragOverIcon.getType().toString());
            content.put(Contenedor.AddNode, container);

            mDragOverIcon.startDragAndDrop (TransferMode.ANY).setContent(content);
            mDragOverIcon.setVisible(true);
            mDragOverIcon.setMouseTransparent(true);
            event.consume();					
            }
        });
    }	
    
    private void buildDragHandlers() {
        
    //drag over transition to move widget form left pane to right pane
    mIconDragOverRoot = new EventHandler <DragEvent>() {

        @Override
        public void handle(DragEvent event) {
 
            Point2D p = right_pane.sceneToLocal(event.getSceneX(), event.getSceneY());

            if (!right_pane.boundsInLocalProperty().get().contains(p)) {
                mDragOverIcon.relocateToPoint(new Point2D(event.getSceneX(), event.getSceneY()));
                return;
            }

            event.consume();
        }
    };
 
    mIconDragOverRightPane = new EventHandler <DragEvent> () {

        @Override
        public void handle(DragEvent event) {

            event.acceptTransferModes(TransferMode.ANY);
 
            mDragOverIcon.relocateToPoint(
                    new Point2D(event.getSceneX(), event.getSceneY())
            );

            event.consume();
        }
    };
 
    mIconDragDropped = new EventHandler <DragEvent> () {

    @Override
    public void handle(DragEvent event) {

        Contenedor container = (Contenedor) event.getDragboard().getContent(Contenedor.AddNode);

        container.addData("scene_coords",new Point2D(event.getSceneX(), event.getSceneY()));

        ClipboardContent content = new ClipboardContent();
        content.put(Contenedor.AddNode, container);

        event.getDragboard().setContent(content);
        event.setDropCompleted(true);
    }
};

    this.setOnDragDone (new EventHandler <DragEvent> (){
			
		@Override
		public void handle (DragEvent event) {
				
			right_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
			right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
			base_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);
								
			mDragOverIcon.setVisible(false);
				
			Contenedor container =(Contenedor) event.getDragboard().getContent(Contenedor.AddNode);
				
		if (container != null) {
            if (container.getValue("scene_coords") != null) {

            IconDrag node = new IconDrag();

                node.setType(TiposdeIconos.valueOf(container.getValue("type")));
                right_pane.getChildren().add(node);
                NodesIds.add(node.getId());

                Point2D cursorPoint = container.getValue("scene_coords");

                node.relocateToPoint(
                    new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32)
                );
		}
            }
                //AddLink drag operation
    container = (Contenedor) event.getDragboard().getContent(Contenedor.AddLink);
                
    if (container != null) {
                    
    //bind the ends of our link to the nodes whose id's are stored in the drag container
    String sourceId = container.getValue("source");
    String targetId = container.getValue("target");

    if (sourceId != null && targetId != null) {
                        
        //System.out.println(container.getData());
        LinkNodo link = new LinkNodo();
                        
        //add our link at the top of the rendering order so it's rendered first
        right_pane.getChildren().add(0,link);
                        
        IconDrag source = null;
        IconDrag target = null;
                        
        for (Node n: right_pane.getChildren()) {
                            
            if (n.getId() == null)
                continue;
                            
        if (n.getId().equals(sourceId))
            source = (IconDrag) n;
                        
        if (n.getId().equals(targetId))
            target = (IconDrag) n;
                            
        }
                        
        if (source != null && target != null){
            DataCollector data = source.getDataCollector();
            target.setDataCollector(data);
            target.ValueDisable(true);
            link.bindEnds(source, target);
        }
    }
    }
            event.consume();
        }   
    });
    }
    
    private void buildMouseEventHandle()
    {
        mGeneradorMouseEvent = new EventHandler <MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                Copilador copiler = new Copilador();
                int i =0;
                boolean Correcto = true;
                for(Node n: right_pane.getChildren())
                {
                    if(NodesIds.get(i).equals(n.getId()))
                    {
                        IconDrag Icn =  (IconDrag)n;
                        DataCollector drag = Icn.getDataCollector();
                        if(drag.noValido == false){Correcto = false;}
                        ColeccionDatos.add(Icn.getDataCollector());
                        i++;  
                    }
                    
                }
                if(!ColeccionDatos.isEmpty() && Correcto == true) try {
                    copiler.RecuperarDatos(ColeccionDatos);
                } catch (IOException ex) {
                    Logger.getLogger(RootLayout.class.getName()).log(Level.SEVERE, null, ex);
                }
                event.consume();
            }
        };
        mMenuItemActionEvent = new EventHandler <ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                Copilador copiler = new Copilador();
                int i =0;
                boolean Correcto = true;
                for(Node n: right_pane.getChildren())
                {
                    if(NodesIds.get(i).equals(n.getId()))
                    {
                        IconDrag Icn =  (IconDrag)n;
                        DataCollector drag = Icn.getDataCollector();
                        if(drag.noValido == false){Correcto = false;}
                        ColeccionDatos.add(Icn.getDataCollector());
                        i++;  
                    }
                    
                }
                if(!ColeccionDatos.isEmpty() && Correcto == true) try {
                    copiler.RecuperarDatos(ColeccionDatos);
                } catch (IOException ex) {
                    Logger.getLogger(RootLayout.class.getName()).log(Level.SEVERE, null, ex);
                }
                event.consume();
            }
        };
    }

    public void WriteTextOutput(String texto)
    {
        Output_Handle.appendText(texto+"\n");
    }
}
