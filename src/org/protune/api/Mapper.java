package org.protune.api;

import java.text.ParseException;

public interface Mapper {
	
	String toPrologRepresentation(FilteredPolicy fp);
	
	String toPrologRepresentation(Notification n);
	
	String toPrologRepresentation(Goal g);
	
	Action parseAction(String s) throws ParseException;
	
	FilteredPolicy parseFilteredPolicy(String s) throws ParseException;
	
}
