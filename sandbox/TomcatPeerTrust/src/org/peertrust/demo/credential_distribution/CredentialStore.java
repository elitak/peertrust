package org.peertrust.demo.credential_distribution;

import java.io.IOException;
import java.util.Hashtable;

import javax.xml.parsers.ParserConfigurationException;

import org.peertrust.demo.common.Utils;
import org.peertrust.net.Peer;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * CredentialStore represents a repository of credentials.
 * This repository is loaded from an xml file.
 * 
 * @author Patrice Congo (token77)
 */
public class CredentialStore {
	
	/** regular expression patern to find place holder for peer alias*/
	public static final String PEER_ALIAS_PLACE_HOLDER="\\$\\{_Requester_\\}";
	
	/**
	 * Represent a Credential
	 * @author Patrice Congo (token77)
	 */
	class Credential{
		
		/** the name of the credential*/
		private String name;
		
		/** the value of the credential*/
		private String value;
		
		/** A description of the credential*/
		private String description;
		
		/**
		 * Creates a new credential
		 * @param name -- the name of the  new credential
		 * @param value -- the value of the new credential
		 * @param description -- the description of the new credential
		 */
		public Credential(String name, String value,String description){
			this.name=name;
			this.value=value;
			this.description=description;			
		}
		
		/**
		 * @return Returns the description.
		 */
		public String getDescription() {
			return description;
		}
		/**
		 * @param description The description to set.
		 */
		public void setDescription(String description) {
			this.description = description;
		}
		/**
		 * @return Returns the name.
		 */
		public String getName() {
			return name;
		}
		/**
		 * @param name The name to set.
		 */
		public void setName(String name) {
			this.name = name;
		}
		/**
		 * @return Returns the value.
		 */
		public String getValue() {
			return value;
		}
		/**
		 * @param value The value to set.
		 */
		public void setValue(String value) {
			this.value = value;
		}
		
		public String toString(){
			return "credential["+
					"name:"+name+
					" value:"+value+
					" description:"+description+
					"]";
		}
	}
	
	/** root tag name of the credential store xml specification*/ 
	static final public String ROOT_ELEMENT="CredentialStore";
	
	/** xml tag of the element representing a credential*/
	static final public String CREDENTIAL_ELEMENT="Credential";
	
	/** 
	 * Attribute name of the attribute representing the
	 *  credential name
	 */
	static final public String ATTRIBUTE_NAME="name";
	
	/**
	 *  Attribute name of the attribute representing the
	 *  credential value
	 */
	static final public String ATTRIBUTE_VALUE="value";
	
	/** 
	 * Attribute name of the attribute representing the
	 *  credential description
	 */
	static final public String ATTRIBUTE_DESCRIPTION="description";
	
	/** 
	 * Name of the element containing the regular expression 
	 * to find alias place holder.
	 */
	static final public String PEER_ALIAS_PLACE_HOLDER_ELEMENT="peerAliasPlaceHolder";
	
	/**
	 * Holds credential name value pairs.
	 */
	private Hashtable credentials;
	
	/** Represents the regular expression to find alias place holder*/ 
	private String peerAliasPlaceHolder;
	
	/**
	 * Constructs a blank credential store.
	 * The credential store can be fill from an XML file usind
	 * CredentialStore.setup()
	 */
	public CredentialStore()
	{
		credentials= new Hashtable();
	}
	
	/**
	 * Sets up the credential store from an XML setup.
	 * @param xmlSetupFile -- the path of the xml setup file
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws NullPointerException 
	 */
	public void setup(	String xmlSetupFile) 
						throws 	NullPointerException, 
								SAXException, 
								IOException, 
								ParserConfigurationException
	{
		Element credElement=
			Utils.getRootElement(xmlSetupFile,ROOT_ELEMENT);
		///get peer alias place holder
		NodeList credNodeList=
			credElement.getElementsByTagName(PEER_ALIAS_PLACE_HOLDER_ELEMENT);
		if(credNodeList==null){
			throw new RuntimeException("<"+ROOT_ELEMENT+"> must contains a <"+
										PEER_ALIAS_PLACE_HOLDER_ELEMENT+"> child element");
		}
		Node n=credNodeList.item(0);
		peerAliasPlaceHolder=n.getTextContent();
		System.out.println("\nPEER_ALIAS_PLACE_HOLDER:"+peerAliasPlaceHolder);
		credNodeList=
			credElement.getElementsByTagName(CREDENTIAL_ELEMENT);
		System.out.println("credElement:"+credElement.getTagName());
		
		NamedNodeMap attrMap;
		Credential cred;
		for(int i=credNodeList.getLength()-1;i>=0;i--){
			n=credNodeList.item(i);
			attrMap=n.getAttributes();
			cred= new Credential(
					attrMap.getNamedItem(ATTRIBUTE_NAME).getNodeValue(),
					attrMap.getNamedItem(ATTRIBUTE_VALUE).getNodeValue(),
					attrMap.getNamedItem(ATTRIBUTE_DESCRIPTION).getNodeValue());
			credentials.put(cred.getName(),cred);
		}
	}
	
	
	/**
	 * To get the value of the named credential.
	 * @param name -- the name of the credential, which value is being retrieved.
	 * 
	 * @return the value of the credential.
	 */
	public String getCredentialValue(
							String name, 
							Peer requestingPeer)
	{
		
		if(name!=null){
			System.out.println("Credentials_store:"+credentials);
			Credential cred=(Credential)credentials.get(name);
			if(cred==null){
				return null;
			}
			String value=cred.getValue();
			if(value!=null){
				System.out.println("cred.replaceAll(\"Requester\","+requestingPeer.getAlias()+");");
				value=value.replaceAll(peerAliasPlaceHolder,requestingPeer.getAlias());
				System.out.println("cred:"+value);
			}
			return value;
		}else{
			return null;
		}
	}

	/**
	 * Lookup a credential description. 
	 * @param 	name -- the name of the credential 
	 * 			which description is to lookup
	 * 
	 * @return 	the description of the credential which name
	 * 			match the provided name.
	 */
	public String getCredentialDescription(String name)
	{		
		if(name!=null){
			Credential cred=(Credential)credentials.get(name);
			if(cred==null){
				return null;
			}
			return cred.getDescription();
		}else{
			return null;
		}
	}
	
	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
			return "CredentialStore:"+credentials;
	}
	
	
//	static public void main(String args[])throws Exception{
//		System.out.println("\n______________________test_credential_store__________");
//		String path=
//			"/home/pat_dev/eclipse_home/workspace_3_1/TomcatPeerTrust/web/resource_management_files/resource_mng_config.xml";
//		CredentialStore store=
//			new CredentialStore();
//		store.setup(path);
//		System.out.println(store);
//		//String getCredentialValue(String name, Peer requestingPeer)
//		String credName="cred_ieeeMember";
//		Peer requestingPeer= new Peer("patrice","addi_1",0);
//		String returnCred=store.getCredentialValue(credName,requestingPeer);
//		System.out.println(credName+":"+returnCred); 
//	}
}
