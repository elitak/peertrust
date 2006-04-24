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
import org.peertrust.exception.ConfigurationException;
import org.peertrust.exception.InferenceEngineException;
import org.peertrust.inference.InferenceEngine;
import org.peertrust.inference.prolog.yprolog.YPrologEngine;

import junit.framework.*;

/**
 * <p>
 * 
 * </p><p>
 * $Id: YPrologEngineTest.java,v 1.3 2006/04/24 12:01:28 dolmedilla Exp $
 * <br/>
 * Date: 21-Jan-2006
 * <br/>
 * Last changed: $Date: 2006/04/24 12:01:28 $
 * by $Author: dolmedilla $
 * </p>
 * @author Daniel Olmedilla
 */
public class YPrologEngineTest extends TestCase {
	private static Logger log = Logger.getLogger(YPrologEngineTest.class);
	
	private InferenceEngine engine ;
	//final String baseUrl = AllTests.baseUrl + "test/org/peertrust/inference/prolog/yprolog/";
	final String baseUrl = "./config/prolog/yprolog/";
	
	private final String BASICS_FILE = baseUrl + "basics.P" ;
	private final String INTERPRETER_FILE = baseUrl + "interpreter.P" ;
	private final String TOOLS_FILE = baseUrl + "tools.P" ;
	private final String TEST_FILE = baseUrl + "test.P" ;
	
	public YPrologEngineTest ( String name ) {
		super( name ) ;
	}

	public static Test suite() {
		return new TestSuite( YPrologEngineTest.class );
	}

	public void setUp() throws ConfigurationException {
		initPeerTrust() ;
	}

	public void initPeerTrust() throws ConfigurationException
	{
		engine = new YPrologEngine() ;
		engine.init() ;
		
		try {
			engine.consultFile(BASICS_FILE) ;
			engine.consultFile(INTERPRETER_FILE) ;
			engine.consultFile(TOOLS_FILE) ;
			engine.consultFile(TEST_FILE) ;
		} catch (InferenceEngineException e) {
			log.error(e.getMessage()) ;
		}
	}

	public void testQuery1() throws InferenceEngineException, ConfigurationException
	{
		String s = "append(X,Y,[a,b,c(U)])" ;
		
		boolean res = engine.execute(s) ;
		
		assertTrue(res) ;
	}
	
	public void testQuery2() throws InferenceEngineException, ConfigurationException
	{
		String s = "append([a],[c(U)],[a,b,c(U)])" ;
		
		boolean res = engine.execute(s) ;
		
		assertFalse(res) ;
	}
	
	public void testConsult1() throws InferenceEngineException, ConfigurationException
	{
		String s = "p(a)" ;
		
		boolean res = engine.execute(s) ;
		assertFalse(res) ;
		
		engine.insert(s) ;
		
		res = engine.execute(s) ;
		assertTrue(res) ;
	}
	
	public void testPeerTrustQuery1() throws InferenceEngineException, ConfigurationException
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
