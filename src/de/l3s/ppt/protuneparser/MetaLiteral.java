package de.l3s.ppt.protuneparser;

public class MetaLiteral {
	private MetaHeadLiteral metaHeadLiteral;
	private StringDescription negSymbol;
	private boolean hasNegSymbol;
	private boolean isLiteral;
	private Literal literal;
	
	public MetaLiteral(StringDescription negSymbol, MetaHeadLiteral metaHeadLiteral) {
		this.metaHeadLiteral = metaHeadLiteral;
		this.negSymbol = negSymbol;
		if (negSymbol == null) {
			hasNegSymbol = false;
		} else {
			hasNegSymbol = true;
		}
		isLiteral = false;
		literal = null;
	}
	public MetaLiteral(Literal literal) {
		metaHeadLiteral = null;
		negSymbol = null;
		hasNegSymbol = false;
		isLiteral = true;
		this.literal = literal;
	}
	public boolean hasNegSymbol() {
		return hasNegSymbol;
	}
	public boolean isLiteral() {
		return isLiteral;
	}
	public Literal getLiteral() {
		return literal;
	}
	public MetaHeadLiteral getMetaHeadLiteral() {
		return metaHeadLiteral;
	}
	public StringDescription getNegSymbol() {
		return negSymbol;
	}
}
