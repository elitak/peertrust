package de.l3s.ppt.protuneparser;

public class SpecialLiteral {
	private boolean isIn;
	private boolean isDeclaration;
	private boolean isCredential;
	private StringDescription commandWord;
	private Function function;
	private PackageCall packageCall;
	private StringDescription id;
	private AnyTerm anyTerm;
	
	public SpecialLiteral(StringDescription commandWord, Function function, 
			PackageCall packageCall) {
		this.commandWord = commandWord;
		this.function = function;
		this.packageCall = packageCall;
		isIn = true;
		isDeclaration = false;
		isCredential = false;
		id = null;
		anyTerm = null;
	}
	public SpecialLiteral(StringDescription commandWord, boolean isDeclaration, 
			StringDescription id, AnyTerm anyTerm) {
		this.commandWord = commandWord;
		function = null;
		packageCall = null;
		isIn = false;
		this.isDeclaration = isDeclaration;
		this.isCredential = !isDeclaration;
		this.id = id;
		this.anyTerm = anyTerm;
	}
	public boolean isIn() {
		return isIn;
	}
	public AnyTerm getAnyTerm() {
		return anyTerm;
	}
	public StringDescription getCommandWord() {
		return commandWord;
	}
	public StringDescription getId() {
		return id;
	}
	public boolean isCredential() {
		return isCredential;
	}
	public boolean isDeclaration() {
		return isDeclaration;
	}
	public PackageCall getPackageCall() {
		return packageCall;
	}
	public Function getFunction() {
		return function;
	}
}
