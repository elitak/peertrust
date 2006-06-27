package org.peertrust.modeler.policysystem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.PropertyConfigurator;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.*;
//import org.eclipse.core.runtime.IPath;
//import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
//import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

//import com.tools.logging.LoggingPlugin;
//import com.tools.logging.PluginLogManager;

/**
 * The main plugin class to be used in the desktop.
 */
public class PolicysystemPlugin extends AbstractUIPlugin {

	//The shared instance.
	private static PolicysystemPlugin plugin;
	
	private static final String LOG_PROPERTIES_FILE = 
										"logger.properties";
	private static final String RDFS_FILE="/model/schema.rdfs";
	private static final String RDF_FILE="/model/empty.rdf";
	
	public static final String IMG_KEY_POLICY="IMG_KEY_POLICY";
	public static final String IMG_PATH_POLICY="/icons/policy.gif";
	
	public static final String IMG_KEY_OVERRIDDEN_POLICY=
										"IMG_KEY_OVERRIDDEN_POLICY";
	public static final String IMG_PATH_OVERRIDDEN_POLICY=
										"/icons/overridden_policy.gif";
	
	public static final String IMG_KEY_ORULE="IMG_KEY_ORULE";
	public static final String IMG_PATH_ORULE="/icons/orule.gif";
	
	public static final String IMG_KEY_FILTER="IMG_KEY_FILTER";
	public static final String IMG_PATH_FILTER="/icons/filter.gif";
	
	public static final String IMG_KEY_RESOURCE="IMG_KEY_REOURCE";
	public static final String IMG_PATH_RESOURCE="/icons/resource.gif";
	
	//private PluginLogManager logManager;
	/**
	 * The constructor.
	 */
	public PolicysystemPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception 
	{
		super.start(context);
		try 
		{
			configure();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception 
	{
		super.stop(context);
//		if (this.logManager != null) {
//			this.logManager.shutdown();
//			this.logManager = null;
//		}
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static PolicysystemPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		PolicysystemPlugin.getDefault().getWorkbench();
		return AbstractUIPlugin.imageDescriptorFromPlugin("org.peertrust.modeler.policysystem", path);
	}
	
	private void configure()
	{
		File configFile = new File(
				Platform.getInstanceLocation().getURL().getPath(),
				LOG_PROPERTIES_FILE);;
		
		// Configure logging		
		if (configFile.exists() == false)
		{
//			log.info("File " + System.getProperty("user.dir") + File.separator + 
//					logConfig + " does not exist") ;
			BasicConfigurator.configure();
//			log.info("Log4j configured based on BasicConfigurator") ;
		}
		else
		{
			try {
				Properties props = new Properties();
				FileInputStream iStream= 
					new FileInputStream(configFile);
				props.load(iStream);
				PropertyConfigurator.configure(props) ;
				//log.info("Log4j configured based on file \"" + logConfig + "\"") ;
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
//	/**
//	 * To configure the logger
//	 *
//	 */
//	private void configure_old() 
//	{
//	      
//		  try {
//			  File logFile= 
//					new File(
//							Platform.getInstanceLocation().getURL().getPath(),
//							LOG_PROPERTIES_FILE);
//			  
//		  	URL url = logFile.toURL();//getBundle().getEntry("/" + LOG_PROPERTIES_FILE);
//		  	if(url==null)
//		  	{
//		  		return;
//		  	}
//		  	
//		  	System.out.println("logFile:"+url);
//		   	InputStream propertiesInputStream = url.openStream();
//		    if (propertiesInputStream != null) {
//		    	Properties props = new Properties();
//		    	props.load(propertiesInputStream);
//				propertiesInputStream.close();
//				if(props.size()>0)
//				{
//					System.out.println(
//							"props:"+props+
//							" Loginplugin:"+
//							LoggingPlugin.getDefault());
//					
//					this.logManager = 
//						new PluginLogManager(this, props);
//					this.logManager.hookPlugin(
//							 getBundle().getSymbolicName(),
//							 getLog());
//				}
//		   	}	
//		  } 
//		  catch (Exception e) {
//			  e.printStackTrace();
////		  	String message = "Error while initializing log properties." + 
////		  	                 e.getMessage();
////			IStatus status = new Status(IStatus.ERROR,
////					getDefault().getBundle().getSymbolicName(),
////					IStatus.ERROR, message, e);
////			getLog().log(status);
////		  	throw new RuntimeException(
////		  	     "Error while initializing log properties.",e);
//		  }         
//	}//end configure_old
	
//	private void configureLogger(String pathConfigfile) 
//	{
//		if(pathConfigfile==null)
//		{
//			return;
//		}		
//		File configFile= new File(pathConfigfile);
//		if(!configFile.exists())
//		{
//			return;
//		}
//		
//		if(logManager!=null)
//		{
//			this.logManager.shutdown();
//			this.logManager = null;
//		}
//		try {
//			  
//		  	URL url = getBundle().getEntry("/" + LOG_PROPERTIES_FILE);
//		  	if(url==null)
//		  	{
//		  		return;
//		  	}
//		  	
//		   	InputStream propertiesInputStream = url.openStream();
//		    if (propertiesInputStream != null) {
//		    	Properties props = new Properties();
//		    	props.load(propertiesInputStream);
//				propertiesInputStream.close();
//				this.logManager = new PluginLogManager(this, props);
//				this.logManager.hookPlugin(
//				 this.getDefault().getBundle().getSymbolicName(),
//				 this.getDefault().getLog()); 
//		   	}	
//		 } 
//		 catch (Exception e) 
//		 {
//		  	String message = "Error while initializing log properties." + 
//		                 e.getMessage();
//			IStatus status = new Status(IStatus.ERROR,
//					getDefault().getBundle().getSymbolicName(),
//					IStatus.ERROR, message, e);
//			getLog().log(status);
//			throw new RuntimeException(
//			     "Error while initializing log properties.",e);
//		  }         
//		}//end configureLogger
	
	public void showMessage(final String message)
	{

		plugin.getWorkbench().getDisplay().syncExec(
		        new Runnable() {
		           public void run(){
		        	   Shell shell=plugin.getWorkbench().getDisplay().getActiveShell();
		       		MessageDialog.openInformation(shell,"Message",message);
		           }
		        }
		     );
	}
	
	public boolean askQuestion(final String message)
	{

		
		Shell shell=plugin.getWorkbench().getDisplay().getActiveShell();
		return MessageDialog.openConfirm(shell,"Message",message);
		
	}
	
	public void showException(String message, Throwable th)
	{
		StringWriter sw= new StringWriter();
		PrintWriter pw= new PrintWriter(sw);
		pw.print(message);
		pw.print("\n=======================================\n");
		th.printStackTrace(pw);
		this.showMessage(sw.getBuffer().toString());
	}
	
	public void copyModelfilesTo(File rdfschemaFile,File rdfModelFile)
	{
		try {
			//copy schema
			URL schemaURL= this.getBundle().getEntry(RDFS_FILE);
			InputStream iStream=schemaURL.openStream();
			FileOutputStream fOut= 
				new FileOutputStream(rdfschemaFile);
			//BufferedInputStream i;
			byte bytes[]= new byte[512];
			int read=-1;
			while((read=iStream.read(bytes,0,512))!=-1)
			{
				fOut.write(bytes,0,read);
			}
		} catch (Exception e) {
			showException("Exception while Copying rdfs schema",e);
		}
		
		try {
			//copy schema
			URL modelURL= this.getBundle().getEntry(RDF_FILE);
			InputStream iStream=modelURL.openStream();
			FileOutputStream fOut= 
				new FileOutputStream(rdfModelFile);
			//BufferedInputStream i;
			byte bytes[]= new byte[512];
			int read=-1;
			while((read=iStream.read(bytes,0,512))!=-1)
			{
				fOut.write(bytes,0,read);
			}
		} catch (Exception e) {
			showException("Exception while Copying rdf model file",e);
		}
	}

	/**
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#createImageRegistry()
	 */
	//protected ImageRegistry createImageRegistry()
	protected void initializeImageRegistry(ImageRegistry reg)
	{
		String couples[][]={
				{IMG_KEY_FILTER,IMG_PATH_FILTER},
				{IMG_KEY_ORULE,IMG_PATH_ORULE},
				{IMG_KEY_OVERRIDDEN_POLICY,IMG_PATH_OVERRIDDEN_POLICY},
				{IMG_KEY_POLICY,IMG_PATH_POLICY},
				{IMG_KEY_RESOURCE,IMG_PATH_RESOURCE},
		};
		
		//ImageRegistry registry=super.createImageRegistry();
//		Bundle bundle = this.getBundle();//Platform.getBundle(PLUGIN_ID);
		
//		IPath path;
		String curCouple[];
		for(int i=0; i<couples.length;i++)
		{
			curCouple=couples[i];
			//System.out.println("loading:"+curCouple[0]+"="+curCouple[1]);
//			path= new Path(curCouple[1]);
			URL url = getBundle().getEntry(curCouple[1]);//Platform.find(bundle, path);
			System.out.println("loading:"+curCouple[0]+"="+curCouple[1]+ " url="+url);
			ImageDescriptor desc = ImageDescriptor.createFromURL(url);
			reg.put(curCouple[0], desc);
		}
		return;// registry;
	}
	
	
}
