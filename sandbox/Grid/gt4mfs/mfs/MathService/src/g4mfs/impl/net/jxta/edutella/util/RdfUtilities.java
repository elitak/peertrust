/*****************************************************************************
 *
 * Copyright (c) 2003 The Edutella Project
 * 
 * Redistributions in source code form must reproduce the above copyright 
 * and this condition. The contents of this file are subject to the 
 * Sun Project JXTA License Version 1.1 (the "License"); you may not use 
 * this file except in compliance with the License. 
 * A copy of the License is available at http://www.jxta.org/jxta_license.html.
 *  
 *****************************************************************************/

package g4mfs.impl.net.jxta.edutella.util;

import java.io.StringReader;
import java.io.StringWriter;
import java.util.Hashtable;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.hp.hpl.jena.mem.ModelMem;
import com.hp.hpl.jena.rdf.model.Bag;
import com.hp.hpl.jena.rdf.model.Literal;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFException;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.ResIterator;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Seq;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.vocabulary.DC;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
import com.hp.hpl.jena.vocabulary.RSS;
import com.hp.hpl.jena.vocabulary.VCARD;

/**
 * RDF Utilities are helping with handling searching and modification
 * of Jena-Framework RDF models, resources, nodes and literals.
 *
 * <p>See the
 * <a href="{@docRoot}/license.txt">License</a>,
 * <a href="{@docRoot}/README">ReadMe</a>.</p>
 *
 * Depends on <code>edutella-eqm</code> for vocabulary,
 * <code>log4j</code> and <code>jena</code>.
 *
 * @author <a href="mailto:siberski@learninglab.de">Wolf Siberski</a>
 * @author $Author: ionut_con $
 * @version $Revision: 1.1 $ $Date: 2005/11/30 10:35:10 $
 * @see <a href="http://www.hpl.hp.com/semweb/jena.htm">Jena Semantic
 * Web Toolkit</a>
 */
public abstract class RdfUtilities {
	

    private RdfUtilities() {}
	
	
    // ************************************************************************
	
	
    public void logStatements(Logger log, Model model) {
		log.info(".logStatements()");
		
		try {
			StmtIterator stmts = model.listStatements();
			
			String s = "Statements in this model:\n";
			while (stmts.hasNext()) {
				s += stmts.next().toString() + "\n";
			}
			
			log.info(s);
		} catch (RDFException rdfe) {
			log.error("RDFException: ", rdfe);
		}
    }
	

    // ************************************************************************


    /**
     * Return the <code>Subject</code> of a <code>Statement</code>
     * that has the given <code>Property</code> and <code>Object</code>.
     *
     * @param model a jena toolkit rdf model
     * @param property property of the statement
     * @param object object of the statement
     * @return the resource representing the subject of the found statement
     * @exception RDFException if an error occurs
     * @see com.hp.hpl.jena.rdf.model.Model#listSubjectsWithProperty(property,object);
     * @see #findSubject(Model, Property, String)
     */
    public static Resource findSubject(Model model,
				       Property property,
				       RDFNode object )
	throws RDFException {
    	ResIterator i = model.listSubjectsWithProperty(property, object);
	
    	if ( i.hasNext() ) {
    		return(i.nextResource());
    	}

    	throw(new RDFException(RDFException.STATEMENTNOTPRESENT,
			       "Subject for Property "
			       + "'" + property.getURI()
			       + "' and Object "
			       + "'" + object.toString()
			       + "' not found!"));
    }


    /**
     * Return the <code>Subject</code> of a <code>Statement</code>
     * that has the given <code>Property</code> and <code>Object</code>.
     *
     * @param model a jena toolkit rdf model
     * @param property property of the statement
     * @param object object of the statement
     * @return the resource representing the subject of the found statement
     * @exception RDFException if an error occurs
     * @see com.hp.hpl.jena.rdf.model.Model#listSubjectsWithProperty(Property, RDFNode);
     * @see #findSubject(Model, Property, RDFNode)
     */
    public static Resource findSubject(Model model,
				       Property property,
				       String object )
	throws RDFException
    {

	ResIterator i = model.listSubjectsWithProperty(property);
	
	while (i.hasNext()) {
	    Resource subject = i.nextResource();
	    Statement stmt = model.getProperty(subject, property);
	    Object _object = stmt.getObject();
	    
	    if (_object instanceof Literal) {
		Literal l = (Literal) _object;
		if (l.getString().equals(object)) {
		    return(subject);
		}
	    } else if (_object instanceof Resource) {
		Resource r = (Resource) _object;
		if (r.getURI().equals(object)) {
		    return(subject);
		}
	    }
	}

	throw(new RDFException(RDFException.STATEMENTNOTPRESENT,
			       "Subject for Property "
			       + "'" + property.getURI()
			       + "' and Object "
			       + "'" + object.toString()
			       + "' not found!"));
	
    }


    // ************************************************************************


    /**
     * Return the <code>Object</code> of a <code>Statement</code>
     * that has the given <code>Subject</code> and <code>Property</code>.
     *
     * @param model a jena toolkit rdf model
     * @param subject property of the statement
     * @param predicate object of the statement
     * @return the node representing the object of the found statement
     * @exception RDFException if an error occurs
     * @see com.hp.hpl.jena.rdf.model.Model#listObjectsOfProperty(Resource, Property);
     * @see #findObject(Model, Resource, Property)
     * @see #findObjectResource(Model, Resource, Property)
     * @see #findObjectLiteral(Model, Resource, Property)
     */
    public static RDFNode findObject(Model model,
				     Resource subject,
				     Property predicate)
	throws RDFException
    {

	NodeIterator i = model.listObjectsOfProperty(subject, predicate);

	if ( i.hasNext() ) {
	    return(i.nextNode());
	}

	throw(new RDFException(RDFException.STATEMENTNOTPRESENT,
			       "Object for Subject "
			       + "'" + subject.getURI()
			       + "' and Predicate "
			       + "'" + predicate.toString()
			       + "' not found!"));
    }
    

    /**
     * Returns the <code>Object</code> of a statement with
     * the given <code>Subject</code> and <code>Property</code> which
     * is a <code>Resource</code> and not a <code>Literal</code>
     *
     * @param model a jena toolkit rdf model
     * @param subject property of the statement
     * @param predicate object of the statement
     * @return the resource representing the object of the found statement
     * @exception RDFException if an error occurs
     * @see com.hp.hpl.jena.rdf.model.Model#listObjectsOfProperty(Resource, Property);
     */
    public static Resource findObjectResource(Model model,
					      Resource subject,
					      Property predicate)
	throws RDFException
    {

	NodeIterator i = model.listObjectsOfProperty(subject, predicate);
	
	while ( i.hasNext() ) {
	    RDFNode node = i.nextNode();
	    if (node instanceof Resource) {
		return((Resource) node);
	    }
	}

	throw(new RDFException(RDFException.STATEMENTNOTPRESENT,
			       "Object for Subject "
			       + "'" + subject.getURI()
			       + "' and Predicate "
			       + "'" + predicate.toString()
			       + "' not found!"));
    }


    /**
     * Returns the <code>Object</code> of a statement with
     * the given <code>Subject</code> and <code>Property</code> which
     * is a <code>Literal</code> and not a <code>Resource</code>
     *
     * @param model a jena toolkit rdf model
     * @param subject property of the statement
     * @param predicate object of the statement
     * @return the literal representing the object of the found statement
     * @exception RDFException if an error occurs
     * @see com.hp.hpl.jena.rdf.model.Model#listObjectsOfProperty(Resource, Property);
     */
    public static Literal findObjectLiteral(Model model,
					   Resource subject,
					   Property predicate)
	throws RDFException
    {
	
	NodeIterator i = model.listObjectsOfProperty(subject, predicate);
	
	while ( i.hasNext() ) {
	    RDFNode node = i.nextNode();
	    if (node instanceof Literal) {
		return((Literal) node);
	    }
	}
	
	throw(new RDFException(RDFException.STATEMENTNOTPRESENT,
			       "Object for Subject "
			       + "'" + subject.getURI()
			       + "' and Predicate "
			       + "'" + predicate.toString()
			       + "' not found!"));
    }


    /**
     * Wrapper for {@link #findObjectLiteral(Model, Resource, Property)}.
     *
     * @param model a jena toolkit rdf model
     * @param subject property of the statement
     * @param predicate object of the statement
     * @return the literal representing the object of the found statement
     * @exception RDFException if an error occurs
     */
    public static Literal findObjectLiteral(Model model,
					    String subject,
					    Property predicate)
	throws RDFException
    {
	return(findObjectLiteral(model,
				 model.getResource(subject),
				 predicate));
    }
    

    // ************************************************************************


    /**
     * Split the given URI at the last <code>':'</code>, <code>'/'</code>
     * or <code>'#'</code>.
     *
     * @param str String representation of the uri to split
     * @return index 0 is the head, index 1 the tail of the uri
     */
    public static String[] splitURI(String str) {
	int colonPos = str.lastIndexOf(':');
	int slashPos = str.lastIndexOf('/');
	int hashPos  = str.lastIndexOf('#');
	int splitPos = Math.max (colonPos, Math.max(slashPos, hashPos));
	
	String[] result = new String[2];
	if ( splitPos == -1 ) {
	    result[0] = "";
	    result[1] = str;
	} else {
	    result[0] = str.substring(0, splitPos+1);
	    result[1] = str.substring(splitPos+1, str.length());
	}
	return result;
    }


    // ************************************************************************


    /**
     * Identification Names for different RDF Schemas
     *
     * @see #namespace_url
     * @see #namespace_id
     */
    public static final String[] namespace_id =
    {
	"RDF Syntax",
	"RDF Schema",
	"Dublin Core",
	"Edutella",
	"VCard",
	"RSS",
    };
    
    /**
     * URL of for different RDF Schemas
     *
     * @see #namespace_url
     * @see #namespace_id
     */
    public static final String[] namespace_url =
    {
	RDF.getURI(),
	RDFS.getURI(),
	DC.getURI(),
	VCARD.getURI(),
	RSS.getURI(),
    };

    /**
     * Describe variable <code>url2id</code> here.
     *
     */
    public static Hashtable url2id = null;
    /**
     * Describe variable <code>id2url</code> here.
     *
     */
    public static Hashtable id2url = null;

    /**
     * Describe <code>getURI</code> method here.
     *
     * @param id a <code>String</code> value
     * @return a <code>String</code> value
     */
    public static final String getURI(String id)
    {
	if ( id2url == null ) {
	    RdfUtilities.createUrl2Id();
	}

	if ( id2url.get(id) == null ) {
	    return(id);
	} else {
	    return((String) id2url.get(id));
	}
    }

    /**
     * Describe <code>getID</code> method here.
     *
     * @param url a <code>String</code> value
     * @return a <code>String</code> value
     */
    public static final String getID(String url)
    {
	if ( url2id == null ) {
	    RdfUtilities.createUrl2Id();
	}

	if ( url2id.get(url) == null ) {
	    return(url);
	} else {
	    return((String) url2id.get(url));
	}
    }

    /**
     * Describe <code>createUrl2Id</code> method here.
     *
     */
    public static final void createUrl2Id() {
	url2id = new Hashtable();
	for (int i = 0; i < namespace_url.length; i++) {
	    url2id.put(namespace_url[i], namespace_id[i]);
	}
    }

    /**
     * Describe <code>createId2Url</code> method here.
     *
     */
    public static final void createId2Url() {
	id2url = new Hashtable();
	for (int i = 0; i < namespace_id.length; i++) {
	    url2id.put(namespace_id[i], namespace_url[i]);
	}
    }


    // ************************************************************************


    /**
     * Uses a subject <code>Resource</code> and a 
     * predicate <code>Property</code> to fill a <code>Vector</code> with
     * the contents of a rdf <code>Seq</code> or <code>Bag</code>.
     *
     * NOTE: Errors are supressed.
     *
     * @param model jena toolkit rdf model
     * @param subject subject resource holding the collection
     * @param hasCollect hasCollection predicate pointing to the collection
     * @param vector vector to fill with the contents of the seq or bag
     * @see #fillVector(Model, String, Property, Vector)
     * @see com.hp.hpl.jena.vocabulary.RDF.Seq
     * @see com.hp.hpl.jena.vocabulary.RDF.Bag
     * @see com.hp.hpl.jena.rdf.model.Seq
     * @see com.hp.hpl.jena.rdf.model.Bag
     */
    public static final void fillVector(Model model,
					Resource subject,
					Property hasCollect,
					Vector vector) {

        try {
            Seq seq = model.getProperty(subject, hasCollect).getSeq();
            
            if (seq != null) {
                NodeIterator i = seq.iterator();
                while(i.hasNext()) {
                    RDFNode node = i.nextNode();
                    String object = null;
                    if (node instanceof Resource) {
                        object = ((Resource) node).getURI();
                    } else if (node instanceof Literal) {
                        object = ((Literal) node).getString();
                    }
                    vector.addElement(object);
                }
            }
            return;
        } catch (RDFException rdfe) {
            // log.error("RDFException: ", rdfe);
        }
        
        try {
            Bag bag = model.getProperty(subject, hasCollect).getBag();
            if (bag != null) {
                NodeIterator i = bag.iterator();
                while(i.hasNext()) {
                    RDFNode node = i.nextNode();
                    String object = null;
                    if (node instanceof Resource) {
                        object = ((Resource) node).getURI();
                    } else if (node instanceof Literal) {
                        object = ((Literal) node).getString();
                    }
                    vector.addElement(object);
                }
            }
            return;
        } catch (RDFException rdfe) {
            // log.error("RDFException: ", rdfe);
        }
    }
    
    /**
     * Uses a subject <code>Resource</code> and a 
     * predicate <code>Property</code> to fill a <code>Vector</code> with
     * the contents of a rdf <code>Seq</code> or <code>Bag</code>.
     *
     * NOTE: Errors are supressed.
     *
     * @param model jena toolkit rdf model
     * @param subject subject resource holding the collection
     * @param hasCollect hasCollection predicate pointing to the collection
     * @param vector vector to fill with the contents of the seq or bag
     * @see #fillVector(Model, String, Property, Vector)
     * @see com.hp.hpl.jena.vocabulary.RDF.Seq
     * @see com.hp.hpl.jena.vocabulary.RDF.Bag
     * @see com.hp.hpl.jena.rdf.model.Seq
     * @see com.hp.hpl.jena.rdf.model.Bag
     */
    public static final void fillVector(Model model,
            String subject,
            Property hasCollect,
            Vector vector)
    {
        try {
            RdfUtilities.fillVector(model, model.getResource(subject),
                    hasCollect, vector);
        } catch (RDFException rdfe) {
            // log.warn("RDFException: ", rdfe);
        }
    }
    
    
    // ************************************************************************
    
    
    /**
     * 
     * @param string
     * @return
     */
	public static Model fromString(String string) {

		Model model = new ModelMem();

		try {
			StringReader reader = new StringReader(string);
			model.read(reader, "");
		} catch (Exception e) {
			return(null);
		}

		return(model);
	}


    /**
     * 
     * @param model
     * @return
     */
	public static String toString(Model model) {

		try {
			StringWriter writer = new StringWriter();
			model.write(writer);
			String string = new String(writer.getBuffer());
			return(string);
		} catch (Exception e) {
			return("");
		}
	}
}
