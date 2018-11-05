/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Display;

import java.io.IOException;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Point2D;
import javafx.scene.control.SplitPane;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.DataFormat;
import javafx.scene.input.DragEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;


/**
 *
 * @author Errot
 */
public class RootLayout extends AnchorPane {
    
    @FXML SplitPane base_pane;
    @FXML AnchorPane right_pane;
    @FXML VBox left_pane;

    private ItemPrueba mDragOverIcon = null;
    private EventHandler mIconDragOverRoot=null;
    private EventHandler mIconDragDropped=null;
    private EventHandler mIconDragOverRightPane=null;
    
      
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
    mDragOverIcon = new ItemPrueba();
    
    mDragOverIcon.setVisible(false);
    mDragOverIcon.setOpacity(0.65);
    getChildren().add(mDragOverIcon);
    
    // Llenar toda la barra izquierda con iconos para pruebas
    for (int i = 0; i<7;i++)
    {
        ItemPrueba icn = new ItemPrueba();
        
        addDragDetection(icn);
        
        icn.setType(TipoItemPrueba.values()[i]);
        left_pane.getChildren().add(icn);
    }
    
    }

    private void addDragDetection(ItemPrueba dragIcon) {
		
       dragIcon.setOnDragDetected (new EventHandler <MouseEvent> () {

	@Override
	public void handle(MouseEvent event) {

            // set the other drag event handles on their respective objects
            base_pane.setOnDragOver(mIconDragOverRoot);
            right_pane.setOnDragOver(mIconDragOverRightPane);
            right_pane.setOnDragDropped(mIconDragDropped);
        
            // get a reference to the clicked DragIcon object
            ItemPrueba icn = (ItemPrueba) event.getSource();

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

        Contenedor container =
            (Contenedor) event.getDragboard().getContent(Contenedor.AddNode);

        container.addData("scene_coords",
            new Point2D(event.getSceneX(), event.getSceneY()));

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
				
			Contenedor container = 
			(Contenedor) event.getDragboard().getContent(Contenedor.AddNode);
				
		if (container != null) {
            if (container.getValue("scene_coords") != null) {

            IconoMoviblePrueba node = new IconoMoviblePrueba();

                node.setType(TipoItemPrueba.valueOf(container.getValue("type")));
                right_pane.getChildren().add(node);

                Point2D cursorPoint = container.getValue("scene_coords");

                node.relocateToPoint(
                    new Point2D(cursorPoint.getX() - 32, cursorPoint.getY() - 32)
                );
		}
            }
            event.consume();
            }
	});
    }

}
