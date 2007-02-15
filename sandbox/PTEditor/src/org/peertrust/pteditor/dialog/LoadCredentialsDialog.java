package org.peertrust.pteditor.dialog;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.LinkedList;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.peertrust.pteditor.model.ICredentialLoader;
import org.peertrust.pteditor.model.MyResource;

public class LoadCredentialsDialog extends Dialog {
	private Button buttonChooseFile=null;
	private List listCredentials=null;
	private java.util.List<MyResource> listResources=new LinkedList<MyResource>();

	private ICredentialLoader loader=new ICredentialLoader() {

		public java.util.List<MyResource> loadCredentials(Object[] obj) {
			java.util.List<MyResource> list=new LinkedList<MyResource>();
			if((obj==null)||(obj.length==0)||(!(obj[0] instanceof String)))
				return list;
			try {
				BufferedReader br=new BufferedReader(new FileReader((String)obj[0]));
				String str;
				while((str=br.readLine())!=null)
					list.add(new MyResource(str,null));
				br.close();
			}
			catch(Exception e) {
			}
			return list;
		}
		
	};

	private SelectionListener selectionlistener=new SelectionListener() {

		public void widgetDefaultSelected(SelectionEvent e) {
			if(e.getSource().equals(buttonChooseFile)) {
				FileDialog dialog=new FileDialog(buttonChooseFile.getShell());
				String file=dialog.open();
				if(file!=null)
					processFile(file);
			}
		}

		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
		
	};
	
	public LoadCredentialsDialog(Shell shell) {
		super(shell);
	}

	protected Control createDialogArea(Composite parent) {
		Composite composite=new Composite(parent,SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		buttonChooseFile=new Button(composite,SWT.NONE);
		buttonChooseFile.setText("Choose File");
		buttonChooseFile.addSelectionListener(selectionlistener);
		buttonChooseFile.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label label=new Label(composite,SWT.NONE);
		label.setText("Credentials found");
		label.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		listCredentials=new List(composite,SWT.SINGLE|SWT.V_SCROLL|SWT.H_SCROLL);
		listCredentials.setLayoutData(new GridData(GridData.FILL_BOTH));
		return composite;
	}
	
	private void processFile(String file) {
		listResources.clear();
		listResources=loader.loadCredentials(new Object[]{file});
		listCredentials.removeAll();
		for(int i=0;i<listResources.size();i++)
			listCredentials.add(listResources.get(i).toString());
	}
	
	public MyResource[] getCredentials() {
		return (MyResource[])listResources.toArray(new MyResource[0]);
	}
}
