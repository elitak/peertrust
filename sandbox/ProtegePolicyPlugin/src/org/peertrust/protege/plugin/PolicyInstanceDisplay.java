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
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.plaf.TabbedPaneUI;
import javax.swing.text.Utilities;



import edu.stanford.smi.protege.event.ClsAdapter;
import edu.stanford.smi.protege.event.ClsEvent;
import edu.stanford.smi.protege.event.ClsListener;
import edu.stanford.smi.protege.event.FrameAdapter;
import edu.stanford.smi.protege.event.FrameEvent;
import edu.stanford.smi.protege.event.FrameListener;
import edu.stanford.smi.protege.event.InstanceEvent;
import edu.stanford.smi.protege.event.InstanceListener;
import edu.stanford.smi.protege.event.ProjectAdapter;
import edu.stanford.smi.protege.event.ProjectEvent;
import edu.stanford.smi.protege.event.ProjectListener;
import edu.stanford.smi.protege.event.WidgetAdapter;
import edu.stanford.smi.protege.event.WidgetEvent;
import edu.stanford.smi.protege.event.WidgetListener;
import edu.stanford.smi.protege.model.Cls;
import edu.stanford.smi.protege.model.FrameSlotCombination;
import edu.stanford.smi.protege.model.Instance;
import edu.stanford.smi.protege.model.KnowledgeBase;
import edu.stanford.smi.protege.model.Model;
import edu.stanford.smi.protege.model.ModelUtilities;
import edu.stanford.smi.protege.model.Project;
import edu.stanford.smi.protege.model.Slot;
import edu.stanford.smi.protege.resource.Icons;
import edu.stanford.smi.protege.ui.DisplayUtilities;
import edu.stanford.smi.protege.ui.InstanceDisplay;
import edu.stanford.smi.protege.util.CollectionUtilities;
import edu.stanford.smi.protege.util.ComponentFactory;
import edu.stanford.smi.protege.util.ComponentUtilities;
import edu.stanford.smi.protege.util.FakeToolBar;
import edu.stanford.smi.protege.util.Log;
import edu.stanford.smi.protege.util.ModalDialog;
import edu.stanford.smi.protege.util.SelectionEvent;
import edu.stanford.smi.protege.util.SelectionListener;
import edu.stanford.smi.protege.util.StandardDateFormat;
import edu.stanford.smi.protege.util.SystemUtilities;
import edu.stanford.smi.protege.widget.AbstractTableWidget;
import edu.stanford.smi.protege.widget.ClsWidget;
import edu.stanford.smi.protege.widget.FormWidget;
import edu.stanford.smi.protege.widget.SlotWidget;
import edu.stanford.smi.protege.widget.TemplateSlotsWidget;

/**
 * @author Congo Patrice
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PolicyInstanceDisplay extends JDesktopPane{//InstanceDisplay {
	public static String TAB_TITLE_SLOT_POLICY= "Slot Policy";
	public static String TAB_TITLE_CLS_POLICY="Cls POLICY";
	private Project _project;
    private JScrollPane _scrollPane;
    private ClsWidget _currentWidget;
    private Instance _currentInstance;
    private JLabel _headerLabel;
    private JComponent _child;
    private Point _lastYellowStickyPosition = new Point();
    private Slot _templateSlotsSlot;
    private SlotPolicyDisplayPanel slotPolicyDisplayPanel;
    private PolicyFrameworkModel policyFrameworkModel;
    ///PolicyDisplayPanel policyDisplayPanel;//= new PolicyDisplayPanel();
    PolicyClassTreeView policyClsTreeView;
    JTabbedPane policyTabbedPane;
    
    private TableModelListener slotPolicyTableModelListener = new TableModelListener(){

		public void tableChanged(TableModelEvent e) {
			try{
				((Component)_currentWidget).repaint();
			}catch(Throwable th){
				th.printStackTrace();
			}
			
		}
    	
    };
    
    private ClsListener _clsListener = new ClsAdapter() {
        public void directSuperclassAdded(ClsEvent event) {
            reloadForm();
        }

        public void directSuperclassRemoved(ClsEvent event) {
            reloadForm();
        }

        public void templateSlotAdded(ClsEvent event) {
            reloadForm();
        }

        public void templateSlotRemoved(ClsEvent event) {
            reloadForm();
        }

        public void templateFacetValueChanged(ClsEvent event) {
            reloadForm();
        }
    };
    private FrameListener _frameListener = new FrameAdapter() {
        public void ownSlotValueChanged(FrameEvent event) {
            super.ownSlotValueChanged(event);
            if (event.getSlot().hasSuperslot(_templateSlotsSlot)) {
                reloadForm();
            }
        }
    };

    private WidgetListener _widgetListener = new WidgetAdapter() {
        public void labelChanged(WidgetEvent event) {
            if (_headerLabel != null) {
                loadHeader();
            }
        }
    };

    private ProjectListener _projectListener = new ProjectAdapter() {
        public void formChanged(ProjectEvent event) {
            Cls cls = event.getCls();
            if (_currentWidget != null && InstanceDisplay.equals(_currentWidget.getCls(), cls)) {
                reloadForm();
            }
        }
    };

    private MouseListener mouseListener= new MouseListener(){

		public void mouseClicked(MouseEvent arg0) {
			System.out.println("mouse clicked .....................");
			policyTabbedPane.setSelectedIndex(policyTabbedPane.indexOfTab(TAB_TITLE_SLOT_POLICY));
			return;
		}

		public void mouseEntered(MouseEvent arg0) {
			//System.out.println("mouse entered .....................");
			return;
		}

		public void mouseExited(MouseEvent arg0) {
			//System.out.println("mouse exited .....................");
			return;
		}

		public void mousePressed(MouseEvent arg0) {
			//System.out.println("mouse Pressed .....................");
			return;
		}

		public void mouseReleased(MouseEvent arg0) {
			//System.out.println("mouse released .....................");
			return;
		}
    	
    };
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
					slotPolicyDisplayPanel.setSlot(fsc.getSlot());
					//set slot polica Table visible
					policyTabbedPane.setSelectedIndex(policyTabbedPane.indexOfTab(TAB_TITLE_SLOT_POLICY));
				}
			}
		}
		
	};
    public static boolean equals(Object o1, Object o2) {
        return SystemUtilities.equals(o1, o2);
    }

    private InstanceListener _instanceListener = new InstanceListener() {
        public void directTypeAdded(InstanceEvent event) {
            setInstance(_currentWidget.getInstance(), _currentWidget.getAssociatedCls());
        }
        public void directTypeRemoved(InstanceEvent event) {
            setInstance(_currentWidget.getInstance(), _currentWidget.getAssociatedCls());
        }
    };
	

    public PolicyInstanceDisplay(Project project, PolicyFrameworkModel policyFrameworkModel) {
        this(project, true, true,policyFrameworkModel);
        
        ///policyDisplayPanel= new PolicyDisplayPanel(project.getKnowledgeBase());
        //policyClsTreeView= new PolicyClassTreeView(project);
    }

    public PolicyInstanceDisplay(Project project, boolean showHeader, 
    							boolean showHeaderLabel,
								PolicyFrameworkModel policyFrameworkModel) {
    	/////////System.out.println("POJECT:::::::::::"+project.getName());
    	//_project = project;
    	_project= policyFrameworkModel.getKnowledgeBase().getProject();
    	//////System.out.println("POJECT:::::::::::"+_project.getName());
    	this.policyFrameworkModel= policyFrameworkModel;
    	policyClsTreeView= new PolicyClassTreeView(project);
    	createSlotPolicyDisplay();
        _child = new JPanel(new BorderLayout());
        if (showHeader) {
            _child.add(createHeader(), BorderLayout.NORTH);
            if (!showHeaderLabel) {
                _headerLabel.setVisible(false);
            }
        }
        //_project = project;
        _templateSlotsSlot = project.getKnowledgeBase().getSlot(Model.Slot.DIRECT_TEMPLATE_SLOTS);
        //_templateSlotsSlot = project.getKnowledgeBase().getSlot(PolicyFrameworkModel.DEFAULT_POLICY_TAGGED_META_SLOT_NAME);
        ///System.out.println("+++++++++++++++++++++++++++++++++TemplateSlot:"+_templateSlotsSlot);
        project.addProjectListener(_projectListener);
        _scrollPane = makeInstanceScrollPane();
        _child.add(_scrollPane, BorderLayout.CENTER);
        add(_child);
    }

    protected JScrollPane makeInstanceScrollPane() {
        return new JScrollPane();
    }
    private void connectPolicyDisplayAndTableWidget(){
    	if(_currentWidget instanceof FormWidget){
    		FormWidget formWidget=(FormWidget)_currentWidget;
    		SlotWidget slotWidget=formWidget.getSlotWidget(_templateSlotsSlot);
    		Rectangle rect=((Component)slotWidget).getBounds();
    		
    		formWidget.replaceWidget(_templateSlotsSlot, 
    								PolicyTemplateSlotsWidget.class.getName());
    		//_templateSlotsSlot=formWidget.getWid
    		//TODO formWidget.replace
			this.slotPolicyDisplayPanel.setPolicyFrameworkModel(this.policyFrameworkModel);
    		
    		slotWidget=formWidget.getSlotWidget(_templateSlotsSlot);
    		
    		//formWidget.resizeWidget((Component)slotWidget, rect);//resizeWidget(Component widget, Point p)
    		((Component)slotWidget).setBounds(rect);
    		((Component)slotWidget).validate(); //Rectangle r = widget.getBounds();
    		((Component)slotWidget).repaint();
    		
    		if(slotWidget instanceof AbstractTableWidget){
    			AbstractTableWidget tableWidget= (AbstractTableWidget)slotWidget;
    			
    			//tableWidget.addSelectionListener(slotPolicyDisplayPanel.getSelectionListener());
    			tableWidget.addSelectionListener(selectionListener);
    			tableWidget.getTable().addMouseListener(mouseListener);
    		}
    	}
    }
    protected void addRuntimeWidget(Instance instance, Cls associatedCls) {
        Cls type = instance.getDirectType();
        _project.getDirectIncludedProjects();
        if (type == null) {
            Log.warning("instance has no type", this, "Instance", instance);
        } else {
            type.addClsListener(_clsListener);
            type.addFrameListener(_frameListener);
            /////System.out.println("instance:"+instance+ "\tassociatedCls:"+associatedCls+ " pjt:"+_project);
            _currentWidget = _project.createRuntimeClsWidget(instance, associatedCls);
            _currentWidget.addWidgetListener(_widgetListener);
            ////TODO add addSelectableHier
            //_currentWidget.addSelectionListener(policyDisplayPanel);
            _currentWidget.addSelectionListener(policyClsTreeView.getSelectionListener());
            
//            System.out.println("_currentWidgetClassType:"+
//            		_currentWidget.getClass().getName());
            connectPolicyDisplayAndTableWidget();
            //_currentWidget.addWidgetListener(policyDisplayPanel);
            JComponent component = createWidgetContainer(_currentWidget);
            ///********************
            JSplitPane rightPane= new JSplitPane(JSplitPane.VERTICAL_SPLIT);
            rightPane.setTopComponent(component);
            ///rightPane.setBottomComponent(policyDisplayPanel);//createPolicyslotPanel());
            ////CLS AND SLOT POLICY
            JTabbedPane tabbedPane= new JTabbedPane();
            tabbedPane.addTab("Cls Policy", policyClsTreeView);
            tabbedPane.addTab("Slot Policy", slotPolicyDisplayPanel);
            //tabbedPane.addTab("Slot Policy", createClsDisplay());            
            
            rightPane.setBottomComponent(tabbedPane);
            _scrollPane.setViewportView(rightPane);
            
            ////////////////////////////////////
            //rightPane.setBottomComponent(policyClsTreeView);
            
            //_scrollPane.setViewportView(rightPane);
            //_scrollPane.setViewportView(component);
            policyTabbedPane=tabbedPane;
            update();
        }
    }

    protected SlotPolicyDisplayPanel createSlotPolicyDisplay(){
    	//SlotPolicyDisplayPanel 
    	slotPolicyDisplayPanel= new SlotPolicyDisplayPanel(this._project,policyFrameworkModel);
    	slotPolicyDisplayPanel.addSlotPolicyTableModelListener(this.slotPolicyTableModelListener);
    	return slotPolicyDisplayPanel;
    }
    
    protected JComponent createWidgetContainer(ClsWidget widget) {
        return (JComponent) widget;
    }

    private Action createCreateYellowStickiesAction() {
        return new AbstractAction("Create Note", Icons.getCreateYellowStickyIcon()) {
            public void actionPerformed(ActionEvent event) {
                createYellowSticky();
            }
        };
    }

    private Action createDeleteYellowStickiesAction() {
        return new AbstractAction("Delete Note", Icons.getDeleteYellowStickyIcon()) {
            public void actionPerformed(ActionEvent event) {
                deleteYellowSticky();
            }
        };
    }

    private JComponent createHeader() {
        JComponent header = new JPanel();
        header.setLayout(new BorderLayout());
        _headerLabel = ComponentFactory.createLabel();
        header.add(_headerLabel, BorderLayout.CENTER);
        FakeToolBar toolBar = ComponentFactory.createFakeToolBar(ComponentFactory.SMALL_BUTTON_SIZE);
        JButton button = ComponentFactory.addToolBarButton(toolBar, createCreateYellowStickiesAction());
        button.setBackground(new Color(255, 255, 204));
        button = ComponentFactory.addToolBarButton(toolBar, createDeleteYellowStickiesAction());
        button.setBackground(new Color(255, 255, 204));
        header.add(toolBar, BorderLayout.EAST);
        return header;
    }

    private void createYellowSticky() {
        ensureYellowStickiesAreVisible();
        KnowledgeBase kb = _project.getKnowledgeBase();
        Instance instance = kb.createInstance(null, kb.getCls(Model.Cls.INSTANCE_ANNOTATION));
        ModelUtilities.setOwnSlotValue(instance, Model.Slot.CREATOR, _project.getUserName());
        DateFormat formatter = new StandardDateFormat();
        String date = formatter.format(new Date());
        ModelUtilities.setOwnSlotValue(instance, Model.Slot.CREATION_TIMESTAMP, date);
        ModelUtilities.setOwnSlotValue(instance, Model.Slot.ANNOTATED_INSTANCE, _currentInstance);
        showYellowSticky(instance);
    }

    private void deleteYellowSticky() {
        Collection stickyInstances = getStickyInstances();
        int count = stickyInstances.size();
        if (count == 1) {
            String text = "Are you sure that you want to delete this note";
            int result = ModalDialog.showMessageDialog(this, text, ModalDialog.MODE_YES_NO);
            if (result == ModalDialog.OPTION_YES) {
                Instance instance = (Instance) CollectionUtilities.getFirstItem(stickyInstances);
                removeSticky(instance);
                instance.delete();
            }
        } else if (count > 1) {
            Collection c =
                DisplayUtilities.pickInstancesFromCollection(this, stickyInstances, "Select a note to delete");
            Iterator i = c.iterator();
            while (i.hasNext()) {
                Instance instance = (Instance) i.next();
                removeSticky(instance);
                instance.delete();
            }
        }
    }

    public void dispose() {
        _project.removeProjectListener(_projectListener);
        if (_currentInstance != null) {
            _currentInstance.removeInstanceListener(_instanceListener);
        }
        if (_currentWidget != null) {
            _currentWidget.removeWidgetListener(_widgetListener);
            _currentWidget.getCls().removeClsListener(_clsListener);
            _currentWidget.getCls().removeFrameListener(_frameListener);
        }
    }

    private void ensureYellowStickiesAreVisible() {
    }

    public ClsWidget getCurrentClsWidget() {
        return _currentWidget;
    }

    public Instance getCurrentInstance() {
        return _currentInstance;
    }

    private Point getNextYellowStickyPosition() {
        int OFFSET = 25;
        int MAX_OFFSET = 100;

        _lastYellowStickyPosition.x += OFFSET;
        _lastYellowStickyPosition.x %= MAX_OFFSET;

        _lastYellowStickyPosition.y += OFFSET;
        _lastYellowStickyPosition.y %= MAX_OFFSET;

        return _lastYellowStickyPosition;
    }

    public Dimension getPreferredSize() {
        return _child.getPreferredSize();
    }

    private Collection getStickyInstances() {
        Collection stickyInstances = new ArrayList();
        if (_currentInstance != null) {
            KnowledgeBase kb = _project.getKnowledgeBase();
            /*
            Slot annotationSlot = kb.getSlot(Model.Slot.ANNOTATED_INSTANCE);
            Collection refs = kb.getReferences(_currentInstance, 0);
            Iterator i = refs.iterator();
            Log.trace("references=" + refs.size(), this, "getStickyInstances");
            while (i.hasNext()) {
                Reference ref = (Reference) i.next();
                if (ref.getSlot() == annotationSlot) {
                    stickyInstances.add(ref.getFrame());
                }
            }
            */
            Slot annotationSlot = kb.getSlot(Model.Slot.ANNOTATED_INSTANCE);
            Iterator i = kb.getCls(Model.Cls.INSTANCE_ANNOTATION).getInstances().iterator();
            while (i.hasNext()) {
                Instance annotationInstance = (Instance) i.next();
                Instance pointedAtInstance = (Instance) annotationInstance.getOwnSlotValue(annotationSlot);
                if (equals(pointedAtInstance, _currentInstance)) {
                    stickyInstances.add(annotationInstance);
                }
            }
        }
        return stickyInstances;
    }

    private Map getYellowStickyMap() {
        String mapName = "InstanceDisplay.yellow_stickies";
        Map map = (Map) _project.getClientInformation(mapName);
        if (map == null) {
            map = new HashMap();
            _project.setClientInformation(mapName, map);
        }
        return map;
    }

    private void loadHeader() {
        Icon icon = null;
        String text = "";
        if (_currentWidget != null) {
            Instance instance = _currentWidget.getInstance();
            text = _currentWidget.getLabel();
            icon = instance.getIcon();
        }
        _headerLabel.setIcon(icon);
        _headerLabel.setText(text);
        policyClsTreeView.resetView();
    }

    private JInternalFrame loadIntoFrame(final Instance instance) {
        JInternalFrame frame = _project.showInInternalFrame(instance);
        Map propertyMap = getYellowStickyMap();
        Rectangle r = (Rectangle) propertyMap.get(instance);
        if (r == null) {
            frame.setLocation(getNextYellowStickyPosition());
            propertyMap.put(instance, frame.getBounds());
        } else {
            frame.setBounds(r);
        }
        frame.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                getYellowStickyMap().put(instance, event.getComponent().getBounds());
            }
            public void componentMoved(ComponentEvent event) {
                getYellowStickyMap().put(instance, event.getComponent().getBounds());
            }
        });
        /*
        frame.addInternalFrameListener(new InternalFrameAdapter() {
            public void internalFrameClosed(InternalFrameEvent event) {
                ComponentUtilities.dispose((Component) event.getSource());
                // Log.enter(this, "internalFrameClosed", event);
                checkForValidAnnotation(instance);
            }
        });
        */
        return frame;
    }

    private void reloadForm() {
        Instance instance = _currentWidget.getInstance();
        Cls cls = _currentWidget.getAssociatedCls();
        removeCurrentWidget();
        setInstance(instance, cls);
        ///policyClsTreeView.resetPolicyTableModel();
    }

    private void removeAllStickies() {
        Iterator i = new ArrayList(Arrays.asList(getComponents())).iterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            if (c instanceof JInternalFrame) {
                JInternalFrame frame = (JInternalFrame) c;
                frame.setVisible(false);
                frame.dispose();
                remove(frame);
            }
        }
    }

    protected void removeCurrentWidget() {
        _currentWidget.getCls().removeClsListener(_clsListener);
        _currentWidget.getCls().removeFrameListener(_frameListener);
        _currentWidget.removeWidgetListener(_widgetListener);
        Component c = (Component) _currentWidget;
        _scrollPane.setViewportView(null);
        ComponentUtilities.dispose(c);
        _currentWidget = null;
        _currentInstance = null;
        update();
    }

    private void removeSticky(Instance instance) {
        Iterator i = new ArrayList(Arrays.asList(getComponents())).iterator();
        while (i.hasNext()) {
            Component c = (Component) i.next();
            if (c instanceof JInternalFrame) {
                JInternalFrame frame = (JInternalFrame) c;
                InstanceDisplay display = (InstanceDisplay) frame.getContentPane().getComponent(0);
                if (equals(display.getCurrentInstance(), instance)) {
                    frame.setVisible(false);
                    frame.dispose();
                    remove(frame);
                    break;
                }
            }
        }
    }

    /**
     * @deprecated
     */
    public void reshape(int x, int y, int w, int h) {
        super.reshape(x, y, w, h);
        _child.setBounds(0, 0, w, h);
    }

    public void setInstance(Instance instance) {
        setInstance(instance, null);
    }

    public void setInstance(Instance instance, Cls associatedCls) {
        ///policyDisplayPanel.setRootCls((Cls)instance);
    	//TODO SELECT THE CLS POLICY DISPLAY
    	////////////////////////////////////policyClsTreeView.setRootCls((Cls)instance);
    	//slotPolicyDisplayPanel.setInstance(instance);
    	// if (instance != _currentInstance) {
        if (_currentInstance != null) {
            _currentInstance.removeInstanceListener(_instanceListener);
            //_currentInstance.removeInstanceListener(policyClsTreeView);
        }
        if (instance == null) {
            if (_currentWidget != null) {
                removeCurrentWidget();
            }
        } else {
        	policyClsTreeView.setRootCls((Cls)instance);
            if (_currentWidget == null) {
                addRuntimeWidget(instance, associatedCls);
            } else {
                if (equals(_currentWidget.getCls(), instance.getDirectType())) {
                	 //System.out.println("instance1:"+instance+ " _currentWidget+"+_currentWidget);
                	_currentWidget.setInstance(instance);
                    _currentWidget.setAssociatedCls(associatedCls);
                } else {
                    removeCurrentWidget();
                    //System.out.println("instance1:"+instance+"\tassociatedCls:"+associatedCls);
                    addRuntimeWidget(instance, associatedCls);
                }
            }
            instance.addInstanceListener(_instanceListener);
        }
        _currentInstance = instance;
        if (_headerLabel != null) {
            loadHeader();
        }
        updateStickies();
        // }
    }

    private void showAllStickies() {
        if (_currentInstance != null) {
            Iterator i = getStickyInstances().iterator();
            while (i.hasNext()) {
                Instance instance = (Instance) i.next();
                showYellowSticky(instance);
            }
        }
    }

    private void showYellowSticky(Instance instance) {
        JInternalFrame frame = loadIntoFrame(instance);
        String author = (String) ModelUtilities.getDirectOwnSlotValue(instance, Model.Slot.CREATOR);
        if (author == null || author.length() == 0)
            author = "<unknown author>";
        String timeString = getTimeString(instance);
        String title = author;
        if (timeString != null) {
            title += ", " + timeString;
        }
        frame.setTitle(title);
        frame.setVisible(true);
        add(frame);
        frame.toFront();
        try {
            frame.setSelected(true);
        } catch (Exception e) {
        }
    }

    private String getTimeString(Instance instance) {
        String timeString = null;
        String timestamp = (String) ModelUtilities.getDirectOwnSlotValue(instance, Model.Slot.CREATION_TIMESTAMP);
        if (timestamp != null) {
            SimpleDateFormat formatter = new StandardDateFormat();
            try {
                Date date = formatter.parse(timestamp);
                Calendar calendar = new GregorianCalendar();
                int thisYear = calendar.get(Calendar.YEAR);
                calendar.setTime(date);
                int stickyYear = calendar.get(Calendar.YEAR);
                String pattern = "MMM dd " + ((thisYear == stickyYear) ? "" : "yyyy ") + "HH:mm";
                formatter.applyPattern(pattern);
                timeString = formatter.format(date);
            } catch (ParseException e) {
                Log.exception(e, this, "showYellowSticky", instance);
                timeString = timestamp;
            }
        }
        return timeString;
    }

    private void update() {
        revalidate();
        repaint();
    }

    private void updateStickies() {
        removeAllStickies();
        showAllStickies();
    }
}
