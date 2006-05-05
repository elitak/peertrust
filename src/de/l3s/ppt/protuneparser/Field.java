package de.l3s.ppt.protuneparser;

public class Field {
	private StringDescription attribute;
	private StringDescription value;
	
	public Field(StringDescription attribute, StringDescription value) {
		this.attribute = attribute;
		this.value = value;
	}
	public StringDescription getAttribute() {
		return attribute;
	}
	public StringDescription getValue() {
		return value;
	}
}
