package org.peertrust.inference.prolog.yprolog;

/** Interface that can be implemented by a class that you want to be
 * convertible to/from a Prolog kb.
 */
public interface PrologObject {

	/** Convert the object to an array of Strings that can be converted to a
	* list of parameters for a Prolog clause.  Each String begins with a
	* character denoting the type of the parameter, followed by the string
	* that describes the parameter itself.  The following types are defined:
	* "a" = an atom;  "e" = a Prolog expression.  Atom parameters are quoted,
	* so they are always interpreted as atoms, expression parameters are not
	* quoted, and are parsed by the prolog interpreter.
	* Example:
	* <pre>
	*   class MyClass implements PrologObject {
	*       int field1;  double field2; String [] field3;
	*       public String [] toPrologTable() {
	*          return new String [] {
	*              "a"+field1,  "a"+field2,
	*              "e["+YProlog.arrayToString(field3)+"]" };
	*       }
	*   }
	* </pre>
	*/
	public String [] toPrologTable();

}
