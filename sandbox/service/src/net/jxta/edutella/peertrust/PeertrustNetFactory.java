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

import net.jxta.edutella.service.peertrust.PeertrustService;
import net.jxta.edutella.service.peertrust.event.PeertrustEvent;
import net.jxta.edutella.util.Configurator;

import org.apache.log4j.Logger;
import org.peertrust.net.AbstractFactory;
import org.peertrust.net.NetClient;
import org.peertrust.net.NetServer;

/**
 * Implementation of the Peertrust AbstractFactory interface for use with Edutella.
 * 
 * $Id: PeertrustNetFactory.java,v 1.1 2004/08/07 12:51:54 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:54 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public class PeertrustNetFactory implements AbstractFactory {

    private static final Logger log = Logger
            .getLogger(PeertrustNetFactory.class);

    private PeertrustService service;

    /**
     * Constructs a new PeertrustNetFactory.
     * 
     * @param service
     *            a reference to the PeertrustService
     */
    public PeertrustNetFactory(PeertrustService service) {
        log
                .debug("$Id: PeertrustNetFactory.java,v 1.1 2004/08/07 12:51:54 magik Exp $");
        this.service = service;
    }

    /**
     * {@inheritDoc}
     */
    public NetClient createNetClient(Configurator config) {
        log.debug("createNetClient()");
        return new PeertrustNetClient(service);
    }

    /**
     * {@inheritDoc}
     */
    public NetServer createNetServer(Configurator config) {
        log.debug("createNetServer()");
        PeertrustNetServer server = new PeertrustNetServer();
        service.register(server, PeertrustEvent.class);
        return server;
    }
}