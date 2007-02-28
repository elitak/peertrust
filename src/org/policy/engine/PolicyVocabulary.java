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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307 USA
*/

package org.policy.engine;

import org.policy.config.Vocabulary;

import com.hp.hpl.jena.mem.ModelMem;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.RDFException;
import com.hp.hpl.jena.rdf.model.Resource;

/**
 * <p>
 * 
 * </p><p>
 * $Id: PolicyVocabulary.java,v 1.1 2007/02/28 08:40:14 dolmedilla Exp $
 * <br/>
 * Date: Feb 28, 2007
 * <br/>
 * Last changed: $Date: 2007/02/28 08:40:14 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla
 */

public class PolicyVocabulary {

	
	 public static Resource PolicyEngineObject ;
	 
	 static
	 {
		try
		{
			 Model m = new ModelMem();

			 PolicyEngineObject = m.createResource(Vocabulary.getURI() + "PEngine");
			 
		} catch (RDFException rdfe)
		{
			throw(new RuntimeException(rdfe.getMessage()));
		}
	}
	
}
