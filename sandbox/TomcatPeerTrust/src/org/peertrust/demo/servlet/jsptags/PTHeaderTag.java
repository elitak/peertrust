/*
 * Created on 14.07.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.servlet.jsptags;


import javax.servlet.jsp.JspException;

import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PTHeaderTag extends TagSupport {
	static public final String NEGO_PARAMS_CACHE="_NEGO_PARAMS_CACHE_"; 
	private String peerName;
	private String ptEvaluatorClassName;
	private PropertyCache propertyCache;
	private String cacheKey;
	
	/**
	 * 
	 */
	public PTHeaderTag() {
		super();
	}

	
	public String getPeerName() {
		return peerName;
	}
	
	public void setPeerName(String peerName) {
		this.peerName = peerName;
	}
	
	public void cachePTParameter(PTParamCreationSpecification creationSpec)throws JspException{
		System.out.println("----------------------propertyCache:"+propertyCache+ 
					" creationSpec:"+creationSpec);
		try {
			propertyCache.cacheProperty(creationSpec);
		} catch (NullPointerException e) {
			e.printStackTrace();
			throw new JspException(
					"----------------------propertyCache:"+propertyCache+ 
						" creationSpec:"+creationSpec,
					e);
		} catch (BadObjectStateException e) {
			throw new JspException("InvalidObject content, maybe caused by jsp tag",e);
		}			
	}
	
	
	///tag sup methods

	public String getPtEvaluatorClassName() {
		return ptEvaluatorClassName;
	}


	public void setPtEvaluatorClassName(String ptEvaluatorClassName) {
		this.ptEvaluatorClassName = ptEvaluatorClassName;
		System.out.println("-------------setting ptEvaluatorClassName:"+ptEvaluatorClassName);
	}


	public PropertyCache getPropertyCache() {
		return propertyCache;
	}

	
	
	public String getCacheKey() {
		return cacheKey;
	}


	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
		System.out.println("--------------------------------0cacheKey:"+cacheKey);
	}


	public void release() {
		// TODO Auto-generated method stub
		super.release();
	}


	public int doStartTag() throws JspException {
		this.propertyCache= new PropertyCache();
		Class c;
		//create evaluator
		
		try {
			c=Class.forName(ptEvaluatorClassName);//.getClass();
			PTPropertyEvaluator evaluator=
				//(PTPropertyEvaluator)c.getConstructor(new Class[0]).newInstance(new Object[0]);
				(PTPropertyEvaluator)c.newInstance();
			evaluator.init(pageContext);
			this.propertyCache.setPtEvaluator(evaluator);
		} catch (ClassNotFoundException e) {
			//e.printStackTrace();
			throw new JspException("cannot find evaluator "+ptEvaluatorClassName,e);
		} catch (InstantiationException e) {
			//e.printStackTrace();
			throw new JspException("CCCannot create an instance of "+ptEvaluatorClassName,e);
		} catch (IllegalAccessException e) {
			//e.printStackTrace();
			throw new JspException("exception while creating evaluator "+ptEvaluatorClassName,e);
		} catch (IllegalArgumentException e) {
			//e.printStackTrace();
			throw new JspException("problem constructing evaluator "+ptEvaluatorClassName,e);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			throw new JspException("problem constructing evaluator "+ptEvaluatorClassName,e);
		} 
//		catch (InvocationTargetException e) {
//			throw new JspException("problem constructing evaluator "+ptEvaluatorClassName,e);
//		} catch (NoSuchMethodException e) {
//			throw new JspException("problem constructing evaluator "+ptEvaluatorClassName,e);
//		}
		return Tag.EVAL_BODY_INCLUDE;//super.doStartTag();
	}	

	public int doEndTag() throws JspException {
		System.out.println("---------------------------------cacheKey:"+this.cacheKey);
		pageContext.setAttribute(this.cacheKey,this.propertyCache);
		return Tag.EVAL_PAGE;//super.doEndTag();
	}
	
	public static final void main(String[] args)throws Exception{
//		PTPropertyEvaluator evaluator=
//			(PTPropertyEvaluator)Class.forName(
//					PtEvaluatorMock.class.getName()).newInstance();
		
//		PTPropertyEvaluator evaluator=
//			(PTPropertyEvaluator)Class.forName(
//					"org.peertrust.demo.servlet.jsptags.PtEvaluatorMock").newInstance();
	}
	
}
