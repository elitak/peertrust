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

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Iterator;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.jxta.edutella.util.RdfUtilities;

import org.apache.log4j.Logger;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.exception.InitializationException;

import com.hp.hpl.jena.mem.ModelMem;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFException;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Selector;
import com.hp.hpl.jena.rdf.model.SimpleSelector;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.RDF;

/**
 * $Id: PeertrustConfigurator.java,v 1.1 2004/10/20 19:26:38 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/10/20 19:26:38 $
 * by $Author: dolmedilla $
 * @description
 */
public class PeertrustConfigurator {
	
	private static final Logger log = Logger.getLogger(PeertrustConfigurator.class);

    // The Configuration File (stored as a Jena RDF Model)
    private Model _configuration ;

    private Vector _registry ;
    
    // Arguments from the command line
    private String[] _args ;
    
    // The root node of the peers configuration RDF graph
    private Resource _peer ;

    // A flag to increasy the noisyness
    private boolean _verbose = false ;

	/**
	 * 
	 */
	public PeertrustConfigurator() {
		super();
		log.debug("$Id: PeertrustConfigurator.java,v 1.1 2004/10/20 19:26:38 dolmedilla Exp $");
	}
	
	/**
     * 
     * @{inheritDoc}
     * 
     * @param newArgs
     * @return
     * @throws ConfigException
     * @see net.jxta.edutella.component.EdutellaComponent#startApp(java.lang.String[])
     */
    public void startApp(String[] newArgs) throws ConfigurationException {
        log.debug(".startApp()");
        
        _registry = new Vector() ;
        _args = newArgs;
        _configuration = loadConfiguration(_args);
        _peer = baseConfigure(_configuration);
    }
    
    private Model loadConfiguration(String [] args) throws ConfigurationException {
        log.debug(".loadConfiguration()");
                
        Model model ;
        try {
            model = loadRdf(args[0]);
        } catch (InitializationException ie) {
            throw new ConfigurationException ("Could not load rdf peer configuration " + args[0], ie);
        }

        return model ;
    }
    
    /**
     * 
     */
    private Model loadRdf(String url) throws InitializationException
    {
        log.debug(".loadConfiguration() (" + url + ")");
        Model model ;
        try {
            model = new ModelMem();
            model.read(url);
            
            log.info("Read " + model.size() + " "
                    + "statements from configuration file");
            
            if (isVerbose()) {
                StmtIterator i = model.listStatements();
                while(i.hasNext()) {
                    log.debug(i.next().toString());
                }
            }
        } catch (RDFException rdfe) {
            log.error("RDFException: ", rdfe);
            throw(new InitializationException(rdfe));
        }
        return model ;
    }
    
    private Resource baseConfigure(Model model) throws ConfigurationException {
        log.debug(".baseConfigure()");
        
        log.info("Retrieving root node of configuration file...");
        Resource myPeer = null;
        
        try {
            myPeer = RdfUtilities.findSubject(model, RDF.type, Vocabulary.PeertrustEngine);
            
            log.info("Root peer resource: " + myPeer.getURI());
            
        } catch (RDFException rdfe) {
            log.error("Can not find the Resouce describing this peer: ", rdfe);
            throw(new ConfigurationException(rdfe));
        }
        return myPeer ;
    }
    
    public Object createComponent (Resource identifier, boolean register) throws ConfigurationException
	{
    	Object object = createObject(_configuration, identifier) ;
    	if (register)
    		_registry.add(object) ;
    	return object ;
	}
    
    public static Object createObject(Model model, Resource identifier) throws ConfigurationException {
        
        log.debug(".createObject() [Resource:" + identifier.getLocalName() + "]");

        Object object ;
        
        try {
        	Resource resource = RdfUtilities.findSubject(model, RDF.type, identifier) ;
        	
            // RDF property pd:javaClass identifies java class for this object.
            Literal literal = RdfUtilities.findObjectLiteral(model,
                    resource, Vocabulary.javaClass);
            
            String objectClass = literal.getString();
            
            object = Class.forName(objectClass).newInstance();
            
            configure(model, resource, object);
                
        } catch (RDFException rdfe) {
            log.error("RDFException ", rdfe);
            throw new ConfigurationException ("Error creating object " + identifier.getURI(), rdfe) ;
        } catch (ClassNotFoundException cnfe) {
            log.error("ClassNotFoundException: ", cnfe);
            throw new ConfigurationException ("Error creating object " + identifier.getURI(), cnfe) ;
        } catch (InstantiationException ie) {
            log.error("InstantiationeException: ", ie);
            throw new ConfigurationException ("Error creating object " + identifier.getURI(), ie) ;
        } catch (IllegalAccessException iae) {
            log.error("IllegalAccessException: ", iae);
            throw new ConfigurationException ("Error creating object " + identifier.getURI(), iae) ;
        }
        
        return object ;
    }
    
    /**
     * 
     */
    public static void configure(Model model, Resource identifier, Object object) throws ConfigurationException {
        log.debug(".configure() [Object:" + identifier.getURI() + "]");
        
        if ( model == null ) {
            log.error("Configuration not available");
            throw new ConfigurationException ("Configuration not available") ;
        }
        
        try {
            // Retrieve name of the Configurable
            // Resource resource = model.createResource(object.getIdentifier());
            
            // Find all Statements, that belong to the Object
            Selector selector = new SimpleSelector(identifier, (Property) null, (RDFNode) null);
            StmtIterator stmtI = model.listStatements(selector);
            
            while ( stmtI.hasNext() ) {
                Statement stmt = stmtI.nextStatement();
                
                Property predicate = stmt.getPredicate();
                RDFNode node = stmt.getObject();
                
                if ( predicate.equals(RDF.type) ) {
                    // Dont set RDF Type
                } else {
                    
                    String _predicate = predicate.getLocalName().trim();
                    String _object = "";
                    
                    if ( node instanceof Literal ) {
                        _object = ((Literal) node).getString().trim();
                    } else if ( node instanceof Resource ) {
                        _object = ((Resource) node).getURI().trim();
                    }
                      
                    setFieldOnObject(object, _predicate, _object);
                }
            }
        } catch (RDFException rdfe) {
            log.error("RDFException: ", rdfe);
        }
    }

    public static void setFieldOnObject(Object object, String attr, String value) {
        log.debug(".setFieldOnObject() [Object,String:" + attr 
                + ",String:" + value + "]");
        
        // Getting Properties from BeanInfo, that match <code>name</code>
        PropertyDescriptor propertyDescriptors[] = null;
        
        try
		{
            BeanInfo beaninfo = Introspector.getBeanInfo(object.getClass());
            propertyDescriptors = beaninfo.getPropertyDescriptors();
        } catch (IntrospectionException ie)
		{
            log.error("IntrospectionException! Could not get bean info: ", ie);
            return;
        }
        
        Iterator i = Arrays.asList(propertyDescriptors).iterator();
        while ( i.hasNext() )
        {
            PropertyDescriptor pd = (PropertyDescriptor) i.next();
            String attribute = pd.getName();
            Method setter = pd.getWriteMethod();
            
            if ( attr.equals(attribute) && (setter != null) )
            {
                
                Class type = pd.getPropertyType();
                
                try
				{
                    Object parameter = null;
                    
                    if (type.equals(String.class)) {
                    	// regular expression for system properties (e.g., ${user.home}
                		String regex = "\\$\\{[a-zA-Z0-9.]*}" ;
                		Pattern pattern = Pattern.compile(regex);
                		Matcher matcher ; 
                			
                		matcher = pattern.matcher(value) ;

            			StringBuffer sb = new StringBuffer();
            	        // Loop through and create a new String 
            	        // with the replacements
            	        while(matcher.find())
            	        {
            	        	String group = matcher.group() ;
            	            matcher.appendReplacement(sb, System.getProperty(group.substring("${".length(), group.length()-"}".length())));
            	        }

            	        // Add the last segment of input to 
            	        // the new String
            	        matcher.appendTail(sb);

            	        parameter = sb.toString() ;
            			
            			log.debug("Property '"+ attr + "', value '" + (String) parameter + "'") ;
                     
                    } else if (type.equals(int.class)) {
                        parameter = Integer.valueOf(value);
                    } else if (type.equals(long.class)) {
                        parameter = Long.valueOf(value);
                    } else if (type.equals(boolean.class)) {
                        parameter = Boolean.valueOf(value);
                    //} else if (type.equals(Resource.class)) {
                    //    parameter = model.createResource(value);
                    } else
                    	log.warn("Class " + type.getName() + " not recognize") ;
                    
                    log.debug("Setting " + attr + " to value " + value + ".");

                    if (parameter != null) {
                        setter.invoke(object,new Object[] { parameter });
                    }
                    
                } catch (NullPointerException npe) {
                    log.error("NullPointerException: ", npe);
                } catch (IllegalAccessException iae) {
                    log.error("IllegalAccessException: " +
                            "Access rights prohibit setting attribute '"
                            + attribute + "'", iae);
                } catch (InvocationTargetException ite) {
                    log.error("InvocationTargetException: " +
                            "Error invoking setter for '"
                            + attribute + "' on object " + object, ite);
                } catch (NumberFormatException nfe) {
                    log.error("NumberFormatException: " +
                            "Attribute '" +
                            attribute + "' type mismatch. " +
                            "(Value: " + value + ")", nfe);
                }
            }
        }
    }
    
    /* **
     * @return Returns the verbose.
     */
    public boolean isVerbose() {
        return _verbose;
    }
}
