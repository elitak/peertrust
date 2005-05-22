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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/


package org.peertrust.protege.plugin;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.*;
import javax.swing.event.TableModelListener;

import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.AllowableAction;
import edu.stanford.smi.protege.util.SelectionEvent;
import edu.stanford.smi.protege.util.SelectionListener;
import edu.stanford.smi.protege.widget.*;

/**
 * <p>
 * Class for displaying slot policies in a table[Nr,Type,Policy]. 
 * It also provide a toolbar with button to add and remove slot policies
 * </p><p>
 * $Id: SlotPolicyDisplayPanel.java,v 1.2 2005/05/22 17:56:45 dolmedilla Exp $
 * <br/>
 * Date: 30-Oct-2004
 * <br/>
 * Last changed: $Date: 2005/05/22 17:56:45 $
 * by $Author: dolmedilla $
 * </p>
 * @author Patrice Congo 
 */
public class SlotPolicyDisplayPanel extends JPanel {
	/**
	 * listener delegate used  to listen to selectable events and update the 
	 * the SlotDisplayPanel accordigling.
	 * Implemented is the mechanism to listen for events from the TemplateSlotsWidget
	 * and transmit the selected slot to the policyTableModel.
	 * 
	 */
	private SelectionListener selectionListener = new SelectionListener(){

		public void selectionChanged(SelectionEvent event) {
			Object source = event.getSource();
			if(source instanceof TemplateSlotsWidget){
				Iterator it=((TemplateSlotsWidget)source).getSelection().iterator();
				if(it.hasNext()){
					FrameSlotCombination fsc= (FrameSlotCombination)it.next();
					policyTableModel.setSlot(fsc.getSlot());
				}
			}
		}
		
	};
	
		
	////////////////////////////////Members Variable////////////////////////////////////////////////
	private JTable slotPolicyTable;
	private SlotPolicyTableModel policyTableModel;
	private Project project;
	private PolicyFrameworkModel policyFrameworkModel;
	private JToolBar toolBar;
	
	//////////////////////////////Constructor//////////////////////////////////////////////////////
	public SlotPolicyDisplayPanel(Project project, PolicyFrameworkModel policyFrameworkModel){
		this.project=project;
		this.policyFrameworkModel= new PolicyFrameworkModel(project.getKnowledgeBase());
		policyTableModel= new SlotPolicyTableModel(this.policyFrameworkModel);
		slotPolicyTable= new JTable(policyTableModel);
		slotPolicyTable.setDefaultRenderer(	slotPolicyTable.getColumnClass(1), 
				new ClsPolicyTableCellRenderer(policyFrameworkModel));
		slotPolicyTable.getColumnModel().getColumn(1).setCellEditor(new ChoiceCellEditor(new String[]{"M","D"}));
		toolBar= new JToolBar(JToolBar.HORIZONTAL); 
		
		configLayout();
	}
	///////////////////////////methods/////////////////////////////////////////////////////////////
	public void initialize(){
		return;
	}
	
	public void setInstancess(Instance instance){
	
		if(instance!=null){
			//policyTemplateSlotWidget.setInstance(instance.getDirectType());
			//comboBoxWidget.setInstance(instance);
			//stringListWidget.setInstance(instance);
			///System.out.println("SLOT DISPLAYING INSTANCE:"+instance);
		}
		return;
	}
	
	private Action createAddSlotPolicyAction(){
//		URL url = getClass().getResource("/res/Add.gif");
//        
//        ///System.out.println("ICON URL:"+url + "classURL"+getClass().getClassLoader().getResource("/"));
//        Icon icon= null;
//        if (url != null) {
//            icon = new ImageIcon(url);
//        }
        Icon icon= Icons.getAddIcon();
		Action createPolicyAction= new AllowableAction("Add slot policy",icon,null) {
			public void actionPerformed(ActionEvent arg0) {
					//onCreate();
				//policyFrameworkModel.changeSlotLocalPolicy(policyTableModel.get);
				policyTableModel.addNewPolicy();
				
				System.out.println("adding dada");
			}
						
		}; 
		return createPolicyAction;
	}
	
	private Action createRemoveSlotPolicyAction(){
//		URL url = getClass().getResource("/res/Remove.gif");        
//        ///System.out.println("ICON URL:"+url + "classURL"+getClass().getClassLoader().getResource("/"));
//        Icon icon= null;
//        if (url != null) {
//            icon = new ImageIcon(url);
//        }
        Icon icon= Icons.getRemoveIcon();
		Action createPolicyAction= new AllowableAction("Remove slot policy",icon,null) {
			public void actionPerformed(ActionEvent arg0) {
				int selRow=slotPolicyTable.getSelectedRow();
				if(selRow!=-1){
					policyTableModel.removePolicy(selRow);
				}
			}
						
		}; 
		return createPolicyAction;
	}
	private void configLayout(){
		///make toolbar
//		GridBagLayout gbl= new GridBagLayout();
//		toolBar.setLayout(gbl);
//		GridBagConstraints gbc= new GridBagConstraints();
//		gbc.weightx=1;
//		gbc.gridx=0;
//		gbc.gridy=0;
//		gbc.gridwidth=5;
//		toolBar.add(new JLabel("DADADADADADADA         "),gbc);
//		
//		gbc.gridx=gbc.gridx+1;
//		gbc.weightx=0;
//		gbc.gridwidth=1;
//		JButton b= new JButton(createAddSlotPolicyAction());
//		toolBar.add(b,gbc);
//		
//		gbc.gridx=gbc.gridx+1;
//		gbc.weightx=0;
//		b= new JButton(createRemoveSlotPolicyAction());
//		toolBar.add(b,gbc);
		
		
		//toolBar.add(new JLabel("DADADADADADADA         "),);
		
		toolBar.add(createAddSlotPolicyAction());
		toolBar.add(createRemoveSlotPolicyAction());
		
		//toolBar.setName("dadad");
		/////////toolBar.setAlignmentX(JToolBar.LEFT_ALIGNMENT);
		//toolBar.set
		//GridLayout gl= new GridLayout(1,1);
		this.setLayout(new BorderLayout());
		
		JScrollPane scrollPane= new JScrollPane(slotPolicyTable); 
		this.add(toolBar,BorderLayout.NORTH);
		this.add(scrollPane,BorderLayout.CENTER);
		return;
	}
	
	public void setSlot(Slot slot){
		policyTableModel.setSlot(slot);
		return;
	}
	
//	protected void addRuntimeWidget(Instance instance, Cls associatedCls) {
//        Cls type = instance.getDirectType();
//        if (type == null) {
//            Log.warning("instance has no type", this, "Instance", instance);
//        } else {
//        	Project _project= associatedCls.getProject();
////        	SlotWidget slotWidget=_project.getcreateRuntimeClsWidget(instance);
//////            type.addClsListener(_clsListener);
//////            type.addFrameListener(_frameListener);
////            _currentWidget = _project.createRuntimeClsWidget(instance, associatedCls);
////            _currentWidget.addWidgetListener(_widgetListener);
////            ////TODO add addSelectableHier
////            //_currentWidget.addSelectionListener(policyDisplayPanel);
////            _currentWidget.addSelectionListener(policyClsTreeView.getSelectionListener());
////            System.out.println("_currentWidgetClassType:"+
////            		_currentWidget.getClass().getName());
////            connectPolicyDisplayAndTableWidget();
////            //_currentWidget.addWidgetListener(policyDisplayPanel);
////            JComponent component = createWidgetContainer(_currentWidget);
////            ///********************
////            JSplitPane rightPane= new JSplitPane(JSplitPane.VERTICAL_SPLIT);
////            rightPane.setTopComponent(component);
////            ///rightPane.setBottomComponent(policyDisplayPanel);//createPolicyslotPanel());
////            rightPane.setBottomComponent(policyClsTreeView);
////            
////            _scrollPane.setViewportView(rightPane);
////            //_scrollPane.setViewportView(component);
////            
////            update();
//        }
//    }
	/**
	 * to get a delegate selection listener that will listen for events comming from a selectable and updating
	 * this slot display. 
	 */
	public SelectionListener getSelectionListener(){
		return selectionListener;
	}
	
	public void setPolicyFrameworkModel(
			PolicyFrameworkModel policyFrameworkModel) {
		this.policyFrameworkModel = policyFrameworkModel;
	}
	
	public void addSlotPolicyTableModelListener(TableModelListener l){
		policyTableModel.addTableModelListener(l);
	}
	
	public void removeSlotPolicyTableModelListener(TableModelListener l){
		policyTableModel.removeTableModelListener(l);
	}
}
