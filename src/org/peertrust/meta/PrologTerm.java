package org.peertrust.meta;

/**
 * $Id: PrologTerm.java,v 1.1 2004/07/01 23:35:58 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:35:58 $
 * @description
 */
public class PrologTerm
{
	static final byte VARIABLE = 0 ;
	static final byte ATOM = 1 ;
	static final byte LIST = 2 ;
	static final byte NUMBER = 3 ;
	
	private String name ;
	private byte status ;
	private String result ;
	
	public PrologTerm (String name, byte status)
	{
			this.name = name ;
			this.status = status ;
			switch (status)
			{
				case VARIABLE:
					this.result =null ; 
					break ;
				case ATOM:
					this.result = name ;
					break ;
			}
	}
	
	public void setName(String name)
	{
		this.name = name ;
	}
	
	public String getName()
	{
		return name ;
	}
	
	public void setStatus(byte status)
	{
		this.status = status ;
	}
	
	public byte getStatus()
	{
		return status ;
	}
	
	public void setResult(String result)
	{
		this.result = result ;	
	}
	
	public String getResult()
	{
		return result ;	
	}
}
