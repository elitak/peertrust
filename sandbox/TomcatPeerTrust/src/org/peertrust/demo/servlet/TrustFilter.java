/**
 * 
 */
package org.peertrust.demo.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.peertrust.demo.resourcemanagement.IllegalAccessPolicyAssociation;
import org.peertrust.demo.resourcemanagement.ProtectedResource;

import org.peertrust.demo.resourcemanagement.RequestServingMechanism;
import org.peertrust.demo.resourcemanagement.Resource;
import org.peertrust.demo.resourcemanagement.ResourceManagementException;
import org.peertrust.demo.resourcemanagement.ResourceRequestSpec;
import org.peertrust.demo.resourcemanagement.TrustManager;
import org.peertrust.net.Peer;

/**
 * TrustFilter does filter the http requests and provides
 * mechanism to serve to request according to its trust level.
 *  
 * @author Patrice Congo
 *
 */
public class TrustFilter implements Filter{
	
	private NegotiationObjectRepository negoObjects;
	/**
	 * The trust manager used to serve the request
	 */
	private TrustManager trustManager;
	
	/**
	 * The filter config
	 */
	private FilterConfig filterConfig;
	
	/**
	 * The trust filter servlet context.
	 */
	ServletContext filterContext;
	
	/**
	 * @see javax.servlet.Filter#destroy()
	 */
	public void destroy() 
	{
		return;
	}
	
	/**
	 * Called to filter the request.
	 * This method classifies the request resource; and serves
	 * it using the trustManager. An additional session registration
	 * check is done in case of a protected resource. If no 
	 * peer registration is available for the current session a registration
	 * page is send back.
	 * 
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	public void doFilter(
			ServletRequest req, 
			ServletResponse resp,
			FilterChain chain) 
			throws IOException, ServletException 
	{
		doInit();
		///session and peer name
		HttpSession session=
			((HttpServletRequest)req).getSession(true);
		if(session.isNew()){
			session.setMaxInactiveInterval(60*30);//30min
		}
		
		//Peer peer=(Peer)session.getAttribute(PeerTrustCommunicationServlet.PEER_NAME_KEY);
		Peer peer=null;
		if(session!=null){
			peer=negoObjects.getSessionPeer(session.getId());
		}
		filterContext.log(
				"\n****************serving resource log ***********"+
				"\nfiltering protected  page session:"+session+
				"\nRegistered peer:"+peer);
		
		String peerName= null;
		if(peer!=null){
			peerName=peer.getAlias();
		}
		///spec
		ResourceRequestSpec spec=
			new ResourceRequestSpec(
						peerName,
						req,
						resp,
						chain,
						filterConfig.getServletContext());
		
		////serving chain
		try {
			trustManager.handle(spec);
		} catch (ResourceManagementException e) {
			throw new ServletException(
					"Error while handling resource request");
		}
	}
	
	
	public void doFilter_old(
					ServletRequest req, 
					ServletResponse resp,
					FilterChain chain) 
					throws IOException, ServletException 
	{
			
		doInit();
		String url=((HttpServletRequest)req).getRequestURI();
		try{
			
			filterContext.log(
				"\n************************FILTERING log***********************************************"+
				"\nurl:"+url+
				"\n************************FILTERING log END***********************************************\n");
			
			Resource res= trustManager.classifyResource(url);
			if(res instanceof ProtectedResource){
				HttpSession session=
					((HttpServletRequest)req).getSession(true);
				if(session.isNew()){
					session.setMaxInactiveInterval(60*30);//30min
				}
				
				//Peer peer=(Peer)session.getAttribute(PeerTrustCommunicationServlet.PEER_NAME_KEY);
				Peer peer=null;
				if(session!=null){
					peer=negoObjects.getSessionPeer(session.getId());
				}
				filterContext.log(
						"****************serving protected resource log ***********"+
						"\nfiltering protected  page session:"+session+
						"\nRegistered peer:"+peer);
				
				String peerName= null;
				if(peer!=null){
					peerName=peer.getAlias();
				}
				
				if(peerName==null){ 
					//start peertrust and register session
					RequestServingMechanism servingMechanism=
						trustManager.getRequestServingMechanismByName("sessionRegistration");
					servingMechanism.serveRequest(	(HttpServletRequest)req,
													(HttpServletResponse)resp,
													chain,
													res,
													filterConfig.getServletContext());//regJsp);
				}else{
					
					res=trustManager.guardResource(res,peerName);
					if(((ProtectedResource)res).getCanAccess()){			
						filterContext.log(
								"\n==================ACCCESSING PROTECTED RESOURCE==================="+
								"trustManager:"+trustManager+ "\t res:"+res+
								"\n==================================================================");
						
						RequestServingMechanism servingMechanism=
							trustManager.getRequestServingMechanismByURL(res.getUrl());//getRequestServingMechanimName());
						servingMechanism.serveRequest(	(HttpServletRequest)req,
														(HttpServletResponse)resp,
														chain,
														res,
														this.filterConfig.getServletContext());
						return;
					}else{
						RequestServingMechanism servingMechanism=
							trustManager.getRequestServingMechanismByName("credentialDownload");
						servingMechanism.serveRequest(	(HttpServletRequest)req,
														(HttpServletResponse)resp,
														chain,
														res,
														filterConfig.getServletContext());
						return;
					}
				}
			}else{
				filterContext.log(
						"\n=======================ACCESSING PUBLIC RESOURCE=================="+
						"trustManager:"+trustManager+ "\t res:"+res+
						"\n==================================================================");
				RequestServingMechanism servingMechanism=
						trustManager.getRequestServingMechanismByURL(res.getUrl());//res.getRequestServingMechanimName());
				servingMechanism.serveRequest(	(HttpServletRequest)req,
													(HttpServletResponse)resp,
													chain,
													res,
													filterConfig.getServletContext());
					return;
				
			}
			
		}catch(ClassCastException castEx){
			filterContext.log(	"Error while sering:"+url,
								castEx);
		} catch (IllegalAccessPolicyAssociation e) {
			filterContext.log(	"Error while sering:"+url,
					e);
		}
		//chain.doFilter(req,resp);
		return;
	}
	/**
	 * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
	 */
	public void init(FilterConfig filterConfig) throws ServletException 
	{
		this.filterConfig=filterConfig;
		doInit();
		return;
	}

	/**
	 * Does the actual init task. 
	 * This task consist of getting the trust manager.
	 */
	private void doInit()
	{
		if(filterConfig==null){
			throw  new Error("filterConfig is null!");
		}
		
		if(negoObjects!=null){
			return;
		}
		
		
		filterContext=filterConfig.getServletContext();
		String negotiationObjectsContext= 
			(String)filterContext.getInitParameter("NegotiationObjectsContext");
		
		filterContext.log(
				"\n>>>>>>>>>>>>>>>>>>FilterName:"+filterConfig.getFilterName()+
				"\n>>>>>>>>>>>>>>>>>>NegotiationObjectsContext:"+negotiationObjectsContext+
				"\n>>>>>>>>>>>>>>>>>>Original Context:"+filterContext.getServletContextName()+
				"\n>>>>>>>>>>>>>>>>>>1FilterName:"+filterConfig.getFilterName());
		//get demo context	

		ServletContext demoContext=
			filterContext.getContext(negotiationObjectsContext);
		
		negoObjects= 
			NegotiationObjects.createAndAddForAppContext(demoContext);
		trustManager=(negoObjects!=null)?negoObjects.getTrustManager():null;
	}
	
	
}
