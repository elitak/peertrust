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

package g4mfs.impl.org.peertrust.net;

import g4mfs.impl.org.peertrust.meta.Trace;

import java.io.Serializable;




/**
 * <p>
 * 
 * </p><p>
 * $Id: Message.java,v 1.1 2005/11/30 10:35:10 ionut_con Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:10 $
 * by $Author: ionut_con $
 * </p>
 * @author olmedilla 
 */
public class Message implements Serializable {
	
 	Peer _source, _target ;
 	Trace _trace ;

 	public Message (Peer source, Peer target) {
 		this (source, target, null) ;
 	}
 	
 	public Message (Peer source, Peer target, Trace trace) {
 		_source  = source ;
 		_target = target ;
 		
 		if (trace == null)
 			this._trace = new Trace() ;
 		else
 			this._trace = trace ;
 	}
 	
	/**
	 * @return Returns the source.
	 */
	public Peer getSource() {
		return _source;
	}
	
	/**
	 * @return Returns the target.
	 */
	public Peer getTarget() {
		return _target;
	}
	
	public Trace getTrace()
	{
		return _trace ;
	}
	
	public String toString()
	{
		
		String str = "source = "+_source.toString()+" target = "+_target.toString()+"trace = "+_trace;
		if(this instanceof Answer) str = str + " proof "+( (Answer) this).getProof();
		else str = str + " no proof ";
		return str;//"source = "+_source.toString()+" target = "+_target.toString()+"trace "+_trace;
	}
 }
