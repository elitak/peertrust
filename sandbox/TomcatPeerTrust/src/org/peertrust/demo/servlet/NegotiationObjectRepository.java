package org.peertrust.demo.servlet;

import org.peertrust.TrustClient;
import org.peertrust.demo.common.Messenger;
import org.peertrust.demo.resourcemanagement.TrustManager;
import org.peertrust.net.Peer;


public interface NegotiationObjectRepository {

	public abstract void destroy();

	public abstract TrustManager getTrustManager();
	
	public abstract Messenger getMessenger();

//	public abstract void registerSession(String key, Peer peer);

	public abstract Peer getSessionPeer(String key);

	/**
	 * @return Returns the trustClient.
	 */
	public abstract TrustClient getTrustClient();

}