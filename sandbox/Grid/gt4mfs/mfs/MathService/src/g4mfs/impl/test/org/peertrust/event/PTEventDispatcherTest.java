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
package g4mfs.impl.test.org.peertrust.event;

import g4mfs.impl.org.peertrust.event.PTEvent;
import g4mfs.impl.org.peertrust.event.PTEventDispatcher;
import g4mfs.impl.org.peertrust.exception.ConfigurationException;


import junit.framework.*;

/**
 * $Id: PTEventDispatcherTest.java,v 1.1 2005/11/30 10:35:11 ionut_con Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2005/11/30 10:35:11 $
 * by $Author: ionut_con $
 * @description
 */
public class PTEventDispatcherTest extends TestCase {

	PTEventDispatcher ed ;
	
	public PTEventDispatcherTest ( String name ) {
		super( name ) ;
	}

	public static Test suite() {
		return new TestSuite( PTEventDispatcherTest.class );
	}

	public void setUp() throws ConfigurationException {
		ed = new PTEventDispatcher() ;
		ed.init() ;
	}

	public void testRegisteredClassForSpecificEvent() {
	
		TestClass t1 = new TestClass(ed) ;
		ed.register(t1, PTEventTestB.class) ;
		
		TestClass t2 = new TestClass(ed) ;
		ed.register(t2, PTEventTestB.class) ;
		
		TestClass t3 = new TestClass(ed) ;
		ed.register(t3, PTEventTestB.class) ;
		
		TestClass t4 = new TestClass(ed) ;
		
		PTEvent event = new PTEventTestB(t4) ;
		
		t4.generateEvent(event) ;
		
		PTEvent event1 = t1.getMessage() ;
		PTEvent event2 = t2.getMessage() ;
		PTEvent event3 = t3.getMessage() ;
		
		assertNotNull(event1) ;
		assertNotNull(event2) ;
		assertNotNull(event3) ;
		
		assertEquals(event, event1) ;
		assertEquals(event, event2) ;
		assertEquals(event, event3) ;
		
		assertSame(event, event1) ;
		assertSame(event, event2) ;
		assertSame(event, event3) ;
	}
	
	public void testRegisteredClassForAllEvents() {
		
		TestClass t1 = new TestClass(ed) ;
		ed.register(t1) ;
		
		TestClass t2 = new TestClass(ed) ;
		ed.register(t2) ;
		
		TestClass t3 = new TestClass(ed) ;
		ed.register(t3) ;
		
		TestClass t4 = new TestClass(ed) ;
		
		PTEvent event = new PTEventTestB(t4) ;
		
		t4.generateEvent(event) ;
		
		PTEvent event1 = t1.getMessage() ;
		PTEvent event2 = t2.getMessage() ;
		PTEvent event3 = t3.getMessage() ;
		
		assertNotNull(event1) ;
		assertNotNull(event2) ;
		assertNotNull(event3) ;
		
		assertEquals(event, event1) ;
		assertEquals(event, event2) ;
		assertEquals(event, event3) ;
		
		assertSame(event, event1) ;
		assertSame(event, event2) ;
		assertSame(event, event3) ;
	}
	
	public void testRegisteredClassForParentEvent() {
		
		TestClass t1 = new TestClass(ed) ;
		ed.register(t1) ;
		
		TestClass t2 = new TestClass(ed) ;
		ed.register(t2, PTEventTestB.class) ;
		
		TestClass t3 = new TestClass(ed) ;
		ed.register(t3, PTEventTestA.class) ;
		
		TestClass t4 = new TestClass(ed) ;
		ed.register(t4, PTEventTestASubClass.class) ;
		
		TestClass t5 = new TestClass(ed) ;
		
		PTEvent event = new PTEventTestASubClass(t5) ;
		
		t5.generateEvent(event) ;
		
		PTEvent event1 = t1.getMessage() ;
		PTEvent event2 = t2.getMessage() ;
		PTEvent event3 = t3.getMessage() ;
		PTEvent event4 = t4.getMessage() ;
		
		assertNotNull(event1) ;
		assertNull(event2) ;
		assertNotNull(event3) ;
		assertNotNull(event4) ;
		
		assertEquals(event, event1) ;
		assertEquals(event, event3) ;
		assertEquals(event, event4) ;
		
		assertSame(event, event1) ;
		assertNotSame(event, event2) ;
		assertSame(event, event3) ;
		assertSame(event, event4) ;
	}
	
	public void testRegisteredClassForSpecificAndAllEvents() {
		
		TestClass t1 = new TestClass(ed) ;
		ed.register(t1) ;
		
		TestClass t2 = new TestClass(ed) ;
		ed.register(t2, PTEventTestB.class) ;
		
		TestClass t3 = new TestClass(ed) ;
		ed.register(t3) ;
		
		TestClass t4 = new TestClass(ed) ;
		ed.register(t4, PTEventTestB.class) ;
		
		TestClass t5 = new TestClass(ed) ;
		
		PTEvent event = new PTEventTestB(t5) ;
		
		t5.generateEvent(event) ;
		
		PTEvent event1 = t1.getMessage() ;
		PTEvent event2 = t2.getMessage() ;
		PTEvent event3 = t3.getMessage() ;
		PTEvent event4 = t4.getMessage() ;
		
		assertNotNull(event1) ;
		assertNotNull(event2) ;
		assertNotNull(event3) ;
		assertNotNull(event4) ;
		
		assertEquals(event, event1) ;
		assertEquals(event, event2) ;
		assertEquals(event, event3) ;
		assertEquals(event, event4) ;
		
		assertSame(event, event1) ;
		assertSame(event, event2) ;
		assertSame(event, event3) ;
		assertSame(event, event4) ;
	}
	
	public void testDifferentEvents() {
		
		TestClass t1 = new TestClass(ed) ;
		ed.register(t1) ;
		
		TestClass t2 = new TestClass(ed) ;
		ed.register(t2, PTEventTestB.class) ;
		
		TestClass t3 = new TestClass(ed) ;
		ed.register(t3) ;
		
		TestClass t4 = new TestClass(ed) ;
		ed.register(t4, PTEventTestA.class) ;
		
		TestClass t5 = new TestClass(ed) ;
		
		PTEvent eventA = new PTEventTestA(t5) ;
		PTEvent eventB = new PTEventTestB(t5) ;
		
		t5.generateEvent(eventA) ;
		
		PTEvent event1 = t1.getMessage() ;
		PTEvent event2 = t2.getMessage() ;
		PTEvent event3 = t3.getMessage() ;
		PTEvent event4 = t4.getMessage() ;
		
		assertNotNull(event1) ;
		assertNull(event2) ;
		assertNotNull(event3) ;
		assertNotNull(event4) ;
		
		assertEquals(eventA, event1) ;
		assertEquals(eventA, event3) ;
		assertEquals(eventA, event4) ;
		
		assertSame(eventA, event1) ;
		assertSame(eventA, event3) ;
		assertSame(eventA, event4) ;
		
		t5.generateEvent(eventB) ;
		
		event1 = t1.getMessage() ;
		event2 = t2.getMessage() ;
		event3 = t3.getMessage() ;
		event4 = t4.getMessage() ;
		
		assertNotNull(event1) ;
		assertNotNull(event2) ;
		assertNotNull(event3) ;
		assertNotNull(event4) ;
		
		assertEquals(eventB, event1) ;
		assertEquals(eventB, event2) ;
		assertEquals(eventB, event3) ;
		
		assertSame(eventB, event1) ;
		assertSame(eventB, event2) ;
		assertSame(eventB, event3) ;
		assertNotSame(eventB, event4) ;
	}
	
	public static void main( String[] args ) {
		try {
			junit.textui.TestRunner.run( suite() );
		} catch ( Throwable t ) {
			t.printStackTrace();
		}
	}
}
