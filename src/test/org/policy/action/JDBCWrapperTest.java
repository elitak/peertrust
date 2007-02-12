package test.org.policy.action;

import java.util.List;

import junit.framework.TestCase;

import org.policy.action.ActionResult;
import org.policy.action.FunctionFailureException;
import org.policy.action.NoSuchFunctionException;
import org.policy.action.Result;
import org.policy.action.ResultSet;
import org.policy.action.packages.JDBCWrapper;

public class JDBCWrapperTest extends TestCase {
	private JDBCWrapper jdbcWapper = null;
	private String functionName;
	private String[] arguments;
	private String[] inputsVar;
	
	protected void setUp() throws Exception{
		super.setUp();
		jdbcWapper = new JDBCWrapper();
		functionName = "query";
		arguments = new String[2];
		arguments[0] = "select userName, Adress from integration";
		arguments[1] = "mysql";
		inputsVar = new String[2];
		inputsVar[0] = "Name";
		inputsVar[1]= "Adresse";
    }

    protected void tearDown() throws Exception {
    	jdbcWapper = null;
    	functionName = null;
		arguments = null;
		inputsVar = null;
    }
    
	public void testExecuteAction(){
		
		try {
			ActionResult actionResult = jdbcWapper.executeAction(functionName, arguments, inputsVar);
			ResultSet resultSet = actionResult.getVariableBindings();  
			List<Result> resultLst = resultSet.getBindings();
			assertEquals(3, actionResult.getNumberResults());
			assertEquals("Christian Lee", resultLst.get(0).getBinding(0));
			assertEquals("Daniel Olmedilla", resultLst.get(1).getBinding(0));
			assertEquals("Piero Bonatti", resultLst.get(2).getBinding(0));
			
			assertEquals("", resultLst.get(0).getBinding(1));
			assertEquals("Gluenderstrasse, Hannover", resultLst.get(1).getBinding(1));
			assertEquals("Via Mateo, Naples", resultLst.get(2).getBinding(1));
		} catch (FunctionFailureException e) {
			e.printStackTrace();
		} catch (NoSuchFunctionException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		}
	}
	
	public void testNoSuchFunctionException(){
		functionName = "create";
		
		try {
			jdbcWapper.executeAction(functionName, arguments, inputsVar);
			fail("NoSuchFunctionException erwartet");
		} catch (FunctionFailureException e) {
			
		} catch (NoSuchFunctionException e) {
			
		} catch (IllegalArgumentException e) {
			
		}
	}
	
	public void testIllegalArgumentException(){
		String[] arguments = {"select userName from integration"};
				
		try {
			jdbcWapper.executeAction(functionName, arguments, inputsVar);
			fail("IllegalArgumentException erwartet");
		} catch (FunctionFailureException e) {
			
		} catch (NoSuchFunctionException e) {
			
		} catch (IllegalArgumentException e) {
			
		}
	}
	
	public void testArgumentNumber(){
		String[] inputsVar = {"Name"};
		
		try {
			ActionResult actionResult = jdbcWapper.executeAction(functionName, arguments, inputsVar);
			ResultSet resultSet = actionResult.getVariableBindings();  
			List<Result> resultLst = resultSet.getBindings();
			assertEquals(3, actionResult.getNumberResults());
			assertEquals("Christian Lee", resultLst.get(0).getBinding(0));
			assertEquals("Daniel Olmedilla", resultLst.get(1).getBinding(0));
			assertEquals("Piero Bonatti", resultLst.get(2).getBinding(0));
			
			assertEquals("the number of variable is", 1, actionResult.getNumberVariables());
			assertEquals("Name", actionResult.getVariable(0));
			
		} catch (FunctionFailureException e) {
		} catch (NoSuchFunctionException e) {
		} catch (IllegalArgumentException e) {
		}
	}
	
	public void testFunctionFailure(){
		String[] arguments = {"select userName from integration","mysql"};
			
		try {
			jdbcWapper.executeAction(functionName, arguments, inputsVar);
			fail("functionFailureException erwartet");
		} catch (FunctionFailureException e) {
		} catch (NoSuchFunctionException e) {
		} catch (IllegalArgumentException e) {
		}
	}
}
