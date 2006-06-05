/**
 * 
 */
package org.peertrust.modeler.policysystem.gui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.ProjectConfig;


public class OpenAction extends Action {
    	final static String DEFAULT_ID="OpenAction";
    	  /**
    	   * OpenAction constructor
    	   */
    	  public OpenAction() 
    	  {
    	   	  super("&Open", //"&Open...@Ctrl+O", 
//    	    		ImageDescriptor.createFromFile(
//    	    				OpenAction.class, "/images/open.gif")
    	    		PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
        	    			ISharedImages.IMG_OBJ_FOLDER));
    	    
//    	    setDisabledImageDescriptor(ImageDescriptor.createFromFile(
//    	        OpenAction.class, "/images/disabledOpen.gif"));
    	    ImageDescriptor id=
    	    	PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(
    	    			ISharedImages.IMG_OBJ_FOLDER);
    	    setDisabledImageDescriptor(id);
    	    
    	    setToolTipText("Open");
    	    setId(DEFAULT_ID);
    	  }

    	  /**
    	   * Opens an existing file
    	   */
    	  public void run() {
    	    // Use the file dialog
    		IWorkbench wb=PlatformUI.getWorkbench();
    		Shell shell=
    			wb.getActiveWorkbenchWindow().getShell();
    	    FileDialog dlg = 
    	    	new FileDialog(shell,SWT.OPEN);
			String fileName = dlg.open();
			if (fileName != null) {
				PolicySystemRDFModel.getInstance().clearRDFModel();
				ProjectConfig.getInstance().setProjectFile(fileName);				
			}
    	  }
    	}