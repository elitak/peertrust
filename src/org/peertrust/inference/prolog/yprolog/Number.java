package org.peertrust.inference.prolog.yprolog;



final class Number extends Term
{
    public Number( String s) {
        super(s,0);
        try {
            varid = Integer.parseInt(s);
        } catch (Exception e) 
            { varid = 0; }
    }
    public Number( int n) {
        super(Integer.toString(n),0);
         varid = n; 
    }

    public String toString() { return Integer.toString(varid);  }

    public int value() { return varid; }
    public int valueThrow() { return varid; }

    public  Term dup()    // to copy correctly CUT & Number terms
    {  
	return new Number( varid ); }
}
