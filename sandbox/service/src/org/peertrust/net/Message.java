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

package org.peertrust.net;

import java.io.Serializable;

/**
 * $Id: Message.java,v 1.1 2004/08/07 12:51:55 magik Exp $
 * 
 * @author olmedilla
 * @date 05-Dec-2003 Last changed $Date: 2004/08/07 12:51:55 $ by $Author:
 *       dolmedilla $
 * @description
 */
public class Message implements Serializable {
    private Peer origin = null;

    Message(Peer origin) {
        this.origin = origin;
    }

    /**
     * @return Returns the origin.
     */
    public Peer getOrigin() {
        return origin;
    }

    /**
     * @param origin
     *            The origin to set.
     */
    public void setOrigin(Peer origin) {
        this.origin = origin;
    }
}