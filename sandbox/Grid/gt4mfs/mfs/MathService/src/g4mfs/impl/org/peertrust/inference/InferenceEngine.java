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

package g4mfs.impl.org.peertrust.inference;

import g4mfs.impl.org.peertrust.exception.ConfigurationException;
import g4mfs.impl.org.peertrust.exception.InferenceEngineException;
import g4mfs.impl.org.peertrust.meta.Tree;

import java.applet.Applet;


/**
 * <p>
 * Any class querying an inference engine in the system must implement this interface.
 * </p><p>
 * $Id: InferenceEngine.java,v 1.1 2005/11/30 10:35:13 ionut_con Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed:  $Date: 2005/11/30 10:35:13 $
 * by $Author: ionut_con $
 * </p>
 * @author olmedilla
 */
public interface InferenceEngine
{
	public void init() throws ConfigurationException ;
	public LogicAnswer [] processTree (LogicQuery query) throws InferenceEngineException ;
	public void unifyTree (Tree tree, String newQuery) throws InferenceEngineException ;
	public boolean execute (String query) throws InferenceEngineException ;
	
	public void insert (String clause) throws InferenceEngineException ;
	public void setDebugMode (boolean debug) throws InferenceEngineException ;
	public void setApplet (Applet applet) throws InferenceEngineException ;
}