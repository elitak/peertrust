/*
 *  Copyright (c) 2001 Sun Microsystems, Inc.  All rights
 *  reserved.
 *
 *  Redistribution and use in source and binary forms, with or without
 *  modification, are permitted provided that the following conditions
 *  are met:
 *
 *  1. Redistributions of source code must retain the above copyright
 *  notice, this list of conditions and the following disclaimer.
 *
 *  2. Redistributions in binary form must reproduce the above copyright
 *  notice, this list of conditions and the following disclaimer in
 *  the documentation and/or other materials provided with the
 *  distribution.
 *
 *  3. The end-user documentation included with the redistribution,
 *  if any, must include the following acknowledgment:
 *  "This product includes software developed by the
 *  Sun Microsystems, Inc. for Project JXTA."
 *  Alternately, this acknowledgment may appear in the software itself,
 *  if and wherever such third-party acknowledgments normally appear.
 *
 *  4. The names "Sun", "Sun Microsystems, Inc.", "JXTA" and "Project JXTA"
 *  must not be used to endorse or promote products derived from this
 *  software without prior written permission. For written
 *  permission, please contact Project JXTA at http://www.jxta.org.
 *
 *  5. Products derived from this software may not be called "JXTA",
 *  nor may "JXTA" appear in their name, without prior written
 *  permission of Sun.
 *
 *  THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 *  OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 *  DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 *  ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 *  SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 *  LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 *  USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 *  ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 *  OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 *  OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 *  SUCH DAMAGE.
 *  ====================================================================
 *
 *  This software consists of voluntary contributions made by many
 *  individuals on behalf of Project JXTA.  For more
 *  information on Project JXTA, please see
 *  <http://www.jxta.org/>.
 *
 *  This license is based on the BSD license adopted by the Apache Foundation.
 *
 *  $Id: ConfigDialog.java,v 1.1 2004/12/21 11:09:33 seb0815 Exp $
 */

package net.jxta.edutella.util;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

/**
 * Display a configuration user interface.
 *
 * @author    John Gardner <john@jxtapose.org>
 * @created   Jan 19, 2002
 * @version   $Revision: 1.1 $
 */
public class ConfigDialog extends JDialog {
	private static final int TEXT_SIZE = 20;

	/** logging category */
	private static final Logger log =
		Logger.getLogger(ConfigDialog.class.getName());

	public static final int OK_CLICKED = 0;
	public static final int CANCEL_CLICKED = 1;

	private static final String[] COMPLETED =
		{
			" has been saved succefully." + "\n",
			" has been downloaded succefully." + "\n" };

	private static final String[] FAILED =
		{
			"There was an error while saving ",
			"There was an error while downloading " };

	/** map of controls associated with configItems */
	private Map entryPanels;
	private boolean reConfigure = false;
	private int clickedButton = -1;
	private String dialogDescription;

	private Configurator configurator;

	/**
	 * Constructor for the UserConfigDialog object
	 */

	public ConfigDialog(
		String title,
		String dialogDescription,
		Configurator configurator) {
		super(JOptionPane.getRootFrame(), title, true);

		this.configurator = configurator;
		this.dialogDescription = dialogDescription;
		entryPanels = new java.util.HashMap();
		buildUI();

		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		int prefXOrigin = screenSize.width / 2 - this.getSize().width / 2;
		int prefYOrigin = screenSize.height / 2 - this.getSize().height / 2;

		setLocation(prefXOrigin, prefYOrigin);
	}

	public int getClickedButton() {
		return clickedButton;
	}

	private void buildUI() {
		JPanel contentPane = (JPanel) getContentPane();
		contentPane.setLayout(new BoxLayout(contentPane, BoxLayout.Y_AXIS));
		contentPane.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

		// create and add the label
		JLabel title = new JLabel(dialogDescription);

		JPanel titlePanel = new JPanel();
		titlePanel.add(BorderLayout.CENTER, title);
		titlePanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		titlePanel.setOpaque(false);
		contentPane.add(titlePanel);

		JPanel mainPanel = new JPanel();
		//mainPanel.setLayout(new BorderLayout());
		mainPanel.setOpaque(false);
		mainPanel.setBorder(BorderFactory.createEtchedBorder());
		mainPanel.add(BorderLayout.CENTER, createControls());

		JPanel borderPanel = new JPanel();
		borderPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
		borderPanel.setOpaque(false);
		borderPanel.setLayout(new GridLayout(1, 1));
		borderPanel.add(mainPanel);

		contentPane.add(borderPanel);

		final JButton cancelButton = new JButton("Cancel");
		final JButton okButton = new JButton("OK");
		contentPane.getRootPane().setDefaultButton(okButton);

		contentPane.add(Box.createVerticalStrut(5));

		Box buttonPanel = Box.createHorizontalBox();
		buttonPanel.add(Box.createHorizontalStrut(5));
		buttonPanel.add(okButton);
		buttonPanel.add(Box.createHorizontalStrut(5));
		buttonPanel.add(Box.createHorizontalGlue());
		buttonPanel.add(cancelButton);
		buttonPanel.add(Box.createHorizontalStrut(5));

		contentPane.add(buttonPanel);

		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickedButton = CANCEL_CLICKED;
				if (reConfigure) {
					dispose();
				} else {
					int selection =
						JOptionPane.showConfirmDialog(
							ConfigDialog.this,
							"Cancelling will exit the application. Do you really want to exit?",
							"Question",
							JOptionPane.YES_NO_OPTION);
					if (selection == JOptionPane.YES_OPTION) {
						System.exit(234);
					}
				}
			}
		});

		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				clickedButton = OK_CLICKED;
				userConfigDone();
			}
		});

		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				cancelButton.doClick();
			}
		});

		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);

		pack();
	}

	private Component createControls() {
		Box mainPane = Box.createVerticalBox();
		for (Iterator i = configurator.getMissingOptions().iterator();
			i.hasNext();
			) {
			Option o = (Option) i.next();
			ConfigDialogEntry entry;

			entry = new ConfigDialogEntry(o);

			entryPanels.put(o.getLongName(), entry);
			mainPane.add(entry);
		}

		return mainPane;
	}

	private final void userConfigDone() {
		try {
			acceptChanges();
			dispose();
		} catch (ValidationException ve) {
			// validation failed.
			StringBuffer message = new StringBuffer();
			message.append("Some of the entries are invalid or missing.  \n");
			List problems = ve.getProblems();
			// assert: problems.size != 0
			if (problems.size() == 1) {
				message.append(
					"Please check the value for \n" + problems.get(0));
				message.append('.');
			} else {
				message.append("Please check the values for: \n     ");
				Iterator iter = problems.iterator();
				message.append(iter.next());
				message.append('\n');
				message.append("     ");
				while (iter.hasNext()) {
					message.append(iter.next());
					message.append('\n');
					message.append("     ");
				}
				//message.append('.');
			}

			JOptionPane.showMessageDialog(
				ConfigDialog.this,
				message.toString(),
				"Missing Information",
				JOptionPane.ERROR_MESSAGE);
		}
	}

	private void acceptChanges() throws ValidationException {
		ValidationException validationException = null;

		for (Iterator i = configurator.getOptions().iterator(); i.hasNext();) {
			Option o = (Option) i.next();

			ConfigDialogEntry entry = (ConfigDialogEntry) entryPanels.get(o.getLongName());
			if (entry != null) {
				if (!"".equals(entry.getEntryValue())) {
					o.setValue(entry.getEntryValue());
				}

				if (o.isRequired()
					&& (o.getValue() == null || o.getValue().length() == 0)) {
					if (validationException == null) {
						validationException = new ValidationException();
					}
					validationException.addProblem(o.getLabel());
				}
			}
		}

		if (validationException != null) {
			throw validationException;
		}
	}

	class ValidationException extends Exception {
		ValidationException() {
			super();
			problems = new java.util.ArrayList();
		}
		void addProblem(String problem) {
			problems.add(problem);
		}
		List getProblems() {
			return problems;
		}
		private List problems;
	}

	public void showDialog() {
		if ( entryPanels.size() > 0 ) {
			pack();
			setLocationRelativeTo(null);
			setVisible(true);
		} else {
			dispose();
		}
	}

} // UserConfigDialog
