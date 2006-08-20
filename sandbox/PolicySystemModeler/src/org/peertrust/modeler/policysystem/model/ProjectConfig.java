package org.peertrust.modeler.policysystem.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.modeler.policysystem.PolicysystemPlugin;
import org.peertrust.modeler.policysystem.model.exceptions.BadConfigFileException;


/**
 * This class implements the Singleton pattern to hold unique information
 * about the current project.
 * 
 * Drawback of this design is that the eclipse resource framework
 * (project nature) is not used
 * @author Patrice Congo
 *
 */
public class ProjectConfig 
{
	/**
	 * prefix for a root property; rootDir0 
	 */
	static final private String ROOT_DIR="rootDir";
	
	static final public String ROOT_DIR_PREFIX=ROOT_DIR+".";
	static final public int ROOT_DIR_PREFIX_LENGTH=
									ROOT_DIR_PREFIX.length();
	/**
	 * Key for the root count property.
	 */
	static final public String ROOT_COUNT="rootCount";
	
	/**
	 * Model rdf schema file name
	 */
	static final public String RDF_SCHEMA_FILE="rdfSchemaFile";
	
	/**
	 * Model rdf file
	 */
	static final public String RDF_MODEL_FILE="rdfModelFile";
	
	
	/**
	 * Singleton instance
	 */
	static final private ProjectConfig instance=new ProjectConfig();
	
	/**
	 * The project file name
	 */
	private String projectFile;
	
	/**
	 * Holds project config change listerner
	 */
	private List<ProjectConfigChangeListener> 
					projectConfigChangeListeners= 
							new Vector<ProjectConfigChangeListener>();
	
	/**
	 * Holds project properties which can be access using a key
	 */
	private Properties properties=new Properties();
	
	/**
	 * the number of root in this system
	 */
	private int rootCount=0;
	
	/**
	 * {@link ProjectConfig} class logger
	 */
	private final static Logger logger= 
					Logger.getLogger(ProjectConfig.class);
	
	/**
	 * holds the root of the resource hierarchy 
	 */
	private final List<URI> roots= new ArrayList<URI>();
	
	
	/**
	 * Default constructor, hidden according to the singleton pattern
	 */
	private ProjectConfig()
	{
		//empty
	}
	
	/**
	 * Get the unique instance this {@link ProjectConfig} class
	 * 
	 * @return the unique instance this {@link ProjectConfig} class
	 */
	synchronized static public ProjectConfig getInstance()
	{
		return instance;
	}

	/**
	 * @return the project file path as string
	 */
	synchronized public String getProjectFile() 
	{
		return projectFile;
	}

	/**
	 * Sets a new project file, and rebuild the project config instance
	 * state accordingly.
	 * 
	 * @param projectFile the new project file to set
	 * @throws BadConfigFileException
	 */
	synchronized public void setProjectFile(String projectFile) 
										throws BadConfigFileException 
	{
		
		logger.info("pjectFile="+projectFile+
					" "+projectConfigChangeListeners);
		this.projectFile = projectFile;
		this.properties.clear();
		try {
			this.properties.load(new FileInputStream(projectFile));
		} catch (Throwable e) {
			logger.warn(
					"Exception while loading project file:"+projectFile,
					e);
			return;
		}
		//get root count
		String rootCountString=null;
		try
		{
			rootCountString=properties.getProperty(ROOT_COUNT,"0");
			rootCount= Integer.parseInt(rootCountString);
			if(rootCount==0)
			{
				properties.put(ROOT_COUNT,String.valueOf(rootCount));
			}
				
		}
		catch(Throwable th)
		{
			logger.warn(
					"Cannot parse root count parse property to integer:"+rootCountString,
					th);
		}
		
		testRdfAndRdfsFilesAvailable(properties);
		testRootIndexing(rootCount,properties);
		fireProjectConfigChange();
	}

	/**
	 * tests Whether the project rdf and rdfs files are available
	 * @param properties -- the properties object containing the data
	 * @throws IllegalArgumentException is either rdf or rdfs file is 
	 * 			not available 
	 */
	private static final void testRdfAndRdfsFilesAvailable(
									Properties properties)
									throws IllegalArgumentException
	{
		if(properties==null)
		{
			throw new IllegalArgumentException(
					"Argument properties must not be null");
		}
		if(properties.get(RDF_MODEL_FILE)==null)
		{
			throw new IllegalArgumentException(
					"Property file must contain the rdf model file property:"+
					"\n\tproperty key="+RDF_MODEL_FILE);
		}
		if(properties.get(RDF_SCHEMA_FILE)==null)
		{
			throw new IllegalArgumentException(
					"Property file must contain the rdf model file property:"+
					"\n\tproperty key="+RDF_SCHEMA_FILE);
		}
	}
	
	/**
	 * Test the root indexing is valid; i.e. root count is consistent 
	 * with indexing
	 * @param rootCount
	 * @param properties -- the properties instance containing the project 
	 * 			properties
	 * @throws BadConfigFileException
	 * 			if indexing isnot consistent with root count count
	 * @throws IllegalArgumentException
	 * 			if the pass properties object is null
	 */
	private static final void testRootIndexing(
								int rootCount,
								Properties properties)
								throws BadConfigFileException, IllegalArgumentException
	{
		if(properties==null)
		{
			throw new IllegalArgumentException(
					"Argument propery must not be null");
		}
		int testCount=0;
		String nextRootKey=null;
		String pair[]=null;
		int curIndex=0;
		for(Enumeration e=properties.keys();e.hasMoreElements();)
		{
			nextRootKey=(String)e.nextElement();
			
			if(nextRootKey.startsWith(ROOT_DIR))
			{
				pair=nextRootKey.split("\\.");
				try {
					curIndex=Integer.parseInt(pair[1]);
				} catch (Throwable e1) {
					throw new BadConfigFileException(
							"Bad index format for root:"+
							"\n\trootKeyValue="+Arrays.asList(pair));
				} 
				if(curIndex>=rootCount)
				{
					throw new BadConfigFileException(
							"Root index must not be >="+
							"\n\tindex="+curIndex+
							"\n\trootCount="+rootCount);
				}
				testCount++;
			}
		}
		if(testCount!=rootCount)
		{
			throw new BadConfigFileException(
					"Root count mismatch: "+
					"\n\texpectedCount="+rootCount+
					"\n\tactualCount="+testCount);
		}
	}
	
	/**
	 * @param l -- add a project config change listener if not
	 * allready in the listener ist
	 */
	synchronized public void addProjectConfigChangeListener(
											ProjectConfigChangeListener l)
	{
		if(l==null)
		{
			return;
		}
		logger.info("Adding Listener:"+l);
		if(!projectConfigChangeListeners.contains(l))
		{
			projectConfigChangeListeners.add(l);
		}
	}
		
	/**
	 * Remove the given project change listener from the listener list
	 * @param l
	 */
	synchronized public void removeProjectConfigChangeListener(
											ProjectConfigChangeListener l)
	{
		if(l==null)
		{
			return;
		}
		projectConfigChangeListeners.remove(l);
	}
	
	/**
	 * Fire project config change
	 */
	synchronized public void fireProjectConfigChange()
	{
		//update list 
		roots.clear();
		for(int i=0;i<rootCount;i++)
		{
			try {
				roots.add(
						new URI(getRoot(i)));
			} catch (URISyntaxException e) {
				throw new RuntimeException(e);
			}
		}
		for(Iterator i=projectConfigChangeListeners.iterator();
			i.hasNext();)
		{
			ProjectConfigChangeListener l=
				(ProjectConfigChangeListener)i.next();
			l.projectConfigChanged(this);
		}
	}

	/**
	 * Gets the project property associated with this key
	 * @param key
	 * @return
	 */
	public String getProperty(String key) 
	{
		return properties.getProperty(key);
	}

	/**
	 * Add a new property to the project config
	 * @param key -- the key of the property
	 * @param value -- the prperty value
	 * @return
	 */
	public String put(String key, String value) 
	{
		if(key==null || value==null)
		{
			throw new IllegalArgumentException(
					"Arguments must not be null:"+
					"\n\tkey="+key+
					"\n\tvalue="+value);
		}
		key=key.trim();
		if("".equals(key))
		{
			throw new IllegalArgumentException(
					"Argument key must not be empty");
		}
		if(key.startsWith(ROOT_DIR_PREFIX))
		{
			String rootIndexString=
						key.substring(ROOT_DIR_PREFIX_LENGTH);
			int rootIndex=-1;
			try
			{
				rootIndex=Integer.parseInt(rootIndexString);
			}
			catch(Throwable th)
			{
				throw new IllegalArgumentException(
						"Root key must have an integer as root index:"+key);
			}
			if(rootIndex<rootCount)
			{
				fireProjectConfigChange();
				return (String)properties.put(key,value);
			}
			else if(rootIndex==rootCount)
			{
				rootCount++;
				properties.put(ROOT_COUNT,String.valueOf(rootCount));
				fireProjectConfigChange();
				return (String) properties.put(key,value);
			}
			else 
			{
				throw new IllegalArgumentException(
						"new Root index must be <=actualRootCount:"+
						"\n\tactual root count="+rootCount+
						"\n\tnew root index="+rootIndex);
			}
		}
		else
		{
			fireProjectConfigChange();
			return (String)properties.put(key, value);
		}
	}

	/**
	 * Store a project config into the given output stream as plain text
	 * @param out -- the destination output stream
	 * @param comments -- a comment to store with the project config data
	 * @throws IOException 
	 */
	public void store(
					OutputStream out, 
					String comments) 
					throws IOException 
	{
		properties.store(out, comments);
	}

	/**
	 * Store a project config into the given output stream as plain xml
	 * @param os -- the destination output stream
	 * @param comments -- a comment to store with the project config data
	 * @throws IOException 
	 */
	public void storeToXML(
					OutputStream os, 
					String comment) 
	throws IOException 
	{
		properties.storeToXML(os, comment);
	}
	
	/**
	 * Gets the number of root in the project
	 * @return the number of roots in this system
	 */
	public int getRootCount()
	{
		return rootCount;
	}
	
	/**
	 * Sets the specified root
	 * @param rootIndex -- the index of the root to set
	 * @param root -- the root path as string
	 * @return
	 */
	public String putRoot(int rootIndex, String root) {
		if(root==null)
		{
			throw new IllegalArgumentException(
					"Argument root must not be empty");
		}
		if(rootIndex>rootCount)
		{
			throw new IllegalArgumentException(
					"rootIndex must not be >rootCount"+
					"\n\trootIndex="+rootIndex+
					"\n\trootCount="+rootCount);
		}
		if(rootIndex==rootCount)
		{
			rootCount++;
			properties.put(ROOT_COUNT,String.valueOf(rootCount));
		}
		fireProjectConfigChange();
		return (String)properties.put(ROOT_DIR_PREFIX+rootIndex, root);
	}
	
	/**
	 * Add a  new root to the project
	 * @param root -- the new root to add
	 * @return the added root
	 */
	public String addRoot(String root) 
	{
		if(root==null)
		{
			throw new IllegalArgumentException(
					"Argument root must not be empty");
		}
		properties.put(ROOT_DIR_PREFIX+rootCount, root);
		rootCount++;
		properties.put(ROOT_COUNT,String.valueOf(rootCount));
		fireProjectConfigChange();
		return root;
	}
	
	/**
	 * Get the project root at the specify index
	 * @param rootIndex
	 * @return
	 */
	public String getRoot(int rootIndex)
	{
		if(rootIndex<0 || rootIndex>=rootCount)
		{
			throw new IllegalArgumentException(
					"Argument rootIndex not in range:"+
					"\n\tminValue=0"+
					"\nßtmaxvalue="+(rootCount-1));
		}
		return (String)properties.get(ROOT_DIR_PREFIX+rootIndex);
	}
	
	/**
	 * Creates a new project config file. A project is pecify by the
	 * <ul>
	 * 	<li>the .rdfs file which contains the schema
	 * 	<li>the .rdf file which contains the policy system instances
	 * 	<li>the .conf which contains project data like root count, roots ..
	 * </ul> 
	 * @param projectName -- the name of the project
	 * @param destFolder -- the destination folder to store  the file
	 * @param rootDir -- the first root dir
	 */
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
			
			//properties.setProperty(ROOT_DIR,rootDir.getCanonicalPath());
			addRoot(rootDir.getCanonicalPath());
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
	
	/**
	 * To get a root which is a parent of the provided resource
	 * 
	 * @param resource -- a uri representing the resource
	 * @return the root for this resource or null if no root is found, 
	 * 			which is a parent for the resource 
	 * @throws IllegalArgumentException
	 */
	public URI getRootFor(URI resource) throws IllegalArgumentException
	{
		if(resource==null)
		{
			throw new IllegalArgumentException(
					"Argument resource must not be null");
		}
		
		URI curRoot=null;
		for(Iterator it=roots.iterator();it.hasNext();)
		{
			curRoot=(URI)it.next();
			if(curRoot.relativize(resource).equals(resource))
			{
				return curRoot;
			}
		}
		return null;
		
	}
	
	/**
	 * gets all roots as file
	 * @return
	 */
	public File[] getRoots()
	{
		File[] files= new File[rootCount];
		for(int i=0;i<rootCount;i++)
		{
			files[i]=new File(getRoot(i));
		}
		return files;
	}
	
	/**
	 * Checks whether a given resource is a root
	 * @param resource
	 * @return
	 */
	public boolean isRoot(URI resource)
	{
		if(resource==null)
		{
			throw new IllegalArgumentException(
					"Argument resource must not be null");
		}
		for(int i=0;i<rootCount;i++)
		{
			if(new File(getRoot(i)).toURI().equals(resource))
			{
				return true;
			}
		}
		return false;
	}
}
