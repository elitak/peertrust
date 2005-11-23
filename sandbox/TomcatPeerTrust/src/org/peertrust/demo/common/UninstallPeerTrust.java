package org.peertrust.demo.common;

import java.io.File;
import java.util.LinkedList;

/**
 * UninstallPeerTrust wraps the code to delete peertrust files
 * in an Exectable.
 * 
 * @author Patrice Congo (token77)
 *
 */
public class UninstallPeerTrust implements Executable {
	/** the name of the peertrust installation folder.*/
	public static final String PEERTRUST_FOLDER_NAME=".peertrust";
	
	/** error messages are save in this string*/
	private String message;
	
	
	/**
	 * When call removes the peertrust folder and its contain.
	 * @param  param --	execution parameter; it is ignore therefore 
	 * 			it can savely be null
	 */
	public void execute(Object param) {
		String home=null;
		try{
			home=System.getProperty("user.home");
		}catch(Exception e){
			message="Could not start unintall-Process.\n"+
					"Reason unable to get your home directory\n"+
					"Exception message:"+e.getLocalizedMessage();
			return;
		}
		
		File instDir= new File(home,PEERTRUST_FOLDER_NAME);
		
		if(instDir.exists()){
			try{
				LinkedList filesToDel= new LinkedList();
				File files[]=null;
				filesToDel.add(instDir);
				
				File currentFile=null;
				for(;!filesToDel.isEmpty();){
					currentFile=(File)filesToDel.removeFirst();
					files=currentFile.listFiles();
					if(files!=null){
						if(files.length==0){
							currentFile.delete();
							if(currentFile.exists()){
								message="Could not delete0 "+currentFile.getAbsolutePath();
								return;
							}
						}else{
							filesToDel.addFirst(currentFile);
							for(int i=0;i<files.length;i++){
								if(files[i].isDirectory()){
									filesToDel.addFirst(files[i]);
								}else{
									files[i].delete();
									if(files[i].exists()){
										message="Could not delete "+files[i].getAbsolutePath();
										return;
									}
									//filesToDel.add(files[i]);
								}
							}
						}
					}
				}
				
				instDir.delete();
				if(!instDir.exists()){
					message="Uninstall successfull!";
				}else{
					message="could not delete directory1:"+instDir.getAbsolutePath();
				}
				return;
			}catch(SecurityException e){
				message="Exception while uninstalling:"+e.getLocalizedMessage();
				return;
			}			
		}else{
			message="PeerTrust not install. Cannot find:"+instDir.getAbsolutePath();
		}
	}

	


	/**
	 * @return Returns the last error message.
	 */
	public String getMessage() {
		return message;
	}


//
//
//	static public void main(String[] args){
//		UninstallPeerTrust uinst= new UninstallPeerTrust();
//		uinst.execute(null);
//		System.out.println(uinst.getMessage());
//	}

}
