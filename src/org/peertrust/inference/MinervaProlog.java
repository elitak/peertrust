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
package org.peertrust.inference;

import java.io.IOException;
import java.util.Enumeration ;
import java.util.Hashtable ;
import java.util.Vector ;
import java.applet.Applet;
import com.ifcomputer.minerva.*;

import org.peertrust.inference.PrologTools ;

import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.exception.InferenceEngineException;
import org.peertrust.meta.Proof;
import org.peertrust.meta.Tree;
import org.peertrust.net.Peer;


/**
 * <p>
 * This class queries a Minerva Prolog inference engine.
 * </p><p>
 * $Id: MinervaProlog.java,v 1.13 2006/01/25 16:07:45 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2006/01/25 16:07:45 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */
public class MinervaProlog implements InferenceEngine, Configurable
{
	private final String META_INTERPRETER_FILENAME = "interpreter" ;
	private final String RDF_PARSER_FILENAME = "rdfParser" ;
	private final String RDF_PARSER_PREDICATE = "read_RDF_file" ;
	private final String PROLOG_PARSER_FILENAME = "load" ;
	private final String PROLOG_PARSER_PREDICATE = "read_prolog_file" ;
	private final String INIT_PREDICATE = "init" ;
	private final String VALIDATE_PREDICATE = "validateProof" ;
	
	private Minerva _engine ;
	private Hashtable _varList = new Hashtable() ;
	
	// Variables configured from config file
	private String _prologFiles ;
	private String _rdfFiles ;
	private String _baseFolder ;
	private String _license = "minervagui.mca" ;
	
	private Applet _applet ;
	
	private boolean _debugMode = false ;
	
	private static Logger log = Logger.getLogger(MinervaProlog.class);
	
	public MinervaProlog ()
	{
		super() ;
		log.debug("$Id: MinervaProlog.java,v 1.13 2006/01/25 16:07:45 dolmedilla Exp $");
	}
		
	public void setApplet (Applet applet)
	{
		_applet = applet ;
	}
		
	public void init () throws ConfigurationException
	{
		log.debug("(Init) PrologFiles = " + _prologFiles + " - RdfFiles = " + _rdfFiles + " - _baseFolder = " + _baseFolder + " - License = " + _license) ;
		
		String[] minervaArgs = new String[2];
		
		minervaArgs[0] = "-c";
		minervaArgs[1] = _baseFolder + _license ;
	
		try {
			if (isApplet())
				_engine = new Minerva(_applet,minervaArgs) ;
			else
				_engine = new Minerva(minervaArgs) ;
		}
		catch (IOException e) {
			log.error("Minerva I/O initialization exception:",e) ;
			throw new ConfigurationException (e) ;
		}
		catch (MinervaSystemError e) {
			log.error("minerva: Minerva initialization exception: ",e) ;
			throw new ConfigurationException (e) ;
		}

		log.debug("Engine initialized") ;

		try
		{
			_engine.load(_baseFolder + META_INTERPRETER_FILENAME) ;
			
			if (_debugMode)
				insert("debug_extra") ;
				//insert("debug_on") ;
			
			_engine.execute(INIT_PREDICATE) ;
			
			_engine.load(_baseFolder + RDF_PARSER_FILENAME) ;
			
			log.debug("Program loaded") ;
			
			StringTokenizer filesString ;
			String tmp ;
			
			if (_rdfFiles != null)
			{
				filesString = new StringTokenizer(_rdfFiles,":") ;

				while (filesString.hasMoreTokens())
				{
					tmp = _baseFolder + filesString.nextToken() ;
					
					MinervaAtom atom ;
					if (isApplet())
						atom = new MinervaAtom("file://" + tmp) ;
					else
						atom = new MinervaAtom(tmp) ;
					
					log.debug("Loading RDF file " + tmp + " into the inference engine") ;
					
					_engine.execute(RDF_PARSER_PREDICATE, atom) ;
					
					log.debug("RDF file " + tmp + " loaded") ;
				}
			}
			
			_engine.load(_baseFolder + PROLOG_PARSER_FILENAME) ;
			
			if (_prologFiles != null)
			{
				filesString = new StringTokenizer(_prologFiles,":") ;
				while (filesString.hasMoreTokens())
				{
					tmp = _baseFolder + filesString.nextToken() ;
	
					MinervaAtom atom ;
					if (isApplet())
						atom = new MinervaAtom("file://" + tmp) ;
					else
						atom = new MinervaAtom(tmp) ;
					
					log.debug("Loading file " + tmp + " into the inference engine") ;
					_engine.execute(PROLOG_PARSER_PREDICATE, new MinervaAtom(tmp)) ;
					log.debug("File " + tmp + " loaded") ;
				}
			}
			
		} catch (MinervaSystemError e)
		{
			log.error("Error loading files in Minerva",e) ;
			throw new ConfigurationException (e) ;
		} catch (IOException e)
		{
			log.error("I/O error loading files in Minerva",e) ;
			throw new ConfigurationException (e) ;
		} catch (InferenceEngineException e) {
			log.error("InferenceEngineException", e) ;
			throw new ConfigurationException (e) ;
		}
	}
	
	public void insert (String clause) throws InferenceEngineException
	{
		log.debug("Inserting " + clause + " in the KB") ;
		try {
			MinervaTerm term = parse (clause) ;
			_engine.execute("asserta", term) ;			
		} catch (MinervaSystemError e) {
			throw new InferenceEngineException(e) ;
		}
	}

	private void initParse ()
	{
		_varList.clear() ;
	}
	
	public MinervaTerm parse (String originalQuery)
	{
		String query = originalQuery.trim() ;
		StringBuffer tmp = new StringBuffer ("") ;
		MinervaTerm term = null ;
		
		char initChar = query.charAt(0) ;
		
		if (initChar == '[')
		{
			int indexL = query.lastIndexOf(']') ;
			
			// is a list
			Vector list = PrologTools.extractTerms (query.substring(1, query.length() - 1)) ;
			MinervaTerm [] minervaArgs = new MinervaTerm[list.size()] ;
				
			for (int i = 0 ; i < list.size() ; i++)
				minervaArgs[i] = parse ((String) list.elementAt(i)) ;
			term = MinervaList.create(minervaArgs) ;

//			StringTokenizer list = new StringTokenizer (query.substring(1, query.length() - 1), ",") ;
//			int numElements = list.countTokens() ;
//			MinervaTerm [] minervaArgs = new MinervaTerm[numElements] ;
//				
//			for (int i = 0 ; i < numElements ; i++)
//				minervaArgs[i] = parseTerm (list.nextToken()) ;
//			term = MinervaList.create(minervaArgs) ;
		}
		else if ( (initChar >= '0') && (initChar <= '9') ) 
		{
			// is a number
			term = new MinervaLong (new Long(query).longValue()) ;
		}
		else
		{
			int indexEd = query.lastIndexOf('@') ;
			int index$ = query.lastIndexOf('$') ;
			int indexPred = query.lastIndexOf(')') ;
			int index ;
			if ( (indexEd > index$) && (indexEd > indexPred) )
			{
				//	predicate with @	
				term = new MinervaCompound ("@", parse(query.substring(0, indexEd)), parse (query.substring(indexEd + 1, query.length()))) ;					
			}
			else if ( (index$ > indexEd) && (index$ > indexPred) )
			{
				// predicate with $
				term = new MinervaCompound ("$", parse(query.substring(0, index$)), parse (query.substring(index$ + 1, query.length()))) ;
			}
			else if ( (indexPred > indexEd) && (indexPred > index$) )
			{
				index = query.indexOf('(') ;
				// it is a predicate with arguments
				String predicate = query.substring(0, index) ;
				Vector args = PrologTools.extractTerms (query.substring(index + 1, query.length() - 1)) ;
				if (args.size() > 0)
				{
					MinervaTerm [] minervaArgs = new MinervaTerm[args.size()] ;
				
					for (int i = 0 ; i < args.size() ; i++)
						minervaArgs[i] = parse ((String) args.elementAt(i)) ;
					term = new MinervaCompound (predicate, minervaArgs) ;
				}
				else
				{
					log.error("minerva: error at '" + query + "'. Predicates without arguments are not allowed") ;
					term = new MinervaAtom("nil") ;
				}										
			}
			else
			{
				if ( ( (query.charAt(0) >= 'A') && (query.charAt(0) <= 'Z') ) ||
						(query.charAt(0) == '_') )
				{
					// it is a variable
					term = (MinervaVariable) _varList.get(query) ;
					if (term == null)
					{
						term = new MinervaVariable() ;
						_varList.put(query, term) ;	
					}
				}
				else
				{
					// it is an atom
					term = new MinervaAtom (query) ;
					// log.debug("Atom found: " + query) ;
				} 
			}
		}
		return term ;
	}
	
	public String unparse (MinervaTerm term)
	{
		String query = null ;
		StringBuffer tmp = new StringBuffer("") ;
		
		switch (term.typeOf())
		{	
			case MinervaTerm.ATOM:
				query = ((MinervaAtom) term).stringValue() ;
				// log.debug("Result atom parsed:" + query) ;
				break ;
			case MinervaTerm.COMPOUND:
				MinervaCompound compound =(MinervaCompound) term ;
				String functor = compound.getFunctor() ;
				if ( (functor.equals("@")) || (functor.equals("$")) )
					query = unparse(compound.getArg(0)) + functor + unparse(compound.getArg(1)) ;
				else
				{
					tmp.append(functor + "(") ;
					for (int i = 0 ; i < compound.getArity() ; i++)
						tmp.append(unparse(compound.getArg(i)) + ",") ;					
					tmp.setCharAt(tmp.length()-1,')') ;
					query = tmp.toString() ;
				}
				break ;
			case MinervaTerm.LIST:
				tmp.append("[") ;
				Enumeration enume = ((MinervaList) term).getEnumeration() ;
				while (enume.hasMoreElements())
					tmp.append(unparse ((MinervaTerm)enume.nextElement()) + ",") ; 
				tmp.setCharAt(tmp.length()-1,']') ;
				query = tmp.toString() ;			
				break ;
			case MinervaTerm.LONG:
				query = Long.toString( ((MinervaLong) term).longValue()) ;
				break ;
			case MinervaTerm.VARIABLE:
				MinervaVariable var = ((MinervaVariable) term) ;
				if (var.getValue() == null)
					query = var.toString() ;
				else
					query = unparse(var.getValue()) ;
				break ;
			default:
				query = "unknown" ;
		}
		return query ;
	}
	
	public boolean execute (String query)
	{
		log.debug("parse: " + query) ;
		MinervaTerm minQuery = parse(query) ;
		boolean ret = false;
		
		if (minQuery.typeOf() == MinervaTerm.COMPOUND)
		{
			MinervaCompound term = (MinervaCompound) minQuery ;
			MinervaTerm [] terms = new MinervaTerm[term.getArity()] ;
			for (int i = 0 ; i < terms.length ; i++)
				terms[i] = term.getArg(i) ; 
			try {
				log.debug("Sending to the engine:" + minQuery.toString()) ;
				ret = _engine.execute(term.getFunctor(),terms) ;
			}
			catch (MinervaSystemError e) {
				log.error ("Error executing a query at Minerva", e) ;
				ret = false ;
			}
		}
		else
			log.error("The query " + query + " is not an executable Prolog predicate") ;
		
		return ret ;
	}
	
	// processTree(tree(Id,Goal,Queries,Proof,Requester),TreeResults)
	public LogicAnswer [] processTree (LogicQuery logicQuery)
	{
		log.debug ("Process logic query: " + logicQuery.getGoal() + " - "  + logicQuery.getSubgoals()) ;
		
		String query = "tree(" + 
								logicQuery.getGoal() + "," +
								logicQuery.getSubgoals() + "," +
//								"[]," +
								logicQuery.getRequester() + ")" ;
		
		log.debug ("Query: " + query) ;
		initParse() ;
		MinervaTerm minQuery = parse(query) ;
		
		log.debug ("Minerva Query: " + minQuery.toString()) ;
		
		MinervaVariable resultVar = new MinervaVariable() ;
		
		try {
			log.debug("Sending to the engine:" + "processTree(" + minQuery + ",Return)") ;
			_engine.execute("processTree", minQuery, resultVar) ;
		}
		catch (MinervaSystemError e) {
			log.error("Error processing the tree in Minerva", e) ;
		}
		
		if (resultVar.getValue() == null)
		{
			log.error("No answers") ;
			return null ;
		}
		else
			log.debug("Receiving from the engine:" + resultVar.getValue().toString()) ;
		
		String result = unparse (resultVar) ;
		log.debug("Parsed results: " + result) ;
				
		Vector answerStrings = PrologTools.extractTerms (result.substring(1,result.length()-1)) ;
		
		if (answerStrings.size() > 0)
		{
			LogicAnswer [] answers = new LogicAnswer[answerStrings.size()] ;
			for (int i = 0 ; i < answerStrings.size() ; i++)
			{
				String currentTreeString = (String) answerStrings.elementAt(i) ;

				log.debug("Current tree string: " + currentTreeString) ;
				
				Vector answerArgs = PrologTools.extractTerms (currentTreeString.substring("resultTree(".length(), currentTreeString.length() - 1)) ;
			
				String delegator = (String) answerArgs.elementAt(5) ;
			
				log.debug ("Delegator: " + delegator) ;
				
				if (delegator.equals("nil"))
					delegator = null ;
					
				answers[i] = new LogicAnswer(	(String) answerArgs.elementAt(0),
												(String) answerArgs.elementAt(4),
												(String) answerArgs.elementAt(1),
												(String) answerArgs.elementAt(2),
												(String) answerArgs.elementAt(3),
												delegator) ;	
			}
			return answers ;
		}
		else
			return null ;
	}

	public void unifyTree (Tree tree, String unifiedGoal)
		{
			String query = "old(" + 
									tree.getGoal() + "," +
									tree.getLastExpandedGoal() + "," +
									tree.getResolvent() + ")" ;
		
			log.debug ("Unify new query: " + unifiedGoal + " and old query: " + query) ;
			
			initParse() ;
			MinervaTerm minQuery = parse(query) ;
			MinervaTerm minNewQuery = parse(unifiedGoal) ;
		
			//System.out.println ("Parsed tree: " + minQuery) ;
			//System.out.println ("Parsed new query: " + minNewQuery) ;
		
			MinervaVariable resultVar = new MinervaVariable() ;
		
			try {
				_engine.execute("unification", minQuery, minNewQuery, resultVar) ;
			}
			catch (MinervaSystemError e) {
				log.error("Minerva: error unificating in Minerva: " + e.getMessage()) ;
			}
		
			String result = unparse (resultVar) ;
			
			log.debug ("Unified parsed results: " + result) ;
		
			Vector treeStrings = PrologTools.extractTerms (result.substring("new(".length(),result.length()-1)) ;
		
			tree.setLastExpandedGoal(null) ;
			tree.setGoal((String)treeStrings.elementAt(0)) ;
			tree.setResolvent((String)treeStrings.elementAt(1)) ;		
		}

	private boolean isApplet ()
	{
		if (_applet == null)
			return false ;
		else
			return true ;
	}

	/**
	 * @return Returns the _baseFolder.
	 */
	public String getBaseFolder() {
		return _baseFolder;
	}
	/**
	 * @param folder The _baseFolder to set.
	 */
	public void setBaseFolder(String folder) {
		_baseFolder = folder;
	}
	
	public void setDebugMode (boolean debug)
	{
		_debugMode = debug ;
	}
	
	public boolean getDebugMode ()
	{
		return _debugMode ;
	}
	
	public void setPrologFiles(String files) {
		_prologFiles = files ;
	}

	public String getPrologFiles() {
		return _prologFiles;
	}
	
	public void setRdfFiles(String files) {
		_rdfFiles = files ;
	}
	
	public String getRdfFiles() {
		return _rdfFiles;
	}
	
	/**
	 * @return Returns the _license.
	 */
	public String getLicense() {
		return _license;
	}
	/**
	 * @param _license The _license to set.
	 */
	public void setLicense(String _license) {
		this._license = _license;
	}
		
	public static void main(String[] args) throws MinervaSystemError, IOException, Exception
	{
		//parseTest() ;
		
		MinervaProlog engine = new MinervaProlog() ;
		engine.setBaseFolder("/home/olmedilla/workspace/peertrust/config/prolog/minerva/") ;
		engine.setLicense("minervagui.mca") ;
		
		engine.init() ;
		
//MinervaTerm [] minArgs = new MinervaTerm[1] ;
//minArgs[0] = new MinervaAtom("alice") ;
//MinervaTerm term = new MinervaCompound("peerName", minArgs) ;
		MinervaTerm term = engine.parse("peerName(alice)") ;
		engine._engine.execute("asserta", term) ;
		
		//engine.insert("peerName(alice)") ;
		
		log.debug(new Boolean(engine.execute("peerName(alice)"))) ;
//		MinervaProlog engine = new MinervaProlog() ;		
//		MinervaVariable var = new MinervaVariable() ;
//
//		engine.engine.load("javaMetaI_1") ;
//		
//		engine.engine.execute("init") ;
//		
//		System.out.println ("Result: " + engine.engine.execute("namePeer", var) + " - Var: " + engine.unparse(var) ) ;
//		
//		System.out.println ("Finished") ;
	}
	
	static void parseTest () throws ConfigurationException
	{
		MinervaProlog engine = new MinervaProlog () ;
		engine.setBaseFolder("/home/olmedilla/workspace/peertrust/") ;
		engine.setLicense("config/prolog/minerva/minervagui.mca") ;
		engine.init() ;
		
		//String query = "prueba(Var, atom, 20, [atom, Var2, [32, Var2, Var, Segunda], tree(juego)], final(Segunda))" ;
		//String query = "prueba(tree(_) @ x, Var, atom, 20, [atom, Var2, [32, Var2, Var, Segunda], tree(juego)], final(Segunda))" ;
		//String query = "tree(prueba @ x $ n, maria(X,j @ t) @ test) $ final" ;
		//String query = "tree(document(project7,V12039161),[query(policy1(document(project7,V12039161,V8970080)),no),query(policy2(document(project7,V12039161,V8970080)),no),query(get_record(project7,V12039161),no)],[r(document(project7,V12039161),[policy1(document(project7,V12039161,V8970080)),policy2(document(project7,V12039161,V8970080))],[get_record(project7,V12039161)])@company7],manuel)" ;
		log.debug ("Result execute: " + engine.execute("write('prueba')")) ;
		
		String query = "[tree(employee(alice7,microsoft)@microsoft@alice7,[],[signed(r(employee(alice7,microsoft)@microsoft,[],[]),microsoft,signature(microsoft))@alice7,proved_by(alice7)@company7],manuel)]" ;
		log.debug ("Initial: " + query) ;
		
		MinervaTerm term = engine.parse(query) ;
		log.debug ("Minerva: " + term.toString()) ;
		
		String query2 = engine.unparse(term) ;
		log.debug ("Query: " + query2) ;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.inference.InferenceEngine#validate(java.lang.String, org.peertrust.net.Peer, org.peertrust.meta.Proof)
	 */
	public boolean validate(String goal, Peer prover, Proof proof) throws InferenceEngineException
	{
		log.debug("Validating Goal " + goal + " at " + prover.getAlias() + " with proof \n\t" + proof) ;
		
		String query = VALIDATE_PREDICATE + "(" + goal + "," + prover.getAlias() + "," + proof.toString() + ")" ;
		
		return execute(query) ;
	}

	public void consultFile(String fileName) throws InferenceEngineException {
		try {
			_engine.load(fileName) ;
		} catch (Exception e) {
			throw new InferenceEngineException(e);
		}		
	}
}
 