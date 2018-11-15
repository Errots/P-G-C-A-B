package crear;

/**
 *
 * @author jlo_7
 */
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import javax.swing.JOptionPane;
import java.util.Scanner;
public class Formato {
    
    
   public void crearArchivo(String nombre, String texto, int dato){     
       
   File archivo = null;
   
   try {
       
   archivo = new File("\\Documentos\\NetBeansProjects\\"+nombre+".txt");
   
   if (!archivo.exists()){
           archivo.createNewFile();
       System.out.println("Se ha creado el archivo");
   
   }  
   }catch(Throwable e){
       System.err.println("No se ha podido crear el archivo");
   }
       try { 
        PrintWriter escribir = new PrintWriter(archivo, "utf-8");
        escribir.println("//"+nombre);
        escribir.println(texto);
        escribir.println(dato);
        escribir.close();
       } catch (Exception e) {
          e.getMessage();
       }
   }
       
    
    }
    
    
       
 
   
       
         
       

 
    
    

