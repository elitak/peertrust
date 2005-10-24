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



public class CredentialStore {
	static final public String ROOT_ELEMENT="CredentialStore";
	static final public String CREDENTIAL_ELEMENT="Credential";
	static final public String ATTRIBUTE_NAME="name";
	static final public String ATTRIBUTE_VALUE="value";
	
	/**
	 * holds credential name value pairs.
	 */
	private Hashtable credentials;
	
	/**
	 * Construct a blank credebtial store.
	 * the credentiakl can be fill from an XML file usind
	 * CredentialStore.setup()
	 */
	public CredentialStore(){
		credentials= new Hashtable();
	}
	
	/**
	 * setup the credential store from an XML setup.
	 * @param xmlSetupFile -- the path of the xml setup file
	 * @throws ParserConfigurationException 
	 * @throws IOException 
	 * @throws SAXException 
	 * @throws NullPointerException 
	 */
	public void setup(String xmlSetupFile) throws NullPointerException, SAXException, IOException, ParserConfigurationException{
		Element credElement=
			Utils.getRootElement(xmlSetupFile,ROOT_ELEMENT);
		NodeList credNodeList=
			credElement.getElementsByTagName(CREDENTIAL_ELEMENT);
		System.out.println("credElement:"+credElement.getTagName());
		Node n;
		NamedNodeMap attrMap;
		for(int i=credNodeList.getLength()-1;i>=0;i--){
			n=credNodeList.item(i);
			attrMap=n.getAttributes();
			credentials.put(
					attrMap.getNamedItem(ATTRIBUTE_NAME).getNodeValue(),
					attrMap.getNamedItem(ATTRIBUTE_VALUE).getNodeValue());
			
		}
	}
	
	/**
	 * To get the value of the named credential.
	 * @param name -- the name of the credential, which value is being retrieved.
	 * 
	 * @return the value of the credential.
	 */
	public String getCredential(String name, Peer requestingPeer){
		if(name!=null){
			String cred=(String)credentials.get(name);
			if(cred!=null){
				System.out.println("cred.replaceAll(\"Requester\","+requestingPeer.getAlias()+");");
				cred=cred.replaceAll("Requester",requestingPeer.getAlias());
				System.out.println("cred:"+cred);
			}
			return cred;
		}else{
			return null;
		}
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
			return "CredentialStore:"+credentials;
	}
	
	
	static public void main(String args[])throws Exception{
		String path=
			"/home/pat_dev/eclipse_home/workspace_3_1/TomcatPeerTrust/web/resource_management_files/resource_mng_config.xml";
		CredentialStore store=
			new CredentialStore();
		store.setup(path);
		System.out.println(store);
	}
}
