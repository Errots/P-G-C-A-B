package Executables;

import Display.RootLayout;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import javafx.application.Platform;

public class EjecutarArchivo extends RootLayout{
     private Thread fxThread;
    private static void printLines(String name, InputStream ins) throws Exception {
    String line = null;
    BufferedReader in = new BufferedReader(
        new InputStreamReader(ins));
    while ((line = in.readLine()) != null) {
        System.out.println(name + " " + line);
    }
  }

  private void runProcess(String command) throws Exception {
    Process pro = Runtime.getRuntime().exec(command);
    printLines(command + " stdout:", pro.getInputStream());
    printLines(command + " stderr:", pro.getErrorStream());
    super.WriteTextOutput(pro.getInputStream().toString());
    super.WriteTextOutput(pro.getErrorStream().toString());
    pro.waitFor();
    System.out.println(command + " exitValue() " + pro.exitValue());
  }
  
  public void Execute(String FilePath, String ProyectPath)
  {
      String FileName = FilePath.substring(FilePath.lastIndexOf("\\")+1);
    try {
      runProcess("javac -d "+ProyectPath+"\\Build "+FilePath);
   
      runProcess("java -cp " +ProyectPath+"\\Build "+FileName.replaceAll(".java", ""));
    } catch (Exception e) {
      WriteTextOutput(e.getMessage());
      e.printStackTrace();
    }
  }

//  public static void main(String[] args) {
//    try {
//      runProcess("javac -cp C:\\Users\\Errot\\Desktop\\ texto.java");
//   
//      runProcess("java -cp C:\\Users\\Errot\\Desktop\\ texto");
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//  }

}

