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

import java.util.LinkedList;
import java.util.NoSuchElementException;

import net.jxta.edutella.service.peertrust.message.PeertrustMessage;
import net.jxta.pipe.OutputPipe;
import net.jxta.protocol.PipeAdvertisement;

/**
 * Class used to store pipes and delayed messages. Delayed messages are sent,
 * after a pipe is established.
 * 
 * $Id: PeerEntry.java,v 1.1 2004/08/07 12:51:56 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:56 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public class PeerEntry {

    private PipeAdvertisement pipeAdvertisement;

    private OutputPipe outputPipe;

    private LinkedList messageQueue;

    /**
     * Constructs a new PeerEntry.
     */
    public PeerEntry() {
        outputPipe = null;
        messageQueue = new LinkedList();
    }

    /**
     * Adds a message to the waiting queue.
     * 
     * @param msg
     *            the PeertrustMessage
     */
    public void addMessage(PeertrustMessage msg) {
        messageQueue.add(msg);
    }

    /**
     * Retrieves the next waiting message and removes it from the queue.
     * 
     * @return the PeertrustMessage
     */
    public PeertrustMessage getNextMessage() {
        try {
            return (PeertrustMessage) messageQueue.removeFirst();
        } catch (NoSuchElementException e) {
            return null;
        }
    }

    /**
     * Returns the associated OutputPipe.
     * 
     * @return the OutputPipe.
     */
    public OutputPipe getOutputPipe() {
        return outputPipe;
    }

    /**
     * Sets the OutputPipe.
     * 
     * @param outputPipe
     *            the OutputPipe
     */
    public void setOutputPipe(OutputPipe outputPipe) {
        this.outputPipe = outputPipe;
    }

    /**
     * Returns the PipeAdvertisment.
     * 
     * @return the PipeAdvertisement
     */
    public PipeAdvertisement getPipeAdvertisement() {
        return pipeAdvertisement;
    }

    /**
     * Sets the PipeAdvertisement.
     * 
     * @param pipeAdvertisement
     *            the PipeAdvertisement
     */
    public void setPipeAdvertisement(PipeAdvertisement pipeAdvertisement) {
        this.pipeAdvertisement = pipeAdvertisement;
    }

}