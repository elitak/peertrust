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

import java.applet.Applet;
import java.io.File;
import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.exception.InferenceEngineException;
import org.peertrust.meta.Tree;

import com.xsb.interprolog.*;


/**
 * <p>
 * This class queries a XSB Prolog inference engine.
 * </p><p>
 * $Id: XsbProlog.java,v 1.2 2005/05/22 17:56:47 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/05/22 17:56:47 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */
public class XsbProlog implements InferenceEngine
{
	private static Logger log = Logger.getLogger(XsbProlog.class);
	//static final String XSB_DIR_PATH = "/home/dolmedilla/Documents/apps/XSB" ;

	private final String META_INTERPRETER_FILENAME = "interpreter" ;
	private final String RDF_PARSER_FILENAME = "rdfParser" ;
	private final String RDF_PARSER_PREDICATE = "read_RDF_file" ;
	private final String PROLOG_PARSER_FILENAME = "load" ;
	private final String PROLOG_PARSER_PREDICATE = "read_prolog_file" ;
	private final String INIT_PREDICATE = "init" ;

	NativeEngine _engine ;

	String _baseFolder;
	boolean _debugMode;
	String _prologFiles;
	String _rdfFiles;
	String _xsbDirPath ;
	
	public XsbProlog ()
	{
		super() ;
		log.debug("$Id: XsbProlog.java,v 1.2 2005/05/22 17:56:47 dolmedilla Exp $");
	}

	/* (non-Javadoc)
	 * @see org.peertrust.inference.InferenceEngine#init()
	 */
	public void init() throws ConfigurationException {
		if (_xsbDirPath == null)
		{ 
			String msg = "The path to XSB is not specified" ;
			log.error (msg) ;
			throw new ConfigurationException(msg) ;
		}
		
		log.debug("(Init) XsbDirPath = " + _xsbDirPath + " - PrologFiles = " + _prologFiles + " - RdfFiles = " + _rdfFiles + " - _baseFolder = " + _baseFolder) ;
		
		_engine = new NativeEngine(_xsbDirPath);
		
		log.debug("Engine initialized") ;

		consult (_engine, _baseFolder + META_INTERPRETER_FILENAME) ;
		
		if (_debugMode)
			insert("debug_on") ;

		_engine.execute(INIT_PREDICATE) ;
		
		consult (_engine, _baseFolder + RDF_PARSER_FILENAME) ;
		
		log.debug("Program loaded") ;
		
		StringTokenizer filesString ;
		String tmp ;
		
		if (_rdfFiles != null)
		{
			filesString = new StringTokenizer(_rdfFiles,":") ;

			while (filesString.hasMoreTokens())
			{
				tmp = _baseFolder + filesString.nextToken() ;
								
				log.debug("Loading RDF file " + tmp + " into the inference engine") ;
				
				_engine.execute(RDF_PARSER_PREDICATE, tmp) ;
				
				log.debug("RDF file " + tmp + " loaded") ;
			}
		}
		
		consult (_engine, _baseFolder + PROLOG_PARSER_FILENAME) ;
		
		if (_prologFiles != null)
		{
			filesString = new StringTokenizer(_prologFiles,":") ;
			while (filesString.hasMoreTokens())
			{
				tmp = _baseFolder + filesString.nextToken() ;
				
				log.debug("Loading file " + tmp + " into the inference engine") ;
				
				_engine.execute(PROLOG_PARSER_PREDICATE, tmp) ;
				
				log.debug("File " + tmp + " loaded") ;
			}
		}


	}


	public void consult (NativeEngine e, String fileName)
	{
		e.consultAbsolute(new File(fileName));
		//e.consultFromPackage(fileName, XsbProlog.class );
	}
	
//	This was Daniel's code
//
// 	String [] execute (String query, String [] variables)
//	{
//		String xsbVariables = "[" + variables[0] ;
//		for (int i = 1 ; i < variables.length ; i++)
//			{
//				xsbVariables +="," + variables[i] ;
//			}
//		xsbVariables += "]" ;
//		
//			System.out.println ("vars: " + xsbVariables) ;
//		Object [] results = engine.deterministicGoal(query, xsbVariables) ;
//		String [] sResults = new String[results.length] ;
//		for (int i = 0 ; i < results.length ; i++)
//			sResults[i] = (String) results[i] ;
//		return sResults ;
//	}
		
	private class PrologPredicate
	{
		
		private String cmd;
		private String predName;
		private String newCmdStr;
		private ArrayList varList = new ArrayList();
		private ArrayList predComps = new ArrayList();
		private String goalStr;
		private String rValueStr;
		private String[] rValues;
		
		public PrologPredicate( String cmd ) {
			
			this.cmd = cmd;
			extractVariables( cmd );
			buildNDGoalString();
		}
		
		public String getCommand() {
			return cmd;
		}
		
		public String getNewCmdStr() {
			return newCmdStr;
		}
		
		public String getPredName() {
			return predName;
		}
		
		public void addPredComp( String comp ) {
			
			predComps.add( comp );
		}
		
		public void addVar( String var ) {
			
			if( varList.contains( var ) )
				return;
			
			varList.add( var );
		}

		public int getNumOfVar() {
			return varList.size();
		}
		
		public String buildBindName() {
			return "Var"+varList.size();
		}
		
		public void buildNDGoalString() {
			
			if( varList.size() == 0 )
				return;
						
			goalStr = "findall(";
			//Build the nonDeterministic Goal String
			for( int i=0; i< varList.size(); i++ ) {
				
				if( i == 0 )
					goalStr += "(";
				else
					goalStr += ",";
				goalStr += varList.get(i);
			}
			
			goalStr += ")," + getNewCmdStr() + "," + buildBindName() + ")";
		}
		
		public String getGoalString() {
			
			return goalStr;
		}
		
		public void extractVariables( String cmdStr ) {
			
			newCmdStr = "";
			
			StringBuffer sbuf = new StringBuffer( cmdStr.trim() );
			
			int cmdNameLen = sbuf.indexOf( "(" );
			
			String cmdName = sbuf.substring( 0, cmdNameLen ).trim();
			predName = new String( cmdName );
			newCmdStr += (cmdName + "(" );
			predComps.add(cmdName + "(" );
			sbuf.delete( 0, cmdNameLen+1 ).toString().trim();
			
			int idxcomma;
			do {
				
				idxcomma = sbuf.indexOf( "," );
				String cmdArg = ( idxcomma == -1)? sbuf.substring(0).trim():sbuf.substring( 0, idxcomma ).trim();
				int idxVarStart = 0;
				int idxVarEnd = cmdArg.length()-1;
				
				while( cmdArg.charAt(idxVarStart) == '(' || cmdArg.charAt(idxVarStart) == '[' ) {
					
					idxVarStart++;
				}
				
				while( cmdArg.charAt(idxVarEnd) == ')' || cmdArg.charAt(idxVarEnd) == ']' ) {
					
					idxVarEnd--;
				}
				
				if( Character.isUpperCase( cmdArg.charAt(idxVarStart) ) ) {
	
					newCmdStr += ( cmdArg.substring(0, idxVarStart) );
					predComps.add( cmdArg.substring(0, idxVarStart) );
					newCmdStr += ( "Var" + varList.size() );		//cmdArg.substring(idxVarStart,idxVarEnd+1).trim()
					predComps.add( "Var" + varList.size() );
					newCmdStr += ( cmdArg.substring(idxVarEnd+1) );
					predComps.add( cmdArg.substring(idxVarEnd+1) );
					
					addVar( "Var" + varList.size() );
				} else {
					newCmdStr += ( cmdArg );
					predComps.add( cmdArg );
				}
				
				if( idxcomma != -1 ) {
					newCmdStr += ( "," );
					predComps.add( "," );
					sbuf.delete(0, idxcomma+1).toString().trim();
				}
				
			} while( idxcomma != -1 );
			
		}

		public String[] buildReturnStrings( String rValueStr ) {
			
			String[] rValues;
			StringBuffer sbuf = new StringBuffer( rValueStr );
			
			int headLen = sbuf.indexOf(predName);
			if( headLen != -1 )
				;
			sbuf.delete( 0, headLen + predName.length() );
			while( Character.isSpaceChar( sbuf.charAt(0) ) ) {
				sbuf.deleteCharAt(0);
			}
			
			int itemEnd = getItemEnd( sbuf );
			String argStr = sbuf.substring(0, itemEnd+1).trim();
//				System.out.println( argStr );
			
			sbuf.delete( 0, itemEnd+1);		//delete the predicate item
			sbuf.deleteCharAt(0);	//remove the comma
			sbuf.deleteCharAt(0);	//remove the square bracket on the links
			
			ArrayList rListValue = new ArrayList();
			
			String itemStr;
//				System.out.println( sbuf );
			while( (itemEnd = getItemEnd( sbuf )) != -1 ) {
				
				itemStr = sbuf.substring(0, itemEnd+1).trim();
//					System.out.println( "itemStr:" + itemStr );
				sbuf.delete(0, itemEnd+2);		//delete include the comma
				rListValue.add( itemStr );
			}
			
//				System.out.println( rListValue );
			
			int rValuesLen = rListValue.size()/varList.size();
//				System.out.println( rValuesLen );
			rValues = (rValuesLen==0)?null:new String[rValuesLen];
			for( int i=0; i< rValuesLen; i++ ) {
				
				ArrayList tmp = new ArrayList( predComps );
				for( int j=0; j< varList.size(); j++ ) {
					
					int idx;
					while( (idx = tmp.indexOf( varList.get(j))) != -1 ) {
						
						tmp.set(idx, (String)rListValue.get(i*varList.size()+j) );
					}
				}
				
				rValues[i] = "";
				for( int k=0; k< tmp.size(); k++ ) {
					
					rValues[i] += tmp.get(k);
				}
					
			}
			
			return rValues;
		}

		public int getItemEnd( StringBuffer sbuf ) {
			
			int paarCnt = 0;
			int idxTail = 0;
			
			char headChar = sbuf.charAt(0);
//				System.out.println( "headChar:" + headChar );
			if( Character.isLetterOrDigit( headChar )) {
				
				while( Character.isLetterOrDigit( sbuf.charAt(idxTail+1) ) ) {
					idxTail++;
				}
				
				return idxTail;
			}
	
			idxTail = 0;
			switch( headChar ) {
				
				case '(':
					
					paarCnt++;
					while( paarCnt != 0 ) {
						
						switch( sbuf.charAt(++idxTail) ) {
							case '(':	paarCnt++;break;
							case ')':	paarCnt--;break;
						}
					}
					
					return idxTail;
				
				case '[':
					
					paarCnt++;
					while( paarCnt != 0 ) {
						
						switch( sbuf.charAt(++idxTail) ) {
							case '[':	paarCnt++;break;
							case ']':	paarCnt--;break;
						}
					}
					
					return idxTail;
				
				default:
					return -1;
			}
		}		
	}


	/* (non-Javadoc)
	 * @see org.peertrust.inference.InferenceEngine#processTree(org.peertrust.inference.LogicQuery)
	 */
	public LogicAnswer[] processTree(LogicQuery query) throws InferenceEngineException {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see org.peertrust.inference.InferenceEngine#unifyTree(org.peertrust.meta.Tree, java.lang.String)
	 */
	public void unifyTree(Tree tree, String newQuery) throws InferenceEngineException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.peertrust.inference.InferenceEngine#execute(java.lang.String)
	 */
	public boolean execute(String query) throws InferenceEngineException {
		
		return _engine.deterministicGoal(query);
	}

	/* (non-Javadoc)
	 * @see org.peertrust.inference.InferenceEngine#insert(java.lang.String)
	 */
	public void insert(String clause) throws InferenceEngineException {
		// TODO Auto-generated method stub
		
	}

	/* (non-Javadoc)
	 * @see org.peertrust.inference.InferenceEngine#setApplet(java.applet.Applet)
	 */
	public void setApplet(Applet applet) throws InferenceEngineException {
		// TODO Auto-generated method stub
	
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
	 * @return Returns the xsbDirPath.
	 */
	public String getXsbDirPath() {
		return _xsbDirPath;
	}
	/**
	 * @param xsbDirPath The xsbDirPath to set.
	 */
	public void setXsbDirPath(String xsbDirPath) {
		this._xsbDirPath = xsbDirPath;
	}
	
	public static void main(String[] args)
	{	
//		XsbProlog xsb = new XsbProlog() ;
//		
//  		xsb.command("[basics]");
//  		String[] vars = new String[1] ;
//  		vars[0] = "X" ;
//  		
//  		//String[] bindings = xsb.execute("append(\"Hello,\",\"dolmedilla\", ML), name(X,ML)", vars) ;
//  		String[] bindings = xsb.execute("X is 20", vars) ;
//  		String message = bindings[0];
//  		System.out.println("\nMessage:"+message);
//  		System.exit(0);
  
  		
  		
  		
// 			"name(User,UL),append(\"Hello,\", UL, ML), name(Message,ML)",
//    		"[string(User)]",
//    		new Object[]{System.getProperty("user.name")},
//    		"[string(Message),string(User)]");
//  		String message = (String)bindings[0]+(String)bindings[1];
//  		System.out.println("\nMessage:"+message);
//  		// the above demonstrates object passing both ways; 
//  		// since we may simply concatenate strings, an alternative coding would be:
//  		bindings = engine.deterministicGoal(
//    		"name('"+System.getProperty("user.name")+"',UL),append(\"Hello,\", UL, ML), name(Message,ML)",
//    		"[string(Message)]");
//  		// (notice the ' surrounding the user name, unnecessary in the first case)
//  		System.out.println("Same:"+bindings[0]);
//  		System.exit(0);

  	}

}