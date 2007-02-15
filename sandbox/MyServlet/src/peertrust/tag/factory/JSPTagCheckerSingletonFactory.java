package peertrust.tag.factory;

import peertrust.tag.impl.JSPTagChecker;
import peertrust.tag.interfaces.IJSPTagChecker;

/**
 * Factory that delivers a checker for the JSP PolicyConditionTag.
 * @see IJSPTagChecker
 * @author Sebastian Wittler
 */
public class JSPTagCheckerSingletonFactory {
	// Singleton for checker
	private static IJSPTagChecker checker=null;
	// Singleton for factory
	private static JSPTagCheckerSingletonFactory factory=null;

	/**
	 * Returns a singleton instance of the factory.
	 * @return Factory.
	 */
	public static JSPTagCheckerSingletonFactory getInstance() {
		if(factory==null)
			factory=new JSPTagCheckerSingletonFactory();
		return factory;
	}
	
	/**
	 * Returns a singleton instance of the checker.
	 * @return Checker.
	 */
	public IJSPTagChecker getChecker() {
		if(checker==null)
			checker=new JSPTagChecker();
		return checker;
	}

	/**
	 * Constructor private because of singleton.
	 */
	private JSPTagCheckerSingletonFactory() {
	}
}
