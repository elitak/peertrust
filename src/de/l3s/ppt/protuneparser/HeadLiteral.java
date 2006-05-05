package de.l3s.ppt.protuneparser;

public class HeadLiteral {
	private PredicateLiteral predicateLiteral;
	private StringDescription stringConstant;
	private ComplexTerm complexTerm;
	private boolean isPredicateLiteral = false;
	private boolean isStringConstant = false;
	private boolean isComplexTerm = false;
	
	public HeadLiteral(PredicateLiteral predicateLiteral) {
		this.predicateLiteral = predicateLiteral;
		complexTerm = null;
		stringConstant = null;
		isPredicateLiteral = true;
	}
	public HeadLiteral(StringDescription stringConstant) {
		this.stringConstant = stringConstant;
		predicateLiteral = null;
		complexTerm = null;
		isStringConstant = true;
	}
	public HeadLiteral(ComplexTerm complexTerm) {
		this.complexTerm = complexTerm;
		predicateLiteral = null;
		stringConstant = null;
		isComplexTerm = true;
	}
	public ComplexTerm getComplexTerm() {
		return complexTerm;
	}
	public PredicateLiteral getPredicateLiteral() {
		return predicateLiteral;
	}
	public StringDescription getStringConstant() {
		return stringConstant;
	}
	public boolean isComplexTerm() {
		return isComplexTerm;
	}
	public boolean isPredicateLiteral() {
		return isPredicateLiteral;
	}
	public boolean isStringConstant() {
		return isStringConstant;
	}
}
