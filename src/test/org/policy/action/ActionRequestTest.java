package test.org.policy.action;

import org.policy.action.ActionRequest;

import junit.framework.TestCase;

public class ActionRequestTest extends TestCase {
	private ActionRequest actionRequest = null;
	
	private static String[] ARGUMENTS = {"name", "year", "adress"};
	private static String[] INPUTVARS = {"Name", "Year", "Adress", "Country"};
	private static String FUNC_1 = "query";
	private static String FUNC_2 = "search";

	public void setUp(){
		actionRequest = new ActionRequest(FUNC_1, null, null);
	}
	
	public void testGetNumberArguments(){
		assertEquals(0, actionRequest.getNumberArguments());
		
		ActionRequest actionRequest1 = new ActionRequest(FUNC_1, ARGUMENTS, null);
		assertEquals(3 , actionRequest1.getNumberArguments());
	}
	
	public void testGetArgument(){
		assertNull(actionRequest.getArgument(0));
		
		ActionRequest actionRequest1 = new ActionRequest(FUNC_1, ARGUMENTS, null);
		assertNull(actionRequest1.getArgument(-1));
		
		assertEquals( ARGUMENTS[0], actionRequest1.getArgument(0));
		assertEquals( ARGUMENTS[1], actionRequest1.getArgument(1));
		assertEquals( ARGUMENTS[2], actionRequest1.getArgument(2));
		
		assertNull(actionRequest1.getArgument(4));
	}
	
	public void testGetNumberInputVars(){
		assertEquals(0, actionRequest.getNumberInputVars());
		
		ActionRequest actionRequest1 = new ActionRequest(FUNC_1, null, INPUTVARS);
		assertEquals(4 , actionRequest1.getNumberInputVars());
	}
	
	public void testGetInputVar(){
		assertNull(actionRequest.getInputVar(0));
		
		ActionRequest actionRequest1 = new ActionRequest(FUNC_1, null, INPUTVARS);
		assertNull(actionRequest1.getInputVar(-1));
		
		assertEquals( INPUTVARS[0], actionRequest1.getInputVar(0));
		assertEquals( INPUTVARS[1], actionRequest1.getInputVar(1));
		assertEquals( INPUTVARS[2], actionRequest1.getInputVar(2));
		assertEquals( INPUTVARS[3], actionRequest1.getInputVar(3));		
		
		assertNull(actionRequest1.getInputVar(4));
	}
	
	public void testGetAndSetFunction(){
		assertEquals( FUNC_1, actionRequest.getFunction());
		
		ActionRequest actionRequest1 = new ActionRequest(null, null, null);
		assertEquals( "", actionRequest1.getFunction());
		
		actionRequest1.setFunction(FUNC_2);
		assertEquals( FUNC_2, actionRequest1.getFunction());
	}
	
}
