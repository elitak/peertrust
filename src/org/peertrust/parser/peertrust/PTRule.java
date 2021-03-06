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

package org.peertrust.parser.peertrust;

/**
 * <p>
 * This class represents structure of PeerTrust rule
 * </p><p>
 * $Id: PTRule.java,v 1.2 2005/08/07 12:06:54 dolmedilla Exp $
 * <br/>
 * Date: 15-Aug-2004
 * <br/>
 * Last changed: $Date: 2005/08/07 12:06:54 $
 * by $Author: dolmedilla $
 * </p>
 * @author Bogdan Vlasenko
 */
public class PTRule
{
    public int simpleRule = 1;
    public int signedRule = 0;
    public int ruleWithGuards = 0;
    public String head = new String();
    public String body = new String();
    public String signedBy = new String();
    public String guards = new String();
    public String beforeSigned = new String();
    public String signedBody = new String();
    public String signedGuards = new String();
}
