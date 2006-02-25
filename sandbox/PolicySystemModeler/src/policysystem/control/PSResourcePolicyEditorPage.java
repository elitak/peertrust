package policysystem.control;

import org.apache.log4j.Logger;
import org.apache.log4j.jmx.LayoutDynamicMBean;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.Page;

import policysystem.model.PolicySystemRDFModel;
import policysystem.model.ResourcePolicyContentProvider;

public class PSResourcePolicyEditorPage extends Page {

	private Composite composite;
	private TableViewer localPolicyView;
	private ToolBar toolBar;
	private ToolBarManager toolBarMng;
	private Logger logger=Logger.getLogger(PSResourcePolicyEditorPage.class);
	
	public void createControl(Composite parent) 
	{
		
		composite=new Composite(parent,SWT.NONE);
		composite.setLayout(new FormLayout());
		
		Composite top= new Composite(composite,SWT.NONE);
		Composite tableComposite= new Composite(composite,SWT.NONE);
		final int HEIGHT=30;
		

		
		FormData tFD=new FormData();
		tFD.top=new FormAttachment(0,HEIGHT+5);//0,7);//,10,SWT.BOTTOM);
		tFD.left= new FormAttachment(0);
		tFD.right= new FormAttachment(100);
		tFD.bottom= new FormAttachment(100);
		tableComposite.setLayoutData(tFD);
		
		FormData formData= new FormData();
		formData.top= new FormAttachment(0,0);
		formData.left= new FormAttachment(0,0);
		formData.right= new FormAttachment(100);
		formData.height= HEIGHT;//new FormAttachment(5);
		
		localPolicyView=makeTable(tableComposite);
		toolBar=makeActions(top);
	}

	public Control getControl() 
	{
		return composite;
	}

	public void setFocus() 
	{
		
	}
	
	

	/* (non-Javadoc)
	 * @see org.eclipse.jface.viewers.ContentViewer#setInput(java.lang.Object)
	 */
	public void setInput(Object input) 
	{
		logger.info("input set:"+input);
		localPolicyView.setInput(input);
	}

	private TableViewer makeTable(Composite parent)
	{
		///layout
		parent.setLayout(new GridLayout());
		////make table
		TableViewer tv= 
			new TableViewer(parent,SWT.FILL|SWT.BORDER|SWT.FULL_SELECTION);		
		GridData gData= 
			new GridData(
					GridData.FILL_BOTH);
		tv.getControl().setLayoutData(gData);
		
		//data
		ResourcePolicyContentProvider provider=
				new ResourcePolicyContentProvider();
		tv.setContentProvider(provider);
		tv.setLabelProvider(provider);
		TableLayout layout= new TableLayout();
		layout.addColumnData(new ColumnWeightData(33,true));
		layout.addColumnData(new ColumnWeightData(33,true));
		layout.addColumnData(new ColumnWeightData(34,true));
		
		Table table=tv.getTable();
		table.setLayout(layout);
		TableColumn nameC=
				new TableColumn(
						table,
						SWT.CENTER,
						0);
		nameC.setText(PolicySystemRDFModel.LNAME_PROP_HAS_NAME);
		
		TableColumn valueC=
			new TableColumn(
					table,
					SWT.CENTER,
					1);
		valueC.setText(PolicySystemRDFModel.LNAME_PROP_HAS_VALUE);
		
		TableColumn filterC=
			new TableColumn(
					table,
					SWT.CENTER,
					2);
		filterC.setText(PolicySystemRDFModel.LNAME_PROP_HAS_FILTER);
		
		table.setLinesVisible(true);
		table.setHeaderVisible(true);		
	
		tv.setInput(PolicySystemRDFModel.LNAME_PROP_HAS_NAME);
		return tv;
	}
	
	private ToolBar makeActions(Composite parent) 
	{
		///manager
		toolBarMng= new ToolBarManager(toolBar);
		///
		Action addAction = new Action() {
			public void run() {
			}
		};
		addAction.setText("save");
		addAction.setToolTipText("Action 1 tooltip");
		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		toolBarMng.add(addAction);
		
		Action removeAction = new Action() {
			public void run() {
				
			}
		};
		
		removeAction.setText("Action 2");
		removeAction.setToolTipText("Action 2 tooltip");
		removeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		toolBarMng.add(removeAction);
		
		parent.setLayout(new GridLayout());
		GridData gd= new GridData(GridData.FILL_BOTH);
		ToolBar tb= toolBarMng.createControl(parent);
		tb.setLayoutData(gd);
		return tb;
	}
}
