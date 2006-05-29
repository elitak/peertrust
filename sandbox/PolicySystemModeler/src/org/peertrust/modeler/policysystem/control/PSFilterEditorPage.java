package org.peertrust.modeler.policysystem.control;

import java.util.Iterator;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;

import org.eclipse.ui.part.Page;
import org.peertrust.modeler.policysystem.model.abtract.PSModelObject;
import org.peertrust.modeler.policysystem.model.abtract.PSFilter;
import org.peertrust.modeler.policysystem.model.abtract.PSPolicy;

/**
 * A page for editing a PSFilter
 * @author pat_dev
 *
 */
public class PSFilterEditorPage extends Page 
{
	
	/**
	 * The top composite thats hold the list editor
	 */
	private Composite top;
	
	
//	/**
//	 * Logger for the PSFilterEditorPage class
//	 */
//	private static final Logger logger= 
//		Logger.getLogger(PSPolicyEditorPage.class);
	
	private PSFilterEditorControl filterEditorControl;
	
	/**
	 * @see org.eclipse.ui.part.IPage#createControl(org.eclipse.swt.widgets.Composite)
	 */
	public void createControl(Composite parent) 
	{
		top = new Composite(parent, SWT.LEFT);
		top.setLayout(new GridLayout());
		filterEditorControl= new PSFilterEditorControl(true);
		filterEditorControl.createControl(top);
		
	}

	/**
	 * @see org.eclipse.ui.part.IPage#getControl()
	 */
	public Control getControl() 
	{
		return top;
	}

	/**
	 * @see org.eclipse.ui.part.IPage#setFocus()
	 */
	public void setFocus() 
	{
		
	}

	/**
	 * @return returns the filter which is neing edited
	 */
	public PSFilter getPSFilter() 
	{
		//return psFilter;
		return (PSFilter)filterEditorControl.getModelObject();
	}

	/**
	 * To set the filter to edit
	 * @param psFilter -- the filter to edit
	 */
	public void setPSFilter(PSFilter psFilter) 
	{
		filterEditorControl.setModelObject(psFilter);
	}
}
