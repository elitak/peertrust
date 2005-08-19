/**
 * 
 */
package org.peertrust.demo.resourcemanagement;

import java.io.IOException;

/**
 * @author pat_dev
 *
 */
public class OntologyBasedResourceClassifier implements ResourceClassifier{
	
	/**
	 * 
	 */
	public OntologyBasedResourceClassifier(String xmlSetupFilePath) {
		super();
	}

	///resource classifier interface implementation
	
	public Resource getResource(String resourceVirtualAbsURL) {
		
		return getTestResource(resourceVirtualAbsURL);
	}

	public void setup(String urlOfXmlConfigFile) throws IOException, UnsupportedFormatException {
		
	}	
	
	
	public Resource getTestResource(String url){
		if(url==null){
			return null;
		}
		//pdf/PeerTrust-ATN.pdf
		if(url.equals("/myapp-0.1-dev/pdf/trustVLDB04.pdf")){
			ProtectedResource res=
				new ProtectedResource("_contains_",url,url);
			res.setAction("_exact_");
			res.setPolicyName("acmMember");
			return res;
		}else if(url.equals("/myapp-0.1-dev/pdf/PeerTrust-ATN.pdf")){
			ProtectedResource res=
				new ProtectedResource("_exact_",url,url);
			res.setAction("_ieee_");
			res.setPolicyName("ieeeMember");
			return res;
		}else if(url.equals("/myapp-0.1-dev/jsp/appletPage.jsp")){
			return new PublicResource("",url,url);
		}else if(url.equals("/myapp-0.1-dev/jsp/service.jsp")){
			return new PublicResource("",url,url);
		}else if(url.endsWith(".jsp")){
			ProtectedResource res=
				new ProtectedResource(	"_endwith_",url,url);
			res.setAction("_monitor_");
			return res;
		}else if(url.indexOf("ieee")>=0){
			ProtectedResource res=
				new ProtectedResource("_contains_",url,url);
			res.setAction("_negotiate_");
			res.setPolicyName("ieeeMember");
			return res;
		}else if(url.indexOf("acm")>=0){
			ProtectedResource res=
				new ProtectedResource("_contains_",url,url);
			res.setAction("_negotiate_");
			res.setPolicyName("acmMember");
			return res;
		}else{
			return new PublicResource("",url,url);
		}
		
		
		
	}
}
