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

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;


//import AbstractTabWidget;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.ClsInverseRelationshipPanel;
import edu.stanford.smi.protege.ui.ClsesPanel;
import edu.stanford.smi.protege.ui.InstanceDisplay;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.SelectionEvent;
import edu.stanford.smi.protege.util.SelectionListener;
import edu.stanford.smi.protege.widget.*;
import java.util.*;
/**
 *Tab plugin to show and manipulate classes and slots with policies 
 * @author congo
 */
public class PolicyTabPlugin extends AbstractTabWidget {
	  
    //*****************************///
	private JTabbedPane tabbedPane;
	private ClsesPanel _clsesPanel;
    private ClsInverseRelationshipPanel _inverseRelationshipPanel;
    //private InstanceDisplay _instanceDisplay;
    private PolicyInstanceDisplay _instanceDisplay;
    private SlotPolicyDisplayPanel _slotPolicyDisplay;
    
    private TemplateSlotsWidget policyTemplateSlotWidget;
    private PolicyFrameworkModel policyFrameworkModel;
    protected JComponent createClsDisplay() {
    	System.out.println("creating policyInstanceDisplay");
        //_instanceDisplay = new InstanceDisplay(getProject());
    	_instanceDisplay=new PolicyInstanceDisplay(getProject(),policyFrameworkModel);
    	
        return _instanceDisplay;
    }

    protected ClsesPanel createClsesPanel() {
        PolicyClsesPanel panel = new PolicyClsesPanel(getProject());
        
        panel.addSelectionListener(new SelectionListener() {
            public void selectionChanged(SelectionEvent event) {
                transmitSelection();
            }
        });
        return panel;
    }
    
    protected SlotPolicyDisplayPanel createSlotPolicyDisplay(){
    	_slotPolicyDisplay= new SlotPolicyDisplayPanel(this.getProject(),policyFrameworkModel);
    	return _slotPolicyDisplay;
    }
    protected JComponent createPolicyslotPanel(){
    	policyTemplateSlotWidget= new TemplateSlotsWidget();
    	//policyTemplateSlotWidget.setInstance()
    	return policyTemplateSlotWidget;
    }
    
    protected JComponent createClsesSplitter() {
        JSplitPane pane = createTopBottomSplitPane("ClsesTab.left.top_bottom", 400);
        _clsesPanel = createClsesPanel();
        pane.setTopComponent(_clsesPanel);
        _inverseRelationshipPanel = createInverseRelationshipPanel();
        pane.setBottomComponent(_inverseRelationshipPanel);
        return pane;
    }

    protected ClsInverseRelationshipPanel createInverseRelationshipPanel() {
        final ClsInverseRelationshipPanel panel = new ClsInverseRelationshipPanel(getProject());
        initInverseRelationshipPanelListener(panel);
        return panel;
    }

    protected void initInverseRelationshipPanelListener(final ClsInverseRelationshipPanel panel) {
        panel.addSelectionListener(new SelectionListener() {
            public void selectionChanged(SelectionEvent event) {
                Collection selection = panel.getSelection();
                if (selection.size() == 1) {
                    Cls cls = (Cls) CollectionUtilities.getFirstItem(selection);
                    _clsesPanel.setDisplayParent(cls);
                }
            }
        });
    }

    /**
     * Split the panel in 2: left are the class and super class display; right is the policy view. 
     * @return
     */
    private JComponent createMainSplitter() {
        JSplitPane pane = createLeftRightSplitPane("ClsesTab.left_right", 250);
        pane.setLeftComponent(createClsesSplitter());
        //pane.setRightComponent(createClsDisplay());
        
        tabbedPane= new JTabbedPane();
        tabbedPane.addTab("   Policy View   ", createClsDisplay());
        
        pane.setRightComponent(tabbedPane);
        
        return pane;
    }

    /**
     * create the policy frame work model.
     */
    protected void createPolicyFrameworkModel(){
    	policyFrameworkModel=new PolicyFrameworkModel(getKnowledgeBase());
    	policyFrameworkModel.addPolicyMetaCls();
    	policyFrameworkModel.addPolicyTaggedMetaSlot();
    }
    
    public void initialize() {
    	createPolicyFrameworkModel();
        setIcon(Icons.getClsesIcon());
        setLabel("Policy");
        setShortDescription("Domain Ontology with Policy show");
        add(createMainSplitter());
        setInitialSelection();
        setClsTree(_clsesPanel.getClsesTree());
    }

    public void setFinderComponent(JComponent c) {
        _clsesPanel.setFinderComponent(c);
    }

    private void setInitialSelection() {
        if (_clsesPanel != null) {
            transmitSelection();
        }
    }
    /**
     * transmit selection to display panels
     *
     */
    protected void transmitSelection() {
        // Log.enter(this, "transmitSelection");
        Collection selection = _clsesPanel.getSelection();
        Instance selectedInstance = null;
        Cls selectedCls = null;
        Cls selectedParent = null;
        if (selection.size() == 1) {
            selectedInstance = (Instance) CollectionUtilities.getFirstItem(selection);
            if (selectedInstance instanceof Cls) {
                selectedCls = (Cls) selectedInstance;
                selectedParent = _clsesPanel.getDisplayParent();
            }
        }
        _inverseRelationshipPanel.setCls(selectedCls, selectedParent);
        
        _instanceDisplay.setInstance(selectedInstance);
        //_slotPolicyDisplay.setInstance(selectedInstance);
        /*Collection slots=selectedInstance.getOwnSlots();
        Iterator it=slots.iterator();
        Slot slot=null;
        for(;it.hasNext();){
        	slot=(Slot)it.next();
        	System.out.println("SlotName:"+slot.getName()+" Value:"+slot.getValueType());
        	//policyTemplateSlotWidget.setSlot(slot);
        }
        //policyTemplateSlotWidget.setInstance(selectedInstance);
        //policyTemplateSlotWidget.reload();/**/
    }
    
    
    public static void main(String[] args) {
    	System.out.println("Starting PolicyTabPlugin ........................//////");
    	edu.stanford.smi.protege.Application.main(args);   
        
    }
}
