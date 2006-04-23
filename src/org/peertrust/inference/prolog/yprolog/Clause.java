package org.peertrust.inference.prolog.yprolog;


///////////////////////////////////////////////////////////////////////
public final class Clause extends TermList
///////////////////////////////////////////////////////////////////////
{

/*    public Clause(Term t) 
    {
        super(t, null);
    }
*/
	/** body=null indicates the clause is a fact. */
    public Clause(Term t, TermList body) 
    {
        super(t, body);
    }


    public final String toString() {
		if (next!=null) {
			return term + " :- " + next + ". ";
		} else {
			return term + ". ";
		}
    }
}
