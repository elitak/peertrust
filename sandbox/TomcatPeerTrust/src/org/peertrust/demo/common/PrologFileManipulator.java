package org.peertrust.demo.common;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class PrologFileManipulator {

	private File prologFile=null;
	 
	public PrologFileManipulator(){
		
	}
	
	public void loadPrologFile(String prologFilePath){
		if(prologFilePath==null){
			throw new RuntimeException("Parameter prologFilePath must not be null");
		}
		this.prologFile= new File(prologFilePath);
		if(!prologFile.exists()){
			throw new RuntimeException("File not found:"+prologFilePath);
		}
	}
	
	public void savePrologFile(){
		
	}
	
	public void addCredential(String credential){
		if(prologFile==null){
			throw new RuntimeException("prologFilePath not set");
		}
		
		if(credential==null){
			throw new RuntimeException("Parameter credential must not be null");
		}
		
		try {
			FileOutputStream fileOut=
				new FileOutputStream(prologFile,true);
			fileOut.write(credential.getBytes());
		
		}  catch (FileNotFoundException e) {
			e.printStackTrace();
			throw new Error("File does not exist anymore:"+prologFile,e);
		}catch (IOException e) {
			e.printStackTrace();
			throw new Error("Could not write prolog file:"+prologFile,e);
		}
	}
	
	public void removeCredential(String credential){
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
