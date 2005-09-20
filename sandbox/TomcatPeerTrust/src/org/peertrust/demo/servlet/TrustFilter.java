/**
 * 
 */
package org.peertrust.demo.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
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
 * @author pat_dev
 *
 */
public class TrustFilter implements Filter{
	NegotiationObjects negoObjects;
	TrustManager trustManager;
	public void destroy() {
		return;
	}
	
	public void doFilter(ServletRequest req, ServletResponse resp,
			FilterChain chain) throws IOException, ServletException {
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
					System.out.println("***************************************************************");
//					PublicResource res=
//						new PublicResource(
//								"_exact_",
//								((HttpServletRequest)req).getRequestURI());//"/demo/jsp/sessionRegistration.jsp");
//					regJsp.setRequestServingMechanimName("sessionRegistration");	
					RequestServingMechanism servingMechanism=
						trustManager.getRequestServingMechanismByName("sessionRegistration");
					servingMechanism.serveRequest(	(HttpServletRequest)req,
													(HttpServletResponse)resp,
													chain,res);//regJsp);
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
														chain,res);
						return;
					}else{
		           		resp.setContentType("text/html");
						resp.getWriter().println(
								"Cannot access "+res.getUrl()+
								" cause:"+((ProtectedResource)res).getReason());
						resp.flushBuffer();
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
												chain,res);
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
	
	public void init(FilterConfig filterConfig) throws ServletException {
		negoObjects= 
			NegotiationObjects.createAndAddForAppContext(
									filterConfig.getServletContext());
		trustManager=negoObjects.getTrustManager();
		System.out.println("TrustManager just created:"+trustManager);
		return;
	}

}
