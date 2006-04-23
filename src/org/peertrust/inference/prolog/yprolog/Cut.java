package org.peertrust.inference.prolog.yprolog;



final class Cut extends Term      
//-------------------------------
{    
    
    public Cut( int stackTop ) 
    {
        super("!",0,"!/0");
        varid = stackTop;
    }    
    public String toString() 
    {
		return "!";
        //return "Cut->" + varid ;
    }

    public  Term dup()    // to copy correctly CUT & Number terms
    {  
       return new Cut( varid ); }
}
