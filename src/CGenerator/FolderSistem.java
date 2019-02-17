package CGenerator;

import Display.RootLayout;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import javafx.scene.control.TextInputDialog;
import javafx.stage.DirectoryChooser;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import java.nio.file.Path;
import java.util.function.Consumer;


public class FolderSistem extends RootLayout{
    public void FolderSistem(){
        String Pathname = "";
        String Message = "";
        
        // Directorios
        DirectoryChooser  filepath = new DirectoryChooser ();
        filepath.setInitialDirectory(new java.io.File("."));
        filepath.setTitle("Seleccione una carpeta");
        File selectedFile = filepath.showDialog(null);

        if (selectedFile != null) { 
          System.out.println("getCurrentDirectory(): " 
             +  selectedFile.getAbsolutePath());
          System.out.println("getSelectedFile() : " 
             +  selectedFile.getName());
          TextInputDialog dialog = new TextInputDialog("Tran");
 
            dialog.setTitle("Nombre del proyecto");
            dialog.setHeaderText(null);
            dialog.setContentText("Nombre:");

            Optional<String> result = dialog.showAndWait();

            Pathname = selectedFile.getAbsolutePath()+"\\"+result.get();
            Path path = Paths.get(selectedFile.getAbsolutePath()+"\\"+result.get()+"\\Build\\..\\SRC\\..\\Data");
            // Archivo Texto
            try {
                Files.createDirectories(path);
                String hora,fecha,Espacio;
                File f=new File(Pathname+"\\Info.txt");
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
                        dos.writeChars(Espacio);
                        dos.writeChars("Proyecto "+result.get()+" creado con exito");
                        dos.writeChars(Espacio);
                        dos.writeChars("Proyecto generado por GCAB");
                        dos.writeChars(Espacio);
                        dos.writeChars(hora);
                        dos.writeChars(Espacio);
                        dos.writeChars(fecha);
                    }
                    // Archivo XML
                    try
                    {
                      DocumentBuilderFactory DBFact = DocumentBuilderFactory.newInstance();
                      DocumentBuilder dBuilder = DBFact.newDocumentBuilder();
                      Document doc = dBuilder.newDocument();

                      Element Proyect = doc.createElement("Proyect");
                      doc.appendChild(Proyect);
                      Attr attr = doc.createAttribute("Name");
                      attr.setValue(result.get());
                      Proyect.setAttributeNode(attr);

                      Element ProyectConfig = doc.createElement("ProyectConfig");
                      Proyect.appendChild(ProyectConfig);

                      Element ProyectPath = doc.createElement("ProyectPath");
                      ProyectPath.appendChild(doc.createTextNode(Pathname));
                      ProyectConfig.appendChild(ProyectPath);

                      Element MainFile = doc.createElement("MainFile");
                      ProyectConfig.appendChild(MainFile);

                      Element NodesIds = doc.createElement("NodesIds");
                      ProyectConfig.appendChild(NodesIds);

                      Element ColeccionDatos = doc.createElement("CollectionsData");
                      ProyectConfig.appendChild(ColeccionDatos);

                      TransformerFactory transformerFactory = TransformerFactory.newInstance();
                      Transformer transformer = transformerFactory.newTransformer();
                      DOMSource source = new DOMSource(doc);
                      StreamResult resultdoc = new StreamResult(new File(Pathname+"\\Build.xml"));
                      transformer.transform(source, resultdoc);

                      // Output to console for testing
                      StreamResult consoleResult = new StreamResult(System.out);
                      transformer.transform(source, consoleResult);

                    }catch(Exception e){
                        Message = e.getMessage();
                        System.out.println(e.getMessage());
                    }

                }
                catch(FileNotFoundException e){
                    Message = e.getMessage();
                    System.out.println("No se encontro el archivo");
                }
                catch(IOException e){
                    Message = e.getMessage();
                    System.out.println("Error al escribir");
                }
            } catch (IOException y) {
                WriteTextOutput(y.getMessage());
                System.err.println("Cannot create directories - " + y);
            }
        }
        super.WriteTextOutput(Message);
        super.SavePath(Pathname);      
    }
        
}
    
    

