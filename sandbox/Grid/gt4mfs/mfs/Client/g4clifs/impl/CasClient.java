/*
 * Created on Jul 9, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4clifs.impl;

import g4mfs.impl.gridpeertrust.wrappers.CasWrapper;

/**
 * @author ionut
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CasClient 
{
	public static void main(String[] args)
	{
		CasWrapper cw = new CasWrapper("https://127.0.0.1:8443/wsrf/services/CASService","/O=Grid/OU=GlobusTest/OU=simpleCA-ionucomp/CN=host/ionucomp");
		//String filename = cw.retrieveProxy();
		//System.out.println("CasClient rezultat"+filename);
		cw.retrieveSAMLAssertion();
	}
	
	
}
