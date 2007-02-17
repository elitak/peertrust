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
package org.policy.crypt.credentials.gui;

import org.peertrust.security.credentials.*;

/**
 * <p>
 * Dummy for the Editor-GUI.
 * </p><p>
 * $Id: DummyCredential.java,v 1.1 2007/02/17 16:59:29 dolmedilla Exp $
 * <br/>
 * Date: 14-Jan-2004
 * <br/>
 * Last changed: $Date: 2007/02/17 16:59:29 $
 * by $Author: dolmedilla $
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
