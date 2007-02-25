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
package test.org.policy.config;

import java.util.Vector;

import org.policy.config.ConfigurationException;
import org.policy.config.Configurator;
import org.policy.config.Vocabulary;

import junit.framework.*;

/**
 * $Id: ConfiguratorTest.java,v 1.2 2007/02/25 23:00:30 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2007/02/25 23:00:30 $
 * by $Author: dolmedilla $
 * @description
 */
public class ConfiguratorTest extends TestCase {

	private final String DEFAULT_COMPONENT = Vocabulary.getURI() + "Example1" ;
	private final String DEFAULT_RETRIEVED_COMPONENT = "Example1" ;
	
	public ConfiguratorTest ( String name ) {
		super( name ) ;
	}

	public static Test suite() {
		return new TestSuite( ConfiguratorTest.class );
	}

	public void setUp() {
		// System.out.println(System.getProperty("user.dir")) ;
	}

	public void testParse() {
	
		Configurator config = new Configurator() ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE1 } ;
		String defaultComponent = DEFAULT_COMPONENT ;
		String[] components = { defaultComponent } ;
		
		try {
			config.startApp(args, components) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
	}

	public void testInitConfiguration() throws ConfigurationException {
		
		Configurator config = new Configurator() ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE1 } ;
		String defaultComponent = DEFAULT_COMPONENT ;
		String[] components = { defaultComponent } ;
		try {
			config.startApp(args, components) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
		
		TestClass ec = (TestClass) config.getComponent(DEFAULT_RETRIEVED_COMPONENT) ;
		assertEquals (ec.getString(), "testing") ;
		assertEquals (ec.isBool(), true) ;
		assertEquals (ec.getInteger(), 34) ;
		assertEquals (ec.getLonginteger(), 33333) ;
		Vector v1 = ec.getVector() ;
		assertTrue (v1.contains("Element 1")) ;
		assertTrue (v1.contains("Element 2")) ;
		
		TestClass ec2 = ec.getExample2() ;
		assertEquals (ec2.getString(), "testing2") ;
		assertEquals (ec2.isBool(), false) ;
		assertEquals (ec2.getInteger(), -98) ;
		assertEquals (ec2.getLonginteger(), -99999) ;
		Vector v2 = ec2.getVector() ;;
		assertTrue (v2.contains("Element 3")) ;
		assertTrue (v2.contains("Element 4")) ;
		
	}

	public void testLoopConfiguration() throws ConfigurationException {
		
		Configurator config = new Configurator() ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE2 } ;
		String defaultComponent = DEFAULT_COMPONENT ;
		String[] components = { defaultComponent } ;
		try {
			config.startApp(args, components) ;
		} catch (ConfigurationException e) {
			return ;
		}
		fail ("Test should return an exception") ;		
	}

	public void testInitSharedObject() throws ConfigurationException {
		
		Configurator config = new Configurator() ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE3 } ;
		String defaultComponent = DEFAULT_COMPONENT ;
		String[] components = { defaultComponent } ;
		
		try {
			config.startApp(args, components) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
		
		TestClass ec = (TestClass) config.getComponent(DEFAULT_RETRIEVED_COMPONENT) ;
		assertEquals (ec.getString(), "testing") ;
		assertEquals (ec.isBool(), true) ;
		assertEquals (ec.getInteger(), 34) ;
		assertEquals (ec.getLonginteger(), 33333) ;
		Vector v1 = ec.getVector() ;
		assertTrue (v1.contains("Element 1")) ;
		assertTrue (v1.contains("Element 2")) ;
		
		TestClass ec2 = ec.getExample2() ;
		assertEquals (ec2.getString(), "testing2") ;
		assertEquals (ec2.isBool(), false) ;
		assertEquals (ec2.getInteger(), -98) ;
		assertEquals (ec2.getLonginteger(), -99999) ;
		Vector v2 = ec2.getVector() ;;
		assertTrue (v2.contains("Element 3")) ;
		assertTrue (v2.contains("Element 4")) ;
		
		TestClass ec3 = (TestClass) ec.getExtraExample() ;
		assertEquals (ec3.getString(), "testing3") ;
		assertEquals (ec3.isBool(), true) ;
		assertEquals (ec3.getInteger(), -65) ;
		assertEquals (ec3.getLonginteger(), -99) ;
		Vector v3 = ec3.getVector() ;
		assertTrue (v3.contains("Element 100")) ;
		assertTrue (v3.contains("Element 111")) ;
		
		TestClass ec4 = ec3.getExample2() ;
		assertSame (ec2, ec4) ;
	}
	
	public void testConfiguratorParentClass() throws ConfigurationException {
		
		Configurator config = new Configurator(this) ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE4 } ;
		String defaultComponent = DEFAULT_COMPONENT ;
		String[] components = { defaultComponent } ;
		
		try {
			config.startApp(args, components) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
		
		TestClass ec = (TestClass) config.getComponent(DEFAULT_RETRIEVED_COMPONENT) ;
		assertEquals (ec.getString(), "testing") ;
		assertEquals (ec.isBool(), true) ;
		assertEquals (ec.getInteger(), 34) ;
		assertEquals (ec.getLonginteger(), 33333) ;
		Vector v1 = ec.getVector() ;
		assertTrue (v1.contains("Element 1")) ;
		assertTrue (v1.contains("Element 2")) ;
		
		Object object = ec.getExtraExample() ;
		
		assertEquals(object, this) ;
	}
	
	public void testInitWithVectorContainingObjects() throws ConfigurationException {
		
		Configurator config = new Configurator() ;
		
		String [] args = { Vocabulary4Tests.CONFIG_FILE5 } ;
		//String defaultComponent = DEFAULT_COMPONENT ;
		String defaultComponent = Vocabulary.getURI() + "Example1" ;
		String[] components = { defaultComponent } ;
		try {
			config.startApp(args, components) ;
		} catch (ConfigurationException e) {
			fail(e.getMessage()) ;
		}
		
		TestClass ec = (TestClass) config.getComponent("Example1") ;
		Vector v1 = ec.getVector() ;
		TestClass2 tc1 = (TestClass2) v1.elementAt(0) ;
		TestClass2 tc2 = (TestClass2) v1.elementAt(1) ;
		
		assertEquals (tc1.getString1(), "Element 1") ;
		assertEquals (tc1.getString2(), "Element 2") ;

		assertEquals (tc2.getString1(), "Element 3") ;
		assertEquals (tc2.getString2(), "Element 4") ;	
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
