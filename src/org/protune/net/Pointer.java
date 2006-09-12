package org.protune.net;

import java.io.*;

public interface Pointer extends Serializable{
	
	void sendMessage(NegotiationMessage nm) throws IOException;

}
