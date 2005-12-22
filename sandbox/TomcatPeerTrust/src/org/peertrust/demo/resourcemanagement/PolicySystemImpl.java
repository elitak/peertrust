/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.io.IOException;
import java.util.Collections;
import java.util.Hashtable;
import java.util.Vector;

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
 * A PolicySystem sytem is a pool of policies.
 * PolicySystemImpl provides a policy system which can be 
 * construct from an xml specifiation.
 *  
 * <policySystem> is an xml is the xml element for a  PolicySystem 
 * object. The policies in the pool are represented by the
 * <policy> element.
 *  
 * @author Patrice Congo (token77)
 */
public class PolicySystemImpl implements 	PolicySystem, 
											Configurable,
											ResourceRequestHandler
{
	/**
	 * The element name that represent the xml polycy system.
	 */
	final static public String ROOT_TAG_POLICY_SYSTEM="policySystem";
	
	/**
	 * the name of the <policSystem> attribute that hold its type
	 * type aims at version control 
	 */
	final static public String ATTRIBUT_TYPE="type";
	
	/**
	 * the element name representing a policy
	 */
	final static public String POLICY_TAG="policy";
	
	/**
	 * The name of the <policy> attribute containing the policy name
	 */
	final static public String ATTRIBUTE_POLICY_NAME="policyName";
	/**
	 * The name of the <polic> attribute containing thepolicy value
	 */
	final static public String ATTRIBUTE_POLICY_VALUE="policyValue";
	//final static public String ATTRIBUTE_INCLUDED_POLICY="includedPolicy"; 
	
	
	
	/**
	 *The cache which holds the policies 
	 */
	private Cache policyCache;
	
	/**
	 * The xml setup file name
	 */
	private StringWrapper setupFilePath;
	
	/**
	 * The next handler
	 */
	private ResourceRequestHandler nextHandler;
	
	/**
	 * Construct a virgin PolicySystem.
	 * To finisch the setup of the new object 2 steps are
	 * required:
	 * <ul>
	 * 	<li/>sets the setupFilePath with the appropriate setter
	 * 	<li/>call init to fill the pool using the set xml file 
	 * <ul>
	 */
	public PolicySystemImpl() {
		super();
	}

	/**
	 * @see org.peertrust.demo.resourcemanagement.PolicySystem#getPolicies(java.lang.String)
	 */
	public Vector getPolicies(String policyName) {
		
		Policy pol=(Policy)policyCache.get(policyName);
		Vector policies=new Vector();
		policies.add(pol);
		Collections.reverse(policies);//policies.
		return policies;
	}

	/**
	 * @see org.peertrust.demo.resourcemanagement.PolicySystem#setup(java.lang.String)
	 */
	public void setup(String xmlSetupFileName)
							throws 	UnsupportedFormatException, 
									ParserConfigurationException, 
									SAXException, 
									IOException 
	{
		if(xmlSetupFileName==null){
			new NullPointerException("Parameter urlOfXMLConfigFile");
		}
		
		try {
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document dom = builder.parse(xmlSetupFileName);
			NodeList policyNodeList=
				dom.getElementsByTagName(ROOT_TAG_POLICY_SYSTEM);
			Element polRootNode=null;
			if(policyNodeList.getLength()!=1){
				throw 
					new Error(	"Illegal xml config file. It must contain exactelly one "+
								"<"+ROOT_TAG_POLICY_SYSTEM+"> but contains "+
								policyNodeList.getLength()+
								"xmlSetupFileName="+xmlSetupFileName);
			}else{
				polRootNode=(Element)policyNodeList.item(0);
				System.out.println("owner dom:"+polRootNode);
			}
			
			//String type= dom.getFirstChild().getAttributes().getNamedItem("type").getNodeValue();
			String type= polRootNode.getAttributes().getNamedItem("type").getNodeValue();
			System.out.println("type:"+type);
			policyNodeList=	polRootNode.getElementsByTagName(POLICY_TAG);
					//dom.getElementsByTagName(POLICY_TAG);
					
			Policy pol;
			Hashtable polTable= new Hashtable();
			for(int i=policyNodeList.getLength()-1;i>=0;i--){
				pol=getPolicyFromXMLNode((Node)policyNodeList.item(i));
				System.out.println("\n i:"+i+" "+pol);
				polTable.put(pol.getPolicyName(),pol);
			}
			
			policyCache= new Cache(new TestElementCreator(polTable));
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
			throw e;
		} catch (SAXException e) {
			e.printStackTrace();
			throw e;
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		}

	}
	/**
	 * Constructs a plicy from the provided xml node.
	 * The node has the following format:
	 * <code>
	 * 		<policy
	 *			policyName="ieeeMember"
	 *			policyValue="ieeeMemberPolicy(Requester)"
	 *			includedPolicy="" 
	 *		/>
	 *	</code>
	 */
	static private Policy getPolicyFromXMLNode(Node polNode){
		NamedNodeMap nodeMap= polNode.getAttributes();
		String policyName=
			nodeMap.getNamedItem(ATTRIBUTE_POLICY_NAME).getNodeValue();
		String policyValue=
			nodeMap.getNamedItem(ATTRIBUTE_POLICY_VALUE).getNodeValue();
		Policy pol=new Policy(policyName,policyValue);
		return pol;
	}


	class TestElementCreator implements CacheElementCreator{
		Hashtable policyTable= new Hashtable();
		TestElementCreator(){
			Policy pol= new Policy("acmMember", "acmMember(alice)@ acm @ alice");
			policyTable.put(pol.getPolicyName(),pol);
			
			pol= new Policy("ieeeMember", "ieeeMember(alice)@ ieee @ alice");
			policyTable.put(pol.getPolicyName(),pol);
			
		}
		
		TestElementCreator(Hashtable policyTable){
			this.policyTable=policyTable;			
		}
		/* (non-Javadoc)
		 * @see org.peertrust.demo.resourcemanagement.CacheElementCreator#createCacheElement(java.lang.Object)
		 */
		public Object createCacheElement(Object key) {
			if(key==null){
				return null;
			}
			return policyTable.get(key);
		}
		
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
		System.out.println("*************init PolicySystemImpl");
		if(setupFilePath==null){
			throw new ConfigurationException("setupFilePath must not be null");
		}
		
		try {
			setup(setupFilePath.getWrappedString());
		} catch (Exception e) {
			throw new ConfigurationException("PolicySystemImpl Setup Fail",e);
		} 
	}

	
	
	
	
	/**
	 * @see org.peertrust.demo.resourcemanagement.ResourceRequestHandler#handle(java.lang.Object)
	 */
	public void handle(Object requestSpec) throws ResourceManagementException {
		if(requestSpec == null){
			throw new ResourceManagementException("Cannot handle null requestSpec");
		}
		
		if(!(requestSpec instanceof ResourceRequestSpec)){
			throw new ResourceManagementException(
						"Cannot handle "+requestSpec+
						" can only handle a "+ResourceRequestSpec.class);
		}
		
		ResourceRequestSpec spec=(ResourceRequestSpec)requestSpec;
		String peerName=spec.getPeerName();
		if(peerName!=null){//a peer registered nego possible
			Resource res=spec.getResource();
			if(res instanceof ProtectedResource){
				String policyName=((ProtectedResource)res).getPolicyName();
				if(policyName==null){
					throw 
						new ResourceManagementException(
								"Policy name associated with "+res.getUrl()+
								" is null");
				}
				if(policyName.trim().length()==0) {
					throw 
						new ResourceManagementException(
							"Policy name associated with "+res.getUrl()+
							" is empty");
				}
				
				Vector associatePolicies=
					this.getPolicies(policyName);
				if(associatePolicies.size()==0){
					throw 
					new ResourceManagementException(
							"Policy with the name "+policyName+ "is empty");
				}
				spec.setAttachedPolicies(associatePolicies);
			}
		}
		
		
		if(nextHandler!=null){
			nextHandler.handle(spec);
		}else{
			throw new ResourceManagementException(
					"PolicySystem cannot act as the end of the chain");
		}
	}

	/**
	 * @see org.peertrust.demo.resourcemanagement.ResourceRequestHandler#setNextHandle(org.peertrust.demo.resourcemanagement.ResourceRequestHandler)
	 */
	public void setNextHandle(ResourceRequestHandler nextHandler) {
		this.nextHandler=nextHandler;
	}

	static public void main(String[] args)throws Throwable{
		final String setupFile=
			//"G:\\eclipse_software\\TomcatPeerTrust\\web\\resource_management_files\\resource_mng_config.xml";
			"/home/pat_dev/eclipse_home/workspace_3_1/TomcatPeerTrust/web/resource_management_files/resource_mng_config.xml";
		PolicySystemImpl polSystem=
				new PolicySystemImpl();
		polSystem.setup(setupFile);
		System.out.println("ieee:"+polSystem.getPolicies("ieeeMemberPolicy").elementAt(0));
	}
}
