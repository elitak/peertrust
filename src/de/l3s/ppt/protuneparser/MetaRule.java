package de.l3s.ppt.protuneparser;

import java.util.ArrayList;

public class MetaRule {
	private MetaHeadLiteral metaHeadLiteral;
	private StringDescription ruleSeparator;
	private boolean hasRuleSeparator;
	private ArrayList metaBody;
	public int offsetInInput = -1;
	
	public MetaRule(MetaHeadLiteral metaHeadLiteral, StringDescription ruleSeparator, ArrayList metaBody, int endOffset) {
		this.metaHeadLiteral = metaHeadLiteral;
		if (ruleSeparator == null) {
			this.ruleSeparator = null;
			hasRuleSeparator = false;
			this.metaBody = null;
		} else {
			this.ruleSeparator = ruleSeparator;
			hasRuleSeparator = true;
			this.metaBody = metaBody;
		}
		offsetInInput = endOffset;
	}
	public boolean hasRuleSeparator() {
		return hasRuleSeparator;
	}
	public ArrayList getMetaBody() {
		return metaBody;
	}
	public MetaHeadLiteral getMetaHeadLiteral() {
		return metaHeadLiteral;
	}
	public StringDescription getRuleSeparator() {
		return ruleSeparator;
	}
	public int getOffsetInInput() {
		return offsetInInput;
	}
}
