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
package net.jxta.edutella.service.peertrust;

import net.jxta.edutella.peer.EdutellaService;
import net.jxta.edutella.peer.destination.Destination;
import net.jxta.edutella.service.peertrust.message.PeertrustMessage;
import net.jxta.service.Service;

/**
 * This interface specifies the PeertrustService for Edutella, which acts as a
 * kind of transportation layer for Peertrust.
 * 
 * $Id: PeertrustService.java,v 1.1 2004/08/07 12:51:56 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:56 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public interface PeertrustService extends EdutellaService, Service {

    /**
     * Sends a direct message through a pipe to a specific destination.
     * 
     * @param destination
     *            the Destination
     * @param message
     *            the PeertrustMessage
     */
    public void sendMessage(Destination destination, PeertrustMessage message);

    /**
     * Sends a broadcast message using pipes. Note: The mechanism used to
     * establish pipe communication for broadcasts incorporates a certain amount
     * of overhead, that is why this method is not being used in the current
     * version of Peertrust but it should be fully functional.
     * 
     * @param message
     *            the PeertrustMessage
     */
    public void sendMessage(PeertrustMessage message);

    /**
     * Sends a broadcast message via the ResolverService. In most cases this is
     * the prefered method for broadcasting but keep in mind that this is
     * insecure and limited to sending Strings.
     * 
     * @param message
     *            the String message
     */
    public void sendMessage(String message);

}