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

package org.peertrust.parser.prolog;

/**
 * <p>
 * This class represents structure of token for both parsers 
 * </p><p>
 * $Id: InputToken.java,v 1.2 2005/08/07 12:06:52 dolmedilla Exp $
 * <br/>
 * Date: 15-Aug-2004
 * <br/>
 * Last changed: $Date: 2005/08/07 12:06:52 $
 * by $Author: dolmedilla $
 * </p>
 * @author Bogdan Vlasenko
 */
public class InputToken
{
    public int kind, beginLine, beginColumn, endLine, endColumn;
    public String image;
}
