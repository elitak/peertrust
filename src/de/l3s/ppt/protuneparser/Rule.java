package de.l3s.ppt.protuneparser;

import java.util.ArrayList;

public class Rule{
	private boolean isMetaRule = false;
	private boolean hasId = false;
	private boolean hasNegSymbol = false;
	private StringDescription id = null;
	private StringDescription idSeparator = null;
	private StringDescription negSymbol = null;
	private StringDescription ruleSep = null;
	private Literal head = null;
	private MetaLiteral metaHead = null;
	private ArrayList body = null;
	public int offsetInInput = -1;

	public Rule( StringDescription id, StringDescription idSeparator, StringDescription negSymbol,
			Literal head, StringDescription ruleSep, ArrayList body, int offset) {
		if (id != null) {
			hasId = true;
			this.id = id;
			this.idSeparator = idSeparator;
		}
		if (negSymbol != null) {
			hasNegSymbol = true;
			this.negSymbol = negSymbol;
		}
		this.head = head;
		this.ruleSep = ruleSep;
		if (body == null) {
			this.body = new ArrayList();
		} else {
			this.body = body;
		}
		this.offsetInInput = offset;
	}
	public Rule( StringDescription id, StringDescription idSeparator, MetaLiteral head, 
			StringDescription ruleSep, ArrayList body, int offset) {
		isMetaRule = true;
		if (id != null) {
			hasId = true;
			this.id = id;
			this.idSeparator = idSeparator;
		}
		this.metaHead = head;
		this.ruleSep = ruleSep;
		if (body == null) {
			this.body = new ArrayList();
		} else {
			this.body = body;
		}
		this.offsetInInput = offset;
	}
	public String getImage() {
		StringBuffer buff = new StringBuffer();
		if (hasId) {
			buff.append(id.getStr());
			buff.append(idSeparator.getStr());
		}
		if (hasNegSymbol) {
			buff.append(negSymbol.getStr());
		}
		if (isMetaRule) {
			buff.append(metaHead.getImage());
		} else {
			buff.append(head.getImage());
		}
		if (body.size() != 0) {
			buff.append(ruleSep.getStr());
			for (int i = 0; i < body.size(); i++) {
				Object obj = body.get(i);
				if (obj instanceof Literal) {
					Literal literal = (Literal) obj;
					buff.append(literal.getImage());
				} else if (obj instanceof MetaLiteral) {
					MetaLiteral literal = (MetaLiteral) obj;
					buff.append(literal.getImage());
				} else if (obj instanceof ComplexTerm) {
					ComplexTerm complexTerm = (ComplexTerm) obj;
					buff.append(complexTerm.getImage());
				}
				if (i != body.size() -1) {
					buff.append(Constants.COMMA);
				}
			}
		}
		buff.append(Constants.DOT);
		return buff.toString();
	}
	public boolean hasId() {
		return hasId;
	}
	public StringDescription getId() {
		return id;
	}
	public StringDescription getIdSeparator() {
		return idSeparator;
	}
	public boolean hasNegSymbol() {
		return hasNegSymbol;
	}
	public StringDescription getNegSymbol() {
		return negSymbol;
	}
	public ArrayList getBody() {
		return body;
	}
	public Literal getHead() {
		return head;
	}
	public boolean isMetaRule() {
		return isMetaRule;
	}
	public MetaLiteral getMetaHead() {
		return metaHead;
	}
	public int getOffsetInInput() {
		return offsetInInput;
	}

}
