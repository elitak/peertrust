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

//import javax.swing.nel;
import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.*;

import edu.stanford.smi.protege.event.ClsEvent;
import edu.stanford.smi.protege.event.ClsListener;
import edu.stanford.smi.protege.model.*;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.util.AllowableAction;
import edu.stanford.smi.protege.util.SelectionEvent;
import edu.stanford.smi.protege.util.SelectionListener;

/**
 * This class is used to show the class with there policies.
 * The classes are shown in a tree; with the selected class as root
 * and it parents as "child". The class policies are shown in a table.
 * 
 * @author Patrice Congo 
 * @date 30-Oct-2004
 * Last changed  $Date: 2005/04/01 10:04:05 $
 * by $Author: dolmedilla $
 * $Id: PolicyClassTreeView.java,v 1.1 2005/04/01 10:04:05 dolmedilla Exp $
 */
public class PolicyClassTreeView extends JPanel{//SelectableContainer {
	class PolicyTreeNode extends DefaultMutableTreeNode{
		
		Cls cls;
		Slot policySlot;
		/**
		 * @param arg0
		 */
		public PolicyTreeNode(Cls cls) {
			super();
			this.cls=cls;
			this.setUserObject(cls.getBrowserText());
			this.setAllowsChildren(true);
			//getPolicySlot();
			// TODO Auto-generated constructor stub
		}
		
		public Cls getCls(){
			return cls;
		}
		
		private Slot getPolicySlot(){
			policySlot=null;
			Collection c=cls.getOwnSlots();//getDirectTemplateSlots();//OwnSlotValues(policy)
			
			for(Iterator it=c.iterator(); it.hasNext();){
				
				Slot slot=(Slot)it.next();
				///System.out.println("Looking for Policy TPSlotName:"+slot.getBrowserText());
				if(slot.getBrowserText().equals(PolicyFrameworkModel.DEFAULT_POLICY_SLOT_VALUE)){//policyFrameworkModel.isPolicyTaggedSlot(slot)){
					policySlot= slot;//slot.
					return policySlot;
				}
			}
			return policySlot;
		}
		
		public Collection getPolicies(){
			//Vector policies=new Vector();			
			/*Collection c= cls.getDirectTemplateSlots();
			for(Iterator it=c.iterator(); it.hasNext();){
				Slot slot=(Slot)it.next();
				if(policyFrameworkModel.isPolicyTaggedSlot(slot)){
					
				}
			}/**/
			getPolicySlot();
			if(policySlot==null){
				////System.out.println("No POLICY FOUND!!!!!!!!");
				return new Vector(2);
			}else{
				////System.out.println("Found************:"+policySlot.getBrowserText());
				return policySlot.getOwnSlots();//getValues();
			}
		}
}
	
	/************/
	private TreeSelectionListener treeSelectionListener= new TreeSelectionListener(){

		public void valueChanged(TreeSelectionEvent selEvent) {
			PolicyTreeNode selNode= (PolicyTreeNode)tree.getLastSelectedPathComponent();
			if(selNode==null){
				return;
			}else{
				Collection c=selNode.getPolicies();
				////System.out.println("Tree selection:"+selNode.getCls().getBrowserText()+" policiesNr:"+c.size());
				//Collection c=selNode.getPolicies();
				//((DefaultTableModel)policyTable.getModel()).addRow(c.toArray());
				policyFrameworkModel.getLocalPolicy(selNode.getCls(),PolicyFrameworkModel.DEFAULT_POLICY_SLOT_VALUE,true);
				//TODO policyTableModel.setCls(selNode.getCls());
				policyTableModel.setCls(selNode.getCls());
				return;
			}
		}
		
	};
	
	private ClsListener clsListerner= new ClsListener() {
		/* (non-Javadoc)
		 * @see edu.stanford.smi.protege.event.ClsListener#directInstanceAdded(edu.stanford.smi.protege.event.ClsEvent)
		 */
		public void directInstanceAdded(ClsEvent event) {
			policyTableModel.resetTableModel();

		}

		/* (non-Javadoc)
		 * @see edu.stanford.smi.protege.event.ClsListener#directInstanceRemoved(edu.stanford.smi.protege.event.ClsEvent)
		 */
		public void directInstanceRemoved(ClsEvent event) {
			policyTableModel.resetTableModel();
		}

		/* (non-Javadoc)
		 * @see edu.stanford.smi.protege.event.ClsListener#directSubclassAdded(edu.stanford.smi.protege.event.ClsEvent)
		 */
		public void directSubclassAdded(ClsEvent event) {
			policyTableModel.resetTableModel();

		}

		/* (non-Javadoc)
		 * @see edu.stanford.smi.protege.event.ClsListener#directSubclassMoved(edu.stanford.smi.protege.event.ClsEvent)
		 */
		public void directSubclassMoved(ClsEvent event) {
			policyTableModel.resetTableModel();
		}

		/* (non-Javadoc)
		 * @see edu.stanford.smi.protege.event.ClsListener#directSubclassRemoved(edu.stanford.smi.protege.event.ClsEvent)
		 */
		public void directSubclassRemoved(ClsEvent event) {
			policyTableModel.resetTableModel();
		}

		/* (non-Javadoc)
		 * @see edu.stanford.smi.protege.event.ClsListener#directSuperclassAdded(edu.stanford.smi.protege.event.ClsEvent)
		 */
		public void directSuperclassAdded(ClsEvent event) {
			policyTableModel.resetTableModel();
		}

		/* (non-Javadoc)
		 * @see edu.stanford.smi.protege.event.ClsListener#directSuperclassRemoved(edu.stanford.smi.protege.event.ClsEvent)
		 */
		public void directSuperclassRemoved(ClsEvent event) {
			policyTableModel.resetTableModel();
		}

		/* (non-Javadoc)
		 * @see edu.stanford.smi.protege.event.ClsListener#templateFacetAdded(edu.stanford.smi.protege.event.ClsEvent)
		 */
		public void templateFacetAdded(ClsEvent event) {
			policyTableModel.resetTableModel();
		}

		/* (non-Javadoc)
		 * @see edu.stanford.smi.protege.event.ClsListener#templateFacetRemoved(edu.stanford.smi.protege.event.ClsEvent)
		 */
		public void templateFacetRemoved(ClsEvent event) {
			policyTableModel.resetTableModel();
		}

		/* (non-Javadoc)
		 * @see edu.stanford.smi.protege.event.ClsListener#templateFacetValueChanged(edu.stanford.smi.protege.event.ClsEvent)
		 */
		public void templateFacetValueChanged(ClsEvent event) {
			policyTableModel.resetTableModel();
		}

		/* (non-Javadoc)
		 * @see edu.stanford.smi.protege.event.ClsListener#templateSlotAdded(edu.stanford.smi.protege.event.ClsEvent)
		 */
		public void templateSlotAdded(ClsEvent event) {
			policyTableModel.resetTableModel();
		}

		/* (non-Javadoc)
		 * @see edu.stanford.smi.protege.event.ClsListener#templateSlotRemoved(edu.stanford.smi.protege.event.ClsEvent)
		 */
		public void templateSlotRemoved(ClsEvent event) {
			policyTableModel.resetTableModel();
		}

		/* (non-Javadoc)
		 * @see edu.stanford.smi.protege.event.ClsListener#templateSlotValueChanged(edu.stanford.smi.protege.event.ClsEvent)
		 */
		public void templateSlotValueChanged(ClsEvent event) {
			policyTableModel.resetTableModel();

		}
	};
//	new SelectionListener() {
//        public void selectionChanged(SelectionEvent event) {
//            onSelectionChange();
//            ///notifySelectionListeners();
//        }
//    };
	////////////////////////////////////////////////////////////////////////////////
	private SelectionListener selectionListener= new SelectionListener(){

		public void selectionChanged(SelectionEvent event) {
			onSelectionChange();			
		}
		
	};
	
	///////////////////////////////////////////////////////////////////////////////
	/*************************************************************************/
	JTree tree; 
	DefaultTreeModel treeModel;
	DefaultMutableTreeNode root;
	PolicyFrameworkModel policyFrameworkModel;
	/////
	JToolBar toolBar= new JToolBar();
	
	/////////////////////////// 
	JTable policyTable;
	ClsPolicyTableModel policyTableModel;  
	
	public PolicyClassTreeView(Project project){
		root= new PolicyTreeNode(project.getKnowledgeBase().getRootCls());//new DefaultMutableTreeNode(project.getKnowledgeBase().getRootCls());
		treeModel= new DefaultTreeModel(root);
		tree= new JTree();
		//tree.setCellRenderer(FrameRenderer.createInstance());
		policyFrameworkModel= new PolicyFrameworkModel(project.getKnowledgeBase());
		URL url= getClass().getResource("res/Class.gif");
		//System.out.println("Class Tree ICON:"+url);
		if(url!=null){
			Icon icon= new ImageIcon(url);
			DefaultTreeCellRenderer cellRenderer=(DefaultTreeCellRenderer)tree.getCellRenderer();
			cellRenderer.setClosedIcon(icon);
			cellRenderer.setOpenIcon(icon);
			//cellRenderer.set
		}
		tree.setModel(treeModel);
		policyTableModel= new ClsPolicyTableModel(policyFrameworkModel);
		policyTable= new JTable(policyTableModel);
		policyTable.setDefaultRenderer(	policyTable.getColumnClass(0), 
										new ClsPolicyTableCellRenderer(policyFrameworkModel));
		policyTable.getColumnModel().getColumn(1).setCellEditor(new ChoiceCellEditor(new String[] {"M","D"}));
		policyTable.getColumnModel().getColumn(4).setCellEditor(new ChoiceCellEditor());
		
		//policyTable.getTableHeader().setName("Dadad");
		configLayout();
		tree.addTreeSelectionListener(treeSelectionListener);
	}
	
	public void setRootCls(Cls cls){
		//The root class is the bottom of the class hierarchy.
		//treeModel.removeAll();
		Collection c;
		if(cls==null){
			cls= this.policyFrameworkModel.getKnowledgeBase().getRootCls();
		}
		policyTableModel.setCls(cls);
		
		///make Tree Structure
		
//		Collection c;
//		if(cls==null){
//			cls= this.policyFrameworkModel.getKnowledgeBase().getRootCls();
//		}
		c= cls.getDirectSuperclasses();
	
		Vector clsWithPolicy=new Vector();
		Cls actualCls;
		///stop listening for all root cls events
		
		root=new PolicyTreeNode(cls);
		treeModel= new DefaultTreeModel(root);
		
		
		Vector parentFrontier= new Vector(20);
		Vector childFrontier= new Vector(20);
		PolicyTreeNode parentNode= (PolicyTreeNode)root;
		PolicyTreeNode childNode=null;
		///add root direct super
		int nodeIndex=0;
		for(Iterator it=c.iterator();it.hasNext();){
			actualCls= (Cls)it.next();
			childNode = new PolicyTreeNode(actualCls);
			treeModel.insertNodeInto(childNode, parentNode, nodeIndex); nodeIndex++;
			childFrontier.add(childNode);
		}
		parentFrontier.addAll(childFrontier);
		childFrontier.removeAllElements();
		//System.out.println("***********************************************************");
		//System.out.println("parentFrontier:"+parentFrontier.size());
		//Vector parentFrontier= new Vector(20);
		//Vector childFrontier= new Vector(20);
		
		do{
			//add to treeModel
			for(Iterator it=parentFrontier.iterator(); it.hasNext();){
				parentNode=(PolicyTreeNode)it.next();
				actualCls=parentNode.getCls();
				c=actualCls.getDirectSuperclasses();
				//System.out.println("actualCls:"+actualCls.getBrowserText()+" superNr:"+c.size());
				nodeIndex=0;
				for(Iterator itChild=c.iterator();itChild.hasNext();){
					actualCls= (Cls)itChild.next();
					childNode = new PolicyTreeNode(actualCls);
					treeModel.insertNodeInto(childNode, parentNode, nodeIndex); nodeIndex++;
					childFrontier.add(childNode);
				}
			}
			parentFrontier.removeAllElements();
			//System.out.println("childFrontierAfter:"+childFrontier.size());
			parentFrontier.addAll(childFrontier);
			childFrontier.removeAllElements();
			//System.out.println("parentFrontierAfter:"+parentFrontier.size());
		}while(!parentFrontier.isEmpty());
		tree.setModel(treeModel);
		//System.out.println("Nodes InTree:"+treeModel.getChildCount(treeModel.getRoot()));
		
	}
	
	public void onSelectionChange(){
		PolicyTreeNode selNode= (PolicyTreeNode)tree.getLastSelectedPathComponent();
		
		if(selNode==null){
			return;
		}else{
			policyTableModel.setCls(selNode.getCls());
			
		}
	}
	
	private void configLayout(){
		JSplitPane splitterPane= new JSplitPane(JSplitPane.HORIZONTAL_SPLIT);
		tree.setPreferredSize(new Dimension(100,70));
		splitterPane.add(tree,JSplitPane.LEFT);
		
		
		//scroll pane for table
		JScrollPane scrollPane = new JScrollPane(policyTable);
		policyTable.setPreferredScrollableViewportSize(new Dimension(500, 70));
		//toolbar
		toolBar.add(createAddSlotPolicyAction());
		toolBar.add(createRemoveSlotPolicyAction());
		//
		JPanel ttPanel= new JPanel();
		ttPanel.setLayout(new BorderLayout());
		ttPanel.add(scrollPane,BorderLayout.CENTER);
		ttPanel.add(toolBar, BorderLayout.NORTH);
		
		/*container.setLayout(new BorderLayout());
		container.add(table.getTableHeader(), BorderLayout.PAGE_START);
		container.add(table, BorderLayout.CENTER);/**/
		splitterPane.add(ttPanel,JSplitPane.RIGHT);
		//splitterPane.add(policyTable, JSplitPane.RIGHT);
		this.setLayout(new GridLayout(1,1));
		this.add(splitterPane);
		//tree.addTreeSelectionListener(treeSel)
		return;
	}
	public void resetView(){
		setRootCls(((PolicyTreeNode)root).getCls());
	}
	public void resetPolicyTableModel(){
		policyTableModel.resetTableModel();
	}
	
	public SelectionListener getSelectionListener(){
		return this.selectionListener;
	}
	/*************************************************************/
     
	private Action createAddSlotPolicyAction(){
		URL url = getClass().getResource("res/Add.gif");
        
        //System.out.println("ICON URL:"+url + "classURL"+getClass().getClassLoader().getResource("/"));
//        Icon icon= null;
//        if (url != null) {
//            icon = new ImageIcon(url);
//        }
        Icon icon= Icons.getAddSlotIcon();
        
		Action createPolicyAction= new AllowableAction("Add slot with policy",icon,null) {
			public void actionPerformed(ActionEvent arg0) {
					//onCreate();
				//policyFrameworkModel.changeSlotLocalPolicy(policyTableModel.get);
				policyTableModel.addNewPolicy(); 				
				//System.out.println("adding dada");
			}
						
		}; 
		return createPolicyAction;
	}
	
	private Action createRemoveSlotPolicyAction(){
//		URL url = getClass().getResource("res/Remove.gif");        
//        //System.out.println("ICON URL:"+url + "classURL"+getClass().getClassLoader().getResource("/"));
//        Icon icon= null;
//        if (url != null) {
//            icon = new ImageIcon(url);
//        }
        Icon icon= Icons.getDeleteSlotIcon();
		Action createPolicyAction= new AllowableAction("Remove slot with policy",icon,null) {
			public void actionPerformed(ActionEvent arg0) {
				int selRow=policyTable.getSelectedRow();
				if(selRow!=-1){
					policyTableModel.removePolicy(selRow);
				}
			}
						
		}; 
		return createPolicyAction;
	}

}
