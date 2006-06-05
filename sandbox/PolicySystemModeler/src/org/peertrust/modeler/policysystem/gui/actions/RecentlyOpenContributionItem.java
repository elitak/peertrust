package org.peertrust.modeler.policysystem.gui.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.peertrust.modeler.policysystem.model.ProjectConfig;
import org.peertrust.modeler.policysystem.model.ProjectConfigChangeListener;


/**
     * This Class provides a Menu Item that hold the 4 most recently
     * open projects  
     */
public class RecentlyOpenContributionItem 	
    				extends ContributionItem//ControlContribution
    				implements ProjectConfigChangeListener,
    							SelectionListener
    {
    	private final String KEYS[]= {"1","2","3","4"};
    	private final String DEFAULT_IDS[]=
    						{	"RECENTLY_OPENED_0",
    							"RECENTLY_OPENED_1",
    							"RECENTLY_OPENED_2",
    							"RECENTLY_OPENED_3"}; 
    	//MenuItem mi;
    	public static final String DEFAULT_ID="RECENTY_OPEN_PJT";
    	
    	public static final String STORE_FILE_NAME="recently_opened.prop";
    	public static final String STORE_FILE_PATH="/"+STORE_FILE_NAME;
    	
    	private List list;
    	private URL storeURL;
    	private Properties recentlyOpened=new Properties();
    	
    	private MenuItem menuItems[]= new MenuItem[4];
    	
    	public RecentlyOpenContributionItem() 
		{
			super(DEFAULT_ID);
		}
    	
		protected RecentlyOpenContributionItem(String id) 
		{
			super(id);
		}

		
		public void fill(Menu menu, int index) {
			//super.fill(menu, index);
			new MenuItem(menu,SWT.SEPARATOR);
//			MenuItem mi= new MenuItem(menu,SWT.CASCADE);
//			Menu cascadeMenu= new Menu(menu);
//			mi.setMenu(cascadeMenu);
//			mi.setText("mi");
			menuItems[0]= new MenuItem(menu,SWT.NONE);
			menuItems[0].setText("1:");
			menuItems[0].setEnabled(false);
			menuItems[0].addSelectionListener(this);
			//menuItems[0].getMenu().
			menuItems[1]= new MenuItem(menu,SWT.NONE);
			menuItems[1].setText("2:");
			menuItems[1].setEnabled(false);
			menuItems[1].addSelectionListener(this);
			
			menuItems[2]= new MenuItem(menu,SWT.NONE);
			menuItems[2].setText("3:");
			menuItems[2].setEnabled(false);
			menuItems[2].addSelectionListener(this);
			
			menuItems[3]= new MenuItem(menu,SWT.NONE);
			menuItems[3].setText("4:");
			menuItems[3].setEnabled(false);
			menuItems[3].addSelectionListener(this);
			
			loadRecentlyOpened();
			ProjectConfig.getInstance().addProjectConfigChangeListener(this);
			doFillList();
		}

		protected Control createControl(Composite parent) 
		{
			list= new List(parent,SWT.NONE);
			ProjectConfig.getInstance().addProjectConfigChangeListener(this);
			loadRecentlyOpened();
			return list;
		}
    	
		
		/**
		 *Loads the most recently opened projects into the 
		 *recentlyOpened property object from the 
		 *propriety bundle file with this path 
		 *<code>STORE_FILE_NAME</code>. 
		 */
		private void loadRecentlyOpened()
		{
			try {
				File storeFile= 
					new File(
							Platform.getInstanceLocation().getURL().getPath(),
							STORE_FILE_NAME);
				if(!storeFile.exists())
				{
					storeFile.createNewFile();
				}
				storeURL=storeFile.toURL();
				recentlyOpened.load(storeURL.openStream());
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
//			Bundle bundle=
//				PolicysystemPlugin.getDefault().getBundle();
//			storeURL= bundle.getEntry(STORE_FILE_PATH);
//			if(storeURL!=null)
//			{
//				try 
//				{
//					recentlyOpened.load(storeURL.openStream());
//					
//				} 
//				catch (IOException e) 
//				{
//					e.printStackTrace();
//				}
//			}
//			else
//			{
//				try 
//				{
//					URL bundleRoot=
//						PolicysystemPlugin.getDefault().getBundle().getEntry(
//																"/");
//					File storeFile= 
//						new File(bundleRoot.getPath(),STORE_FILE_NAME);
//					storeFile.createNewFile();
//					storeURL=storeFile.toURL();
//				} 
//				catch (IOException e) 
//				{
//					e.printStackTrace();
//				}
//			}
			
		}
	    
		/**
		 *To fill the list with the element in the property object
		 *File0=/path/to/dotconf will be entered in the list as
		 *0 /path/to/dotconf
		 */
		private void doFillList()
	    {
			
			final int SIZE=KEYS.length;
			
	    	for( int i=0;i<KEYS.length;i++)
	    	{
	    		String filePath=recentlyOpened.getProperty(KEYS[i]);
	    		if(filePath==null)
	    		{
	    			break;
	    		}
	    		else
	    		{
	    			menuItems[i].setText((i+1)+": "+filePath);
	    			menuItems[i].setEnabled(true);
	    		}
	    		
	    	}
	    	
	    }
		
		/**
		 * Gets the new loaded project and add the path of its config
		 * file to the recently opened project files and update the
		 * list.
		 * @param projectConfig
		 */
		private void doUpdate(ProjectConfig projectConfig)
		{
			String currentProjectFile=
				ProjectConfig.getInstance().getProjectFile();
			String lastProjectFile=
				currentProjectFile;
			
			System.out.println("lastPjtFile:"+lastProjectFile);
			//remove old entry			
			String nextProjectFile;
			int size=recentlyOpened.size()+1;
			size=(size>KEYS.length)?KEYS.length:size;
			int skipOffset=0;
			for(	int i=0;
					(i<size) && (i<KEYS.length) && (lastProjectFile!=null);
					i++)
			{
				nextProjectFile=recentlyOpened.getProperty(KEYS[i]);
				if(currentProjectFile.equals(nextProjectFile))
				{
					skipOffset=1;
				}
				else
				{
					recentlyOpened.put(KEYS[i-skipOffset],lastProjectFile);
					lastProjectFile=nextProjectFile;
				}
			}
			doFillList();
			try {
				recentlyOpened.store(
						new FileOutputStream(storeURL.getPath()),
						"Recently opened Project Files");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	
		/**
		 * @see org.peertrust.modeler.policysystem.model.ProjectConfigChangeListener#projectConfigChanged(org.peertrust.modeler.policysystem.model.ProjectConfig)
		 */
		public void projectConfigChanged(ProjectConfig config) 
		{
			doUpdate(config);
		}

		
		
		
		public void widgetSelected(SelectionEvent e) 
		{
			ProjectConfig projectConf=ProjectConfig.getInstance();
			if(e.getSource()==menuItems[0])
			{
				projectConf.setProjectFile(
						recentlyOpened.getProperty(KEYS[0]));
			}
			else if(e.getSource()==menuItems[1])
			{
				projectConf.setProjectFile(
						recentlyOpened.getProperty(KEYS[1]));
			}
			else if(e.getSource()==menuItems[2])
			{
				projectConf.setProjectFile(
						recentlyOpened.getProperty(KEYS[2]));
			}
			else if(e.getSource()==menuItems[3])
			{
				projectConf.setProjectFile(
						recentlyOpened.getProperty(KEYS[3]));
			}
			else
			{
				System.out.println("SelectionEvent:"+e);
			}
		}

		
		public void widgetDefaultSelected(SelectionEvent e) 
		{
			
		}
    
    }