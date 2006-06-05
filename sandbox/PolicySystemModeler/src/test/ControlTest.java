/**
 * 
 */
package test;



import org.eclipse.jface.viewers.ComboViewer;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.peertrust.modeler.policysystem.gui.providers.PSModelObjectComboContentProvider;

/**
 * @author Patrice Congo
 */
public class ControlTest {
	
	/**
	 * 
	 */
	public ControlTest() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	static public void testCombo()
	{
		Display display = new Display ();
		Shell shell = new Shell (display);
		Combo combo = new Combo (shell, SWT.READ_ONLY);
		combo.setItems (new String [] {"A", "B", "C"});
		combo.setSize (200, 200);
		shell.pack ();
		shell.open ();
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}

//	static public void testComboView()
//	{
//		Display display = new Display ();
//		Shell shell = new Shell (display);
//		
//		ComboViewer comboViewer= new ComboViewer(shell);
//		PSModelObjectComboContentProvider provider=
//					new PSModelObjectComboContentProvider();
//		comboViewer.setContentProvider(provider);
//	}
	
	static public void testComboView()
	{
		Display display = new Display ();
		Shell shell = new Shell (display);
		shell.setLayout(new FillLayout());
		Composite composite= new Composite(shell,SWT.FILL);
		composite.setLayout(new RowLayout());
		Button button= new Button(composite,SWT.BORDER);
		button.setText("button");
		final ComboViewer comboViewer= new ComboViewer(composite,SWT.BORDER);
		PSModelObjectComboContentProvider provider=
					new PSModelObjectComboContentProvider();
		comboViewer.setContentProvider(provider);
		Object[] input=new Object[] {new Integer(1), new Integer(2), new Integer(3)};
		comboViewer.setInput(input);
		comboViewer.setSelection(
					//new ModelObjectISelection(input[1]),true);
					new StructuredSelection(input[1]),true);
		
		shell.pack ();
		shell.open ();
		
		System.out.println("blblblblblblblblblblblblblwwwwwwwwwwww:"+
					comboViewer.getSelection());
		button.addMouseListener(
				new MouseListener()
				{

					public void mouseDoubleClick(MouseEvent e) {
						
					}

					public void mouseDown(MouseEvent e) {
							
					}

					public void mouseUp(MouseEvent e) {
						System.out.println("blblblblblblblblblblblblblwwwwwwwwwwww:"+
								comboViewer.getSelection());
					}});
		
		while (!shell.isDisposed ()) {
			if (!display.readAndDispatch ()) display.sleep ();
		}
		display.dispose ();
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//testCombo();
		testComboView();
		
	}

}
