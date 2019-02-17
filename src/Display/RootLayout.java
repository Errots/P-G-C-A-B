package Display;

import CGenerator.FolderSistem;
import CGenerator.Copilador;
import CGenerator.DataCollector;
import Configurations.ConfigurationData;
import Configurations.ProyectConfigurator;
import Executables.EjecutarArchivo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
import javafx.scene.control.ScrollPane;
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
    @FXML MenuItem OpenProyect_Handle;
    @FXML MenuItem GenerateCode_Handle;
    @FXML TextArea Output_Handle;
    @FXML ImageView Generator_Handle;
    @FXML ImageView NewProyect_Handle;
    @FXML ScrollPane Dropzone_handle;
    @FXML ImageView Execute_Handle;
    @FXML MenuItem SaveProyect_Handle;

    private Iconos mDragOverIcon = null;
    private EventHandler mIconDragOverRoot=null;
    private EventHandler mIconDragDropped=null;
    private EventHandler mIconDragOverRightPane=null;
    private EventHandler mMenuItemActionEvent=null;
    private EventHandler mOpenProyectActionEvent=null;
    private EventHandler mGeneradorMouseEvent=null;
    private EventHandler mNewProyectMouseEvent=null;
    private EventHandler mSaveProyectActionEvent=null;
    private EventHandler mExecuteMouseEvent=null;
    
    private  ArrayList<String> NodesIds = new ArrayList<String>();
    
    private String ProyectPath = null;
    
    private String MainFile = null;
    
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
    OpenProyect_Handle.setOnAction(mOpenProyectActionEvent);
    Generator_Handle.setOnMouseClicked(mGeneradorMouseEvent);
    NewProyect_Handle.setOnMouseClicked(mNewProyectMouseEvent);
    SaveProyect_Handle.setOnAction(mSaveProyectActionEvent);
    Execute_Handle.setOnMouseClicked(mExecuteMouseEvent);
    

    
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

            if (!Dropzone_handle.disableProperty().getValue()) {
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
            }
            event.consume();					
            }
        });
    }	
    
    private void buildDragHandlers() {
       
        
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
            data.NombreItem = "";
            source.AddChild(targetId);
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
        mNewProyectMouseEvent = new EventHandler <MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                FolderSistem directori = new FolderSistem();
                directori.FolderSistem();
                        
                event.consume();
            }
        };
        
        mGeneradorMouseEvent = new EventHandler <MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                ColeccionDatos.clear();
                Copilador copiler = new Copilador();
                ArrayList<Double> positions = new ArrayList<Double>();
                boolean Correcto = true;
                for(Node n: right_pane.getChildren())
                {
                    for (int j = 0; j < NodesIds.size(); j++) {
                        if(NodesIds.get(j).equals(n.getId()))
                        {
                            IconDrag Icn =  (IconDrag)n;
                            DataCollector drag = Icn.getDataCollector();
                            if(drag.noValido == false){Correcto = false;}
                            ColeccionDatos.add(Icn.getDataCollector());
                        }   
                    } 
                    
                }
                Collections.sort(positions);
                if(!ColeccionDatos.isEmpty() && Correcto == true) try {
                    for (int j = 0; j<ColeccionDatos.size(); j++) {
                        for (int E = 0; E<positions.size(); E++) {
                            if (ColeccionDatos.get(j).positionX == positions.get(E)) {
                                DataCollector Dato = ColeccionDatos.get(E);
                                ColeccionDatos.set(E, ColeccionDatos.get(j));
                                ColeccionDatos.set(j, Dato);
                            }
                        }
                    }
                    copiler.RecuperarDatos(ColeccionDatos);
                    WriteTextOutput("Copilacion correcta");
                } catch (Exception ex) {
                    Logger.getLogger(RootLayout.class.getName()).log(Level.SEVERE, null, ex);
                    WriteTextOutput(ex.getMessage());
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
                ColeccionDatos.clear();
                boolean Correcto = true;
                for(Node n: right_pane.getChildren())
                {
                    for (int j = 0; j < NodesIds.size(); j++) {
                        if(NodesIds.get(j).equals(n.getId()))
                        {
                            IconDrag Icn =  (IconDrag)n;
                            DataCollector drag = Icn.getDataCollector();
                            if(drag.noValido == false){Correcto = false;}
                            ColeccionDatos.add(Icn.getDataCollector());
                        }   
                    } 
                    
                }
                if(!ColeccionDatos.isEmpty() && Correcto == true) try {
                    copiler.RecuperarDatos(ColeccionDatos);
                    WriteTextOutput("Copilacion correcta");
                } catch (Exception ex) {
                    Logger.getLogger(RootLayout.class.getName()).log(Level.SEVERE, null, ex);
                    WriteTextOutput(ex.getMessage());
                }
                event.consume();
            }
        };
        
        
        mOpenProyectActionEvent = new EventHandler <ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                ProyectConfigurator configure = new ProyectConfigurator();
                ConfigurationData collector =  configure.OpenProyect();
                MainFile = collector.MainFile;
                ProyectPath = collector.ProyectPath;
                ColeccionDatos = collector.ColeccionDatos;
                NodesIds = collector.NodesIds;
                Dropzone_handle.setDisable(false);
                
                for (DataCollector ColeccionDato : collector.ColeccionDatos) {
                    IconDrag node = new IconDrag();

                    node.setType(TiposdeIconos.valueOf(ColeccionDato.TipoItem));
                    right_pane.getChildren().add(node);
                    NodesIds.add(node.getId());

                    node.setDataCollector(ColeccionDato);
                }
                
                event.consume();
            }
        };
        
        mSaveProyectActionEvent = new EventHandler <ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                ConfigurationData data = new ConfigurationData();
                data.ColeccionDatos = ColeccionDatos;
                data.MainFile = MainFile;
                data.NodesIds = NodesIds;
                data.ProyectPath = ProyectPath;
                ProyectConfigurator config = new ProyectConfigurator();
                config.SaveProyect(data);
                
                event.consume();
            }
        };
        
        mExecuteMouseEvent = new EventHandler <MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
                Copilador copiler = new Copilador();
                ColeccionDatos.clear();
                boolean Correcto = true;
                for(Node n: right_pane.getChildren())
                {
                    for (int j = 0; j < NodesIds.size(); j++) {
                        if(NodesIds.get(j).equals(n.getId()))
                        {
                            IconDrag Icn =  (IconDrag)n;
                            DataCollector drag = Icn.getDataCollector();
                            if(drag.noValido == false){Correcto = false;}
                            ColeccionDatos.add(Icn.getDataCollector());
                        }   
                    }   
                }
                if(!ColeccionDatos.isEmpty() && Correcto == true) try {
                    boolean next = copiler.EjecutarDatos(ColeccionDatos,ProyectPath);
                    if (next) {
                        WriteTextOutput("Copilacion correcta");
                        WriteTextOutput("Executando archivo");
                    }
                 } catch (Exception ex) {
                    Logger.getLogger(RootLayout.class.getName()).log(Level.SEVERE, null, ex);
                    WriteTextOutput(ex.getMessage());
                }
                event.consume();
            }
        };
    }

    public void RemoveNode(String id)
    {
        NodesIds.remove(id);
    }
    
    public void SetNodeEnable(String id)
    {
        int i =0;
        for(Node n: right_pane.getChildren())
        {
            if(NodesIds.get(i).equals(id))
            {
                IconDrag Icn =  (IconDrag)n;
                Icn.setDisable(true);
                i++;  
            }

        }
    }
    
    
    
    public String GetPath()
    {
        return ProyectPath;
    }
    
    public void SavePath(String path)
    {
        if (!path.isEmpty()) {
            Dropzone_handle.setDisable(false);
            ProyectPath = path;
        } 
    }
    
    public void SaveMainFile(String path)
    {
        MainFile = path;
    }
    
    public void WriteTextOutput(String texto)
    {
        Output_Handle.appendText(texto+"\n");
    }
}

