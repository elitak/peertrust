package org.peertrust.modeler.policysystem.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.PolicysystemPlugin;


public class ProjectConfig 
{
	static final public String ROOT_DIR="rootDir";
	static final public String RDF_SCHEMA_FILE="rdfSchemaFile";
	static final public String RDF_MODEL_FILE="rdfModelFile";
	
	
	static final private ProjectConfig instance=new ProjectConfig();
	
	private String projectFile;
	private Vector projectConfigChangeListeners= new Vector();
	private Properties properties=new Properties();
	
	private static Logger logger= 
					Logger.getLogger(ProjectConfig.class);
	
	private ProjectConfig()
	{
		
	}
	
	synchronized static public ProjectConfig getInstance()
	{
		return instance;
	}

	synchronized public String getProjectFile() 
	{
		return projectFile;
	}

	synchronized public void setProjectFile(String projectFile) 
	{
		System.out.println("pjectFile="+projectFile+
					" "+projectConfigChangeListeners);
		this.projectFile = projectFile;
		this.properties.clear();
		try {
			this.properties.load(new FileInputStream(projectFile));
		} catch (FileNotFoundException e) {
			e.printStackTrace();return;
		} catch (IOException e) {
			e.printStackTrace();return;
		}
		fireProjectConfigChange();
	}
	
	synchronized public void addProjectConfigChangeListener(
											ProjectConfigChangeListener l)
	{
		if(l==null)
		{
			return;
		}
		logger.info("Adding Listener:"+l);
		projectConfigChangeListeners.add(l);
	}
		
	synchronized public void removeProjectConfigChangeListener(
											ProjectConfigChangeListener l)
	{
		if(l==null)
		{
			return;
		}
		projectConfigChangeListeners.remove(l);
	}
	
	synchronized public void fireProjectConfigChange()
	{
		for(Iterator i=projectConfigChangeListeners.iterator();
			i.hasNext();)
		{
			ProjectConfigChangeListener l=
				(ProjectConfigChangeListener)i.next();
			l.projectConfigChanged(this);
		}
	}

	public String getProperty(String key) {
		return properties.getProperty(key);
	}

	public String put(String key, String value) {
		return (String)properties.put(key, value);
	}

	public void store(OutputStream out, String comments) throws IOException {
		properties.store(out, comments);
	}

	public void storeToXML(OutputStream os, String comment) throws IOException {
		properties.storeToXML(os, comment);
	}
	
	public void createNewProjectConfigFile(
						String projectName,
						File destFolder,
						File rootDir)
	{
		try {
			//File destFolder=configFile.getParentFile();
			File rdfsFile= new File(destFolder,projectName+".rdfs");
			File rdfFile= new File(destFolder,projectName+".rdf");
			projectFile= (new File(destFolder,projectName+".conf")).getAbsolutePath();
			
			PolicysystemPlugin.getDefault().copyModelfilesTo(rdfsFile,rdfFile);
			
			properties.setProperty(ROOT_DIR,rootDir.getCanonicalPath());
			properties.setProperty(RDF_MODEL_FILE,rdfFile.getCanonicalPath());
			properties.setProperty(RDF_SCHEMA_FILE,rdfsFile.getCanonicalPath());
			FileOutputStream outStream= new FileOutputStream(projectFile);
			
			store(outStream,"Policy System Project");
			fireProjectConfigChange();
		} catch (Exception e) {
			PolicysystemPlugin.getDefault().showException(
					"Error while creating new Pjt",e);
		}
	}
	
}
