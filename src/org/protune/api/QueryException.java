package org.protune.api;

/**
 * Exception thrown by a {@link org.protune.api.PrologEngine} object when some problem arises in
 * answering a Prolog query (e.g. when a syntactic error was found in the goal or when an answer was
 * asked for, but the query was unsuccessful).
 * @author jldecoi
 */
public class QueryException extends Exception {
	
	static final long serialVersionUID = 44;
	
}
