/*
 * Created on 29.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.common;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
//import java.net.MalformedURLException;
import java.net.URL;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.apache.xml.serialize.LineSeparator;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class RDFConfigFileUpdater {

	//private URL sourceBase;
	private String installConfigFileName;
	private File installDir; //e.g. "C:\\dev_root\\pt\\" file:\\ does not work under win
	private Hashtable fileTable= new Hashtable();
	private Document installXMLDocument;
	
	public RDFConfigFileUpdater(String installFileName,
								String installDir) throws IOException, SAXException, ParserConfigurationException{
		this.installConfigFileName=installFileName;
		this.installDir= new File(installDir);
		
		////
		URL urlInstallFile= new URL(this.installDir.toURI().toURL(), installConfigFileName);
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		//System.out.println("urlInstallFile:"+urlInstallFile+" "+installConfigFileName);
		installXMLDocument = builder.parse(urlInstallFile.toString());
		
		//Node n= installXMLDocument.getFirstChild().getAttributes().getNamedItem("isIntalled");//
		
		//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!:"+Boolean.getBoolean(n.getNodeValue())+" "+ new Boolean(true));
		verifyInstallationState(installXMLDocument);
		
	}
	
	
    public void update() throws Exception{
    	/////////////
    	/*URL urlInstallFile= new URL(installDir.toURI().toURL(), installConfigFileName);
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		System.out.println("urlInstallFile:"+urlInstallFile+" "+installConfigFileName);
		installXMLDocument = builder.parse(urlInstallFile.toString());
		
		//Node n= installXMLDocument.getFirstChild().getAttributes().getNamedItem("isIntalled");//
		
		//System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!:"+Boolean.getBoolean(n.getNodeValue())+" "+ new Boolean(true));
		verifyInstallationState(installXMLDocument);/**/
		
//		if("true".equalsIgnoreCase(n.getNodeValue())){
//			return;
//		}
    	////////////
    	//verifyInstallationState(installXMLDocument);
    	configRDFFile();
    	//n.setNodeValue(Boolean.toString(true));
    	//////////save
    	File instConfigFile	=	new File(installDir,installConfigFileName);
		OutputFormat format = 	new OutputFormat();//(Document)core);
		format.setLineSeparator(LineSeparator.Windows);
		format.setIndenting(true);
		format.setLineWidth(0);             
		format.setPreserveSpace(true);
		XMLSerializer serializer = new XMLSerializer (
		new FileWriter(instConfigFile), format);
		serializer.asDOMSerializer();
		serializer.serialize(installXMLDocument);
		
    	return;
    }
    
    static private void setTextValue(String[] xmlPath,Document rdfDocument){
    	if(xmlPath.length<3){
    		return;
    	}else{
    		//get resource e.g pt:EntitiesTable
    		NodeList nl= rdfDocument.getElementsByTagName(xmlPath[0]);
    		Node node=null;
//    		Node el=nl.item(0); //the resource
//    		nl=el.getOwnerDocument().getElementsByTagName(xmlPath[1]);
    		for(int i=0;i<200;i++){//<200 just to quit somewhen
    			node=nl.item(i);//the resource definition which have a about property
    			if(node==null){
    				break;
    			}else{
    				
    				if(node.getAttributes().getNamedItem("rdf:about")!=null){
    					break;
    				}    				
    			}
    		}
    		if(node!=null){
	    		nl=node.getChildNodes();//getOwnerDocument().getElementsByTagName(xmlPath[1]);
	    		node=nl.item(0);
	    		//el.setNodeValue(xmlPath[2]);
	    		for(int i=0;i<200;i++){//<200 just to quit somewhen
	    			node=nl.item(i);//the resource definition which have a about property
	    			if(node==null){
	    				break;
	    			}else{
	    				String name=node.getNodeName();
	    				//System.out.println("name:"+name+" "+xmlPath[1]);
	    				if(name.equals(xmlPath[1])){
	    					break;
	    				}    				
	    			}
	    		}
	    		
	    		node.setTextContent(xmlPath[2]);
    		}else{
    			System.out.println("could not find:"+xmlPath);
    		}
    	}
    }
    
    
	public void configRDFFile(){
		File rdfFile= new File(installDir,(String)fileTable.get("configFile"));
		
		if(rdfFile.exists()){
			//rdfFile.renameTo(new File(installDir,(String)fileTable.get("rdfFile")+".bk"));
    		try {
				DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document=builder.parse(rdfFile);
				//check if already installed
				//Node n= document.getFirstChild();//
				//System.out.println("NODEROOT:"+n.getNodeName());//+" "+n.getAttributes().getNamedItem("isIntalled"));
				//change the document.
				String ptBaseFolder=(installDir).toString();
				if(!ptBaseFolder.endsWith(File.separator)){
					ptBaseFolder=ptBaseFolder+File.separator;
				}
				String xmlPath[][]={
						{"pt:EntitiesTable", "pt:entitiesFile",(new File(installDir,(String)fileTable.get("entitiesFile"))).toString()},
						//{"pt:InferenceEngine","pt:baseFolder", (new File(installDir)).toString()+File.separator},
						{"pt:InferenceEngine","pt:baseFolder", ptBaseFolder},
						{"pt:InferenceEngine","pt:prologFiles", (String)fileTable.get("prologFiles")},
						{"pt:InferenceEngine","pt:license", (String)fileTable.get("license")},
						{"pt:CredentialStore","pt:file", (new File(installDir,(String)fileTable.get("keystore"))).toString()}
						//{"pt:MetaInterpreter","pt:peerName", "patrice"}		
								};
				
				for(int i=0; i<xmlPath.length;i++){
					setTextValue(xmlPath[i],document);
				}
				
				//backup
				rdfFile.renameTo(new File(installDir,(String)fileTable.get("configFile")+".bk"));
				
				//save
				rdfFile=new File(installDir,(String)fileTable.get("configFile"));
				OutputFormat format = new OutputFormat();//(Document)core);
				format.setLineSeparator(LineSeparator.Windows);
				format.setIndenting(true);
				format.setLineWidth(0);             
				format.setPreserveSpace(true);
				XMLSerializer serializer = new XMLSerializer (
				new FileWriter(rdfFile), format);
				serializer.asDOMSerializer();
				serializer.serialize(document);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}   
    
	public void changeAlias(String newAlias){
		if(newAlias==null){
			return;
		}
		
		File rdfFile= new File(installDir,(String)fileTable.get("configFile"));
		
		if(rdfFile.exists()){
			//rdfFile.renameTo(new File(installDir,(String)fileTable.get("rdfFile")+".bk"));
    		try {
				DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
				DocumentBuilder builder = factory.newDocumentBuilder();
				Document document=builder.parse(rdfFile);
				//check if already installed
				//Node n= document.getFirstChild();//
				//System.out.println("NODEROOT:"+n.getNodeName());//+" "+n.getAttributes().getNamedItem("isIntalled"));
				//change the document.
				String ptBaseFolder=(installDir).toString();
				if(!ptBaseFolder.endsWith(File.separator)){
					ptBaseFolder=ptBaseFolder+File.separator;
				}
				String xmlPath[][]={
						{"pt:MetaInterpreter","pt:peerName", newAlias}		
								};
				
				for(int i=0; i<xmlPath.length;i++){
					setTextValue(xmlPath[i],document);
				}
				
				//backup
				rdfFile.renameTo(new File(installDir,(String)fileTable.get("configFile")+".bk"));
				
				//save
				rdfFile=new File(installDir,(String)fileTable.get("configFile"));
				OutputFormat format = new OutputFormat();//(Document)core);
				format.setLineSeparator(LineSeparator.Windows);
				format.setIndenting(true);
				format.setLineWidth(0);             
				format.setPreserveSpace(true);
				XMLSerializer serializer = new XMLSerializer (
				new FileWriter(rdfFile), format);
				serializer.asDOMSerializer();
				serializer.serialize(document);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SAXException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
    private void verifyInstallationState(Document document)throws IOException{
			String fileName;
			NodeList nList=document.getElementsByTagName("file");
			Node node;
			String fileID;
			File file=null;
			for(int i=0;i<nList.getLength();i++){
				node=nList.item(i);
				fileName=node.getTextContent();
				fileID=node.getAttributes().getNamedItem("ID").getTextContent();
				fileTable.put(fileID,fileName);
				//checkExistence
				file= new File(installDir,fileName);
				if(!file.exists()){
					throw new IOException("file not found:"+file.toString());
				}
			}
			return;
    }
    
    public String getRDFConfigFile() throws IOException{
    	String rdfFileName=(String)fileTable.get("configFile");
    	File rdfFile= new File(installDir,rdfFileName);
    	return rdfFile.toURI().toString();
    }
  
	public static void main(String[] args) throws Exception {
//		InstallationSession is= 
//			new InstallationSession(
//					"file:/C:/dev_root/TomcatPeerTrust/web/PeerTrustConfig/","install.xml",
//					"C:\\dev_root\\pt\\");//http://127.0.0.1:7703/myapp-0.1-dev/
		RDFConfigFileUpdater is= 
			new RDFConfigFileUpdater(
					"install.xml",
					"/home/congo/.peertrust");
					//"C:\\Dokumente und Einstellungen\\pat_dev\\PeerTrustConfig");//http://127.0.0.1:7703/myapp-0.1-dev/
		//System.out.println(is.getFilesToInstall());
		
		//is.changeAlias("coolio");
		is.update();
		
	}

}
