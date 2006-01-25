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

///////////////////////////////////////////////////////////////////////
   public class Term 
///////////////////////////////////////////////////////////////////////
{
    public static Term CUT = new Cut(0);
    //    public static Term CUT = new Term("!",0);

    static Random rnd = new Random();
    public static boolean trace = false;
    
    private String functor; 
    private int arity; 
    private Term args[];

	private String dbkey=null;

	// some accessor functions to allow examining term -- Boris
	// XXX use the new getarity and getfunctor here?
   	public Term [] getArgs() { return args; }
	public int getArity() { return arity; }
	public String getFunctor() { return functor; }
	public Term getReference() {
		if (deref) return ref;
		return null;
	}
 
    // private static int varnum=1;
    static int varnum=1;
    
    // If bound is false then term is a free variable
    private boolean bound; 
    protected int varid;
    
    // If bound is true and deref is true then the term is 
    // a reference to ``ref''
    private boolean deref; private Term ref;

    public Term deref() 
    {
        Term t = this;
        while (t.bound && t.deref) t=t.ref;
        return t;
    }


    public boolean bound() 
    {
        Term t = this;
        while (t.bound && t.deref) t=t.ref;
        return t.bound;
    }


/** Controls whether occurcheck is used in unification.
Note that in version 1.0 the occurcheck was always performed
which accounted for the lower performance.
*/
    static public boolean occurcheck = false;

/**
prettyprint controls printing of lists as <tt>[a,b]</tt>
rather than <tt>cons(a,cons(b,null))</tt>
*/
    static public boolean prettyprint = true;

/**
Controls whether predicates can begin with an underscore.
Beginning a system with an underscore makes in inaccessible 
to the user.
*/
    static public boolean internalparse = false;

/** create fresh var */
    public Term()
    { 
        varid = varnum++;  //  JV??? Necessary ???
        bound = false;
        deref = false;
    }

/** create var with specified varid */
    public Term(int i) { 
        varid = i;
        bound = false;
        deref = false;
    }

/** Used by dup() and new Cut() */
    protected Term(String s, int a,String dbk) {
        functor = s; arity = a; dbkey=dbk;
        bound = true; deref = false;
        args = new Term[arity];
    }

/** create a term with  a given functor and arity.
@param s - the functor
@param a - the arity
*/
    public Term(String s, int a) {
        functor = s; arity = a;
        bound = true; deref = false;
        args = new Term[arity];
		//dbkey = functor + "/" + arity;
    }

    public Term(String s, Term [] a) {
        functor = s; arity = a.length ;
        bound = true; deref = false;
        args = a;
		//dbkey = functor + "/" + arity;
    }

    public Term(String op, Term a1, Term a2)  // Binary Operator
    {
        functor = op; arity = 2;
        bound = true; deref = false;
        args = new Term[2];
        args[0] = a1;
        args[1] = a2;
		//dbkey = functor + "/2";
    }


/** Binds a variable to a term */
    public final void bind(Term t) 
    {
        if (this==t) return; // XXXX binding to self should have no effect
        if (!bound) {
            bound = true; deref = true;
            ref = t;
        } 
	else { 
	    error("Term.bind(" + this + ")" ,"Can't bind nonvar!"); 
	    new Throwable().printStackTrace();
	}
    }

/** Unbinds a term -- ie. resets it to a variable */
    public final void unbind() 
    {
        bound = false; ref=null;
    }

/** Used to set specific arguments. A primitive way of 
constructing terms is to create them with Term(s,f) and then 
build up the arguments. Using the parser is much simpler */
    final public void setarg(int pos,Term val) {
        // only to be used on bound terms
        if (bound & (!deref)) args[pos] = val;
        else  error("Term.setarg(" + pos + "," + val + ")",
                "Can't setarg on variables!");
    }

/** Retrieves an argument of a term */
    public final Term getarg(int pos) {
        // should check if pos is valid
        if (bound) {
            if (deref) {return ref.getarg(pos);}
            else {return args[pos];}
        } else {
            fatalerror("FATAL: Term.getarg", 
                      "Error - lookup on unbound term!");
            return null; // dummy ... never reached 
        }
    }

/** Gets the functor of a term */
    public final String getfunctor() {
        if (bound) {
            if (deref) {return ref.getfunctor();}
            else return functor;
        } else return "";
    }

/** Gets the arity of a term */
    public final int getarity() {
        if (bound) {
            if (deref) {return ref.getarity();}
            else return arity;
        } else return 0;
    }

/** Gets the dbkey of a term */
	public final String getdbkey() {
		if (bound) {
			if (deref) {return ref.getdbkey();}
			else {
				if (dbkey==null) dbkey = functor + "/" + arity;
				return dbkey;
			}
		} else return null;
	}

/** Compares term with other term non-numerically. Returns greater than 0
* if term is more than other term, 0 if equals, -1 otherwise. */
public int compareto(Term othert) {
	if (bound) {
		if (deref) {
			return ref.compareto(othert); 
		} else {
			return functor.compareTo(othert.getfunctor());
		}
	}
	// default: do full canonical expansion.  Warning: quotes are included in
	// lexical comparison!
	return toString().compareTo(othert.toString());
}

/** Checks whether a variable occurs in the term */
// XXXX Since a variable is not considered to occur in itself 
// XXXX added occurs1 and a new front end called occurs.
    final boolean occurs(int var) {
        if (varid==var) return false; 
        else return occurs1(var);
    }

    final boolean occurs1(int var) {
        if (bound) {
        if (deref) return ref.occurs1(var);
        else { // bound and not deref
                for (int i=0 ; i < arity ; i++) 
                    if (args[i].occurs1(var)) return true;
                return false;
            }
        } else // unbound
          return (varid==var);
    }

/** Unification is the basic primitive operation in logic programming. 
* @param s - the stack is used to store the addresses of variables which are 
bound by the unification. This is needed when backtracking.
*/
    final public boolean unify(Term t,Stack s) 
    {
        if (bound && deref) return ref.unify(t,s);
        if (t.bound && t.deref) return unify(t.ref,s);
        if (bound && t.bound) { // bound and not deref
            // we don't need to check for t.deref, so we can get functor,
			// arity, args directly instead of using the get functions.
            if (functor.equals(t.functor) && (arity==t.arity))
            {
                for (int i=0; i<arity ; i++) 
                    if (!args[i].unify(t.args[i],s)) 
                        return false;
                return true;

            } 
            else  
                return false; // functor/arity don't match ...

        }  // at least one arg not bound ...
        if (bound) {
        // return t.unify(this,s);
            // XXXX Added missing occur check
            if (occurcheck) {if (this.occurs(t.varid)) return false;}  // XXXX
            t.bind(this);
            s.push(t);
            return true;
        } 
        // Do occurcheck if turned on ...
        if (occurcheck) {if (t.occurs(varid)) return false;}
        this.bind(t);
        s.push(this); // save for backtracking
        return true;
    }

/** refresh creates new variables. If the variables allready exist in 
its argument then they are used - this is used when parsing a clause so 
that variables throught the clause are shared.  Includes a copy operation */

    public final Term refresh(Term l[]) 
    {
        Term t;
        if (bound) {
            if (deref) { 
                return ref.refresh(l);
            }
            else // bound & not deref

                if (arity==0) { 
                    return this;     //  JV Idea !!
                }
            else { // bound & not deref
                t = dup();
                // t = new Term(functor,arity);
                // t.bound = true; t.deref = false; 
                // t.functor = functor; t.arity = arity;
                for (int i=0;i<arity;i++) 
                    t.args[i] = args[i].refresh(l);
                return t;
            }
        } else //unbound
            return getvar(l,varid);
    }

    private final Term getvar(Term l[], int v) 
    {
        if (l[v]==null) 
            l[v] = new Term();
        return l[v];
    }

// ----------------------------------------------------------
//	Copy a term to put in the database
//	   - with new variables (freshly renumbered)
// ----------------------------------------------------------	
    static Hashtable cvdict = new Hashtable();
    static int       cvn;

    public final Term cleanUp() 
    {
    	cvdict.clear();
    	cvn = 0;
    	return this.cleanUp2();
    }
    
    public final Term cleanUp2() 
    {
        Term t;
        if (bound) 
        {
            if (deref) 
                return ref.cleanUp2();
            else if (arity==0) return this;    
            else { 
                t = dup();
                // t = new Term(functor,arity);
                for (int i=0;i<arity;i++) 
                    t.args[i] = args[i].cleanUp2();
            }
	} 
	else //unbound
        {
            t = (Term) cvdict.get( this );
			if (t==null) {
				t = new Term( cvn++ );
				cvdict.put( this, t);
			}
		}
		return t;
	}

// ----------------------------------------------------------	
    public  Term dup()    // to copy correctly CUT & Number terms
// ----------------------------------------------------------	
    { return new Term(functor,arity,dbkey); }

/** toString presents the term in a canonical way, which amounts to the
 * following:
 * (1) the resulting string is parseable by the Prolog parser
 * (2) a term which is identical to another, will produce the same string.
 *     This includes unified variables: variables are represented by X[varid].
 */
    public  String toString()  {
	// XXX what about quote escapes?
        String s;
        if (bound) {
          // if (deref) return "v" + varid + "->" + ref.toString();
          if (deref) return ref.toString();
          else {
            if (functor.equals("null") & arity==0 & prettyprint)
                return "[]";
            if (functor.equals("cons") & arity==2 & prettyprint) {
                Term t; 
                s = "[" + args[0];
                t = args[1];

                while (t.getfunctor().equals("cons") &
                    t.getarity() == 2) {
                    s = s + "," + t.getarg(0);
                    t = t.getarg(1);
                }

                if (t.getfunctor().equals("null") &
                    t.getarity() == 0) 
                    s = s + "]";
                else s = s + "|" + t + "]";

                return s;
            } else {
                if (arity == 0) {
                    s = YProlog.addQuotes(functor,true);
                } else {
                    s = YProlog.addQuotes(functor,false) + "(";
                    for (int i=0; i < (arity - 1); i++) 
                        s =s + args[i].toString() + ",";
                    s = s + args[arity-1].toString() + ")";
                }
            }
            return s;
          }
        } else return ("X" + varid);
    }

	public int value() {
		try {
			return valueThrow();
		} catch (ArithmeticException e) {
			IO.error("Term.value",e.getMessage());
			return 0;
		}
	}

	// version of value() that throws an exception when it fails -- Boris
    public int valueThrow() throws ArithmeticException {
	int i, res = 0;

	if (!bound) throw new ArithmeticException("unbound term");
	else if (deref) return ref.valueThrow();
	else if (functor == "rnd" && arity==1) 
	    return rnd.nextInt( args[0].valueThrow() ); 
	else if (arity<2) 
	    throw new ArithmeticException("numeric expression not binary");
	else if (functor == "+") 
	    return args[0].valueThrow()+args[1].valueThrow(); 
	else if (functor == "-") 
	    return args[0].valueThrow()-args[1].valueThrow(); 
	else if (functor == "*") 
	    return args[0].valueThrow()*args[1].valueThrow(); 
	else if (functor == "/") 
	    return args[0].valueThrow()/args[1].valueThrow(); 
	else if (functor == "mod") 
	    return args[0].valueThrow() % args[1].valueThrow(); 
	else 
	    throw new ArithmeticException("unknown numeric operator: " + functor);

    }

    public boolean isBound() { return bound(); }

    public final static void error(String caller,String mesg) {
        System.out.print(
            "ERROR: in " + caller + " : " + mesg + "\n");
    }
    
    public final static void fatalerror(String caller,String mesg) {
        System.out.print(
            "FATAL ERROR: in " + caller + " : " + mesg + "\n");
        System.exit(1);
    }

    static void traceln(String msg ) {
        if (trace) System.out.println(msg);
    }

    public String dump() {
	return " - Term: " + functor + "/" + arity + ", "
            + (bound? "bound, " : "")
            + (deref? "ref, " : "")
            + varid ;
    }

}

    
