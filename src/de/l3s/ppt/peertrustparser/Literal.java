package de.l3s.ppt.peertrustparser;

import java.util.ArrayList;

public class Literal {
	private StringDescription predicate;
	private ArrayList arguments;
	
	public Literal( StringDescription predicate) {
		this.predicate = predicate;
		arguments = null;
	}
	public Literal( StringDescription predicate, ArrayList arguments) {
		this.predicate = predicate;
		this.arguments = arguments;
	}
	public StringDescription getPredicate() {
		return predicate;
	}
	public ArrayList getArguments() {
		return arguments;
	}
	public String getImage() {
		StringBuffer image = new StringBuffer();
		image.append(predicate.getStr());
		image.append(Constants.OPENING_BRACKET);
		if (arguments != null) {
			for (int i = 0; i < arguments.size(); i++) {
				image.append(getArgumentImage(arguments.get(i)));
				if ( i != arguments.size() - 1 ) {
					image.append(Constants.COMMA);
				}
			}
		}
		image.append(Constants.CLOSING_BRACKET);
		return image.toString();
	}
	private String getArgumentImage(Object obj) {
		if (obj instanceof StringDescription) {
			return ((StringDescription)obj).getStr();
		} else {
			ArrayList list = (ArrayList) obj;
			StringBuffer image = new StringBuffer();
			image.append(Constants.OPENING_SQUARE_BRACKET);
			for (int i = 0; i < list.size(); i++) {
				image.append(getArgumentImage(list.get(i)));
				if ( i != list.size() - 1 ) {
					image.append(Constants.COMMA);
				}
			}
			image.append(Constants.CLOSING_SQUARE_BRACKET);
			return image.toString();
		}
	}
}
