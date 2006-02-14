package policysystem;

import org.eclipse.swt.SWT;
import org.eclipse.swt.internal.Platform;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.application.IWorkbenchWindowConfigurer;
import org.eclipse.ui.application.WorkbenchAdvisor;
import org.eclipse.ui.application.WorkbenchWindowAdvisor;

import policysystem.model.PolicySystemRDFModel;

public class ApplicationWorkbenchAdvisor extends WorkbenchAdvisor {
	public static String rdfFileName;
	
	private static final String PERSPECTIVE_ID = "policysystem.perspective";
	
    public WorkbenchWindowAdvisor createWorkbenchWindowAdvisor(IWorkbenchWindowConfigurer configurer) 
    {
        return new ApplicationWorkbenchWindowAdvisor(configurer);
    }

	public String getInitialWindowPerspectiveId() {
		return PERSPECTIVE_ID;
	}

	public void preStartup() {
		super.preStartup();
		PolicySystemRDFModel.getInstance();
//		rdfFileName=
//			askForRDFFileName(
//					PlatformUI.getWorkbench().getDisplay().getActiveShell());
	}
	
	static public String askForRDFFileName(Shell parent){
		//Shell parent=
		if(parent==null){
			parent=new Shell();//Display.getDefault().getActiveShell();
		}
		FileDialog fileDlg= new FileDialog(parent);
		fileDlg.setText("choose rdf file");
		fileDlg.setFilterNames(new String[]{"*.rdf"});
		String fileName=fileDlg.open();
		//String fileName=new File(fileDlg.getParent(),fileFileName();
		System.out.println("dir="+fileName);
		return fileName;
	}
	
}
