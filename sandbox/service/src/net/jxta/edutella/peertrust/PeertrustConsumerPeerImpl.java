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

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.Iterator;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import net.jxta.edutella.component.EdutellaComponent;
import net.jxta.edutella.component.Handler;
import net.jxta.edutella.component.ParentComponent;
import net.jxta.edutella.component.event.EdutellaEvent;
import net.jxta.edutella.component.event.EventListener;
import net.jxta.edutella.component.peergroup.AbstractEdutellaComponent;
import net.jxta.edutella.component.peergroup.EdutellaPeerComponent;
import net.jxta.edutella.consumer.ConsumerComponent;
import net.jxta.edutella.consumer.PeerListComponent;
import net.jxta.edutella.consumer.QueryComponent;
import net.jxta.edutella.consumer.ResultComponent;
import net.jxta.edutella.eqm.Query;
import net.jxta.edutella.peer.destination.Destination;
import net.jxta.edutella.service.event.ResultEvent;
import net.jxta.edutella.service.peertrust.event.PeertrustEvent;
import net.jxta.edutella.service.peertrust.message.PeertrustMessage;
import net.jxta.edutella.service.query.ProviderQueryService;
import net.jxta.peergroup.PeerGroup;

import org.apache.log4j.Logger;
import org.peertrust.net.Peer;

/**
 * Implementation of the default Edutella consumer modified for Peertrust
 * testing purposes. Normal Edutella searching functionality is disabled.
 * Pressing the "Broadcast" button sends a Peertrust broadcast query.
 * 
 * $Id: PeertrustConsumerPeerImpl.java,v 1.1 2004/08/07 12:51:54 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:54 $ by $Author: magik $
 * 
 * @author Ingo Brunkhorst
 * @version $Revision: 1.1 $
 */
public class PeertrustConsumerPeerImpl extends AbstractEdutellaComponent
        implements EdutellaPeerComponent, ParentComponent {

    private static final Logger log = Logger
            .getLogger(PeertrustConsumerPeerImpl.class);

    private JFrame consumer;

    private JMenuBar consumerMenu;

    private JPanel listPanel;

    private JPanel dialogPanel;

    private JButton sendQuery;

    private JButton broadcastQuery;

    private PeerListComponent peerList;

    private QueryComponent queryComponent;

    private ResultComponent resultComponent;

    private PeertrustPeerGroup peerGroup;

    private String iconUrl;

    private PeertrustProcessor processor;

    // ************************************************************************

    public PeertrustConsumerPeerImpl() {
        super();
        log
                .debug("$Id: PeertrustConsumerPeerImpl.java,v 1.1 2004/08/07 12:51:54 magik Exp $");
    }

    // ************************************************************************

    /**
     * {@inheritDoc}
     */
    public void init(ParentComponent parent, String identifier) {
        super.init(parent, identifier);
        log.debug(".init()");

        getConsumer();
    }

    /**
     * {@inheritDoc}
     */
    public void init(PeerGroup newPeerGroup) {
        super.init(newPeerGroup);
        log.debug(".init() [" + newPeerGroup.getPeerGroupName() + "]");

        if (newPeerGroup instanceof PeertrustPeerGroup) {
            setPeerGroup((PeertrustPeerGroup) newPeerGroup);
        }
    }

    // ************************************************************************

    protected JFrame getConsumer() {
        if (this.consumer != null) {
            return (this.consumer);
        }

        QuitHandler quitHandler = new QuitHandler();

        this.consumer = new JFrame(getIdentifier());
        addIcon(this.consumer, getIconUrl());

        this.consumer.addWindowListener(quitHandler);

        this.consumer.getContentPane().setLayout(new BorderLayout());

        this.consumerMenu = new JMenuBar();
        this.consumer.setJMenuBar(this.consumerMenu);

        this.listPanel = new JPanel();
        this.dialogPanel = new JPanel();
        JPanel consumerPanel = new JPanel();

        BoxLayout consumerBox = new BoxLayout(consumerPanel, BoxLayout.X_AXIS);
        consumerPanel.setLayout(consumerBox);
        BoxLayout listBox = new BoxLayout(this.listPanel, BoxLayout.Y_AXIS);
        this.listPanel.setLayout(listBox);
        BoxLayout dialogBox = new BoxLayout(this.dialogPanel, BoxLayout.Y_AXIS);
        this.dialogPanel.setLayout(dialogBox);

        consumerPanel.add(this.listPanel);
        consumerPanel.add(this.dialogPanel);

        this.sendQuery = new JButton("Search");
        this.sendQuery.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.sendQuery.setHorizontalTextPosition(SwingConstants.CENTER);
        this.sendQuery.setMnemonic(KeyEvent.VK_S);
        this.sendQuery.setDefaultCapable(true);
        this.sendQuery.setActionCommand("search");
        this.sendQuery.addActionListener(new SendQueryHandler());

        this.broadcastQuery = new JButton("Broadcast");
        this.broadcastQuery.setVerticalTextPosition(SwingConstants.BOTTOM);
        this.broadcastQuery.setHorizontalTextPosition(SwingConstants.CENTER);
        this.broadcastQuery.setMnemonic(KeyEvent.VK_S);
        this.broadcastQuery.setDefaultCapable(true);
        this.broadcastQuery.setActionCommand("broadcast");
        this.broadcastQuery.addActionListener(new SendQueryHandler());

        this.consumer.getContentPane().add(consumerPanel, BorderLayout.CENTER);
        JPanel p2 = new JPanel();
        p2.add(this.sendQuery);
        p2.add(this.broadcastQuery);
        this.consumer.getContentPane().add(p2, BorderLayout.SOUTH);

        return (this.consumer);
    }

    protected void addIcon(JFrame newFrame, String url) {
        log.debug(".addIcon()");

        try {
            ImageIcon icon = new ImageIcon(new URL(url));
            newFrame.setIconImage(icon.getImage());
        } catch (Exception e) {
            log.warn("Ignoring Error... ", e);
        }
    }

    // ************************************************************************

    /**
     * {@inheritDoc}
     */
    public int startApp(String[] args) {
        int ret = super.startApp(args);
        log.debug(".startApp()");

        Iterator i = getComponents().keySet().iterator();

        while (i.hasNext()) {
            EdutellaComponent component = (EdutellaComponent) getComponents()
                    .get(i.next());
            component.startApp(args);

            if (component instanceof ConsumerComponent) {
                ConsumerComponent consumerComponent = (ConsumerComponent) component;

                JMenu menu = consumerComponent.getMenu();
                if (menu != null) {
                    getConsumer().getJMenuBar().add(menu);
                }

                if (component instanceof PeerListComponent) {
                    setPeerList((PeerListComponent) component);
                } else if (component instanceof QueryComponent) {
                    setQueryComponent((QueryComponent) component);
                } else if (component instanceof ResultComponent) {
                    setResultComponent((ResultComponent) component);
                } else {
                    JFrame frame = new JFrame();
                    frame.getContentPane().add(consumerComponent.getPanel());
                    frame.pack();
                    frame.doLayout();
                    frame.setVisible(true);
                    JMenuBar bar = new JMenuBar();
                    frame.setJMenuBar(bar);
                    bar.add(consumerComponent.getMenu());
                }
            }
        }

        getListPanel().add(getPeerList().getPanel());
        getDialogPanel().add(getQueryComponent().getPanel());
        getDialogPanel().add(getResultComponent().getPanel());

        getConsumer().pack();
        getConsumer().invalidate();
        getConsumer().setSize(800, 600);
        getConsumer().setVisible(true);

        processor = new PeertrustProcessorImpl(peerGroup.getPeertrustService());
        return (ret);
    }

    /**
     * {@inheritDoc}
     */
    public void stopApp() {
        super.stopApp();
        log.debug(".stopApp()");
    }

    // ************************************************************************

    /**
     * A handler to deal with the application dialog being closed or the "Quit"
     * menu item being activated.
     */
    public class QuitHandler extends WindowAdapter implements ActionListener {
        /**
         * Handles the "Quit" menu item.
         * 
         * @param e
         *            the event for the menu item.
         */
        public void actionPerformed(ActionEvent e) {
            quit();
        }

        /**
         * Handles the window's system Close button.
         * 
         * @param e
         *            the event for the window closing.
         */
        public void windowClosing(WindowEvent e) {
            quit();
        }

        /**
         * Quits the application.
         */
        private void quit() {
            Iterator i = getComponents().keySet().iterator();
            while (i.hasNext()) {
                EdutellaComponent component = (EdutellaComponent) getComponents()
                        .get(i.next());
                component.stopApp();
            }
            System.exit(0);
        }
    };

    private class SendQueryHandler implements ActionListener, EventListener,
            Handler {
        private int id = 0;

        private SendQueryHandler() {
            this.id = 0;
        }

        public void actionPerformed(ActionEvent event) {

            String action = event.getActionCommand();

            ProviderQueryService queryService = getPeerGroup()
                    .getProviderQueryService();

            Query query = getQueryComponent().getQuery();
            if (query == null) {
                return;
            }

            if (action.equals("search")) {
                this.id++;
                Destination destination = getPeerList().getDestination();
                if (destination != null) {
                    /*
                     * queryService.sendQuery(destination, query, this.id, this,
                     * false);
                     */
                    /*
                     * PeertrustNetClient netClient = new PeertrustNetClient(
                     * getPeerGroup().getPeertrustService()); log.debug("send
                     * button destination: [" + ((JxtaDestination)
                     * destination).getPeerID() .toString() + "]"); Peer peer =
                     * new Peer("eLearn", ((JxtaDestination)
                     * destination).getPeerID() .toString());
                     * org.peertrust.net.Query pQuery = new
                     * org.peertrust.net.Query(
                     * "request(spanishCourse,Session)", new Peer("alice",
                     * getPeerGroup().getPeerID() .toString()), 1);
                     * netClient.send(pQuery, peer);
                     */

                } else {
                    //queryService.sendQuery(query, this.id, this);
                    /*
                     * PeertrustNetClient netClient = new PeertrustNetClient(
                     * getPeerGroup().getPeertrustService()); Peer peer = new
                     * Peer("broadcast", ""); org.peertrust.net.Query pQuery =
                     * new org.peertrust.net.Query(
                     * "request(spanishCourse,Session)", new Peer("alice",
                     * getPeerGroup().getPeerID().toString()), 1);
                     * netClient.send(pQuery, peer);
                     */
                }
            } else if (action.equals("broadcast")) {
                this.id++;
                //queryService.sendQuery(query, this.id, this);
                /*
                 * PeertrustNetClient netClient = new PeertrustNetClient(
                 * getPeerGroup().getPeertrustService()); Peer peer = new
                 * Peer("broadcast", "");
                 */
                //netClient.send(pQuery, peer);
                PeertrustEvent peertrustEvent = new PeertrustEvent(this);
                PeertrustMessage msg = new PeertrustMessage();
                org.peertrust.net.Query pQuery = new org.peertrust.net.Query(
                        "request(spanishCourse,Session) @ broadcast", new Peer(
                                "", ""), 1);
                msg.setObject(pQuery);
                peertrustEvent.setMessage(msg);
                peertrustEvent.setSender("requester");
                getPeerGroup().getPeertrustService().dispatchEvent(
                        peertrustEvent);
            }
        }

        public void event(EdutellaEvent event) {
            if (event instanceof ResultEvent) {
                ResultEvent result = (ResultEvent) event;

                if (result.getStatusCode() == ResultEvent.OK) {
                    getResultComponent().addResult(result.getResultSet());
                } else {
                    JOptionPane.showMessageDialog(null, "Error: "
                            + result.getComment(), "Ok",
                            JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }

    };

    // ************************************************************************

    /**
     * Gets the value of log
     * 
     * @return the value of log
     */
    public static Logger getLog() {
        return PeertrustConsumerPeerImpl.log;
    }

    /**
     * Sets the value of consumer
     * 
     * @param consumer
     *            Value to assign to this.consumer
     */
    public void setConsumer(JFrame consumer) {
        this.consumer = consumer;
    }

    /**
     * Gets the value of consumerMenu
     * 
     * @return the value of consumerMenu
     */
    public JMenuBar getConsumerMenu() {
        return this.consumerMenu;
    }

    /**
     * Sets the value of consumerMenu
     * 
     * @param consumerMenu
     *            Value to assign to this.consumerMenu
     */
    public void setConsumerMenu(JMenuBar consumerMenu) {
        this.consumerMenu = consumerMenu;
    }

    /**
     * Gets the value of listPanel
     * 
     * @return the value of listPanel
     */
    public JPanel getListPanel() {
        return this.listPanel;
    }

    /**
     * Sets the value of listPanel
     * 
     * @param listPanel
     *            Value to assign to this.listPanel
     */
    public void setListPanel(JPanel listPanel) {
        this.listPanel = listPanel;
    }

    /**
     * Gets the value of dialogPanel
     * 
     * @return the value of dialogPanel
     */
    public JPanel getDialogPanel() {
        return this.dialogPanel;
    }

    /**
     * Sets the value of dialogPanel
     * 
     * @param dialogPanel
     *            Value to assign to this.dialogPanel
     */
    public void setDialogPanel(JPanel dialogPanel) {
        this.dialogPanel = dialogPanel;
    }

    /**
     * Gets the value of sendQuery
     * 
     * @return the value of sendQuery
     */
    public JButton getSendQuery() {
        return this.sendQuery;
    }

    /**
     * Sets the value of sendQuery
     * 
     * @param sendQuery
     *            Value to assign to this.sendQuery
     */
    public void setSendQuery(JButton sendQuery) {
        this.sendQuery = sendQuery;
    }

    /**
     * Gets the value of peerList
     * 
     * @return the value of peerList
     */
    public PeerListComponent getPeerList() {
        return this.peerList;
    }

    /**
     * Sets the value of peerList
     * 
     * @param peerList
     *            Value to assign to this.peerList
     */
    public void setPeerList(PeerListComponent peerList) {
        this.peerList = peerList;
    }

    /**
     * Gets the value of queryComponent
     * 
     * @return the value of queryComponent
     */
    public QueryComponent getQueryComponent() {
        return this.queryComponent;
    }

    /**
     * Sets the value of queryComponent
     * 
     * @param queryComponent
     *            Value to assign to this.queryComponent
     */
    public void setQueryComponent(QueryComponent queryComponent) {
        this.queryComponent = queryComponent;
    }

    /**
     * Gets the value of resultComponent
     * 
     * @return the value of resultComponent
     */
    public ResultComponent getResultComponent() {
        return this.resultComponent;
    }

    /**
     * Sets the value of resultComponent
     * 
     * @param resultComponent
     *            Value to assign to this.resultComponent
     */
    public void setResultComponent(ResultComponent resultComponent) {
        this.resultComponent = resultComponent;
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
     * Gets the value of iconUrl
     * 
     * @return the value of iconUrl
     */
    public String getIconUrl() {
        return this.iconUrl;
    }

    /**
     * Sets the value of iconUrl
     * 
     * @param iconUrl
     *            Value to assign to this.iconUrl
     */
    public void setIconUrl(String iconUrl) {
        this.iconUrl = iconUrl;
    }

}