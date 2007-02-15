package org.peertrust.pteditor.editors;

import java.util.List;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.IPersistableElement;
import org.peertrust.pteditor.model.MyPolicy;
import org.peertrust.pteditor.model.MyResource;

public class FileEditorInput implements IEditorInput {
	private MyResource myres=null;
	private MyPolicy policyEdited=null;
	private MyPolicy oldPolicy=null;
	
	public FileEditorInput(MyResource res) {
		this(res,null);
	}

	public FileEditorInput(MyResource res,MyPolicy policy) {
		super();
		myres=res;
		policyEdited=policy;
		if(policyEdited==null)
			policyEdited=new MyPolicy(res);
		else {
			oldPolicy=policyEdited;
			policyEdited=policyEdited.getCopy();
		}
	}

	public boolean exists() {
		return false;
	}

	public ImageDescriptor getImageDescriptor() {
		return null;
	}

	public String getName() {
		return myres.toString();
	}

	public void setName(String name) {
		myres.setName(name);
	}

	public IPersistableElement getPersistable() {
		return null;
	}

	public String getToolTipText() {
		return getName();
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	public boolean equals(Object obj) {
		if(super.equals(obj))
			return true;
		if(!(obj instanceof FileEditorInput))
			return false;
		FileEditorInput other=(FileEditorInput)obj;
		return myres.equals(other.myres);
//		return true;
	}
	
	public boolean isDirectory() {
		return myres.getType()==MyResource.TYPE_DIRECTORY;
	}
	
	public String getFilter() {
		return policyEdited.getFilter();
	}
	
	public void setFilter(String filter) {
		policyEdited.setFilter(filter);
	}

	public List<MyResource> getExceptionalResources() {
		return policyEdited.getExcResources();
	}

	public void setExceptionalResources(MyResource items[]) {
		policyEdited.setExcResources(items);
	}
	
	public List<MyPolicy> getPolicies() {
		return myres.getPolicies();
	}

	public void savePolicies() {
		if(oldPolicy!=null)
			myres.replacePolicy(oldPolicy,policyEdited);
		else
			myres.addPolicy(policyEdited);
	}
	
	public MyPolicy getPolicyEdited() {
		return policyEdited;
	}

	public void setPolicyEdited(MyPolicy policyEdited) {
		this.policyEdited=policyEdited;
	}
	
	public String getPolicyName() {
		return policyEdited.getPolicy();
	}
	
	public void setPolicyName(String policy) {
		policyEdited.setPolicy(policy);
	}
	
	public String getDirectoryName() {
		return myres.getName();
	}

	public boolean getPolicyDefault() {
		return policyEdited.getDefault();
	}

	public void setPolicyDefault(boolean default1) {
		policyEdited.setDefault(default1);
	}
	
	public List<MyPolicy> getInheritedPolicies() {
		return myres.getInheritedPolicies();
	}
	
	public List<MyPolicy> getOverridePolicies() {
		return policyEdited.getOverridePolicies();
	}
	
	public void setOverridePolicies(MyPolicy[] policies) {
		policyEdited.setOverridePolicies(policies);
	}
	
	public String getFullPath() {
		return myres.getFullPath();
	}
	
	public MyResource getResource() {
		return myres;
	}
}
