/**
 * 
 */
package org.peertrust.demo.servlet.jsptags;

class PropertyValueToSetPair{
	private String property;
	private String valueToSet;
	
	public PropertyValueToSetPair(String property, String valueToSet) throws NullPointerException{
		if( property==null || valueToSet==null){
			throw new NullPointerException(
					"Parameter null: property="+property+
					" valueToSet="+valueToSet);
		}
		this.property=property;
		this.valueToSet=valueToSet;			
	}

	public String getProperty() {
		return property;
	}

	public void setProperty(String property) {
		this.property = property;
	}

	public String getValueToSet() {
		return valueToSet;
	}

	public void setValueToSet(String valueToSet) {
		this.valueToSet = valueToSet;
	}		
	
	
}