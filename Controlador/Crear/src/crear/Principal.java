package crear;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Random;
import java.util.Scanner;

public class Principal {
 
    public static void main(String[] args) {
      
    Scanner leer = new Scanner(System.in);
     Formato op = new Formato();
    System.out.println("Aquí escribiras el nombre del archivo");
    String nombre = leer.nextLine();
    System.out.println("Aquí escribiras un texto");
    String texto = leer.nextLine();
    System.out.println("Aquí escribiras tu dato");
    int dato = leer.nextInt();
    
    op.crearArchivo(nombre, texto, dato);
        

    }
    
}
