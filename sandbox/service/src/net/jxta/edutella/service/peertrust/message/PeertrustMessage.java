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

import java.io.Serializable;

/**
 * Wrapper message for transfering arbitrary serializable objects.
 * 
 * $Id: PeertrustMessage.java,v 1.1 2004/08/07 12:51:55 magik Exp $
 * 
 * Last changed $Date: 2004/08/07 12:51:55 $ by $Author: magik $
 * 
 * @author Mathias Fiedler, Marc Herrlich
 * @version $Revision: 1.1 $
 */
public class PeertrustMessage implements Serializable {

    private Object object;

    /**
     * Constructs a new PeertrustMessage.
     */
    public PeertrustMessage() {
    }

    /**
     * Sets the Object.
     * 
     * @param object
     *            the Object
     */
    public void setObject(Object object) {
        this.object = object;
    }

    /**
     * Returns the Object.
     * 
     * @return the Object
     */
    public Object getObject() {
        return object;
    }

    /**
     * Returns a String representation of the Object.
     * 
     * @return the String representation
     */
    public String toString() {
        return object.toString();
    }
}