package org.policy.action.standard;

import org.policy.config.Configurable;

public interface PackageRegistry extends Configurable 
{
	void addPackage (String packageName, AbstractExecutorWrapper packageExecutor) ;
	
	AbstractExecutorWrapper removePackage (String packageName) ;
	
	AbstractExecutorWrapper getPackageExecutor (String packageName) throws UnavailablePackageException ;
}
