package net.jxta.edutella.util;
/**
 * Represents a command line option as needed by Configurable, ...
 * 
 * 
 */

public class Option {
	public boolean isFlag;
	public String shortName;
	public String longName;
	public String description;
	public boolean required;
	public String defaultValue = null;
	public String value = null;
	public String label;
	public Class type = null;
	public boolean isPassword = false;

	/**
	 * @param shortName Short name of the option; 'f' means it 
	 *         can be set using "-f" on the command line
	 * @param longName Long name of the option; can be used as
	 *         "-longName" on the command line or using a property
	 *         "myapp.longName" in the property file.
	 */
	public Option(char shortName, String longName, String label, String description) {
		this(
			shortName,
			longName,
			description,
			false,
			Boolean.FALSE.toString(),
			true);
		this.label = label;
	}

	/**
	 * @param shortName Short name of the option; 'f' means it 
	 *         can be set using "-f" on the command line
	 * @param longName Long name of the option; can be used as
	 *         "-longName" on the command line or using a property
	 *         "myapp.longName" in the property file.
	 */
	public Option(char shortName, String longName, String label, String description, boolean isRequired) {
		this(
			shortName,
			longName,
			description,
			isRequired,
			Boolean.FALSE.toString(),
			true);
		this.label = label;
	}

	/**
	 * @param shortName Short name of the option; 'f' means it 
	 *         can be set using "-f" on the command line
	 * @param longName Long name of the option; can be used as
	 *         "-longName" on the command line or using a property
	 *         "myapp.longName" in the property file.
	 */
	public Option(char shortName, String longName, String description) {
		this(
			shortName,
			longName,
			description,
			false,
			Boolean.FALSE.toString(),
			true);
	}

	public Option(
		char shortName,
		String longName,
		String description,
		boolean required,
		String defaultValue,
		boolean isFlag) {
		if (longName == null || description == null) {
			throw new IllegalArgumentException("longName and description must be set");
		}
		if (shortName >= '1' && shortName <= '9') {
			if (!required) {
				throw new IllegalArgumentException("args without option name must be required");
			}
		} else if (shortName != ' ' && (shortName < 'a' || shortName > 'z')) {
			throw new IllegalArgumentException(
				"option name '"
					+ shortName
					+ "' invalid. must be between a and z");
		}
		this.isFlag = false;
		this.required = required;
		this.defaultValue = defaultValue;
		this.shortName = new Character(shortName).toString();
		this.longName = longName;
		this.description = description;
		this.isFlag = isFlag;
	}

	/**
	 * Returns the description.
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Returns the isFlag.
	 * @return boolean
	 */
	public boolean isFlag() {
		return isFlag;
	}

	/**
	 * Returns the longName.
	 * @return String
	 */
	public String getName() {
		int lastDot = longName.lastIndexOf('.');
		if ( lastDot == -1 ) {
			return longName;
		} else {
			return longName.substring(lastDot+1, longName.length());
		}
	}

	/**
	 * Returns the required.
	 * @return boolean
	 */
	public boolean isRequired() {
		return required;
	}

	/**
	 * Returns the shortName.
	 * @return String
	 */
	public String getShortName() {
		return shortName;
	}

	/**
	 * Returns the value.
	 * @return String
	 */
	public String getValue() {
		return value;
	}

	/**
	 * Returns the longName.
	 * @return String
	 */
	public String getLongName() {
		return longName;
	}

	/**
	 * Sets the value.
	 * @param value The value to set
	 */
	public void setValue(String value) {
		this.value = value;
	}

	/**
	 * Returns the label.
	 * @return String
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * Method hasShortName.
	 * @return char
	 */
	public boolean hasShortName() {
		return !getShortName().equals(" ");
	}

	/**
	 * Returns the type.
	 * @return Class
	 */
	public Class getType() {
		return type;
	}

	/**
	 * Sets the type.
	 * @param type The type to set
	 */
	public void setType(Class type) {
		this.type = type;
	}

	/**
	 * Returns the isPassword.
	 * @return boolean
	 */
	public boolean isPassword() {
		return isPassword;
	}

	/**
	 * Sets the isPassword.
	 * @param isPassword The isPassword to set
	 */
	public void setIsPassword(boolean isPassword) {
		this.isPassword = isPassword;
	}

	/**
	 * Sets the label for this option
	 * @param string
	 */
	public void setLabel(String label) {
		this.label = label;		
	}

}
