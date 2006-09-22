package org.protune.net;

/**
 * Exception to be sent when some entity during the computation realises that the message it is working
 * on does not belong to the expected type.
 * @author jldecoi
 */
public class WrongMessageTypeException extends Exception {
	
	static final long serialVersionUID = 6;
	
}
