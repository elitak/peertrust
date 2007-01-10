package org.protune.core;

import org.protune.api.Action;
import org.protune.api.FilteredPolicy;
import org.protune.api.Goal;
import org.protune.api.Policy;

/**
 * The objects of the classes implementing the interface <tt>FilterEngine</tt> carry out every activity
 * related to the filtering process of a policy.
 * @author jldecoi
 */
public interface FilterEngine {

	Action[] extractLocalActions(Goal g, Status s);
	
	Action[] extractExternalActions(Goal g, Status s);
	
	FilteredPolicy filter(Goal g, Status s, Policy p) throws Exception;
	
	boolean prove(Goal g, Status s) throws Exception;
	
}
