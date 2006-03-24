/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.io.File;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;

public class FileResourceSelector extends SimpleSelector
{
	/**
	 * 
	 */
	private String root;
	private Logger logger;
	private File identity; 
	
	public FileResourceSelector(File identity)
	{
		super(	(Resource)null,
				PolicySystemRDFModel.PROP_HAS_IDENTITY,
				(String)null);
		logger= Logger.getLogger(FileResourceSelector.class);
		root=
			ProjectConfig.getInstance().getProperty(ProjectConfig.ROOT_DIR);
		if(root==null)
		{
			logger.warn("not rootDir in Config");
		}
		this.identity=identity;
	}
//	public boolean test(Statement arg0) {
//		return false;
//	}

	public boolean isSimple() 
	{
		return false;
	}

	public boolean selects(Statement stm) 
	{
		
		if(identity==null || root==null)
		{
			logger.warn(
					"Root and identity must not be null: root="+
					root+" identity="+identity);
			return false;
		}
		RDFNode rdfNode= stm.getObject();
		if(rdfNode.isLiteral())
		{
			String relPath=rdfNode.toString();
			File file;
			if(((Resource)
					stm.getSubject()).hasProperty(
							PolicySystemRDFModel.PROP_HAS_SUPER))
			{
				file=new File(root,relPath);
			}
			else
			{
				file= new File(relPath);
			}
			boolean comp=file.getAbsolutePath().equals(identity.getAbsolutePath());
			logger.info("Checking:"+
						"\n\tstm.....="+stm+
						"\n\tfile....="+file+
						"\n\trelPath.="+relPath+
						"\n\tidentity="+identity+
						"\n\tcmp.....="+comp);
			return comp;
		}
		else
		{
			logger.warn("Object not a literal:"+rdfNode);
			return false;
		}
	}
}