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

package org.peertrust.inference;

import org.peertrust.meta.Tree;

/**
 * $Id: InferenceEngine.java,v 1.2 2004/07/08 15:10:42 dolmedilla Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/07/08 15:10:42 $
 * by $Author: dolmedilla $
 * @description
 */
public interface InferenceEngine
{
	public LogicAnswer [] processTree (LogicQuery query) ;
	public void unifyTree (Tree tree, String newQuery) ;
	public boolean execute (String query) ;
}