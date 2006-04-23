package org.peertrust.inference.prolog.yprolog;

/////////////////////////////////////////////////////
//
//  PostOffice.java:  Dispatcher for messages between Prolog Agents.
//  ================
//  Author: J. Vaucher (vaucher@iro.umontreal.ca)
//  Date: 2002/5/10
//  URL:  www.iro.umontreal.ca/~vaucher/~XProlog
//
// Separated Message class

import java.util.*;

class Message 
{
    int source;
    Term content;

    public Message(int s, Term m)
    {
	source = s;
	content = m;
    }
    public String toString(){
	return source + ":" + content ;
    }
}
