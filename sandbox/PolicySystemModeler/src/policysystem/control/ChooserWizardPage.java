/**
 * 
 */
package policysystem.control;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;

import policysystem.control.PSOverriddingRuleEditorPage.ChooserWizard;
import policysystem.model.PolicySystemRDFModel;
import policysystem.model.abtract.ModelObjectWrapper;
import policysystem.model.abtract.PSPolicy;
import policysystem.model.abtract.PSResource;

public class ChooserWizardPage extends WizardPage
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
				setControl(list);
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
		
		public static PSPolicy choosePlicy(Shell  shell)
		{
			//DialogPage p;
			final PSPolicy selPol[]={null};
			Wizard wiz=new Wizard()
			{
				private Object selected;
				public boolean performFinish() 
				{
					selPol[0]=
						(PSPolicy)((ChooserWizardPage)this.getPages()[0]).getSelected();
					return true;
				}
			};
			 
			ChooserWizardPage page=
				new ChooserWizardPage("Choose Policy",PSPolicy.class);
			wiz.addPage(page);
			WizardDialog dlg=
				new WizardDialog(
						shell,
						wiz);
			
			int resp=dlg.open();
			PSPolicy sel=
				selPol[0];//(PSPolicy)page.getSelected();
			if(sel==null)
			{
				return null;
			}
			return sel;
		}
				
	}