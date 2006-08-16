package org.policy.filtering;

import org.peertrust.config.*;
import org.peertrust.exception.*;
import org.policy.action.*;
import java.io.*;

//the interface for a prolog engine
public interface PrologApi extends Configurable {

	//initiate the prolog engine
	public void init () throws ConfigurationException ;
	
	//load a program from a string
	public void load( String source) throws PrologEngineException;

	//load a program from a file
	public void load( File file) throws PrologEngineException, IOException;

	//execute a command
	public boolean execute( String command) throws PrologEngineException;

	//execute a query just once
	public ActionResult queryOnce( String command) throws PrologEngineException, FunctionFailureException;

	//execute a query just once and return only the bindings for the specified vars
	public ActionResult queryOnce( String command, String[] vars) throws PrologEngineException, FunctionFailureException;
	
	//execute a query untill it fails
	public ActionResult query( String command) throws PrologEngineException, FunctionFailureException;
	
	//execute a query untill it fails and return only the bindings for the specified vars
	public ActionResult query( String command, String[] vars) throws PrologEngineException, FunctionFailureException;

	//assert the rule to the knowledge base
	public boolean assertRule( String rule) throws PrologEngineException;
	
	//retract a rule
	public ActionResult retract( String rule) throws PrologEngineException, FunctionFailureException;
	
	//retract the rule and return the bindings only for vars
	public ActionResult retract( String rule, String[] vars) throws PrologEngineException, FunctionFailureException;
	
	//retract all rules matching the rule given as argument
	public ActionResult retractall( String fact) throws PrologEngineException, FunctionFailureException;
	
	//retract all rules matching the rule given as argument and return the bindings only for vars
	public ActionResult retractall( String fact, String[] vars) throws PrologEngineException, FunctionFailureException;

}

