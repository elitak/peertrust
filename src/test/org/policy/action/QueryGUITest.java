package test.org.policy.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextArea;

import org.policy.action.standard.view.QueryGUI;

import junit.framework.TestCase;

public class QueryGUITest extends TestCase {
	private static String TESTSTR = "in (user(Name, Address), rdbms: query('select userName, adress from integration', 'mysql'))";
	
	public void testQueryGUI(){
		QueryGUI gui = new QueryGUI();
		
		JTextArea eingabe = gui.getEingabeTextArea();
		eingabe.setText(TESTSTR);
		JButton okButton = gui.getOkButton();
		ActionListener[] listeners = okButton.getActionListeners();
		
		listeners = okButton.getActionListeners();
		for(int i=0;i<listeners.length;i++){
			listeners[i].actionPerformed(new ActionEvent(okButton, 0, ""));
		}
		String name = gui.getTable().getResult().get(1).getBinding(0);
		assertEquals("Daniel Olmedilla", name);
	}
}
