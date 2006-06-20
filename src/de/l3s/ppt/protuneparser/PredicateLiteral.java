package de.l3s.ppt.protuneparser;

import java.util.ArrayList;

public class PredicateLiteral {
	private StringDescription predicate = null;
	private ArrayList arguments = null;

	public PredicateLiteral(StringDescription predicate, ArrayList arguments) {
		this.predicate = predicate;
		this.arguments = arguments;
	}
	public ArrayList getArguments() {
		return arguments;
	}
	public StringDescription getPredicate() {
		return predicate;
	}
}
