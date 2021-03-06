/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.util.ArrayList;
import java.util.List;
import org.peertrust.modeler.policysystem.model.abtract.PSModelChangeVeto;
import org.peertrust.modeler.policysystem.model.abtract.PSModelStatement;

/**
 * Default implemention of the <code>PSModelChangeVeto</code> interface
 * 
 * @author Patrice Congo
 *
 */
public class PSModelChangeVetoImpl implements PSModelChangeVeto 
{
	/**
	 * The vector holding the vetoing statements
	 */
	List<PSModelStatement> vetoingObject;
	
	/**
	 *To create an empty model change veto   
	 */
	public PSModelChangeVetoImpl() 
	{
		super();
		vetoingObject=new ArrayList<PSModelStatement>();
	}

	/**
	 * @see org.peertrust.modeler.policysystem.model.abtract.PSModelChangeVeto#getVetoingStatements()
	 */
	public PSModelStatement[] getVetoingStatements() 
	{
		final int SIZE=vetoingObject.size();
		PSModelStatement stms[]= new PSModelStatement[SIZE];
		stms=vetoingObject.toArray(stms);
				//because a new array is created if stms due to concurent
				//change does not contains enougth space
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
