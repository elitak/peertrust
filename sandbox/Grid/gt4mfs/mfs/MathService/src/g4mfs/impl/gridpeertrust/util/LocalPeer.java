/*
 * Created on Jul 16, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.util;

import g4mfs.impl.org.peertrust.net.Peer;

import java.util.HashMap;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * Class used for finding the local peer(source) associated with a destination
 * the source address of a source peer may differ depending on the destination entity with which the peer interacts
 * for an usual client the source address assoicated with the address of the destination service would the address of a notification
 * for a service the source address associated with a client would be the service address
 * for a service acting as a client to a second service the source address associated with the second service would be the address of a notification
  */
public class LocalPeer 
{
	HashMap hashMap; // used to maintain the relation destination (key) - local peer
	String alias; // local peer alias this would stay constant over all communication
	
	
	public LocalPeer()
	{
		hashMap = new HashMap();
	}
	
	public LocalPeer(String alias)
	{
		hashMap = new HashMap();
		this.alias = alias;
	}

	public void setAlias(String alias)
	{
		this.alias = alias;
	}
	
	public String getAlias()
	{
		return alias;
	}
	
	
	/**
	 * @param dest String the address 
	 * @param source String the source - the local address used by the local peer to communicate with the dest Peer  
	 */
	public void add(String dest, String source)
	{
		Peer localPeer = new Peer(alias,source,0);
		hashMap.put(dest,localPeer);
	}
	
	
	
	/**
	 * @param dest the destination for which the localPeer intends to send a message
	 * @return the source Peer as whom the local Peer identifies to the dest Peer
	 */
	public Peer get(String dest)
	{
		Peer localPeer = (Peer) hashMap.get(dest);
		return localPeer;		
	}	
	
}
