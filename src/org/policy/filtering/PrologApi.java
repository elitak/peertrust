package org.policy.filtering;

import org.peertrust.config.*;
import org.peertrust.exception.*;

//import org.policy.action.*;
//import java.io.*;

//the interface for a prolog engine
public interface PrologApi extends Configurable {

	//initiate the prolog engine
	public void init () throws ConfigurationException ;
	
/*	
	//load a program from a string
	public void load( String source) throws Exception;
	
	//load a program from a file
	public void load( File file) throws Exception;
	
	//execute a command
	public boolean execute( String command) throws Exception;
	
	//execute a query just once
	public ActionResult queryOnce( String command) throws Exception;
	
	//execute a query just once and return only the bindings for the specified vars
	public ActionResult queryOnce( String command, String[] vars) throws Exception;
	
	//execute a query untill it fails
	public ActionResult query( String command) throws Exception;
	
	//execute a query untill it fails and return only the bindings for the specified vars
	public ActionResult query( String command, String[] vars) throws Exception;
	
	//assert the fact to the program
	public boolean add( String fact) throws Exception;
	
	//retract the fact
	public ActionResult retract( String fact) throws Exception;
	
	//retract the fact and return the bindings only for vars
	public ActionResult retract( String fact, String[] vars) throws Exception;
	
	//retract all rules matching fact
	public ActionResult retractall( String fact) throws Exception;
	
	//retract all rules matching fact and return the bindings only for vars
	public ActionResult retractall( String fact, String[] vars) throws Exception;
	
	//describe the action result
	public String toString( ActionResult result);
*/
}

