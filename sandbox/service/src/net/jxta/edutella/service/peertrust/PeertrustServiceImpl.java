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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import net.jxta.document.Advertisement;
import net.jxta.edutella.component.peergroup.AbstractQueryService;
import net.jxta.edutella.peer.destination.Destination;
import net.jxta.edutella.peer.destination.JxtaDestination;
import net.jxta.edutella.service.message.ResolverMessage;
import net.jxta.edutella.service.peertrust.event.PeertrustEvent;
import net.jxta.edutella.service.peertrust.message.PeertrustMessage;
import net.jxta.edutella.service.peertrust.message.PeertrustQueryMessage;
import net.jxta.edutella.service.peertrust.message.PeertrustQueryMessageImpl;
import net.jxta.edutella.service.peertrust.message.PeertrustResponseMessage;
import net.jxta.edutella.service.peertrust.message.PeertrustResponseMessageImpl;
import net.jxta.edutella.util.ServiceMessageFactory;
import net.jxta.endpoint.ByteArrayMessageElement;
import net.jxta.endpoint.Message;
import net.jxta.endpoint.MessageElement;
import net.jxta.endpoint.StringMessageElement;
import net.jxta.id.ID;
import net.jxta.impl.protocol.ResolverQuery;
import net.jxta.impl.protocol.ResolverResponse;
import net.jxta.peer.PeerID;
import net.jxta.peergroup.PeerGroup;
import net.jxta.pipe.OutputPipe;
import net.jxta.pipe.PipeMsgEvent;
import net.jxta.pipe.PipeMsgListener;
import net.jxta.pipe.PipeService;
import net.jxta.protocol.PipeAdvertisement;
import net.jxta.protocol.ResolverQueryMsg;
import net.jxta.protocol.ResolverResponseMsg;
import net.jxta.resolver.ResolverService;
import net.jxta.service.Service;
import net.jxta.util.AdvertisementUtilities;

import org.apache.log4j.Logger;

/**
 * Implementation of the PeertrustService for Edutella.
 * 
 * $Id: PeertrustServiceImpl.java,v 1.1 2004/08/07 12:51:56 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:56 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public class PeertrustServiceImpl extends AbstractQueryService implements
        PeertrustService, PipeMsgListener {

    private static final long TIMEOUT = 30 * 1000;

    private static final String PEERTRUST_ELEMENT = "PEERTRUST";

    private static final String ID_ELEMENT = "ID";

    private static final Logger log = Logger
            .getLogger(PeertrustServiceImpl.class);

    private Map peerEntries = new HashMap();

    private Map queryEntries = new HashMap();

    private Set listeners = new HashSet();

    /**
     * Constructs a new PeertrustServiceImpl instance.
     */
    public PeertrustServiceImpl() {
        super();
        log
                .debug("$Id: PeertrustServiceImpl.java,v 1.1 2004/08/07 12:51:56 magik Exp $");
    }

    /**
     * {@inheritDoc}
     */
    public void init(PeerGroup newPeerGroup, ID id, Advertisement advertisement) {
        super.init(newPeerGroup, id, advertisement);
        log.debug(".init()");
    }

    /**
     * {@inheritDoc}
     */
    public int startApp(String[] args) {
        int ret = super.startApp(args);
        log.debug(".startApp()");

        ServiceMessageFactory.registerMessageInstance(
                PeertrustQueryMessage.DOCUMENT_ROOT_ELEMENT,
                new PeertrustQueryMessageImpl.Instantiator());

        ServiceMessageFactory.registerMessageInstance(
                PeertrustResponseMessage.DOCUMENT_ROOT_ELEMENT,
                new PeertrustResponseMessageImpl.Instantiator());
        return ret;
    }

    /**
     * {@inheritDoc}
     */
    public void stopApp() {
        log.debug(".stopApp()");
        super.stopApp();
    }

    /**
     * {@inheritDoc}
     */
    public int processQuery(ResolverQueryMsg queryMessage) {
        log.debug(".processQuery()");

        try {
            ResolverMessage message = ServiceMessageFactory
                    .fromString(queryMessage.getQuery());
            if (message.isMessageType(PeertrustQueryMessageImpl
                    .getMessageType())) {
                PeertrustQueryMessage initMsg = (PeertrustQueryMessage) message;
                if (initMsg.isBroadcast() && initMsg.getQuery() != null
                        && !initMsg.getQuery().equals("")) {
                    PeertrustEvent peertrustEvent = new PeertrustEvent(this);
                    PeertrustMessage msg = new PeertrustMessage();
                    msg.setObject(initMsg.getQuery());
                    peertrustEvent.setMessage(msg);
                    peertrustEvent.setSender(queryMessage.getSrc());
                    this.dispatchEvent(peertrustEvent);
                    return ResolverService.OK;
                }
                PeertrustResponseMessage response = new PeertrustResponseMessageImpl();
                response.setPeerID(this.getPeerGroup().getPeerID());
                if (initMsg.isBroadcast()) {
                    response.setBroadcast(true);
                } else {
                    getPeerGroup().getPipeService().createInputPipe(
                            initMsg.getPipeAdvertisement(), this);
                }
                ResolverResponse resolverResponse = new ResolverResponse();
                resolverResponse.setHandlerName(getHandlerName());
                resolverResponse.setQueryId(queryMessage.getQueryId());
                resolverResponse.setResponse(response.toString());
                getResolverService().sendResponse(queryMessage.getSrc(),
                        resolverResponse);
            } else {
                log.warn("unknown message type");
            }
        } catch (Exception e) {
            log.error("Exception", e);
        }
        return ResolverService.OK;
    }

    /**
     * {@inheritDoc}
     */
    public void processResponse(ResolverResponseMsg responseMessage) {
        log.debug(".processResponse()");

        try {
            ResolverMessage message = ServiceMessageFactory
                    .fromString(responseMessage.getResponse());
            if (message.isMessageType(PeertrustResponseMessageImpl
                    .getMessageType())) {
                PeertrustResponseMessage response = (PeertrustResponseMessage) message;
                if (response.isBroadcast()) {
                    QueryEntry entry = (QueryEntry) queryEntries
                            .get(new Integer(responseMessage.getQueryId()));
                    if (entry != null) {
                        Destination dest = new JxtaDestination(response
                                .getPeerID());
                        sendMessage(dest, entry.getMessage());
                    }
                } else {
                    PeerEntry entry = (PeerEntry) peerEntries.get(response
                            .getPeerID());
                    if (entry == null) {
                        return;
                    }
                    OutputPipe pipe = entry.getOutputPipe();
                    if (pipe == null) {
                        try {
                            pipe = getPeerGroup().getPipeService()
                                    .createOutputPipe(
                                            entry.getPipeAdvertisement(),
                                            5 * 1000);
                        } catch (IOException io) {
                            log.error("Pipe creation failed.", io);
                        }
                        entry.setOutputPipe(pipe);
                    }
                    PeertrustMessage msg;
                    while ((msg = entry.getNextMessage()) != null) {
                        Message jxtaMsg = new Message();
                        jxtaMsg.addMessageElement(new StringMessageElement(
                                ID_ELEMENT, getPeerGroup().getPeerID()
                                        .toString(), null));
                        ByteArrayOutputStream out = new ByteArrayOutputStream();
                        new ObjectOutputStream(out).writeObject(msg);
                        jxtaMsg.addMessageElement(new ByteArrayMessageElement(
                                PEERTRUST_ELEMENT, null, out.toByteArray(),
                                null));
                        pipe.send(jxtaMsg);
                    }
                }
            } else {
                log.warn("unknown response message type");
            }
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Service getInterface() {
        return (PeertrustService) this;
    }

    /**
     * {@inheritDoc}
     */
    public void sendMessage(String message) {
        log.debug(".sendMessage() (Broadcast with query)");

        PeertrustQueryMessage peertrustQuery = new PeertrustQueryMessageImpl();
        peertrustQuery.setBroadcast(true);
        peertrustQuery.setQuery(message);

        ResolverQuery resolverQuery = new ResolverQuery(getHandlerName(), null,
                getPeerGroup().getPeerID().toString(), peertrustQuery
                        .toString(), resolverId);

        // Send the query using the ResolverService.
        getResolverService().sendQuery(null, resolverQuery);

        // Increment ResolverId.
        resolverId++;
    }

    /**
     * {@inheritDoc}
     */
    public void sendMessage(PeertrustMessage message) {
        log.debug(".sendMessage() (Broadcast)");

        for (Iterator i = queryEntries.values().iterator(); i.hasNext();) {
            QueryEntry e = (QueryEntry) i.next();
            if (System.currentTimeMillis() - e.getTime() > TIMEOUT) {
                i.remove();
            }
        }

        QueryEntry entry = new QueryEntry();
        entry.setMessage(message);
        entry.setTime(System.currentTimeMillis());

        queryEntries.put(new Integer(resolverId), entry);

        PeertrustQueryMessage peertrustQuery = new PeertrustQueryMessageImpl();
        peertrustQuery.setBroadcast(true);

        ResolverQuery resolverQuery = new ResolverQuery(getHandlerName(), null,
                getPeerGroup().getPeerID().toString(), peertrustQuery
                        .toString(), resolverId);

        // Send the query using the ResolverService.
        getResolverService().sendQuery(null, resolverQuery);

        // Increment ResolverId.
        resolverId++;
    }

    /**
     * {@inheritDoc}
     */
    public void sendMessage(Destination destination, PeertrustMessage query) {
        log.debug(".sendMessage() (direct)");

        PeerID peerID = ((JxtaDestination) destination).getPeerID();

        PeerEntry entry = (PeerEntry) peerEntries.get(peerID);
        if (entry == null) {
            entry = new PeerEntry();
            peerEntries.put(peerID, entry);
        }

        OutputPipe pipe = entry.getOutputPipe();

        if (pipe == null) {
            entry.addMessage(query);

            PipeAdvertisement pipeAdvertisement = AdvertisementUtilities
                    .createPipeAdvertisement(this.getPeerGroup(),
                            PipeService.UnicastSecureType);

            entry.setPipeAdvertisement(pipeAdvertisement);

            PeertrustQueryMessage peertrustQuery = new PeertrustQueryMessageImpl();

            try {
                peertrustQuery.setPipeAdvertisement(pipeAdvertisement);
            } catch (Exception e) {
                log.error(e.getMessage(), e);
            }

            ResolverQuery resolverQuery = new ResolverQuery(getHandlerName(),
                    null, getPeerGroup().getPeerID().toString(), peertrustQuery
                            .toString(), resolverId);

            // Send the query using the ResolverService.
            getResolverService().sendQuery(peerID.toString(), resolverQuery);

            // Increment ResolverId.
            resolverId++;
        } else {
            try {
                Message msg = new Message();
                msg.addMessageElement(new StringMessageElement(ID_ELEMENT,
                        getPeerGroup().getPeerID().toString(), null));
                ByteArrayOutputStream out = new ByteArrayOutputStream();
                new ObjectOutputStream(out).writeObject(query);
                msg.addMessageElement(new ByteArrayMessageElement(
                        PEERTRUST_ELEMENT, null, out.toByteArray(), null));
            } catch (IOException e) {
                log.warn("Exception", e);
            }
        }
    }

    /**
     * Callback for PipeMsgEvents.
     * 
     * @param PipeMsgEvent
     *            the PipeMsgEvent
     */
    public void pipeMsgEvent(PipeMsgEvent event) {
        Message message = event.getMessage();
        MessageElement idElem = message.getMessageElement(ID_ELEMENT);
        MessageElement elem = message.getMessageElement(PEERTRUST_ELEMENT);

        PeertrustMessage msg = null;

        try {
            msg = (PeertrustMessage) new ObjectInputStream(
                    new ByteArrayInputStream(elem.getBytes(false)))
                    .readObject();
            PeertrustEvent peertrustEvent = new PeertrustEvent(event
                    .getSource());
            peertrustEvent.setMessage(msg);
            peertrustEvent.setSender(idElem.toString());
            this.dispatchEvent(peertrustEvent);
        } catch (Exception e) {
            log.error("Exception", e);
        }
    }

}