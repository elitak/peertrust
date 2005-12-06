package org.peertrust.demo.resourcemanagement;

import org.peertrust.config.Configurable;
import org.peertrust.exception.ConfigurationException;

/**
 * StringWrapper wraps a trings. Thus allowing it e.g. to be 
 * use as a resource in rdf config file.
 * 
 * @author Patrice congo (token77)
 *
 */
public class StringWrapper implements Configurable{
	/**
	 * The string which is being wrapped
	 */
	private String wrappedString;
	
	public StringWrapper(){
		//empty
	}

	/**
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException {
		if(wrappedString==null){
			throw new ConfigurationException("wrapped string not set");
		}
	}

	/**
	 * @return Returns the wrappedString.
	 */
	public String getWrappedString() {
		return wrappedString;
	}

	/**
	 * @param wrappedString The wrappedString to set.
	 */
	public void setWrappedString(String wrappedString) {
		this.wrappedString = wrappedString;
	}

	/**
	 * @see java.lang.Object#toString()
	 */
	public String toString() {
		return wrappedString;
	}	
}
