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
package g4mfs.impl.org.peertrust.net;

import g4mfs.impl.org.peertrust.config.Configurable;
import g4mfs.impl.org.peertrust.exception.ConfigurationException;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Hashtable;

import org.apache.log4j.Logger;

/**
 * <p>
 * 
 * </p><p>
 * $Id: EntitiesTable.java,v 1.1 2005/11/30 10:35:10 ionut_con Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/11/30 10:35:10 $
 * by $Author: ionut_con $
 * </p>
 * @author olmedilla 
 */
public class EntitiesTable implements Configurable {

	private static Logger log = Logger.getLogger(EntitiesTable.class);
	
	private Hashtable _entities ;
	private String _entitiesFile ;
	
	/**
	 * 
	 */
	public EntitiesTable() {
		super();
		log.debug("$Id: EntitiesTable.java,v 1.1 2005/11/30 10:35:10 ionut_con Exp $");
	}
	
	public void init() throws ConfigurationException
	{
		log.debug("(Init) Entities file: " + _entitiesFile) ;
		
		if (_entitiesFile != null)
			_entities = readEntities(_entitiesFile) ;
		else
			_entities = new Hashtable() ;
	}

	public void put (Object key, Object value)
	{
		_entities.put(key, value) ;
	}
	
	public Object get (Object key)
	{
		return _entities.get(key) ;
	}
	
	private Hashtable readEntities (String fileName) throws ConfigurationException
	{
		Hashtable entities = new Hashtable() ;
		
		BufferedReader input = null;
		try {
			input = new BufferedReader(new FileReader(fileName)); // Open the file.
		}
		catch(FileNotFoundException e) { // The file may not exist.
			log.error("Entities file not found: " + fileName, e) ;
			throw new ConfigurationException (e) ;
		}
		
		// Now we read the file, line by line, echoing each line to
		// the terminal.
		try {
			String line;
			String [] attributes ;
			while( (line = input.readLine()) != null ) {
				if (line.charAt(0) != '%')
				{
					attributes = line.split("\t") ;
					entities.put(attributes[0], new Peer (attributes[0], attributes[1], Integer.parseInt(attributes[2]))) ;
				}
			 }
		}
		catch(IOException x) {
			x.printStackTrace();
			throw new ConfigurationException (x) ;
		}
		return entities ;
	}
	
	/**
	 * @return Returns the _entitiesFile.
	 */
	public String getEntitiesFile() {
		return _entitiesFile;
	}
	/**
	 * @param file The _entitiesFile to set.
	 */
	public void setEntitiesFile(String file) {
		_entitiesFile = file;
	}
	
	public static void main (String [] args) throws ConfigurationException
	{
		EntitiesTable et = new EntitiesTable() ;
		et.setEntitiesFile("/home/olmedilla/workspace/peertrust/config/entities.dat") ;
		et.init() ;
		Peer p = (Peer) et.get("test") ;
		p = (Peer) et.get("company") ;
		p = (Peer) et.get("elearn") ;
	}
}
