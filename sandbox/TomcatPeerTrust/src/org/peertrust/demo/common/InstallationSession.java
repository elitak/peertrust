/*
 * Created on 19.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Hashtable;
import java.util.Vector;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.xml.serialize.LineSeparator;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
//import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
//import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * @author pat_dev
 *Use to install the most recent pt files to the local system.
 */
public class InstallationSession {
	static final String[] FILES_IDS= {	"prologFile",
										"entitiesFile",
										"configFile",
										"license",
										"keystore"};
	private URL sourceBase;
	private String installConfigFileName;
	private String installDir; 
	
	private String message="None";
	
	private DocumentBuilder builder;
	private DocumentBuilderFactory factory;
	private Document localInstallDom=null;
	private Document remoteInstallDom=null;
	private Vector appFiles;
	private Vector userCustomFiles;
	
	private Hashtable appFileTable= new Hashtable();
	
	/**
	 * Create an InstallationSession with a specified source base, 
	 * the installation file name and theloacal installation dir
	 * @param sourceBase -- the directory from with install data can be loaded
	 * @param installFileName -- the name of the installation frame
	 * @param installDir -- the local installation dir
	 * @throws ParserConfigurationException 
	 * @throws SAXException
	 * @throws IOException
	 */
	public InstallationSession(	String sourceBase, 
								String installFileName,
								String installDir) throws ParserConfigurationException, SAXException, IOException{
		if(sourceBase==null){
			throw new NullPointerException("Param sourceBase is null");
		}else if(installFileName==null){
			throw new NullPointerException("Param installFileName is null");
		}else if(installDir==null){
			throw new NullPointerException("Param installDir is null");
		}else{
			this.sourceBase= new URL(sourceBase);
			this.installConfigFileName=installFileName;
			this.installDir=installDir;
			
			///xml objects
			factory=DocumentBuilderFactory.newInstance();
			builder = factory.newDocumentBuilder();
	//		try {
				localInstallDom=getLocalInstallDocument();
	//		} catch (IOException e) {//pt not install
	//			localInstallDom=null;
	//		}
			
			remoteInstallDom=getRemoteInstallDocument();
			
			appFiles=getAppFilesToInstall();
			userCustomFiles=getUserCustomFilesToInstall();
		}
	}
	
	/**
	 * Checks if the pt is install.
	 * @return
	 * @throws SAXException
	 * @throws IOException
	 */
	public boolean isPeerTrustInstall() throws SAXException, IOException{
    	File ptDir= null;
    	ptDir=new File(installDir);
    	if(ptDir.exists()){
    		return verifyInstallDirContent(ptDir);
    	}else{
    		message="PeerTrust Not install";
    		return false;
    	}
    }
    
	/**
	 * Checks if the installation is uptodate.
	 * @return
	 */
	public boolean isInstallationUptodate(){
		String remoteVersion=getVersion(remoteInstallDom);
		String localVersion=getVersion(localInstallDom);
		if(localVersion==null || remoteVersion==null){
			return false;
		}else if(remoteVersion.compareTo(localVersion)>0){
			return false;
		}else{
			return true;
		}
	}
	
	/**
	 * Installs the peer trust files.
	 * @throws SAXException
	 * @throws IOException
	 */
    public void install() throws SAXException, IOException{
    	if(!isInstallationUptodate()){
    		doInstallation(true,true);
    			localInstallDom=getLocalInstallDocument();
    	}
    	return;
    }
    
    
    public void doInstallation(boolean doInstall, boolean doInstallUserCustomFiles) throws IOException{
    	if(doInstall){
	    	File ptDir = new File(installDir);
	    	if(!ptDir.exists()){
	    		ptDir.mkdirs();
	    	}	    	
	    	//copy the file to local folder
	    	URL url= null;
	    	String fileName;
	    	File file;
	    	//application specific files force copy
	    	int len=appFiles.size();
	    	for(int i=0;i<len;i++){
	    		fileName=(String)appFiles.elementAt(i);
	    		url=new URL(	sourceBase,
	    						fileName);
	    		file=new File(ptDir,fileName);
	    		if(!file.exists()){
	    			file.createNewFile();
	    		}
	    		copyFile(url.toString(),file);    		
	    	}
	    	
	    	//copy install file to local file system
    		file=new File(ptDir,this.installConfigFileName);
    		url = new URL ( sourceBase,this.installConfigFileName) ;
    		if(!file.exists()){
    			file.createNewFile();
    			copyFile(url.toString(),file);  
    		}
    	}
    		
    	//user custom files copy if only not existant
    	if(doInstallUserCustomFiles){
	    	File ptDir = new File(installDir);
	    	URL url= null;
	    	String fileName;
	    	File file;
    		//copy user custom files only if the do not exists
    		final int len=userCustomFiles.size();
	    	for(int i=0;i<len;i++){
	    		fileName=(String)userCustomFiles.elementAt(i);
	    		url=new URL(	sourceBase,
	    						fileName);
	    		file=new File(ptDir,fileName);
	    		if(!file.exists()){
	    			file.createNewFile();
	    			copyFile(url.toString(),file);  
	    		}	
	    	}
    	}	    	
    	//change rdf config file
    	configRDFFile();
    	
    }
    
    public void setTextValue(String[] xmlPath,Document rdfDocument){
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
    			//System.out.println("could not find:"+xmlPath);
    		}
    	}
    }
    
    public String getConfigFileURL() throws SAXException, IOException{
    	getAppFilesToInstall();//ensure table init
    	File rdfFile= new File(installDir,(String)appFileTable.get("configFile"));
    	return rdfFile.toURI().toURL().toString();
    }
    
	public void configRDFFile(){
		File rdfFile= new File(installDir,(String)appFileTable.get("configFile"));
		
		if(rdfFile.exists()){
			//rdfFile.renameTo(new File(installDir,(String)fileTable.get("rdfFile")+".bk"));
    		try {
    			Document document=builder.parse(rdfFile);
				//change the document.
				String ptBaseFolder=(new File(installDir)).toString();
				if(!ptBaseFolder.endsWith(File.separator)){
					ptBaseFolder=ptBaseFolder+File.separator;
				}
				String xmlPath[][]={
								{"pt:EntitiesTable", "pt:entitiesFile",(new File(installDir,(String)appFileTable.get("entitiesFile"))).toString()},
								//{"pt:InferenceEngine","pt:baseFolder", (new File(installDir)).toString()+File.separator},
								{"pt:InferenceEngine","pt:baseFolder", ptBaseFolder},
								{"pt:InferenceEngine","pt:prologFiles", (String)appFileTable.get("prologFiles")},
								{"pt:InferenceEngine","pt:license", (String)appFileTable.get("license")},
								{"pt:CredentialStore","pt:file", (new File(installDir,(String)appFileTable.get("keystore"))).toString()}
								};
				
				for(int i=0; i<xmlPath.length;i++){
					setTextValue(xmlPath[i],document);
				}
				
				//backup
				rdfFile.renameTo(new File(installDir,(String)appFileTable.get("configFile")+".bk"));
				
				//save
				rdfFile=new File(installDir,(String)appFileTable.get("configFile"));
				OutputFormat format = new OutputFormat();//(Document)core);
				format.setLineSeparator(LineSeparator.Windows);
				format.setIndenting(true);
				format.setLineWidth(0);             
				format.setPreserveSpace(true);
				XMLSerializer serializer = new XMLSerializer (
				new FileWriter(rdfFile), format);
				serializer.asDOMSerializer();
				serializer.serialize(document);
			} catch (SAXException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
    
    public void copyFile(String httpSource, File localDestination){
		HttpClient httpClient= new HttpClient();
		GetMethod getMethod= new GetMethod(httpSource);
		try {
			httpClient.executeMethod(getMethod);
			byte[] fileContent=getMethod.getResponseBody();			
			FileOutputStream fStream=
				new FileOutputStream(localDestination);
			fStream.write(fileContent);
			fStream.close();
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
    
    public boolean verifyInstallDirContent(File instDir) throws SAXException, 
																IOException{

    	File file;
    	String fileName;
    	int len=appFiles.size();    	
    	for(int i=0;i<len;i++){
    		fileName=(String)appFiles.elementAt(i);
			file= new File(instDir,fileName);
			if(!file.exists()){
				message="Could not find "+fileName+
						" in "+instDir;
				return false;
			}
		}
    	
    	len=userCustomFiles.size();    	
    	for(int i=0;i<len;i++){
    		fileName=(String)userCustomFiles.elementAt(i);
			file= new File(instDir,fileName);
			if(!file.exists()){
				message="Could not find "+fileName+
						" in "+instDir;
				return false;
			}
		}
    	
    	return true;
    }
    
    
    
    public Vector getAppFilesToInstall() throws SAXException, IOException{
    	Vector fNames= new Vector();
    	
    	Document document = remoteInstallDom;//getLocalInstallDocument();
		String fileName;
		NodeList nList=document.getElementsByTagName("appFiles");
		//System.out.println(" e:"+nList.item(0).getNodeName());
		nList=nList.item(0).getChildNodes();//document.getElementsByTagName("file");
		//System.out.println(" w:"+nList.item(1).getNodeName());
		Node node;
		String fileID;
		for(int i=0;i<nList.getLength();i++){
			node=nList.item(i);
			if(node.getNodeName().equals("file")){
				fileName=node.getTextContent();
				//System.out.println("nodeName:"+node.getNodeName()+" fileName:"+fileName);
				fileID=node.getAttributes().getNamedItem("ID").getTextContent();
				fNames.add(node.getTextContent());
				appFileTable.put(fileID,fileName);
			}
		}			
		return fNames;
    }
    /**
     * Gets the user costom classes which should be copied to the loacl system.
     * it also populates the appFileTable with those file
     * @return a Vector of user custom files.
     * @throws SAXException
     * @throws IOException
     */
    private Vector getUserCustomFilesToInstall() throws SAXException, IOException{
    	Vector fNames= new Vector();
    	
    	Document document = remoteInstallDom;//getLocalInstallDocument();
		String fileName;
		NodeList nList=document.getElementsByTagName("userCustomFiles");
		nList=nList.item(0).getChildNodes();//document.getElementsByTagName("file");
		Node node;
		String fileID;
		for(int i=0;i<nList.getLength();i++){
			node=nList.item(i);
			if(node.getNodeName().equals("file")){
				fileName=node.getTextContent();
				fileID=node.getAttributes().getNamedItem("ID").getTextContent();
				fNames.add(node.getTextContent());
				appFileTable.put(fileID,fileName);
			}
		}			
		return fNames;
    }
    /**
     * Gets the intall.xml from the server and build a corresponding document.
     * @return reteurn the Document build from install.xml
     * @throws SAXException
     * @throws IOException
     * @throws NullPointerException
     */
    private Document getRemoteInstallDocument() throws 	SAXException, 
														IOException,
														NullPointerException{
    		if(this.sourceBase==null){
    			throw new NullPointerException("sourceBase is null");
    		}else if(this.installConfigFileName==null){
    			throw new NullPointerException("installConfigFileName is null");
    		}else{
	    		URL url= new URL(this.sourceBase,this.installConfigFileName);
				Document doc=builder.parse(url.toString());
				return doc;
    		}
    }
    /**
     * Gets the locally install install.xml as document.
     * 
     * @return a Document built from local install.xml 
     * 			or null if the file does not exists or
     * 			any exception during parsing
     * @throws SAXException
     * @throws IOException
     */
    private Document getLocalInstallDocument() throws NullPointerException{
    	
    	if(this.sourceBase==null){
			throw new NullPointerException("sourceBase is null");
		}else if(this.installConfigFileName==null){
			throw new NullPointerException("installConfigFileName is null");
		}else{
			File docFile= new File(this.installDir,this.installConfigFileName);
			if(!docFile.exists()){
	    		return null;
	    	}
	    	try{
	    		Document doc=builder.parse(docFile);
	    		return doc;
	    	}catch(Exception e){
	    		return null;
	    	}
		}
    }
    
    /**
     * Gets the version of the install.xml document.
     * @param doc - a Document built from an install.xml file
     * @return the version as string 
     * 			or null if doc is null or the document das not
     * 			have a version attribute  
     */   
    final private static String getVersion(Document doc){
    	if(doc==null){
    		return null;
    	}
    	Node root=doc.getFirstChild();
    	Node versionAtt=root.getAttributes().getNamedItem("version");
    	if(versionAtt==null){
    		return null;
    	}else{
    		String version= versionAtt.getTextContent();   	
    		return  version;
    	}
    }
    
    
    
	public String getMessage() {
		return message;
	}
	
	public static void main(String[] args) throws Exception{
//		InstallationSession is= 
//			new InstallationSession(
//					"file:/C:/dev_root/TomcatPeerTrust/web/PeerTrustConfig/","install.xml",
//					"C:\\dev_root\\pt\\");//http://127.0.0.1:7703/myapp-0.1-dev/
		System.out.println("1.0.1>1.0.0:"+("1.0.0.1".compareTo("1.0.0")));
		InstallationSession is= 
			new InstallationSession(
					"http://127.0.0.1:7703/myapp-0.1-dev/PeerTrustConfig/","install.xml",
					"C:\\dev_root\\pt\\");//http://127.0.0.1:7703/myapp-0.1-dev/
		//System.out.println(is.getFilesToInstall());
		is.install();
		//is.configRDFFile();
//		System.out.println("isInst:"+is.isPeerTrustInstall()+
//							"\n"+is.getMessage());
		
	}
	
	
}
