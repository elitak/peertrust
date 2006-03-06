package de.l3s.ppt.protuneparser;

public class MetaLiteral {
	private boolean hasNegSymbol = false;
	private boolean hasAttributeAndValue = false;
	private StringDescription negSymbol = null;
	private Literal literal = null;
	private StringDescription attribute = null;
	private Term value = null;
	
	public MetaLiteral(StringDescription negSymbol, Literal literal, StringDescription attribute, Term value) {
		if (negSymbol != null) {
			this.negSymbol = negSymbol;
			hasNegSymbol = true;
		}
		this.literal = literal;
		if (attribute != null) {
			this.attribute = attribute;
			this.value = value;
			hasAttributeAndValue = true;
		}
	}
	
	public String getImage() {
		StringBuffer buff = new StringBuffer();
		if (hasNegSymbol) {
			buff.append(negSymbol.getStr());
		}
		buff.append(literal.getImage());
		if (hasAttributeAndValue) {
			buff.append(Constants.DOT);
			buff.append(attribute.getStr());
			buff.append(Constants.SEMICOLON);
			buff.append(value.getImage());
		}
		return buff.toString();
	}

	public StringDescription getAttribute() {
		return attribute;
	}

	public boolean hasNegSymbol() {
		return hasNegSymbol;
	}

	public Literal getLiteral() {
		return literal;
	}

	public StringDescription getNegSymbol() {
		return negSymbol;
	}

	public Term getValue() {
		return value;
	}

	public boolean hasAttributeAndValue() {
		return hasAttributeAndValue;
	}

}
