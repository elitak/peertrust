package test.org.policy.action;

import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;

import org.policy.action.standard.ActionResult;
import org.policy.action.standard.FunctionFailureException;
import org.policy.action.standard.NoSuchFunctionException;
import org.policy.action.standard.Result;
import org.policy.action.standard.ResultSet;
import org.policy.action.standard.packages.LDAPWrapper;

public class LDAPWrapperTest extends TestCase {
	private LDAPWrapper ldapWapper = null;
	private String functionName;
	private String[] arguments;
	private String[] inputsVar;
	protected void setUp() throws Exception {
		super.setUp();
		ldapWapper = new LDAPWrapper();
		functionName = "search";
		arguments = new String[2];
	//	arguments[0] = "select userName, Adress from integration";
	//	arguments[1] = "mysql";
	//	inputsVar = new String[2];
	//	inputsVar[0] = "Name";
	//	inputsVar[1]= "Adresse";
		
	}

	protected void tearDown() throws Exception {
		super.tearDown();
		ldapWapper = null;
    	functionName = null;
		arguments = null;
		inputsVar = null;
	}
public void testExecuteAction(){
		arguments[0] = "cn,objectclass,sn";
		arguments[1] = "cn=*";
		inputsVar = new String[2];
		inputsVar[0] = "cn";
		inputsVar[1]= "objectclass";
		try {
			ActionResult actionResult = ldapWapper.executeAction(functionName, arguments, inputsVar);
			ResultSet resultSet = actionResult.getVariableBindings();  
			List<Result> resultLst = resultSet.getBindings();
			assertEquals(3, actionResult.getNumberResults());
			assertEquals("cn: Manager", resultLst.get(0).getBinding(0));
			assertEquals("cn: guest", resultLst.get(1).getBinding(0));
			assertEquals("cn: hairun", resultLst.get(2).getBinding(0));
			
			assertEquals("objectClass: organizationalRole", resultLst.get(0).getBinding(1));
			assertEquals("objectClass: organizationalRole", resultLst.get(1).getBinding(1));
			assertEquals("objectClass: person", resultLst.get(2).getBinding(1));
			
			assertEquals("", resultLst.get(0).getBinding(2));
			assertEquals("", resultLst.get(1).getBinding(2));
			assertEquals("sn: hairun", resultLst.get(2).getBinding(2));
			
		} catch (FunctionFailureException e) {
			e.printStackTrace();
		} catch (NoSuchFunctionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}

	public void testNoSuchFunctionException(){
		functionName = "xxx";
		
		try {
			ldapWapper.executeAction(functionName, arguments, inputsVar);
			fail("NoSuchFunctionException erwartet");
		} catch (FunctionFailureException e) {
		
		} catch (NoSuchFunctionException e) {
		
		} catch (IllegalArgumentException e) {
		
		}
	}
	public void testIllegalArgumentException(){
		String[] arguments = {"cn"};
		try {
			ldapWapper.executeAction(functionName, arguments, inputsVar);
			fail("IllegalArgumentException erwartet");
		} catch (FunctionFailureException e) {
				
		} catch (NoSuchFunctionException e) {
			
		} catch (IllegalArgumentException e) {
			
		}
	}
	
	public void testFunctionFailure(){
		String[] arguments = {"cn,objectclass","cn=*"};
		String[] inputsVar = {"cn","objectclass","sn"};	
		try {
			ldapWapper.executeAction(functionName, arguments, inputsVar);
			fail("functionFailureException erwartet");
		} catch (FunctionFailureException e) {
		} catch (NoSuchFunctionException e) {
		} catch (IllegalArgumentException e) {
		}
	}
	
	public void testArgumentNumber(){
		arguments[0] = "cn,objectclass,sn";
		arguments[1] = "cn=*";
		String[] inputsVar = {"cn","objectclass"};
		
		try {
			ActionResult actionResult = ldapWapper.executeAction(functionName, arguments, inputsVar);
			ResultSet resultSet = actionResult.getVariableBindings();  
			List<Result> resultLst = resultSet.getBindings();
			assertEquals(3, actionResult.getNumberResults());
			
			assertEquals("the number of variable is", 2, actionResult.getNumberVariables());
			assertEquals("cn", actionResult.getVariable(0));
			
		} catch (FunctionFailureException e) {
		} catch (NoSuchFunctionException e) {
		} catch (IllegalArgumentException e) {
		}
	}

}
