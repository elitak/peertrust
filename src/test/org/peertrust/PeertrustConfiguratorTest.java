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
package test.org.peertrust;

import org.peertrust.PeertrustConfigurator;
import org.peertrust.Vocabulary;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.inference.InferenceEngine;
import org.peertrust.strategy.Queue;

import junit.framework.*;

/**
 * $Id: PeertrustConfiguratorTest.java,v 1.1 2004/10/20 19:26:41 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/10/20 19:26:41 $
 * by $Author: dolmedilla $
 * @description
 */
public class PeertrustConfiguratorTest extends TestCase {

	public PeertrustConfiguratorTest ( String name ) {
		super( name ) ;
	}

	public static Test suite() {
		return new TestSuite( PeertrustConfiguratorTest.class );
	}

	public void setUp() {
	}

	public void testParse() {
	
		PeertrustConfigurator config = new PeertrustConfigurator() ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE } ;
		try {
			config.startApp(args) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
	}

	public void testCreateQueue() throws ConfigurationException {
		
		PeertrustConfigurator config = new PeertrustConfigurator() ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE } ;
		try {
			config.startApp(args) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
		Queue engine = (Queue) config.createComponent(Vocabulary.Queue, true) ;
	}
	
	public void testCreateEngine() throws ConfigurationException {
		
		PeertrustConfigurator config = new PeertrustConfigurator() ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE } ;
		try {
			config.startApp(args) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
		InferenceEngine engine = (InferenceEngine) config.createComponent(Vocabulary.InferenceEngine, true) ;
	}

	public static void main( String[] args ) {
		try {
			junit.textui.TestRunner.run( suite() );
		} catch ( Throwable t ) {
			t.printStackTrace();
		}
	}
}
