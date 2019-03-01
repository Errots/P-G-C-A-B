package CGenerator;

import java.io.IOException;
import java.util.ArrayList;
import static java.util.Collections.sort;

public class Copilador
{    
    ArrayList<String> imports = new ArrayList<>();
    ArrayList<String> comandos = new ArrayList<>();
   
    
    public boolean RecuperarDatos(ArrayList<DataCollector> data) 
    {
        try{
        for (DataCollector Data: data)
        {
            GenerarComando(Data);
        }
        PreCodeDisplay code = new PreCodeDisplay();
        code.WriteText(comandos, imports);
        return true;
        }catch(Exception e){System.out.println(e.getMessage());return false;}

    }
    
    public String EjecutarDatos(ArrayList<DataCollector> data,String path) 
    {
        try{
        for (DataCollector Data: data)
        {
            GenerarComando(Data);
        }
        GeneradorArchivo Gene = new GeneradorArchivo();
        String filepath = Gene.ExecutarArchivo(comandos, imports, path);
        return filepath;
        }catch(Exception e){System.out.println(e.getMessage());return null;}

    }
    
    public void GenerarComando(DataCollector data)
    {
        String comando = "";
        String incluir = "";
        String objeto = "";
        
        switch(data.TipoItem)
        {
            case "Entero":
                comando ="int "+data.NombreItem+" = "+data.ValorItem+";";
                comandos.add(comando);
            break;
            
            case "Flotante":
                comando ="float "+data.NombreItem+" = "+data.ValorItem+";";
                comandos.add(comando);
            break;
            
            case "Doble":
                comando ="double "+data.NombreItem+" = "+data.ValorItem+";";
                comandos.add(comando);    
            break;
            
            case "Texto":
                comando ="String "+data.NombreItem+" =  \""+data.ValorItem+"\";";
                comandos.add(comando);   
            break;
            
            case "Leer":
                incluir = "import java.util.Scanner;";
                objeto = "Scanner leer = new Scanner(System.in);";
                comando ="String "+data.NombreItem+" = leer.nextLine();";
                comandos.add(0, objeto);
                comandos.add(comando);
                if(!imports.contains(incluir)) imports.add(incluir);

            break;
            
            case "Mostrar":
                comando ="System.out.println(\""+data.ValorItem+"\");";
                comandos.add(comandos.size(),comando);    
            break;
        }
    }
    
    
}