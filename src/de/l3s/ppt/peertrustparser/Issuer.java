package de.l3s.ppt.peertrustparser;

public class Issuer {
	private StringDescription issuer; 
	private Issuer additionalIssuer;
	
	public Issuer(StringDescription issuer, Issuer additonalIssuer) {
		this.issuer = issuer;
		this.additionalIssuer = additonalIssuer;
	}
	public Issuer(StringDescription issuer) {
		this.issuer = issuer;
		additionalIssuer = null;
	}
	public Issuer getAdditionalIssuer() {
		return additionalIssuer;
	}
	public StringDescription getIssuer() {
		return issuer;
	}
	public String getImage() {
		if (additionalIssuer == null) {
			return issuer.getStr();
		} else {
			return issuer.getStr() + Constants.ISSUER_CHAR + additionalIssuer.getImage();
		}
	}
}
