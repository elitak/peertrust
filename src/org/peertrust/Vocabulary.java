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
package org.peertrust;

/**
 * $Id: Vocabulary.java,v 1.1 2004/07/08 15:11:00 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/08 15:11:00 $
 * by $Author: dolmedilla $
 * @description
 */
public class Vocabulary {

	private static final String METAI_PREFIX = "metaI." ; 
	public static final String PEERNAME = METAI_PREFIX + "peerName" ;
	public static final String BASE_FOLDER_TAG = METAI_PREFIX + "baseFolder" ; 
	public static final String SERVER_PORT_TAG = METAI_PREFIX + "serverPort" ;
	public static final String LOCAL_ADDRESS_TAG = METAI_PREFIX + "address" ;
	public static final String KEYSTORE_FILE_TAG = METAI_PREFIX + "keystoreFile" ;
	public static final String KEY_PASSWORD_TAG = METAI_PREFIX + "keyPassword" ;
	public static final String STORE_PASSWORD_TAG = METAI_PREFIX + "storePassword" ;
}
