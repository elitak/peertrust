package test.org.policy.filtering;

import org.policy.filtering.*;
import org.policy.action.*;
import org.peertrust.exception.*;
import java.io.*;

public class Juri {

	private PrologApi prolog;
	private final String FILTERING_FILE = "./config/prolog/jlog/filtering.pro" ;
	private final String POLICY_FILE = "./config/policy_code_samples/OnlineLibrary/LibraryPolicy.prolog" ;
	
	//starts the engine
	public Juri() throws ConfigurationException{
		prolog = new JLogPrologApi() ;
		prolog.init() ;
	}
	
	
	//reads information from the session SId
	//SId= the session number
	ActionResult readState(int SId) throws Exception{
		
		ActionResult ar=prolog.query("session("+SId+",state(X)).");
		return ar;
	}
	
	//filtering
	String filter(int SId,String policy, String[] credentials, String request)throws Exception{
		
		//loads the policy
		prolog.load(policy);
		prolog.load(new File(FILTERING_FILE));
	
		//starts the specified session
		String s = "start_session("+SId+","+processRule(request)+")." ;
		boolean result = prolog.execute(s) ;
		if(result==false){
			throw new Exception("Could not start session");
		}

		//add the given credentials to the session
		for(int i=0;i<credentials.length;i++){
			
			s = "add_to_session("+SId+",received("+processRule(credentials[i])+"))." ;
			result = prolog.execute(s);
			if(result==false){
				throw new Exception("Could not assert credentials to the session");
			}
		}
		
		//step one of the filtering
		ActionResult ar = prolog.queryOnce("filter_policy1( "+SId+", ActionList).");
		//this is the list of action results which contains elements like
		//immediate_action(Literal,Action) for provisional predicates
		// immediate_action(Function1(Arg1,..,Argn), Package, Function2, [Args]) for the in predicate
		String list=ar.getVariableBindings().getResult(0).getBinding(0);

		while(list.matches("\\[\\]")==false){
			
			//execute actions in the list
			String ActionResults="[]";
			
			//step two of the filtering
			//ActionResults is a list with elements in the form
			//successfull(Result) if the action result was successful
			//unsuccessfull(Result) otherwise
			ar = prolog.queryOnce("filter_policy2("+SId+", "+ActionResults+", ActionList).");
			list=ar.getVariableBindings().getResult(0).getBinding(0);
		}
		
		//third step of the filtering
		result= prolog.execute("filter_policy3("+SId+").");
		if(result==false){
			throw new Exception("Could not finish the filtering");
		}
		
		//extract the filtered policy
		StringBuffer sb=new StringBuffer();
		
		ar=prolog.query("session("+SId+",X),X=rule(_,_,_).");
		for(int i=0;i<ar.getNumberResults();i++){
			
			sb.append(ar.getResult(i).getBinding(0)+"\n");
		}
		
		ar=prolog.query("session("+SId+",X),X=metarule(_,_,_).");
		for(int i=0;i<ar.getNumberResults();i++){
			
			sb.append(ar.getResult(i).getBinding(0)+"\n");
		}
		
		//delete the session
		prolog.retractall("session("+SId+",_).");
		
		return sb.toString();
	}
	
	private String processRule(String rule){
		
		for(int i=rule.length()-1;i>=0;i--){
			
			if(rule.charAt(i)=='.')
				return rule.substring(0,i);
			
			if(rule.charAt(i)!=' ' && rule.charAt(i)!='\t' )
				break;
		}
		
		return rule;
	}
	
	public static void main(String args[]){
		
		try{
			
		Juri iuri=new Juri();
		
		BufferedReader br=new BufferedReader(new FileReader(iuri.POLICY_FILE));
		
		StringBuffer sb=new StringBuffer();
		while(true){
			
			String line=br.readLine();
			if(line==null)
				break;
			
			sb.append(line+"\n");
		}
		
		br.close();
		
		
		String filteredpolicy=iuri.filter(0,sb.toString(),new String[0],"allow(access(books)).");
		System.out.println(filteredpolicy);
		
		ActionResult ar=iuri.readState(0);
		System.out.println(ar.toString());
		
		
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
