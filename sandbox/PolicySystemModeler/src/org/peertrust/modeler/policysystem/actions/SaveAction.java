/**
 * 
 */
package org.peertrust.modeler.policysystem.actions;

import java.io.File;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.ProjectConfig;
import org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEvent;
import org.peertrust.modeler.policysystem.model.abtract.PSModelChangeEventListener;


/**
 * The workbench action to save the current policy system project
 * 
 * @author Patrice Congo
 *
 */
public class SaveAction 
		extends Action
		implements PSModelChangeEventListener
{
	private PolicySystemRDFModel psModel;
	
	/** the default action id*/
	final static String DEFAULT_ID="SaveAction";
	
	/**
   * OpenAction constructor
   */
  public SaveAction() {
    super("&Save", //"&Open...@Ctrl+O", 
    		PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
  	    			ISharedImages.IMG_TOOL_UP));
    
    ImageDescriptor id=
    	PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
    			ISharedImages.IMG_OBJ_FOLDER);
    setDisabledImageDescriptor(id);
    
    setToolTipText("Save");
    super.setId(DEFAULT_ID);
    this.setEnabled(false);
    psModel=PolicySystemRDFModel.getInstance();
    psModel.addPSModelChangeEventListener(this);
  }

  /**
   * saves the rdf model
   * @see Action#run()
   */
  public void run() {
    try {
		// Use the file dialog
		 ProjectConfig pConfig= ProjectConfig.getInstance();
		String rdfModelFile=  
			pConfig.getProperty(ProjectConfig.RDF_MODEL_FILE);
		if(rdfModelFile==null)
		{
			return;
		}
		File saveTmp= new File(rdfModelFile+".tmp");
		
		saveTmp.createNewFile();
		//PolicySystemRDFModel.getInstance()
		psModel.saveTo(saveTmp.getCanonicalPath());
		File old= new File(rdfModelFile);
		old.renameTo(new File(rdfModelFile+".old"));
		saveTmp.renameTo(new File(rdfModelFile));
		setEnabled(false);
	} 
    catch (Exception e) 
	{
		e.printStackTrace();
	}
  }
  
  private void registerForModelChange()
  {
	  //PolicySystemRDFModel.getInstance()
	  psModel.addPSModelChangeEventListener(this);
	  
  }

	/**
	 * @see PSModelChangeEventListener#onPSModelChange(PSModelChangeEvent) 
	 */
	public void onPSModelChange(PSModelChangeEvent event) {
		this.setEnabled(true);
	}
}
