/**
 * 
 */
package org.peertrust.demo.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.peertrust.demo.resourcemanagement.IllegalAccessPolicyAssociation;
import org.peertrust.demo.resourcemanagement.ProtectedResource;
import org.peertrust.demo.resourcemanagement.PublicResource;
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
			System.out.println("filtering url:"+url);
			
			Resource res= trustManager.classifyResource(url);
			if(res instanceof PublicResource){
				System.out.println("filtering public page");
				if(url.equals(res.getUrl())){
					chain.doFilter(req,resp);
				}else{
					RequestDispatcher dispatcher = 
	    				req.getRequestDispatcher(res.getUrl());
	    			try{
	    				dispatcher.forward(req, resp);
	    				return;
	    			}catch(ServletException e){
	    				System.out.println("--exception while including .jnlp --\n");
	    				e.printStackTrace();
	    				return;///send error code
	    			}
				}
			}else{
				System.out.println("filtering protected  page");
				HttpSession session=
					((HttpServletRequest)req).getSession(false);
				System.out.println("filtering protected  page session id:"+session);
				//Peer peer=(Peer)session.getAttribute(PeerTrustCommunicationServlet.PEER_NAME_KEY);
				Peer peer= negoObjects.getSessionPeer(session.getId());
				String peerName= null;
				if(peer!=null){
					peerName=peer.getAlias();
				}
				
				System.out.println("filtering peerName:"+peerName);
				res=trustManager.guardResource(res,peerName);
				if(((ProtectedResource)res).getCanAccess()){
					
					RequestDispatcher dispatcher = 
	    				req.getRequestDispatcher(res.getUrl());
	    			try{
	    				dispatcher.forward(req, resp);
	    				//dispatcher.include(req,resp);
	    				return;
	    			}catch(ServletException e){
	    				System.out.println("--exception while including .jnlp --\n");
	    				e.printStackTrace();
	    				return;///send error code
	    			}
				}else{
	           		resp.setContentType("text/html");
					resp.getWriter().println(
							"Cannot access "+res.getUrl()+
							" cause:"+((ProtectedResource)res).getReason());
					resp.flushBuffer();
					return;
				}
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
