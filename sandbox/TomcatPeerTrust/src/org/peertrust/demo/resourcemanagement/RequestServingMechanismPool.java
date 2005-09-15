package org.peertrust.demo.resourcemanagement;

import java.io.IOException;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RequestServingMechanismPool {
	

	private Hashtable mechanismsPool; 
	private RequestServingMechanism defaultMechanism;
	
	public RequestServingMechanismPool(){
		mechanismsPool= new Hashtable();
		defaultMechanism=null;
	}
	
	public void setup(String xmlSetupFilePath)throws IOException, SetupException{
		if(xmlSetupFilePath==null){
			new NullPointerException("Parameter urlOfXMLConfigFile");
		}
		
		try {
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document dom = builder.parse(xmlSetupFilePath);
			
			NodeList rootNodeList=
				dom.getElementsByTagName(RequestServingMechanism.ROOT_TAG_SERVING_MECHANISM);
			Element mRootNode=null;
			if(rootNodeList.getLength()!=1){//
				throw new Error(	
						"Illegal xml config file. It must contain exactelly one "+
						"<"+RequestServingMechanism.ROOT_TAG_SERVING_MECHANISM+"> but contains "+
						rootNodeList.getLength());
			}else{
				mRootNode=(Element)rootNodeList.item(0);
			}
			
			//String type= dom.getFirstChild().getAttributes().getNamedItem("type").getNodeValue();
			
			NodeList mechanismNodeList=
					mRootNode.getElementsByTagName(RequestServingMechanism.MECHANISM_TAG);
			RequestServingMechanism m;
			for(int i=mechanismNodeList.getLength()-1;i>=0;i--){
				m=getRequestServingMechnaismFromXMLNode((Node)mechanismNodeList.item(i));
				System.out.println("i:"+i+" m:"+m);
				if(m.getMatchingPattern().equals("*")){
					defaultMechanism=m;
				}else{
					mechanismsPool.put(m.getMechanismName(),m);
				}
			}
			
			//defaultMechanism=(RequestServingMechanism)mechanismsPool.get(RequestServingMechanism.DEFAULT_NAME);
			if(defaultMechanism==null){
				throw new Error("No default mechnanism defined. "+
								"please define a mechanism with * as matching pattern");
			}
			
			System.out.println("\n=================================================================");
			System.out.println(""+defaultMechanism);
			System.out.println("\n=================================================================");
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw new SetupException("could not setup mechanism",e);
		} catch (SAXException e) {
			e.printStackTrace();
			throw new SetupException("could not setup mechanism",e);
		} catch (IOException e) {
			e.printStackTrace();
			throw new SetupException("could not setup mechanism",e);
		}
	}
	
	static private RequestServingMechanism getRequestServingMechnaismFromXMLNode(Node mechanismNode)
													throws SetupException{
		
		try{
			NamedNodeMap attrs=mechanismNode.getAttributes();			
			String mechanismClassName=attrs.getNamedItem(RequestServingMechanism.ATTRIBUTE_CLASS).getTextContent();
			Class mechanismClass=Class.forName(mechanismClassName);
			RequestServingMechanism mechanism= 
				(RequestServingMechanism)mechanismClass.newInstance();
			mechanism.setup(mechanismNode);
			return mechanism;
		}catch(NullPointerException e){
			throw new SetupException("could not build mechrereanaism",e);
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			throw new SetupException("could not build mechanism",e);
		} catch (InstantiationException e) {
			//e.printStackTrace();
			throw new SetupException("could not build mechanism",e);
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
			throw new SetupException("could not build mechanism",e);
		}
			
	}
	
	public RequestServingMechanism getMechanism(String url){
		if(url==null){
			return defaultMechanism;
		}
		RequestServingMechanism m=null;
		for(Enumeration e=mechanismsPool.elements();e.hasMoreElements();){
			m=(RequestServingMechanism)e.nextElement();
			if(Pattern.matches(m.getMatchingPattern(),url)){
				return m;
			}
		}
		return defaultMechanism;//nothing found
		
	}
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return 	"\nResquestServingMechanismPool:"+
				"default:"+defaultMechanism+
				"\nPool:"+this.mechanismsPool.toString()+"\n";
	}
	

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String xmlSetupFilePath=
			"/home/pat_dev/eclipse_home/workspace_3_1/TomcatPeerTrust/web/resource_management_files/resource_mng_config.xml";
		RequestServingMechanismPool pool=
					new RequestServingMechanismPool();
		pool.setup(xmlSetupFilePath);
		System.out.println("\n------------------------------default:\n"+
													pool.getMechanism("_default_"));
		System.out.println("\n------------------------------default:\n"+
													pool.getMechanism("/gdfdhdfg/demo/forward_to_service_jsp"));
	}

}
