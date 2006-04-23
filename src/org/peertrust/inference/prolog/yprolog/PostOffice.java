package org.peertrust.inference.prolog.yprolog;

// Changed agents in list to weak references to ensure no memory leaks occur
// when repeatedly creating and disposing Engines.  This does mean that the
// agent is deleted from the list when there is no other code referencing it.
//
// Separated Message class

/////////////////////////////////////////////////////
//
//  PostOffice.java:  Dispatcher for messages between Prolog Agents.
//  ================
//  Author: J. Vaucher (vaucher@iro.umontreal.ca)
//  Date: 2002/5/10
//  URL:  www.iro.umontreal.ca/~vaucher/~XProlog
//

import java.util.*;
import java.lang.ref.WeakReference;

public class PostOffice {

	static ArrayList agents = new ArrayList();

	public static int register ( List l )
	{
		agents.add( new WeakReference(l) );
		return agents.size()-1;
	}
		
	public static void send( int src, int dest, Term msg ) 
	{
		if ( dest>=agents.size()) return;
		Message m = new Message(src, msg);
		List l = (List) ((WeakReference)agents.get(dest)).get();
		l.add( m ); // throws NullPointerException if l became null
	}

	public static void broadcast( int src, Term msg ) 
	{
		Message m = new Message(src, msg);

		for (int i = 0; i<agents.size(); i++) {
			List l = (List) ((WeakReference)agents.get(i)).get();
			if (l!=null) l.add( new Message(src, msg) );
		}
	}

}


