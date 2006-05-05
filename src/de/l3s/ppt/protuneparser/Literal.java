package de.l3s.ppt.protuneparser;

public class Literal {
	private HeadLiteral headLiteral;
	private StringDescription negSymbol;
	private boolean hasNegSymbol;
	private SpecialLiteral specialLiteral;
	private boolean isHeadLiteral;
	private boolean isTermOperatorTerm;
	private boolean isSpecialLiteral;
	private StringDescription termBefore;
	private StringDescription operator;
	private StringDescription termAfter;
	
	public Literal(StringDescription negSymbol, HeadLiteral headLiteral) {
		this.headLiteral = headLiteral;
		if (negSymbol != null) {
			hasNegSymbol = true;
			this.negSymbol = negSymbol;
		} else {
			hasNegSymbol = false;
			this.negSymbol = null;
		}
		specialLiteral = null;
		isHeadLiteral = true;
		isTermOperatorTerm = false;
		isSpecialLiteral = false;
		termBefore = null;
		operator = null;
		termAfter = null;
	}
	public Literal(StringDescription negSymbol, StringDescription termBefore,
			StringDescription operator, StringDescription termAfter) {
		headLiteral = null;
		if (negSymbol != null) {
			hasNegSymbol = true;
			this.negSymbol = negSymbol;
		} else {
			hasNegSymbol = false;
			this.negSymbol = null;
		}
		specialLiteral = null;
		isHeadLiteral = false;
		isTermOperatorTerm = true;
		isSpecialLiteral = false;
		this.termBefore = termBefore;
		this.operator = operator;
		this.termAfter = termAfter;
	}
	public Literal(SpecialLiteral specialLiteral) {
		headLiteral = null;
		hasNegSymbol = false;
		negSymbol = null;
		this.specialLiteral = specialLiteral;
		isHeadLiteral = false;
		isTermOperatorTerm = false;
		isSpecialLiteral = true;
		termBefore = null;
		operator = null;
		termAfter = null;
	}
	public boolean hasNegSymbol() {
		return hasNegSymbol;
	}
	public HeadLiteral getHeadLiteral() {
		return headLiteral;
	}
	public boolean isHeadLiteral() {
		return isHeadLiteral;
	}
	public boolean isSpecialLiteral() {
		return isSpecialLiteral;
	}
	public boolean isTermOperatorTerm() {
		return isTermOperatorTerm;
	}
	public StringDescription getNegSymbol() {
		return negSymbol;
	}
	public StringDescription getOperator() {
		return operator;
	}
	public SpecialLiteral getSpecialLiteral() {
		return specialLiteral;
	}
	public StringDescription getTermAfter() {
		return termAfter;
	}
	public StringDescription getTermBefore() {
		return termBefore;
	}
}
