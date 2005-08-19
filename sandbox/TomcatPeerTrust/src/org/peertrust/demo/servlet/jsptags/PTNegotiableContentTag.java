/**
 * 
 */
package org.peertrust.demo.servlet.jsptags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author pat_dev
 *
 */
public class PTNegotiableContentTag extends TagSupport {
	private PropertyCache propertyCache;
	private String cacheKey;
	private boolean matchAlreadyFound;
	
	private boolean ifAlreadyDone;
	private boolean elseAlreadyDone;
	
	/**
	 * 
	 */
	public PTNegotiableContentTag() {
		super();
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
	
	public boolean isMatchAlreadyFound() {
		return matchAlreadyFound;
	}

	public boolean ifExibitsAttribute(String name, String value)throws JspException{
		System.out.println("ifExibitsAttribute("+name+","+value+")");
		if(name==null || value==null){
			throw 
				new NullPointerException(
						"Both parameters must not be null: name="+
						name+" value="+value);
		}
		if(ifAlreadyDone){
			throw new JspException("ifAlready done; negotiatiable content must exactely one if");
		}
		if(elseAlreadyDone){
			throw new JspException("if cannot come after else");
		}
		ifAlreadyDone=true;
		if(value.equalsIgnoreCase(propertyCache.getParameter(name))){
			matchAlreadyFound=true;
			return true;
		}
		return false;
			
	}

	public boolean elseIfExibitsAttribute(String name, String value)throws JspException{
		System.out.println("elseIfExibitsAttribute("+name+","+value+")");
		if(name==null || value==null){
			throw 
				new NullPointerException(
						"Both parameters must not be null: name="+
						name+" value="+value);
		}
		if(!ifAlreadyDone){
			throw new JspException("elseif must come after if");
		}
		if(elseAlreadyDone){
			throw new JspException("elseif cannot come before else");
		}
		if(value.equalsIgnoreCase(propertyCache.getParameter(name))){
			matchAlreadyFound=true;
			return true;
		}
		
		return false;
			
	}
	
	public boolean elseExibitsNoAttribute()throws JspException{
		System.out.println("elseExibitsNoAttribute()");
		if(!ifAlreadyDone){
			throw new JspException("else must come after if");
		}
		elseAlreadyDone=true;
		matchAlreadyFound=true;
		return true;
			
	}
	//////////////////////////////////////////tag suport methods
	
	public int doEndTag() throws JspException {
		return Tag.EVAL_PAGE;
	}

	public int doStartTag() throws JspException {
		matchAlreadyFound=false;
		ifAlreadyDone=false;
		elseAlreadyDone=false;
		
		try {
			propertyCache= (PropertyCache)pageContext.getAttribute(cacheKey);
			System.out.println("************************propertyCache:"+propertyCache);
		} catch (ClassCastException e) {
			e.printStackTrace();
			throw new JspException("PropertyCache expected",e);
		}
		return Tag.EVAL_BODY_INCLUDE;
	}

	public void release() {
		propertyCache=null;
		cacheKey=null;
		matchAlreadyFound=false;
		ifAlreadyDone=false;
		elseAlreadyDone=false;
	}
	
}
