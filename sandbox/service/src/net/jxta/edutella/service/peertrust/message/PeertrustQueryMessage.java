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
package net.jxta.edutella.service.peertrust.message;

import net.jxta.edutella.service.message.ResolverMessage;
import net.jxta.protocol.PipeAdvertisement;

/**
 * ResolverMessage class for parsing and writing ResolverQueries.
 * 
 * $Id: PeertrustQueryMessage.java,v 1.1 2004/08/07 12:51:55 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:55 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public interface PeertrustQueryMessage extends ResolverMessage {

    public static final String DOCUMENT_ROOT_ELEMENT = "PeertrustQueryMessage";

    public static final String TAG_PREFIX = "net.jxta.edutella.service.peertrust.";

    /**
     * Returns the PipeAdvertisement.
     * 
     * @return the PipeAdvertisement
     */
    public PipeAdvertisement getPipeAdvertisement();

    /**
     * Sets the PipeAdvertisement.
     * 
     * @param pipeAdvertisement
     *            the PipeAdvertisment
     */
    public void setPipeAdvertisement(PipeAdvertisement pipeAdvertisement);

    /**
     * Indicates if the message is a broadcast or a direct message.
     * 
     * @return true if the message is a broadcast
     */
    public boolean isBroadcast();

    /**
     * Sets the broadcast indicator.
     * 
     * @param broadcast
     *            specifies if the message is a broadcast
     */
    public void setBroadcast(boolean broadcast);

    /**
     * Returns the query. Note: Only broadcast messages of this type directly
     * contain queries.
     * 
     * @return the query
     */
    public String getQuery();

    /**
     * Sets the query. Note: Only broadcast message of this type should contain
     * queries.
     * 
     * @param query
     *            the query
     */
    public void setQuery(String query);

}