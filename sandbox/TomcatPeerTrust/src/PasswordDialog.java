import java.awt.BorderLayout;
import javax.swing.JPanel;
import javax.swing.JDialog;
import java.awt.GridBagLayout;
import javax.swing.JButton;
import java.awt.GridBagConstraints;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JCheckBox;

/**
 * 
 */

/**
 * @author pat_dev
 *
 */
public class PasswordDialog extends JDialog {

	private JPanel jContentPane = null;
	private JPasswordField jPasswordField = null;
	private JTextField jTextField = null;
	private JLabel nameLabel = null;
	private JLabel passwordLabel = null;
	private JButton comitButton = null;
	private JCheckBox savePwdCheckBox = null;
	private JButton cancelButton = null;
	/**
	 * This is the default constructor
	 */
	public PasswordDialog() {
		super();
		initialize();
	}

	/**
	 * This method initializes this
	 * 
	 * @return void
	 */
	private void initialize() {
		this.setSize(300, 200);
		this.setTitle("Password");
		this.setContentPane(getJContentPane());
	}

	/**
	 * This method initializes jContentPane
	 * 
	 * @return javax.swing.JPanel
	 */
	private JPanel getJContentPane() {
		if (jContentPane == null) {
			GridBagConstraints gridBagConstraints41 = new GridBagConstraints();
			gridBagConstraints41.gridx = 4;
			gridBagConstraints41.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints41.gridy = 6;
			GridBagConstraints gridBagConstraints31 = new GridBagConstraints();
			gridBagConstraints31.gridx = 4;
			gridBagConstraints31.anchor = java.awt.GridBagConstraints.WEST;
			gridBagConstraints31.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints31.gridy = 2;
			GridBagConstraints gridBagConstraints21 = new GridBagConstraints();
			gridBagConstraints21.gridx = 4;
			gridBagConstraints21.gridwidth = 1;
			gridBagConstraints21.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints21.gridheight = 2;
			gridBagConstraints21.gridy = 4;
			GridBagConstraints gridBagConstraints4 = new GridBagConstraints();
			gridBagConstraints4.gridx = 0;
			gridBagConstraints4.gridy = 1;
			passwordLabel = new JLabel();
			passwordLabel.setText("Password:");
			GridBagConstraints gridBagConstraints3 = new GridBagConstraints();
			gridBagConstraints3.gridx = 0;
			gridBagConstraints3.gridy = 0;
			nameLabel = new JLabel();
			nameLabel.setText("User Name");
			GridBagConstraints gridBagConstraints2 = new GridBagConstraints();
			gridBagConstraints2.fill = java.awt.GridBagConstraints.HORIZONTAL;
			gridBagConstraints2.gridy = 0;
			gridBagConstraints2.weightx = 1.0;
			gridBagConstraints2.gridwidth = 4;
			jContentPane.add(getJTextField(), gridBagConstraints2);
			jContentPane.add(getComitButton(), gridBagConstraints21);
			jContentPane.add(getSavePwdCheckBox(), gridBagConstraints31);
			jContentPane.add(getCancelButton(), gridBagConstraints41);
			gridBagConstraints2.gridx = 1;
			GridBagConstraints gridBagConstraints1 = new GridBagConstraints();
			gridBagConstraints1.fill = java.awt.GridBagConstraints.BOTH;
			gridBagConstraints1.gridy = 1;
			gridBagConstraints1.weightx = 1.0;
			gridBagConstraints1.gridwidth = 4;
			gridBagConstraints1.anchor = java.awt.GridBagConstraints.CENTER;
			gridBagConstraints1.gridx = 1;
			jContentPane = new JPanel();
			jContentPane.setLayout(new GridBagLayout());
			jContentPane.setName("Password");
			jContentPane.setPreferredSize(new java.awt.Dimension(67,21));
			jContentPane.add(getJPasswordField(), gridBagConstraints1);
			jContentPane.add(nameLabel, gridBagConstraints3);
			jContentPane.add(passwordLabel, gridBagConstraints4);
		}
		return jContentPane;
	}

	/**
	 * This method initializes jPasswordField	
	 * 	
	 * @return javax.swing.JPasswordField	
	 */    
	private JPasswordField getJPasswordField() {
		if (jPasswordField == null) {
			jPasswordField = new JPasswordField();
		}
		return jPasswordField;
	}

	/**
	 * This method initializes jTextField	
	 * 	
	 * @return javax.swing.JTextField	
	 */    
	private JTextField getJTextField() {
		if (jTextField == null) {
			jTextField = new JTextField();
		}
		return jTextField;
	}

	/**
	 * This method initializes comitButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getComitButton() {
		if (comitButton == null) {
			comitButton = new JButton();
			comitButton.setPreferredSize(new java.awt.Dimension(34,21));
		}
		return comitButton;
	}

	/**
	 * This method initializes savePwdCheckBox	
	 * 	
	 * @return javax.swing.JCheckBox	
	 */    
	private JCheckBox getSavePwdCheckBox() {
		if (savePwdCheckBox == null) {
			savePwdCheckBox = new JCheckBox();
		}
		return savePwdCheckBox;
	}

	/**
	 * This method initializes cancelButton	
	 * 	
	 * @return javax.swing.JButton	
	 */    
	private JButton getCancelButton() {
		if (cancelButton == null) {
			cancelButton = new JButton();
			cancelButton.setPreferredSize(new java.awt.Dimension(34,21));
			cancelButton.setText("Cancel");
		}
		return cancelButton;
	}

}
