package org.protune.api;

import java.text.ParseException;
import org.protune.core.*;

/**
 * The interface <tt>Mapper</tt> provides conversion utilities between the Java (resp. Prolog) part and
 * the Prolog (resp. Java) part of the system. The <i>Protune</i> system deals with a number of entities (e.g.
 * notifications, checks, policies ...) which are represented in different ways in the Java (resp.
 * Prolog) subsystem. The methods of the interface <tt>Mapper</tt> convert these entities between the
 * one and the other representation.<br />
 * <b>NOTE:</b> The interface <tt>Mapper</tt> should be aware of each entity available in the system
 * (which requires to be represented both in the Java and Prolog subsystems), therefore whenever a new
 * entity of this kind is added, the interface <tt>Mapper</tt> (all classes implementing it) should be
 * modified. On the other hand it provides many advantages.
 * <ul>
 * <li>It would have been possible to define for each entity available in the system
 *   <ul>
 *   <li>a constructor <tt>&lt;entityName&gt;(String)</tt> creating a Java object (a Java representation
 *   of the entity) starting from its Prolog representation.</li>
 *   <li>a method <tt>toString()</tt> returning a string representing the entity (a Prolog
 *   representation of the entity) starting from its Java representation.</li>
 *   </ul>
 *   In this case <em>each class</em> defining the entity should have been modified upon changes in the
 *   Prolog representation. 
 * </li>
 * <li>With our choice a change in the Prolog representation simply requires a new implementation of the
 * interface <tt>Mapper</tt>.</li>
 * </ul>
 * The interface <tt>Mapper</tt> provides two kinds of methods
 * <ul>
 * <li>methods converting entities from the Java representation to the Prolog one</li>
 * <li>methods converting entities from the Prolog representation to the Java one</li>
 * </ul>
 * Since the way of querying the Prolog engine depends on the representation of the entities stored in
 * the engine's state, a third kind of methods is required
 * <ul>
 * <li>query methods, returning the Prolog goal which should be proved by the Prolog engine when
 * needed. Have a look to the class {@link org.protune.core.ProtuneFilterEngine} in order to
 * understand how they can be used.</li>
 * </ul>
 * Tab. 1 lists the various kinds of methods.
 * <table border="1" cellspacing="0">
 * <thead><tr>
 * <th>Java to Prolog methods</th>
 * <th>Prolog to Java methods</th>
 * <th>query methods</th>
 * </tr></thead>
 * <tbody><tr>
 * <td><pre>
 * {@link #toPrologRepresentation(Action)}
 * {@link #toPrologRepresentation(TimeReadAction)}
 * {@link #toPrologRepresentation(LogAction)}
 * {@link #toPrologRepresentation(Notification)}
 * {@link #toPrologRepresentation(ActionWellPerformed)}
 * {@link #toPrologRepresentation(ActionWrongPerformed)}
 * {@link #toPrologRepresentation(Check)}
 * {@link #toPrologRepresentation(NotificationReliable)}
 * {@link #toPrologRepresentation(NotificationUnreliable)}
 * {@link #toPrologRepresentation(NegotiationElement)}
 * {@link #toPrologRepresentation(Policy)}
 * {@link #toPrologRepresentation(FilteredPolicy)}
 * {@link #toPrologRepresentation(TerminationAlgorithm)}
 * {@link #toPrologRepresentation(DummyTerminationAlgorithm)}
 * {@link #toPrologRepresentation(Object[])}
 * </pre></td>
 * <td><pre>
 * {@link #parseTimeReadAction(String)}
 * {@link #parseLogAction(String)}
 * {@link #parseActionWellPerformed(String)}
 * {@link #parseActionWrongPerformed(String)}
 * {@link #parseNotificationReliable(String)}
 * {@link #parseNotificationUnreliable(String)}
 * {@link #parseNegotiationElement(String)}
 * {@link #parsePolicy(String)}
 * {@link #parseFilteredPolicy(String)}
 * {@link #parseArray(String)}
 * </pre></td>
 * <td><pre>
 * {@link #getCurrentNegotiationStepNumberStatement()}
 * {@link #increaseNegotiationStepNumberStatement()}
 * {@link #addNegotiationElementStatement(String)}
 * {@link #isNegotiationSatisfiedStatement()}
 * {@link #extractActionsStatement(String)}
 * {@link #startFilteringStatement(String)}
 * {@link #firstFilteringStepStatement()}
 * {@link #isEmptyListStatement(String)}
 * {@link #secondFilteringStepStatement(String)}
 * {@link #thirdFilteringStepStatement()}
 * {@link #getRulesStatement()}
 * {@link #getMetarulesStatement()}
 * {@link #isUnlockedStatement(String)}
 * </pre></td>
 * </tr></tbody>
 * </table>
 * <b>Tab. 1</b> - <tt>Mapper</tt> methods overview.
 * @author jldecoi
 */
public interface Mapper {
	
	public static final String FILTERING_THEORY = "./config/prolog/jlog/filtering.pro";

	// Java --> Prolog.
	
	public String toPrologRepresentation(Action a);
	
	public String toPrologRepresentation(TimeReadAction tra);

	public String toPrologRepresentation(LogAction la);

	public String toPrologRepresentation(Notification n);

	public String toPrologRepresentation(ActionWellPerformed awp);

	public String toPrologRepresentation(ActionWrongPerformed awp);

	public String toPrologRepresentation(Check c);

	public String toPrologRepresentation(NotificationReliable nr);

	public String toPrologRepresentation(NotificationUnreliable nu);
	
	public String toPrologRepresentation(NegotiationElement ne);

	public String toPrologRepresentation(Policy p);

	public String toPrologRepresentation(FilteredPolicy fp);

	public String toPrologRepresentation(Goal g);
	
	public String toPrologRepresentation(TerminationAlgorithm ta);
	
	public String toPrologRepresentation(DummyTerminationAlgorithm dta);
	
	/**
	 * Translates a Java array into a Prolog representation of a list. The elements contained in the
	 * array are supposed to be Java object the Mapper is aware of (i.e.
	 * {@link org.protune.api.Notification}, {@link org.protune.api.Check},
	 * {@link org.protune.api.Policy} ...), therefore for each object an invocation to the 
	 * <tt>accept(Mapper)</tt> method should be made.
	 * @param oa
	 * @return
	 */
	public String toPrologRepresentation(Object[] oa);

	// Prolog --> Java.

	public TimeReadAction parseTimeReadAction(String s) throws ParseException;

	public LogAction parseLogAction(String s) throws ParseException;

	public ActionWellPerformed parseActionWellPerformed(String s) throws ParseException;

	public ActionWrongPerformed parseActionWrongPerformed(String s) throws ParseException;

	public NotificationReliable parseNotificationReliable(String s) throws ParseException;

	public NotificationUnreliable parseNotificationUnreliable(String s) throws ParseException;
	
	public NegotiationElement parseNegotiationElement(String s) throws ParseException;

	public Policy parsePolicy(String s) throws ParseException;

	public FilteredPolicy parseFilteredPolicy(String s) throws ParseException;
	
	/**
	 * Translates a Prolog representation of a list into a Java array. The elements contained in the
	 * list are supposed to be (Prolog representations of) entities the Mapper is aware of (i.e.
	 * notifications, checks, policies ...), therefore for each element an invocation to the suitable
	 * <tt>parse&lt;entity&gt;</tt> method should be made.
	 * @param s
	 * @return
	 */
	public Object[] parseArray(String s) throws ParseException;
	
	// Query methods.
	
	public String getCurrentNegotiationStepNumberStatement();
	
	public String increaseNegotiationStepNumberStatement();
	
	/**
	 * Something like
	 * <blockquote><tt>return "add_to_session( 0, received( " + negotiationElement +
	 * " ) ).";</tt></blockquote>
	 * @param negotiationElement
	 */
	public String addNegotiationElementStatement(String negotiationElement);
	
	public String isNegotiationSatisfiedStatement();
	
	public String extractActionsStatement(String filteredPolicy);
	
	/**
	 * Something like
	 * <blockquote><tt>return "start_session( 0, allow( " + action + " ) ).";</tt></blockquote>
	 * @return
	 */
	public String startFilteringStatement(String action);
	
	/**
	 * Something like
	 * <blockquote><tt>return "filter_policy1( 0, ActionList ).";</tt></blockquote>
	 * @return
	 */
	public String firstFilteringStepStatement();
	
	/**
	 * Something like
	 * <blockquote><tt>return list.matches("\\[\\]");</tt></blockquote>
	 * @param list
	 * @return
	 */
	public boolean isEmptyListStatement(String list);
	
	/**
	 * Something like
	 * <blockquote><tt>return "filter_policy2( 0, " + notificationList + ", ActionList
	 * ).";</tt></blockquote>
	 * @param notificationList
	 * @return
	 */
	public String secondFilteringStepStatement(String notificationList);
	
	/**
	 * Something like
	 * <blockquote><tt>return "filter_policy3(0).";</tt></blockquote>
	 * @return
	 */
	public String thirdFilteringStepStatement();
	
	/**
	 * Something like
	 * <blockquote><tt>return "session(0, X), X = rule(_, _, _).";</tt></blockquote>
	 * @return
	 */
	public String getRulesStatement();
	
	/**
	 * Something like
	 * <blockquote><tt>return "session(0, X), X = metarule(_, _, _).";</tt></blockquote>
	 * @return
	 */
	public String getMetarulesStatement();
	
	/**
	 * Something like
	 * <blockquote><tt>return "session( 0, state( received( session( 0, state( received( " +
	 * filteredPolicy + " ) ) ) ) ) ).";</tt></blockquote>
	 * @param action
	 * @return
	 */
	public String isUnlockedStatement(String action) throws Exception;
	
}
