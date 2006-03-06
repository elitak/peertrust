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

import junit.framework.TestSuite;

/**
 * <p>
 * 
 * </p><p>
 * $Id: AllTests.java,v 1.1 2006/03/06 12:47:56 dolmedilla Exp $
 * <br/>
 * Date: 19-Jan-2006
 * <br/>
 * Last changed: $Date: 2006/03/06 12:47:56 $
 * by $Author: dolmedilla $
 * </p>
 * @author Daniel Olmedilla
 */
public class AllTests extends TestSuite {
	// this has to be the path to the local source directory
	public static String baseUrl = "file:./src/";

	public AllTests(){
		super();
		init();
	}

	public AllTests(String name) {
		super(name);
		init();
	}

	public void init() {
		addTest(new test.org.peertrust.inference.prolog.yprolog.AllTests());
	}

	public static void main(String args[]) {
		junit.textui.TestRunner.run(new AllTests("All PeerTrust Tests"));
	}
}

