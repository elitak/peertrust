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

import net.jxta.edutella.component.event.EdutellaEvent;
import net.jxta.edutella.service.peertrust.event.PeertrustEvent;
import net.jxta.edutella.service.peertrust.event.PeertrustEventListener;
import net.jxta.edutella.service.peertrust.message.PeertrustMessage;

import org.apache.log4j.Logger;
import org.peertrust.net.Answer;
import org.peertrust.net.Message;
import org.peertrust.net.NetServer;
import org.peertrust.net.Peer;
import org.peertrust.net.Query;

/**
 * Implementation of the Peertrust NetServer interface for use with Edutella.
 * 
 * $Id: PeertrustNetServer.java,v 1.1 2004/08/07 12:51:54 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:54 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public class PeertrustNetServer implements NetServer, PeertrustEventListener {

    private static final Logger log = Logger
            .getLogger(PeertrustNetServer.class);

    private Message message = null;

    /**
     * Constructs a new PeertrustNetServer. Usually called through a factory
     * class.
     */
    public PeertrustNetServer() {
        log
                .debug("$Id: PeertrustNetServer.java,v 1.1 2004/08/07 12:51:54 magik Exp $");
    }

    /**
     * {@inheritDoc}
     */
    public synchronized Message listen() {
        log.debug("listen()");
        try {
            while (message == null) {
                log.debug("listen(): waiting...");
                wait();
            }
        } catch (InterruptedException e) {
            log.error("Exception", e);
        }
        log.debug("listen(): processing message...");
        Message ret = message;
        message = null;
        notify();
        if (ret instanceof Answer)
            log.debug("listen(): new answer received: "
                    + ((Answer) ret).getGoal() + " - status:"
                    + ((Answer) ret).getStatus());
        return ret;
    }

    /**
     * @param event
     *            EdutellaEvent containing a message received from
     *            PeertrustService
     */
    public synchronized void event(EdutellaEvent event) {
        log.debug("event()");
        if (event instanceof PeertrustEvent) {
            PeertrustEvent pEvent = (PeertrustEvent) event;
            PeertrustMessage msg = pEvent.getMessage();
            try {
                while (this.message != null) {
                    log.debug("event(): waiting...");
                    wait();
                }
            } catch (InterruptedException e) {
                log.error("Exception", e);
            }
            Object o = msg.getObject();
            if (o instanceof Message) {
                this.message = (Message) o;
                this.message.getOrigin().setAlias(pEvent.getSender());
                this.message.getOrigin().setAddress(pEvent.getSender());
            } else if (o instanceof String) {
                String str = (String) o;
                int seppos = str.indexOf("$");
                long queryId = Long.parseLong(str.substring(0, seppos));
                String goal = str.substring(seppos + 1);
                this.message = new Query(goal, new Peer(pEvent.getSender(),
                        pEvent.getSender()), queryId);
            } else {
                log.error("event(): unknown object type");
            }
            log.debug("event(): received Message: " + this.message.toString());
            notify();
        }
    }

}