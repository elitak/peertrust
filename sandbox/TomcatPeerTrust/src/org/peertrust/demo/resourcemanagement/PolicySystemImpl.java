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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;



/**
 * @author pat_dev
 *
 */
public class PolicySystemImpl implements PolicySystem {
	final static public String ROOT_TAG_POLICY_SYSTEM="policySystem";
	final static public String ATTRIBUT_TYPE="type";
	final static public String POLICY_TAG="policy";
	final static public String ATTRIBUTE_POLICY_NAME="policyName";
	final static public String ATTRIBUTE_POLICY_VALUE="policyValue";
	final static public String ATTRIBUTE_INCLUDED_POLICY="includedPolicy"; 
	
	private Cache policyCache;
	
	
	/**
	 * 
	 */
	public PolicySystemImpl() {
		super();
		//policyCache= new Cache(new TestElementCreator());
	}

	/* (non-Javadoc)
	 * @see org.peertrust.demo.resourcemanagement.PolicySystem#getPolicies(java.lang.String)
	 */
	public Vector getPolicies(String policyName) {
		
		Policy pol=(Policy)policyCache.get(policyName);
		Vector policies=new Vector();
		String includedPol;
		while(pol!=null){
			policies.add(pol);
			includedPol=pol.getIncludedPolicy();
			if(includedPol==null){
				pol=null;
			}else{
				pol=(Policy)policyCache.get(includedPol);
			}
		}
		Collections.reverse(policies);//policies.
		return policies;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.demo.resourcemanagement.PolicySystem#setup(java.lang.String)
	 */
	public void setup(String xmlSetupFileName)
			throws UnsupportedFormatException, ParserConfigurationException, SAXException, IOException {
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
								"<"+ROOT_TAG_POLICY_SYSTEM+"> but contains "+policyNodeList.getLength());
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
	/*
	 * <policy
		policyName="ieeeMember"
		policyValue="ieeeMemberPolicy(Requester)"
		includedPolicy="" 
		/>
	 */
	static private Policy getPolicyFromXMLNode(Node polNode){
		NamedNodeMap nodeMap= polNode.getAttributes();
		String policyName=
			nodeMap.getNamedItem(ATTRIBUTE_POLICY_NAME).getNodeValue();
		String policyValue=
			nodeMap.getNamedItem(ATTRIBUTE_POLICY_VALUE).getNodeValue();
		String includedPolicy=
			nodeMap.getNamedItem(ATTRIBUTE_INCLUDED_POLICY).getNodeValue();
		Policy pol=new Policy(policyName,policyValue);
		includedPolicy=includedPolicy.trim();
		if(includedPolicy.length()!=0){
			pol.setIncludedPolicy(includedPolicy);
		}
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
