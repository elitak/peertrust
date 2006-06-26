/**
 * 
 */
package org.peertrust.modeler.policysystem.gui.control;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.BooleanFieldEditor;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;




/**
 * @author pat_dev
 *
 */
public class NewPSResourceEditorPage extends WizardPage {
	/**
	 * Logger for the <code>NewPSResourceEditorPage</code>
	 */
	final static private Logger logger= 
			Logger.getLogger(NewPSResourceEditorPage.class);
	
	/**
	 * Editor for new resource name
	 */
	private StringFieldEditor nameEditor;
	
	/**
	 * Editor for the new resource canHaveChild property
	 */
	private BooleanFieldEditor canHaveChildrenEditor;
	
	
//	/**
//	 * Container composite
//	 */
//	private Composite top;
	
	/**
	 * 
	 */
	public NewPSResourceEditorPage(String name) {
		super(name);
	}

	/**
	 * @see org.eclipse.ui.part.Page#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) {
		Composite top= new Composite(parent,SWT.FILL);
		top.setLayout(new GridLayout());
		
		nameEditor= new StringFieldEditor("New Resource Name","New Resource Name",top);
		canHaveChildrenEditor= 
			new BooleanFieldEditor("canHaveChildrenEditor","Can have children",top);
		super.setControl(top);
	}

//	/**
//	 * @see org.eclipse.ui.part.Page#getControl()
//	 */
//	public Control getControl() {
//		return top;
//	}

	/**
	 * @see org.eclipse.ui.part.Page#setFocus()
	 */
	public void setFocus() {
		//empty
	}
	
	/**
	 * Help create a new resource by collecting the new resource name and 
	 * canHaveChildProperty using a wizard dialog
	 * @param shell -- the parent shell for the  
	 * @return an array containing the new resource property:
	 * 		<ul>
	 * 			<li/>array[0] == name as </code>String</code>
	 * 			<li/>array[1] == canHaveChild as </code>Boolean</code><
	 * 		</ul> 
	 */
	public static Object[] helpCreateNewResource(Shell  shell)
	{
		//DialogPage p;
		final Object resourceData[]={null,null};
		Wizard wiz=new Wizard()
		{
			//private Object selected;
			public boolean performFinish() 
			{
				try {
					NewPSResourceEditorPage editorPage=
						(NewPSResourceEditorPage)this.getPages()[0];
					resourceData[0]=
						editorPage.nameEditor.getStringValue();
					resourceData[1]=
						new Boolean(editorPage.canHaveChildrenEditor.getBooleanValue());
					return true;
				} catch (Throwable th) {
					logger.warn("Exception while getting new resource data",th);
					return false;
				}
			}
		};
		 
		NewPSResourceEditorPage page=
			new NewPSResourceEditorPage("New resource Editor");
		wiz.addPage(page);
		WizardDialog dlg=
			new WizardDialog(
					shell,
					wiz);
		dlg.create();
		dlg.setTitle("Create new PSResource");
		dlg.setMessage("Set new resource name and canHaveChildProperty");
		int ret=dlg.open();
		if(ret==WizardDialog.OK)
		{
			return resourceData;
		}
		else
		{
			return null;
		}
	}

}
