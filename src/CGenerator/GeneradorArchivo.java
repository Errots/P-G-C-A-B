/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package CGenerator;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author cpu18
 */
public class GeneradorArchivo {

    public void Fechahora(String fecha){
       

    }
    
public void CrearArchivoError(String Error,String Ubicacion,String hora, String fecha, String Espacio)
    {
    File f=new File("C:/Error_log.txt");
     Date date = new Date();
    //Caso 1: obtener la hora y salida por pantalla con formato:
DateFormat hourFormat = new SimpleDateFormat("HH:mm:ss");
hora = ("Hora: "+hourFormat.format(date));
//Caso 2: obtener la fecha y salida por pantalla con formato:
DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
fecha =("Fecha: "+dateFormat.format(date));
//Caso 3: obtenerhora y fecha y salida por pantalla con formato:
DateFormat hourdateFormat = new SimpleDateFormat("HH:mm:ss dd/MM/yyyy");
Espacio = "========";
// = hourdateFormat.format(date);
try{
 FileOutputStream fis=new FileOutputStream(f);
        try (DataOutputStream dos = new DataOutputStream(fis)) {
            dos.writeChars(Error);
            dos.writeChars(Ubicacion);
            dos.writeChars(Espacio);
            dos.writeChars(hora);
            dos.writeChars(Espacio);
            dos.writeChars(fecha);
        }
}
catch(FileNotFoundException e){
 System.out.println("No se encontro el archivo");
}
catch(IOException e){
 System.out.println("Error al escribir");
}
    }

public void CrearArchivo(String ubicacion,String texto) {
GeneradorArchivo java = new GeneradorArchivo();
        
File f=new File("D:/prueba.out");
try{
 FileOutputStream fis=new FileOutputStream(f);
 DataOutputStream dos=new DataOutputStream(fis);
 dos.close();
}
catch(FileNotFoundException e){
 System.out.println("No se encontro el archivo");
 java.CrearArchivoError(e.getMessage(), "FileNotFoundException", e.getMessage(), e.getMessage(), e.getMessage() );
 
}
catch(IOException e){
 System.out.println("Error al escribir");
} 
    }
    
}
