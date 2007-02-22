package test.org.policy.reasoning.prolog;

import junit.framework.*;
import org.apache.log4j.*;
import org.policy.reasoning.prolog.*;
import org.policy.action.standard.ActionResult;
import org.policy.action.standard.FunctionFailureException;
import org.policy.action.standard.packages.*;
import org.peertrust.exception.*;
import java.io.*;
import java.util.*;

public class PrologApiTest extends TestCase {

	private static Logger logger = Logger.getLogger(PrologApiTest.class);
	
	private PrologApi prolog;
	
	private final String var="[A-Z_]+[a-zA-Z0-9_]*";
	private final String sv="\\[[0-9]+([','][ ][0-9]+)*\\]";
	
	private final String FILTERING_FILE = "./config/prolog/jlog/filtering.pro" ;
	private final String POLICY_FILE = "./config/policy_code_samples/OnlineLibrary/LibraryPolicy.prolog" ;
	
	public PrologApiTest ( String name ) {
		super( name ) ;
	}

	public static Test suite() {
		return new TestSuite( PrologApiTest.class );
	}

	public void setUp() throws ConfigurationException {
		
		init() ;
	}

	public void init() throws ConfigurationException
	{
		prolog = new JLogProlog() ;
		prolog.init() ;
		
		logger.setLevel(Level.ALL);
	}
	
	//test the engine - unbound variables
	//if they are returned in the prolog format [a-zA-Z_0-9]+ (could be returned as null)
	//if unification is done on unbound vars
	public void testQuery1() throws PrologEngineException, FunctionFailureException{
		
		//test the variables
		prolog.load("predicate(X,X).predicate(X,Y).");
		ActionResult ar = prolog.query("predicate(A,B).");
		
		String a0=ar.getVariableBindings().getResult(0).getBinding(0);
		String b0=ar.getVariableBindings().getResult(0).getBinding(1);
		
		if( a0.matches(var)==false || b0.matches(var)==false ){
			logger.warn("Unbound variables are not returned in the Prolog form,e.g., [a-zA-Z_0-9]+.");
			return;
		}
		
		String a1=ar.getVariableBindings().getResult(1).getBinding(0);
		String b1=ar.getVariableBindings().getResult(1).getBinding(1);
		
		if( a1.matches(var)==false || b1.matches(var)==false ){
			logger.warn("Unbound variables are not returned in the Prolog form,e.g., [a-zA-Z_0-9]+.");
			return;
		}
		
		logger.warn("The prolog engine supports unbound variable in the form [a-zA-Z_0-9]+");
		
		if( a0.equals(b0)==false && a1.equals(b1)==false){
			logger.warn("The Prolog engine does not support unification for unbound variables");
			return;
		}
		
		logger.warn("The Prolog engine supports unification for unbound variables");	
		
	}
	
	//test the engine - strings
	//if they are returned as strings (may be returned as vector of bytes)
	public void testQuery2() throws PrologEngineException, FunctionFailureException{
		
		//test the variables
		prolog.load("predicate(\"string\").");
		ActionResult ar = prolog.queryOnce("predicate(A).");
		
		String a=ar.getVariableBindings().getResult(0).getBinding(0);
		
		if(a.matches("string")==false){
			
			logger.warn("The prolog engine does not deal with strings well.");
			
			if(a.matches(sv)==true)
				logger.warn("Strings are returned as vector of bytes.");
			
			return;
		}
		
		logger.debug("The Prolog engine deal with strings");	
		
	}
	
	//	test the engine - identifiers between apostrofies
	//if they are returned still between apostrofies ( 'Identifier' may be returned without apostrofies and then is a variable)
	public void testQuery3() throws PrologEngineException, FunctionFailureException{
		
		//test the variables
		prolog.load("predicate1(predicate2('Identifier')).");
		ActionResult ar = prolog.queryOnce("predicate1(predicate2(A)).");

		String a=ar.getVariableBindings().getResult(0).getBinding(0);
		
		if(a.matches("'"+var+"'")==false){
			logger.warn("The prolog engine does not deal with well with identifiers beetween apostrofies.");
			return;
		}
		
		
		logger.debug("The Prolog engine supports identifier beetween apostrofies");	

	}

	//tests the filtering
	public void testQuery4() throws PrologEngineException, FunctionFailureException, IOException{

		prolog.load(new File(FILTERING_FILE));
		prolog.load(new File(POLICY_FILE));

		logger.debug("The files have been consulted.");
		
		//start a new session
		String s = "start_session(0,allow(access(books)))." ;
		boolean result = prolog.execute(s) ;
		assertTrue(result) ;

		//add the given credentials
		s = "add_to_session(0,received(credential(sa,student_card)))." ;
		result = prolog.execute(s);
		assertTrue(result) ;
		
		s = "add_to_session(0,received(complex_term(student_card,type,student)))." ;
		result = prolog.execute(s);
		assertTrue(result) ;
		
		s = "add_to_session(0,received(complex_term(student_card,issuer,hu)))." ;
		result = prolog.execute(s);
		assertTrue(result) ;
		
		s = "add_to_session(0,received(complex_term(student_card,public_key,05272117)))." ;
		result = prolog.execute(s);
		assertTrue(result) ;


		//filtering
		
		//relevant_rules,partial_evaluation,relevant_rules,select_actions
		ActionResult ar = prolog.queryOnce("filter_policy1( 0, ActionList).");
		String list=ar.getVariableBindings().getResult(0).getBinding(0);
		
		//result=list.matches("\\[immediate_action\\(challenge\\(5272117\\)[',']"+sv+"\\)[','][ ]immediate_action\\(public_key\\(hu[',']"+var+"\\)[',']"+sv+"\\)\\]");
		result=list.contains("challenge(5272117)".subSequence(0,"challenge(5272117)".length()-1));
		assertTrue(result) ;
		
		result=list.contains("public_key(hu,".subSequence(0,"public_key(hu,".length()-1));
		assertTrue(result) ;


		//process_action_results,partial_evaluation,select_actions
		String ActionResults="[successfull(challenge(5272117)), successfull(public_key(hu,1721275))]";
		ar = prolog.queryOnce("filter_policy2( 0, "+ActionResults+", ActionList).");
		
		list=ar.getVariableBindings().getResult(0).getBinding(0);
		//result=list.matches("\\[immediate_action\\(verify_signature\\(student_card,1721275\\)[',']"+sv+"\\)\\]");
		result=list.contains("verify_signature(student_card,1721275)".subSequence(0,"verify_signature(student_card,1721275)".length()-1));
		assertTrue(result) ;


		//	process_action_results,partial_evaluation,select_actions
		ActionResults="[successfull(verify_signature(student_card,1721275))]";
		ar = prolog.queryOnce("filter_policy2( 0, "+ActionResults+", ActionList).");
		list=ar.getVariableBindings().getResult(0).getBinding(0);
		result=list.matches("\\[\\]");
		assertTrue(result) ;
		
		//relevant_rules,blurring_rules,relevant_rules,select_peer_actions
		result= prolog.execute("filter_policy3(0).");
		assertTrue(result) ;
		
		ar=prolog.query("session(0,X),X=rule(_,_,_).");
		System.out.println(ar.toString());
		
		ar=prolog.query("session(0,X),X=metarule(_,_,_).");
		System.out.println(ar.toString());
	}

	public static void main( String[] args ){
		
		try {
			junit.textui.TestRunner.run( suite() );
		} catch ( Throwable t ) {
			t.printStackTrace();
		}
		
	}
}