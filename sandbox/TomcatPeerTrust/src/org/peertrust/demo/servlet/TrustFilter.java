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
		try{
			String url=((HttpServletRequest)req).getRequestURI();
			System.out.println("\n************************FILTERING***********************************************");
			System.out.println("\nurl:"+url);
			System.out.println("\n************************FILTERING END***********************************************\n");
			
			Resource res= trustManager.classifyResource(url);
			if(res instanceof ProtectedResource){
				System.out.println("filtering protected  page");
				HttpSession session=
					((HttpServletRequest)req).getSession(true);
				if(session.isNew()){
					session.setMaxInactiveInterval(60*30);//30min
				}
				System.out.println("filtering protected  page session:"+session);
				//Peer peer=(Peer)session.getAttribute(PeerTrustCommunicationServlet.PEER_NAME_KEY);
				Peer peer=null;
				if(session!=null){
					peer=negoObjects.getSessionPeer(session.getId());
				}
				String peerName= null;
				if(peer!=null){
					peerName=peer.getAlias();
				}
				if(peerName==null){ 
					//start peertrust and register session
					System.out.println("***************NO PEER NAME REGISTRATION PAGE***********************************");
					//System.out.println("***************************************************************");	
					RequestServingMechanism servingMechanism=
						trustManager.getRequestServingMechanismByName("sessionRegistration");
					servingMechanism.serveRequest(	(HttpServletRequest)req,
													(HttpServletResponse)resp,
													chain,
													res,
													filterConfig.getServletContext());//regJsp);
				}else{
					System.out.println("filtering peerName:"+peerName);
					res=trustManager.guardResource(res,peerName);
					if(((ProtectedResource)res).getCanAccess()){			
						System.out.println("\n==================ACCCESSING PROTECTED RESOURCE===================");
						System.out.println("trustManager:"+trustManager+ "\t res:"+res);
						System.out.println("\n==================================================================");
						
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
					System.out.println("\n=======================ACCESSING PUBLIC RESOURCE==================");
					System.out.println("trustManager:"+trustManager+ "\t res:"+res);
					System.out.println("\n==================================================================");
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
			castEx.printStackTrace();
		} catch (IllegalAccessPolicyAssociation e) {
			e.printStackTrace();
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
			System.out.println("filterConfig is null!");
			return;
		}
		
		if(negoObjects!=null){
			System.out.println("Initialization already done!");
			return;
		}
		
		
		ServletContext filterContext=filterConfig.getServletContext();
		String negotiationObjectsContext= 
			(String)filterContext.getInitParameter("NegotiationObjectsContext");
		
		System.out.println(">>>>>>>>>>>>>>>>>>FilterName:"+filterConfig.getFilterName());
		System.out.println(">>>>>>>>>>>>>>>>>>NegotiationObjectsContext:"+negotiationObjectsContext);
		System.out.println(">>>>>>>>>>>>>>>>>>Original Context:"+filterContext.getServletContextName());
		filterContext.log(">>>>>>>>>>>>>>>>>>1FilterName:"+filterConfig.getFilterName());
		//get demo context	

		ServletContext demoContext=filterContext.getContext(negotiationObjectsContext);//	"/demo");
		
		negoObjects= 
			NegotiationObjects.createAndAddForAppContext(demoContext);
		trustManager=(negoObjects!=null)?negoObjects.getTrustManager():null;
	}
	
	
}
