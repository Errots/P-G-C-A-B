package Display;

import CGenerator.FolderSistem;
import CGenerator.Copilador;
import CGenerator.DataCollector;
import Configurations.ConfigurationData;
import Configurations.ProyectConfigurator;
import Executables.EjecutarArchivo;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import org.controlsfx.control.PopOver;


public class RootLayout extends AnchorPane {
    
     @FXML SplitPane base_pane;
    @FXML AnchorPane right_pane;
    @FXML AnchorPane Floatpane;
    @FXML VBox variables_pane;
    @FXML VBox conditionals_pane;
    @FXML VBox operators_pane;
    @FXML VBox bucles_pane;
    @FXML MenuItem OpenProyect_Handle;
    @FXML MenuItem GenerateCode_Handle;
    @FXML TextArea Output_Handle;
    @FXML ImageView Generator_Handle;
    @FXML ImageView NewProyect_Handle;
    @FXML ScrollPane Dropzone_handle;
    @FXML ImageView Execute_Handle;
    @FXML MenuItem SaveProyect_Handle;
    @FXML CheckMenuItem ChangeLanguage_Handle;

    public EventHandler mExtraIconDragDropped=null;
    public EventHandler mExtraIconDragOverRightPane=null;
    public EventHandler mExtraIconDragOverRoot=null;
        
    public Iconos mDragOverIcon = null;
    public EventHandler mEnterKeyboardEvent=null;
    public EventHandler mIconDragOverRoot=null;
    public EventHandler mIconDragDropped=null;
    public EventHandler mIconDragOverRightPane=null;
    private EventHandler mRightPaneClickEvent=null;
    private EventHandler mMenuItemActionEvent=null;
    private EventHandler mOpenProyectActionEvent=null;
    private EventHandler mGeneradorMouseEvent=null;
    private EventHandler mNewProyectMouseEvent=null;
    private EventHandler mSaveProyectActionEvent=null;
    private EventHandler mExecuteMouseEvent=null;
    private EventHandler mChangeLanguageActionEvent=null;
    
    Timer timer = new Timer();
    
    private  ArrayList<String> NodesIds = new ArrayList<String>();
    
    private String ProyectPath = null;
    
    private String MainFile = null;
    
    private ArrayList<DataCollector> ColeccionDatos = new ArrayList<>();
    
    public ResourceBundle resourceBundle = ResourceBundle.getBundle("Resource.Language", new Locale("es","ES"));
      
    public ClipboardContent SecondClip = new ClipboardContent();
    
    public ConditionItemsControl CIC;
    
    private InputStreamConsumer Exe;
    
    public RootLayout(){
        FXMLLoader fxmlLoader = new FXMLLoader(
		getClass().getResource("/Display/FXMLs/MainLayout.fxml"),resourceBundle
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
    buildKeyboardEventHandle();
    GenerateCode_Handle.setOnAction(mMenuItemActionEvent);
    OpenProyect_Handle.setOnAction(mOpenProyectActionEvent);
    Generator_Handle.setOnMouseClicked(mGeneradorMouseEvent);
    NewProyect_Handle.setOnMouseClicked(mNewProyectMouseEvent);
    right_pane.setOnMouseClicked(mRightPaneClickEvent);
    

    SaveProyect_Handle.setOnAction(mSaveProyectActionEvent);
    Execute_Handle.setOnMouseClicked(mExecuteMouseEvent);
    ChangeLanguage_Handle.setOnAction(mChangeLanguageActionEvent);
    CIC = new ConditionItemsControl();
    
    
    
    
//    right_pane.getChildren().add(CIC);
    // Llenar toda la barra izquierda con iconos para pruebas
     for (int i = 0; i<TiposdeIconos.values().length;i++)
    {
        Iconos icn = new Iconos();
        
        addDragDetection(icn);
        
        icn.setType(TiposdeIconos.values()[i],resourceBundle);
        if (i<6) {
            variables_pane.getChildren().add(icn);
        }else if (i<11) {
            operators_pane.getChildren().add(icn);
        }else if (i<14) {
            conditionals_pane.getChildren().add(icn);
        }else if (i<18) {
            bucles_pane.getChildren().add(icn);
        }
        
        
    }
     
     timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                right_pane.getChildren().forEach((n) -> {
                    for (int j = 0; j < NodesIds.size(); j++) {
                        if(NodesIds.get(j).equals(n.getId()))
                        {
                            IconDrag Ic = (IconDrag) n;
                            Ic.Check();
                        }
//                        else NodesIds.remove(NodesIds.get(j));
                    }
                }); 
            }
       }, 3000, 3000);
    
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
            CIC.popup.getRoot().setOnDragOver(mExtraIconDragOverRightPane);
            CIC.Extraright_pane.setOnDragOver(mExtraIconDragOverRoot);
            CIC.popup.getRoot().setOnDragDropped(mExtraIconDragDropped);
        
            // get a reference to the clicked DragIcon object
            Iconos icn = (Iconos) event.getSource();

            //begin drag ops
            mDragOverIcon.setType(icn.getType(),resourceBundle);
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
			CIC.popup.getRoot().removeEventHandler(DragEvent.DRAG_OVER,mExtraIconDragOverRoot);
			CIC.Extraright_pane.removeEventHandler(DragEvent.DRAG_OVER, mExtraIconDragOverRightPane);
			CIC.popup.getRoot().removeEventHandler(DragEvent.DRAG_DROPPED, mExtraIconDragDropped);	
                       right_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRightPane);
			right_pane.removeEventHandler(DragEvent.DRAG_DROPPED, mIconDragDropped);
                        base_pane.removeEventHandler(DragEvent.DRAG_OVER, mIconDragOverRoot);
								
			mDragOverIcon.setVisible(false);
				
			Contenedor container =(Contenedor) event.getDragboard().getContent(Contenedor.AddNode);
                        System.out.println(event.getAcceptingObject());

    // Base normal de operacion de objeto            

		if(SecondClip.isEmpty()){if (container != null) {
                    System.out.println(container.getData().toString());
            if (container.getValue("scene_coords") != null) {

                        IconDrag node = new IconDrag(resourceBundle,CIC);
                        node.setType(TiposdeIconos.valueOf(container.getValue("type")),resourceBundle);
                        right_pane.getChildren().add(node);
                        NodesIds.add(node.getId());

                        Point2D cursorPoint = container.getValue("scene_coords");

                        node.relocateToPoint(
                            new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32)
                        );
                    
		}
            }
    container = (Contenedor) event.getDragboard().getContent(Contenedor.AddLink);
    if (container != null) {
        System.out.println(container.getData().toString());
                    
    String sourceId = container.getValue("source");
    String targetId = container.getValue("target");

    if (sourceId != null && targetId != null) {
                        
        LinkNodo link = new LinkNodo();
                        
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
            data.positionX = 0;
            data.positionY = 0;
            source.AddChild(targetId);
            target.setDataCollector(data);
            
            link.bindEnds(source, target);
        }
    }
    }}else
    {
        System.out.println(event.getAcceptingObject());
        // Secundario de operacion local de objecto
    Contenedor Secondcontainer =(Contenedor) SecondClip.get(Contenedor.AddNode);

    if (Secondcontainer.getValue("scene_coords") != null) {
        ConditionItemsControl NCIC = new ConditionItemsControl();        
        NCIC.popup.getRoot().setOnDragOver(mExtraIconDragOverRightPane);
        NCIC.Extraright_pane.setOnDragOver(mExtraIconDragOverRoot);
        NCIC.popup.getRoot().setOnDragDropped(mExtraIconDragDropped);
        IconDrag node = new IconDrag(resourceBundle,NCIC);
        node.setType(TiposdeIconos.valueOf(Secondcontainer.getValue("type")),resourceBundle);
        CIC.Extraright_pane.getChildren().add(node);

        Point2D cursorPoint = Secondcontainer.getValue("scene_coords");

        node.relocateToPoint(
            new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32)
        );

    }
    SecondClip.clear();}
            event.consume();
        }  
                
    });
    
    CIC.popup.getScene().setOnDragDone(new EventHandler <DragEvent> (){

        @Override
        public void handle(DragEvent event) {
            
        
        Contenedor container = (Contenedor) event.getDragboard().getContent(Contenedor.AddLink);
        
        if (container != null) {

        String sourceId = container.getValue("source");
        String targetId = container.getValue("target");

        if (sourceId != null && targetId != null) {

            LinkNodo link = new LinkNodo();

            CIC.Extraright_pane.getChildren().add(0,link);

            IconDrag source = null;
            IconDrag target = null;

            for (Node n: CIC.Extraright_pane.getChildren()) {

                if (n.getId() == null)
                    continue;

                if (n.getId().equals(sourceId))
                source = (IconDrag) n;

            if (n.getId().equals(targetId))
                target = (IconDrag) n;

            }

            if (source != null && target != null){
                DataCollector data = source.getDataCollector();
            data.positionX = 0;
            data.positionY = 0;
            source.AddChild(targetId);
            target.setDataCollector(data);
            link.bindEnds(source, target);
            }
        }}
            event.consume();
        }
    });
    
    mExtraIconDragOverRoot = new EventHandler <DragEvent>() {

        @Override
        public void handle(DragEvent event) {
 
            Point2D p = CIC.Extraright_pane.screenToLocal(event.getScreenX(), event.getScreenY());

            if (!CIC.Extraright_pane.boundsInLocalProperty().get().contains(p)) {
                mDragOverIcon.relocateToPoint(new Point2D(event.getScreenX(), event.getScreenY()));
                return;
            }

            event.consume();
        }
    };
    
    mExtraIconDragOverRightPane = new EventHandler <DragEvent> () {

        @Override
        public void handle(DragEvent event) {

            event.acceptTransferModes(TransferMode.ANY);
 
            mDragOverIcon.relocateToPoint(
                    new Point2D(event.getScreenX(), event.getScreenY())
            );

            event.consume();
        }
    };
 
    mExtraIconDragDropped = new EventHandler <DragEvent> () {

    @Override
    public void handle(DragEvent event) {

        Contenedor container = (Contenedor) event.getDragboard().getContent(Contenedor.AddNode);

        container.addData("scene_coords",new Point2D(event.getSceneX(), event.getSceneY()));

        ClipboardContent content = new ClipboardContent();
        content.put(Contenedor.AddNode, container);

        System.out.println(container.getData().toString());
        
        SecondClip.put(Contenedor.AddNode, container);
        event.getDragboard().setContent(content);
        event.setDropCompleted(true);
    }
};
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
        
        mRightPaneClickEvent = new EventHandler <MouseEvent>()
        {
            @Override
            public void handle(MouseEvent event)
            {
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
                boolean Correcto = true;
                for(Node n: right_pane.getChildren())
                {
                    for (int j = 0; j < NodesIds.size(); j++) {
                        if(NodesIds.get(j).equals(n.getId()))
                        {
                            IconDrag Icn =  (IconDrag)n;
                            DataCollector drag = Icn.getDataCollector();
                            drag.Conditionales.clear();

                            if (Icn.getType() == TiposdeIconos.Si ||Icn.getType() == TiposdeIconos.Sino || Icn.getType() == TiposdeIconos.Mientras || Icn.getType() == TiposdeIconos.Cuando || Icn.getType() == TiposdeIconos.Mientrasque || Icn.getType() == TiposdeIconos.Cada || Icn.getType() == TiposdeIconos.Porcada ) {
                                if (Icn.getConditionalNodes() != null) {
                                    for(Node q: Icn.getConditionalNodes())
                                    {
                                        
                                        IconDrag Icn2 =  (IconDrag)q;
                                        DataCollector drag2 = Icn2.getDataCollector();
                                        if(drag2.noValido == false){Correcto = false;}
                                        drag.Conditionales.add(drag2);
                                    }
                                }
                            }
                            if(drag.noValido == false){Correcto = false;}
                            ColeccionDatos.add(Icn.getDataCollector());
                        }   
                    } 
                    
                }
                

                ColeccionDatos.sort(Comparator.comparing((dataCollector) -> dataCollector.GetPosition()));
                if(!ColeccionDatos.isEmpty() && Correcto == true) try {
                    
                    copiler.RecuperarDatos(ColeccionDatos, ProyectPath);
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
                    copiler.RecuperarDatos(ColeccionDatos, ProyectPath);
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
                    IconDrag node = new IconDrag(resourceBundle,CIC);

                    node.setType(TiposdeIconos.valueOf(ColeccionDato.TipoItem),resourceBundle);
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
                            drag.Conditionales.clear();
                            if (Icn.getType() == TiposdeIconos.Si ||Icn.getType() == TiposdeIconos.Sino || Icn.getType() == TiposdeIconos.Mientras || Icn.getType() == TiposdeIconos.Cuando || Icn.getType() == TiposdeIconos.Mientrasque || Icn.getType() == TiposdeIconos.Cada || Icn.getType() == TiposdeIconos.Porcada ) {
                                if (Icn.getConditionalNodes() != null) {
                                    for(Node q: Icn.getConditionalNodes())
                                    {
                                        IconDrag Icn2 =  (IconDrag)q;
                                        DataCollector drag2 = Icn2.getDataCollector();
                                        if(drag2.noValido == false){Correcto = false;}
                                        drag.Conditionales.add(drag2);
                                    }
                                }
                            }
                            if(drag.noValido == false){Correcto = false;}
                            ColeccionDatos.add(Icn.getDataCollector());
                        }   
                    }   
                }
                ColeccionDatos.sort(Comparator.comparing((dataCollector) -> dataCollector.GetPosition()));
                if(!ColeccionDatos.isEmpty() && Correcto == true) try {
                    String FilePath = copiler.EjecutarDatos(ColeccionDatos,ProyectPath);
                    if (!FilePath.isEmpty()) {
                        WriteTextOutput("Copilacion correcta");
                        WriteTextOutput("Executando archivo");
                        MainFile = FilePath;
                        Execute(MainFile,ProyectPath);
                    }
                 } catch (Exception ex) {
                    Logger.getLogger(RootLayout.class.getName()).log(Level.SEVERE, null, ex);
                    WriteTextOutput(ex.getMessage());
                }
                event.consume();
            }
        };
        
        mChangeLanguageActionEvent = new EventHandler <ActionEvent>()
        {
            @Override
            public void handle(ActionEvent event)
            {
                if (ChangeLanguage_Handle.isSelected()) {
                    ChangeLanguage_Handle.setSelected(!ChangeLanguage_Handle.isSelected());
                    loadView(new Locale("en", "EN"));
                }else{ChangeLanguage_Handle.setSelected(!ChangeLanguage_Handle.isSelected());loadView(new Locale("es", "ES"));} 
                event.consume();
            }
        };
    }
    
    private void buildKeyboardEventHandle(){
    mEnterKeyboardEvent = new EventHandler <KeyEvent>()
        {
            @Override
            public void handle(KeyEvent event) {
                if (event.getCode() == KeyCode.ENTER)  {
                    String[] command = Output_Handle.getText().split("\\n");
                    Exe.Input(command[command.length-1]);

                    event.consume();
                }
            }
        };
    }
    
    private void printLines(String name, InputStream ins) throws Exception {
    String line;
    
    BufferedReader in = new BufferedReader(
        new InputStreamReader(ins));
    while ((line = in.readLine()) != null) {
        if (line == null) { break; }
        System.out.println(name + " " + line);
        WriteTextOutput(line);
    }
 
  }
    public int run(String clazz, String path) throws IOException, InterruptedException {        
        ProcessBuilder pb = new ProcessBuilder("java","-cp",path,clazz);
        
        Exe = new InputStreamConsumer(pb,false);
        Exe.start();

        int result = 1;

//        consumer.join();

        

        return result;
    }

    public int compile(String file,String path) throws IOException, InterruptedException {        
        ProcessBuilder pb = new ProcessBuilder("javac","-d",path,file);
        InputStreamConsumer consumer = new InputStreamConsumer(pb,true);
        consumer.start();
        int result = 1;
        consumer.join();

        System.out.println(consumer.getOutput());
        return result;        
    }
    
    public class InputStreamConsumer extends Thread {

        private final InputStream is;
        private IOException exp;
        private StringBuilder output;
        private final ProcessBuilder PB;
        private final boolean IsCopiler;

        public InputStreamConsumer(ProcessBuilder pb,boolean Copiler) throws IOException {
            IsCopiler = Copiler;
            PB = pb;
            PB.redirectError();
            Process p = PB.start();
            
            this.is = p.getInputStream();
        }

        @Override
        public  void run() {
            
            int in = -1;
            output = new StringBuilder(64);
            try {
                while ((in = is.read()) != -1) {
                    output.append((char) in);
                }
                if ((in = is.read()) == -1 && !IsCopiler) {
                    WriteTextOutput(output.toString());
                    Output_Handle.setOnKeyPressed(mEnterKeyboardEvent);
                }
            } catch (IOException ex) {
                exp = ex;
            }
        }

        public void Input(String command) {
            PB.command(command);
            Output_Handle.removeEventHandler(KeyEvent.KEY_PRESSED, mEnterKeyboardEvent);
            
        }
        
        public StringBuilder getOutput() {
            return output;
        }

        public IOException getException() {
            return exp;
        }
    }
    
  
  
  public void Execute(String FilePath, String ProyectPath)
  {
      String FileName = FilePath.substring(FilePath.lastIndexOf("\\")+1);
    try {
      int result = compile(FilePath,ProyectPath+"\\Build");
      System.out.println("javac returned " + result);
      run(FileName.replaceAll(".java", ""),ProyectPath+"\\Build");
    } catch (IOException | InterruptedException e) {
      WriteTextOutput(e.getMessage());
    }
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
    
    public EventHandler GetEvents()
    {
        return mIconDragOverRightPane;
    }
    
    private void loadView(Locale locale) {
        resourceBundle = ResourceBundle.getBundle("Resource.Language", locale);
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
		getClass().getResource("/Display/FXMLs/MainLayout.fxml"),resourceBundle
		);
            fxmlLoader.setRoot(this);
            fxmlLoader.setController(this);
            
            // replace the content
            this.getChildren().clear();
            
            fxmlLoader.load();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        ChangeLanguage_Handle.setSelected(!ChangeLanguage_Handle.isSelected());
        buildDragHandlers();

    }
}

