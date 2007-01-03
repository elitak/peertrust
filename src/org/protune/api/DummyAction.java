package org.protune.api;

public class DummyAction extends Action {

	String toWrite;
	
	DummyAction(String s){
		toWrite = s;
	}
	
	public Notification perform() {
		System.out.println(toWrite);
		return new ActionWellPerformed(this);
	}

}
