package org.peertrust.modeler.policysystem.gui.actions;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.peertrust.modeler.policysystem.model.ProjectConfig;
import org.peertrust.modeler.policysystem.model.ProjectConfigChangeListener;
import org.peertrust.modeler.policysystem.model.exceptions.BadConfigFileException;


/**
     * This Class provides a Menu Item that hold the 4 most recently
     * open projects  
     */
public class RecentlyOpenContributionItem 	
    				extends ContributionItem//ControlContribution
    				implements ProjectConfigChangeListener,
    							SelectionListener
    {
		/**
		 * The logger for this class
		 */
		private static final Logger logger= 
			Logger.getLogger(RecentlyOpenContributionItem.class);
		
    	/**
    	 * Keys used to identified the recently open files
    	 */
    	private final String KEYS[]= {"1","2","3","4"};
    	
    	//MenuItem mi;
    	/**
    	 * Used as default id for this contribution item
    	 */
    	public static final String DEFAULT_ID="RECENTY_OPEN_PJT";
    	
    	/**
    	 * The name of the file which contains the properties
    	 */
    	public static final String STORE_FILE_NAME="recently_opened.prop";
    	//private static final String STORE_FILE_PATH="/"+STORE_FILE_NAME;
    	
    	//private List list;
    	
    	/**
    	 * The url of the file, in which the properties will be stored
    	 */
    	private URL storeURL;
    	
    	/**
    	 * Contains the pairs (KEY[i],recently opened file)
    	 */
    	private Properties recentlyOpened=new Properties();
    	
    	/**
    	 * The contribution menu items
    	 */
    	private MenuItem menuItems[]= new MenuItem[4];
    	
    	/**
    	 * Construct a new RecentlyOpenContributionItem identified by
    	 * DEFAULT_ID
    	 */
    	public RecentlyOpenContributionItem() 
		{
			super(DEFAULT_ID);
		}
    	
    	/**
    	 * Construct a new RecentlyOpenContributionItem identified by
    	 * the given id
    	 */
		protected RecentlyOpenContributionItem(String id) 
		{
			super(id);
		}

		
		/**
		 * @see org.eclipse.jface.action.IContributionItem#fill(org.eclipse.swt.widgets.Menu, int)
		 */
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

//		/**
//		 * @param parent
//		 * @return
//		 */
//		protected Control createControl(Composite parent) 
//		{
//			list= new List(parent,SWT.NONE);
//			ProjectConfig.getInstance().addProjectConfigChangeListener(this);
//			loadRecentlyOpened();
//			return list;
//		}
    	
		
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
				logger.warn(
						"Exception while loading the recently opened files"+
							"\n\tmalformed url:"+storeURL,
						e);
			} catch (IOException e) {
				logger.warn(
						"Exception while loading the recently opened files:\n"+storeURL,
						e);
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
			
	    	for( int i=0;i<SIZE;i++)
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
				logger.warn(
						"Exception while storing the properties"+
						"representing the recently opened files into"+
						"this loaction:\n"+storeURL,e);
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
			try {
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
					logger.info("SelectionEvent:"+e);
				}
			} catch (BadConfigFileException e1) {
				e1.printStackTrace();
			}
		}

		
		public void widgetDefaultSelected(SelectionEvent e) 
		{
			
		}
    
    }