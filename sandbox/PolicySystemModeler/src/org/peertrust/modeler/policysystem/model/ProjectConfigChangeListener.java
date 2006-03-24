package org.peertrust.modeler.policysystem.model;

/**
 * Interface to implement in order to listen to changes
 * at a ProjectConfig
 * 
 * @author Patrice Congo
 *
 */
public interface ProjectConfigChangeListener 
{
	/**
	 * Call if a Project config has changed
	 * @param config -- the changed project config
	 */
	void projectConfigChanged(ProjectConfig config);	
}
