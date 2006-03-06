package de.l3s.ppt.peertrustparser;

import java.util.ArrayList;

public class Guards {
	private ArrayList beforeSlash;
	private ArrayList afterSlash;
	private boolean hasSlash;
	
	public Guards(ArrayList beforeSlash, ArrayList afterSlash, boolean hasSlash) {
		if (beforeSlash != null) {
			this.beforeSlash = beforeSlash;
		} else {
			this.beforeSlash = new ArrayList();
		}
		if (afterSlash != null) {
			this.afterSlash = afterSlash;
		} else {
			this.afterSlash = new ArrayList();
		}
		this.hasSlash = hasSlash;
	}
	public ArrayList getAfterSlash() {
		return afterSlash;
	}
	public ArrayList getBeforeSlash() {
		return beforeSlash;
	}
	public boolean HasSlash() {
		return hasSlash;
	}
	public String getImage() {
		StringBuffer image = new StringBuffer();
		image.append(getExtLiteralsImage(beforeSlash));
		if (hasSlash) {
			image.append(Constants.VERTICAL_SLASH);
			image.append(getExtLiteralsImage(afterSlash));
		}
		return image.toString();
	}
	private String getExtLiteralsImage(ArrayList list) {
		StringBuffer image = new StringBuffer();
		for (int i = 0; i < list.size(); i++) {
			image.append(((ExtendedLiteral)list.get(i)).getImage());
			if ( i != list.size() - 1 ) {
				image.append(Constants.COMMA);
			}
		}
		return image.toString();
	}
}
