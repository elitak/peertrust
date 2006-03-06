package de.l3s.ppt.peertrustparser;

public class StringDescription {
	private int beginOffset;
	private int endOffset;
	private String str;
	private boolean isConstant;
	
	public StringDescription(String str, int beginOffset, int endOffset, boolean isConstant) {
		this.str = str;
		this.beginOffset = beginOffset;
		this.endOffset = endOffset;
		this.isConstant = isConstant;
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

	public boolean isConstant() {
		return isConstant;
	}

}
