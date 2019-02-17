package Configurations;

import CGenerator.DataCollector;
import Display.RootLayout;
import Display.TiposdeIconos;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import javafx.stage.FileChooser;
import org.w3c.dom.Attr;

public class ProyectConfigurator extends RootLayout{
    public ConfigurationData OpenProyect()
    {
        ConfigurationData data = new ConfigurationData();
        try{
        FileChooser  filepath = new FileChooser ();
        filepath.setInitialDirectory(new java.io.File("."));
        filepath.setTitle("Seleccione una carpeta");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        filepath.getExtensionFilters().add(extFilter);
        File selectedFile = filepath.showOpenDialog(null);
            if (selectedFile.isFile()) {
                DocumentBuilderFactory factory =
                DocumentBuilderFactory.newInstance();
                DocumentBuilder builder = factory.newDocumentBuilder();
                Document doc = builder.parse(selectedFile);
                doc.getDocumentElement().normalize();
                
                NodeList nList = doc.getElementsByTagName("Proyect");
                Node nNode = nList.item(0);
                Element eElement = (Element) nNode;
                WriteTextOutput("Incializando proyecto "+eElement.getAttribute("Name"));
                
                NodeList Config = doc.getElementsByTagName("ProyectConfig");
                Node ConfigNode = Config.item(0);
                Element ConfigElement = (Element) ConfigNode;
                
                data.MainFile =ConfigElement.getElementsByTagName("MainFile").item(0).getTextContent();
                data.ProyectPath =ConfigElement.getElementsByTagName("ProyectPath").item(0).getTextContent();
                
                NodeList NodeIds = doc.getElementsByTagName("NodesIds");
                if (NodeIds.getLength() != 0) {
                    Node NodeIdsNode = NodeIds.item(0);
                Element NodeIdsElement = (Element) NodeIdsNode;
                NodeList NodesData = NodeIdsElement.getElementsByTagName("Id");
                for (int i = 0; i < NodesData.getLength(); i++) {
                    Node Idata = NodesData.item(i);
                    data.NodesIds.add(Idata.getTextContent());
                }
                }
                
                
                NodeList ColeccionDatos = doc.getElementsByTagName("CollectionsData");
                if (ColeccionDatos.getLength() !=0) {
                    Node ColeccionDatosNode = ColeccionDatos.item(0);
                Element ColeccionDatosElement = (Element) ColeccionDatosNode;
                NodeList ColeccionData = ColeccionDatosElement.getElementsByTagName("CollectionData");
                for (int i = 0; i < ColeccionData.getLength(); i++) {
                    Element Cdata = (Element)ColeccionData.item(i);
                    
                    DataCollector coleccion = new DataCollector();
                    coleccion.Id = Cdata.getElementsByTagName("id").item(0).getTextContent();
                    coleccion.Id2 = Cdata.getElementsByTagName("id2").item(0).getTextContent();
                    coleccion.NombreItem = Cdata.getElementsByTagName("NombreItem").item(0).getTextContent();
                    coleccion.ValorItem = Cdata.getElementsByTagName("Valor").item(0).getTextContent();
                    coleccion.TipoItem = Cdata.getElementsByTagName("TipoItem").item(0).getTextContent();
                    Element Valido = (Element)Cdata.getElementsByTagName("Valido").item(0);
                    coleccion.noValido = !Valido.getAttribute("Valor").isEmpty();
                    coleccion.positionX = Double.valueOf(Cdata.getElementsByTagName("X").item(0).getTextContent());
                    coleccion.positionY = Double.valueOf(Cdata.getElementsByTagName("Y").item(0).getTextContent());      
                    
                    NodeList Nlinks = Cdata.getElementsByTagName("LinkId");
                    for (int t = 0; t < Nlinks.getLength(); t++) {
                        coleccion.mLinkIds.add(Nlinks.item(t).getTextContent());
                    }
                    NodeList Ntext = Cdata.getElementsByTagName("Texto");
                    for (int t = 0; t < Ntext.getLength(); t++) {
                        coleccion.Textos.add(Ntext.item(t).getTextContent());
                    }
                    NodeList Nchild = Cdata.getElementsByTagName("ChildId");
                    for (int t = 0; t < Nchild.getLength(); t++) {
                        coleccion.ChildIds.add(Nchild.item(t).getTextContent());
                    }

                    data.ColeccionDatos.add(coleccion);
                }
                } 
                return data;
            }
        }catch(Exception e){WriteTextOutput(e.getMessage());}
        return data;
    }
    
    public void SaveProyect(ConfigurationData data)
    {
        try{
            File inputFile = new File(data.ProyectPath+"\\Build.xml");
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(inputFile);
            
            doc.getDocumentElement().normalize();

                NodeList Config = doc.getElementsByTagName("ProyectConfig");
                Node ConfigNode = Config.item(0);
                Element ConfigElement = (Element) ConfigNode;
                
                ConfigElement.getElementsByTagName("MainFile").item(0).setTextContent(data.MainFile);
                ConfigElement.getElementsByTagName("ProyectPath").item(0).setTextContent(data.ProyectPath);
                
                NodeList NodeIds = doc.getElementsByTagName("NodesIds");
                if (!data.NodesIds.isEmpty()) {
                    Node NodeIdsNode = NodeIds.item(0);
                    Element NodeIdsElement = (Element) NodeIdsNode;
                    NodeList NodesData = NodeIdsElement.getElementsByTagName("Id");
                    for (int i = 0; i < data.NodesIds.size(); i++) {
                        Node Idata = NodesData.item(i);
                        if (!data.NodesIds.get(i).equals(Idata.getTextContent())) {
                            Idata.setTextContent(data.NodesIds.get(i));
                        }else if (Idata == null) {
                            Element Id = doc.createElement("Id");
                            Id.appendChild(doc.createTextNode(data.NodesIds.get(i)));
                            NodeIdsElement.appendChild(Id);
                        }
                    }
                }else
                {
                    Node NodeIdsNode = NodeIds.item(0);
                    Element NodeIdsElement = (Element) NodeIdsNode;
                    NodeList NodesData = NodeIdsElement.getElementsByTagName("Id");
                    for (int i = 0; i < NodesData.getLength(); i++) {
                        Node Idata = NodesData.item(i);
                        NodeIdsNode.removeChild(Idata);
                    }
                }
                
                
                NodeList ColeccionDatos = doc.getElementsByTagName("CollectionsData");
                if (!data.ColeccionDatos.isEmpty()) {
                    Node ColeccionDatosNode = ColeccionDatos.item(0);
                Element ColeccionDatosElement = (Element) ColeccionDatosNode;
                for (int i = 0; i < data.ColeccionDatos.size(); i++) {
                    
                    Element dColecc = doc.createElement("CollectionData");
                    ColeccionDatosElement.appendChild(dColecc);
                    
                    Element id = doc.createElement("id");
                    id.appendChild(doc.createTextNode(data.ColeccionDatos.get(i).Id));
                    dColecc.appendChild(id);
                    
                    Element id2 = doc.createElement("id2");
                    id2.appendChild(doc.createTextNode(data.ColeccionDatos.get(i).Id2));
                    dColecc.appendChild(id2);
                    
                    Element NombreItem = doc.createElement("NombreItem");
                    NombreItem.appendChild(doc.createTextNode(data.ColeccionDatos.get(i).NombreItem));
                    dColecc.appendChild(NombreItem);
                    
                    Element TipoItems = doc.createElement("TipoItem");
                    TipoItems.appendChild(doc.createTextNode(data.ColeccionDatos.get(i).TipoItem));
                    dColecc.appendChild(TipoItems);
                    
                    Element Valor = doc.createElement("Valor");
                    Valor.appendChild(doc.createTextNode(data.ColeccionDatos.get(i).ValorItem));
                    dColecc.appendChild(Valor);
                    
                    Element Valido = doc.createElement("Valido");
                    dColecc.appendChild(Valido);
                    Attr attr = doc.createAttribute("Valor");
                    if (data.ColeccionDatos.get(i).noValido) {
                        attr.setValue("true");
                    }
                    attr.setValue("");
                    Valido.setAttributeNode(attr);
                    
                    Element X = doc.createElement("X");
                    X.appendChild(doc.createTextNode(Double.toString(data.ColeccionDatos.get(i).positionX)));
                    dColecc.appendChild(X);
                    
                    Element Y = doc.createElement("Y");
                    Y.appendChild(doc.createTextNode(Double.toString(data.ColeccionDatos.get(i).positionY)));
                    dColecc.appendChild(Y);
                    
                    for (int t = 0; t < data.ColeccionDatos.get(i).mLinkIds.size(); t++) {
                        Element LinkId = doc.createElement("LinkId");
                        LinkId.appendChild(doc.createTextNode(data.ColeccionDatos.get(i).mLinkIds.get(t)));
                        dColecc.appendChild(LinkId);
                    }
                    
                    for (int t = 0; t < data.ColeccionDatos.get(i).Textos.size(); t++) {
                        Element Texto = doc.createElement("Texto");
                        Texto.appendChild(doc.createTextNode(data.ColeccionDatos.get(i).Textos.get(t)));
                        dColecc.appendChild(Texto);
                    }
                    for (int t = 0; t < data.ColeccionDatos.get(i).ChildIds.size(); t++) {
                        Element ChildId = doc.createElement("ChildId");
                        ChildId.appendChild(doc.createTextNode(data.ColeccionDatos.get(i).ChildIds.get(t)));
                        dColecc.appendChild(ChildId);
                    }
                }
                } 
            
        }catch(Exception e){WriteTextOutput(e.getMessage());}
    }
}
