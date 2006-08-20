package org.peertrust.modeler.policysystem.model.abtract;

/**
 * The interface to implement by a class used to signal
 * a veto to a change attempt on the policy system model.
 * E.g. if a policy is to be remove but is still used to
 * protect a resource.
 * 
 * @author Patrice Congo
 *
 */
public interface PSModelChangeVeto 
{
	/**
	 * A flag veto that means there was no vetoing
	 */
	public static final PSModelChangeVeto NO_VETO=
		new PSModelChangeVeto()
		{
			final PSModelStatement NO_STMS[]=new PSModelStatement[0];
			public PSModelStatement[] getVetoingStatements() 
			{
				return NO_STMS;
			}

			public void addPSModelStatement(PSModelStatement statement) 
			{
				//empty
			}

			public void removePSModelStatement(PSModelStatement statement) 
			{
				//empty
			}

			public void clear() 
			{
				//empty				
			}
		
		};
		
	/**
	 * Returns the statement which veto the modification of the
	 * model.
	 * 
	 * @return
	 */
	public PSModelStatement[] getVetoingStatements();
	
	/**
	 * To add a new vetoing model statement
	 * @param statement
	 */
	public void addPSModelStatement(PSModelStatement statement);
	
	/**
	 * To remove a vetoing statement
	 * @param statement
	 */
	public void removePSModelStatement(PSModelStatement statement);
	
	/**
	 * To remove all vetoing statement
	 */
	public void clear();
}
