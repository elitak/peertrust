package org.peertrust.pteditor.dialog;

import java.util.List;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerFilter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.peertrust.pteditor.model.MyResource;

public class MyResourceChooserDialog extends Dialog {

	class MyResourceContentTreeProvider implements ITreeContentProvider {
		public Object[] getChildren(Object parentElement) {
			List<MyResource> kids=((MyResource)parentElement).getChildren();
		    return kids == null ? new Object[0] : kids.toArray(new MyResource[0]);
	    }

		public Object getParent(Object element) {
			return ((MyResource)element).getParent();
		}

		public boolean hasChildren(Object element) {
			return (getChildren(element).length>0);
		}

		public Object[] getElements(Object inputElement) {
			return getChildren(inputElement);
		}

		public void dispose() {
		}

		public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		}
	}

	public class FileTreeLabelProvider extends LabelProvider {
		public String getText(Object element) {
			return ((MyResource)element).toString();
		}
		
		public Image getImage(Object element) {
			if (((MyResource) element).getType()==MyResource.TYPE_DIRECTORY)
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FOLDER);
			else
				return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_FILE);
		}
		
	}
	
	private ViewerFilter viewerfilter=new ViewerFilter() {
		public boolean select(Viewer viewer, Object parentElement, Object element) {
			if((strFilter==null)||(strFilter.length==0)||(!(element instanceof MyResource)))
				return true;
			MyResource res=(MyResource)element;
			if(!res.getChildren().isEmpty())
				return true;
			List<MyResource> list=res.getPathFromRootToResource();
			if(list.contains(myres)) {
				StringBuffer strbuf=new StringBuffer();
				for(int i=list.indexOf(myres)+1;i<list.size();i++)
					strbuf.append("/"+list.get(i).toString());
				for(int i=0;i<strFilter.length;i++)
					if((strFilter[i]!=null)&&(strFilter[i].length()>0)) {
						if(strbuf.toString().matches(strFilter[i]))
							return true;
					}
					else
						return true;
			}
			return false;
		}
	};

	private TreeViewer treeviewerRes=null;
	private MyResource myres=null;
	private MyResource resourceSelected=null;
	private String strFilter[]=null;
	
	public MyResourceChooserDialog(Shell parentShell,MyResource res,String filters[]) {
		super(parentShell);
		myres=res;
		strFilter=filters;
		if(strFilter!=null) {
			for(int i=0;i<strFilter.length;i++) {
				strFilter[i]=strFilter[i].trim();
				String strParts[]=strFilter[i].split("/");
				int j=0;
				while(j<strParts.length) {
					boolean bDoubleSlash=false;
					if((strParts[j].length()==0)&&(j>0)) {
						bDoubleSlash=true;
						if(j+1<strParts.length)
							j++;
					}
					int a=0;
					while(a<strParts[j].length()) {
						char c=strParts[j].charAt(a);
						if(c=='*') {
							String str=(bDoubleSlash) ? ".*" : "[^/]*";
							strParts[j]=strParts[j].substring(0,a)+str+
								strParts[j].substring(a+1);
							a+=(bDoubleSlash) ? 2 : 5;
						}
						else if(c=='.') {
							strParts[j]=strParts[j].substring(0,a)+"\\."+
							strParts[j].substring(a+1);
							a+=3;
						}
						else
							a++;
					}
					j++;
				}
				strFilter[i]="";
				for(j=0;j<strParts.length;j++) {
					strFilter[i]+=strParts[j];
					if(j<strParts.length-1)
						strFilter[i]+="/";
				}
				strFilter[i]=strFilter[i].replaceAll("//","/");
//				// /*.jpg
//				System.out.println("/as.jpg".matches("/[^/]*\\.jpg"));
//				// //*.jpg
//				System.out.println("/test/bye/as.jpg".matches("/.*\\.jpg"));
//				// /*/*.jpg
//				System.out.println("/test/as.jpg".matches("/[^/]*/[^/]*\\.jpg"));
			}
		}
	}
	
	protected Control createDialogArea(Composite parent) {
		Composite composite=new Composite(parent,SWT.NONE);
		composite.setLayout(new GridLayout());
		composite.setLayoutData(new GridData(GridData.FILL_BOTH));
		treeviewerRes=new TreeViewer(composite);
		treeviewerRes.setContentProvider(new MyResourceContentTreeProvider());
		treeviewerRes.setLabelProvider(new FileTreeLabelProvider());
		treeviewerRes.addFilter(viewerfilter);
		treeviewerRes.setInput(myres);
		treeviewerRes.getControl().setLayoutData(new GridData(GridData.FILL_BOTH));
		return composite;
	}

	protected void buttonPressed(int buttonId) {
		if(buttonId==IDialogConstants.OK_ID) {
			resourceSelected=null;
			if(!(treeviewerRes.getSelection() instanceof IStructuredSelection))
				return;
			IStructuredSelection sel=(IStructuredSelection)treeviewerRes.getSelection();
			if((sel.getFirstElement()==null)||(!(sel.getFirstElement() instanceof MyResource)))
				return;
			resourceSelected=(MyResource)sel.getFirstElement();
		}
		super.buttonPressed(buttonId);
	}

	public MyResource getValue() {
		return resourceSelected;
	}
}
