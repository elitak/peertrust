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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
*/

package org.policy.util;

import org.peertrust.exception.ConfigurationException;

/**
 * <p>
 * 
 * </p><p>
 * $Id: ThreadTester.java,v 1.1 2007/02/17 16:59:28 dolmedilla Exp $
 * <br/>
 * Date: Feb 15, 2007
 * <br/>
 * Last changed: $Date: 2007/02/17 16:59:28 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class ThreadTester
{
	
	public ThreadTester ()
	{
		
	}
	
	public static void main(String[] args)
	{
		ThreadTester t = new ThreadTester () ;
		AnotherThread objectOne = t.new AnotherThread () ;
		NewThread objectTwo = t.new NewThread (objectOne) ;
		
		Thread one = new Thread (objectOne) ;
		Thread two = new Thread (objectTwo) ;
		
		one.start() ;
		two.start() ;
	}

	public class AnotherThread implements Runnable
	{
		int i = 0 ;
		
		public AnotherThread ()
		{
			
		}

		public void print (String s, boolean b)
		{
			synchronized (this)
			{
				System.out.println (s + i) ;
				i++ ;
			
			if (b)
				try
				{
					Thread.sleep (10000) ;
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			if (b)
				try
				{
					Thread.sleep (100) ;
				} catch (InterruptedException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		
		public void run()
		{
			for (int j = 0 ; j < 1000 ; j++)
			{
				/* synchronized (this)
				{
					System.out.println ("run " + i) ;
					i++ ;
				}*/
				print("AnotherThread ", false) ;
			}			
		}
	}
		
	public class NewThread implements Runnable
	{
		AnotherThread _t ;
		public NewThread (AnotherThread t)
		{
			_t = t ;
		}

		public void run()
		{
			for (int j = 0 ; j < 10 ; j++)
			{
				_t.print("NewThread ", true) ;
			}			
			
		}
	}
}
