package org.peertrust.modeler.policysystem.views;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;

public class PSPolicySystemLabelProvider extends LabelProvider 
{

	private static final Image IMG_OVERRIDDEN = 
     	PlatformUI.getWorkbench()
     		.getSharedImages().getImage(
     				ISharedImages.IMG_TOOL_COPY_DISABLED);
	private static final Image IMG_OVERRIDDER = 
     	PlatformUI.getWorkbench()
     		.getSharedImages().getImage(
     				ISharedImages.IMG_TOOL_COPY_DISABLED);
	
	public PSPolicySystemLabelProvider() 
	{
		super();
	}
	
	 

}
