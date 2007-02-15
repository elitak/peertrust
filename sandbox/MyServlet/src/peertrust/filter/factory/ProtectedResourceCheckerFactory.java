package peertrust.filter.factory;

import peertrust.filter.impl.RDFProtectedResourceChecker;
import peertrust.filter.interfaces.IProtectedResourceChecker;

/**
 * Factory for a checker object, that can determine if a resource is protected
 * by policies or if the client fullfills these.
 * @see IProtectedResourceChecker
 * @author Sebastian Wittler
 */
public class ProtectedResourceCheckerFactory {
	// This class is a singleton
	private static ProtectedResourceCheckerFactory checkerFactory=null;

	/**
	 * Singleton method to obtain an instance of the factory.
	 * @return Singleton object of this factory.
	 */
	public static ProtectedResourceCheckerFactory getInstance() {
		if(checkerFactory==null)
			checkerFactory=new ProtectedResourceCheckerFactory();
		return checkerFactory;
	}
	
	/**
	 * Returns a resource checker object.
	 * @return The resource checker.
	 */
	public IProtectedResourceChecker createChecker() {
		return new RDFProtectedResourceChecker();
	}

	/**
	 * Factory is singleton, therefore the constructor is private.
	 */
	private ProtectedResourceCheckerFactory() {
	}
}
