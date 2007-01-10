package org.protune.api;

import java.text.ParseException;

import org.protune.core.Action;
import org.protune.core.FilteredPolicy;
import org.protune.core.Goal;
import org.protune.core.Notification;

public interface Mapper {
	
	public String toPrologRepresentation(FilteredPolicy fp);
	
	public String toPrologRepresentation(Notification n);
	
	public String toPrologRepresentation(Goal g);
	
	public Action parseAction(String s) throws ParseException;
	
	public FilteredPolicy parseFilteredPolicy(String s) throws ParseException;
	
}
