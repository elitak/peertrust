/**
 * Copyright 2004
 * 
 * This file is part of Peertrust.
 * 
 * Foobar is free software; you can redistribute it and/or modify
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

package test.org.peertrust.meta;

import org.peertrust.inference.MinervaProlog;

import com.ifcomputer.minerva.MinervaTerm;

import junit.framework.*;

/**
 * $Id: MinervaPrologTest.java,v 1.1 2004/07/01 23:38:29 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:38:29 $
 * @description
 */
public class MinervaPrologTest extends TestCase {
	public MinervaProlog engine = new MinervaProlog(null) ;
	
	public MinervaPrologTest ( String name ) {
		super( name ) ;
	}

	public static Test suite() {
		return new TestSuite( MinervaPrologTest.class );
	}

	public void setUp() {
	}

	public void testParse1() {
	
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
	
	public void testAssert1 () {
		String query = "asserta(predicate(test))" ;
		
		boolean result = engine.execute(query) ;
		assertEquals (true, result) ;

		String query2 = "predicate(test)" ;
		
		boolean result2 = engine.execute(query2) ;
		assertEquals (true, result2) ;
	}
	
	public static void main( String[] args ) {
		try {
			junit.textui.TestRunner.run( suite() );
		} catch ( Throwable t ) {
			t.printStackTrace();
		}
	}
}
