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
package org.peertrust;

import org.apache.log4j.Logger;
import org.peertrust.config.*;
import org.peertrust.event.EventDispatcher;
import org.peertrust.event.PTEventListener;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.meta.MetaInterpreter;
import org.peertrust.meta.MetaInterpreterListener;

/**
 * $Id: PTEngine.java,v 1.2 2004/11/24 10:24:03 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/11/24 10:24:03 $
 * by $Author: dolmedilla $
 * @description
 */
public class PTEngine implements Configurable {
	private static Logger log = Logger.getLogger(PTEngine.class);
	
//	// name of the default configuration file name
//	private static final String DEFAULT_CONFIG_FILENAME = "trustConfig.rdf" ;
//	// name of the log configuration file
//	private static final String LOG_CONFIG_FILE = ".logconfig" ;
	
//	private PTConfigurator _configurator ;

	private MetaInterpreter _metaInterpreter ;
	private MetaInterpreterListener _metaInterpreterListener ;
	private PTEventListener _eventListener ;
	private EventDispatcher _dispatcher ;

	/**
	 * 
	 */
	public PTEngine() {
		super();
		log.debug("$Id: PTEngine.java,v 1.2 2004/11/24 10:24:03 dolmedilla Exp $");
	}
	
	public void init() throws ConfigurationException {
		String msg = null ;
		if (_dispatcher == null)
			msg = "There not exist an event dispatcher" ;
		else if (_metaInterpreter == null)
			msg = "There not exist a meta interpreter" ;
		else if (_metaInterpreterListener == null)
			msg = "There not exist a meta interpreter listener" ;
			
		if (msg != null)
		{
			log.error (msg) ;
			throw new ConfigurationException(msg) ;
		}

//		_eventListener = (PTEventListener) _configurator.createComponent(Vocabulary.EventListener, true) ;
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook()) ;
	}
	
	public void stop()
	{
		_metaInterpreter.stop() ;
		_metaInterpreterListener.stop() ;
	}
	
//	public EventDispatcher getDispatcher()
//	{
//		return _dispatcher ;
//	}
//	
    /**
     * 
     * 
     * <p>See the
     * <a href="{@docRoot}/license.txt">License</a>,
     * <a href="{@docRoot}/README">ReadMe</a>.</p>
     *
     * @author <a href="mailto:brunkhor@learninglab.de">Ingo Brunkhorst</a>
     * @author $Author: dolmedilla $
     * @version $Revision: 1.2 $
     */
    public class ShutdownHook extends Thread {

        /**
         * 
         * Create a new instance of <code>ShutdownHook</code>
         * 
         *
         */
        public ShutdownHook() {
            super();
        }
        
        /**
         * 
         * @inheritDoc
         * 
         * 
         * @see java.lang.Runnable#run()
         */
        public void run() {
            PTEngine.this.stop() ;
        }
    };
//    
//	public static void main (String [] args) throws ConfigurationException
//	{
//		String [] args2 = { "file:" + DEFAULT_CONFIG_FILENAME } ;
//		
//		PTEngine engine = new PTEngine () ;
//		
//		engine.init(args2) ;
//	}
	/**
	 * @param interpreter The _metaInterpreter to set.
	 */
	public void setMetaInterpreter(MetaInterpreter interpreter) {
		_metaInterpreter = interpreter;
	}
	/**
	 * @param interpreterListener The _metaInterpreterListener to set.
	 */
	public void setMetaInterpreterListener(
			MetaInterpreterListener interpreterListener) {
		_metaInterpreterListener = interpreterListener;
	}
	/**
	 * 
	 */
	public EventDispatcher getEventDispatcher() {
		return _dispatcher ;
	}
	
	/**
	 * @param _dispatcher The _dispatcher to set.
	 */
	public void setEventDispatcher(EventDispatcher _dispatcher) {
		this._dispatcher = _dispatcher;
	}
}
