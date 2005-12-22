package org.peertrust.demo.resourcemanagement;

import java.io.IOException;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Vector;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;
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
 * Privodes an Implementation of a ResourceClassifier.
 * The actual classification is specified in an 
 * xml file.
 * 
 * @author Patrice Congo(token77)
 *
 */
public class ResourceClassifierImpl implements 	ResourceClassifier,
												Configurable,
												ResourceRequestHandler
{
	
	public static final String RESOURCE_TAG_NAME="resourceClass";
	public static final String ATTRIBUTE_URL="url";
	public static final String ATTRIBUTE_MATCHING_STRATEGY="matchingStrategy";
	public static final String ATTRIBUTE_POLICY_NAME="policyName";
	public static final String ATTRIBUTE_REQUEST_SERVING_MECHANISM="requestServingMechanism";
	public static final String ROOT_TAG_NAME="ResourceClassifier";
	public static final String ATTRIBUTE_CREDENTIALS="credentials";
	
	/**
	 * 
	 */
	final private Comparator exactMatchingComparator=
							makeExactMatchingComparator();
	/**
	 * A comparator which uses regular expression for comparison
	 */
	final private Comparator regExprMatchingComparator=
							makeRegExprMatchingComparator();
		
	/**
	 *Contains the resource models or templates which are cloned and customized 
	 *for the actual resource.  
	 */
	private Vector resources= new Vector();
	
	/**
	 * the pathof the xml setup file which contains the classification
	 * spefication.
	 */
	private StringWrapper setupFilePath;
	
	private ResourceRequestHandler nextHandler;
	
	/**
	 * Constructs a virgin ResourceClassificationImpl object.
	 * For this object to become valide and usable a further 2 step
	 * setup is necessary:
	 * <ul>
	 * <li>set setupFilePath with the appropriate setter,
	 * <li>call init the finalze the setup
	 * </ul>
	 * This is e.g. done automatically when ResourceClassifierimpl is
	 * created from a rdf config file.
	 */
	public ResourceClassifierImpl() {
		super();
	}

	/**
	 * Implements a linear seach algotrithm which return the index
	 * of a resource matching an url according to the provided 
	 * comparator.
	 * 
	 * @param 	resources -- a vector of resource templates.
	 * @param 	url -- the url to match.
	 * @param 	exactMatchingComparator -- the Comparator use for the 
	 * 			comparaison.
	 * @return	return the index of the template resource matching the url
	 */
	public static int linearSearch(
									Vector resources,
									String url,
									Comparator exactMatchingComparator){
		
		
		final int LEN=resources.size();
		for(int i=0;i<LEN;i++){
			if(exactMatchingComparator.compare(resources.elementAt(i),url)==0){
				return i;
			}
		}
		return -1;
	}
	
	/**
	 * Construsts a comparator which performs an exact matche between 
	 * the url of a resource template with a real url
	 * 
	 * @return the created comparator
	 * @see java.util.Comparator
	 */
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
					String resURL=res.getUrl();
					//System.out.println("\n*********Comparing: "+resURL+"\t"+url+"\n");
					return resURL.compareTo(url);
				}
				
			}
			
		};
	}
	
	/**
	 * Construsts a comparator which performs an regular expresion based
	 * matche between the url of a resource template with a real url
	 * 
	 * @return the created comparator
	 * @see java.util.Comparator
	 */
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
					String resURL=res.getUrl();
					System.out.println("\n*********Matching: "+resURL+"\t"+url+"\n");
					if(Pattern.matches(resURL,url)){
						return 0;
					}else{
						return 1;
					}
				}
				
			}
			
		};
	}
	
	/**
	 * To get the resource corresponding to the a provided url.
	 * The work is delegated to getResourceFromCollection.
	 * @param reourceURL -- the url of the resource
	 * @return 	the resource corresponding to the provided url
	 */
	public Resource getResource(String resourceURL) {
		
		return getResourceFromCollection(resourceURL);
	}


	/**
	 * Parses the xml classificatio specification file and builds the
	 * object internal state.
	 * 
	 * @param urlOfXmlConfigFile -- the path of the axm setup file.
	 * @throws IOException
	 * @throws UnsupportedFormatException
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @see ResourceClassifier#setup(String)
	 */
	public void setup(StringWrapper urlOfXmlConfigFile) throws IOException, UnsupportedFormatException, ParserConfigurationException, SAXException {
		if(urlOfXmlConfigFile==null){
			new NullPointerException("Parameter urlOfXMLConfigFile");
		}
		
		try {
			DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			Document dom = builder.parse(urlOfXmlConfigFile.getWrappedString());
			NodeList rootNodeList=
				dom.getElementsByTagName(ROOT_TAG_NAME);
			Element resRootNode=null;
			if(rootNodeList.getLength()!=1){//
				throw new Error(	"Illegal xml config file. It must contain exactelly one "+
						"<"+ROOT_TAG_NAME+"> but contains "+rootNodeList.getLength());
			}else{
				resRootNode=(Element)rootNodeList.item(0);
			}
			String type= resRootNode.getAttributes().getNamedItem("type").getNodeValue();
			System.out.println("type:"+type);
			NodeList resClassNodeList=
					resRootNode.getElementsByTagName(RESOURCE_TAG_NAME);
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
	
	/**
	 * Build the reource object from the appropriate xml node.
	 * @param resourceClassNode -- the node representing the resource
	 * @param return the constructed resource object
	 */
	static private Resource getResourceFromXMLNode(Node resourceClassNode){
		System.out.println("resourceClassNode:"+resourceClassNode);
		NamedNodeMap nodeMap=resourceClassNode.getAttributes();
		System.out.println("nodeMap:"+nodeMap.getLength());
		//boolean isProtected= 
		//	Boolean.parseBoolean(nodeMap.getNamedItem(ATTRIBUTE_IS_PROTECTED).getNodeValue());
		String url=nodeMap.getNamedItem(ATTRIBUTE_URL).getNodeValue();
		String matchingStrategy=
			nodeMap.getNamedItem(ATTRIBUTE_MATCHING_STRATEGY).getNodeValue();
		Node n=nodeMap.getNamedItem(ATTRIBUTE_REQUEST_SERVING_MECHANISM);
		String requestServingMechanism=
								(n!=null)?n.getNodeValue():null;
		
		n=nodeMap.getNamedItem(ATTRIBUTE_POLICY_NAME);
		String policyName= 
				(n!=null)?n.getNodeValue():null;
				
		n=nodeMap.getNamedItem(ATTRIBUTE_CREDENTIALS);
		String credentials= 
				(n!=null)?n.getNodeValue():null;
								
		if(policyName!=null){
			ProtectedResource res= new ProtectedResource(matchingStrategy,url);
			res.setPolicyName(policyName);
			if(requestServingMechanism!=null){
				res.setRequestServingMechanimName(requestServingMechanism);
			}
			
			if(credentials!=null){ 
				String[] credsArray=credentials.split(",");
				for(int i=0;i<credsArray.length;i++){
					res.addCredential(credsArray[i]);
					System.out.println("res.addCredential(credsArray[i]):"+credsArray[i]);
				}
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
	
	/**
	 * Gets the resource template from the resource template vector
	 * and uses it to build the url specific resource object.
	 * First an exact match is try then a match based on regular
	 * expression 
	 * @param url -- the url for which a resource object is build
	 * 
	 * @return the resource object for the provided url
	 */
	private Resource getResourceFromCollection(String url){
		if(url==null){
			return null;
		}else{
			int i=-1;linearSearch(resources,url,exactMatchingComparator);
			if(i<0){
				i=linearSearch(resources,url,regExprMatchingComparator);
			}
			if(i<0){
				//nothning found
				return new PublicResource("_X_",url);
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
			throw new Error("setupFilePath must not be null");
		}
		try{
			setup(setupFilePath);
		}catch(Throwable th){
			throw new ConfigurationException("Recource Classifier setup fail",th);
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
		String url=
			((HttpServletRequest)spec.getServletRequest()).getRequestURI();
		
			
//		filterContext.log(
//			"\n************************FILTERING log***********************************************"+
//			"\nurl:"+url+
//			"\n************************FILTERING log END***********************************************\n");
			
		spec.setResource(getResource(url));
		
		if(nextHandler!=null){
			nextHandler.handle(spec);
		}else{
			throw new ResourceManagementException(
					"ResourceClassifier cannot act as the end of the chain");
		}
	}

	/**
	 * @see org.peertrust.demo.resourcemanagement.ResourceRequestHandler#setNextHandle(org.peertrust.demo.resourcemanagement.ResourceRequestHandler)
	 */
	public void setNextHandle(ResourceRequestHandler nextHandler) {
		this.nextHandler=nextHandler;
	}

	static public void main(String[] agrs)throws Exception{
//		String setupFile=
//			//"G:\\eclipse_software\\TomcatPeerTrust\\web\\resource_management_files\\resource_classification.xml";
//			"/home/pat_dev/eclipse_home/workspace_3_1/TomcatPeerTrust/web/resource_management_files/resource_classification.xml";
//		ResourceClassifierImpl classifier=
//							new ResourceClassifierImpl();
//		classifier.setup(setupFile);
//		System.out.println("url:/myapp-0.1-dev/pdf/trustVLDB04.pdf:\n "+ 
//							classifier.getResource("/myapp-0.1-dev/pdf/trustVLDB04.pdf"));
//		
//		System.out.println("dada.jsp:\n "+ 
//				classifier.getResource("dada.jsp"));
//		System.out.println("dadajsp:\n "+ 
//				classifier.getResource("dadajsp"));
//		System.out.println(Boolean.parseBoolean("falsedd"));
		System.out.println("Spited:"+Arrays.asList("dada".split(",")));

	}
}
