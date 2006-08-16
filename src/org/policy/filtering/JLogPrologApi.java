package org.policy.filtering;

import ubc.cs.JLog.Foundation.*;
import org.apache.log4j.*;
import org.peertrust.exception.*;
import org.policy.action.*;

import java.io.*;
import java.util.*;

//implementation of the prolog api for JLog
public class JLogPrologApi implements PrologApi{
	
	private jPrologAPI jlog=null;
	private Logger logger=null;
	
	//-------------------------------------------------------------------------------------------
	public void init() throws ConfigurationException{

		jlog=new jPrologAPI("");
		jlog.setFailUnknownPredicate(true);
		
		logger = Logger.getLogger("JLogPrologApi");
		logger.setLevel(Level.ALL);
		
		logger.debug("init: Jlog engine succesfully initialized.");
	}
	
	//-------------------------------------------------------------------------------------------
	public void load( String source) throws PrologEngineException{
		
		if(jlog==null)
			throw new PrologEngineException("No prolog engine, must call init first.");
		
		jlog.consultSource(source);
		
		logger.debug("load: Source string consulted succesfully.");
	}
	
	//-------------------------------------------------------------------------------------------
	public void load( File file) throws PrologEngineException, IOException{
		
		if(jlog==null)
			throw new PrologEngineException("No prolog engine, must call init first.");
		
		BufferedReader br=new BufferedReader(new FileReader(file));
		StringBuffer sb=new StringBuffer();

		String line=null;
		while(true){
				
			line=br.readLine();
			if(line==null)
				break;
				
			sb.append(line+"\n");
		}
		
		load(sb.toString());
		
		logger.debug("load: Source file consulted succesfully.");
	}
	
	//-------------------------------------------------------------------------------------------
	public boolean execute( String command) throws PrologEngineException{
		
		if(jlog==null)
			throw new PrologEngineException("No prolog engine, must call init first.");
		
		Hashtable result=jlog.query(command);
		
		logger.debug("execute: Query \""+command+"\" executed.");
		
		if(result==null)
			return false;
		
		return true;
	}
	
	//-------------------------------------------------------------------------------------------
	//transform objects returned by the prolog engine in strings
	private String transform(Object o){
		
		//object is a string stored as a vector
		if(o instanceof Vector){
			
			Vector v=(Vector)o;
			byte[] b=new byte[v.size()];
			for(int i=0;i<v.size();i++)
				b[i]=((Integer)v.get(i)).byteValue();
					
			return new String(b);
		}
		
		return o.toString();
	}

	//-------------------------------------------------------------------------------------------
	//transform an enumeration of objects into a vector of strings
	private String[] transform( Enumeration e, int size){
		
		String[] sv=new String[size];
		
		int i=0;
		while(e.hasMoreElements()){
			
			sv[i]=transform(e.nextElement());
			i++;
		}
		
		return sv;
	}
	
	//-------------------------------------------------------------------------------------------
	//creates an ActionResult from a hashtable returned by jlog as a query result
	private ActionResult getActionResult(Hashtable result) throws FunctionFailureException{
		
		if(result==null)
			return new ActionResult(false);
		
		String[] variableList=transform(result.keys(),result.size());
		ActionResult ar=new ActionResult( true, variableList);
	
		String[] bindings=new String[result.size()];
		for(int i=0;i<result.size();i++)
			bindings[i]=transform(result.get(variableList[i]));

		ar.getVariableBindings().addResult(new Result(bindings));
		
		return ar;
	}
	
	//-------------------------------------------------------------------------------------------
	private ActionResult getActionResult( Hashtable result, String[] vars) throws FunctionFailureException{
		
		if(result==null)
			return new ActionResult(false);
		
		Vector v=new Vector();
		Vector b=new Vector();
		
		for(int i=0;i<vars.length;i++){
			
			if(result.containsKey(vars[i])){
				v.add(vars[i]);
				b.add(transform(result.get(vars[i])));
			}
		}
			
		String[] variableList=transform(v.elements(),v.size());
		ActionResult ar=new ActionResult( true, variableList);
		ar.getVariableBindings().addResult(new Result(transform(b.elements(),b.size())));
		
		return ar;
	}
	
	//-------------------------------------------------------------------------------------------
	public ActionResult queryOnce( String command) throws PrologEngineException, FunctionFailureException{
		
		if(jlog==null)
			throw new PrologEngineException("No prolog engine, must call init first.");
		
		Hashtable result=jlog.queryOnce(command);
		
		logger.debug("queryOnce: Query \""+command+"\" executed.");
		
		return getActionResult(result);
	}
	
	//-------------------------------------------------------------------------------------------
	public ActionResult queryOnce( String command, String[] vars) throws PrologEngineException, FunctionFailureException{
		
		if(jlog==null)
			throw new PrologEngineException("No prolog engine, must call init first.");
		
		Hashtable result=jlog.queryOnce(command);
		
		logger.debug("queryOnce: Query \""+command+"\" executed.");
		
		return getActionResult(result,vars);
	}
	
	//-------------------------------------------------------------------------------------------
	public ActionResult query( String command) throws PrologEngineException, FunctionFailureException{
		
		if(jlog==null)
			throw new PrologEngineException("No prolog engine, must call init first.");
		
		Hashtable result=jlog.query(command);
		ActionResult ar=getActionResult(result);
		
		logger.debug("query: Execute query \""+command+"\" first time.");
		
		if(ar.getExecutionResult()==false)
			return ar;
		
		int count=2;
		while(true){
			
			result=jlog.retry();
			
			logger.debug("query: Retry query \""+command+"\" "+count+" time.");
			count++;
			
			if(result==null)
				break;
			
			String[] bindings=new String[result.size()];
			for(int i=0;i<result.size();i++)
				bindings[i]=transform(result.get(ar.getVariable(i)));
			
			ar.getVariableBindings().addResult(new Result(bindings));
		}
		
		return ar;
	}
	
	//-------------------------------------------------------------------------------------------
	public ActionResult query( String command, String[] vars) throws PrologEngineException, FunctionFailureException{
	
		if(jlog==null)
			throw new PrologEngineException("No prolog engine, must call init first.");
		
		Hashtable result=jlog.query(command);
		ActionResult ar=getActionResult(result,vars);
		
		logger.debug("query: Execute query \""+command+"\" first time.");
		
		if(ar.getExecutionResult()==false)
			return ar;
		
		int count=2;
		while(true){
			
			result=jlog.retry();
			
			logger.debug("query: Retry query \""+command+"\" "+count+" time.");
			count++;
			
			if(result==null)
				break;
			
			String[] bindings=new String[ar.getNumberVariables()];
			for(int i=0;i<ar.getNumberVariables();i++)
				bindings[i]=transform(result.get(ar.getVariable(i)));
			
			ar.getVariableBindings().addResult(new Result(bindings));
		}
		
		return ar;
	}
	//-------------------------------------------------------------------------------------------
	private String processRule(String rule){
		
		for(int i=rule.length()-1;i>=0;i--){
			
			if(rule.charAt(i)=='.')
				return rule.substring(0,i);
			
			if(rule.charAt(i)!=' ' && rule.charAt(i)!='\t' )
				break;
		}
		
		return rule;
	}
	
	//-------------------------------------------------------------------------------------------
	public boolean assertRule( String rule) throws PrologEngineException{
		
		if(jlog==null)
			throw new PrologEngineException("No prolog engine, must call init first.");
		
		String s=processRule(rule);
		Hashtable result=jlog.queryOnce("assert("+s+").");
		
		logger.debug("assertRule: Assertion of rule \""+s+"\" executed.");
		
		if(result==null)
			return false;
		
		return true;
	}
	
	//-------------------------------------------------------------------------------------------
	public ActionResult retract( String rule) throws PrologEngineException, FunctionFailureException{
		
		if(jlog==null)
			throw new PrologEngineException("No prolog engine, must call init first.");
		
		String s=processRule(rule);
		ActionResult ar=queryOnce("retract("+s+").");
		
		logger.debug("retract: Retraction of rule \""+s+"\" executed.");
		
		return ar;
	}
	
	//-------------------------------------------------------------------------------------------
	public ActionResult retract( String rule, String[] vars) throws PrologEngineException, FunctionFailureException{
		
		if(jlog==null)
			throw new PrologEngineException("No prolog engine, must call init first.");
		
		String s=processRule(rule);
		ActionResult ar=queryOnce("retract("+s+").",vars);
		
		logger.debug("retract: Retraction of rule \""+s+"\" executed.");
		
		return ar;
	}
	
	//-------------------------------------------------------------------------------------------
	public ActionResult retractall( String rule) throws PrologEngineException, FunctionFailureException{
		
		if(jlog==null)
			throw new PrologEngineException("No prolog engine, must call init first.");
		
		String s=processRule(rule);
		ActionResult ar=query("retract("+s+").");
		
		logger.debug("retractall: Retraction of rules matching \""+s+"\" executed.");
		
		return ar;
	}
	
	//-------------------------------------------------------------------------------------------
	public ActionResult retractall( String rule, String[] vars) throws PrologEngineException, FunctionFailureException{
		
		if(jlog==null)
			throw new PrologEngineException("No prolog engine, must call init first.");
		
		String s=processRule(rule);
		ActionResult ar=query("retract("+s+").",vars);
		
		logger.debug("retractall: Retraction of rules matching \""+s+"\" executed.");
		
		return ar;
	}
	
	//---------------------------------------------------------------------------------------------------------------
	public static void main(String[] args){

		try{
			
			PrologApi prolog=new JLogPrologApi();
			prolog.init();
			
			
			prolog.load(new File("./src/org/policy/filtering/kb1.pro"));
			prolog.load(new File("./src/org/policy/filtering/kb2.pro"));
			
			if(prolog.execute("autor(denisa)."))
				System.out.println("true");
			else
				System.out.println("false");
			
			ActionResult ar=prolog.queryOnce("autor(X,Y).");
			System.out.println(ar.toString());
			
			ar=prolog.queryOnce("autor(X,Y).", new String[]{"X"});
			System.out.println(ar.toString());
			
			ar=prolog.query("autor1(X,Y).");
			System.out.println(ar.toString());
			
			ar=prolog.query("autor1(X,Y).", new String[]{"X"});
			System.out.println(ar.toString());
			
			if(prolog.assertRule("autor2(maria,'paper1')"))
				System.out.println("true");
			else
				System.out.println("false");
			
			if(prolog.assertRule("autor2(X,'paper1'):-autor(X). \t "))
				System.out.println("true");
			else
				System.out.println("false");
			
			ar=prolog.query("autor2(X,Y).");
			System.out.println(ar.toString());
			
			ar=prolog.retract("autor2(X,'paper1')");
			System.out.println(ar.toString());
			
			prolog.assertRule("autor3(denisa,'paper1')");
			prolog.assertRule("autor3(dragos,'paper1')");
			
			ar=prolog.retractall("autor3(X,'paper1').",new String[]{"X"});
			System.out.println(ar.toString());	
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}

