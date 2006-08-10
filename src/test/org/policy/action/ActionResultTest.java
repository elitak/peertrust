package test.org.policy.action;

import org.policy.action.ActionResult;

import junit.framework.TestCase;

public class ActionResultTest extends TestCase {
	private ActionResult actionResult = null;
	private static String[] VAR_LIST = {"name", "year", "adress" };
	
	public void setUp(){
		actionResult = new ActionResult(true);
	}
	
	public void testGetNumberVariables(){
		assertEquals("the number of variable is", 0, actionResult.getNumberVariables());
		
		String[] variableList = VAR_LIST;
		actionResult.setVariableList(variableList);
		assertEquals("the number of variable is", 3, actionResult.getNumberVariables());
	}
	
	public void testSetAndGetVariableList(){
		String[] variableList1 = null;
		actionResult.setVariableList(variableList1);
		assertNull(actionResult.getVariable(0));
	
		String[] variableList = VAR_LIST;
		actionResult.setVariableList(variableList);
		assertNull(actionResult.getVariable(-1));
		
		assertEquals(variableList[0], actionResult.getVariable(0));
		assertEquals(variableList[1], actionResult.getVariable(1));
		assertEquals(variableList[2], actionResult.getVariable(2));
		
		assertNull(actionResult.getVariable(3));
	}
	
	public void testGetNumberResults(){
		assertEquals(0, actionResult.getNumberResults());
	}
	
	public void testGetResult(){
		assertNull(actionResult.getResult(0));
	}
	public void testGetExecutionResult(){
		assertTrue(actionResult.getExecutionResult());
		
		ActionResult actionResult1 = new ActionResult(false);
		assertFalse(actionResult1.getExecutionResult());
	}
}
