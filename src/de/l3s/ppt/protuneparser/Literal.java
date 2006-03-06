package de.l3s.ppt.protuneparser;

import java.util.ArrayList;

public class Literal {
	private boolean termOperatorTerm = false;
	private boolean predicateArguments = false;
	private boolean declaration = false;
	
	private ArrayList variableBindingList = null;
	private Term termBefore = null;
	private Term termAfter = null;
	private StringDescription operator = null;
	private StringDescription predicate = null;
	private StringDescription commandWord = null;
	private Arguments arguments = null;
	
	public Literal(StringDescription commandWord, ArrayList variableBindingList) {
		this.commandWord = commandWord;
		this.variableBindingList = variableBindingList;
		declaration = true;
	}
	public Literal(Term termBefore, StringDescription operator, Term termAfter) {
		this.termBefore = termBefore;
		this.operator = operator;
		this.termAfter = termAfter;
		termOperatorTerm = true;
	}
	public Literal(StringDescription predicate, Arguments arguments) {
		this.predicate = predicate;
		this.arguments = arguments;
		predicateArguments = true;
	}
	
	public String getImage() {
		StringBuffer buff = new StringBuffer();
		if (declaration) {
			buff.append(Constants.DECLARATION);
			buff.append(Constants.OPENING_BRACKET);
			buff.append(Constants.OPENING_SQUARE_BRACKET);
			for (int i = 0; i < variableBindingList.size(); i++) {
				VariableBinding binding = (VariableBinding)variableBindingList.get(i);
				buff.append(binding.getImage());
				if (i != variableBindingList.size() - 1) {
					buff.append(Constants.COMMA);
				}
			}
			buff.append(Constants.CLOSING_SQUARE_BRACKET);
			buff.append(Constants.CLOSING_BRACKET);
		} else if (termOperatorTerm) {
			buff.append(termBefore.getImage());
			buff.append(operator.getStr());
			buff.append(termAfter.getImage());
		} else {
			buff.append(predicate.getStr());
			buff.append(arguments.getImage());
		}
		return buff.toString();
	}

	public boolean isDeclaration() {
		return declaration;
	}

	public boolean isPredicateArguments() {
		return predicateArguments;
	}

	public boolean isTermOperatorTerm() {
		return termOperatorTerm;
	}

	public ArrayList getVariableBindingList() {
		return variableBindingList;
	}
	public StringDescription getOperator() {
		return operator;
	}
	public Term getTermAfter() {
		return termAfter;
	}
	public Term getTermBefore() {
		return termBefore;
	}
	public Arguments getArguments() {
		return arguments;
	}
	public StringDescription getPredicate() {
		return predicate;
	}
	public StringDescription getCommandWord() {
		return commandWord;
	}

}
