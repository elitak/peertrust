package de.l3s.ppt.rcp.protune;

import java.util.Hashtable;

import org.eclipse.jface.text.TextAttribute;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class ProtuneColorManager {
	public static final String DEFAULT_COLOR = "default";
	public static final String CONSTANT_COLOR = "constant";
	public static final String VARIABLE_COLOR = "variable";
	public static final String ID_COLOR = "id";
	public static final String RESERVED_WORD_COLOR = "reserved_word";
	public static final String META_DEFAULT_COLOR = "meta_default";
	public static final String META_CONSTANT_COLOR = "meta_constant";
	public static final String META_VARIABLE_COLOR = "meta_variable";
	public static final String META_ID_COLOR = "meta_id";
	public static final String META_RESERVED_WORD_COLOR = "meta_reserved_word";
	public static final String COMMENT_COLOR = "comment";
	public static final String ERROR_COLOR = "error";
	
	protected Hashtable colorTable;
	
	public ProtuneColorManager() {
		colorTable = new Hashtable();
		Color backgroundColor = new Color(Display.getCurrent(), 255, 255, 255);
		Color defaultColor = new Color(Display.getCurrent(), 0, 0, 0);
		Color constantColor = new Color(Display.getCurrent(), 63, 95, 191); 
		Color variableColor = new Color(Display.getCurrent(), 0, 0, 192);
		Color idColor = new Color(Display.getCurrent(), 127, 127, 127);
		Color reservedWordColor = new Color(Display.getCurrent(), 127, 0, 85);
		Color commentColor = new Color(Display.getCurrent(), 63, 127, 95);
		Color errorColor = new Color(Display.getCurrent(), 255, 0, 0);

		colorTable.put(DEFAULT_COLOR, new TextAttribute(defaultColor, backgroundColor, SWT.NORMAL));
		colorTable.put(CONSTANT_COLOR, new TextAttribute(constantColor, backgroundColor, SWT.NORMAL));
		colorTable.put(VARIABLE_COLOR, new TextAttribute(variableColor, backgroundColor, SWT.BOLD));
		colorTable.put(ID_COLOR, new TextAttribute(idColor, backgroundColor, SWT.NORMAL));
		colorTable.put(RESERVED_WORD_COLOR, new TextAttribute(reservedWordColor, backgroundColor, SWT.BOLD));
		colorTable.put(META_DEFAULT_COLOR, new TextAttribute(defaultColor, backgroundColor, SWT.ITALIC));
		colorTable.put(META_CONSTANT_COLOR, new TextAttribute(constantColor, backgroundColor, SWT.ITALIC));
		colorTable.put(META_VARIABLE_COLOR, new TextAttribute(variableColor, backgroundColor, SWT.BOLD | SWT.ITALIC));
		colorTable.put(META_ID_COLOR, new TextAttribute(idColor, backgroundColor, SWT.ITALIC));
		colorTable.put(META_RESERVED_WORD_COLOR, new TextAttribute(reservedWordColor, backgroundColor, SWT.BOLD | SWT.ITALIC));
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
