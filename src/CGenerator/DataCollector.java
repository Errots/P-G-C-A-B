package CGenerator;

import Display.TiposdeIconos;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class DataCollector {
    public String Id = "";
    public String NombreItem = "";
    public String TipoItem = "";
    public String ValorItem = "";
    public String Id2 = "";
    public String FirstCondition ="";
    public String ConditionType = "";
    public String SecondCondition ="";
    public String FirstValue = "";
    public String OperationType = "";
    public String SecondValue ="";
    public boolean Saved = false;

    public ArrayList<String> Textos = new ArrayList <String> ();
    public ArrayList<DataCollector> Conditionales = new ArrayList <DataCollector>();
    public boolean noValido = true;
    public double positionX = 0.00;
    public double positionY = 0.00;
    public List<String> mLinkIds = new ArrayList <String> ();
    public List<String> ChildIds = new ArrayList <String> ();
    
    public double GetPosition(){
        return positionX;
    }
}


