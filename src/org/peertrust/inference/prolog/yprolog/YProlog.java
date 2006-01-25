/**
* This class is distributed under the revised BSD license.
* 
* Copyright (c) 1996-2005 by:
* Michael Winikoff, RMIT University, Melbourne, Australia
* Jean Vaucher, Montreal university, Canada
* Boris van Schooten, University of Twente, Netherlands
* All rights reserved.
* 
* Redistribution and use in source and binary forms, with or without
* modification, are permitted provided that the following conditions are met:
* 
*     * Redistributions of source code must retain the above copyright notice,
*      this list of conditions and the following disclaimer.
*     * Redistributions in binary form must reproduce the above copyright notice,
*       this list of conditions and the following disclaimer in the
*       documentation and/or other materials provided with the distribution.
*     * Neither the name of the organizations nor the names of its contributors
*       may be used to endorse or promote products derived from this software
*       without specific prior written permission.
* 
* * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
* AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
* IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
* DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
* FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
* DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
* SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
* CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
* OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
* OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/

package org.peertrust.inference.prolog.yprolog;

import java.util.*;
import java.io.*;

import java.lang.reflect.*;

/** The interface to the YProlog interpreter.   
*/
/**
 * @author olmedilla
 *
 */
/**
 * @author olmedilla
 *
 */
public class YProlog {

    public static String VERSION =
		"YProlog v.0.4, 2 dec 2005";

	private static Hashtable type_conv_ids = new Hashtable();
	static {
		type_conv_ids.put("boolean",new Integer(0)); /* boolean */
		type_conv_ids.put("byte",new Integer(1)); /* byte */
		type_conv_ids.put("short",new Integer(2)); /* short */
		type_conv_ids.put("int",new Integer(3)); /* int */
		type_conv_ids.put("long",new Integer(4)); /* long */
		type_conv_ids.put("float",new Integer(5)); /* float */
		type_conv_ids.put("double",new Integer(6)); /* double */
		type_conv_ids.put("char",new Integer(7)); /* char */
		type_conv_ids.put("java.lang.String",new Integer(8));
		type_conv_ids.put("[Ljava.lang.String;",new Integer(9));
	}

	Engine yp_eng=null;

	/** Create new engine with empty environment.
	*/
	public YProlog () {
		try {
			yp_eng = new Engine();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/** Add statements to Prolog environment.
	*/
	public void consult(String statements) {
		try {
			yp_eng.consultStringFront(statements);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/** Add statements to Prolog environment, read from file.
	*/
	public void consultFile(String filename) {
		try {
			yp_eng.consult(filename);
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	/** Quick method for asserting a single fact from a functor string and a
	 * list of string parameters, Vector version.
	 */
	public void assertFact(String functor, Vector params) {
    	Term [] par_terms = new Term [params.size()];
		int i=0;
		for (Enumeration e=params.elements(); e.hasMoreElements(); i++) {
			String par = (String) e.nextElement();
			par_terms[i] = new Term(par,0);
		}
		yp_eng.db.addClauseFront(
			new Clause(new Term(functor, par_terms), null) );
	}

	/** Quick method for asserting a single fact from a functor string and a
	 * list of string parameters, array version.
	 */
	public void assertFact(String functor, String [] params) {
    	Term [] par_terms = new Term [params.length];
		for (int i=params.length-1; i>=0; i--) {
			par_terms[i] = new Term(params[i],0);
		}
		yp_eng.db.addClauseFront(
			new Clause(new Term(functor, par_terms), null) );
	}


	/** Add statements from table as read from stream.  The stream is closed
	* when the method is finished. 
	*
	* @see #assertStream(BufferedReader,int,String,String,String)
	* @return the actual number of results read
	*/
	public int assertStream(InputStream instream, int max_nr_results,
	String fieldseparator, String functor, String keyspec) 
	throws IOException {
		BufferedReader in = new BufferedReader(new InputStreamReader(instream));
		int res=assertStream(in,max_nr_results,fieldseparator,functor,keyspec);
		// When we are finished with the bufferedreader, some streams are
		// closed and others are not.  Here we ensure that it is always
		// closed.
		in.close();
		return res;
	}


	/** Add statements from table as read from stream reader. The stream is
	* not closed.  The table's entries are assumed to be EOL-terminated, and
	* the fields are assumed to be separated by fieldseparator.  The entries
	* are asserted as facts, with functor as functor and the fields as
	* arguments. The table may contain rows with different lengths (i.e.
	* different numbers of separators), which will be asserted as facts with
	* different arities.
	*
	* <P> An optional database key specification can be supplied.  This
	* specifies which fields are keys.  A "*" character specifies that the
	* field at that position is a key, any other character specifies a
	* non-key.  For example, "**...." specifies a six-field table, with the
	* first two fields being keys and the others being non-keys.  The key
	* specification is used to ensure uniqueness of different entries with the
	* same key: any entries in the db with the same key are removed before
	* each entry is asserted.
	*
	* <P> Stops when max_nr_results is reached, or EOF is encountered.
	* Reads all results if max_nr_results equal or less than 0.
	*
	* @param max_nr_results  max number of results to look for, 0 = all
	* @param keyspec  database key specification, null if n/a
	* @return the actual number of results read
	*/
	public int assertStream(BufferedReader in, int max_nr_results,
	String fieldseparator, String functor, String keyspec)
	throws IOException {
		String line;
		int sep_len = fieldseparator.length();
		int nr_results=0;
		String prefix = functor+"(";
		while (max_nr_results <= 0 || nr_results < max_nr_results) {
			line = in.readLine();
			if (line==null) break;
			Vector fields = new Vector(40,40);
			int idx=0;
			int previdx=0;
			while ( (idx=line.indexOf(fieldseparator,idx)) >= 0) {
				fields.add(line.substring(previdx,idx));
				idx += sep_len;
				previdx = idx;
			}
			fields.add(line.substring(previdx,line.length()));
			if (keyspec!=null) {
				StringBuffer retract_op = new StringBuffer(prefix);
				int fieldidx=0;
				for (Enumeration e=fields.elements(); e.hasMoreElements();
				fieldidx++) {
					String field = (String) e.nextElement();
					if (keyspec.charAt(fieldidx) == '*') {
						retract_op.append(addQuotesQuick(field));
					} else {
						retract_op.append("_");
					}
					if (e.hasMoreElements()) retract_op.append(",");
				}
				retract_op.append(").");
				retract(retract_op.toString());
			}
			assertFact(functor,fields);
			nr_results++;
		}
		return nr_results;
	}

	/** Convert the contents of the supplied objects to Prolog facts.  The
	* objects are assumed to have the PrologObject interface.  The
	* toPrologTable method is used to convert each object to a parameter
	* list.  The fact with given functor and thus obtained parameters is
	* asserted.  Does nothing if objects==null.
	*/
	public void assertObjects(String functor,Vector objects){
		if (objects==null) return;
		for (Enumeration e=objects.elements(); e.hasMoreElements(); ) {
			PrologObject obj = (PrologObject) e.nextElement();
			assertFact(functor, obj.toPrologTable());
		}
	}

	/** For all solutions of the goal, retract all objects unifying with the
	* first term of the solution. Example: retract("a(X), X&lt;100") will
	* retract all a(X) with X &lt; 100.  Will only retract facts, not rules.
	* The retract operation is done after all solutions have been found first.
	*/
	public void retract(String goal) {
		yp_eng.clauses_to_delete = new Vector(40,80);
		if (query(goal)==null) return;
		while (yp_eng.more()) ;
		for (Enumeration e=yp_eng.clauses_to_delete.elements();
		e.hasMoreElements(); ) {
			TermList elem_del = (TermList) e.nextElement();
			//System.out.println("elem_del "+elem_del+" prev="
			//	+elem_del.prevClause+" next="+elem_del.nextClause);
			if (elem_del.prevClause!=null) {
				elem_del.prevClause.nextClause = elem_del.nextClause;
				if (elem_del.nextClause!=null)
					elem_del.nextClause.prevClause = elem_del.prevClause;
			} else {
        		String key = elem_del.term.getdbkey();
				if (elem_del.nextClause!=null) {
					elem_del.nextClause.prevClause=null;
					yp_eng.db.put(key, elem_del.nextClause);
				} else {
					yp_eng.db.remove(key);
				}
			}
		}
		yp_eng.clauses_to_delete=null;
	}


	/** Prolog query, return raw result. */
	protected TermList query(String goal) {
		try {
			if (yp_eng.setQuery(goal)) {
				return yp_eng.answer();
			} else {
				return null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/** Prolog query, returning result as String.  The String is a Prolog
	* clause which is the same as the goal clause, but with the variables
	* bound.
	*
	* @return string describing result, or null if no solution.
	*/
	public String queryToString(String goal) {
		TermList res = query(goal);
		if (res!=null) return res.toString();
		return null;
	}

	/** Get next result from goal as prolog clause.
	* @return result, or null if no more results
	* @see #queryToString(String)
	*/
	public String moreToString() {
		if (yp_eng.more()) {
			return yp_eng.answer().toString();
		} else {
			return null;
		}
	}

	/** Return a number of goal matches as a String containing prolog
	* statements, null if no results.  The statements are in the form of
	* instances of the query expression, with the variables filled in.
	* Each is ended with a dot ".".  A separator can be defined, which is
	* added between consecutive statements.  The order of the results is the
	* same as would be returned by queryToString / moreToString.
	*
	* Returns all results if max_nr_results equal or less than 0.
	*
	* @param max_nr_results  max number of results to look for, 0 = all
	* @return String, null if no results.
	* @see #queryToString(String)
	* @see #moreToString()
	*/
	public String queryToString(String goal, int max_nr_results,
	String separator) {
		int nr_results=1;
		String res1 = queryToString(goal);
		if (res1 == null) return null;
		StringBuffer results=new StringBuffer(res1);
		results.append(".");
		while (max_nr_results <= 0 || nr_results < max_nr_results) {
			String resi = moreToString();
			if (resi == null) {
				break;
			} else {
				results.append(separator);
				results.append(resi);
				results.append(".");
			}
			nr_results++;
		}
		return results.toString();
	}

	/** Prolog query, returning SQL style result.  The engine solves the
	* supplied prolog expression as a regular prolog goal.  The bindings of
	* the parameters of the first term are returned as an array.  In case you
	* only want specific variable bindings to be returned, you can use the
	* seltbl predicate (analogous to the sql select statement).
	*
	* <P> If the parameters are atoms, they are returned as unquoted strings.
	* In case they are not atoms, but lists or predicates, they are converted
	* to strings in the regular way.
	*
	* @return parameter values of 1st term in goal, or null if no solution.
	*/
	public String [] queryToTable(String goal) {
		TermList res = query(goal);
		if (res!=null) return buildResult(res);
		return null;
	}


	/** Get next result from goal, SQL style.
	* @return result, or null if no more results
	* @see #queryToTable(String)
	*/
	public String [] moreToTable() {
		if (yp_eng.more()) {
			return buildResult(yp_eng.answer());
		} else {
			return null;
		}
	}

	/** Return a number of goal matches SQL style, as a Vector of arrays of
	* Strings, null if no results.  Returns all results if max_nr_results
	* equal or less than 0.  If remove_duplicates is true, the order of the
	* results is undefined.  Otherwise, the order is as returned by
	* queryToTable(String) / moreToTable(String).
	*
	* @param remove_duplicates  true if duplicate entries are to be removed
	* @param max_nr_results  max number of results to look for, 0 = all
	* @return Vector of arrays of strings, null if no results.
	* @see #queryToTable(String)
	* @see #moreToTable()
	*/
	public Vector queryToTable(String goal, int max_nr_results,
	boolean remove_duplicates) {
		int nr_results=1;
		String [] res1 = queryToTable(goal);
		if (res1 == null) return null;
		Vector results = new Vector(40,80);
		results.add(res1);
		while (max_nr_results <= 0 || nr_results < max_nr_results) {
			String [] resi = moreToTable();
			if (resi == null) {
				break;
			} else {
				results.add(resi);
			}
			nr_results++;
		}
		if (remove_duplicates) {
			int idx=0;
			Hashtable set_hash = new Hashtable();
			for (Enumeration e=results.elements(); e.hasMoreElements();){
				String [] elem = (String []) e.nextElement();
				List elem_as_list = Arrays.asList(elem);
				//Vector elem_vec = new Vector(elem.length,20);
				//for (int i=0; i<elem.length; i++) elem_vec.add(elem[i]);
				set_hash.put(elem_as_list,new Integer(idx++));
			}
			results = new Vector(40,80);
			for (Enumeration e=set_hash.keys();e.hasMoreElements();) {
				List e_l = (List)e.nextElement();
				results.add(e_l.toArray());
			}
		}
		return results;
	}


	/** Output a number of goal matches to a stream in table form.  Only
	* outputs the parameter values of the first term of the goal, in the same
	* manner as queryToTable.  Consecutive parameters of each result are
	* separated by fieldseparator, each result is terminated by
	* lineterminator.
	*
	* Returns all results if max_nr_results equal or less than 0.
	*
	* @param max_nr_results  max number of results to look for, 0 = all
	* @return  number of results actually written
	* @see #queryToTable(String)
	*/
	public int queryToStream(PrintStream stream,
	String goal, int max_nr_results,
	String fieldseparator,String lineterminator) {
		int nr_results=0;
		String [] resi = queryToTable(goal);
		while (max_nr_results <= 0 || nr_results < max_nr_results) {
			if (resi == null) return nr_results;
			for (int i=0; i<resi.length; i++) {
				stream.print(resi[i]);
				if (i<resi.length-1) stream.print(fieldseparator);
			}
			stream.print(lineterminator);
			nr_results++;
			resi = moreToTable();
		}
		return nr_results;
	}

	/** Return a number of goal matches as a Vector of Objects, null if no
	* results. Returns all results if max_nr_results equal or
	* less than 0.  If remove_duplicates is true, the order of the results is
	* undefined.  Otherwise, the order is as returned by
	* queryToTable(String) / moreToTable(String).  The objects are of the
	* class given by cls.  They are constructed as described in
	* resultToObject.
	*
	* @param remove_duplicates  true if duplicate entries are to be removed
	* @param max_nr_results  max number of results to look for, 0 = all
	* @see #resultToObject(String[],Class)
	*/
	public Vector queryToObjects(String goal, Class cls, int max_nr_results,
	boolean remove_duplicates) {
		Vector res = queryToTable(goal,max_nr_results,remove_duplicates);
		Vector res_obj = new Vector(40,80);
		if (res==null) return null;
		int nr_params = ((String [])res.get(0)).length;
		Constructor con = matchConstructor(nr_params,cls);
		Class [] partypes = con.getParameterTypes();
		for (Enumeration e=res.elements(); e.hasMoreElements(); ) {
			res_obj.add(
				resultToObject((String [])e.nextElement(), con, partypes) );
		}
		return res_obj;
	}

	/* XXX dump is similar to KnowledgeBase dump / list methods */

	/** Dumps all predicates matching the predicate spec to a set of prolog
	* statements, null if none found.  The predicate spec is defined as
	* [predicate name] '/' [predicate arity], i.e. "mypredicate/3"
	*/
	public String dump(String predicate_spec) {
		if (yp_eng.db.primitives.contains(predicate_spec)) return null;
		Clause thisclause = (Clause)yp_eng.db.get(predicate_spec);
		if (thisclause==null) return null;
		StringBuffer res = new StringBuffer(thisclause.toString());
		while (thisclause.nextClause!=null) {
			thisclause = thisclause.nextClause;
			res.append(thisclause.toString());
		}
		return res.toString();
	}

	/** Dumps all predicates in the kb to a set of prolog statements.
	*/
	public String dump() {
		StringBuffer res = new StringBuffer();
		for (Enumeration e=yp_eng.db.keys(); e.hasMoreElements(); ) {
			String pred_spec = (String)e.nextElement();
			String pred_string = dump(pred_spec);
			if (pred_string!=null) res.append(pred_string);
		}
		return res.toString();
	}

	/** Start interactive mode.  This method returns when the user typed
	* 'quit'.
	*/
	public void interactive(InputStream in, PrintStream out) {
		Go.interactive(this,in,out);
	} 

	/** Start interactive mode, use stdin/stdout.  This method returns
	* when the user typed 'quit'.
	*/
	public void interactive() {
		Go.interactive(this,System.in,System.out);
	} 


	/** Build a result from the term list, which is assumed to represent a
	* solved goal. The array of objects stands for the parameter
	* instantiation of the goal's first term.  The returned strings are
	* unquoted values in case the parameters are atoms, otherwise they are
	* converted to strings in the regular way.
	*/
	private String [] buildResult(TermList result) {
		Term call = result.getTerm(); // We only take the 1st term
		Term [] args = call.getArgs();
		int nr_args = call.getArity();
		String [] res = new String [nr_args];
		for (int i=0; i<nr_args; i++) {
			// get arg's term, dereference if necessary
			Term argterm = args[i].getReference();
			if (argterm==null) {
				argterm = args[i];
			} else {
				while (argterm.getReference() != null) {
					argterm = argterm.getReference();
				}
			}
			if (argterm.getArity() > 0) {
				// non atom parameters, such as lists, are just converted to
				// strings. Maybe do something else later.
				res[i] = argterm.toString();
			} else {
				res[i] = argterm.getFunctor();
			}
		}
		return res;
	}

	/** Convert an array result to an object by finding an appropriate
	* constructor.  First it tries to find a constructor with the same number
	* of parameters as there are elements in the array.  If found, it tries
	* to convert each element of the array to the type of each parameter.
	* Then it calls the constructor to create the object that is returned.
	*/
	public static Object resultToObject(String [] result, Class c) {
		Constructor [] con = c.getConstructors();
		Class [] partypes=null;
		int resultlen = result.length;
		for (int i=con.length-1; i>=0; i--) {
			if ( (partypes=con[i].getParameterTypes()).length == resultlen) {
				return resultToObject(result, con[i], partypes);
			}
		}
		return null;
	}

	/** Try to find a constructor with the given number of parameters.
	* Returns null if not found.
	*/
	public static Constructor matchConstructor(int nr_params,Class c) {
		Constructor [] con = c.getConstructors();
		for (int i=con.length-1; i>=0; i--) {
			if (con[i].getParameterTypes().length == nr_params) {
				return con[i];
			}
		}
		return null;
	}

	private static Object resultToObject(String [] result, Constructor con,
	Class [] partypes) { try {
		int len = result.length;
		Object [] pars = new Object [len];
		for (int i=0; i<len; i++) {
			//System.out.println("PAR:"+partypes[i].getName());
			String res = result[i];
			Object res_obj=null;
			switch ( ((Integer)type_conv_ids.get(partypes[i].getName()))
				.intValue() ) {
			case 0: /* boolean */
				if (res.equals("true")) {
					res_obj=Boolean.TRUE;
				} else if (res.equals("false")) {
					res_obj=Boolean.FALSE;
				}
			break;
			case 1: /* byte */
				res_obj=new Byte(Byte.parseByte(res));
			break;
			case 2: /* short */
				res_obj=new Short(Short.parseShort(res));
			break;
			case 3: /* int */
				res_obj=new Integer(Integer.parseInt(res));
			break;
			case 4: /* long */
				res_obj=new Long(Long.parseLong(res));
			break;
			case 5: /* float */
				res = stripQuotes(res);
				res_obj=new Float(Float.parseFloat(res));
			break;
			case 6: /* double */
				res = stripQuotes(res);
				res_obj=new Double(Double.parseDouble(res));
			break;
			case 7: /* char */
				res = stripQuotes(res);
				res_obj=new Character(res.charAt(0));
			break;
			case 8: /* String */
				res = stripQuotes(res);
				res_obj=res;
			break;
			case 9: /* String [] */
				res_obj = listToStringArray(res);
			break;
			}
			pars[i]=res_obj;
		}
		return con.newInstance(pars);
	} catch (Exception e) {
		e.printStackTrace();
		return null;
	} }

	/** Strip Prolog quotes (single quotes) at beginning and end if present.
	*/
	public static String stripQuotes(String in) {
		if (in.charAt(0) == '\'') {
			if (in.charAt(in.length()-1) == '\'') {
				return in.substring(1,in.length()-1);
			} else {
				IO.error("stripQuotes","no matching end quote");
				return in;
			}
		} else {
			return in;
		}
	}

	/** Parse a String that represents a Prolog list into a String
	* array.  A list is assumed to look like this: [atom,atom,...].  Each atom
	* may be a bare word or a quoted string.  It does not handle whitespaces,
	* and is only meant to parse output of Term toString.
	*/
	public static String [] listToStringArray(String prologlist) {
		Vector arr = new Vector(20,50);
		boolean inside_quotes=false;
		boolean stuff_in_buffer=false;
		if (!(prologlist.charAt(0) == '[')
		||  !(prologlist.charAt(prologlist.length()-1) == ']') ) {
			System.out.println("Warning: can't find list brackets!");
		} else {
			prologlist = prologlist.substring(1, prologlist.length()-1);
		}
		StringBuffer next_string=new StringBuffer();
		for (StringTokenizer toker=new StringTokenizer(prologlist,"',",true);
		toker.hasMoreTokens(); ) {
			String tok = toker.nextToken();
			if (tok.equals(",")) {
				if (!inside_quotes) {
					//comma indicates end of atom
					if (stuff_in_buffer) {
						arr.add(next_string.toString());
						next_string=new StringBuffer();
						stuff_in_buffer=false;
					}
				} else {
					//inside string, just add it
					next_string.append(tok);
					stuff_in_buffer=true;
				}
			} else if (tok.equals("'")) {
				if (inside_quotes) {
					// close quote
					arr.add(next_string.toString());
					next_string=new StringBuffer();
					stuff_in_buffer=false;
				} else {
					//open quote
					stuff_in_buffer=true;
				}
				inside_quotes = !inside_quotes;
			} else {
				next_string.append(tok);
				stuff_in_buffer=true;
			}
		}
		if (inside_quotes) {
			System.out.println("Warning: Unclosed quotes!");
		}
		// add what's left in next_string
		if (stuff_in_buffer) arr.add(next_string.toString());
		String [] arr_str = new String [arr.size()];
		int i=0;
		for (Enumeration e=arr.elements(); e.hasMoreElements(); i++) {
			arr_str[i] = (String) e.nextElement();
		}
		return arr_str;
	}

	/** Quote string as necessary for Prolog to understand it as an atom or a
	* functor.  This is a quick version of addQuotes that always adds quotes.
	*/
	public static String addQuotesQuick(String in) {
		return "'"+in+"'";
	}

	/** Quote string as necessary for Prolog to understand it as an atom or a
	* functor.
	* Atoms are sequences of digits, or identifiers starting with a lowercase
	* letter, except "is" and "mod", which are keywords in this prolog.
	* Functors are like atoms, except that they may not start with a digit.
	* Strings not conforming to this atom format are enquoted with single
	* quotes.
	*/
	public static String addQuotes(String in,boolean is_atom) {
		boolean isregular=true, isnumeric=true;
		char c;
		if (in.equals("is")
		||  in.equals("mod")
		||  in.equals("")  ) {
			isregular=false;
		} else {
			c = in.charAt(0);
			if (!Character.isDigit(c)) isnumeric=false;
			if (!isnumeric
			&&  !(c >= 'a' && c <= 'z') ) {
				isregular=false;
			}
		}
		if (isregular && !isnumeric) {
			for (int i=1; i<in.length(); i++) {
				c = in.charAt(i);
				if (!Character.isDigit(c)
				&&  !Character.isLetter(c)
				&&  !(c == '_') ) {
					isregular=false;
					break;
				}
			}
		}
		if (isregular && isnumeric) {
			for (int i=1; i<in.length(); i++) {
				c = in.charAt(i);
				if (!Character.isDigit(c)) {
					isregular=false;
					break;
				}
			}
		}
		if (is_atom && isregular) {
			return in;
		} else if (!is_atom && isregular && !isnumeric) {
			return in;
		} else {
        	return "'"+in+"'";
		}
	}

	/** Convert results, as given by queryToString(String,int,String),
	* to String, for printing.  Returns empty string if null is passed.
	* @see #queryToString(String,int,String)
	*/
	public static String resultsToString(Vector results) {
		if (results==null) return "";
		// using StringBuffer is a *lot* more efficient for large result lists
		StringBuffer res_strbuf = new StringBuffer();
		for (Enumeration e=results.elements(); e.hasMoreElements(); ) {
			String [] resi= (String []) e.nextElement();
			res_strbuf.append(arrayToString(resi));
			if (e.hasMoreElements()) res_strbuf.append( ";" );
		}
		return res_strbuf.toString();
	}

	/** Convert array of objects to Prolog syntax compatible comma separated
	* string.  The objects are converted to Strings and then quoted as
	* appropriate.  Returns empty string when null is passed or array is
	* empty.
	*/
	public static String arrayToString(Object [] array) {
		if (array==null) return "";
		StringBuffer res=new StringBuffer();
		int len=array.length;
		for (int i=0; i<len; i++) {
			res.append(addQuotes(array[i].toString(),true));
			if (i < len-1) res.append(",");
		}
		return res.toString();
	}

	/** Test function.
	*/
	public static void main (String args[]) throws IOException {
		System.out.println("\nStarting YProlog Test procedure.\n");
		YProlog p = new YProlog();
		p.consult("l([1,2,3,4,5]).");
		p.consult("l([5,[4,[3],2],1]).");
		p.consult("l([]).");
		p.consult("l([8,7,6,5]).");
		p.consult("l2([X]) :- l([X|Xs]).");
		p.consult("m(1).");
		p.consult("m('abcdefghij').");
		p.consult("m('&(*$&*(@#$#@').");
		p.consult("b(X) :- a(Y), X is Y + 20000.");
		p.consult("b(X) :- a(Y), X is Y + 40000.");
		System.out.println("Testing dump function...");
		String dumped;
		System.out.println(dumped=p.dump());
		YProlog p2 = new YProlog();
		p2.consult(dumped);
		System.out.println(p2.dump());
		System.out.println("Adding 10,000 terms, a(0) - a(9999)...");
		for (int i=0; i<10000; i++) {
			p.consult("a("+i+").");
		}
		System.out.println("Getting all 10,000 results...");
		resultsToString(p.queryToTable("a(X)",10000,false));
		System.out.println("Getting 10,000 queries with 1 result...");
		for (int i=0; i<10000; i++) {
			resultsToString(p.queryToTable("a(X)",1,false));
		}
		System.out.println("Testing regex; printing all numbers with three '1's...");
		System.out.println(resultsToString(p.queryToTable(
			"a(X),matchregex(X,'.*[1].*[1].*[1].*')", 0,false )));
		System.out.println("Testing set query: printing all unique ints after dividing by 100...");
		System.out.println(resultsToString(p.queryToTable(
			"seltbl(X), a(Y),X is Y / 100", 0,true)));
		//System.out.println(resultsToString(p.queryToTable(
		//	"a(X),retract(a(X))", 1000,true)));
		//System.out.println(resultsToString(p.queryToTable(
		//	"a(X)", 0,true)));
		System.out.println("Testing object retrieval.  Retrieving 10,000 objects...");
		Vector obj = p.queryToObjects("seltbl(false,Byte,Short,Int,Int,'1.11','1.1111111111','_','&*^*(&$@#',[a,'Bb','c,d']), b(Int), Int<40000, Byte is Int/400, Short is Int/4",
			TestConstructorClass.class, 0, false);
		System.out.println("1st object: "+arrayToString(((PrologObject)obj.elementAt(0)).toPrologTable()));
		System.out.println("Testing object storage.  Storing the 10,000 objects in another kb...");
		p2.assertObjects("testobj",obj);
		System.out.println("Testing fast pattern retract. Applying sieve of Eratosthenes...");
		int lowest=2;
		while (lowest < 500) {
			if (p.queryToString("a("+lowest+")")!=null) {
				// remove all multiples of prime
				p.retract("a(X), ==(X mod "+lowest+",0)");
				// re-enter the prime in the db
				p.consult("a("+lowest+").");
			}
			lowest++;
		}
		System.out.println(resultsToString(p.queryToTable("a(X), X<500",0,false)));
		//p.assertStream(new BufferedReader(new FileReader("b")),
		//	0," ","bfile(",").");
		System.out.println("\nStarting interactive mode.");
		p.interactive(System.in, System.out);
	}

}

class TestConstructorClass implements PrologObject {
	boolean z; byte b; short s; int i; long l; 
	float f; double d;  char c; String str; String [] strarr;
	public TestConstructorClass(boolean z, byte b,short s,int i,long l,
	float f,double d, char c,String str, String [] strarr) {
		this.z=z; this.b=b; this.s=s; this.i=i; this.l=l;
		this.f=f; this.d=d; this.c=c; this.str=str; this.strarr=strarr;
		//System.out.println(z+" "+b+" "+s+" "+i+" "+l+" "+f+" "+d+" "+c+" "+str);
	}
	public String [] toPrologTable() {
		return new String [] {
			z+"", b+"",s+"",i+"",l+"", f+"", d+"", c+"", str,
			"["+YProlog.arrayToString(strarr)+"]"
		};
	}
}


