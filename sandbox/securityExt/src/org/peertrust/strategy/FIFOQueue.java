/**
 * Copyright 2004
 * 
 * This file is part of Peertrust.
 * 
 * Peertrust is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Peertrust is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Peertrust; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.peertrust.strategy;

import java.util.Vector;

import org.peertrust.meta.Tree;

/**
 * $Id: FIFOQueue.java,v 1.1 2004/12/21 11:09:37 seb0815 Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/12/21 11:09:37 $
 * by $Author: seb0815 $
 * @description
 */
public class FIFOQueue implements Queue
{
	Vector queue = new Vector() ;
	
	/**
	 * @see org.peertrust.strategy.Queue#add(trust.meta.Tree)
	 */
	public synchronized void add(Tree tree)
	{
		queue.add(tree) ;
	}

	/**
	 * @see org.peertrust.strategy.Queue#add(trust.meta.Tree)
	 */
	public synchronized void add(Tree[] trees)
	{
		for (int i = 0 ; i < trees.length ; i++)
			queue.add(trees[i]) ;
	}

	/**
	 * @see org.peertrust.strategy.Queue#pop()
	 */
	synchronized public Tree pop()
	{
		Tree tmp ;
		for (int i = 0 ; i < queue.size() ; i++)
		{
			tmp = (Tree) queue.elementAt(i) ;
			if (tmp.isProcessable())
			{
				queue.remove(i) ;
				return tmp ;
			}
			else
				if (tmp.isOutDated())
					queue.remove(i) ;
		}
			
		return null ;
	}

	/**
	 * @see org.peertrust.strategy.Queue#getFirst()
	 */
	public synchronized Tree getFirst()
	{
		Tree tmp ;
		for (int i = 0 ; i < queue.size() ; i++)
		{
			tmp = (Tree) queue.elementAt(i) ;
			if (tmp.isProcessable())
				return tmp ;
		}
			
		return null ;
	}

	/**
	 * @see org.peertrust.strategy.Queue#search(trust.meta.Tree)
	 */
	public synchronized Tree search(Tree pattern)
	{
		int index = queue.indexOf(pattern) ;
		if (index == -1)
			return null ;
		else
			return (Tree) queue.elementAt(index) ; 
	}

	/**
	 * @see org.peertrust.strategy.Queue#update(trust.meta.Tree, trust.meta.Tree)
	 */
	public synchronized int update(Tree pattern, Tree newTree)
	{
		int index = queue.indexOf(pattern) ;
		if (index == -1)
			return -1 ;
		else {
			Tree tree = (Tree) queue.elementAt(index) ;
			tree.update(newTree) ;
			//queue.setElementAt(tree, index) ;		
			return 0 ;
		}  
		  
	}

	/**
	 * @see org.peertrust.strategy.Queue#replace(trust.meta.Tree, trust.meta.Tree)
	 */
	public synchronized int replace(Tree pattern, Tree newTree)
	{
		int index = queue.indexOf(pattern) ;
		if (index == -1)
			return -1 ;
		else {
			queue.setElementAt(newTree, index) ;
			return 0 ;
		}
	}
	
	public synchronized Tree remove (Tree pattern) 
	{
		int index = queue.indexOf(pattern) ;
		if (index == -1)
			return null ;
		else
			return (Tree) queue.remove(index) ;
	}

	public int getSize() {
		return queue.size();
	}
}
