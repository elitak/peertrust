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

import java.util.Vector;

import org.peertrust.inference.MinervaProlog;
import org.peertrust.meta.Tree;

import junit.framework.*;

/**
 * $Id: TreeTest.java,v 1.1 2004/07/01 23:38:29 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:38:29 $
 * @description
 */
public class TreeTest extends TestCase {
	public MinervaProlog engine = new MinervaProlog(null) ;
	
	public TreeTest ( String name ) {
		super( name ) ;
	}

	public static Test suite() {
		return new TestSuite( TreeTest.class );
	}

	public void setUp() {
	}

	public void testParse1() {
	
		String proof = "[r(request(spanishCourse,V18725445)$alice,[policy1(request(spanishCourse),alice)],[get(spanishCourse,V18725445)])@elearn]" ;
	
		Tree tree = new Tree(1) ;
		
		Vector vector = tree.generateProofVector(proof) ;
		
		assertEquals (1, vector.size()) ;
	}
	
	public void testParse2() {
	
		String proof = "[r(policy1(request(spanishCourse),alice),[drivingLicense(alice)@caState@alice,policeOfficer(alice)@caStatePolice@alice],[])@elearn]" ;
	
		Tree tree = new Tree(1) ;
		
		Vector vector = tree.generateProofVector(proof) ;
		
		assertEquals (1, vector.size()) ;
	}
	
	public void testParse3() {
		
		String proof = "[]" ;
	
		Tree tree = new Tree(1) ;
		
		Vector vector = tree.generateProofVector(proof) ;
		
		assertEquals (0, vector.size()) ;
	}

	public static void main( String[] args ) {
		try {
			junit.textui.TestRunner.run( suite() );
		} catch ( Throwable t ) {
			t.printStackTrace();
		}
	}
}
