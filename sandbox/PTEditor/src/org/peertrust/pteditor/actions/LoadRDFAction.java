package org.peertrust.pteditor.actions;

import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.ui.ISelectionListener;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.actions.ActionFactory.IWorkbenchAction;
import org.peertrust.pteditor.model.MyResource;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;

public class LoadRDFAction extends Action implements IWorkbenchAction,
	ISelectionListener {
	private final IWorkbenchWindow window;
	public static final String ID="org.peertrust.pteditor.saveasrdffile";
	private IStructuredSelection selectionResource=null;

	public LoadRDFAction(IWorkbenchWindow window) {
		this.window=window;
		setId(ID);
		setText("&Iport from RDF File");
		setToolTipText("Import from RDF File");
		setImageDescriptor(PlatformUI.getWorkbench().getSharedImages().getImageDescriptor(ISharedImages.IMG_OBJS_INFO_TSK));
		setEnabled(true);
		window.getSelectionService().addSelectionListener(this);
		setEnabled(false);
	}

	public void selectionChanged(IWorkbenchPart part, ISelection selection) {
		if(selection instanceof IStructuredSelection) {
			IStructuredSelection sel=(IStructuredSelection)selection;
			if((sel.size()==1)&&(sel.getFirstElement() instanceof MyResource))
				selectionResource=sel;
			else
				selectionResource=null;
		}
		setEnabled(selectionResource!=null);
	}

	public void run() {
		Object item=selectionResource.getFirstElement();
		if((item==null)||(!(item instanceof MyResource)))
			return;
/*		MyResource myres=(MyResource)item;
		while(myres.getParent()!=null)
			myres=myres.getParent();
		myres=myres.getChildren().get(0);
		Model modelData=ModelFactory.createDefaultModel();
		Resource resRoot=modelData.createResource("http://sourceforge.net/projects/peertrust/#Root");
		List<MyResource> listChildren=myres.getChildren();
		Property propStartPoint=modelData.getProperty("http://sourceforge.net/projects/peertrust/#startingPoint");
		Property propPolicy=modelData.getProperty("http://sourceforge.net/projects/peertrust/#policy");
//		Property propFilter=modelData.getProperty("http://sourceforge.net/projects/peertrust/#filter");
//		Property propExcFilterFile=modelData.getProperty("http://sourceforge.net/projects/peertrust/#exception_filter_file");
		Property propSubItem=modelData.getProperty("http://sourceforge.net/projects/peertrust/#subitem");
		for(int i=0;i<listChildren.size();i++) {
			MyResource mres=listChildren.get(i);
			Resource res=modelData.createResource(mres.getName());
			resRoot.addProperty(propStartPoint,res);
			Set<Object[]> setResources=new LinkedHashSet<Object[]>();
			setResources.add(new Object[]{mres,res});
			while(!setResources.isEmpty()) {
				Iterator<Object[]> iter=setResources.iterator();
				Object obj[]=iter.next();
				mres=(MyResource)obj[0];
				res=(Resource)obj[1];
				setResources.remove(obj);
				for(int j=0;j<mres.getChildren().size();j++) {
					MyResource _myres=mres.getChildren().get(j);
					Resource _res=modelData.createResource(_myres.getName());
					res.addProperty(propSubItem,_res);
					setResources.add(new Object[]{_myres,_res});
				}
			}
		}
System.out.println(modelData);*/
		FileDialog dialog=new FileDialog(window.getShell());
		dialog.open();
	}
	
	public void dispose() {
		window.getSelectionService().removeSelectionListener(this);
	}
}
