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
package org.peertrust.config;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.mem.ModelMem;
import com.hp.hpl.jena.rdf.model.RDFException;

/**
 * $Id: Vocabulary.java,v 1.1 2004/11/18 12:50:46 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/11/18 12:50:46 $
 * by $Author: dolmedilla $
 * @description
 */
public class Vocabulary {

    public static final String uri = "http://www.l3s.de/~olmedilla/peertrust/Vocabulary#";


    public static String getURI()
    {
        return(Vocabulary.uri);
    }

    public static Property javaClass;

    /**
     * Peer
    */
    public static Resource PeertrustEngine;

//    public static Property peerName ;
//
//    public static Property baseFolder ;
//    
//    public static Property entitiesFile ;
//    
//    public static Property hostName ;
//    
//    public static Property port ;
//    
//    public static Property keyStoreFile ;
//    
//    public static Property keyPassword ;
//    
//    public static Property storePassword ;
//    
    /**
     * Inference engine
    */
    public static Resource InferenceEngine;
//
//    public static Property prologFiles ;
//    
//    public static Property rdfFiles ;
//    
//    public static Property license ;
//
    /**
     * Queue
    */
    public static Resource Queue;

    /**
     * CommunicationChannel
    */
    public static Resource CommunicationChannelFactory;

    /**
     * CredentialStore
    */
    public static Resource CredentialStore;
    
    /**
     * MetaIntepreter
    */
    public static Resource MetaInterpreter;

    /**
     * MetaIntepreterListener
    */
    public static Resource MetaInterpreterListener ;
    
    /**
     * EventListener
    */
    public static Resource EventListener ;
    
    /**
     * EventDispatcher
    */
    public static Resource EventDispatcher ;

  static {
    try {
        Model m = new ModelMem();

      javaClass =
      	  m.createProperty(Vocabulary.uri + "javaClass") ;

      PeertrustEngine =
        m.createResource(Vocabulary.uri + "PeertrustEngine");

      InferenceEngine =
          m.createResource(Vocabulary.uri + "InferenceEngine");

      Queue =
        m.createResource(Vocabulary.uri + "Queue");

      CommunicationChannelFactory =
        m.createResource(Vocabulary.uri + "CommunicationChannelFactory");
      
      CredentialStore =
        m.createResource(Vocabulary.uri + "CredentialStore");

      MetaInterpreter =
        m.createResource(Vocabulary.uri + "MetaInterpreter");

      MetaInterpreterListener =
        m.createResource(Vocabulary.uri + "MetaInterpreterListener");
      
      EventListener =
        m.createResource(Vocabulary.uri + "EventListener");
      
      EventDispatcher =
        m.createResource(Vocabulary.uri + "EventDispatcher");

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
