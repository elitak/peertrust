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
package net.jxta.edutella.peertrust;

import net.jxta.edutella.peer.destination.JxtaDestination;
import net.jxta.edutella.service.peertrust.PeertrustService;
import net.jxta.edutella.service.peertrust.message.PeertrustMessage;

import org.apache.log4j.Logger;
import org.peertrust.net.Message;
import org.peertrust.net.NetClient;
import org.peertrust.net.Peer;
import org.peertrust.net.Query;

/**
 * Implementation of the Peertrust NetClient interface for use with Edutella.
 * 
 * $Id: PeertrustNetClient.java,v 1.1 2004/08/07 12:51:54 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:54 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public class PeertrustNetClient implements NetClient {

    private static final Logger log = Logger
            .getLogger(PeertrustNetClient.class);

    private PeertrustService service;

    /**
     * Constructs a new PeertrustNetClient. Normally called from an appropiate
     * factory class.
     * 
     * @param service
     *            a reference to the PeertrustService
     */
    public PeertrustNetClient(PeertrustService service) {
        log
                .debug("$Id: PeertrustNetClient.java,v 1.1 2004/08/07 12:51:54 magik Exp $");
        this.service = service;
    }

    /**
     * {@inheritDoc}
     */
    public void send(Message message, Peer destination) {
        log.debug("send()[Peer.alias: " + destination.getAlias()
                + ", Peer.address:" + destination.getAddress() + "]");
        if (message instanceof Query
                && destination.getAlias().equals("broadcast")) {
            Query query = (Query) message;
            service.sendMessage("" + query.getReqQueryId() + "$"
                    + query.getGoal());
            log.debug("send message via broadcast");
        } else {
            PeertrustMessage msg = new PeertrustMessage();
            msg.setObject(message);
            service.sendMessage(new JxtaDestination(destination.getAddress()),
                    msg);
            log.debug("send direct message");
        }
    }

}