package policysystem;

import java.io.File;
import java.io.InputStream;
import java.net.URL;
import java.util.Properties;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.plugin.*;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

import com.tools.logging.PluginLogManager;

/**
 * The main plugin class to be used in the desktop.
 */
public class PolicysystemPlugin extends AbstractUIPlugin {

	//The shared instance.
	private static PolicysystemPlugin plugin;
	
	private static final String LOG_PROPERTIES_FILE = "logger.properties";
	
	private PluginLogManager logManager;
	/**
	 * The constructor.
	 */
	public PolicysystemPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		try {
			configure();
		} catch (Exception e) {
			
		}
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		if (this.logManager != null) {
			this.logManager.shutdown();
			this.logManager = null;
		}
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
		return AbstractUIPlugin.imageDescriptorFromPlugin("policysystem", path);
	}
	
	/**
	 * To configure the logger
	 *
	 */
	private void configure() {
	      
		  try {
		  	URL url = getBundle().getEntry("/" + LOG_PROPERTIES_FILE);
		  	if(url==null)
		  	{
		  		return;
		  	}
		  	
		   	InputStream propertiesInputStream = url.openStream();
		    if (propertiesInputStream != null) {
		    	Properties props = new Properties();
		    	props.load(propertiesInputStream);
				propertiesInputStream.close();
				if(props.size()>0)
				{
					this.logManager = new PluginLogManager(this, props);
					this.logManager.hookPlugin(
					 this.getDefault().getBundle().getSymbolicName(),
					 this.getDefault().getLog());
				}
		   	}	
		  } 
		  catch (Exception e) {
		  	String message = "Error while initializing log properties." + 
		  	                 e.getMessage();
			IStatus status = new Status(IStatus.ERROR,
					getDefault().getBundle().getSymbolicName(),
					IStatus.ERROR, message, e);
			getLog().log(status);
		  	throw new RuntimeException(
		  	     "Error while initializing log properties.",e);
		  }         
	}
	
	private void configureLogger(String pathConfigfile) 
	{
		if(pathConfigfile==null)
		{
			return;
		}		
		File configFile= new File(pathConfigfile);
		if(!configFile.exists())
		{
			return;
		}
		
		if(logManager!=null)
		{
			this.logManager.shutdown();
			this.logManager = null;
		}
		try {
			  
		  	URL url = getBundle().getEntry("/" + LOG_PROPERTIES_FILE);
		  	if(url==null)
		  	{
		  		return;
		  	}
		  	
		   	InputStream propertiesInputStream = url.openStream();
		    if (propertiesInputStream != null) {
		    	Properties props = new Properties();
		    	props.load(propertiesInputStream);
				propertiesInputStream.close();
				this.logManager = new PluginLogManager(this, props);
				this.logManager.hookPlugin(
				 this.getDefault().getBundle().getSymbolicName(),
				 this.getDefault().getLog()); 
		   	}	
		 } 
		 catch (Exception e) 
		 {
		  	String message = "Error while initializing log properties." + 
		                 e.getMessage();
			IStatus status = new Status(IStatus.ERROR,
					getDefault().getBundle().getSymbolicName(),
					IStatus.ERROR, message, e);
			getLog().log(status);
			throw new RuntimeException(
			     "Error while initializing log properties.",e);
		  }         
		}
	
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
}
