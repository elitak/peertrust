package de.l3s.ppt.protuneparser;

public class StringDescription {
	private int beginOffset;
	private int endOffset;
	private String str;
	
	public StringDescription(String str, int beginOffset, int endOffset) {
		this.str = str;
		this.beginOffset = beginOffset;
		this.endOffset = endOffset;
	}

	public int getBeginOffset() {
		return beginOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}

	public String getStr() {
		return str;
	}

}
