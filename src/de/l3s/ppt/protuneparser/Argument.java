package de.l3s.ppt.protuneparser;

public class Argument {
	private AnyTerm anyTerm;
	private PredicateLiteral predicateLiteral;
	private boolean hasPredicateLiteral;
	
	public Argument(AnyTerm anyTerm) {
		this.anyTerm = anyTerm;
		predicateLiteral = null;
		hasPredicateLiteral = false;
	}
	public Argument(PredicateLiteral predicateLiteral) {
		anyTerm = null;
		this.predicateLiteral = predicateLiteral;
		hasPredicateLiteral = true;
	}
	public AnyTerm getAnyTerm() {
		return anyTerm;
	}
	public boolean hasPredicateLiteral() {
		return hasPredicateLiteral;
	}
	public PredicateLiteral getPredicateLiteral() {
		return predicateLiteral;
	}
}
