package org.policy.filtering;

/*
import ubc.cs.JLog.Foundation.*;
import org.apache.log4j.*;
import org.peertrust.exception.*;
*/

//import org.policy.action.*;

//import java.io.*;
//import java.util.*;

//implementation of the prolog api for JLog
public class JLogPrologApi //implements PrologApi
{
/*
	private jPrologAPI jlog=null;
	private Logger logger=null;
	
	//-------------------------------------------------------------------------------------------
	public void init() throws ConfigurationException{

		jlog=new jPrologAPI(null);
		jlog.setFailUnknownPredicate(true);
		
		logger = Logger.getLogger("JLogPrologApi");
		logger.setLevel(Level.ALL);
		
		logger.debug("init: Jlog engine succesfull initialized.");
	}
	
	/*-------------------------------------------------------------------------------------------
//
	public void load( String source) throws Exception{
		
		if(jlog==null)
			throw new Exception("No prolog engine, must call init first.");
		
		jlog.consultSource(source);
	}
	
	/*-------------------------------------------------------------------------------------------
	public void load( File file) throws Exception{
		
		if(jlog==null)
			throw new Exception("No prolog engine, must call init first.");
		
		StringBuffer sb=new StringBuffer();
		
		try{
			
			BufferedReader br=new BufferedReader(new FileReader(file));
			
			String line;
			while(true){
				
				line=br.readLine();
				if(line==null)
					break;
				
				sb.append(line+"\n");
			}
			
		}catch(Exception e){
			new Exception("Exceptie la citirea din fisier", e.getCause());
		}
		
		load(sb.toString());
	}
	
	/*-------------------------------------------------------------------------------------------
	public boolean execute( String command) throws Exception{
		
		if(jlog==null)
			throw new Exception("No prolog engine, must call init first.");
		
		Hashtable result=jlog.query(command);
		
		if(result==null)
			return false;
		
		return true;
	}
	
	/*-------------------------------------------------------------------------------------------
	private String transform(Object o){
		
		if(o instanceof Vector){
			
			Vector v=(Vector)o;
			byte[] b=new byte[v.size()];
			for(int i=0;i<v.size();i++)
				b[i]=((Integer)v.get(i)).byteValue();
					
			return new String(b);
		}
		
		return o.toString();
	}
	
	/*-------------------------------------------------------------------------------------------
	private String[] transform( Enumeration e, int size){
		
		String[] sv=new String[size];
		
		int i=0;
		while(e.hasMoreElements()){
			
			sv[i]=transform(e.nextElement());
			i++;
		}
		
		return sv;
	}
	
	/*-------------------------------------------------------------------------------------------
	private ActionResult getActionResult(Hashtable result) throws Exception{
		
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
	
	/*-------------------------------------------------------------------------------------------
	private ActionResult getActionResult( Hashtable result, String[] vars) throws Exception{
		
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
	/*-------------------------------------------------------------------------------------------
	public ActionResult queryOnce( String command) throws Exception{
		
		if(jlog==null)
			throw new Exception("No prolog engine, must call init first.");
		
		Hashtable result=jlog.queryOnce(command);
		return getActionResult(result);
	}
	
	/*-------------------------------------------------------------------------------------------
	public ActionResult queryOnce( String command, String[] vars) throws Exception{
		
		if(jlog==null)
			throw new Exception("No prolog engine, must call init first.");
		
		Hashtable result=jlog.queryOnce(command);
		return getActionResult(result,vars);
	}
	
	/*-------------------------------------------------------------------------------------------
	public ActionResult query( String command) throws Exception{
		
		if(jlog==null)
			throw new Exception("No prolog engine, must call init first.");
		
		Hashtable result=jlog.query(command);
		ActionResult ar=getActionResult(result);
		
		if(ar.getExecutionResult()==false)
			return ar;
		
		while(true){
			
			result=jlog.retry();
			if(result==null)
				break;
			
			String[] bindings=new String[result.size()];
			for(int i=0;i<result.size();i++)
				bindings[i]=transform(result.get(ar.getVariable(i)));
			
			ar.getVariableBindings().addResult(new Result(bindings));
		}
		
		return ar;
	}
	
	/*-------------------------------------------------------------------------------------------
	public ActionResult query( String command, String[] vars) throws Exception{
	
		if(jlog==null)
			throw new Exception("No prolog engine, must call init first.");
		
		Hashtable result=jlog.query(command);
		ActionResult ar=getActionResult(result,vars);
		
		if(ar.getExecutionResult()==false)
			return ar;
		
		while(true){
			
			result=jlog.retry();
			if(result==null)
				break;
			
			String[] bindings=new String[ar.getNumberVariables()];
			for(int i=0;i<ar.getNumberVariables();i++)
				bindings[i]=transform(result.get(ar.getVariable(i)));
			
			ar.getVariableBindings().addResult(new Result(bindings));
		}
		
		return ar;
	}
	
	/*-------------------------------------------------------------------------------------------
	public boolean add( String fact) throws Exception{
		
		if(jlog==null)
			throw new Exception("No prolog engine, must call init first.");
		
		Hashtable result=jlog.queryOnce("assert("+fact+").");
		if(result==null)
			return false;
		
		return true;
	}
	
	/*-------------------------------------------------------------------------------------------
	public ActionResult retract( String fact) throws Exception{
		
		if(jlog==null)
			throw new Exception("No prolog engine, must call init first.");
		
		ActionResult ar=queryOnce("retract("+fact+").");
		return ar;
	}
	
	/*-------------------------------------------------------------------------------------------
	public ActionResult retract( String fact, String[] vars) throws Exception{
		
		if(jlog==null)
			throw new Exception("No prolog engine, must call init first.");
		
		ActionResult ar=queryOnce("retract("+fact+").",vars);
		return ar;
	}
	
	/*-------------------------------------------------------------------------------------------
	public ActionResult retractall( String fact) throws Exception{
		
		if(jlog==null)
			throw new Exception("No prolog engine, must call init first.");
		
		ActionResult ar=query("retract("+fact+").");
		return ar;
	}
	
	/*-------------------------------------------------------------------------------------------
	public ActionResult retractall( String fact, String[] vars) throws Exception{
		
		if(jlog==null)
			throw new Exception("No prolog engine, must call init first.");
		
		ActionResult ar=query("retract("+fact+").",vars);
		return ar;
	}
	
	/*-------------------------------------------------------------------------------------------
	public String toString( ActionResult ar){
		
		StringBuffer sb=new StringBuffer();
		
		if(ar.getExecutionResult()==false){
			sb.append("false.\n");
		}
		else{
			sb.append("true.\n");
			
			for(int i=0; i<ar.getVariableBindings().getNumberResults();i++){
				
				sb.append((i+1)+": ");
				
				for(int j=0; j<ar.getNumberVariables(); j++){
					
					sb.append(ar.getVariable(j));
					sb.append("=");
					sb.append(ar.getResult(i).getBinding(j));
					sb.append("\n");
				}
				
				sb.append("\n");
			}
		}
		
		return sb.toString();
	}
	
	/*-------------------------------------------------------------------------------------------
	public void function(){
		
		//System.out.println(jlog.getTranslation());
		//jlog.setVariableTranslationKeys("X",null," ([jVariable] : String)");
		/*
		jTermTranslation tt=new jTermTranslation();
		tt.setDefaults();
		jlog.setTranslation(tt);
		
		Hashtable result=jlog.query("autor(A,B).");
		if(result==null){
			System.out.println("false");
			return;
		}
		
		Object o=result.get("A");
		System.out.println(o.toString());
		o=result.get("B");
		System.out.println(o.toString());
		
		result=jlog.retry();
		if(result==null){
			System.out.println("false");
			return;
		}
		
		o=result.get("A");
		System.out.println(o.toString());
		o=result.get("B");
		System.out.println(o.toString());
	}
*/
	//---------------------------------------------------------------------------------------------------------------
	public static void main(String[] args){

		System.out.println("hello!");
		/*
		try{
			System.out.println("hello!");
			//PrologApi prolog=new JLogPrologApi();
			//prolog.init();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		*/
		/*
		prolog.load("autor(denisa).");
		if(prolog.execute("autor(X)."))
			System.out.println("true");
		else
			System.out.println("false");
		
		prolog.load(new File("kb.pro"));
		if(prolog.execute("autor(dragos)."))
			System.out.println("true");
		else
			System.out.println("false");
		
		
		try{
			prolog.load(new File("kb.pro"));
			/*
			ActionResult ar=prolog.query("exemplu(X,Y).");
			System.out.println(prolog.toString(ar));
			
			ar=prolog.query("autor(X,Y).",new String[]{"X"});
			System.out.println(prolog.toString(ar));
			
			prolog.add("autor(maria)");
			ar=prolog.query("autor(X).");
			System.out.println(prolog.toString(ar));
			
			ar=prolog.retract( "autor(X)", new String[]{"X"});
			System.out.println(prolog.toString(ar));
			
			prolog.add("autor1(maria,'paper1')");
			prolog.add("autor1(ines,'paper2')");
			
			
			
			((JLogPrologApi)prolog).function();
			
			//ActionResult ar=prolog.query( "autor(X).");
			//System.out.println(prolog.toString(ar));
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	*/
}

