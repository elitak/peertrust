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

package org.peertrust.inference;

import java.applet.Applet;

import org.peertrust.exception.ConfigurationException;
import org.peertrust.exception.InferenceEngineException;
import org.peertrust.meta.Proof;
import org.peertrust.meta.Tree;
import org.peertrust.net.Peer;

/**
 * <p>
 * Any class querying an inference engine in the system must implement this interface.
 * </p><p>
 * $Id: InferenceEngine.java,v 1.6 2006/01/25 16:07:45 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed:  $Date: 2006/01/25 16:07:45 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */
public interface InferenceEngine
{
	public void init() throws ConfigurationException ;
	public LogicAnswer [] processTree (LogicQuery query) throws InferenceEngineException ;
	public void unifyTree (Tree tree, String newQuery) throws InferenceEngineException ;
	public boolean execute (String query) throws InferenceEngineException ;
	public boolean validate(String Goal,Peer prover,Proof proof) throws InferenceEngineException ;
	public void consultFile(String fileName) throws InferenceEngineException ;
	
	public void insert (String clause) throws InferenceEngineException ;
	public void setDebugMode (boolean debug) throws InferenceEngineException ;
	public void setApplet (Applet applet) throws InferenceEngineException ;
}