/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Display;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javafx.scene.input.DataFormat;
import javafx.util.Pair;
import javafx.util.Pair;

/**
 *
 * @author Errot
 */
public class Contenedor implements Serializable {
    
    public static final DataFormat DragNode = 
    new DataFormat("Display.IconDrag.drag");
    
    public static final DataFormat AddLink =
    new DataFormat("Display.NodoLink.add");
  private static final long serialVersionUID = -1890998765646621338L;

	public static final DataFormat AddNode = 
			new DataFormat("Display.Iconos.add");
	
	private final List <Pair<String, Object> > mDataPairs = new ArrayList <Pair<String, Object> > ();
	
	public void addData (String key, Object value) {
		mDataPairs.add(new Pair<String, Object>(key, value));		
	}
	
	public <T> T getValue (String key) {
		
		for (Pair<String, Object> data: mDataPairs) {
			
			if (data.getKey().equals(key))
				return (T) data.getValue();
				
		}
		
		return null;
	}
	
	public List <Pair<String, Object> > getData () { return mDataPairs; }	
}
