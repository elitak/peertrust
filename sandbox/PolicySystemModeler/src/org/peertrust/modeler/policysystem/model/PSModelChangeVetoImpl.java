/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.util.Vector;

import org.peertrust.modeler.policysystem.model.abtract.PSModelChangeVeto;
import org.peertrust.modeler.policysystem.model.abtract.PSModelStatement;

/**
 * Implements the PSModelChangeVeto interface
 * @author Patrice Congo
 *
 */
public class PSModelChangeVetoImpl implements PSModelChangeVeto 
{
	/**
	 * The vector holding the vetoing statements
	 */
	Vector vetoingObject;
	
	/**
	 *To create an empty model change veto   
	 */
	public PSModelChangeVetoImpl() 
	{
		super();
		vetoingObject=new Vector();
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelChangeVeto#getVetoingStatements()
	 */
	public PSModelStatement[] getVetoingStatements() 
	{
		final int SIZE=vetoingObject.size();
		PSModelStatement stms[] = new PSModelStatement[SIZE];
		vetoingObject.toArray(stms);
		
		return stms;
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelChangeVeto#addPSModelStatement(org.peertrust.modeler.policysystem.model.abtract.PSModelStatement)
	 */
	public void addPSModelStatement(PSModelStatement statement) 
	{
		if(statement==null)
		{
			return;
		}
		
		vetoingObject.add(statement);
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelChangeVeto#removePSModelStatement(org.peertrust.modeler.policysystem.model.abtract.PSModelStatement)
	 */
	public void removePSModelStatement(PSModelStatement statement) 
	{
		if(statement==null)
		{
			return;
		}
		vetoingObject.remove(statement);
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelChangeVeto#clear()
	 */
	public void clear() 
	{
		vetoingObject.clear();
	}

}
