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
 * $Id: Peer.java,v 1.1 2004/08/07 12:51:55 magik Exp $
 * 
 * @author olmedilla
 * @date 05-Dec-2003 Last changed $Date: 2004/08/07 12:51:55 $ by $Author:
 *       dolmedilla $
 * @description
 */
public class Peer implements Serializable {
    private static final int DEFAULT_PORT = 30000;

    private String alias;

    private String address;

    private int port;

    public Peer(String alias, String address, int port) {
        this.alias = alias;
        this.address = address;
        this.port = port;
    }

    public Peer(String alias, String address) {
        this(alias, address, DEFAULT_PORT);
    }

    public Peer() {
        this("", "", DEFAULT_PORT);
    }

    public boolean equals(Object object) {
        Peer peer = (Peer) object;
        if (this.address.compareTo(peer.getAddress()) == 0)
            return true;
        else
            return false;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

}