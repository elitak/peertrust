package de.l3s.ppt.peertrustparser;

public class Rule {
	private ExtendedLiteral head;
	private boolean hasSeparator;
	private RuleBody body;
	public int offsetInInput = -1;
	
	public Rule( ExtendedLiteral head, boolean hasSearator, RuleBody body, int offset) {
		this.head = head;
		this.hasSeparator = hasSearator;
		this.body = body;
		this.offsetInInput = offset;
	}
	public String getImage() {
		StringBuffer image = new StringBuffer();
		image.append(head.getImage());
		if (hasSeparator) {
			image.append(Constants.RULE_SEPARATOR);
		}
		if (body != null) {
			image.append(body.getImage());
		}
		image.append(Constants.DOT);
		return image.toString();
	}
	public RuleBody getBody() {
		return body;
	}
	public boolean isHasSeparator() {
		return hasSeparator;
	}
	public ExtendedLiteral getHead() {
		return head;
	}
}
