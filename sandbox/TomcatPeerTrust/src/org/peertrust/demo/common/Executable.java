package org.peertrust.demo.common;
/**
 * An executable is wrapper for code to allow it to be
 * run in different context. It can for example be used to 
 * as replacement to e callback. if the callee does not want
 * or cannot reveil it interface to the caller.
 *  
 * @author Patrice Congo (token 77)
 */
public interface Executable {
	/**
	 * Executes this executable.
	 * @param param -- data necessaryfor the execution
	 */
	public void execute(Object param);
}
