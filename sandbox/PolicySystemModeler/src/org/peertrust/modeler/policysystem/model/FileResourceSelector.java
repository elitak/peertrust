/**
 * 
 */
package org.peertrust.modeler.policysystem.model;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;

public class FileResourceSelector extends SimpleSelector
{
	
	
//	final static private Logger logger= 
//				Logger.getLogger(FileResourceSelector.class);
	
//	/**
//	 * A URI representing the resource
//	 */
//	private URI identity; 
	
//	/**
//	 * The root for the resource to select
//	 */
//	private URI root;
	
	/**
	 * Create a selector which selects the identity
	 * @param identity
	 */
	public FileResourceSelector(URI identity)
	{
		super(	(Resource)null,
				PolicySystemRDFModel.PROP_HAS_IDENTITY,
				(String)identity.toString()
				);
		//ProjectConfig projectConfig=ProjectConfig.getInstance();
		//test
		if(identity==null)
		{
			throw new IllegalArgumentException(
					"Argument identity must not be null");
		}
		
//		try {
//			//TODO use udi
//			root=
//				ProjectConfig.getInstance().getRootFor(identity);//new URI(ProjectConfig.getInstance().getProperty(ProjectConfig.ROOT_DIR));
//		} catch (Throwable e) {
//			throw new RuntimeException(e);
//		}
//		if(root==null)
//		{
//			logger.warn("not rootDir in Config");
//		}
//		this.identity=identity;
		
	}
//	public boolean test(PSModelStatement arg0) {
//		return false;
//	}

	public boolean isSimple() 
	{
		return false;
	}

	public boolean selects(Statement stm) 
	{
		return super.selects(stm);
//		if(identity==null /*|| root==null*/)
//		{
//			logger.warn(
//					"identity must not be null:");
//			return false;
//		}
//		
//		RDFNode rdfNode= stm.getObject();
//		if(rdfNode.isLiteral())
//		{
//			String relPath=rdfNode.toString();
//			URI identityUri;
//			if(((Resource)
//					stm.getSubject()).hasProperty(
//							PolicySystemRDFModel.PROP_HAS_SUPER))
//			{//not a root
//				identityUri=root.resolve(relPath);
//			}
//			else
//			{
//				try {
//					identityUri= new URI(relPath);
//				} catch (URISyntaxException e) {
//					throw new RuntimeException(e);
//				}
//			}
//			//boolean comp=file.getAbsolutePath().equals(identity.getAbsolutePath());
//			boolean comp=identityUri.equals(identity);//relPath.equals(new File(identity).getAbsolutePath());
//			logger.info("Checking:"+
//						"\n\tstm.....="+stm+
//						"\n\tfile....="+identityUri+
//						"\n\trelPath.="+relPath+
//						"\n\tidentity="+identity+
//						"\n\tcmp.....="+comp);
//			return comp;
//		}
//		else
//		{
//			logger.warn("Object not a literal:"+rdfNode);
//			return false;
//		}
	}
	
	static public void main(String[] uri) throws MalformedURLException
	{
		File curDir= new File("").getAbsoluteFile();
		URI curUri=curDir.toURI();
		URI parent=curDir.getParentFile().toURI();
		URI relUri=parent.relativize(curUri);
		System.out.println("cururi:"+curUri);
		System.out.println("relUri:"+parent.resolve(relUri.toString()));
		System.out.println("relUriw:"+parent.relativize(parent));
	}
}