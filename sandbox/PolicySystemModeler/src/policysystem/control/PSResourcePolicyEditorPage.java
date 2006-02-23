package policysystem.control;

import org.apache.log4j.Logger;
import org.apache.log4j.jmx.LayoutDynamicMBean;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableLayout;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
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
		
		composite=new Composite(parent,SWT.FILL);
		composite.setLayout(new FormLayout());
		toolBar=new ToolBar(composite,SWT.NONE);
		GridData gridData=new GridData(GridData.FILL_HORIZONTAL);
		
		toolBar.setLayoutData(gridData);
		localPolicyView= new TableViewer(composite);
		makeTable();
		localPolicyView.getControl().setLayoutData(
				new GridData(GridData.HORIZONTAL_ALIGN_CENTER,
							GridData.VERTICAL_ALIGN_END,
							true,
							true));
		toolBarMng= new ToolBarManager(toolBar);
		makeActions();
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

	private void makeTable()
	{
		ResourcePolicyContentProvider provider=
				new ResourcePolicyContentProvider();
		localPolicyView.setContentProvider(provider);
		localPolicyView.setLabelProvider(provider);
		TableLayout layout= new TableLayout();
		layout.addColumnData(new ColumnWeightData(33,true));
		layout.addColumnData(new ColumnWeightData(33,true));
		layout.addColumnData(new ColumnWeightData(34,true));
		
		Table table=localPolicyView.getTable();
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
		localPolicyView.setInput(PolicySystemRDFModel.LNAME_PROP_HAS_NAME);
	}
	
	private void makeActions() {
		Action addAction = new Action() {
			public void run() {
			}
		};
		addAction.setText("save");
		addAction.setToolTipText("Action 1 tooltip");
		addAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
			getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		
		Action removeAction = new Action() {
			public void run() {
				
			}
		};
		removeAction.setText("Action 2");
		removeAction.setToolTipText("Action 2 tooltip");
		removeAction.setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().
				getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));

	}
}
