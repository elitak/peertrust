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

package org.peertrust.net;

/**
 * <p>
 * 
 * </p><p>
 * $Id: AbstractFactory.java,v 1.3 2005/05/22 17:56:44 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/05/22 17:56:44 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public interface AbstractFactory {
	
	public Peer getServerPeer (String alias) ;
	
	public NetClient createNetClient() ;
	
	public NetServer createNetServer() ;
}
