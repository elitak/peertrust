/*
 * Created on May 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.server;

import org.globus.wsrf.impl.security.authorization.exceptions.AuthorizationException;

/**
 * @author ionut
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MyAuthorizationException extends AuthorizationException 
{

	String reason;
	
	public MyAuthorizationException(String str)
	{
		super(str);
	}

	public void setReason(String reason)
	{
		this.reason = reason;
	}
	
	public String getReason()
	{
		return reason;
	}
	
}
