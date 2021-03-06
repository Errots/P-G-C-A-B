package CGenerator;

import Display.RootLayout;
import Executables.EjecutarArchivo;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import javafx.stage.FileChooser;


public class GeneradorArchivo extends RootLayout{
    
    
public void CrearArchivoError(String Error,String Ubicacion)
{
    String hora,fecha,Espacio;
    File f=new File("/P-G-C-A-B/Error_log.txt");
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

public void CrearArchivo(ArrayList<String> texto,ArrayList<String> imports, String Path) {
FileChooser chooser = new FileChooser();
chooser.setTitle("Selecciones donde guardar");
    if (Path == null) {
        chooser.setInitialDirectory(new java.io.File("."));
    }else chooser.setInitialDirectory(new java.io.File(Path));
File selectedFile = null;
selectedFile = chooser.showSaveDialog(null);
if (selectedFile != null){
String nombre = selectedFile.getName();
String nombreClass = nombre.substring(0, nombre.indexOf("."));
nombreClass.trim();
GeneradorArchivo java = new GeneradorArchivo();
        
try{
    try (FileOutputStream fis = new FileOutputStream(selectedFile);BufferedWriter  dos = new BufferedWriter(new OutputStreamWriter(fis))) {
        for(String Item : imports)
        {
            dos.write(Item);
            dos.newLine();
        }
        String Final = "}}";        
        texto.add(0, "public static void main(String[] args){");
        texto.add(0, "public class "+nombreClass+"{");
        for(String Item : texto)
        {
            dos.write(Item);
            dos.newLine();
        }
        dos.write(Final);
        dos.close();
//        synchronized (RootLayout.this) { RootLayout.this.notify(); }
        super.SaveMainFile(selectedFile.getAbsolutePath());
        super.WriteTextOutput("Codigo guardado con exito");
    }
}
catch(FileNotFoundException e){
 System.out.println("No se encontro el archivo");
 java.CrearArchivoError(e.getMessage(), "FileNotFoundException");
 
}
catch(IOException e){
 System.out.println("Error al escribir");
} 
}else
{
    super.WriteTextOutput("Se cancelo el codigo");
}
}

public String ExecutarArchivo(ArrayList<String> texto,ArrayList<String> imports,String Path) {
FileChooser chooser = new FileChooser();
chooser.setTitle("Selecciones donde guardar");
chooser.setInitialDirectory(new java.io.File(Path));
FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Java files (*.java)", "*.java");
chooser.getExtensionFilters().add(extFilter);
chooser.setInitialFileName("Proyecto.java");
File selectedFile = null;
selectedFile = chooser.showSaveDialog(null);
if (selectedFile != null){
String nombre = selectedFile.getName();
String nombreClass = nombre.substring(0, nombre.indexOf("."));
nombreClass.trim();
GeneradorArchivo java = new GeneradorArchivo();
        
try{
    try (FileOutputStream fis = new FileOutputStream(selectedFile);BufferedWriter  dos = new BufferedWriter(new OutputStreamWriter(fis))) {
        for(String Item : imports)
        {
            dos.write(Item);
            dos.newLine();
        }
        String Final = "}}";        
        texto.add(0, "public static void main(String[] args){");
        texto.add(0, "public class "+nombreClass+"{");
        for(String Item : texto)
        {
            dos.write(Item);
            dos.newLine();
        }
        dos.write(Final);
        dos.close();

        return selectedFile.getAbsolutePath();
    }
}
catch(FileNotFoundException e){
 System.out.println("No se encontro el archivo");
 java.CrearArchivoError(e.getMessage(), "FileNotFoundException");
 
}
catch(IOException e){
 System.out.println("Error al escribir");
} 
}else
{
    WriteTextOutput("Se cancelo el codigo");
}
return null;
}
    
}
