package org.peertrust.strategy;

import org.peertrust.meta.Tree;

/**
 * $Id: Queue.java,v 1.1 2004/07/01 23:38:29 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:38:29 $
 * @description
 */
public interface Queue
{
	// all the methods should by synchronized
	public void add (Tree tree) ;
	
	public void add (Tree [] trees) ;
	
	public Tree pop () ;
	
	public Tree getFirst () ;
	
	public Tree search (Tree pattern) ;
	
	public int update (Tree pattern, Tree tree) ;
	
	public int replace (Tree pattern, Tree tree) ;
	
	public Tree remove (Tree pattern) ;
	
}
