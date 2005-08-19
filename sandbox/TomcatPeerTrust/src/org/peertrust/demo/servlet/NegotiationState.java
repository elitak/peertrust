/*
 * Created on Apr 5, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.servlet;

import org.peertrust.net.Answer;


public class NegotiationState{
	static final int NEGOTIATION_STATE_SUCCESSFULL=0;
	static final int NEGOTIATION_STATE_FAIL=1;
	static final int NEGOTIATION_STATE_GOING_ON=2;
	
	//private String id;
	private String negotiationResource;
	private String negotiationID;
	private Answer finalAnswer=null;
	
	private int actualState;
	private String failReason;
	private long timeStamp=0;
	
//	private NegotiationState(String id){
//		this.negotiationID=id;
//		actualState=NEGOTIATION_STATE_GOING_ON;
//		return;
//	}
	
	public NegotiationState(String negoID, String negoRes){
		//this(negoID+"@"+negoRes);
		this.negotiationResource=negoRes;
		this.negotiationID=negoID;
		this.actualState=NEGOTIATION_STATE_GOING_ON;
		this.timeStamp=System.currentTimeMillis();
		return;
	}
	
	public void setNegotiationHasFail(String reason){
		actualState=NEGOTIATION_STATE_FAIL;
		this.failReason=reason;
		return;
	}
	
	public void setNegotiationSuccessfull(){
		actualState= NEGOTIATION_STATE_SUCCESSFULL;
		return;
	}
	
	public boolean isNegotiationSuccessfull(){
		return (actualState==NEGOTIATION_STATE_SUCCESSFULL);
	}
	
	public boolean isNegotiationGoingOn(){
		return (actualState==NEGOTIATION_STATE_GOING_ON);
	} 
	public boolean hasFailed(){
		return (actualState==NEGOTIATION_STATE_FAIL);
	}
	public String getFailReason(){
		return failReason;
	}
	
	
	public String getNegotiationID() {
		return negotiationID;
	}
	
	public boolean isTooOld(long currentTimeInMillis, long maxAgeInMillis){
		return (currentTimeInMillis-timeStamp)>maxAgeInMillis;
	}
	
	
	
	public Answer getFinalAnswer() {
		return finalAnswer;
	}
	public void setFinalAnswer(Answer finalAnswer) {
		this.finalAnswer = finalAnswer;
		int status=finalAnswer.getStatus();
		this.timeStamp=System.currentTimeMillis();
		if(status==Answer.ANSWER || status==Answer.LAST_ANSWER){
			actualState=NEGOTIATION_STATE_SUCCESSFULL;
		}else if(status==Answer.FAILURE){
			actualState=NEGOTIATION_STATE_FAIL;
		}else{
			actualState=NEGOTIATION_STATE_FAIL;
		}
	}
}