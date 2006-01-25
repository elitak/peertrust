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
