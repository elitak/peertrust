package org.peertrust.inference.prolog.yprolog;

// fixed uninitialised variable:
// nextClause is now explicitly initialised at null

///////////////////////////////////////////////////////////////////////
public class TermList 
///////////////////////////////////////////////////////////////////////
{
    Term term;
    TermList next = null;
    Clause prevClause=null;  // links back to previous clause in database
    Clause nextClause=null;// serves 2 purposes: either links clauses in database 
                           //   or points to defining clause for goals

	String term_dbkey=null;

	// We want to examine the term -- Boris
	public Term getTerm() { return term; }

    public TermList() {}        // for Clause

	public TermList(Term t) {
		term = t; 
		term_dbkey = t.getdbkey();
	}
    
	public TermList(Term t, TermList n) {
		term = t; next = n;
		term_dbkey = t.getdbkey();
	}


    public String toString() 
    {
	//int i=0;
        String s; TermList tl;
        s = term.toString();
        //s = new String("[" + term.toString());
        tl = next;
        while (tl != null) {
        //while (tl != null && ++i < 3) {
            s = s + ", " + tl.term.toString();
            tl = tl.next;
        }
	if(tl!=null) s += ",....";
        //s += "]";

        return s ;
    }

	public void resolve(KnowledgeBase db) {
		nextClause = (Clause) db.get(term_dbkey);
		//nextClause = (Clause) db.get( term.getfunctor() 
		//                              + "/" + term.getarity() );
	}
	public void lookupIn(KnowledgeBase db) {
		nextClause = (Clause) db.get(term_dbkey);
		//nextClause = (Clause) db.get( term.getfunctor() 
		//                                  + "/" + term.getarity() );
	}

}
