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
package g4mfs.impl.org.peertrust.security.credentials.gui;

import g4mfs.impl.org.peertrust.security.credentials.Credential;

import g4mfs.impl.org.peertrust.security.credentials.*;

/**
 * <p>
 * Dummy for the Editor-GUI.
 * </p><p>
 * $Id: DummyCredential.java,v 1.1 2005/11/30 10:35:18 ionut_con Exp $
 * <br/>
 * Date: 14-Jan-2004
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:18 $
 * by $Author: ionut_con $
 * </p>
 * @author Eric Knauss (mailto: oerich@gmx.net)
 */
public class DummyCredential extends Credential {

	public String getStringRepresentation() {
		return "-@-";
	}

	public String getSignerName() {
		return "-";
	}

	protected void initCredential( Object arg ) {
	}


	/* (non-Javadoc)
	 * @see credentials.Credential#getEncoded()
	 */
	public Object getEncoded() {
		
		return getSignerName();
	}
}	
