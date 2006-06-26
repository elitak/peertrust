/**
 * 
 */
package org.peertrust.modeler.policysystem.gui.control;

import java.util.Arrays;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.jface.wizard.WizardPage;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Shell;
import org.peertrust.modeler.policysystem.model.PolicySystemRDFModel;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSOverridingRule;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;
import org.peertrust.modeler.policysystem.model.abtract.PSResource;




public class ChooserWizardPage extends WizardPage
	{
		/**
		 * 
		 */
		//private final PSOverriddingRuleEditorPage page;
		/**
		 * The type of model oject to show in the list.
		 * E.g. PSPolicy
		 */
		private Class type;
		
		/**
		 * The list control used to show the model objects
		 */
		private List list;
		
		/**
		 * An array of model objects to show in in the list
		 */
		private Hashtable modelObjectTable;
		
//		/**
//		 *The name resp. label of the model objects 
//		 */
//		private String[] names;
		
		/**
		 * Holds name or label of the item which are not to be
		 * displayed
		 */
		private java.util.List namesToIgnore;
		/**
		 * Logger for this class
		 */
		private static Logger logger=Logger.getLogger(ChooserWizardPage.class);
		
		/**
		 * Flag
		 */
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
		public ChooserWizardPage(
						String pageName,
						Class type,
						boolean allowMultipleSelection,
						String[] namesToIgnore) 
		{
			super(pageName);
			this.type=type;
			this.allowMultipleSelection=allowMultipleSelection;
			if(namesToIgnore!=null)
			{
				if(namesToIgnore.length!=0)
				{
					this.namesToIgnore= Arrays.asList(namesToIgnore);
					//System.arraycopy(namesToIgnore,0,this.namesToIgnore,0,namesToIgnore.length);
				}
				
			}
		}

		
		/**
		 * Create a wizard page to choose the between an object 
		 * of the provided type in the model
		 * @param pageName -- the page name
		 * @param type -- the type of the objects to choose from 
		 * 			e.g. <code>PSPolicy.class</code>
		 */
		public ChooserWizardPage(
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
				Vector modelObjects;
				if(type.equals(PSPolicy.class))
				{
					modelObjects =
						PolicySystemRDFModel.getInstance().getPolicies();
				}
				else if(type.equals(PSResource.class))
				{
					modelObjects=
						PolicySystemRDFModel.getInstance().getResources();
				}
				else if(type.equals(PSOverridingRule.class))
				{
					modelObjects=
						PolicySystemRDFModel.getInstance().getOverriddingRules(null);
				}
				else if(type.equals(PSFilter.class))
				{
					modelObjects=
						PolicySystemRDFModel.getInstance().getFilters(null);
				}
				else
				{
					modelObjects=new Vector(0);
				}
				
				logger.info("Policies:"+modelObjects);
				
				//ArrayList al= new ArrayList(16);
				modelObjectTable= new Hashtable();
				String name;
				PSModelObject modelObj;
				
				for(Iterator i=modelObjects.iterator();i.hasNext();)
				{
					modelObj=(PSModelObject)i.next();
					name=modelObj.getLabel().getValue();
					modelObjectTable.put(name,modelObj);
					if(namesToIgnore!=null)
					{
						if(!namesToIgnore.contains(name))
						{
							list.add(name);
						}
					}
					else
					{
						//al.add(name);
						list.add(name);
					}
						
				}
				
//				names= new String[al.size()];
//				System.out.println("al.size:"+al.size()+ " al:"+al);
//				if(names.length>0)
//				{
//					al.toArray(names);
//				}
//				list.setItems(names);
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
			String[] selItems=list.getSelection();
			//System.out.println("sel:"+selIndices);
			if(selItems.length<=0)
			{
				return null;
			}
			else
			{
				if(allowMultipleSelection==false)
				{
					return modelObjectTable.get(selItems[0]);//modelObjects[selIndices[0]];
				}
				else
				{
					Object[] selections= new Object[selItems.length];
					for(int i=0;i<selItems.length;i++)
					{
						selections[i]=modelObjectTable.get(selItems[i]);
					}
					logger.info(
							"sel:"+Arrays.asList(selItems)+
							"\nmodelObjects:"+modelObjectTable);
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
		 * @return
		 */
		public java.util.List getNamesToIgnore() 
		{
			return namesToIgnore;
		}


		/**
		 * @param namesToIgnore
		 */
		public void setNamesToIgnore(java.util.List namesToIgnore) 
		{
			this.namesToIgnore = namesToIgnore;
		}


		/**
		 * A utility methode that kan be used to choose a single policy
		 * currently in the model.
		 * @param shell -- the parent shell for creating the wizard
		 * @return the chosen poly or null if no policy was selected
		 */
		public static PSPolicy choosePolicy(Shell  shell)
		{
			//DialogPage p;
			final PSPolicy selPol[]={null};
			Wizard wiz=new Wizard()
			{
				//private Object selected;
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
			dlg.create();
			dlg.setTitle("Select Policy");
			dlg.setMessage("Select Policy");
			/*int resp=*/dlg.open();
			PSPolicy sel= selPol[0];//(PSPolicy)page.getSelected();
			if(sel==null)
			{
				return null;
			}
			return sel;
		}
		
		public static PSPolicy[] choosePlicies(Shell  shell, String[] itemsToIgnore)
		{
			//DialogPage p;
			//final PSPolicy selPol[]=null;
			final Object selPol[]={null};
			Wizard wiz=new Wizard()
			{
				//private Object selected;
				public boolean performFinish() 
				{
					selPol[0]=((ChooserWizardPage)
										this.getPages()[0]).getSelected();
					return true;
				}
			};
			 
			ChooserWizardPage page=
				new ChooserWizardPage(
							"Choose Policy",
							PSPolicy.class,
							true,
							itemsToIgnore);
			wiz.addPage(page);
			WizardDialog dlg=
				new WizardDialog(
						shell,
						wiz);
			dlg.create();
			dlg.setTitle("Select Policies");
			dlg.setMessage("Select Policies");
			/*int resp=*/dlg.open();
			logger.info("selPol[0]:"+selPol[0]);
			
			return (PSPolicy[])selPol[0];
		}
		
		public static PSModelObject[] chooseModelObjects(
										Shell  shell, 
										Class modelObjectTypes,
										String[] itemsToIgnore)
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
				//private Object selected;
				public boolean performFinish() 
				{
					selPol[0]=((ChooserWizardPage)
										this.getPages()[0]).getSelected();
					return true;
				}
			};
			 
			ChooserWizardPage page=
				new ChooserWizardPage(
							"Select Model Object",
							modelObjectTypes,
							true,
							itemsToIgnore);
			wiz.addPage(page);
			WizardDialog dlg=
				new WizardDialog(
						shell,
						wiz);
			dlg.create();
			//dlg.setTitle(getWizardDlgTitle(modelObjectTypes,true));
			dlg.setMessage(getWizardDlgTitle(modelObjectTypes,true));
			dlg.getShell().setText(getWizardDlgTitle(modelObjectTypes,true));
			/*int resp=*/dlg.open();
			if(selPol[0]==null)
			{
				return null;
			}
			else if(selPol[0] instanceof PSModelObject)
			{
				return new PSModelObject[]{(PSModelObject)selPol[0]};
			}
			else if(selPol[0] instanceof Object[])
			{
				final int LEN= ((Object[])selPol[0]).length;
				PSModelObject[] mows=
					new PSModelObject[LEN];
				for(int i=0;i<LEN;i++)
				{
					mows[i]=(PSModelObject)((Object[])selPol[0])[0];
				}
				return mows;
			}
			else
			{
				logger.warn("Cannot handle Selection[0]:"+selPol[0]);
				return null;
			}
		}
		
		private static final String getWizardDlgTitle(
									Class modelObjectType,
									boolean allowMultiSelection)
		{
			if(allowMultiSelection==true)
			{
				if(modelObjectType.equals(PSResource.class))
				{
					return "Select Resource";
				}
				else if(modelObjectType.equals(PSPolicy.class))
				{
					return "Select Policy";
				}
				else if(modelObjectType.equals(PSOverridingRule.class))
				{
					return "Select Overridding rule";
				}
				else if(modelObjectType.equals(PSFilter.class))
				{
					return "Select Filter";
				}
				else
				{
					return "Select Model Object";
				}
			}
			else
			{
				if(modelObjectType.equals(PSResource.class))
				{
					return "Select Resources";
				}
				else if(modelObjectType.equals(PSPolicy.class))
				{
					return "Select Policies";
				}
				else if(modelObjectType.equals(PSOverridingRule.class))
				{
					return "Select Overridding rules";
				}
				else if(modelObjectType.equals(PSFilter.class))
				{
					return "Select Filters";
				}
				else
				{
					return "Select Model Objects";
				}
			}
		}
				
	}