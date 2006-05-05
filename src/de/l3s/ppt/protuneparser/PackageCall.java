package de.l3s.ppt.protuneparser;

public class PackageCall {
	private StringDescription packageName;
	private Function function;
	
	public PackageCall(StringDescription packageName, Function function) {
		this.packageName = packageName;
		this.function = function;
	}
	public Function getFunction() {
		return function;
	}
	public StringDescription getPackageName() {
		return packageName;
	}
}
