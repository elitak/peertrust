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
package test.org.peertrust.config;

import java.util.Vector;

import org.peertrust.config.PTConfigurator;
import org.peertrust.config.Vocabulary;
import org.peertrust.exception.ConfigurationException;

import junit.framework.*;

/**
 * $Id: PeertrustConfiguratorTest.java,v 1.2 2004/11/20 19:47:53 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/11/20 19:47:53 $
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
	
		PTConfigurator config = new PTConfigurator() ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE } ;
		try {
			config.startApp(args) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
	}

	public void testInitConfiguration() throws ConfigurationException {
		
		PTConfigurator config = new PTConfigurator() ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE } ;
		try {
			config.startApp(args) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
		
		TestClass ec = (TestClass) config.getComponent("Example1") ;
		assertEquals (ec.getString(), "testing") ;
		assertEquals (ec.isBool(), true) ;
		assertEquals (ec.getInteger(), 33) ;
		assertEquals (ec.getLonginteger(), 33333) ;
		Vector v1 = ec.getVector() ;
		assertTrue (v1.contains("Element 1")) ;
		assertTrue (v1.contains("Element 2")) ;
		
		TestClass ec2 = ec.getExample2() ;
		assertEquals (ec2.getString(), "testing2") ;
		assertEquals (ec2.isBool(), false) ;
		assertEquals (ec2.getInteger(), -99) ;
		assertEquals (ec2.getLonginteger(), -99999) ;
		Vector v2 = ec2.getVector() ;;
		assertTrue (v2.contains("Element 3")) ;
		assertTrue (v2.contains("Element 4")) ;
		
	}

	public void testLoopConfiguration() throws ConfigurationException {
		
		PTConfigurator config = new PTConfigurator() ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE2 } ;
		try {
			config.startApp(args) ;
		} catch (ConfigurationException e) {
			return ;
		}
		fail ("Test should return an exception") ;		
	}

	public void testInitSharedObject() throws ConfigurationException {
		
		PTConfigurator config = new PTConfigurator() ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE3 } ;
		try {
			config.startApp(args) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
		
		TestClass ec = (TestClass) config.getComponent("Example1") ;
		assertEquals (ec.getString(), "testing") ;
		assertEquals (ec.isBool(), true) ;
		assertEquals (ec.getInteger(), 33) ;
		assertEquals (ec.getLonginteger(), 33333) ;
		Vector v1 = ec.getVector() ;
		assertTrue (v1.contains("Element 1")) ;
		assertTrue (v1.contains("Element 2")) ;
		
		TestClass ec2 = ec.getExample2() ;
		assertEquals (ec2.getString(), "testing2") ;
		assertEquals (ec2.isBool(), false) ;
		assertEquals (ec2.getInteger(), -99) ;
		assertEquals (ec2.getLonginteger(), -99999) ;
		Vector v2 = ec2.getVector() ;;
		assertTrue (v2.contains("Element 3")) ;
		assertTrue (v2.contains("Element 4")) ;
		
		TestClass ec3 = (TestClass) ec.getExtraExample() ;
		assertEquals (ec3.getString(), "testing3") ;
		assertEquals (ec3.isBool(), true) ;
		assertEquals (ec3.getInteger(), -66) ;
		assertEquals (ec3.getLonginteger(), -99) ;
		Vector v3 = ec3.getVector() ;
		assertTrue (v3.contains("Element 100")) ;
		assertTrue (v3.contains("Element 111")) ;
		
		TestClass ec4 = ec3.getExample2() ;
		assertSame (ec2, ec4) ;
	}
	
	public void testConfiguratorParentClass() throws ConfigurationException {
		
		PTConfigurator config = new PTConfigurator(this) ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE4 } ;
		try {
			config.startApp(args) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
		
		TestClass ec = (TestClass) config.getComponent("Example1") ;
		assertEquals (ec.getString(), "testing") ;
		assertEquals (ec.isBool(), true) ;
		assertEquals (ec.getInteger(), 33) ;
		assertEquals (ec.getLonginteger(), 33333) ;
		Vector v1 = ec.getVector() ;
		assertTrue (v1.contains("Element 1")) ;
		assertTrue (v1.contains("Element 2")) ;
		
		Object object = ec.getExtraExample() ;
		
		assertEquals(object, this) ;
	}
	
	public void testRealConfig() throws ConfigurationException {
		
		PTConfigurator config = new PTConfigurator(this) ;
		
		String [] args = { Vocabulary4Tests.REAL_CONFIG_FILE } ;
		try {
			config.startApp(args) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
		
		Object object = config.getComponent(Vocabulary.PeertrustEngine) ;
		assertNotNull(object) ;
		
		object = config.getComponent(Vocabulary.EventDispatcher) ;
		assertNotNull(object) ;		

		object = config.getComponent(Vocabulary.MetaInterpreter) ;
		assertNotNull(object) ;		

		object = config.getComponent(Vocabulary.MetaInterpreterListener) ;
		assertNotNull(object) ;		

		object = config.getComponent(Vocabulary.InferenceEngine) ;
		assertNotNull(object) ;		

		object = config.getComponent(Vocabulary.Queue) ;
		assertNotNull(object) ;		

		object = config.getComponent(Vocabulary.CredentialStore) ;
		assertNotNull(object) ;		

		object = config.getComponent(Vocabulary.CommunicationChannelFactory) ;
		assertNotNull(object) ;		
	}
	
	// TO-DO: errors
	// class without init method
	// class does not fit target object
	
	public static void main( String[] args ) {
		try {
			junit.textui.TestRunner.run( suite() );
		} catch ( Throwable t ) {
			t.printStackTrace();
		}
	}
}
