/**
 * 
 */
package org.peertrust.modeler.policysystem.control;

import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;

/**
 * @author pat_dev
 *
 */
public class PSPolicyEditDialog extends TitleAreaDialog 
{
	PSPolicyEditorControl editorControl;
	/**
	 * 
	 * @param parentShell
	 */
	public PSPolicyEditDialog(Shell parentShell) 
	{
		super(parentShell);
		editorControl= new PSPolicyEditorControl(false);
	}

	protected Control createContents(Composite parent) {
		
		return super.createContents(parent);
	}

	protected Control createDialogArea(Composite parent) 
	{
		Composite compo=(Composite)super.createDialogArea(parent);
		editorControl.createControl(compo);		
		return compo;
		
	}

	protected void cancelPressed() 
	{
		super.cancelPressed();
	}

	
	protected void okPressed() 
	{
		int ret=editorControl.saveEdit();
		if(ret!=PSPolicyEditorControl.SAVE_RESULT_OK)
		{
			///failure
		}
		super.okPressed();
	}

	
}
