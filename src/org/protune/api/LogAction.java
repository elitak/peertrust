package org.protune.api;

import java.io.*;

/**
 * Class {@link org.protune.api.LogAction} represents the provisional predicate <tt>logged(X,
 * logfile_name)</tt> which holds if <tt>X</tt> is recorded into <tt>logfile_name</tt> (cf. deliverable
 * <tt>I2-D2</tt>).
 * @author jldecoi
 */
public class LogAction extends SideEffectAction {
	
	String toWrite;
	String fileName;

	LogAction(String s1, String s2){
		toWrite = s1;
		fileName = s2;
	}
	
	public Notification perform() {
		try{
			FileWriter f = new FileWriter(fileName, true);
			f.write(toWrite);
			f.close();
			return new ActionWellPerformed(this);
		}
		catch(IOException ioe){
			return new ActionWrongPerformed(this);
		}
	}
	
	/*
	 * According to deliverable <tt>I2-D2</tt> the Prolog representation of this action is
	 * <blockquote><tt>logged(&lt;toWrite&gt;, &lt;fileName&gt;)</tt></blockquote>
	 * <b>OPEN ISSUE:</b> Should it maybe have been <tt>do(logged(&lt;toWrite&gt;,
	 * &lt;fileName&gt;))</tt>?.
	 *
	String toGoal(){
		return "logged(" + toWrite + ", " + fileName + ")";
	}*/
	
}
