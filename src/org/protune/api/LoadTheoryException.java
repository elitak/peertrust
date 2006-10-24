package org.protune.api;

/**
 * Exception thrown by a {@link org.protune.api.PrologEngine} object when some problem arises in loading
 * a Prolog theory (e.g. when a syntactic error was found in the theory).
 * @author jldecoi
 */
public class LoadTheoryException extends Exception {
	
	static final long serialVersionUID = 43;
	
}
