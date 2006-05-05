package de.l3s.ppt.protuneparser;

import java.util.ArrayList;

public class ComplexTerm {
	private StringDescription variableOrConstant;
	private ArrayList fieldList;
	
	public ComplexTerm(StringDescription variableOrConstant, ArrayList fieldList) {
		this.variableOrConstant = variableOrConstant;
		this.fieldList = fieldList;
	}
	public ArrayList getFieldList() {
		return fieldList;
	}
	public StringDescription getVariableOrConstant() {
		return variableOrConstant;
	}

}
