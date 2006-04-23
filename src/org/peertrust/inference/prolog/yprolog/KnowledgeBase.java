package org.peertrust.inference.prolog.yprolog;

import java.util.*; 
import java.io.*;

// addClauseFront adds clause at beginning of linked list rather than
// the end, for efficiency

///////////////////////////////////////////////////////////////////////
 public  class KnowledgeBase
///////////////////////////////////////////////////////////////////////
{
	// XXX get and set store strings -> Terms in ht, yuck
    Hashtable ht ;  /* "functor/arity" -> Clause */
    HashSet   primitives; /* "functor/arity" -> Clause */
    
    //String oldIndex;
    
    public KnowledgeBase() 
    {
        reset();
    }
        
    public Object get( Object key) 
            {   return ht.get(key); }
    
    public void put( Object key, Object value) 
            {   ht.put(key,value); }

    public void remove( Object key) 
            {   ht.remove(key); }
            
    public String toString() 
            {    return ht.toString(); }
    
    public Enumeration elements() 
            {    return ht.elements(); }

    public Enumeration keys() 
            {    return ht.keys(); }

            
    public void reset() 
    {
        ht = new Hashtable();
        primitives = new HashSet();    
        //oldIndex = "" ;      
    }
       
    public void consult( String fName ) 
	throws ParseException,
	       FileNotFoundException
    {
        //oldIndex = "" ;      
        new Parser( new FileReader(fName)).Program( this );
    }


	/** Merge other kb into this one.  Puts the clauses of the other db in
	* front of this one.  Can be used to efficiently add a small kb to a big
	* one.
	*/
	public void merge(KnowledgeBase kb) {
		for (Enumeration e=kb.ht.elements(); e.hasMoreElements(); ) {
			addClauseFront((Clause)e.nextElement());
		}
		if (kb.primitives.size() > 0) {
			throw new Error("kb may not contain primitives");
		}
	}


// ==========================================================
    public void addPrimitive( Clause clause)  
// ----------------------------------------------------------
    {
        Term term = clause.term;
        
        String index = term.getfunctor() + "/" + term.getarity();
        Clause c = (Clause) ht.get(index);
        if ( c!= null) 
        {
            while (c.nextClause != null) c = c.nextClause;
            c.nextClause = clause;
			clause.prevClause=c;
        }
        else {
            primitives.add( index );
            ht.put(index, clause);    
        }
    }

	/** Adds clause in front of the list of clauses instead of at the back.
	* (i.e. "addclausea" rather than "addclausez").
	*/
    public void addClauseFront(Clause clause)  {
// -----------------------------------------------------------
        Term term = clause.term;
        
        String index = term.getdbkey();
        if (primitives.contains(index))
        {
            System.out.println("Trying to modify primitive predicate: " + index);
            return;
        }
        else {
			// this code adds this clause and any clauses after it at the
			// beginning rather than the end.
			Clause c = (Clause) ht.get(index);
			Clause lastclause=clause;
			while (lastclause.nextClause != null) 
                lastclause = lastclause.nextClause;
			lastclause.nextClause = c;
			if (c!=null) c.prevClause=lastclause;
			ht.put(index,clause);
        }
    }

    public void addClause( Clause clause)  
// -----------------------------------------------------------
    {
        Term term = clause.term;
        
        String index = term.getdbkey();
        if (primitives.contains(index))
        {
            System.out.println("Trying to modify primitive predicate: " + index);
            return;
        }
        // this code simulated the wprolog "feature" which caused
		// new clauses to delete old ones
        //if (!oldIndex.equals(index)) {
        //    ht.remove( index );
        //    ht.put(index, clause);    
        //    oldIndex = index;
        //} else {
		Clause c = (Clause) ht.get(index);
		if (c==null) {
			ht.put(index,clause);
		} else {
			while (c.nextClause != null) 
			c = c.nextClause;
			c.nextClause = clause;
			clause.prevClause = c;
		}
    }

// ==========================================================
    public void assert_(Term term)  
// -----------------------------------------------------------------------
    {
        term = term.cleanUp();
        Clause newC = new Clause( term.deref(), null );
        
        String index = term.getfunctor() + "/" + term.getarity();
        if (primitives.contains(index))
        {
            IO.error("Assert","Trying to insert a primitive: " 
                  + index);
            return;
        }
            
        Clause c = (Clause) ht.get(index);
        if ( c!= null) 
        {
                while (c.nextClause != null) c = c.nextClause;
                c.nextClause = newC;
				newC.prevClause = c;
        }
        else {
                ht.put(index, newC);    
        }
    }

// ==========================================================
    public void asserta(Term term)  
// -----------------------------------------------------------------------
    {
        String index = term.getfunctor() + "/" + term.getarity();
        if (primitives.contains(index)){
            IO.error("Assert","Trying to insert a primitive: " + index);
            return;
        }

        term = term.cleanUp();
        Clause newC = new Clause( term.deref(), null);        
        Clause c = (Clause) ht.get(index);
        newC.nextClause = c;
		if (c!=null) c.prevClause=newC;
        ht.put(index, newC);
    }

// ==========================================================
    public boolean retract(Term term, Stack stack)  
// -----------------------------------------------------------------------
    {
        Clause newC = new Clause( term, null );
        String index = term.getfunctor() + "/" + term.getarity();
        if (primitives.contains(index)){
            IO.error("Retract","Trying to retract a primitive: " + index);
            return false;
        }
        Clause cc = null,
            c = (Clause) ht.get(index);

        while ( c!=null) 
        {
            Term vars[] = new Term[ Parser.maxVarnum ];
            Term xxx = c.term.refresh(vars);
            int top = stack.size();
            if (xxx.unify(term, stack))
            {
                if ( cc != null ) {
                    cc.nextClause = c.nextClause;
					if (c.nextClause!=null) c.nextClause.prevClause = cc;
                } else if ( c.nextClause==null )
                    ht.remove(index);
                else 
                    ht.put(index, c.nextClause);
                return true;
            }
            for (int i = stack.size()-top; i>0; i--)
            {
                Term t = (Term)stack.pop();
                t.unbind();
            }
            cc = c;
            c = c.nextClause;
        }
        return false;
    }
    
// ==========================================================
    public boolean retractall ( Term term, Term arity )  
// ----------------------------------------------------------
    {
	String key = term.getfunctor() + "/" + arity.getfunctor();
        if (primitives.contains(key)){
            IO.error("Retractall","Trying to retract a primitive: " + key);
            return false;
        }
	ht.remove( key );
	return true;
    }

// ==========================================================
    public Term get(Term key)  
// ----------------------------------------------------------
	{	
		return (Term) ht.get( key.toString() );
	}
	
// ==========================================================
    public void set(Term key, Term value)  
// ----------------------------------------------------------
	{	
		ht.put( key.toString(), value.cleanUp() );
	}

// =======================================================

    public void dump() { dump(false);}
    
    public void dump(boolean full) 
    {
        System.out.println();
        int i = 1;
        Enumeration e = ht.keys();
        while (e.hasMoreElements() )
        {
            Object key = e.nextElement();
            if (!full && primitives.contains(key)) 
                continue;
            Object val = ht.get(key);
            if (val instanceof Clause) 
            {
            	System.out.println(i++ + ". " + key + ": " );
				Clause head = (Clause) ht.get(key);
				do {
					System.out.print("    " + head.term);
					if (head.next != null) 
						System.out.print ( " :- " + head.next );
					System.out.println(".");
					head = head.nextClause;
				}
				while ( head != null );
			}
			else   // get/set pair
				System.out.println(i++ + ". " + 
				          key + " = " + val);
        }
        System.out.println();
    }

    public void list( Term term, Term arity) 
    {
        String key = term.getfunctor() + "/" + arity.getfunctor();
	System.out.println();
	System.out.println( key + ": " );
	Clause head = (Clause) ht.get(key);
	while ( head != null )
	    {
		System.out.print("    " + head.term);
		if (head.next != null) 
		    System.out.print ( " :- " + head.next );
		System.out.println(".");
		head = head.nextClause;
	    }
    }
}
