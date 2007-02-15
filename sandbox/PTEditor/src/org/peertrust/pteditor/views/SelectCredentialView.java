package org.peertrust.pteditor.views;

//TODO: Abstürze
//TODO: Filter & Regular expressions
//TODO: Algorithm & Examples
//TODO: Abspeichern & Laden
//TODO: Override & Exception
//TODO: Check & Console

import java.util.List;

import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.dnd.DND;
import org.eclipse.swt.dnd.DragSourceAdapter;
import org.eclipse.swt.dnd.DragSourceEvent;
import org.eclipse.swt.dnd.DropTargetAdapter;
import org.eclipse.swt.dnd.DropTargetEvent;
import org.eclipse.swt.dnd.Transfer;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.TreeItem;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.part.ViewPart;
import org.peertrust.pteditor.MyImageRegistry;
import org.peertrust.pteditor.actions.AddPolicyAction;
import org.peertrust.pteditor.actions.LoadCredentialsAction;
import org.peertrust.pteditor.actions.NewSiblingClassAction;
import org.peertrust.pteditor.actions.NewSubclassAction;
import org.peertrust.pteditor.model.IMyResourceChangedListener;
import org.peertrust.pteditor.model.MyPolicy;
import org.peertrust.pteditor.model.MyResource;

public class SelectCredentialView extends ViewPart {
	public static final String ID="PTEditor.view.selectCredential";

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

	public class MyResourceTreeLabelProvider extends LabelProvider {
		public String getText(Object element) {
			return ((MyResource)element).toString();
		}
		
		public Image getImage(Object element) {
			int nType=((MyResource)element).getType();
			if(nType==MyResource.TYPE_ROOT)
				return MyImageRegistry.getImageRegistry().get("root");
			else if(nType==MyResource.TYPE_CLASS)
				return MyImageRegistry.getImageRegistry().get("class");
			else if(nType==MyResource.TYPE_CREDENTIAL)
				return MyImageRegistry.getImageRegistry().get("credential");
			return null;
		}
	}

	private IMyResourceChangedListener listenerMyResource=new IMyResourceChangedListener() {
		public void notifyResourceChanged(MyResource res) {
			if(!bTurnOffRefreshList)
				treeviewerCredentials.refresh();
		}
	};
	
	private TreeViewer treeviewerCredentials;
	private MyResource root=new MyResource("root",null,MyResource.TYPE_ROOT);
	private boolean bTurnOffRefreshList=false;

	public SelectCredentialView() {
		super();
	}

	public void createPartControl(Composite parent) {
		MyResource resourceRoot=new MyResource("Resources",root,MyResource.TYPE_ROOT);
MyResource creditcard=new MyResource("Credit card",resourceRoot);
creditcard.addPolicy(new MyPolicy("show only if partner is trusted",creditcard));
creditcard.addPolicy(new MyPolicy("is partner allowed to see card?",creditcard));
new MyResource("VISA",creditcard);
creditcard.getChildren().get(0).addPolicy(new MyPolicy("is partner VISA-trusted?",creditcard.getChildren().get(0)));
creditcard.getChildren().get(0).addPolicy(new MyPolicy("is VISA-ID correct?",creditcard.getChildren().get(0)));
new MyResource("Master Card",creditcard);
creditcard.getChildren().get(1).addPolicy(new MyPolicy("is partner Master Card-trusted?",creditcard.getChildren().get(1)));
creditcard.getChildren().get(1).addPolicy(new MyPolicy("Is Master Card-ID correct?",creditcard.getChildren().get(1)));
		treeviewerCredentials=new TreeViewer(parent,SWT.BORDER|SWT.V_SCROLL|SWT.SINGLE);
		getSite().setSelectionProvider(treeviewerCredentials);
		treeviewerCredentials.setContentProvider(new MyResourceContentTreeProvider());
		treeviewerCredentials.setLabelProvider(new MyResourceTreeLabelProvider());
treeviewerCredentials.setInput(root);

		treeviewerCredentials.addDragSupport(DND.DROP_MOVE,
			new Transfer[]{MyResourceTransfer.getInstance()},
			new DragSourceAdapter() {
				public void dragStart(DragSourceEvent event) {
					ISelection sel=treeviewerCredentials.getSelection();
					if((sel==null)||(!(sel instanceof IStructuredSelection))) {
						event.doit=false;
						return;
					}
					IStructuredSelection ssel=(IStructuredSelection)sel;
					if((ssel.getFirstElement()==null)||
						(!(ssel.getFirstElement() instanceof MyResource))) {
						event.doit=false;
						return;
					}
					MyResource res=(MyResource)ssel.getFirstElement();
					if(res.getType()==MyResource.TYPE_ROOT) {
						event.doit=false;
						return;
					}
					event.doit=true;
				}

				public void dragSetData(DragSourceEvent event) {
					if(MyResourceTransfer.getInstance().isSupportedType(event.dataType)) {
						ISelection sel=treeviewerCredentials.getSelection();
						if((sel==null)||(!(sel instanceof IStructuredSelection)))
							return;
						IStructuredSelection ssel=(IStructuredSelection)sel;
						if((ssel.getFirstElement()==null)||
							(!(ssel.getFirstElement() instanceof MyResource)))
							return;
						MyResource res=(MyResource)ssel.getFirstElement();
						if(res.getType()==MyResource.TYPE_ROOT)
							return;
						event.data=res;
						return;
					}
					event.doit=false;
				}
				
				public void dragFinished(DragSourceEvent event) {
					if(event.detail==DND.DROP_NONE) {
						return;
					}
					ISelection sel=treeviewerCredentials.getSelection();
					if((sel==null)||(!(sel instanceof IStructuredSelection)))
						return;
					IStructuredSelection ssel=(IStructuredSelection)sel;
					if((ssel.getFirstElement()==null)||
						(!(ssel.getFirstElement() instanceof MyResource)))
						return;
					MyResource myres=(MyResource)ssel.getFirstElement();
					MyResource parent=myres.getParent();
					parent.removeChild(myres);
				}
			}
		);

		treeviewerCredentials.addDropSupport(DND.DROP_MOVE,
			new Transfer[]{MyResourceTransfer.getInstance()},
			new DropTargetAdapter() {
				public void drop(DropTargetEvent event) {
					setTurnOffRefreshList(true);
					if(MyResourceTransfer.getInstance().isSupportedType(event.currentDataType)) {
						if((event.item==null)||(!(event.item instanceof TreeItem))) {
							setTurnOffRefreshList(false);
							return;
						}
						TreeItem item=(TreeItem)event.item;
						MyResource copied=(MyResource)MyResourceTransfer.getInstance().
							nativeToJava(event.currentDataType);
						MyResource newparent=(MyResource)item.getData();
						if((copied.getParent()!=null)&&(!copied.getParent().equals(newparent))) {
							if(!copied.getPathFromRootToResource().contains(newparent)) {
								List<MyResource> list=copied.getChildren();
								for(int i=0;i<list.size();i++) {
									list.get(i).setParent(copied.getParent());
									copied.removeChild(list.get(i));
									copied.getParent().addChild(list.get(i));
								}
							}
						}
						else {
							event.detail=DND.DROP_NONE;
							setTurnOffRefreshList(false);
							treeviewerCredentials.refresh();
						}
						newparent.addChild(copied);
					}
					setTurnOffRefreshList(false);
				}
			});
		
		MenuManager menumgr=new MenuManager("selectCredentialPopup");
		menumgr.add(new AddPolicyAction(getSite().getWorkbenchWindow()));
		menumgr.add(new NewSubclassAction(getSite().getWorkbenchWindow()));
		menumgr.add(new NewSiblingClassAction(getSite().getWorkbenchWindow()));
		menumgr.add(new LoadCredentialsAction(getSite().getWorkbenchWindow()));
		menumgr.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
		Menu menu=menumgr.createContextMenu(treeviewerCredentials.getControl());
		treeviewerCredentials.getControl().setMenu(menu);
		getSite().registerContextMenu(menumgr,treeviewerCredentials);
		
		root.addMyResourceListener(listenerMyResource);
	}

	public void setFocus() {
		treeviewerCredentials.getControl().setFocus();
	}
	
	public void dispose() {
		root.removeMyResourceListener(listenerMyResource);
	}
	
	private void setTurnOffRefreshList(boolean value) {
		bTurnOffRefreshList=value;
	}
}
