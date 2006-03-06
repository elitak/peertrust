package de.l3s.ppt.peertrustparser;

public class RuleBody {
	private StringDescription signedBy;
	private StringDescription commandWord;
	private Guards body;
	private Guards policy;
	
	public RuleBody(Guards policy, StringDescription commandWord,
			StringDescription signedBy, Guards body) {
		this.policy = policy;
		this.body = body;
		this.signedBy = signedBy;
		this.commandWord = commandWord;
	}
	public Guards getBody() {
		return body;
	}
	public StringDescription getSignedBy() {
		return signedBy;
	}
	public Guards getPolicy() {
		return policy;
	}
	public String getImage() {
		StringBuffer image = new StringBuffer();
		if (policy != null) {
			image.append(policy.getImage());
		}
		if (signedBy != null) {
			image.append(Constants.SIGNED_BY);
			image.append(Constants.OPENING_SQUARE_BRACKET);
			image.append(signedBy.getStr());
			image.append(Constants.CLOSING_SQUARE_BRACKET);
		}
		if (body != null) {
			image.append(body.getImage());
		}
		return image.toString();
	}
	public StringDescription getCommandWord() {
		return commandWord;
	}
}
