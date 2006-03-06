package de.l3s.ppt.peertrustparser;

public class ExtendedLiteral {
	private Literal literal;
	private Issuer issuer;
	private Requester requester;
	
	public ExtendedLiteral( Literal literal, Issuer issuer, Requester requester) {
		this.literal = literal;
		this.issuer = issuer;
		this.requester = requester;
	}
	public String getImage() {
		StringBuffer image = new StringBuffer();
		image.append(literal.getImage());
		if (issuer != null ) {
			image.append(Constants.ISSUER_CHAR);
			image.append(issuer.getImage());
		}
		if (requester != null ) {
			image.append(Constants.REQUESTER_CHAR);
			image.append(requester.getImage());
		}
		return image.toString();
	}
	public Issuer getIssuer() {
		return issuer;
	}
	public Literal getLiteral() {
		return literal;
	}
	public Requester getRequester() {
		return requester;
	}
}
