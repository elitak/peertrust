package de.l3s.ppt.protuneparser;

public class AnyTerm {
	private StringDescription term;
	private ComplexTerm complexTerm;
	private boolean isComplex;
	
	public AnyTerm(StringDescription term) {
		this.term = term;
		this.complexTerm = null;
		isComplex = false;
	}
	public AnyTerm(ComplexTerm complexTerm) {
		this.term = null;
		this.complexTerm = complexTerm;
		isComplex = true;
	}
	public ComplexTerm getComplexTerm() {
		return complexTerm;
	}
	public boolean isComplex() {
		return isComplex;
	}
	public StringDescription getTerm() {
		return term;
	}
}
