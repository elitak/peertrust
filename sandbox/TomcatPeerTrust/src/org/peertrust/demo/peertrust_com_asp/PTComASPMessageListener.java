package org.peertrust.demo.peertrust_com_asp;

import java.io.Serializable;

//import org.peertrust.net.Message;
import org.peertrust.net.Peer;


public interface PTComASPMessageListener {
	public void PTMessageReceived(Serializable message, Peer source, Peer target); 
}
