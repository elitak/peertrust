/**
 * 
 */
package org.peertrust.modeler.policysystem.views;


import org.apache.log4j.Logger;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.ListViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.part.ViewPart;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;


/**
 * @author congo
 *
 */
public class PSPoliciesView extends ViewPart {

	public static final String ID="org.peertrust.modeler.policysystem.PSPolicyView";
	private ListViewer listView;
	private IStructuredContentProvider contentProvider;
	private Logger logger= Logger.getLogger(PSPoliciesView.class);; 
	
	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#createPartControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createPartControl(Composite parent) 
	{
		listView= new ListViewer(parent);
		contentProvider = makeContentProvider();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.ui.part.WorkbenchPart#setFocus()
	 */
	public void setFocus() {
		
	}

	
	private final IStructuredContentProvider makeContentProvider()
	{
		IStructuredContentProvider cp=
			new IStructuredContentProvider()
			{

				public Object[] getElements(Object inputElement) {
					if(inputElement==null)
					{
						logger.warn(
								"input object must not be null"+
								"returning null array");
						return new Object[0];
					}
					else if(inputElement instanceof PSPoliciesView)
					{
						return new Object[]{"policy"}; 
					}
					else
					{
						logger.warn(
								"unsupoorted inputelement"+
								" returning empty object array");
						return new Object[0];
					}
				}

				public void dispose() {
					
				}

				public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
					
				}
			
			
			};
			
			return cp;
	}
}
