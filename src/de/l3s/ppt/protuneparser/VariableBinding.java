package de.l3s.ppt.protuneparser;

public class VariableBinding {
	private StringDescription variable;
	private StringDescription constant;
	
	public VariableBinding( StringDescription variable, StringDescription constant) {
		this.variable = variable;
		this.constant = constant;
	}
	public StringDescription getConstant() {
		return constant;
	}
	public StringDescription getVariable() {
		return variable;
	}
	public String getImage() {
		StringBuffer buff = new StringBuffer();
		buff.append(variable.getStr());
		buff.append(Constants.EQUAL);
		buff.append(constant.getStr());
		return buff.toString();
	}
}
