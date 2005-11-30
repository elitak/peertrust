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

package g4mfs.impl.org.peertrust.protege.plugin;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.Collection;

import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.ImageIcon;


import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.ClsesPanel;
import edu.stanford.smi.protege.util.AllowableAction;

/**
 * <p>
 * This class is used to show the class with there policies.
 * The classes are shown in a tree; with the selected class as root
 * and it parents as "child". The class policies are shown in a table.
 * </p><p>
 * $Id: PolicyClsesPanel.java,v 1.1 2005/11/30 10:35:15 ionut_con Exp $
 * <br/>
 * Date: 30-Oct-2004
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:15 $
 * by $Author: ionut_con $
 * </p>
 * @author Patrice Congo 
 */
public class PolicyClsesPanel extends ClsesPanel {
	protected PolicyFrameworkModel policyFrameworkModel;
	
	protected AllowableAction createPolicyClassAction;
	/**
	 * @param project
	 */
	public PolicyClsesPanel(Project project) {
		super(project);
		policyFrameworkModel= new PolicyFrameworkModel(project.getKnowledgeBase());
		//_labeledComponent.addHeaderButton(getCreatePolicyClassAction()); 
		
	}
	/**
	 * Utility methode to create an action that make a PolicyTaggedCls
	 * using the policyFramework model.
	 * @return an Action that responds to action events by creating a PolicyTaggedCls
	 */
	public Action getCreatePolicyClassAction(){
//		URL url = getClass().getResource("/res/Create.gif");
//		
//		Icon icon= new ImageIcon(url);
		Icon icon;
		try{
			icon=Icons.getCreateClsIcon();
		}catch(Throwable th){
			URL url = getClass().getResource("/res/class.create.gif");
			icon= new ImageIcon(url);
		}
		
		createPolicyClassAction= new AllowableAction("Add class with policy",icon,null) {
			public void actionPerformed(ActionEvent arg0) {
				Collection parents = _subclassPane.getSelection();
                if (!parents.isEmpty()) {
                    Cls cls = getKnowledgeBase().createCls(null, parents,policyFrameworkModel.getPolicyMetaCls());
                    policyFrameworkModel.setPolicyType(cls,"D");
                    _subclassPane.extendSelection(cls);
                }
			}
		};
		
		return createPolicyClassAction;
	}
	
	/**
	 * Override to create an action that build a PolicyTaggedCls.
	 * @return
	 */
	protected AllowableAction getCreateClsAction() {
		//return super.getCreateClsAction();
		return (AllowableAction)getCreatePolicyClassAction();
	}
	
	///not in 3.0
//	protected void reload() {
//		super.reload();
//		Object selection = _relationshipView.getSelectedItem();
////		if(selection==null){
////			enableButton(createPolicyClassAction, true);
////		}else if(selection.equals(SUBCLASS_RELATIONSHIP)){
////			enableButton(createPolicyClassAction, true);
////		}else{
////			enableButton(createPolicyClassAction, false);
////		}
//		
//		return;
//	}
	
//	protected void relload() {
//        Frame selectedFrame = (Frame) CollectionUtilities.getFirstItem(getSelection());
//        Object selection = null;
//        if (_relationshipView != null) {
//            selection = _relationshipView.getSelectedItem();
//        }
//        if (selection == null) {
//            selection = SUBCLASS_RELATIONSHIP;
//        }
//        if (selection.equals(SUBCLASS_RELATIONSHIP)) {
//            if (selectedFrame instanceof Cls) {
//                _subclassPane.setSelectedCls((Cls) selectedFrame);
//            }
//            loadComponent(_subclassPane);
//            enableButtons(true);
//        } else if (selection.equals(REFERENCED_RELATIONSHIP)) {
//            _relationshipPane.load(selectedFrame, null);
//            loadComponent(_relationshipPane);
//            enableButtons(false);
//        } else {
//            Slot slot = (Slot) selection;
//            _relationshipPane.load(selectedFrame, slot);
//            loadComponent(_relationshipPane);
//            enableButtons(false);
//        }
//        notifySelectionListeners();
//    }

}
