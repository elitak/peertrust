/*
 * Created on 17.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.servlet;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;



/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NegotiationOutcome {
	
	private boolean canAccess=false;
	private String reason="nothing";
	private String realNegoResource;
	private String requestNegoResource;
	
	public NegotiationOutcome(	HttpServletRequest request, 
								ServletConfig config, 
								ServletContext appContext){
		getOutcome(request,config,appContext);
	}
	
	private void getOutcome(	
						HttpServletRequest request,
						ServletConfig config,
						ServletContext appContext){
		
		NegotiationObjects negoObjects= 
			NegotiationObjects.createAndAddForAppContext(config);
//		ServletPeerTrustEventListener ptEListener= 
//				negoObjects.getPeerTrustEventListener();
		requestNegoResource=(String)request.getParameter("negoResource");
		if(requestNegoResource==null){
			this.canAccess=false;
			this.reason="No Resource Reuested!";
			realNegoResource=appContext.getInitParameter("defaultResource");
		}else{
			realNegoResource=appContext.getInitParameter(requestNegoResource);
			
			if(realNegoResource==null){
				realNegoResource=appContext.getInitParameter("defaultResource");
			}
			if(negoObjects.isFreePage(requestNegoResource)){		
				canAccess=true;reason="freePage";
			}else{
			
//				realNegoResource=appContext.getInitParameter(requestNegoResource);
//				
//				if(realNegoResource==null){
//					realNegoResource=appContext.getInitParameter("DefaultResource");
//				}
				String negoID= (String)request.getParameter("negoSessionID");
				if(negoID==null){
					//may be user/password are used so check it
					canAccess=checkPassword(request,config,appContext);
				}else{
					canAccess=checkPTNegoOutcome(negoID,negoObjects.getPeerTrustEventListener());
				}
			}
		}
		return;
	}
	
	
	public boolean checkPassword(	
							HttpServletRequest request, 
							ServletConfig config, 
							ServletContext appContext){
		//String userName=request.getParameter("userName");
		String userName=request.getParameter("userName");
		if(userName==null){
			reason="No User Name";
			return false;
		}else{			
			String password= request.getParameter("password");
			String initPassword=appContext.getInitParameter("username="+userName);
			if(password==null ){
				reason="no password provided";
				return false;
			}else if(initPassword==null){			
				reason="unknown access";
				return false;
			}else{
				boolean result=password.equals(initPassword);
				if(!result){
					reason="bad password";
				}
				return result;
			}			
		}
	}
	
	boolean checkPTNegoOutcome(String negoID,ServletPeerTrustEventListener ptEventListener){
		boolean result= false;
		try {
			result=ptEventListener.isNetgotiationSuccessfull(negoID);
			if(!result){
				reason="nego fail";
			}
		} catch (RuntimeException e){
			e.printStackTrace();
			result=false;
			reason="Internal Error";
		}
		return result;
	}
	
	public String toString(){
		return 
			"\nnegoResource:"+requestNegoResource+
			" canAccess:"+canAccess+
			" reason:"+reason;
	}
	
	
	public boolean isCanAccess() {
		return canAccess;
	}
	public void setCanAccess(boolean canAccess) {
		this.canAccess = canAccess;
	}
	public String getRealNegoResource() {
		return realNegoResource;
	}
	public void setRealNegoResource(String realNegoResource) {
		this.realNegoResource = realNegoResource;
	}
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getRequestNegoResource() {
		return requestNegoResource;
	}
	public void setRequestNegoResource(String requestNegoResource) {
		this.requestNegoResource = requestNegoResource;
	}
}
