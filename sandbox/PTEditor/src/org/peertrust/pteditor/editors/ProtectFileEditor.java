package org.peertrust.pteditor.editors;

import java.util.LinkedList;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IEditorSite;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.part.EditorPart;
import org.peertrust.pteditor.dialog.MyResourceChooserDialog;
import org.peertrust.pteditor.model.MyPolicy;
import org.peertrust.pteditor.model.MyResource;

public class ProtectFileEditor extends EditorPart  {
	public static final String ID="PTEditor.editor.editFileProtect";
	
	private Text textPolicy=null;
	private Text textFilter=null;
	private ListViewer listviewerExcRes=null;
	private List listOverridePolicies=null;
	private Button buttonNewExcRes=null;
	private Button buttonRemoveExcRes=null;
	private Button buttonDefaultPolicy=null;
	private Button buttonMandatoryPolicy=null;
	private Button buttonOK=null;
	private Button buttonCancel=null;
	private boolean bDirty=false;
	
	private ModifyListener listenerModify=new ModifyListener() {
		public void modifyText(ModifyEvent e) {
			bDirty=true;
			firePropertyChange(PROP_DIRTY);
		}
	};
	
	private SelectionListener listenerSelection=new SelectionListener() {

		public void widgetDefaultSelected(SelectionEvent e) {
			if(e.getSource().equals(buttonNewExcRes)) {
				String strExtensions[]=textFilter.getText().split(";");
				MyResourceChooserDialog dialog=new MyResourceChooserDialog(
					buttonNewExcRes.getShell(),getInput().getResource(),strExtensions);
				dialog.open();
				if(dialog.getValue()!=null) {
					listviewerExcRes.add(dialog.getValue());
					bDirty=true;
					firePropertyChange(PROP_DIRTY);
				}
//				FileDialog filedialog=new FileDialog(buttonNewExcRes.getShell(),
//					SWT.OPEN);
//				String strExtensions[]=textFilter.getText().split(";");
//				for(int i=0;i<strExtensions.length;i++)
//					if(strExtensions[i].startsWith("."))
//						strExtensions[i]="*"+strExtensions[i];
//				filedialog.setFilterPath(getInput().getDirectoryName());
//				filedialog.setFilterExtensions(strExtensions);
//				String strFileChosen=filedialog.open();
//				if(strFileChosen==null)
//					return;
//				listExcRes.add(strFileChosen);
//				bDirty=true;
//				firePropertyChange(PROP_DIRTY);
			}
			else if(e.getSource().equals(buttonRemoveExcRes)) {
				if(!(listviewerExcRes.getSelection() instanceof IStructuredSelection))
					return;
				IStructuredSelection sel=(IStructuredSelection)listviewerExcRes.getSelection();
				if((sel.getFirstElement()==null)||(!(sel.getFirstElement() instanceof MyResource)))
					return;
				listviewerExcRes.remove(sel.getFirstElement());
				bDirty=true;
				firePropertyChange(PROP_DIRTY);
			}
			else if(e.getSource().equals(buttonOK)) {
				doSave(null);
				getEditorSite().getPage().closeEditor(getEditorSite().getPage().findEditor(getInput()),false);
			}
			else if(e.getSource().equals(buttonCancel))
				getEditorSite().getPage().closeEditor(getEditorSite().getPage().findEditor(getInput()),false);
			else {
				bDirty=true;
				firePropertyChange(PROP_DIRTY);
			}
		}

		public void widgetSelected(SelectionEvent e) {
			widgetDefaultSelected(e);
		}
		
	};
	
	private IStructuredContentProvider contentproviderExc=new IStructuredContentProvider() {
		public Object[] getElements(Object inputElement) {
			if((inputElement==null)||(!(inputElement instanceof java.util.List)))
				return new Object[0];
			java.util.List<MyResource> list=(java.util.List<MyResource>)inputElement;
			return list.size()==0 ? new Object[0] : list.toArray();
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	};
	
	public ProtectFileEditor() {
		super();
	}

	public void doSave(IProgressMonitor monitor) {
		if(textPolicy.getText().length()==0) {
			MessageBox msgbox=new MessageBox(textPolicy.getShell());
			msgbox.setMessage("Policy cannot be empty!");
			msgbox.open();
			return;
		}
		getInput().setPolicyName(textPolicy.getText());
		getInput().setPolicyDefault(buttonDefaultPolicy.getSelection());
		MyPolicy policies[]=new MyPolicy[listOverridePolicies.getSelection().length];
		for(int i=0;i<listOverridePolicies.getSelection().length;i++)
			policies[i]=getInput().getInheritedPolicies().get(listOverridePolicies.getSelectionIndices()[i]);
		getInput().setOverridePolicies(policies);
		java.util.List<MyResource> list=new LinkedList<MyResource>();
		int i=0;
		while(listviewerExcRes.getElementAt(i)!=null)
			list.add((MyResource)listviewerExcRes.getElementAt(i++));
		getInput().setExceptionalResources((MyResource[])list.toArray(new MyResource[0]));
		if(textFilter.getText().length()>0)
			getInput().setFilter(textFilter.getText());
		getInput().savePolicies();
		bDirty=false;
		firePropertyChange(PROP_DIRTY);
	}

	public void doSaveAs() {
	}

	public void init(IEditorSite site, IEditorInput input)
		throws PartInitException {
		setSite(site);
		setInput(input);
		setPartName(input.getName());
	}

	public boolean isDirty() {
		return bDirty;
	}

	public boolean isSaveAsAllowed() {
		return false;
	}

	public void createPartControl(Composite parent) {
		ScrolledComposite scrolled_composite=new ScrolledComposite(parent,SWT.V_SCROLL);
		scrolled_composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		Composite composite=new Composite(scrolled_composite,SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		scrolled_composite.setContent(composite);

		Composite composite2=new Composite(composite,SWT.NONE);
		GridLayout gridlayout=new GridLayout();
		composite2.setLayout(gridlayout);
		composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		Label label=new Label(composite2,SWT.NONE);
		label.setText(getInput().getFullPath());

		composite2=new Composite(composite,SWT.NONE);
		gridlayout=new GridLayout();
		gridlayout.numColumns=3;
		composite2.setLayout(gridlayout);
		composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label=new Label(composite2,SWT.NONE);
		label.setText("Policy");
		textPolicy=new Text(composite2,SWT.SINGLE);
		String policy=getInput().getPolicyName();
		if(policy!=null)
			textPolicy.setText(policy);
		textPolicy.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textPolicy.addModifyListener(listenerModify);
		new Button(composite2,SWT.NONE).setText("Set Condition");

		composite2=new Composite(composite,SWT.NONE);
		gridlayout=new GridLayout();
		gridlayout.numColumns=2;
		composite2.setLayout(gridlayout);
		composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonDefaultPolicy=new Button(composite2,SWT.RADIO);
		buttonDefaultPolicy.setText("Default policy");
		buttonDefaultPolicy.setSelection(getInput().getPolicyDefault());
		buttonDefaultPolicy.addSelectionListener(listenerSelection);
		buttonMandatoryPolicy=new Button(composite2,SWT.RADIO);
		buttonMandatoryPolicy.setText("Mandatory policy");
		buttonMandatoryPolicy.setSelection(!getInput().getPolicyDefault());
		buttonMandatoryPolicy.addSelectionListener(listenerSelection);

		Composite compositeDirectory=new Composite(composite,SWT.NONE);
		compositeDirectory.setLayout(new GridLayout());
		compositeDirectory.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		composite2=new Composite(compositeDirectory,SWT.NONE);
		gridlayout=new GridLayout();
		gridlayout.numColumns=2;
		composite2.setLayout(gridlayout);
		composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label=new Label(composite2,SWT.NONE);
		label.setText("Filter");
		textFilter=new Text(composite2,SWT.SINGLE);
		String filter=getInput().getFilter();
		if(filter!=null)
			textFilter.setText(filter);
		textFilter.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		textFilter.addModifyListener(listenerModify);

		composite2=new Composite(composite,SWT.NONE);
		gridlayout=new GridLayout();
		gridlayout.numColumns=3;
		composite2.setLayout(gridlayout);
		composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label=new Label(composite2,SWT.NONE);
		label.setText("Exceptional resources");
		listviewerExcRes=new ListViewer(composite2,SWT.SINGLE);
		listviewerExcRes.setContentProvider(contentproviderExc);
//		java.util.List<MyResource> list=getInput().getExceptionalResources();
//		if(list!=null)
//			for(int i=0;i<list.size();i++)
//				listExcRes.add(list.get(i));
		listviewerExcRes.setInput(getInput().getExceptionalResources());
		listviewerExcRes.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		composite2=new Composite(composite2,SWT.NONE);
		composite2.setLayout(new GridLayout());
		buttonNewExcRes=new Button(composite2,SWT.NONE);
		buttonNewExcRes.setText("Add resource");
		buttonNewExcRes.addSelectionListener(listenerSelection);
		buttonNewExcRes.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonRemoveExcRes=new Button(composite2,SWT.NONE);
		buttonRemoveExcRes.setText("Remove resource");
		buttonRemoveExcRes.addSelectionListener(listenerSelection);
		buttonRemoveExcRes.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		composite2=new Composite(composite,SWT.NONE);
		gridlayout=new GridLayout();
		gridlayout.numColumns=2;
		composite2.setLayout(gridlayout);
		composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		label=new Label(composite2,SWT.NONE);
		label.setText("Overrides");
		listOverridePolicies=new List(composite2,SWT.MULTI);
		listOverridePolicies.setLayoutData(new GridData(GridData.FILL_BOTH));
		listOverridePolicies.addSelectionListener(listenerSelection);
		for(int i=0;i<getInput().getInheritedPolicies().size();i++)
//			if(getInput().getInheritedPolicies().get(i).getDefault())
			listOverridePolicies.add(getInput().getInheritedPolicies().get(i).toString());
		if(getInput().getOverridePolicies()!=null)
			for(int i=0;i<getInput().getOverridePolicies().size();i++)
				listOverridePolicies.select(getInput().getInheritedPolicies().indexOf(getInput().getOverridePolicies().get(i)));

		composite2=new Composite(composite,SWT.NONE);
		gridlayout=new GridLayout();
		gridlayout.numColumns=2;
		composite2.setLayout(gridlayout);
		composite2.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonOK=new Button(composite2,SWT.NONE);
		buttonOK.setText("OK");
		buttonOK.addSelectionListener(listenerSelection);
		buttonOK.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		buttonCancel=new Button(composite2,SWT.NONE);
		buttonCancel.setText("Cancel");
		buttonCancel.addSelectionListener(listenerSelection);
		buttonCancel.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		
		scrolled_composite.setExpandHorizontal(true);
		scrolled_composite.setExpandVertical(true);
		Point point=composite.computeSize(SWT.DEFAULT,SWT.DEFAULT);
		scrolled_composite.setMinHeight(point.y);
		scrolled_composite.setMinWidth(point.x);
	}

	public void setFocus() {
		textPolicy.setFocus();
	}

	private FileEditorInput getInput() {
		return (FileEditorInput)getEditorInput();
	}
	
	public boolean isSaveOnCloseNeeded() {
		return true;
	}
}
