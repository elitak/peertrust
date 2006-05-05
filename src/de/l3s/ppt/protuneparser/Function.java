package de.l3s.ppt.protuneparser;

import java.util.ArrayList;

public class Function {
	private StringDescription predicate;
	private boolean hasBrackets;
	private ArrayList terms;

	public Function(StringDescription predicate, boolean hasBrackets, ArrayList terms) {
		this.predicate = predicate;
		this.hasBrackets = hasBrackets;
		this.terms = terms;
	}
	public ArrayList getTermList() {
		return terms;
	}
	public boolean hasBrackets() {
		return hasBrackets;
	}
	public StringDescription getPredicate() {
		return predicate;
	}
}
