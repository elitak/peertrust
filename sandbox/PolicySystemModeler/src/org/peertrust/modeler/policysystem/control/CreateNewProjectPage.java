package org.peertrust.modeler.policysystem.control;



import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.Page;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;


public class CreateNewProjectPage extends WizardPage 
{
	

	private Composite top;
	private PSFilter psFilter;
	private StringFieldEditor newProjectNameSFE;
	private StringButtonFieldEditor newProjectDestFolderSFE;
	private StringButtonFieldEditor policySystemFolder;
	
	public CreateNewProjectPage(String pageName) 
	{
		super(pageName);
	}
	
	public void createControl(Composite parent) 
	{
		top = new Composite(parent, SWT.LEFT);
		setControl(top);
		
		newProjectNameSFE=
			new StringFieldEditor("labelFieldEditor","Project Name",top);
		Label headerdd= new Label(top,SWT.NONE);	
		
		newProjectDestFolderSFE=
			new StringButtonFieldEditor("valueFieldEditor","Folder",top);
		newProjectDestFolderSFE.setFieldValueProvider(
				new FolderNameProvider());
		policySystemFolder=
			new StringButtonFieldEditor("PolicySystemFolder","Based Folder",top);
		policySystemFolder.setFieldValueProvider(
				new FolderNameProvider());
	}

	public Control getControl() 
	{
		return top;
	}

	public void setFocus() 
	{
		
	}


	
	
	public String getNewProjectName()
	{
		return newProjectNameSFE.getStringValue();
	}
	
	public String getNewProjectFolder()
	{
		return newProjectDestFolderSFE.getStringValue();
	}
	
	public String getPolicySystemBaseFolder()
	{
		return policySystemFolder.getStringValue();
	}
	
	
	class FolderNameProvider implements FieldValueProvider
	{

		public String getFieldValue() {
			IWorkbench wb=PlatformUI.getWorkbench();
	  		Shell shell=
	  			wb.getActiveWorkbenchWindow().getShell();
	  	    DirectoryDialog dlg = 
		    	new DirectoryDialog(shell,SWT.OPEN);
			String fileName = dlg.open();
			return fileName;
		}
		
	}
	
}
