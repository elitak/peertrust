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

import net.jxta.edutella.service.peertrust.message.PeertrustMessage;

/**
 * Class storing delayed broadcast messages. Stored messages are sent to every
 * Peer responding within specified timeout.
 * 
 * $Id: QueryEntry.java,v 1.1 2004/08/07 12:51:56 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:56 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public class QueryEntry {

    private PeertrustMessage message;

    private long time;

    /**
     * Constructs a new QueryEntry.
     */
    public QueryEntry() {
        message = null;
        time = -1;
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

    /**
     * Returns the time.
     * 
     * @return the time.
     */
    public long getTime() {
        return time;
    }

    /**
     * Sets the time.
     * 
     * @param time
     *            the time
     */
    public void setTime(long time) {
        this.time = time;
    }

}