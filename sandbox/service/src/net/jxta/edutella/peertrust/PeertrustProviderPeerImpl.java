/*****************************************************************************
 *
 * Copyright (c) 2003 The Edutella Project
 *
 * Redistributions in source code form must reproduce the above copyright
 * and this condition. The contents of this file are subject to the
 * Sun Project JXTA License Version 1.1 (the "License"); you may not use
 * this file except in compliance with the License.
 * A copy of the License is available at http://www.jxta.org/jxta_license.html.
 *
 *****************************************************************************/
package net.jxta.edutella.peertrust;

import net.jxta.edutella.component.ParentComponent;
import net.jxta.edutella.component.event.EdutellaEvent;
import net.jxta.edutella.component.peergroup.AbstractEdutellaComponent;
import net.jxta.edutella.component.peergroup.EdutellaPeerComponent;
import net.jxta.edutella.peer.PeerServiceRegistry;
import net.jxta.edutella.peer.characterize.DescriptionModel;
import net.jxta.edutella.peer.document.ProviderDescription;
import net.jxta.edutella.provider.EdutellaProvider;
import net.jxta.edutella.provider.event.ProvideMetadataRequestEvent;
import net.jxta.edutella.provider.event.ProvideMetadataResultEvent;
import net.jxta.edutella.service.discovery.ProviderDiscoveryService;
import net.jxta.edutella.service.discovery.ProviderPublishTask;
import net.jxta.edutella.service.query.ProviderQueryService;
import net.jxta.edutella.service.registration.RegistrationService;
import net.jxta.peergroup.PeerGroup;

import org.apache.log4j.Logger;

/**
 * Implementation of the default Edutella provider modified for Peertrust
 * testing purposes.
 * 
 * $Id: PeertrustProviderPeerImpl.java,v 1.1 2004/08/07 12:51:54 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:54 $ by $Author: magik $
 * 
 * @author Ingo Brunkhorst
 * @version $Revision: 1.1 $
 */
public class PeertrustProviderPeerImpl extends AbstractEdutellaComponent
        implements EdutellaProvider, ParentComponent, EdutellaPeerComponent {

    private static final Logger log = Logger
            .getLogger(PeertrustProviderPeerImpl.class);

    private PeertrustPeerGroup peerGroup;

    private ProviderPublishTask publishTask;

    private ProviderDiscoveryService discoveryService;

    private ProviderQueryService queryService;

    private RegistrationService registrationService;

    private PeertrustProcessor processor;

    /**
     * Constructs a new PeertrustProviderPeerImpl instance.
     */
    public PeertrustProviderPeerImpl() {
        super();
        log
                .debug("$Id: PeertrustProviderPeerImpl.java,v 1.1 2004/08/07 12:51:54 magik Exp $");
    }

    /**
     * {@inheritDoc}
     */
    public void init(PeerGroup stdPeerGroup) {
        super.init(stdPeerGroup);
        log.debug(".init() [" + stdPeerGroup.getPeerGroupName() + "]");

        if (stdPeerGroup instanceof PeertrustPeerGroup) {
            setPeerGroup((PeertrustPeerGroup) stdPeerGroup);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void init(ParentComponent parent, String identifier) {
        super.init(parent, identifier);
        log.debug(".init()");
    }

    /**
     * {@inheritDoc}
     */
    public int startApp(String[] args) {
        log.debug(".startApp()");
        int ret = super.startApp(args);

        this.queryService = this.peerGroup.getProviderQueryService();
        this.discoveryService = this.peerGroup.getProviderDiscoveryService();
        this.registrationService = this.peerGroup.getRegistrationService();

        this.queryService.register(this);

        ProvideMetadataRequestEvent pmre = new ProvideMetadataRequestEvent(this);

        try {
            this.dispatchEvent(pmre);
        } catch (Exception e) {
            log.error("Exception!", e);
        }

        getDiscoveryService().publishProvider(true);

        processor = new PeertrustProcessorImpl(peerGroup.getPeertrustService());
        return (ret);
    }

    /**
     * {@inheritDoc}
     */
    public void stopApp() {
        log.debug(".stopApp()");

        getDiscoveryService().publishProvider(false);

        super.stopApp();
    }

    /**
     * {@inheritDoc}
     */
    public void event(EdutellaEvent event) {
        log.debug(".event()");

        if (event instanceof ProvideMetadataResultEvent) {
            log.info("Received Metadata from the ProviderConnection.");

            ProvideMetadataResultEvent resultEvent = (ProvideMetadataResultEvent) event;
            DescriptionModel description = resultEvent.getDescription();

            ProviderDescription document = (ProviderDescription) PeerServiceRegistry
                    .getDocument();
            document.setDescriptionModel(description);

            getRegistrationService().register(document);
        } else {
            super.event(event);
        }
    }

    /**
     * Gets the value of log
     * 
     * @return the value of log
     */
    public static Logger getLog() {
        return PeertrustProviderPeerImpl.log;
    }

    /**
     * Gets the value of peerGroup
     * 
     * @return the value of peerGroup
     */
    public PeertrustPeerGroup getPeerGroup() {
        return this.peerGroup;
    }

    /**
     * Sets the value of peerGroup
     * 
     * @param peerGroup
     *            Value to assign to this.peerGroup
     */
    public void setPeerGroup(PeertrustPeerGroup peerGroup) {
        this.peerGroup = peerGroup;
    }

    /**
     * Gets the value of startupTask
     * 
     * @return the value of startupTask
     */
    public ProviderPublishTask getPublishTask() {
        return this.publishTask;
    }

    /**
     * Sets the value of startupTask
     * 
     * @param startupTask
     *            Value to assign to this.startupTask
     */
    public void setPublishTask(ProviderPublishTask startupTask) {
        this.publishTask = startupTask;
    }

    /**
     * Gets the value of discoveryService
     * 
     * @return the value of discoveryService
     */
    public ProviderDiscoveryService getDiscoveryService() {
        return this.discoveryService;
    }

    /**
     * Sets the value of discoveryService
     * 
     * @param discoveryService
     *            Value to assign to this.discoveryService
     */
    public void setDiscoveryService(ProviderDiscoveryService discoveryService) {
        this.discoveryService = discoveryService;
    }

    /**
     * Gets the value of queryService
     * 
     * @return the value of queryService
     */
    public ProviderQueryService getQueryService() {
        return this.queryService;
    }

    /**
     * Sets the value of queryService
     * 
     * @param queryService
     *            Value to assign to this.queryService
     */
    public void setQueryService(ProviderQueryService queryService) {
        this.queryService = queryService;
    }

    /**
     * Gets the value of registrationService
     * 
     * @return the value of registrationService
     */
    public RegistrationService getRegistrationService() {
        return this.registrationService;
    }

    /**
     * Sets the value of registrationService
     * 
     * @param registrationService
     *            Value to assign to this.registrationService
     */
    public void setRegistrationService(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

}