/*
 * Created on 08-Dec-2003
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.peertrust.inference;

import org.peertrust.meta.Tree;

/**
 * $Id: InferenceEngine.java,v 1.1 2004/07/01 23:35:28 dolmedilla Exp $
 * @author $Author: dolmedilla $
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/01 23:35:28 $
 * @description
 */
public interface InferenceEngine
{
	public LogicAnswer [] processTree (LogicQuery query) ;
	public void unifyTree (Tree tree, String newQuery) ;
	public boolean execute (String query) ;
}