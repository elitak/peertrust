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
import net.jxta.edutella.util.Configurator;

import org.apache.log4j.Logger;
import org.peertrust.inference.MinervaProlog;
import org.peertrust.meta.MetaInterpreter;
import org.peertrust.strategy.FIFOQueue;

/**
 * Implementation of the PeertrustProcessor. This class manages the connection
 * between Edutella and the "standalone" Peertrust code.
 * 
 * $Id: PeertrustProcessorImpl.java,v 1.1 2004/08/07 12:51:54 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:54 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public class PeertrustProcessorImpl implements PeertrustProcessor {

    private static final Logger log = Logger
            .getLogger(PeertrustProcessorImpl.class);

    /**
     * Constructs a new PeertrustProcessor and the needed core objects from
     * Peertrust, like the MetaInterpreter.
     * 
     * @param service
     *            a reference to the PeertrustService
     */
    public PeertrustProcessorImpl(PeertrustService service) {
        log
                .debug("$Id: PeertrustProcessorImpl.java,v 1.1 2004/08/07 12:51:54 magik Exp $");
        try {
            Configurator cf = new Configurator("trust.properties",
                    new String[0]);
            cf.setAppInfo("Automated Trust Negotiation Peer");
            MinervaProlog engine = new MinervaProlog(cf);
            cf.register(engine);
            MetaInterpreter metaI = new MetaInterpreter(new FIFOQueue(),
                    engine, cf, new PeertrustNetFactory(service));
            cf.register(metaI);
            cf.finishConfig();
        } catch (Exception e) {
            log.warn("Exception", e);
        }
    }

}