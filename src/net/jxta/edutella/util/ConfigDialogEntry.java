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
 *  $Id: ConfigDialogEntry.java,v 1.1 2004/07/08 15:12:30 dolmedilla Exp $
 */
package net.jxta.edutella.util;
import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

public class ConfigDialogEntry extends JPanel {
	private JLabel entryLabel;
	private JTextField entryField;
	private Option option;

	/**
	 * Contruct a entry label and an entry field.
	 * @param labelText string title for entry field
	 * @param textFieldLength size of entry field to create.
	 * @param password if true, make the entry field a password field.
	 */
	public ConfigDialogEntry(Option option) {
		this.option = option;
		buildUI();
	}

	/**
	 * Accessor for getting the text in the JTextField.
	 * 
	 * @return String
	 */
	public String getEntryValue() {
		return entryField.getText();
	}

	/**
	 * Accessor for the JTextField.
	 * 
	 * @return JTextField
	 */
	public JTextField getEntryField() {
		return entryField;
	}

	/**
	 * Helper method used to build the UI.
	 * 
	 * @param label  
	 * @return columns
	 */
	private void buildUI() {
		String label = option.getLabel();
		if (label == null || label.equals("")) {
			label = " ";
		}

		entryLabel = new JLabel(label);
		if (option.isPassword()) {
			entryField = new JPasswordField();
		} else {
			entryField = new JTextField();
		}

		if (option.getValue() != null) {
			entryField.setText(option.getValue());
		}
		if (option.getDescription() != null) {
			entryLabel.setToolTipText(option.getDescription());
			entryField.setToolTipText(option.getDescription());
		}
		entryField.setColumns(30);

		setOpaque(false);
		setLayout(new BorderLayout());
		JPanel containerPanel = new JPanel();
		containerPanel.setOpaque(false);
		containerPanel.setLayout(new BorderLayout());
		containerPanel.add(entryLabel, BorderLayout.WEST);
		containerPanel.add(Box.createHorizontalGlue());
		containerPanel.add(Box.createHorizontalStrut(10), BorderLayout.CENTER);
		if (option.getType() == File.class) {
			JPanel fileSelection = new JPanel();
			fileSelection.setOpaque(false);
			fileSelection.setLayout(
				new BoxLayout(fileSelection, BoxLayout.X_AXIS));
			fileSelection.add(entryField);
			JButton loadButton = new JButton("Load...");
			loadButton.setActionCommand("load");
			ButtonListener l = new ButtonListener();
			loadButton.addActionListener(l);
			fileSelection.add(loadButton);
			containerPanel.add(fileSelection, BorderLayout.EAST);
		} else {
			containerPanel.add(entryField, BorderLayout.EAST);
		}
		containerPanel.add(Box.createVerticalStrut(5), BorderLayout.SOUTH);
		add(containerPanel, BorderLayout.NORTH);
	}

	private class ButtonListener implements ActionListener {
		private JFileChooser fileChooser;

		public ButtonListener() {
			fileChooser = new JFileChooser();
			File samples = new File("./samples");
			if (!samples.exists()) {
				samples = new File(".");
			}
			fileChooser.setCurrentDirectory(samples);
			fileChooser.setMultiSelectionEnabled(true);
		}

		public void actionPerformed(ActionEvent event) {
			if (event.getActionCommand().equals("load")) {
				int returnVal =
					fileChooser.showOpenDialog(ConfigDialogEntry.this);
				if (returnVal == JFileChooser.APPROVE_OPTION) {
					File files[] = fileChooser.getSelectedFiles();
					String text = "";
					char delim = File.pathSeparatorChar;
					for (int i = 0; i < files.length; i++) {
						if (i > 0) {
							text += delim;
						}
						text += files[i].toString();
					}
					entryField.setText(text);
				}
			}
		}
	}

}
