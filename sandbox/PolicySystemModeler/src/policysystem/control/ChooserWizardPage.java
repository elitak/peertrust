/**
 * 
 */
package policysystem.control;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;

import policysystem.model.PolicySystemRDFModel;
import policysystem.model.abtract.ModelObjectWrapper;
import policysystem.model.abtract.PSPolicy;
import policysystem.model.abtract.PSResource;

class ChooserWizardPage extends WizardPage
	{
		/**
		 * 
		 */
		//private final PSOverriddingRuleEditorPage page;
		private Class type;
		private List list;
		private Object[] modelObjects;
		private String[] names;
		private Logger logger=Logger.getLogger(ChooserWizardPage.class);
		protected ChooserWizardPage(
						String pageName,
						Class type) 
		{
			super(pageName);
			//this.page = page;
			this.type=type;
		}

		public void createControl(Composite parent) 
		{
			try {
				list= new List(parent,SWT.FILL);
				if(type.equals(PSPolicy.class))
				{
					modelObjects =
						PolicySystemRDFModel.getInstance().getPolicies().toArray();
				}
				else if(type.equals(PSResource.class))
				{
					modelObjects=
						PolicySystemRDFModel.getInstance().getResources().toArray();
				}
				else
				{
					modelObjects=new Object[0];
				}
				
				logger.info("Policies:"+modelObjects);
				names= new String[modelObjects.length];
				for(int i=modelObjects.length-1;i>=0;i--)
				{
					names[i]=((ModelObjectWrapper)modelObjects[i]).getLabel();
				}
				list.setItems(names);
				list.setSize(parent.getClientArea().width,
							parent.getClientArea().width);
				super.setControl(list);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public Object getSelected()
		{
			int[] selIndices=list.getSelectionIndices();
			if(selIndices.length<=0)
			{
				return null;
			}
			else
			{
				return modelObjects[selIndices[0]];
			}
		}

		public String getName() {
		
			return super.getName();
		}
				
	}