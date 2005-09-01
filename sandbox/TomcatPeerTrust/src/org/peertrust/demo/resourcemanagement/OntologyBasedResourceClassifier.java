/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.io.IOException;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * @author pat_dev
 *
 */
public class OntologyBasedResourceClassifier implements ResourceClassifier{
	
	public static final String RESOURCE_TAG_NAME="resourceClass";
	public static final String ATTRIBUTE_IS_PROTECTED="isProtected"; 
	public static final String ATTRIBUTE_URL="url";
	public static final String ATTRIBUTE_MATCHING_STRATEGY="matchingStrategy";
	public static final String ATTRIBUTE_POLICY_NAME="policyName";
	public static final String ATTRIBUTE_REQUEST_SERVING_MECHANISM="requestServingMechanism";
	
	final private Comparator exactMatchingComparator=
							makeExactMatchingComparator();
	final private Comparator regExprMatchingComparator=
							makeRegExprMatchingComparator();
		
	
	private Vector resources= new Vector();
	/**
	 * 
	 */
	public OntologyBasedResourceClassifier() {
		super();
	}

	private Comparator makeExactMatchingComparator(){
		return new Comparator(){

			public int compare(Object obj0, Object obj1) {
				if(obj0==null || obj1==null){
					if(obj0==obj1){
						return 0;
					}else if(obj0==null){
						return 1;
					}else{
						return -1;
					}
				}else{
					Resource res=null;
					String url=null;
					if(obj0 instanceof Resource){
						res=(Resource)obj0;
						url=(String)obj1;
					}else if(obj1 instanceof Resource){
						res=(Resource)obj1;
						url=(String)obj0;
					}else{
						return -1;//not equals
					}
					return res.getUrl().compareTo(url);
				}
				
			}
			
		};
	}
	
	private Comparator makeRegExprMatchingComparator(){
		return new Comparator(){

			
			public int compare(Object obj0, Object obj1) {
				if(obj0==null || obj1==null){
					if(obj0==obj1){
						return 0;
					}else if(obj0==null){
						return 1;
					}else{
						return -1;
					}
				}else{
					Resource res=null;
					String url=null;
					if(obj0 instanceof Resource){
						res=(Resource)obj0;
						url=(String)obj1;
					}else if(obj1 instanceof Resource){
						res=(Resource)obj1;
						url=(String)obj0;
					}else{
						return -1;//not equals
					}
					if(Pattern.matches(res.getUrl(),url)){
						return 0;
					}else{
						return 1;
					}
				}
				
			}
			
		};
	}
	
	///resource classifier interface implementation
	
	public Resource getResource(String resourceVirtualAbsURL) {
		
		return getResourceFromCollection(resourceVirtualAbsURL);
	}

	public void setup(String urlOfXmlConfigFile) throws IOException, UnsupportedFormatException, ParserConfigurationException, SAXException {
		if(urlOfXmlConfigFile==null){
			new NullPointerException("Parameter urlOfXMLConfigFile");
		}
		
		try {
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document dom = builder.parse(urlOfXmlConfigFile);
			
			String type= dom.getFirstChild().getAttributes().getNamedItem("type").getNodeValue();
			System.out.println("type:"+type);
			NodeList resClassNodeList=
					dom.getElementsByTagName(RESOURCE_TAG_NAME);
			Resource res;
			for(int i=resClassNodeList.getLength()-1;i>=0;i--){
				res=getResourceFromXMLNode((Node)resClassNodeList.item(i));
				System.out.println("\n i:"+i+" "+res);
				resources.add(res);
			}
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
	<resourceClass
	isProtected="false" 
	url="/myapp-0.1-dev/jsp/service.jsp"
	matchingStrategy="_exact_"
	policyName="acmMember"
		/>
		 
	*/
	static private Resource getResourceFromXMLNode(Node resourceClassNode){
		System.out.println("resourceClassNode:"+resourceClassNode);
		NamedNodeMap nodeMap=resourceClassNode.getAttributes();
		System.out.println("nodeMap:"+nodeMap.getLength());
		boolean isProtected= 
			Boolean.parseBoolean(nodeMap.getNamedItem(ATTRIBUTE_IS_PROTECTED).getNodeValue());
		String url=nodeMap.getNamedItem(ATTRIBUTE_URL).getNodeValue();
		String matchingStrategy=
			nodeMap.getNamedItem(ATTRIBUTE_MATCHING_STRATEGY).getNodeValue();
		Node n=nodeMap.getNamedItem(ATTRIBUTE_REQUEST_SERVING_MECHANISM);
		String requestServingMechanism=
								(n!=null)?n.getNodeValue():null;
		
		if(isProtected){
			String policyName= 
				nodeMap.getNamedItem(ATTRIBUTE_POLICY_NAME).getNodeValue();
			ProtectedResource res= new ProtectedResource(matchingStrategy,url);
			res.setPolicyName(policyName);
			if(requestServingMechanism!=null){
				res.setRequestServingMechanimName(requestServingMechanism);
			}
			return res;
		}else{
			PublicResource res= new PublicResource(matchingStrategy,url);
			if(requestServingMechanism!=null){
				res.setRequestServingMechanimName(requestServingMechanism);
			}
			return res;
		}
	}
	
	public Resource getResourceFromCollection(String url){
		if(url==null){
			return null;
		}else{
			int i=Collections.binarySearch(resources,url,exactMatchingComparator);
			if(i<0){
				i=Collections.binarySearch(resources,url,regExprMatchingComparator);
			}
			if(i<0){
				//nothning found
				return null;
			}else{
				Object obj= resources.elementAt(i);
				if(obj instanceof PublicResource){
					return ((PublicResource)obj).getCopy(url);
				}else{
					return ((ProtectedResource)obj).getCopy(url);
				}
			}
			
		}
		
	}
	
	public Resource gggetTestResource(String url){
		if(url==null){
			return null;
		}
		//pdf/PeerTrust-ATN.pdf
		if(url.equals("/myapp-0.1-dev/pdf/trustVLDB04.pdf")){
			ProtectedResource res=
				new ProtectedResource("_contains_",url);
			res.setPolicyName("acmMember");
			return res;
		}else if(url.equals("/myapp-0.1-dev/pdf/PeerTrust-ATN.pdf")){
			ProtectedResource res=
				new ProtectedResource("_exact_",url);
			res.setPolicyName("ieeeMember");
			return res;
		}else if(url.equals("/myapp-0.1-dev/jsp/appletPage.jsp")){
			return new PublicResource("",url);
		}else if(url.equals("/myapp-0.1-dev/jsp/service.jsp")){
			return new PublicResource("",url);
		}else if(url.endsWith(".jsp")){
			ProtectedResource res=
				new ProtectedResource(	"_endwith_",url);
			return res;
		}else if(url.indexOf("ieee")>=0){
			ProtectedResource res=
				new ProtectedResource("_contains_",url);
			res.setPolicyName("ieeeMember");
			return res;
		}else if(url.indexOf("acm")>=0){
			ProtectedResource res=
				new ProtectedResource("_contains_",url);
			res.setPolicyName("acmMember");
			return res;
		}else{
			return new PublicResource("",url);
		}
		
		
		
	}
	
	static public void main(String[] agrs)throws Exception{
		String setupFile=
			//"G:\\eclipse_software\\TomcatPeerTrust\\web\\resource_management_files\\resource_classification.xml";
			"/home/pat_dev/eclipse_home/workspace_3_1/TomcatPeerTrust/web/resource_management_files/resource_classification.xml";
		OntologyBasedResourceClassifier classifier=
							new OntologyBasedResourceClassifier();
		classifier.setup(setupFile);
		System.out.println("url:/myapp-0.1-dev/pdf/trustVLDB04.pdf:\n "+ 
							classifier.getResource("/myapp-0.1-dev/pdf/trustVLDB04.pdf"));
		
		System.out.println("dada.jsp:\n "+ 
				classifier.getResource("dada.jsp"));
		System.out.println("dadajsp:\n "+ 
				classifier.getResource("dadajsp"));
		System.out.println(Boolean.parseBoolean("falsedd"));

	}
}
