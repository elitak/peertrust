package test.org.protune;

public class Dummy {

	protected String a;
	protected String b;
	
	public Dummy(String s1, String s2){
		a = s1;
		b = s2;
	}
	
	public String toString(){
		return "a=\"" + a + "\", b=\"" + b + "\"";
	}
	
}
