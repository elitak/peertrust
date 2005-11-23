/*
 * Created on 14.07.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.servlet.jsptags;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.JspTagException;
import javax.servlet.jsp.tagext.BodyTagSupport;
import javax.servlet.jsp.tagext.Tag;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PTParamCreateTag extends BodyTagSupport{
	//private String name;
	private PTParamCreationSpecification ptCreateSpec;
	
	private boolean ifSpecAlreadySet=false;
	private boolean elseSpecAlreadySet=false;
	
	/**
	 * 
	 */
	public PTParamCreateTag() {
		super();
	}

	
	public int doStartTag() throws JspTagException {
		ifSpecAlreadySet=false;
		elseSpecAlreadySet=false;
		//check parent 
		//PTHeaderTag headerTag;
		Tag tag =
	        (PTHeaderTag)getParent();
	      if (tag == null) {
	        throw new JspTagException(
	        		"ptCreateTag must be nested in header tag; this tag does not have any parent");
	      }
	      
	      if(!(tag instanceof PTHeaderTag)){
	    	  throw new JspTagException(
      			"ptCreateTag must be nested in header tag; the parent type is:"+tag.getClass());
	      }
	      
		return EVAL_BODY_INCLUDE;
	}
	
	public int doEndTag() throws JspTagException {
		//save param
		try {
			((PTHeaderTag)getParent()).cachePTParameter(this.ptCreateSpec);
		} catch (JspException e) {
			e.printStackTrace();
			throw new JspTagException("could not cache "+this.ptCreateSpec.getParamName());
		}
		return EVAL_PAGE;
	}


	public String getName() {
		//return name;
		return ptCreateSpec.getParamName();
	}


	public void setName(String name) {
		//this.name = name;
		System.out.println("-------------------- name:"+name);
		//this.ptCreateSpec.set
		this.ptCreateSpec= new PTParamCreationSpecification(name);
	}
	
	public void setIfSpec(String property, String valueToset) throws JspTagException{
		System.out.println("------------------------------ifSpecAlreadySet:"+ifSpecAlreadySet);
		if(this.ifSpecAlreadySet){
			throw 
				new JspTagException(
						"Value already set and cannot be set again; several ifPropertyHold Tag");
		}
		ptCreateSpec.setIfSpec( 
				new PropertyValueToSetPair(property,valueToset));
		this.ifSpecAlreadySet=true;
	}
	
	public void setElseSpecString(String valueToSet)throws JspTagException{
		if(!this.ifSpecAlreadySet){
			throw 
				new JspTagException(
						"elseNoproperty hold tag cannot come before if tag");
		}
		if(elseSpecAlreadySet){
			throw 
			new JspTagException(
					"elseNoproperty can only set once; PTParamCreate tag must not contain 2 elseNoPropertyHold tags");
		}
		ptCreateSpec.setElseSpec(
				new PropertyValueToSetPair("_else_fake_nane_",valueToSet));
		this.elseSpecAlreadySet=true;
	}
	
	public void addElseIfSpec(String property, String valueToset)throws JspTagException{
		if(!this.ifSpecAlreadySet){
			throw 
				new JspTagException("ElseIfTagHoldTag must accor after a IfPropertyHoldTag");
		}
		if(this.elseSpecAlreadySet){
			throw 
				new JspTagException("ElseIfProperty tag cannot occur after elseNoPropertyHold tag");
		}
		
		ptCreateSpec.addElseIfSpec(
				new PropertyValueToSetPair(property,valueToset));
	}


	public void release() {
		//name=null;
		ptCreateSpec=null;
		
		ifSpecAlreadySet=false;
		elseSpecAlreadySet=false;
	}
	
	
}
