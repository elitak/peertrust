/**
 * Copyright 2004
 * 
 * This file is part of Peertrust.
 * 
 * Peertrust is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Peertrust is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Peertrust; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  
USA
*/

package test.org.policy.action;

import junit.framework.TestCase;

import org.policy.action.parser.Parse;

public class ParserTest extends TestCase {
	private static String TESTSTR = " in (user(Name, Address), rdbms: query('select name, address from test', 'project'))";
	private static String[] INPUTVARS = {"Name", "Address"};
	private static String PACKAGENAME = "rdbms";
	private static String FUNKTIONNAME = "query";
	private static String[] ARGUMENTS = {"select name, address from test", "project"};
	private static String[] VARIABLES = {"name", "address"};

	private Parse parser;
	
	protected void setUp(){
		parser = new Parse();
    }

    protected void tearDown() throws Exception {
    	parser = null;
    }
    
    public void testStatementName(){
    	parser.excuteParse(TESTSTR);
    	assertEquals("in", parser.getStatementName());
    }
    
    public void testInputsVars(){
    	parser.excuteParse(TESTSTR);
    	assertEquals(INPUTVARS[0], parser.getInVarsName()[0]);
    	assertEquals(INPUTVARS[1], parser.getInVarsName()[1]);
    }
    
    public void testPackageName(){
    	parser.excuteParse(TESTSTR);
    	assertEquals(PACKAGENAME, parser.getPackageName());
    }
    
    public void testFunctionName(){
    	parser.excuteParse(TESTSTR);
    	assertEquals(FUNKTIONNAME, parser.getFunctionName());
    }
    
    public void testArguments(){
    	parser.excuteParse(TESTSTR);
    	assertEquals(ARGUMENTS[0], parser.getArgument()[0]);
    	assertEquals(ARGUMENTS[1], parser.getArgument()[1]);
    }
    
    public void testSelectVars(){
    	parser.excuteParse(TESTSTR);
    	assertEquals(VARIABLES[0], parser.getSelectVars(ARGUMENTS[0])[0]);
    	assertEquals(VARIABLES[1], parser.getSelectVars(ARGUMENTS[0])[1]);
    }

}
