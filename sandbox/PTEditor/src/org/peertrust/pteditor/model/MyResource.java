package org.peertrust.pteditor.model;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.core.runtime.ListenerList;

public class MyResource {
	public static final int TYPE_CREDENTIAL=0;
	public static final int TYPE_DIRECTORY=1;
	public static final int TYPE_FILE=2;
	public static final int TYPE_CLASS=3;
	public static final int TYPE_ROOT=4;
	public static final int TYPE_FILE_ROOT=5;
	public static final int TYPE_INVALID=6;

	private String strName;
	private String strFileName;
	private static HashMap<Long,MyResource> hashmapAllResources=new HashMap<Long,MyResource>();
	private static int nNextID=0;
	private Long nID;
	private Set<Long> setPolicies;
	private Long parentResource;
	private Set<Long> setChildren;
	private int nType;
	private ListenerList listenerList=null;
	
	public MyResource(String name,MyResource parent,int type) {
		strName=name;
		setPolicies=new TreeSet<Long>();
		parentResource=(parent!=null) ? parent.getID() : new Long(-1);
		setChildren=new TreeSet<Long>();
		strFileName=null;
		if(type==TYPE_INVALID)
			determineType();
		else
			nType=type;
		if(name!=null) {
			nID=new Long(nNextID++);
			hashmapAllResources.put(nID,this);
		}
		if(parent!=null)
			parent.addChild(this);
		fireMyResourceListener(nID);
	}
	
	public MyResource(String name,MyResource parent) {
		this(name,parent,TYPE_INVALID);
	}

	public Long getID() {
		return nID;
	}
	
	public void setParent(MyResource parent) {
		parentResource=(parent!=null) ? parent.getID() : new Long(-1);
		fireMyResourceListener(nID);
	}

	public void setName(String name) {
		strName=name;
		determineType();
		fireMyResourceListener(nID);
	}
	
	public String toString() {
		if(strFileName!=null) {
			if(nType==TYPE_FILE)
				return strFileName;
			else if(nType==TYPE_DIRECTORY)
				return strFileName;
		}
		return strName;
	}
	
	public void setPolicies(MyPolicy[] policies) {
		setPolicies.clear();
		for(int i=0;i<policies.length;i++)
			setPolicies.add(policies[i].getID());
		fireMyResourceListener(nID);
	}
	
	public void addPolicy(MyPolicy policy) {
		setPolicies.add(policy.getID());
		fireMyResourceListener(nID);
	}
	
	public List<MyPolicy> getPolicies() {
		List<MyPolicy> list=new LinkedList<MyPolicy>();
		Iterator<Long> iter=setPolicies.iterator();
		while(iter.hasNext())
			list.add(MyPolicy.getAllPolicies().get(iter.next()));
		return list;
	}
	
	public void setChildren(MyResource children[]) {
		setChildren.clear();
		for(int i=0;i<children.length;i++)
			setChildren.add(children[i].getID());
		fireMyResourceListener(nID);
	}
	
	public List<MyResource> getChildren() {
		List<MyResource> list=new LinkedList<MyResource>();
		Iterator<Long> iter=setChildren.iterator();
		while(iter.hasNext())
			list.add(getAllResources().get(iter.next()));
		return list;
	}
	
	public MyResource getParent() {
		return getAllResources().get(parentResource);
	}
	
	public byte[] toByteArray() {
		ByteArrayOutputStream bos=new ByteArrayOutputStream();
		DataOutputStream dos=new DataOutputStream(bos);
		try {
			dos.writeUTF(strName);
			dos.writeUTF((strFileName!=null) ? strFileName : "");
			dos.writeLong(nID.longValue());
			dos.writeInt(setPolicies.size());
			Iterator<Long> iter=setPolicies.iterator();
			while(iter.hasNext())
				dos.writeLong(iter.next().longValue());
			dos.writeLong(parentResource.longValue());
			dos.writeInt(setChildren.size());
			iter=setChildren.iterator();
			while(iter.hasNext())
				dos.writeLong(iter.next().longValue());
			dos.writeInt(nType);
//			private ListenerList listenerList=null;
			dos.close();
			bos.close();
		} catch (IOException e) {
		}
		byte data[]=bos.toByteArray();
		return data;
	}
	
	public static MyResource readByteArray(byte data[]) {
		ByteArrayInputStream bis=new ByteArrayInputStream(data);
		DataInputStream dis=new DataInputStream(bis);
		MyResource myres=new MyResource(null,null);
		try {
			myres.strName=dis.readUTF();
			String str=dis.readUTF();
			myres.strFileName=(str.length()>0) ? str : null;
			myres.nID=new Long(dis.readLong());
			int length=dis.readInt();
			myres.setPolicies=new TreeSet<Long>();
			for(int i=0;i<length;i++)
				myres.setPolicies.add(new Long(dis.readLong()));
			myres.parentResource=new Long(dis.readLong());
			length=dis.readInt();
			myres.setChildren=new TreeSet<Long>();
			for(int i=0;i<length;i++)
				myres.setChildren.add(new Long(dis.readLong()));
			myres.nType=dis.readInt();
//			private ListenerList listenerList=null;
			dis.close();
			bis.close();
		} catch (IOException e) {
			return null;
		}
		hashmapAllResources.put(myres.nID,myres);
		return myres;
	}

	private void determineType() {
		if(strName==null) {
			nType=TYPE_INVALID;
			return;
		}
		if((getParent()!=null)&&(getParent().getParent()==null)) {
			nType=TYPE_INVALID;
			return;
		}
		File file=new File(strName);
		if(file.exists()) {
			strFileName=file.getName();
			if(file.isDirectory())
				nType=TYPE_DIRECTORY;
			else
				nType=TYPE_FILE;
		}
		else
			nType=TYPE_CREDENTIAL;
	}
	
	public int getType() {
		return nType;
	}

	public void addMyResourceListener(IMyResourceChangedListener _listener) {
		if(listenerList==null)
			listenerList=new ListenerList();
		listenerList.add(_listener);
	}
	
	public void fireMyResourceListener(Long res) {
		if(getAllResources().get(res)==null)
			return;
		if(listenerList!=null) {
			Object obj[]=listenerList.getListeners();
			for(int i=0;i<obj.length;i++) {
				if(!(obj[i] instanceof IMyResourceChangedListener))
					continue;
				((IMyResourceChangedListener)obj[i]).notifyResourceChanged(
					getAllResources().get(res));
			}
		}
		if(parentResource.longValue()>=0) {
			MyResource _res=getAllResources().get(parentResource);
			if(_res!=null)
				_res.fireMyResourceListener(res);
		}
	}
	
	public void removeMyResourceListener(IMyResourceChangedListener listener) {
		if(listenerList==null)
			return;
		listenerList.remove(listener);
	}
	
	public void removeChild(MyResource res) {
		if(!setChildren.contains(res.getID()))
			return;
		setChildren.remove(res.getID());
		if(res.getParent()!=null)
			res.getParent().fireMyResourceListener(res.getID());
	}
	
	public void addChild(MyResource res) {
		setChildren.add(res.getID());
		res.setParent(this);
		res.fireMyResourceListener(nID);
	}
	
	public String getName() {
		return strName;
	}

	public List<MyPolicy> getInheritedPolicies() {
		List<MyPolicy> list=new LinkedList<MyPolicy>();
		MyResource res=this;
		List<MyPolicy> policies;
		while(res.getParent()!=null) {
			res=res.getParent();
			policies=res.getPolicies();
			for(int i=0;i<policies.size();i++)
				list.add(i,policies.get(i));
		}
		return list;
	}
	
	public void removePolicy(MyPolicy policy) {
		setPolicies.remove(policy.getID());
		fireMyResourceListener(nID);
	}
	
	public void replacePolicy(MyPolicy originalPolicy,MyPolicy newPolicy) {
		originalPolicy.cloneFromPolicy(newPolicy);
		fireMyResourceListener(nID);
	}

	public List<MyPolicy> getAllPolicies() {
		List<MyPolicy> listPolicies=new LinkedList<MyPolicy>();
		List<MyResource> listPath=getPathFromRootToResource();
		for(int i=0;i<listPath.size();i++) {
			MyResource res=listPath.get(i);
			for(int j=0;j<res.getPolicies().size();j++) {
				MyPolicy policy=res.getPolicies().get(j);
				if(policy.getOverridePolicies()!=null) {
					for(int k=0;k<policy.getOverridePolicies().size();k++) {
						MyPolicy old_policy=policy.getOverridePolicies().get(k);
						if((old_policy.getDefault())&&(listPolicies.contains(old_policy)))
							listPolicies.remove(old_policy);
					}
				}
				listPolicies.add(policy);
			}
		}
//System.out.println(listPolicies);
		return listPolicies;
	}
	
	public List<MyResource> getPathFromRootToResource() {
		List<MyResource> listPath=new LinkedList<MyResource>();
		MyResource res=this;
		listPath.add(res);
		while(res.getParent()!=null) {
			listPath.add(0,res.getParent());
			res=res.getParent();
		}
		return listPath;
	}
	
	public String getFullPath() {
		if((nType==TYPE_DIRECTORY)||(nType==TYPE_FILE))
			return getName();
		List<MyResource> listAllRes=getPathFromRootToResource();
		StringBuffer strbuf=new StringBuffer();
		int size=listAllRes.size();
		for(int i=1;i<size;i++) {
			strbuf.append(listAllRes.get(i));
			if(i<size-1)
				strbuf.append('/');
		}
		return strbuf.toString();
	}
	
	protected static HashMap<Long,MyResource> getAllResources() {
		return hashmapAllResources;
	}
	
	public static HashMap<String,MyResource> checkFileHierarchy(MyResource resourceRoot) {
		HashMap<String,MyResource> hashmap=new HashMap<String,MyResource>();
		List<MyResource> listFileRoots=getFileRoots(resourceRoot);
		for(int i=0;i<listFileRoots.size();i++) {
			MyResource res=listFileRoots.get(i);
			if(!new File(res.getName()).isDirectory())
				hashmap.put((hashmap.size()+1)+": File root "+res.getName()+
					" doesn't exist",res);
			else {
				List<MyResource> list=new LinkedList<MyResource>();
				list.add(res);
				while(!list.isEmpty())
					checkFiles(list,hashmap);
			}
		}
		//Fehlerliste erstellen
		//File roots holen
		//File roots durchgehen
			//Gibt es directory noch?
				//directory in liste
				//solange liste nicht leer
					//hole erstes directory der liste
					//sind alle files und directories da (resource teilenge real)?
						//wenn nein, meldung in fehlerliste
					//neue files oder directories?
						//wenn ja, ungefragt hinzufügen
					//directories merken
			//Ansonsten
				//Meldung in fehlerliste
		return hashmap;
	}
	
	private static void checkFiles(List<MyResource> list,
		HashMap<String,MyResource> hashmap) {
		MyResource resource=list.remove(0);
		List<MyResource> listChildren=resource.getChildren();
		File fileResource=new File(resource.getName());
		File files[]=fileResource.listFiles();
		for(int i=0;i<listChildren.size();i++) {
			MyResource res=listChildren.get(i);
			boolean bFound=false;
			String strFilename=res.getName();
			fileResource=new File(strFilename);
			for(int j=0;j<files.length;j++)
				if((files[j]!=null)&&(strFilename.equals(files[j].getAbsolutePath()))) {
					bFound=true;
					files[j]=null;
					break;
				}
			if(!bFound)
				hashmap.put((hashmap.size()+1)+": File or directory "+res.getName()+
					" doesn't exist",res);
			else if(fileResource.isDirectory())
				list.add(res);
		}
		for(int j=0;j<files.length;j++)
			if(files[j]!=null)
				new MyResource(files[j].getAbsolutePath(),resource);
	}

	private static List<MyResource> getFileRoots(MyResource resourceRoot) {
		List<MyResource> listReturn=new LinkedList<MyResource>();
		List<MyResource> listChildren=null;
		do {
			listChildren=resourceRoot.getChildren();
			for(int i=0;i<listChildren.size();i++) {
				if(listChildren.get(i).getType()==MyResource.TYPE_FILE_ROOT)
					listReturn.add(listChildren.get(i));
			}
			if((listChildren.size()>0)&&(listReturn.size()==0))
				resourceRoot=listChildren.get(0);
		} while((listReturn.size()==0)&&(listChildren.size()>0));
		return listReturn;
	}
	
//	public static void dumpList() {
//		for(int i=0;i<hashmapAllResources.size();i++)
//			System.out.println(i+" "+hashmapAllResources.get(new Long(i))+" / "+hashmapAllResources.get(new Long(i)).getParent()+" / "+hashmapAllResources.get(new Long(i)).getChildren());
//	}
}