package org.peertrust.inference;

import org.peertrust.inference.*;

import com.declarativa.interprolog.*;
import com.xsb.interprolog.*;

/**
 * $Id: XsbProlog.java,v 1.1 2004/12/21 11:09:34 seb0815 Exp $
 * @author $Author: seb0815 $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/12/21 11:09:34 $
 * @description
 */
public class XsbProlog extends InferenceEngine
{
	static final String XSB_DIR_PATH = "/home/dolmedilla/Documents/apps/XSB" ;
	
	InferenceEngine engine ;
	XsbProlog ()
	{
		engine = new NativeEngine(XSB_DIR_PATH);
	}
	
	boolean command (String command)
	{
			return engine.command(command) ;
	}
	
	String [] execute (String query, String [] variables)
	{
		String xsbVariables = "[" + variables[0] ;
		for (int i = 1 ; i < variables.length ; i++)
			{
				xsbVariables +="," + variables[i] ;
			}
		xsbVariables += "]" ;
		
			System.out.println ("vars: " + xsbVariables) ;
		Object [] results = engine.deterministicGoal(query, xsbVariables) ;
		String [] sResults = new String[results.length] ;
		for (int i = 0 ; i < results.length ; i++)
			sResults[i] = (String) results[i] ;
		return sResults ;
	}
	
	public static void main(String[] args)
	{	
		XsbProlog xsb = new XsbProlog() ;
		
  		xsb.command("[basics]");
  		String[] vars = new String[1] ;
  		vars[0] = "X" ;
  		
  		//String[] bindings = xsb.execute("append(\"Hello,\",\"dolmedilla\", ML), name(X,ML)", vars) ;
  		String[] bindings = xsb.execute("X is 20", vars) ;
  		String message = bindings[0];
  		System.out.println("\nMessage:"+message);
  		System.exit(0);
  
// 			"name(User,UL),append(\"Hello,\", UL, ML), name(Message,ML)",
//    		"[string(User)]",
//    		new Object[]{System.getProperty("user.name")},
//    		"[string(Message),string(User)]");
//  		String message = (String)bindings[0]+(String)bindings[1];
//  		System.out.println("\nMessage:"+message);
//  		// the above demonstrates object passing both ways; 
//  		// since we may simply concatenate strings, an alternative coding would be:
//  		bindings = engine.deterministicGoal(
//    		"name('"+System.getProperty("user.name")+"',UL),append(\"Hello,\", UL, ML), name(Message,ML)",
//    		"[string(Message)]");
//  		// (notice the ' surrounding the user name, unnecessary in the first case)
//  		System.out.println("Same:"+bindings[0]);
//  		System.exit(0);

  	}
}