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

import java.net.MalformedURLException;
import java.net.UnknownServiceException;

import net.jxta.edutella.component.peergroup.EdutellaPeerGroupImpl;
import net.jxta.edutella.peer.PeerServiceRegistry;
import net.jxta.edutella.service.peertrust.PeertrustService;
import net.jxta.exception.ServiceNotFoundException;
import net.jxta.id.IDFactory;
import net.jxta.platform.ModuleClassID;

import org.apache.log4j.Logger;

/**
 * Implementation of the Edutella peergroup for Peertrust peers.
 * 
 * $Id: PeertrustPeerGroupImpl.java,v 1.1 2004/08/07 12:51:54 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:54 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public class PeertrustPeerGroupImpl extends EdutellaPeerGroupImpl implements
        PeertrustPeerGroup {

    private static final Logger log = Logger
            .getLogger(PeertrustPeerGroupImpl.class);

    private PeertrustService peertrustService;

    private String peertrustServiceResource;

    /**
     * Constructs a new PeertrustPeerGroupImpl object.
     */
    public PeertrustPeerGroupImpl() {
        log
                .debug("$Id: PeertrustPeerGroupImpl.java,v 1.1 2004/08/07 12:51:54 magik Exp $");
    }

    /**
     * {@inheritDoc}
     */
    public PeertrustService getPeertrustService() {
        log.debug(".getPeertrustService()");

        if (this.peertrustService != null) {
            return (this.peertrustService);
        }
        try {
            String moduleClassId = PeerServiceRegistry.getSystemBuilder()
                    .getServiceId(this.getPeertrustServiceResource());
            ModuleClassID moduleClassID = (ModuleClassID) IDFactory
                    .fromURL(IDFactory.jxtaURL(moduleClassId));
            this.peertrustService = (PeertrustService) this
                    .lookupService(moduleClassID);
        } catch (ServiceNotFoundException snfe) {
            log.error("ServiceNotFoundException: ", snfe);
            PeerServiceRegistry.fatal();
        } catch (MalformedURLException murle) {
            log.error("MalformedURLException: ", murle);
            PeerServiceRegistry.fatal();
        } catch (UnknownServiceException use) {
            log.error("UnkownServiceExcetpion: ", use);
            PeerServiceRegistry.fatal();
        }
        return (this.peertrustService);
    }

    /**
     * Returns the PeertrustServiceResource.
     * 
     * @return the PeertrustServiceResource
     */
    public String getPeertrustServiceResource() {
        log.debug(".getPeertrustServiceResource()");
        return peertrustServiceResource;
    }

    /**
     * Sets the PeertrustServiceResource.
     * 
     * @param peertrustServiceResource
     *            the PeertrustServiceResource
     */
    public void setPeertrustServiceResource(String peertrustServiceResource) {
        log.debug(".setPeertrustServiceResource()");
        this.peertrustServiceResource = peertrustServiceResource;
    }

}