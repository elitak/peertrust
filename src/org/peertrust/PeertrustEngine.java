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
import org.apache.log4j.PropertyConfigurator;
import org.peertrust.event.EventDispatcher;
import org.peertrust.event.PeerTrustEventListener;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.meta.MetaInterpreter;

/**
 * $Id: PeertrustEngine.java,v 1.1 2004/10/20 19:26:38 dolmedilla Exp $
 * @author olmedilla 
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/10/20 19:26:38 $
 * by $Author: dolmedilla $
 * @description
 */
public class PeertrustEngine {
	
	// name of the default configuration file name
	private static final String DEFAULT_CONFIG_FILENAME = "trustConfig.rdf" ;
	// name of the log configuration file
	private static final String LOG_CONFIG_FILE = ".logconfig" ;
	
	private static Logger log ;
	
	private PeertrustConfigurator _configurator ;

	private MetaInterpreter _metaI ;
	private PeerTrustEventListener _eventListener ;
	static private EventDispatcher _dispatcher ;

	/**
	 * 
	 */
	public PeertrustEngine() {
		super();
		// Configure logging
		PropertyConfigurator.configure(LOG_CONFIG_FILE) ;

        log = Logger.getLogger(PeertrustEngine.class) ;
        log.info("Log4j configured based on file \"" + LOG_CONFIG_FILE + "\"");

		log.debug("$Id: PeertrustEngine.java,v 1.1 2004/10/20 19:26:38 dolmedilla Exp $");
	}
	
	private void init(String [] args) throws ConfigurationException {
		_configurator = new PeertrustConfigurator() ;
		_configurator.startApp(args) ;
				
		_dispatcher = (EventDispatcher) _configurator.createComponent(Vocabulary.EventDispatcher, true) ;
		
		_metaI = (MetaInterpreter) _configurator.createComponent(Vocabulary.MetaInterpreter, true) ;
		_metaI.setConfigurator(_configurator) ;
		_metaI.init() ;
		
		_eventListener = (PeerTrustEventListener) _configurator.createComponent(Vocabulary.EventListener, true) ;
		
		Runtime.getRuntime().addShutdownHook(new ShutdownHook()) ;
	}
	
	private void stop()
	{
		_metaI.stop() ;
	}
	
	public static EventDispatcher getDispatcher()
	{
		return _dispatcher ;
	}
	
    /**
     * 
     * 
     * <p>See the
     * <a href="{@docRoot}/license.txt">License</a>,
     * <a href="{@docRoot}/README">ReadMe</a>.</p>
     *
     * @author <a href="mailto:brunkhor@learninglab.de">Ingo Brunkhorst</a>
     * @author $Author: dolmedilla $
     * @version $Revision: 1.1 $
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
            PeertrustEngine.this.stop() ;
        }
    };
    
	public static void main (String [] args) throws ConfigurationException
	{
		String [] args2 = { "file:" + DEFAULT_CONFIG_FILENAME } ;
		
		PeertrustEngine engine = new PeertrustEngine () ;
		
		engine.init(args2) ;
	}
}
