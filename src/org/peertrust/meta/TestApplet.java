package org.peertrust.meta;

import java.awt.HeadlessException;

import javax.swing.JApplet;

/**
 * $Id: TestApplet.java,v 1.1 2004/07/01 23:35:58 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:35:58 $
 * @description
 */
public class TestApplet extends JApplet
{

	/**
	 * @throws java.awt.HeadlessException
	 */
	public TestApplet() throws HeadlessException
	{
		super();
	}
	
	public void init()
	{
		System.out.println("init") ;
	}

	public void start()
		{
			System.out.println("start") ;
		}
	
	public void stop()
		{
			System.out.println("stop") ;
		}
		
	public void destroy()
		{
			System.out.println("destroy") ;
		}
}
