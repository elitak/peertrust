/**
 * 
 */
package unittest.model;

import java.io.File;

import org.peertrust.modeler.policysystem.model.ProjectConfig;
import org.peertrust.modeler.policysystem.model.exceptions.BadConfigFileException;

import junit.framework.TestCase;

/**
 * @author pat_dev
 *
 */
public class ConfigFileTest extends TestCase {
	static final public String CF_1_ROOT_CONF=
							"test_data/test_config_files/cf_1_root.conf";
	static final public String CF_0_ROOT_CONF=
						"test_data/test_config_files/cf_0_root.conf";
	//static final public File CURRENT_DIR= new File("").getAbsoluteFile();
	/**
	 * 
	 */
	public ConfigFileTest() {
		super();
	}

	/**
	 * @param arg0
	 */
	public ConfigFileTest(String arg0) {
		super(arg0);
	}
	
	public void testLoad_CF_1_CONF()
	{
		ProjectConfig config=ProjectConfig.getInstance();
		//config.setProjectFile();
		String configFile= new File(CF_1_ROOT_CONF).getAbsoluteFile().toString();
		try {
			config.setProjectFile(configFile);
		} catch (BadConfigFileException e) {
			fail("No Exception expected while loading:"+configFile+
					"\n\tmessage="+e.getMessage());
		}
		assertEquals("cf_1_root.conf contains 1 root",1,config.getRootCount());
		assertNotNull(
				"cf_1_root.conf must contain this property:"+ProjectConfig.RDF_SCHEMA_FILE,
				config.getProperty(ProjectConfig.RDF_SCHEMA_FILE));
		assertNotNull(
				"cf_1_root.conf must contain this property:"+ProjectConfig.RDF_MODEL_FILE,
				config.getProperty(ProjectConfig.RDF_MODEL_FILE));
		
		try
		{
			config.put("rootDir.3","/");
			fail("Exception expected since next rooDir index is 1");
		}
		catch(IllegalArgumentException e)
		{
			//empty
		}
		int count=config.getRootCount();
		config.addRoot("/root");
		assertEquals("","/root",config.getRoot(count));
		try
		{
			config.getRoot(count+1);
			fail("Config does not contains rootDir."+(count+1));
		}
		catch(IllegalArgumentException e)
		{
			//empty
		}
	}
	
	public void testLoad_CF_0_CONF()
	{
		ProjectConfig config=ProjectConfig.getInstance();
		//config.setProjectFile();
		String configFile= new File(CF_0_ROOT_CONF).getAbsoluteFile().toString();
		try {
			config.setProjectFile(configFile);
		} catch (BadConfigFileException e) {
			fail("No Exception expected while loading:"+configFile+
					"\n\tmessage="+e.getMessage());
		}
		assertEquals("cf_0_root.conf contains 1 root",0,config.getRootCount());
		assertNotNull(
				"cf_0_root.conf must contain this property:"+ProjectConfig.RDF_SCHEMA_FILE,
				config.getProperty(ProjectConfig.RDF_SCHEMA_FILE));
		assertNotNull(
				"cf_0_root.conf must contain this property:"+ProjectConfig.RDF_MODEL_FILE,
				config.getProperty(ProjectConfig.RDF_MODEL_FILE));
	}
}
