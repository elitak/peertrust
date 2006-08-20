/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSModelStatement;

/**
 * The Default implementation of a PSModelStatement.
 * 
 * @author Patrice Congo
 *
 */
public class PSModelStatementImpl implements PSModelStatement {

	/**
	 * The stament property key
	 */
	private String property;
	
	
	/**
	 * The statement subject
	 */
	private PSModelObject subject;
	
	
	/**
	 * The stament object
	 */
	private Object object;
	
	/**
	 *Create an empty PSModelStamentImpl object 
	 */
	public PSModelStatementImpl() 
	{
		super();
	}

	/**
	 *Create an empty PSModelStamentImpl object 
	 */
	public PSModelStatementImpl(
						PSModelObject subject, 
						String property,
						Object object) 
	{
		super();
		this.subject=subject;
		this.object=object;
		this.property=property;
	}
	
	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelStatement#getSubject()
	 */
	public PSModelObject getSubject() 
	{
		return subject;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelStatement#setSubject(org.peertrust.modeler.policysystem.model.abtract.PSModelObject)
	 */
	public void setSubject(PSModelObject subject) 
	{
		this.subject=subject;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelStatement#getObject()
	 */
	public Object getObject() 
	{
		return object;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelStatement#setObject(java.lang.Object)
	 */
	public void setObject(Object object) 
	{
		this.object=object;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelStatement#getProperty()
	 */
	public String getProperty() 
	{
		return property;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelStatement#setProperty(java.lang.String)
	 */
	public void setProperty(String property) 
	{
		this.property=property;
	}

}
