package org.peertrust.demo.resourcemanagement;

import java.io.IOException;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * RequestServingMechanismPool is a pool of RequestServingMechanism.
 * The response to a resource request depends:
 *	 i) on the protection level of the of the resource ,
 *	 ii)on the setup level of the http session (is the peer already known 
 *		by the server)
 * A RequestServingMechnism specifies the steps to serve a request. 
 * Its XML represention is the <mechanism> element. 
 * The class attribute 
 * the class that implement the particular mechnism. E.g. RequestServingByFollowingChain 
 * which just follow the normal request hanndling chain. 
 * In some cases (e.g session registration) it is necessary to 
 * forward the request to another handler(jsp or servlet). 
 * the forwardto attribute is used to specify a url of the service.
 * The mechnism are gathered in a pool represented by the 
 * <RequestservingMechnism> element. 
 * Note that This element must contains a mechanism named default, 
 * which will handle the request that match not other mechnism. 
 * @author Patrice Congo(token77)
 *
 */
public class RequestServingMechanismPool implements Configurable
{
	

	/**
	 * an hastable containing the request serving mechanisms
	 */
	private Hashtable mechanismsPool; 
	
	/**
	 * The default serving mechanism
	 */
	private RequestServingMechanism defaultMechanism;
	
	/**
	 * the path to the xml setting path
	 */
	private StringWrapper setupFilePath;
	
	/**
	 * Construct a virgin RequestServingMechanismPool.
	 * To fill the pool, the following steps are necessary:
	 * <ul>
	 * 	<li/>Set the setupFilePath using the appropriate setter
	 * 	<li/>Call the init to fill the pool from the xml mechnism description
	 * </ul>
	 *
	 */
	public RequestServingMechanismPool()
	{
		mechanismsPool= new Hashtable();
		defaultMechanism=null;
	}
	
	/**
	 *	Parses  the xml description file an fill the pool accordingly. 
	 * @param xmlSetupFilePath
	 * @throws IOException
	 * @throws SetupException
	 */
	private void setup(	StringWrapper xmlSetupFilePath)
						throws IOException, SetupException
	{
		if(xmlSetupFilePath==null){
			new NullPointerException("Parameter urlOfXMLConfigFile");
		}
		
		try {
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document dom = builder.parse(xmlSetupFilePath.getWrappedString());
			
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
	
	/**
	 * Build a RequestSeringMechanism object from the provided
	 * xml node
	 * @param mechanismNode -- the xml document node
	 * @return the RequestServingMechanism represented by the xml node
	 * @throws SetupException
	 */
	static private RequestServingMechanism 
						getRequestServingMechnaismFromXMLNode(
													Node mechanismNode)
													throws SetupException
	{
		
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
	
	/**
	 * To get the mechanism corresponding that is to be used to
	 * serves the provided request url
	 * @param url -- the requested url
	 * @return the RequestServingMechanism for this url 
	 */
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
	
	/**
	 * To get the RequestServingMechanism with the specified name.
	 * @param name -- the name of the mechanism
	 * @return the RequetServingMechanism with the specified name
	 */
	public RequestServingMechanism getMechanismByName(String name){
		if(name==null){
			return null;
		}else{
			RequestServingMechanism m=(RequestServingMechanism)mechanismsPool.get(name);		
			return m;
		}
		
	}
	
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return 	"\nResquestServingMechanismPool:"+
				"default:"+defaultMechanism+
				"\nPool:"+this.mechanismsPool.toString()+"\n";
	}
	

	
	
	
	
	/**
	 * @return Returns the setupFilePath.
	 */
	public StringWrapper getSetupFilePath() {
		return setupFilePath;
	}

	/**
	 * @param setupFilePath The setupFilePath to set.
	 */
	public void setSetupFilePath(StringWrapper setupFilePath) {
		this.setupFilePath = setupFilePath;
	}

	/**
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {
		if(setupFilePath==null){
			throw new ConfigurationException(
					"setupFilePath must not be null"); 
		}
		
		try {
			setup(setupFilePath);
		} catch (IOException e) {
			throw new ConfigurationException("Error while setting up",e);
		} catch (SetupException e) {
			throw new ConfigurationException("Error while setting up",e);
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) throws Exception {
		String _xmlSetupFilePath=
			"/home/pat_dev/eclipse_home/workspace_3_1/TomcatPeerTrust/web/resource_management_files/resource_mng_config.xml";
		StringWrapper xmlSetupFilePath= new StringWrapper();
		xmlSetupFilePath.setWrappedString(_xmlSetupFilePath);
		xmlSetupFilePath.init();
		RequestServingMechanismPool pool=
					new RequestServingMechanismPool();
		pool.setup(xmlSetupFilePath);
		System.out.println("\n------------------------------default:\n"+
													pool.getMechanism("_default_"));
		System.out.println("\n------------------------------default:\n"+
													pool.getMechanism("/gdfdhdfg/demo/forward_to_service_jsp"));
	}

}
