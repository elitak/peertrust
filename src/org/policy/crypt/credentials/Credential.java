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
package org.policy.crypt.credentials;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * <p>
 * This class aims to ease the validation, management and distribution of
 * Security Credentials for automated trust-negotiation.
 * </p><p>
 * $Id: Credential.java,v 1.1 2007/02/17 16:59:29 dolmedilla Exp $
 * <br/>
 * Date: 14-Jan-2004
 * <br/>
 * Last changed: $Date: 2007/02/17 16:59:29 $
 * by $Author: dolmedilla $
 * </p>
 * @author Eric Knauss (mailto: oerich@gmx.net)
 */
public abstract class Credential implements Serializable {

	public abstract String getStringRepresentation();

	public abstract String getSignerName();

	private static Logger log = Logger.getLogger(Credential.class);
	
	/**
	 * Returns the credential in its special format. This might be a Certificate 
	 * or a signed xml document or what ever.
	 * @return An encoded credential
	 */
	public abstract Object getEncoded();
}
