/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSModelStatement;

/**
 * @author pat_dev
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
	private PSModelObject object;
	
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
						PSModelObject object) 
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
	public PSModelObject getObject() 
	{
		return object;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelStatement#setObject(org.peertrust.modeler.policysystem.model.abtract.PSModelObject)
	 */
	public void setObject(PSModelObject object) 
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
