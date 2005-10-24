package org.peertrust.demo.servlet;

import java.util.concurrent.BlockingQueue;

import org.peertrust.TrustClient;
import org.peertrust.demo.resourcemanagement.TrustManager;
import org.peertrust.net.Message;
import org.peertrust.net.Peer;

public interface NegotiationObjectRepository {

	public abstract void destroy();

	public abstract TrustManager getTrustManager();

	public abstract void addMessage(Object mes);

	public abstract BlockingQueue getMessageFIFO(Peer peer);

	public abstract BlockingQueue removeMessageFIFO(String key);

	public abstract BlockingQueue removeMessageFIFO(Peer key);

	public abstract void send(Message mes, String finalDestination);

	public abstract void registerSession(String key, Peer peer);

	public abstract Peer getSessionPeer(String key);

	/**
	 * @return Returns the trustClient.
	 */
	public abstract TrustClient getTrustClient();

	public abstract NegotiationObjects getThis() throws NullPointerException;

}