package de.l3s.ppt.peertrustparser;

public class Requester {
	private StringDescription requester;
	private Requester additionalRequester;
	
	public Requester( StringDescription requester, Requester additionalRequester) {
		this.requester = requester;
		this.additionalRequester = additionalRequester;
	}
	public Requester( StringDescription requester) {
		this.requester = requester;
		additionalRequester = null;
	}
	public Requester getAdditionalRequester() {
		return additionalRequester;
	}
	public StringDescription getRequester() {
		return requester;
	}
	public String getImage() {
		if (additionalRequester == null) {
			return requester.getStr();
		} else {
			return requester.getStr() + Constants.REQUESTER_CHAR + additionalRequester.getImage();
		}
	}
}
