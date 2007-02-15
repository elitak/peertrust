package org.peertrust.pteditor.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class MyPolicy {
	private String strPolicy="";
	private String strFilter=null;
	private Set<Long> setExcRes=null;
	private boolean bDefault=true;
	private Set<Long> setOverridePolicies=null;
	private Long owner=null;
	private static HashMap<Long,MyPolicy> hashmapAllPolicies=new HashMap<Long,MyPolicy>();
	private static int nNextID=0;
	private Long nID;
	
	public MyPolicy(MyResource _owner) {
		this(null,_owner);
	}

	public MyPolicy(String policy,MyResource _owner) {
		strPolicy=policy;
		owner=(_owner!=null) ? _owner.getID() : new Long(-1);
		nID=new Long(nNextID++);
		hashmapAllPolicies.put(nID,this);
		fireMyResourceListener();
	}
	
	public MyPolicy getCopy() {
		MyPolicy policy=new MyPolicy(null);
		policy.owner=owner;
		policy.strPolicy=strPolicy;
		policy.strFilter=strFilter;
		policy.setExcRes=setExcRes;
		policy.bDefault=bDefault;
		policy.setOverridePolicies=setOverridePolicies;
		policy.nID=nID;
		return policy;
	}

	public void cloneFromPolicy(MyPolicy policy) {
		owner=policy.owner;
		strPolicy=policy.strPolicy;
		strFilter=policy.strFilter;
		setExcRes=policy.setExcRes;
		bDefault=policy.bDefault;
		setOverridePolicies=policy.setOverridePolicies;
		nID=policy.nID;
	}
	
	public Long getID() {
		return nID;
	}
	
	public String toString() {
		return strPolicy;
	}

	public List<MyResource> getExcResources() {
		List<MyResource> list=new LinkedList<MyResource>();
		if(setExcRes==null)
			return list;
		Iterator<Long> iter=setExcRes.iterator();
		while(iter.hasNext())
			list.add(MyResource.getAllResources().get(iter.next()));
		return list;
	}

	public void setExcResources(MyResource[] excRes) {
		if(setExcRes==null)
			setExcRes=new TreeSet<Long>();
		setExcRes.clear();
		for(int i=0;i<excRes.length;i++)
			setExcRes.add(excRes[i].getID());
		fireMyResourceListener();
	}
	
	public void addExcResource(MyResource res) {
		if(setExcRes==null)
			setExcRes=new TreeSet<Long>();
		setExcRes.add(res.getID());
		fireMyResourceListener();
	}

	public void removeExcResource(MyResource res) {
		if(setExcRes==null)
			setExcRes=new TreeSet<Long>();
		setExcRes.remove(res.getID());
		fireMyResourceListener();
	}

	public String getFilter() {
		return strFilter;
	}

	public void setFilter(String strFilter) {
		this.strFilter = strFilter;
		fireMyResourceListener();
	}

	public String getPolicy() {
		return strPolicy;
	}

	public void setPolicy(String strPolicy) {
		this.strPolicy = strPolicy;
		fireMyResourceListener();
	}
		
	public boolean equals(Object obj) {
		if(!(obj instanceof MyPolicy))
			return false;
		MyPolicy other=(MyPolicy)obj;
		if((strPolicy!=null)&&(other.strPolicy!=null)&&
			(!strPolicy.equals(other.strPolicy)))
			return false;
		if((strFilter!=null)&&(other.strFilter!=null)&&
			(!strFilter.equals(other.strFilter)))
			return false;
		if((setExcRes!=null)&&(other.setExcRes!=null)&&
			(!setExcRes.equals(other.setExcRes)))
			return false;
		return true;
	}

	public boolean getDefault() {
		return bDefault;
	}

	public void setDefault(boolean default1) {
		bDefault=default1;
		fireMyResourceListener();
	}
	
	public List<MyPolicy> getOverridePolicies() {
		List<MyPolicy> list=new LinkedList<MyPolicy>();
		if(setExcRes==null)
			return list;
		Iterator<Long> iter=setOverridePolicies.iterator();
		while(iter.hasNext())
			list.add(getAllPolicies().get(iter.next()));
		return list;
	}
	
	public void setOverridePolicies(MyPolicy[] policies) {
		if(setOverridePolicies==null)
			setOverridePolicies=new TreeSet<Long>();
		else
			setOverridePolicies.clear();
		for(int i=0;i<policies.length;i++)
			setOverridePolicies.add(policies[i].getID());
		fireMyResourceListener();
	}
	
	public MyResource getOwner() {
		return MyResource.getAllResources().get(owner);
	}

	protected static HashMap<Long,MyPolicy> getAllPolicies() {
		return hashmapAllPolicies;
	}
	
	private void fireMyResourceListener() {
		if(owner.longValue()>=0) {
			MyResource _res=MyResource.getAllResources().get(owner);
			if(_res!=null)
				_res.fireMyResourceListener(owner);
		}
	}
}
