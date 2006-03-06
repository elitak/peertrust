package de.l3s.ppt.protuneparser;

public class ComplexTerm {
	private StringDescription variable = null;
	private StringDescription attribute = null;
	private Term term = null;
	
	public ComplexTerm(StringDescription variable, StringDescription attribute, Term term) {
		this.variable = variable;
		this.attribute = attribute;
		this.term = term;
	}
	public String getImage() {
		StringBuffer buff = new StringBuffer();
		buff.append(variable.getStr());
		buff.append(Constants.DOT);
		buff.append(attribute.getStr());
		buff.append(Constants.SEMICOLON);
		buff.append(term.getImage());
		return buff.toString();
	}
	public StringDescription getAttribute() {
		return attribute;
	}
	public Term getTerm() {
		return term;
	}
	public StringDescription getVariable() {
		return variable;
	}

}
