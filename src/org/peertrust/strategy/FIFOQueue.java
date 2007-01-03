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

import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.meta.Tree;

/**
 * <p>
 * Specific queue with a simple FIFO (First In First Out) strategy.
 * </p><p>
 * $Id: FIFOQueue.java,v 1.6 2007/01/03 12:31:02 jldecoi Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2007/01/03 12:31:02 $
 * by $Author: jldecoi $
 * </p>
 * @author olmedilla
 */
public class FIFOQueue implements Queue, Configurable
{
	Vector _queue ;
	
	/**
	 * @see org.peertrust.strategy.Queue#addLocal(trust.meta.Tree)
	 */
	public synchronized void add(Tree tree)
	{
		_queue.add(tree) ;
	}


	/* (non-Javadoc)
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {
		_queue = new Vector() ;
	}
	
	/**
	 * @see org.peertrust.strategy.Queue#addLocal(trust.meta.Tree)
	 */
	public synchronized void add(Tree[] trees)
	{
		for (int i = 0 ; i < trees.length ; i++)
			_queue.add(trees[i]) ;
	}

	/**
	 * @see org.peertrust.strategy.Queue#pop()
	 */
	synchronized public Tree pop()
	{
		Tree tmp ;
		for (int i = 0 ; i < _queue.size() ; i++)
		{
			tmp = (Tree) _queue.elementAt(i) ;
			if (tmp.isProcessable())
			{
				_queue.remove(i) ;
				return tmp ;
			}
			else
				if (tmp.isOutDated())
					_queue.remove(i) ;
		}
			
		return null ;
	}

	/**
	 * @see org.peertrust.strategy.Queue#getFirst()
	 */
	public synchronized Tree getFirst()
	{
		Tree tmp ;
		for (int i = 0 ; i < _queue.size() ; i++)
		{
			tmp = (Tree) _queue.elementAt(i) ;
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
		int index = _queue.indexOf(pattern) ;
		if (index == -1)
			return null ;
		else
			return (Tree) _queue.elementAt(index) ; 
	}

	/**
	 * @see org.peertrust.strategy.Queue#update(trust.meta.Tree, trust.meta.Tree)
	 */
	public synchronized int update(Tree pattern, Tree newTree)
	{
		int index = _queue.indexOf(pattern) ;
		if (index == -1)
			return -1 ;
		else {
			Tree tree = (Tree) _queue.elementAt(index) ;
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
		int index = _queue.indexOf(pattern) ;
		if (index == -1)
			return -1 ;
		else {
			_queue.setElementAt(newTree, index) ;
			return 0 ;
		}
	}
	
	public synchronized Tree remove (Tree pattern) 
	{
		int index = _queue.indexOf(pattern) ;
		if (index == -1)
			return null ;
		else
			return (Tree) _queue.remove(index) ;
	}
	
	public String toString()
	{
		StringBuffer message = new StringBuffer("QUEUE with " + _queue.size() + " elements\n") ;
		for (int i = 0 ; i < _queue.size() ; i++)
			message.append("Element " + i + ": " + _queue.elementAt(i) + "\n") ;
		
		return message.toString() ;
	}
}
