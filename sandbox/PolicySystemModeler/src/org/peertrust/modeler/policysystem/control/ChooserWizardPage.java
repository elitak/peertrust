/**
 * 
 */
package org.peertrust.modeler.policysystem.control;

import org.apache.log4j.Logger;
import org.eclipse.jface.dialogs.DialogPage;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.peertrust.modeler.policysystem.control.PSOverriddingRuleEditorPage.ChooserWizard;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.abtract.ModelObjectWrapper;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;

import com.hp.hpl.jena.rdf.model.Model;


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
		private static Logger logger=Logger.getLogger(ChooserWizardPage.class);
		private boolean allowMultipleSelection=false;
		
		/**
		 * Create a wizard page to choose the between an object 
		 * of the provided type in the model
		 * @param pageName -- the page name
		 * @param type -- the type of the objects to choose from 
		 * 			e.g. <code>PSPolicy.class</code>
		 * @param allowMultipleSelection -- passed true if multiple selection is
		 * 			to be carried is allow
		 */
		protected ChooserWizardPage(
						String pageName,
						Class type,
						boolean allowMultipleSelection) 
		{
			super(pageName);
			this.type=type;
			this.allowMultipleSelection=allowMultipleSelection;
		}

		
		/**
		 * Create a wizard page to choose the between an object 
		 * of the provided type in the model
		 * @param pageName -- the page name
		 * @param type -- the type of the objects to choose from 
		 * 			e.g. <code>PSPolicy.class</code>
		 */
		protected ChooserWizardPage(
						String pageName,
						Class type) 
		{
			super(pageName);
			this.type=type;
		}

		/**
		 * @see org.eclipse.jface.dialogs.IDialogPage#createControl(org.eclipse.swt.widgets.Composite)
		 */
		public void createControl(Composite parent) 
		{
			try {
				if(allowMultipleSelection)
				{
					list= new List(parent,SWT.FILL|SWT.MULTI);
				}
				else
				{
					list= new List(parent,SWT.FILL);
				}
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
		
		/**
		 * @return the selected list items:
		 * <ul>
		 * <li>a single object if allowMultipleSelection is false
		 * <li>otherwise an array of all selected elements
		 * </ul>
		 */
		public Object getSelected()
		{
			int[] selIndices=list.getSelectionIndices();
			if(selIndices.length<=0)
			{
				return null;
			}
			else
			{
				if(allowMultipleSelection==false)
				{
					return modelObjects[selIndices[0]];
				}
				else
				{
					Object[] selections= new Object[selIndices.length];
					for(int i=0;i<selIndices.length;i++)
					{
						selections[i]=modelObjects[selIndices[i]];
					}
					return selections;
				}
			}
		}

		/**
		 * @see org.eclipse.jface.wizard.IWizardPage#getName()
		 */
		public String getName() {
		
			return super.getName();
		}
		
		/**
		 * A utility methode that kan be used to choose a single policy
		 * currently in the model.
		 * @param shell -- the parent shell for creating the wizard
		 * @return the chosen poly or null if no policy was selected
		 */
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
		
		public static PSPolicy[] choosePlicies(Shell  shell)
		{
			//DialogPage p;
			//final PSPolicy selPol[]=null;
			final Object selPol[]={null};
			Wizard wiz=new Wizard()
			{
				private Object selected;
				public boolean performFinish() 
				{
					selPol[0]=((ChooserWizardPage)
										this.getPages()[0]).getSelected();
					return true;
				}
			};
			 
			ChooserWizardPage page=
				new ChooserWizardPage("Choose Policy",PSPolicy.class,true);
			wiz.addPage(page);
			WizardDialog dlg=
				new WizardDialog(
						shell,
						wiz);
			
			int resp=dlg.open();
			System.out.println("selPol[0]:"+selPol[0]);
			
			return (PSPolicy[])selPol[0];
		}
		
		public static ModelObjectWrapper[] chooseModelObjects(
										Shell  shell, 
										Class modelObjectTypes)
		{
			if(shell==null || modelObjectTypes==null)
			{
				logger.warn("Parameters must not be null: shell="+shell+
							" modelObjectTypes="+modelObjectTypes);
				return null;
			}
			//DialogPage p;
			//final PSPolicy selPol[]=null;
			final Object selPol[]={null};
			Wizard wiz=new Wizard()
			{
				private Object selected;
				public boolean performFinish() 
				{
					selPol[0]=((ChooserWizardPage)
										this.getPages()[0]).getSelected();
					return true;
				}
			};
			 
			ChooserWizardPage page=
				new ChooserWizardPage("Choose Policy",modelObjectTypes,true);
			wiz.addPage(page);
			WizardDialog dlg=
				new WizardDialog(
						shell,
						wiz);
			
			int resp=dlg.open();
			if(selPol[0]==null)
			{
				return null;
			}
			else if(selPol[0] instanceof ModelObjectWrapper)
			{
				return new ModelObjectWrapper[]{(ModelObjectWrapper)selPol[0]};
			}
			else if(selPol[0] instanceof Object[])
			{
				final int LEN= ((Object[])selPol[0]).length;
				ModelObjectWrapper[] mows=
					new ModelObjectWrapper[LEN];
				for(int i=0;i<LEN;i++)
				{
					mows[i]=(ModelObjectWrapper)((Object[])selPol[0])[0];
				}
				return mows;
			}
			else
			{
				logger.warn("Cannot handle Selection[0]:"+selPol[0]);
				return null;
			}
		}
				
	}