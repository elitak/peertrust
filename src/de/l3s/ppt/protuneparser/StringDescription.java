package de.l3s.ppt.protuneparser;

public class StringDescription {
	private int beginOffset;
	private int endOffset;
	private String str;
	private int type;
	
	public static final int CONSTANT_TYPE = 0x01;
	public static final int VARIABLE_TYPE = 0x02;
	public static final int STRING_CONSTANT_TYPE = 0x04;
	public static final int RULE_SEPARATOR_TYPE = 0x08;
	public static final int NEG_SYMBOL_TYPE = 0x10;
	public static final int OPERATOR_TYPE = 0x20;
	public static final int VALUE_TYPE = 0x40;
	public static final int ID_TYPE = 0x80;
	public static final int NUMBER_TYPE = 0x100;
	public static final int PREDICATE_TYPE = 0x200;
	public static final int PACKAGE_TYPE = 0x400;
	public static final int TERM_TYPE = 0x800;
	public static final int RESERVED_WORD_TYPE = 0x1000;
	public static final int ATTRIBUTE_TYPE = 0x2000;
	public static final int COMMENT_TYPE = 0x4000;
	public static final int QUOTED_TYPE = 0x8000;
	
	public StringDescription(String str, int beginOffset, int endOffset, int type) {
		this.str = str;
		this.beginOffset = beginOffset;
		this.endOffset = endOffset;
		this.type = type;
	}

	public int getBeginOffset() {
		return beginOffset;
	}

	public int getEndOffset() {
		return endOffset;
	}

	public String getStr() {
		return str;
	}
	
	public void addType(int additionalType) {
		type = type | additionalType;
	}
	
	public boolean isConstant() {
		if ((type & CONSTANT_TYPE) == 1) return true;
		else return false;
	}
	public boolean isVariable() {
		if ((type & VARIABLE_TYPE) == 1) return true;
		else return false;
	}
	public boolean isStringConstant() {
		if ((type & STRING_CONSTANT_TYPE) == 1) return true;
		else return false;
	}
	public boolean isRuleSeparator() {
		if ((type & RULE_SEPARATOR_TYPE) == 1) return true;
		else return false;
	}
	public boolean isNegSymbol() {
		if ((type & NEG_SYMBOL_TYPE) == 1) return true;
		else return false;
	}
	public boolean isOperator() {
		if ((type & OPERATOR_TYPE) == 1) return true;
		else return false;
	}
	public boolean isValue() {
		if ((type & VALUE_TYPE) == 1) return true;
		else return false;
	}
	public boolean isId() {
		if ((type & ID_TYPE) == 1) return true;
		else return false;
	}
	public boolean isNumber() {
		if ((type & NUMBER_TYPE) == 1) return true;
		else return false;
	}
	public boolean isPredicate() {
		if ((type & PREDICATE_TYPE) == 1) return true;
		else return false;
	}
	public boolean isPackage() {
		if ((type & PACKAGE_TYPE) == 1) return true;
		else return false;
	}
	public boolean isTerm() {
		if ((type & TERM_TYPE) == 1) return true;
		else return false;
	}
	public boolean isReservedWord() {
		if ((type & RESERVED_WORD_TYPE) == 1) return true;
		else return false;
	}
	public boolean isAttribute() {
		if ((type & ATTRIBUTE_TYPE) == 1) return true;
		else return false;
	}
	public boolean isComment() {
		if ((type & COMMENT_TYPE) == 1) return true;
		else return false;
	}
	public boolean isQuoted() {
		if ((type & QUOTED_TYPE) == 1) return true;
		else return false;
	}
	public int getType() {
		return type;
	}
}
