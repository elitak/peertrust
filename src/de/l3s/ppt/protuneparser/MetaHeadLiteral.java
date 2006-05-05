package de.l3s.ppt.protuneparser;

public class MetaHeadLiteral {
	private StringDescription id;
	private HeadLiteral headLiteral;
	private Field field;
	private boolean hasId;
	
	public MetaHeadLiteral(StringDescription id, Field field) {
		this.id = id;
		this.field = field;
		headLiteral = null;
		hasId = true;
	}
	public MetaHeadLiteral(HeadLiteral headLiteral, Field field) {
		id = null;
		this.field = field;
		this.headLiteral = headLiteral;
		hasId = false;
	}
	public Field getField() {
		return field;
	}
	public boolean hasId() {
		return hasId;
	}
	public HeadLiteral getHeadLiteral() {
		return headLiteral;
	}
	public StringDescription getId() {
		return id;
	}
	/*
	public String getIdImage() {
		if (id != null) {
			return id.getStr();
		} else {
			return null;
		}
	}
	public String getHeadLiteralImage() {
		if (headLiteral != null) {
			//return id.getStr();
		} else {
			return null;
		}
	}
	*/
}
