package org.peertrust.pteditor.views;

import org.eclipse.swt.dnd.ByteArrayTransfer;
import org.eclipse.swt.dnd.TransferData;
import org.peertrust.pteditor.model.MyResource;

public class MyResourceTransfer extends ByteArrayTransfer {
	private static final String RESTYPENAME = "my_type_name";
	private static final int RESTYPEID = registerType(RESTYPENAME);
	private static MyResourceTransfer _instance = new MyResourceTransfer();

	private MyResourceTransfer() {
	}
 
	public static MyResourceTransfer getInstance() {
		return _instance;
	}
	 
	public void javaToNative(Object object,TransferData transferData) {
		if(object==null)
			return;
		if(!(object instanceof MyResource))
			return;
		if(isSupportedType(transferData)) {
			MyResource myRes=(MyResource)object;
			byte[] buffer=myRes.toByteArray();
			super.javaToNative(buffer,transferData);
		}
	}

	public Object nativeToJava(TransferData transferData) {	
		if(isSupportedType(transferData)) {
 			byte[] buffer=(byte[])super.nativeToJava(transferData);
			if((buffer==null)||(buffer.length==0))
				return null;
			MyResource res=MyResource.readByteArray(buffer);
			return res;
		}
		return null;
	}

	protected String[] getTypeNames(){
		return new String[] {RESTYPENAME};
	}
	
	protected int[] getTypeIds(){
		return new int[] {RESTYPEID};
	}
}