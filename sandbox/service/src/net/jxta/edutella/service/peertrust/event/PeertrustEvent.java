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
package net.jxta.edutella.service.peertrust.event;

import net.jxta.edutella.component.event.AbstractEvent;
import net.jxta.edutella.service.peertrust.message.PeertrustMessage;

/**
 * Event for the communication between the PeertrustProcessor and the
 * PeertrustNetServer.
 * 
 * $Id: PeertrustEvent.java,v 1.1 2004/08/07 12:51:56 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:56 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public class PeertrustEvent extends AbstractEvent {

    private String sender;

    private PeertrustMessage message;

    /**
     * Constructs a new PeertrustEvent.
     * 
     * @param source
     *            the source of the event
     */
    public PeertrustEvent(Object source) {
        super(source);
    }

    /**
     * Returns the sender.
     * 
     * @return the sender
     */
    public String getSender() {
        return sender;
    }

    /**
     * Sets the sender.
     * 
     * @param sender
     *            the sender
     */
    public void setSender(String sender) {
        this.sender = sender;
    }

    /**
     * Returns the PeertrustMessage.
     * 
     * @return the PeertrustMessage
     */
    public PeertrustMessage getMessage() {
        return message;
    }

    /**
     * Sets the PeertrustMessage.
     * 
     * @param message
     *            the PeertrustMessage
     */
    public void setMessage(PeertrustMessage message) {
        this.message = message;
    }
}