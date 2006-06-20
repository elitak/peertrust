package de.l3s.ppt.rcp.peertrust;

import java.util.Hashtable;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.SWT;

public class PeerTrustColorManager {
	public static final String DEFAULT_COLOR = "default";
	public static final String CONSTANT_COLOR = "constant";
	public static final String VARIABLE_COLOR = "variable";
	public static final String ISSUER_COLOR = "issuer";
	public static final String REQUESTER_COLOR = "requester";
	public static final String SIGNED_BY_COLOR = "signed_by";
	public static final String RESERVED_WORD_COLOR = "reserved_word";
	public static final String COMMENT_COLOR = "comment";
	public static final String ERROR_COLOR = "error";

	protected Hashtable colorTable;
	
	public PeerTrustColorManager() {
		colorTable = new Hashtable();
		Color backgroundColor = new Color(Display.getCurrent(), 255, 255, 255);
		Color defaultColor = new Color(Display.getCurrent(), 0, 0, 0);
		Color constantColor = new Color(Display.getCurrent(), 63, 95, 191); 
		Color variableColor = new Color(Display.getCurrent(), 0, 0, 192);
		Color issuerColor = new Color(Display.getCurrent(), 0, 200, 0);
		Color requesterColor = new Color(Display.getCurrent(), 255, 0, 0);
		Color signedByColor = new Color(Display.getCurrent(), 255, 140, 0);
		Color reservedWordColor = new Color(Display.getCurrent(), 127, 0, 85);
		Color commentColor = new Color(Display.getCurrent(), 63, 127, 95);
		Color errorColor = new Color(Display.getCurrent(), 255, 0, 0);

		colorTable.put(DEFAULT_COLOR, new TextAttribute(defaultColor, backgroundColor, SWT.NORMAL));
		colorTable.put(CONSTANT_COLOR, new TextAttribute(constantColor, backgroundColor, SWT.NORMAL));
		colorTable.put(VARIABLE_COLOR, new TextAttribute(variableColor, backgroundColor, SWT.BOLD));
		colorTable.put(ISSUER_COLOR, new TextAttribute(issuerColor, backgroundColor, SWT.NORMAL));
		colorTable.put(REQUESTER_COLOR, new TextAttribute(requesterColor, backgroundColor, SWT.NORMAL));
		colorTable.put(SIGNED_BY_COLOR, new TextAttribute(signedByColor, backgroundColor, SWT.NORMAL));
		colorTable.put(RESERVED_WORD_COLOR, new TextAttribute(reservedWordColor, backgroundColor, SWT.BOLD));
		colorTable.put(COMMENT_COLOR, new TextAttribute(commentColor, backgroundColor, SWT.NORMAL));
		colorTable.put(ERROR_COLOR, new TextAttribute(errorColor, backgroundColor, SWT.NORMAL));
	}
	
	public TextAttribute getTextAttribute(String color) {
		if (colorTable.containsKey(color)) {
			return (TextAttribute)colorTable.get(color);
		} else {
			return (TextAttribute)colorTable.get(DEFAULT_COLOR);
		}
	}
}
