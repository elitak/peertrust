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

import java.io.StringWriter;
import java.util.Enumeration;

import net.jxta.document.Document;
import net.jxta.document.MimeMediaType;
import net.jxta.document.StructuredDocument;
import net.jxta.document.StructuredDocumentFactory;
import net.jxta.document.StructuredTextDocument;
import net.jxta.document.TextElement;
import net.jxta.edutella.peer.element.BooleanElement;
import net.jxta.edutella.peer.element.BooleanElementImpl;
import net.jxta.edutella.peer.element.PeerIdentifierElement;
import net.jxta.edutella.peer.element.PeerIdentifierElementImpl;
import net.jxta.edutella.service.message.MessageException;
import net.jxta.edutella.service.message.ResolverMessage;
import net.jxta.edutella.util.ServiceMessageFactory;
import net.jxta.peer.PeerID;

/**
 * Implementation of the PeertrustResponseMessage interface.
 * 
 * $Id: PeertrustResponseMessageImpl.java,v 1.1 2004/08/07 12:51:55 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:55 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public class PeertrustResponseMessageImpl implements PeertrustResponseMessage {

    private PeerIdentifierElement peerElement;

    private BooleanElement booleanElement;

    /**
     * Constructs a new PeertrustResponseMessageImpl instance.
     */
    public PeertrustResponseMessageImpl() {
        super();
        peerElement = new PeerIdentifierElementImpl();
        booleanElement = new BooleanElementImpl(false);
    }

    /**
     * Returns the type of the message.
     * 
     * @return the message type
     */
    public static String getMessageType() {
        return (PeertrustResponseMessage.DOCUMENT_ROOT_ELEMENT);
    }

    /**
     * {@inheritDoc}
     */
    public static class Instantiator implements
            ServiceMessageFactory.Instantiator {

        /**
         * {@inheritDoc}
         */
        public String getMessageType() {
            return (PeertrustResponseMessage.DOCUMENT_ROOT_ELEMENT);
        }

        /**
         * {@inheritDoc}
         */
        public ResolverMessage newInstance() {
            return (new PeertrustResponseMessageImpl());
        }
    };

    /**
     * {@inheritDoc}
     */
    public void readDocument(TextElement textElement)
            throws IllegalArgumentException {
        if (!textElement.getName().equals(getMessageType())) {
            throw (new IllegalArgumentException("Message is of wrong type"));
        }

        Enumeration elements = textElement.getChildren();
        while (elements.hasMoreElements()) {
            TextElement element = (TextElement) elements.nextElement();
            peerElement.read(element, PeertrustResponseMessage.TAG_PREFIX);
            booleanElement.read(element, PeertrustQueryMessage.TAG_PREFIX);
        }
    }

    /**
     * {@inheritDoc}
     */
    public Document getDocument(MimeMediaType mimeMediaType) {
        StructuredDocument structuredDocument = (StructuredTextDocument) StructuredDocumentFactory
                .newStructuredDocument(mimeMediaType, DOCUMENT_ROOT_ELEMENT);

        try {
            this.peerElement.write(structuredDocument,
                    PeertrustResponseMessage.TAG_PREFIX);
            this.booleanElement.write(structuredDocument,
                    PeertrustQueryMessage.TAG_PREFIX);
        } catch (Exception e) {
            throw (new MessageException("Incomplete Message", e));
        }

        return (structuredDocument);
    }

    /**
     * {@inheritDoc}
     */
    public boolean isMessageType(String messageType) {
        return (messageType.equals(getMessageType()));
    }

    /**
     * {@inheritDoc}
     */
    public String toString() {
        try {
            StringWriter out = new StringWriter();
            StructuredTextDocument doc = (StructuredTextDocument) getDocument(MimeMediaType.XMLUTF8);
            doc.sendToWriter(out);
            return out.toString();
        } catch (Exception e) {
            return "";
        }
    }

    /**
     * {@inheritDoc}
     */
    public void setPeerID(PeerID id) {
        this.peerElement.setPeerID(id);
    }

    /**
     * {@inheritDoc}
     */
    public PeerID getPeerID() {
        return this.peerElement.getPeerID();
    }

    /**
     * {@inheritDoc}
     */
    public boolean isBroadcast() {
        return booleanElement.getValue();
    }

    /**
     * {@inheritDoc}
     */
    public void setBroadcast(boolean broadcast) {
        booleanElement.setValue(broadcast);
    }

}