package test.org.policy.action;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import junit.framework.TestCase;

import org.policy.action.FunctionFailureException;
import org.policy.action.NoSuchFunctionException;
import org.policy.action.packages.LDAPWrapper;
import org.policy.action.packages.RegExpWrapper;

public class RegExpWrapperTest extends TestCase {
	RegExpWrapper rew;
	private String functionName;
	private String[] arguments;
	private String[] inputsVar;
	protected void setUp() throws Exception {
		rew = new RegExpWrapper();
		functionName = "regex";
		arguments = new String[2];
	}

	protected void tearDown() throws Exception {
		rew = null;
	}
	public void testRegExpInFile() throws FileNotFoundException{
		String regExp = "test";
		String filePath = "D:/Programme/eclipse/workspace/Integration/test/org/policy/action/regexTest.txt";
		Vector<List> result; 
		result = rew.regExpInFile(regExp, filePath);
		assertTrue(result.size() == 9);
		
		Iterator iterOfResult;
		iterOfResult = result.iterator();
		List valueList;
		String matcher;
		while(iterOfResult.hasNext()){
			valueList = (List)iterOfResult.next();
			assertEquals("test", (String)valueList.get(0));
		}
	}
	public void testNoSuchFunctionException(){
		functionName = "xxx";
		try {
			rew.executeAction(functionName, arguments, inputsVar);
			fail("NoSuchFunctionException erwartet");
		} catch (FunctionFailureException e) {
			
		} catch (NoSuchFunctionException e) {
			
		} catch (IllegalArgumentException e) {
			
		}
	}
	
	public void testIllegalArgumentException(){
		String[] arguments = {"t*.java"};
				
		try {
			rew.executeAction(functionName, arguments, inputsVar);
			fail("IllegalArgumentException erwartet");
		} catch (FunctionFailureException e) {
			
		} catch (NoSuchFunctionException e) {
			
		} catch (IllegalArgumentException e) {
			
		}
	}
	public void testFunctionFailure(){
		String regExp = "test";
		String filePath = "D:/Programme/eclipse/workspace/Integration/test/org/policy/action/regexTest.txt";
		String[] arguments = {regExp,filePath};
		String[] inputsVar = {"javaFileWithBeginT","other"};
		try {
			rew.executeAction(functionName, arguments, inputsVar);
			fail("functionFailureException erwartet");
		} catch (FunctionFailureException e) {
		} catch (NoSuchFunctionException e) {
		} catch (IllegalArgumentException e) {
		}
	}
	
}
