package org.protune.api;

import java.io.Serializable;

public class DummyAction extends Action implements Serializable{

	String toWrite;
	
	public DummyAction(String s){
		toWrite = "";
		for(int i=0; i<s.length(); i++)
		   if(s.charAt(i)=='_') toWrite += 'a';
		   else toWrite += s.charAt(i);
	}
	
	public Notification perform() {
		System.out.println(toWrite);
		return new ActionWellPerformed(this);
	}

	public String toString(){
		return toWrite;
	}
	
}
