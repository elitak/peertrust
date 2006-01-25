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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package test.org.peertrust.inference.prolog.parser;


import org.apache.log4j.Logger;
import org.peertrust.inference.prolog.parser.PrologTools;
import org.peertrust.inference.prolog.parser.PrologTerm;
import org.peertrust.inference.prolog.yprolog.ParseException;
import org.peertrust.inference.prolog.yprolog.TokenMgrError;

import junit.framework.*;

/**
 * <p>
 * 
 * </p><p>
 * $Id: PrologToolsTest.java,v 1.1 2006/01/25 16:07:46 dolmedilla Exp $
 * <br/>
 * Date: 19-Jan-2006
 * <br/>
 * Last changed: $Date: 2006/01/25 16:07:46 $
 * by $Author: dolmedilla $
 * </p>
 * @author Daniel Olmedilla
 */
public class PrologToolsTest extends TestCase {
	private static Logger log = Logger.getLogger(PrologToolsTest.class);
	
	public PrologToolsTest ( String name ) {
		super( name ) ;
	}

	public static Test suite() {
		return new TestSuite( PrologToolsTest.class );
	}

	public void setUp() {
	}

	public void testParseError()
	{
		String s = "[tree(employee(alice7,microsoft)@'Microsoft'@alice7,[],[signed(r(employee(alice7,microsoft)@microsoft,[],[]),microsoft,signature(microsoft))@alice7,proved_by(alice7)@company7],manuel)]" ;
		
		try {
			PrologTools.getTerm(s);
		} catch (TokenMgrError me) {
			assertTrue(me.getMessage(),true) ;
			return ;
		} catch (ParseException e) {
			fail(e.getMessage()) ;
		}
		
		fail("No exception raised") ;		
	}

	public void testParse1()
	{
		String s = "[1,2,X0,a]" ;
		
		PrologTerm term = null;
		try {
			term = PrologTools.getTerm(s);
		} catch (ParseException e) {
			fail(e.getMessage());
		}
		
		assertFalse(term == null) ;
		assertEquals(s, term.toString()) ;
	}

	public void testParse2()
	{
		String s = "predicate(X0,a,23)" ;
		
		PrologTerm term = null;
		try {
			term = PrologTools.getTerm(s);
		} catch (ParseException e) {
			fail(e.getMessage());
		}
				
		assertFalse(term == null) ;
		assertEquals(s, term.toString()) ;
	}

	public void testParse3()
	{
		String s = "predicate([1,2,X0,a],X1,a,pred2(23))" ;
		
		PrologTerm term = null;
		try {
			term = PrologTools.getTerm(s);
		} catch (ParseException e) {
			fail(e.getMessage());
		}
		
		assertFalse(term == null) ;
		assertEquals(s, term.toString()) ;
	}

	public void testParse5()
	{
		String s = "[tree(employee(alice7,microsoft),[],[signed(r(employee(alice7,microsoft),[],[]),microsoft,signature(microsoft)),proved_by(alice7)],manuel)]" ;
		
		PrologTerm term = null;
		try {
			term = PrologTools.getTerm(s);
		} catch (ParseException e) {
			fail(e.getMessage());
		}
		
		System.out.println(s) ;
		System.out.println(term.toString()) ;

		assertFalse(term == null) ;
		assertEquals(s, term.toString()) ;
	}

	public void testParse6()
	{
		String s = "tree(document(project7,X0),[query(policy1(document(project7,X0,X1)),no),query(policy2(document(project7,X0,X1)),no),query(get_record(project7,X0),no)],[r(document(project7,X0),[policy1(document(project7,X0,X1)),policy2(document(project7,X0,X1))],[get_record(project7,X0)])],manuel)" ;
		
		PrologTerm term = null;
		try {
			term = PrologTools.getTerm(s);
		} catch (ParseException e) {
			fail(e.getMessage());
		}
		
		assertFalse(term == null) ;
		assertEquals(s, term.toString()) ;
	}

/*	public void testParse1() {
	
		String query = "[tree(employee(alice7,microsoft)@'Microsoft'@alice7,[],[signed(r(employee(alice7,microsoft)@microsoft,[],[]),microsoft,signature(microsoft))@alice7,proved_by(alice7)@company7],manuel)]" ;
	
		MinervaTerm term = engine.parse(query) ;
	
		String query2 = engine.unparse(term) ;
		assertEquals (query, query2) ;
	}
	
	// improve parsers with a check  for spaces and variable names 
	public void testParse2() {
	
		String query = "tree(document(project7,V12039161),[query(policy1(document(project7,V12039161,V8970080)),no),query(policy2(document(project7,V12039161,V8970080)),no),query(get_record(project7,V12039161),no)],[r(document(project7,V12039161),[policy1(document(project7,V12039161,V8970080)),policy2(document(project7,V12039161,V8970080))],[get_record(project7,V12039161)])@company7],manuel)" ;
	
		MinervaTerm term = engine.parse(query) ;
	
		String query2 = engine.unparse(term) ;
		assertEquals (query, query2) ;
	}

	public void testParse3() {
	
		String query = "tree(prueba @ x $ n, maria(X, j @ t) @ test) $ final" ;
	
		MinervaTerm term = engine.parse(query) ;
	
		String query2 = engine.unparse(term) ;
		assertEquals (query, query2) ;
	}

	public MinervaProlog initMinerva () throws ConfigurationException
	{
		PTConfigurator config = new PTConfigurator() ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE } ;
		String defaultComponent = Vocabulary.PeertrustEngine.toString() ;
		String[] components = { defaultComponent } ;
		
		try {
			config.startApp(args, components) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
		
		MinervaProlog min = (MinervaProlog) config.createComponent(Vocabulary.InferenceEngine) ;

		min.init() ;
		
		return min ;
	}
	
	public void testInitialization () throws ConfigurationException {
		
		MinervaProlog min = initMinerva() ;
		
		assertNotNull(min.getBaseFolder()) ;
		System.out.println ("BaseFolder: " + min.getBaseFolder()) ;
		assertNotNull(min.getPrologFiles()) ;
		System.out.println ("PrologFiles: " + min.getPrologFiles()) ;
		assertNotNull(min.getRdfFiles()) ;
		System.out.println ("RdfFiles: " + min.getRdfFiles()) ;
	}

	public void testAssert1 () throws ConfigurationException {

		MinervaProlog engine = initMinerva() ;
		
		String query = "asserta(predicate(test))" ;
		
		boolean result = engine.execute(query) ;
		assertEquals (true, result) ;

		String query2 = "predicate(test)" ;
		
		boolean result2 = engine.execute(query2) ;
		assertEquals (true, result2) ;
	}
*/
	public static void main( String[] args ) {
		try {
			junit.textui.TestRunner.run( suite() );
		} catch ( Throwable t ) {
			t.printStackTrace();
		}
	}
}
