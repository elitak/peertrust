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

package test.org.peertrust.inference.prolog.yprolog;


import org.apache.log4j.Logger;
import org.peertrust.exception.InferenceEngineException;
import org.peertrust.inference.prolog.parser.PrologTools;
import org.peertrust.inference.prolog.parser.PrologTerm;
import org.peertrust.inference.prolog.yprolog.ParseException;
import org.peertrust.inference.prolog.yprolog.TokenMgrError;
import org.peertrust.inference.prolog.yprolog.YPrologEngine;

import junit.framework.*;

/**
 * <p>
 * 
 * </p><p>
 * $Id: YPrologEngineTest.java,v 1.1 2006/01/25 16:07:46 dolmedilla Exp $
 * <br/>
 * Date: 21-Jan-2006
 * <br/>
 * Last changed: $Date: 2006/01/25 16:07:46 $
 * by $Author: dolmedilla $
 * </p>
 * @author Daniel Olmedilla
 */
public class YPrologEngineTest extends TestCase {
	private static Logger log = Logger.getLogger(YPrologEngineTest.class);
	
	private final String TEST_FILE = "YPrologEngineTest.P" ;
	
	private YPrologEngine engine ;
	
	public YPrologEngineTest ( String name ) {
		super( name ) ;
	}

	public static Test suite() {
		return new TestSuite( YPrologEngineTest.class );
	}

	public void setUp() {
		engine = new YPrologEngine() ;
		try {
			engine.consultFile(TEST_FILE) ;
		} catch (InferenceEngineException e) {
			log.error(e.getMessage()) ;
		}
	}

	public void testQuery1() throws InferenceEngineException
	{
		String s = "append(X,Y,[a,b,c(U)])" ;
		
		boolean res = engine.execute(s) ;
		
		assertTrue(res) ;
	}

	public static void main( String[] args ) {
		try {
			junit.textui.TestRunner.run( suite() );
		} catch ( Throwable t ) {
			t.printStackTrace();
		}
	}
}
