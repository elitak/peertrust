package org.protune.net;

import java.io.*;

/**
 * In order to grant that communication among nodes actually takes place, it is required that the
 * information sent by one node is received by the other one, which means that each node needs to have a
 * reference (pointer) to the location of the other one. The interface <tt>Pointer</tt> represents the
 * abstraction of a reference to a remote service. A concrete implementation can simply exploit socket
 * (as the current one in the Protune system) or whatever more complex communication means (RPC, RMI, Web
 * Services ...), as long as the method {@link #sendMessage(Message)} is provided, ensuring that the
 * information which should be received by the other node is actually sent.
 * @author jldecoi
 */
public interface Pointer extends Serializable{
	
	public void sendMessage(Message m) throws IOException;

}
