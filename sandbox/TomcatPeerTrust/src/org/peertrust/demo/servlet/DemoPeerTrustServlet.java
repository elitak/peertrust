package org.peertrust.demo.servlet;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.net.SocketAppender;
/*
 * Created on 19.02.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author kbs
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class DemoPeerTrustServlet extends HttpServlet {
	Logger logger;
    static final String respHTML="<html><body><big>I am not Small<big></body></html>";
    //static final String JWS_APP_URL_REAL="http://127.0.0.1:7703/myapp-0.1-dev/launch.jnlp";
    //static final String JWS_APP_URL="http://127.0.0.1:7703/myapp-0.1-dev/DemoPeerTrustServlet/launch_1.jnlp";
    static final String JWS_APP_URL="/myapp-0.1-dev/DemoPeerTrustServlet/launch_1.jnlp";
    
    //static final String JWS_APP_URL_REAL="http://127.0.0.1:7703/myapp-0.1-dev/jsp/launch.jsp";
    //static final String JWS_APP_URL_REAL="http://127.0.0.1:7703/jsp/launch_1.jsp";
    static final String JWS_APP_URL_REAL="/jsp/launch_1.jsp";
    
    ServletPeerTrustEventListener peerTrustEventListener=null;
    int sessionMaxInactiveTime=10*60;
	//private ServletSideNetServer trustServer;
	//private ServletSideNetClient trustClient;
    private String serviceServletPath=null;
    private NegotiationObjects negoObjects;
    
	public void init(ServletConfig config) throws ServletException {
		logger= Logger.getLogger(DemoPeerTrustServlet.class);
		//register to listen to
		logger.setLevel(Level.ALL);
        logger.setAdditivity(true);
        logger.addAppender(new SocketAppender("127.0.0.1",4445));
        
		
		// TODO Auto-generated method stub
		ServletContext context= config.getServletContext();
		serviceServletPath=context.getInitParameter("ServiceServletPath");
		
		try{
		
			logger.info(".............init.................");
			negoObjects= 
				NegotiationObjects.createAndAddForAppContext(config);
			peerTrustEventListener= negoObjects.getPeerTrustEventListener();
			//peerTrustEventListener.getClass();
		}catch(Throwable th){
			//th.printStackTrace();
			logger.error("--negotiation object not created--",th);
		}
		
//		try {
//			Context ic = new InitialContext();
//
//			 NegotiationObjects negoObjects = 
//			 	(NegotiationObjects) ic.lookup("java:comp/env/negotiationObjects");
//			 trustServer=negoObjects.getTrustServer();
//			 trustClient=negoObjects.getTrustClient();
//			 peerTrustEngin=negoObjects.getPeerTrustEngin();
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}
	}
    /* (non-Javadoc)
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        // TODO Auto-generated method stub
        //super.doGet(arg0, arg1);
        /*PrintWriter pw=resp.getWriter();
        pw.print(respHTML);
        pw.flush();/**/
        serveStart(req,resp);
        return;
    }
    
    
    
    private void serveStart(HttpServletRequest req, HttpServletResponse resp){
   	 
   	String urlWithParams = null;
   	HttpSession session = req.getSession( true ); 
   	
   	PrintWriter out;
   	String requestURI=req.getRequestURI();
   	logger.info(requestURI);
//   	System.out.println("**********************************");
//   	System.out.println((new Date()).toString());
//   	System.out.println(requestURI);
       try {
           out = resp.getWriter();
           String action = req.getParameter("action");
           if(action==null){
           	action="_blablabla_";
           }
           
           if(action.equalsIgnoreCase("ask")){
           		resp.setContentType("text/plain");
           		resp.getWriter().print("NO");  
           		
           		return;
           }else if(requestURI.endsWith(serviceServletPath)){
			out.println( "<HTML>" );
    		out.println( "<HEAD><TITLE>Actual Page</TITLE></HEAD>" ); 
    		out.println( "<BODY>" ); 
    		out.println( "<CENTER><H1>Actual Service Page is being displayed</H1><BR><B>" );
    		out.println(requestURI);out.println( "<BR/>");
    		
    		//String id= session.getId()+"@"+session.getAttribute("negoResource");
    		//String id= (String)req.getParameter("reqID");
    		String id= (String)req.getParameter("negoSessionID");
    		logger.info("serviceNegoID:"+id);
    		if(id!=null){
	    		peerTrustEventListener.waitForResult(id,3);
	    		Date date= new Date();
	    		out.println( "<BR/> id="+date.toString()+"<BR/>");
	    		out.println( "<BR/> id="+id+"<BR/>");
	    		if(this.peerTrustEventListener.isNegotiationGoingOn(id)){
	    			out.println("ongoing");
	    		}else if(peerTrustEventListener.isNetgotiationSuccessfull(id)){
	    			out.println("success");
	    		}else if(peerTrustEventListener.hasNetgotiationFailed(id)){
	    			out.println("has failed");
	    		}else{
	    			out.println("no nego for:"+id);
	    		}
    		}else{
    			out.println("negoSessionID:"+id);
    		}
    		out.println( "</B></CENTER>" );
    		out.println( "</BODY>" );
    		out.println( "</HTML>" );
    		out.close();
		///}else if(JWS_APP_URL.endsWith(requestURI)){
		}else{
			if(session.getAttribute("WAIT")==null){
		
           	preventCachingAndStoring(resp);
           	//if ( session.getValue( "WAIT" ) == null ){ 
           	session.setMaxInactiveInterval(sessionMaxInactiveTime);
           	session.setAttribute("WAIT","waiting");
	        	session.setAttribute("negoResource",req.getParameter("negoResource"));
	        	resp.setContentType( "text/html" );
	        	out.println( "<HTML>" );
	    		out.println( "<HEAD>" );
	    		out.println( "<TITLE>Please Wait...</TITLE>" ); 
//	    		out.println( "<META HTTP-EQUIV=\"REFRESH\" CONTENT=\"2;" );
//	    		out.println( 
//	    				resp.encodeURL("http://"+req.getLocalAddr()+
//	    						":"+req.getServerPort()+JWS_APP_URL+
//								"?negoSessionID="+session.getId()+
//								"&negoResource="+req.getParameter("negoResource")+
//								"&"));
//	    		//out.println(getServletContext().getInitParameter("launchPage"));
//	    		out.println( "\">");
	    		out.println("</HEAD>" );//contains also end of meta 
	    		out.println( "<BODY>" );
	    		out.println( "<CENTER>" );
	    		out.println( "<H1>Trust Negotiation in Progress</H1><HR>" ); 
	    		out.println( "<H2>Please wait......</H2>" );
	    		out.println("<H2>"+session.getId()+"</H2>");
	    		out.println("<H2>"+peerTrustEventListener+"</H2>");
	    		out.println( "</CENTER>" );
	    		
	    		out.println("<iframe src='");
	    		out.print(resp.encodeURL("http://"+req.getLocalAddr()+
						":"+req.getServerPort()+JWS_APP_URL+
						"?negoSessionID="+session.getId()+
						"&negoResource="+req.getParameter("negoResource")+
						"&"));
	    		out.print("' class='invisible_launch_frame'></iframe>");
	    		
	    		out.println( "</BODY>" );
	    		out.println( "</HTML>" );
	    		out.close(); 
	        	
	    	}else{ 
	    		if(requestURI.endsWith("launch_1.jnlp")){
	    			resp.setContentType("application/x-java-jnlp-file");
	    			urlWithParams=
	    				//resp.encodeURL(//
	    				//"http://"+req.getLocalAddr()+":"+req.getServerPort()+
	    						JWS_APP_URL_REAL;//+
								//"?negoSessionID="+session.getId()+
								//";negoResource="+req.getParameter("negoResource");
								//);
	    			RequestDispatcher dispatcher = 
	    				req.getRequestDispatcher(urlWithParams);/*
	    						JWS_APP_URL_REAL);/*+
	    						"?negoSessionID="+session.getId()+
								";negoResource="+req.getParameter("negoResource"));/**/
	    			try{
	    				//req.
	    				//dispatcher.forward(req, resp);
	    				dispatcher.include(req,resp);
	    				return;
	    			}catch(ServletException e){
	    				logger.debug("--exception while including .jnlp --",e);
	    				return;///send error code
	    			}
	    		}else{
		    		//session.removeValue( "WAIT" );
	    			session.removeAttribute("WAIT");
	    			session.removeAttribute("peerIP");
	    			session.removeAttribute("peerPort");
		    		out.println( "<HTML>" );
		    		out.println( "<HEAD><TITLE>Actual Page</TITLE></HEAD>" ); 
		    		out.println( "<BODY>" ); 
		    		out.println( "<CENTER><H1>Actual Page is being displayed</H1><BR><B>" );
		    		out.println(requestURI);
		    		out.println("Started computation<BR>");
		    		out.println(req.getRequestURL());
//		    		for ( int i = 1; i < 6; i++ ) 			//do your time consuming Process
//		    		{ 
//		    			out.println("Inside computation"+i+"<BR>");
//		    			try
//		    			{ 
//		    				Thread.sleep( 2000 ); 
//		    			} 
//		    			catch ( InterruptedException e ) 
//		    			{
//		    			} 
//		    		} 
//		    		out.println("Finished computation");
		    		out.println( "</B></CENTER>" );
		    		out.println( "</BODY>" );
		    		out.println( "</HTML>" );
		    		out.close();
	    		}
	    	}
		}
       } catch (IOException e1) {
           //e1.printStackTrace();
       	logger.debug("Error while processing:"+req.getRequestURI(),e1);
           resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
           try {e1.printStackTrace(resp.getWriter());resp.getWriter().print(urlWithParams);} catch (IOException e) {e.printStackTrace();}
           
       }catch(Throwable th){
       	logger.debug("Error while processing:"+req.getRequestURI(),th);
       	resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
       	try {th.printStackTrace(resp.getWriter());resp.getWriter().print(urlWithParams);} catch (IOException e) {e.printStackTrace();}
       }
       return;
   }
  
    public void preventCachingAndStoring(HttpServletResponse resp){
    	resp.setHeader("Pragma","no-cache");
		resp.setHeader("Cache-Control","no-cache");
		resp.setHeader("Cache-Control","no-store");		
		resp.setHeader("Expires","0");
		return;
    }
    
    public void getCoonectionKeptAlive(HttpServletResponse resp){
    	resp.setHeader("connection","Keep-Alive");
    }
    
    
	/* (non-Javadoc)
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest arg0, HttpServletResponse arg1)
			throws ServletException, IOException {
		this.serveStart(arg0, arg1);
		return;
	}
	
	
	public void destroy() {
		
		logger.info("stoping:"+this.getClass().getName());
		negoObjects.destroy();
		
		super.destroy();
		
	}
}
