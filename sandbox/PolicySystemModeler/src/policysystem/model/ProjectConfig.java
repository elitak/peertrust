package policysystem.model;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

public class ProjectConfig 
{
	
	static final private ProjectConfig instance=new ProjectConfig();
	
	private String projectFile;
	private Vector projectConfigChangeListeners= new Vector();
	private Properties properties=new Properties();
	
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
		System.out.println("adding:"+l);
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
		for(int i=projectConfigChangeListeners.size()-1;i>=0;i--)
		{
			ProjectConfigChangeListener l=
				(ProjectConfigChangeListener)
					projectConfigChangeListeners.get(i);
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
	
	
}
