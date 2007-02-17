/**
 * Copyright 2004
 * 
 * This file is part of Peertrust.
 * 
 * Peertrust is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Peertrust is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Peertrust; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
USA
*/

package org.policy.action.view;


import javax.swing.JFrame;
import java.awt.Dimension;

import java.awt.Color;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JButton;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.policy.action.AbstractExecutorWrapper;
import org.policy.action.ActionResult;
import org.policy.action.FunctionFailureException;
import org.policy.action.NoSuchFunctionException;
import org.policy.action.packages.JDBCWrapper;
import org.policy.action.packages.LDAPWrapper;
import org.policy.action.packages.RegExpWrapper;
import org.policy.action.parser.Parse;
import org.policy.action.tools.Functions;
import org.policy.action.view.ResultTable;

import java.awt.Font;

/**
 * <p>
 * This frame is used for input and shows the execution results.
 * </p><p>
 * $Id: QueryGUI.java,v 1.1 2007/02/17 16:59:28 dolmedilla Exp $
 * <br/>
 * Date: 05-May-2006
 * <br/>
 * Last changed: $Date: 2007/02/17 16:59:28 $
 * by $Author: dolmedilla $
 * </p>
 * @author C. Jin && M. Li
 */
public class QueryGUI extends JFrame{

	private JPanel jContentPane = null;
	private JScrollPane eingabePane = null; 
	private JScrollPane ausgabePane = null; 
	private JTextArea eingabeTextArea = null;
	private JButton okButton = null;
	private ResultTable table = null;

	/**
	 * constructs a new frame
	 *
	 */
	public QueryGUI() {
		super();
		initialize();
	}

	private void initialize() {
        this.setSize(new Dimension(550, 400));
        this.setContentPane(getJContentPane());
        this.setTitle("Query");
        this.setVisible(true);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); 
	}

	private JPanel getJContentPane() {
		if (jContentPane == null) {
			jContentPane = new JPanel();
			jContentPane.setLayout(null);
			jContentPane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			jContentPane.add(getJEingabePane(), null);
			jContentPane.add(getOkButton(), null);
			jContentPane.add(getJAusgabePane(),null);
		}
		return jContentPane;
	}

	public JScrollPane getJEingabePane() {
		if (eingabePane == null) {
			eingabePane = new JScrollPane();
			eingabePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			eingabePane.setViewportBorder(BorderFactory.createTitledBorder(null, "Input", TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Verdana", Font.BOLD, 12), new Color(51, 51, 51)));
			eingabePane.setViewportView(getEingabeTextArea());
			eingabePane.setSize(new Dimension(432, 100));
		}
		return eingabePane;
	}
		
	public JScrollPane getJAusgabePane() {
//		if (ausgabePane == null) {
			ausgabePane = new JScrollPane();
			ausgabePane.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.RAISED));
			ausgabePane.setViewportBorder(BorderFactory.createTitledBorder(null, null, TitledBorder.DEFAULT_JUSTIFICATION, TitledBorder.DEFAULT_POSITION, new Font("Verdana", Font.BOLD, 12), new Color(51, 51, 51)));
			ausgabePane.setSize(new Dimension(432, 100));
//		}
		return ausgabePane;
	}

	public JTextArea getEingabeTextArea(){
		if (eingabeTextArea == null){
			eingabeTextArea = new JTextArea("");
			eingabeTextArea.setFont(new Font("Verdana", Font.PLAIN, 12));
			eingabeTextArea.setVisible(true);
			eingabeTextArea.setEditable(true);
			eingabeTextArea.setText("");
			eingabeTextArea.setRows(2);
			eingabeTextArea.setLineWrap(true);
			eingabeTextArea.setWrapStyleWord(true);
       	}
      
      	return eingabeTextArea;
    }

	
	public JButton getOkButton() {
		if (okButton == null) {
			okButton = new JButton();
			okButton.setBounds(new Rectangle(453, 10, 50, 25));
			okButton.setFont(new Font("Verdana", Font.BOLD, 8));
			okButton.setText("OK");
		}
		okButton.addActionListener(new java.awt.event.ActionListener() {
			@SuppressWarnings("unchecked")
			public void actionPerformed(java.awt.event.ActionEvent e) {
				Parse parser = new Parse(eingabeTextArea.getText());
				try {
					String function = parser.getFunctionName();
					String[] arguments =parser.getArgument();
					String[] inputVars = parser.getInVarsName();
					String packageName = parser.getPackageName();
					if (packageName.equals(Functions.PACKAGE_JDBC)){
						AbstractExecutorWrapper jdbcWrapper = new JDBCWrapper();
						ActionResult actionResult = jdbcWrapper.executeAction(function, arguments, inputVars);
						if (null != actionResult){
							getTable().init(actionResult);
							table.getJTablePane();
						}
					}
					else if(packageName.equals(Functions.PACKAGE_REGEX)){
						AbstractExecutorWrapper regExpWrapper = new RegExpWrapper();
						ActionResult actionResult = regExpWrapper.executeAction(function, arguments, inputVars);
						if (null != actionResult){
							getTable().init(actionResult);
							table.getJTablePane();
						}
					}
					else if(packageName.equals(Functions.PACKAGE_LDAP)){
						AbstractExecutorWrapper ldapWrapper = new LDAPWrapper();
						ActionResult actionResult = ldapWrapper.executeAction(function, arguments, inputVars);
						if (null != actionResult){
							getTable().init(actionResult);
							table.getJTablePane();
						}
					}
					
					else {
						JOptionPane.showMessageDialog(null, "packagename is not correct!","Error",
		     				    JOptionPane.ERROR_MESSAGE);
		     				   return;
					}
				} catch (NoSuchFunctionException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),"Error",
	     				    JOptionPane.ERROR_MESSAGE);
	     				   return;
				} catch (FunctionFailureException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),"Error",
	     				    JOptionPane.ERROR_MESSAGE);
	     				   return;
				} catch (IllegalArgumentException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(),"Error",
	     				    JOptionPane.ERROR_MESSAGE);
	     				   return;
				}
			}
		});
		return okButton;
	}
	
	public ResultTable getTable(){
		if (null == table)
			table = new ResultTable(ausgabePane);
		
		return table;
	}
} 
