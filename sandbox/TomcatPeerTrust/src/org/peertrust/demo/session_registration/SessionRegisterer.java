package org.peertrust.demo.session_registration;

import java.io.IOException;
import java.io.Serializable;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;
import org.peertrust.config.Configurable;
import org.peertrust.config.Vocabulary;
import org.peertrust.demo.common.Messenger;
import org.peertrust.demo.peertrust_com_asp.PTComASPMessageListener;
import org.peertrust.demo.peertrust_com_asp.PTCommunicationASP;
import org.peertrust.event.PTEventListener;
import org.peertrust.exception.ConfigurationException;
import org.peertrust.net.EntitiesTable;
import org.peertrust.net.Peer;

public class SessionRegisterer implements 	PTComASPMessageListener, 
											Configurable 
{
	/**
	 * The message logger
	 */
	private Logger logger=Logger.getLogger(SessionRegisterer.class);
	
	/**
	 * An hashtable which contains session key, peer pairs
	 */
	private Hashtable sessionTable;
	
	/**
	 * the peertrust negotiation entity table that contains peers
	 */
	private EntitiesTable entitiesTable;
	//TODO better alternative will be to extends the entity table to contains session peer pairs
	
	
	private Messenger messenger;
	
	private PTCommunicationASP communicationASP;
	/**
	 * Implements PTMessageReceived(Serializable, Peer, Peer)  to listen to
	 * HttpSessionRegistrationRequest
	 * @see org.peertrust.demo.peertrust_com_asp.PTComASPMessageListener#PTMessageReceived(Serializable, Peer, Peer)
	 */
	public void PTMessageReceived(	
			Serializable message,
			Peer source, 
			Peer target) 
	{
		logger.info(
		"\n************************Registerering log**********************\n"+
		message+	
		"\nSource Peer:"+source+
		"\n************Registerering log END**********************");
		//register session
		if(message instanceof HttpSessionRegistrationRequest){
		
			HttpSessionRegistrationRequest req=
			(HttpSessionRegistrationRequest)message;
			if(req.doMakeRegistration()){
				String sessionId=
				((HttpSessionRegistrationRequest)message).getSessionKey();
				sessionTable.put(sessionId, source);
				
				//register peer to entity table
		//		EntitiesTable entitiesTable = 
		//		(EntitiesTable) trustClient.getComponent(Vocabulary.EntitiesTable) ;
				entitiesTable.put(source.getAlias(),source);
			}else if(req.doRemoveRegistration()){
				sessionTable.remove(req.getSessionKey());
		//		EntitiesTable entitiesTable = 
		//		(EntitiesTable) trustClient.getComponent(Vocabulary.EntitiesTable) ;
				entitiesTable.remove(source.getAlias());
				try {
					//getMessenger().closeChannel(source);
					messenger.closeChannel(source);
				} catch (IOException e) {
					logger.debug(	
							"Unregistering;Could not close channel "+
									"for "+source,
							e);
				}
			}else{	
				logger.debug("Cannot handle "+message.getClass());
			}
		}		
	}

	public void init() throws ConfigurationException {
		if(entitiesTable==null){
			throw new ConfigurationException(
					"entitiesTable not set at "+this.getClass());
		}
		if(messenger==null){
			throw new ConfigurationException(
					"messenger not set"+this.getClass());
		}
		
		if(communicationASP==null){
			throw new ConfigurationException(
					"CommunicationASP not set at"+this.getClass());
		}
		sessionTable= new Hashtable();
		//register for http session registration requests	
		communicationASP.registerPTComASPMessageListener(
								this,
								HttpSessionRegistrationRequest.class);
			
	}

	/**
	 * @return Returns the communicationASP.
	 */
	public PTCommunicationASP getCommunicationASP() {
		return communicationASP;
	}

	/**
	 * @param communicationASP The communicationASP to set.
	 */
	public void setCommunicationASP(PTCommunicationASP communicationASP) {
		this.communicationASP = communicationASP;
	}

	/**
	 * @return Returns the entitiesTable.
	 */
	public EntitiesTable getEntitiesTable() {
		return entitiesTable;
	}

	/**
	 * @param entitiesTable The entitiesTable to set.
	 */
	public void setEntitiesTable(EntitiesTable entitiesTable) {
		this.entitiesTable = entitiesTable;
	}

	/**
	 * @return Returns the messenger.
	 */
	public Messenger getMessenger() {
		return messenger;
	}

	/**
	 * @param messenger The messenger to set.
	 */
	public void setMessenger(Messenger messenger) {
		this.messenger = messenger;
	}
	
	/**
	 * To get the peer registern for the spefied session
	 * @param key -- the registered session key
	 * @see org.peertrust.demo.servlet.NegotiationObjectRepository#getSessionPeer(java.lang.String)
	 */
	public Peer getSessionPeer(String key){
		if(key==null){
			return null;
		}
		return (Peer)sessionTable.get(key);
	}
	
	/**
	 * Removed all session entries linked to the specified 
	 * peer;
	 * @param 	leavingPeer -- the peer which entries will be
	 * 			removed.
	 */
	public void  removePeertrustSessionEntries(Peer leavingPeer){
		if(leavingPeer==null){
			return;
		}
		
		String leavingPeerAlias=leavingPeer.getAlias();
		
		if(leavingPeerAlias==null){
			return;
		}
		Peer currentPeer;
		for(	Iterator it=sessionTable.entrySet().iterator(); 
				it.hasNext();){
			Map.Entry entry=(Map.Entry)it.next();
			currentPeer=(Peer)entry.getValue();
			if(leavingPeerAlias.equals(currentPeer.getAlias())){
				sessionTable.remove(entry.getKey());					
			}
						
		}
		
//		EntitiesTable entitiesTable = 
//			(EntitiesTable) trustClient.getComponent(Vocabulary.EntitiesTable) ;
		entitiesTable.remove(leavingPeerAlias);
		try {
			getMessenger().closeChannel(leavingPeer);
		} catch (IOException e) {
			logger.debug(	
					"Unregistering;Could not close channel "+
							"for "+leavingPeer,
					e);
		}
		
	}

}
