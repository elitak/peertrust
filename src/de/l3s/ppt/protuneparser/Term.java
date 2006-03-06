package de.l3s.ppt.protuneparser;

import java.util.ArrayList;

public class Term {
	private boolean variable = false;
	private boolean constant = false;
	private boolean termList = false;
	
	private StringDescription str = null;
	private ArrayList list = null;
	
	public Term (boolean isVariable, StringDescription str) {
		this.str = str;
		if (isVariable) {
			variable = true;
		} else {
			constant = true;
		}
	}
	public Term (ArrayList list) {
		if (list == null) {
			this.list = new ArrayList();
		} else {
			this.list = list;
		}
		termList = true;
	}
	
	public String getImage() {
		StringBuffer buff = new StringBuffer();
		if (termList) {
			buff.append(Constants.OPENING_SQUARE_BRACKET);
			for (int i = 0; i < list.size(); i++) {
				Term term = (Term)list.get(i);
				buff.append(term.getImage());
				if (i != list.size() - 1) {
					buff.append(Constants.COMMA);
				}
			}
			buff.append(Constants.CLOSING_SQUARE_BRACKET);
		} else {
			buff.append(str.getStr());
		}
		return buff.toString();
	}

	public boolean isConstant() {
		return constant;
	}

	public ArrayList getList() {
		return list;
	}

	public StringDescription getStr() {
		return str;
	}

	public boolean isTermList() {
		return termList;
	}

	public boolean isVariable() {
		return variable;
	}
}
