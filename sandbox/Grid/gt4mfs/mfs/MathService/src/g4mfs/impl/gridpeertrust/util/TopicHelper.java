/*
 * Created on Aug 27, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.util;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * used for creating a unique topic for the client
 */
public class TopicHelper 
{

	// get a unique value for this client -> the hash code fo his DN and his certificate issuer -> a topic for this client might already exist
	public static String getUniqueLocal(String subjectDN,String issuerDN)
	{
		String str = subjectDN+issuerDN;
		int hash = str.hashCode();
		return (new Integer(hash)).toString();
	}
}
