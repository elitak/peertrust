package org.peertrust.demo.resourcemanagement;

import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class RequestServingMechanismPool {
	final static public String MECHANISM_TAG="mechanism";
	final static public String ATTRIBUTE_CLASS="class";
	final static public String ATTRIBUTE_NAME="name";
	final static public String ATTRIBUTE_FORWARD_TO="forwardTo";
	final static public String DEFAULT_NAME="default";
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
			
			//String type= dom.getFirstChild().getAttributes().getNamedItem("type").getNodeValue();
			
			NodeList mechanismNodeList=
					dom.getElementsByTagName(MECHANISM_TAG);
			System.out.println("Size node list:"+mechanismNodeList.getLength());
			RequestServingMechanism m;
			for(int i=mechanismNodeList.getLength()-1;i>=0;i--){
				m=getRequestServingMechnaismFromXMLNode((Node)mechanismNodeList.item(i));
				System.out.println("i:"+i+" m:"+m);
				mechanismsPool.put(m.getMechanismName(),m);
			}
			defaultMechanism=(RequestServingMechanism)mechanismsPool.get(DEFAULT_NAME);
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
			String mechanismClassName=attrs.getNamedItem(ATTRIBUTE_CLASS).getTextContent();
			Class mechanismClass=Class.forName(mechanismClassName);
			RequestServingMechanism mechanism= 
				(RequestServingMechanism)mechanismClass.newInstance();
			mechanism.setup(mechanismNode);
			return mechanism;
		}catch(NullPointerException e){
			throw new SetupException("could not build mechnaism",e);
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
	
	public RequestServingMechanism getMechanism(String mechanismName){
		if(mechanismName==null){
			return defaultMechanism;
		}
		
		RequestServingMechanism mechanism= 
			(RequestServingMechanism)mechanismsPool.get(mechanismName);
		if(mechanism!=null){
			return mechanism;
		}else{
			return defaultMechanism;
		}
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
			"/home/pat_dev/eclipse_home/workspace_3_1/TomcatPeerTrust/web/resource_management_files/request_serving_mechanism.xml";
		RequestServingMechanismPool pool=
					new RequestServingMechanismPool();
		pool.setup(xmlSetupFilePath);
		System.out.println("\n------------------------------default:\n"+
													pool.getMechanism("default"));
		System.out.println("\n------------------------------default:\n"+
													pool.getMechanism("forward_to_service_jsp"));
	}

}
