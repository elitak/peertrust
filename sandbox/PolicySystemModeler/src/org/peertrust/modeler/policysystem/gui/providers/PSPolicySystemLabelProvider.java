package org.peertrust.modeler.policysystem.gui.providers;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.peertrust.modeler.policysystem.PolicysystemPlugin;
import org.peertrust.modeler.policysystem.gui.control.PSOverriddingRuleEditControl;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;

public class PSPolicySystemLabelProvider extends LabelProvider 
{

//	private static final Image IMG_OVERRIDDEN = 
//     	PlatformUI.getWorkbench()
//     		.getSharedImages().getImage(
//     				ISharedImages.IMG_TOOL_COPY_DISABLED);
//	private static final Image IMG_OVERRIDDER = 
//     	PlatformUI.getWorkbench()
//     		.getSharedImages().getImage(
//     				ISharedImages.IMG_TOOL_COPY_DISABLED);
	private ImageRegistry imageRegistry;
	public PSPolicySystemLabelProvider() 
	{
		super();
		this.imageRegistry=PolicysystemPlugin.getDefault().getImageRegistry();
	}
	/**
	 * @see org.eclipse.jface.viewers.LabelProvider#getImage(java.lang.Object)
	 */
	public Image getImage(Object element) 
	{
		
		if(element == null)
		{
			return null;
		}
		
		Image img;
		if(element instanceof PSPolicy)
		{
			String role=
				((PSPolicy)element).getRole();
			if(role==null)
			{
				img= imageRegistry.get(PolicysystemPlugin.IMG_KEY_POLICY);
			}
			else if(role.equals(PSPolicy.ROLE_ORULE_OVERRIDDEN))
			{
				img= imageRegistry.get(PolicysystemPlugin.IMG_KEY_OVERRIDDEN_POLICY);
			}
			else
			{
				img= imageRegistry.get(PolicysystemPlugin.IMG_KEY_POLICY);
			}
		}
		else if(element instanceof PSOverridingRule)
		{
			img= imageRegistry.get(PolicysystemPlugin.IMG_KEY_ORULE);
		}
		else if(element instanceof PSResource)
		{
			img = imageRegistry.get(PolicysystemPlugin.IMG_KEY_RESOURCE);
		}
		else
		{
			System.out.println("ERROR");
			img=null;
		}
		System.out.println("getImage for:"+element.getClass()+ " img="+img);
		return img;
	}
	/**
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	public String getText(Object element) 
	{
		return super.getText(element);
	}
	
	
	
	 

}
