package CGenerator;

import java.io.IOException;
import java.util.ArrayList;
import static java.util.Collections.sort;

public class Copilador
{    
    ArrayList<String> imports = new ArrayList<>();
    ArrayList<String> comandos = new ArrayList<>();
   
    
    public boolean RecuperarDatos(ArrayList<DataCollector> data, String Path ) 
    {
        try{
        for (DataCollector Data: data)
        {
            GenerarComando(Data);
        }
        PreCodeDisplay code = new PreCodeDisplay();
        code.Path = Path;
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
    
    public String ConditionalGen (ArrayList<DataCollector> data){
        ArrayList<String> comands = GenerarComandoConds(data);
        
        String result =""; 
        for(String text:comands)
        {
            result += text+"\n";
        }
        return result;
    }
    
    public void GenerarComando(DataCollector data)
    {
        String comando = "";
        String incluir = "";
        String objeto = "";
        String result = "";
        
        
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
                switch(data.OperationType){
                    case "Entero":
                        comando ="int "+data.NombreItem+" = leer.nextInt();";
                        break;
                        case "Flotante":
                        comando ="float "+data.NombreItem+" = leer.nextFloat();";
                        break;
                        case "Doble":
                        comando ="double "+data.NombreItem+" = leer.nextDouble();";
                        break;
                        case "Texto":
                        comando ="String "+data.NombreItem+" = leer.nextLine();";
                        break;
                }
                
                comandos.add(0, objeto);
                comandos.add(comando);
                if(!imports.contains(incluir)) imports.add(incluir);

            break;
            
            case "Mostrar":
                comando ="System.out.println(\""+data.ValorItem+"\");";
                comandos.add(comandos.size(),comando);    
            break;
            
//             case "Mas":
//                 comando = data.FirstValue+data.OperationType+data.SecondValue;
//                 comandos.add(comando);
//            break;
//            case "Menos":
//                comando = data.FirstValue+data.OperationType+data.SecondValue;
//                 comandos.add(comando);
//            break;
//            case "Entre":
//                comando = data.FirstValue+data.OperationType+data.SecondValue;
//                 comandos.add(comando);
//            break;
//            case "Por":
//                comando = data.FirstValue+data.OperationType+data.SecondValue;
//                 comandos.add(comando);
//            break;
//            case "Diferencia":
//                comando = data.FirstValue+data.OperationType+data.SecondValue;
//                 comandos.add(comando);
//            break;
            case "Si":    
                result = ConditionalGen(data.Conditionales);
                comando = "if ("+data.FirstCondition+data.ConditionType+data.SecondCondition+"){\n"+result+"\n}";
                comandos.add(comando);
            break;
            case "Sino":
                result = ConditionalGen(data.Conditionales);
                if (data.FirstCondition.isEmpty() || data.SecondCondition.isEmpty()) 
                  comando = "else if ("+data.FirstCondition+data.ConditionType+data.SecondCondition+"){\n"+result+"\n}";  
                else comando = "else {\n"+result+"\n}";
                
                comandos.add(comando);
            break;
            case "Cuando":
                result = ConditionalGen(data.Conditionales);
                comando = "if ("+data.FirstCondition+data.ConditionType+data.SecondCondition+"){\n"+result+"\n}";
                comandos.add(comando);
            break;
            case "Mientras":
                result = ConditionalGen(data.Conditionales);
                comando = "while ("+data.FirstCondition+data.ConditionType+data.SecondCondition+"){\n"+result+"\n}";
                comandos.add(comando);
            break;
            case "Mientrasque":
                result = ConditionalGen(data.Conditionales);
                comando = "do {\n"+result+"\n} "+"while ("+data.FirstCondition+data.ConditionType+data.SecondCondition+");";
                comandos.add(comando);
            break;
            case "Porcada":
                result = ConditionalGen(data.Conditionales);
                comando = "for(int i =0;i<"+data.FirstValue+";i++){\n"+result+"\n}";
                comandos.add(comando);
            break;
            case "Cada":
                result = ConditionalGen(data.Conditionales);
                comando = "for(int i =0;i<"+data.FirstValue+";i++){\n"+result+"\n}";
                comandos.add(comando);
            break;
        }
    }
    
    public ArrayList<String> GenerarComandoConds(ArrayList<DataCollector> Data)
    {
        ArrayList<String> comands = new ArrayList<String>();

        String comando = "";
        String incluir = "";
        String objeto = "";
        String result = "";
        
        for(DataCollector data: Data){
        switch(data.TipoItem)
        {
            case "Entero":
                comando ="int "+data.NombreItem+" = "+data.ValorItem+";";
                comands.add(comando);
            break;
            
            case "Flotante":
                comando ="float "+data.NombreItem+" = "+data.ValorItem+";";
                comands.add(comando);
            break;
            
            case "Doble":
                comando ="double "+data.NombreItem+" = "+data.ValorItem+";";
                comands.add(comando);    
            break;
            
            case "Texto":
                comando ="String "+data.NombreItem+" =  \""+data.ValorItem+"\";";
                comands.add(comando);   
            break;
            
            case "Leer":
                incluir = "import java.util.Scanner;";
                objeto = "Scanner leer = new Scanner(System.in);";
                comando ="String "+data.NombreItem+" = leer.nextLine();";
                comands.add(0, objeto);
                comands.add(comando);
                if(!imports.contains(incluir)) imports.add(incluir);

            break;
            
            case "Mostrar":
                comando ="System.out.println(\""+data.ValorItem+"\");";
                comands.add(comands.size(),comando);    
            break;
            
             case "Mas":
                 comando = data.FirstValue+data.OperationType+data.SecondValue;
                 comands.add(comando);
            break;
            case "Menos":
                comando = data.FirstValue+data.OperationType+data.SecondValue;
                 comands.add(comando);
            break;
            case "Entre":
                comando = data.FirstValue+data.OperationType+data.SecondValue;
                 comands.add(comando);
            break;
            case "Por":
                comando = data.FirstValue+data.OperationType+data.SecondValue;
                 comands.add(comando);
            break;
            case "Diferencia":
                comando = data.FirstValue+data.OperationType+data.SecondValue;
                 comands.add(comando);
            break;
            case "Si":    
                result = ConditionalGen(data.Conditionales);
                comando = "if ("+data.FirstCondition+data.ConditionType+data.SecondCondition+"){\n"+result+"\n}";
                comands.add(comando);
            break;
            case "Sino":
                result = ConditionalGen(data.Conditionales);
                if (data.FirstCondition.isEmpty() || data.SecondCondition.isEmpty()) 
                  comando = "else if ("+data.FirstCondition+data.ConditionType+data.SecondCondition+"){\n"+result+"\n}";  
                else comando = "else {\n"+result+"\n}";
                
                comands.add(comando);
            break;
            case "Cuando":
                result = ConditionalGen(data.Conditionales);
                comando = "if ("+data.FirstCondition+data.ConditionType+data.SecondCondition+"){\n"+result+"\n}";
                comands.add(comando);
            break;
            case "Mientras":
                result = ConditionalGen(data.Conditionales);
                comando = "while ("+data.FirstCondition+data.ConditionType+data.SecondCondition+"){\n"+result+"\n}";
                comands.add(comando);
            break;
            case "Mientrasque":
                result = ConditionalGen(data.Conditionales);
                comando = "do {\n"+result+"\n} "+"while ("+data.FirstCondition+data.ConditionType+data.SecondCondition+");";
                comands.add(comando);
            break;
            case "Porcada":
                result = ConditionalGen(data.Conditionales);
                comando = "for(int pgcab =0,pgcab<"+data.FirstValue+",pgcab++){\n"+result+"\n}";
                comands.add(comando);
            break;
            case "Cada":
                result = ConditionalGen(data.Conditionales);
                comando = "for(int pgcab =0,pgcab<"+data.FirstValue+",pgcab++){\n"+result+"\n}";
                comands.add(comando);
            break;
        }
        }
        return comands;
    }
    
    
}