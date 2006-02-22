package policysystem.control;


import org.eclipse.jface.window.ApplicationWindow;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Dialog;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.PlatformUI;

/**
 * @author Gervase Gallant gervasegallant@yahoo.com
 *
 * 
 * 
 */
public class NewProjectDlg extends Dialog{
	private String key = null;
	private String[] row = null;
	private String[] phone = null;
	private String[] defaultPhone = {"0","","",""};
	
	
	private Shell shell = null;
	
	private Button saveButton = null; 
	private Text firstName = null;
	private Text lastName = null;
	private Text middleName = null;
	private Text street = null;
	private Text city = null;
	private Text state = null;
	private Text zip = null;
	private Text country = null;
	private Combo phoneType = null;
	private Text areaCode = null;
	private Text phoneNumber = null;
	/**
	 * @param arg0
	 */
	public NewProjectDlg(Shell arg0) {
		this(arg0, SWT.APPLICATION_MODAL);
		
	}

	/**
	 * @param arg0
	 * @param arg1
	 */
	public NewProjectDlg(Shell arg0, int arg1) {
		super(arg0, arg1);
		
	}

	public static void main(String[] args) 
	{
		Shell shell=new Shell();
		shell.setActive();
		shell.setLayout(new FillLayout());
		Display display = shell.getDisplay();
//		shell=d.ActiveShell();
		NewProjectDlg dlg=new NewProjectDlg(shell);
		dlg.show();
	}
	
	public void show(){
//		shell = new Shell(this.getParent(), SWT.DIALOG_TRIM| SWT.APPLICATION_MODAL);
//		shell.setText("Edit");
//		shell.setBounds(this.getDialogBounds(300,300));
//		shell.setLayout(new FillLayout());
//		
//		this.initData();
//		//new Button(shell,SWT.PUSH).setText("Go");
//		this.initWidgets();
//		
//		shell.pack();
//		shell.open(); 
//		Display display = this.getParent().getDisplay(); 
//		while (!shell.isDisposed()) { 
//			if (!display.readAndDispatch()) 
//			display.sleep(); 
//		}
		
	}
	
	public void initWidgets(){
		Group group = new Group(shell, SWT.NONE);
		group.setText("Person");
		
		GridLayout layout = new GridLayout();
		layout.numColumns=2;
		group.setLayout(layout);
		
	
		new Label(group, SWT.SHADOW_OUT).setText("FirstName");
		firstName = new Text(group, SWT.BORDER);
		firstName.setText(row[1]);
		
		new Label(group, SWT.NONE).setText("MiddleName");
		middleName = new Text(group, SWT.BORDER);
		middleName.setText(row[2]);
		
		new Label(group, SWT.NONE).setText("LastName");
		lastName = new Text(group, SWT.BORDER);
		lastName.setText(row[3]);
		
		new Label(group, SWT.NONE).setText("Street");
		street = new Text(group, SWT.BORDER);
		street.setText(row[4]);
		
		new Label(group, SWT.NONE).setText("City");
		city = new Text(group, SWT.BORDER);
		city.setText(row[5]);

		new Label(group, SWT.NONE).setText("State");
		state = new Text(group, SWT.BORDER);
		state.setText(row[6]);
		
		new Label(group, SWT.NONE).setText("Zip");
		zip = new Text(group, SWT.BORDER);
		zip.setText(row[7]);

		new Label(group, SWT.NONE).setText("Country");
		country = new Text(group, SWT.BORDER);
		country.setText(row[8]);
		
		
		Group group2 = new Group(shell, SWT.NONE);
		group2.setText(" Phone ");
		
		GridLayout layout2 = new GridLayout();
		layout2.numColumns=2;
		group2.setLayout(layout2);
		GridData gridData2 = new GridData(GridData.HORIZONTAL_ALIGN_FILL);
		gridData2.horizontalSpan = 2;
		group2.setLayoutData(gridData2);
		
		new Label(group2, SWT.NONE).setText("Phone Type");
		phoneType = new Combo (group2, SWT.READ_ONLY);
		phoneType.setItems (new String [] {"Fax", "Business", "Cell", "Home"});
		phoneType.setText("Home");
		
		new Label(group2, SWT.NONE).setText("Area Code");
		areaCode = new Text(group2, SWT.BORDER);
		areaCode.setText(phone[1]);
		
		new Label(group2, SWT.NONE).setText("Number");
		phoneNumber = new Text(group2, SWT.BORDER);
		phoneNumber.setText(phone[2]);
		
		new Label(group2, SWT.SEPARATOR).setVisible(false); //invisible label to fill the left side
		saveButton = new Button(group2, SWT.PUSH); //button on the right...
		saveButton.setText("Save All");
		saveButton.addSelectionListener( new SelectionAdapter(){
			public void widgetSelected(SelectionEvent e){
					if (e.getSource() == NewProjectDlg.this.saveButton){
						NewProjectDlg.this.saveAll();
						NewProjectDlg.this.askQuit();	
					}
					
			}
			
		}
			
		);
		
		
	}
	
	public Rectangle getDialogBounds(int height, int width){
		Rectangle temp = this.getParent().getBounds();
		return new Rectangle(temp.x + 50,temp.y + 50,width, height);
	}
	
	/**
	 * @return
	 */
	public String getKey() {
		return key;
	}

	/**
	 * @param string
	 */
	public void setKey(String string) {
		key = string;
	}
	
	/**
	 * set up one row of data for the form
	 */
	private void initData(){
		row = new String[]{"0","1","2","3","4","5","6","7","8","9","10"};//PersonData.getInstance().find(this.getKey());
		phone = this.getPhone("Home"); //default
		
	}
	private String[] getPhone(String type){
		String[] ret = null;//PhoneData.getInstance().find(this.getKey(), type);
		if (ret == null){
			return defaultPhone;
			
		}else {
			return ret;
		}	
		
	}	
	
	private void saveAll(){
		row[1] = firstName.getText();
		row[2] = middleName.getText();
		row[3] = lastName.getText();
		row[4] = street.getText();
		row[5] = city.getText();
		row[6] = state.getText();
		row[7] = zip.getText();
		row[8] = country.getText();
		
		
		
		phone[3] = phoneType.getText();
		phone[1] = areaCode.getText();
		phone[2] = phoneNumber.getText();
		
		
	}
	
	private void askQuit(){
		MessageBox mb = new MessageBox(shell,SWT.OK|SWT.CANCEL|SWT.ICON_QUESTION);
		mb.setText("Quit Edit?");
		mb.setMessage("Are you finished editing?");
		int reply = mb.open();
		if (reply == SWT.OK){
			shell.dispose();	
		}	
	}							
							
}
