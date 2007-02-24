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
package org.policy.config;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.mem.ModelMem;
import com.hp.hpl.jena.rdf.model.RDFException;

/**
 * <p>
 * Specifies the vocabulary accepted by the configurator in the configuration file.
 * </p><p>
 * $Id: Vocabulary.java,v 1.3 2007/02/24 19:20:54 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2007/02/24 19:20:54 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class Vocabulary {

    public static final String uri = "http://www.L3S.de/~olmedilla/policy/Vocabulary#";


    public static String getURI()
    {
        return(Vocabulary.uri);
    }

    public static Property javaClass;

    /**
     * Peer
    */
    public static Resource PolicyEngine;

    
    ///////////////////////////////////////////////////////////////////////////
    
    
  static {
    try {
        Model m = new ModelMem();

      javaClass =
      	  m.createProperty(Vocabulary.uri + "javaClass") ;

      PolicyEngine =
        m.createResource(Vocabulary.uri + "PolicyEngine");
      
    } catch (RDFException rdfe) {
      throw(new RuntimeException(rdfe.getMessage()));
    }
  }

//	private static final String METAI_PREFIX = "metaI." ; 
//	public static final String PEERNAME = METAI_PREFIX + "peerName" ;
//	public static final String BASE_FOLDER_TAG = METAI_PREFIX + "baseFolder" ; 
//	public static final String SERVER_PORT_TAG = METAI_PREFIX + "serverPort" ;
//	public static final String LOCAL_ADDRESS_TAG = METAI_PREFIX + "address" ;
//	public static final String KEYSTORE_FILE_TAG = METAI_PREFIX + "keystoreFile" ;
//	public static final String KEY_PASSWORD_TAG = METAI_PREFIX + "keyPassword" ;
//	public static final String STORE_PASSWORD_TAG = METAI_PREFIX + "storePassword" ;
}
