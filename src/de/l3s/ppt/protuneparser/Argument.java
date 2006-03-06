package de.l3s.ppt.protuneparser;

public class Argument {
    private boolean isTerm = false;
    private boolean startsWithPackage = false;
    private boolean startsWithPredicate = false;

    private Term term = null;

    private Arguments arguments = null;

    private StringDescription packageName = null;
    private StringDescription function = null;
    private StringDescription predicate = null;

    public Argument(Term term) {
        isTerm = true;
        this.term = term;
    }
    public Argument(StringDescription packageName, StringDescription function, Term term) {
        startsWithPackage = true;
        this.packageName = packageName;
        this.function = function;
        this.term = term;
    }
    public Argument(StringDescription predicate, Arguments arguments) {
        startsWithPredicate = true;
        this.predicate = predicate;
        this.arguments = arguments;
    }
    public boolean isTerm() {
        return isTerm;
    }
    public boolean startsWithPackage() {
        return startsWithPackage;
    }
    public boolean startsWithPredicate() {
        return startsWithPredicate;
    }
    public StringDescription getPackageName() {
        return packageName;
    }
    public StringDescription getFunction() {
        return function;
    }
    public StringDescription getPredicate() {
        return predicate;
    }
    public Term getTerm() {
        return term;
    }
    public Arguments getArguments() {
        return arguments;
    }
    public String getImage() {
    	StringBuffer buff = new StringBuffer();
    	if (isTerm) {
    		buff.append(term.getImage());
    	} else if (startsWithPredicate) {
    		buff.append(predicate.getStr());
    		buff.append(arguments.getImage());
    	} else {
    		buff.append(packageName.getStr());
    		buff.append(Constants.SEMICOLON);
    		buff.append(function.getStr());
    		buff.append(Constants.OPENING_BRACKET);
    		buff.append(term.getImage());
    		buff.append(Constants.CLOSING_BRACKET);
    	}
    	return buff.toString();
    }
}
