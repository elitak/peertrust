package org.protune.api;

import org.policy.action.ActionResult;
import org.policy.filtering.JLogPrologApi;
import org.peertrust.exception.ConfigurationException;
import java.io.*;

/**
 * The objects of the class <tt>Filter</tt> carry out every activity related to the filtering process
 * of a policy. This one is the class, the interface between Denisa's work and mine consists mostly
 * of.<br />
 * The class <tt>Filter</tt> contains three methods (and the debugging method {@link #main(String[])})
 * <ul>
 * <li>{@link #extractActions(FilteredPolicy)}: extracts the actions contained in the filtered policy
 * sent by the other peer. This is the only method I asked Denisa for and which was not
 * provided.<br />
 * <b>OPEN ISSUE:</b> I am not able to create such a method: it requires a knowledge of Denisa's work
 * which I do not have. In order to get this knowledge I estimate I would need weeks.</li>
 * <li>{@link #filter(Policy, String[], Action)}: this method relies heavily on the one provided by
 * Denisa. I almost copied what she did, therefore it should be correct, but bugs are always
 * possible.<br />
 * <b>OPEN ISSUE:</b> The method is not completed (execution of actions is still missing).</li>
 * <li>{@link #isUnlocked(Action)}: tests whether the policy protecting the credential is satisfied.
 * In order to create this method I needed to see what Denisa did (I spent almost two days on this),
 * therefore most likely it contains bugs (although so far it seems it works).</li>
 * </ul>
 * @author jldecoi
 */
public class Filter {

	public static final String FILTERING_THEORY = "./config/prolog/jlog/filtering.pro";
	JLogPrologApi jlpa;
	
	/**
	 * Debugging method to be deleted.
	 * @param args
	 * @throws ConfigurationException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 */
	public static void main(String[] args) throws
			ConfigurationException,
			FileNotFoundException,
			IOException,
			Exception{
		BufferedReader br =
			new BufferedReader(new FileReader(
					"./config/policy_code_samples/OnlineLibrary/LibraryPolicy.prolog"
			));
		
		StringBuffer sb = new StringBuffer();
		while(true){
			String line = br.readLine();
			if(line==null) break;
			sb.append(line + "\n");
		}
		
		br.close();
		
		Filter t = new Filter();
		String[] status = {"session( 0, state( received( allow(access(books)) ) ) )"};
		try{
			Policy p = new Policy(sb.toString());
			System.out.println(t.filter(
					p,
					status,
					new TimeReadAction()
			));
			System.out.println(t.isUnlocked(new TimeReadAction()));
		}
		catch(Exception e){
			System.out.println(e);
		}
	}
	
	/**
	 * Extracts the actions contained in the filtered policy sent by the other peer.
	 * @param fp The filtered policy sent by the other peer.
	 * @return The actions contained in the filtered policy sent by the other peer.
	 */
	public Action[] extractActions(FilteredPolicy fp){
		return null;
	}
	
	public Action[] extractActions(FilteredPolicy[] fpa){
		return extractActions(FilteredPolicy.concat(fpa));
	}
	
	/**
	 * Returns the filtered policy protecting the credential, declaration
	 * or action contained in the parameter <tt>request</tt>. The filtering is
	 * performed according to the policy available on the peer and
	 * according to the set of credentials sent by the other peer.<br />
	 * <b>OPEN ISSUE:</b> Execution of actions is still missing.<br />
	 * <b>OPEN ISSUE:</b> At the beginning of each call the inference engine (the field
	 * <tt>jlpa</tt>) should be reset. Since I do not know how this can be done, I
	 * simply created another instance of inference engine.
	 * @param credentials The set of credentials sent by the other peer. Each credential
	 * contained in the state should have a string representation like this one:
	 * <pre>
	 * session( SId, state( received( &lt;credential&gt; ) ) )
	 * </pre>
	 * <b>NOTE:</b> Each (string representation of a) credential is not allowed
	 * to terminate with a period (<tt>.</tt>).
	 * @param policy The policy available on the peer.<br />
	 * <b>OPEN ISSUE:</b> The policy is supposed not to have side-effects, i.e.
	 * the reasoning on the policy does <strong>not</strong> modify
	 * the policy itself (as it could e.g. happen if the policy uses
	 * predicates like <tt>assert</tt> or <tt>retract</tt>).
	 * @param request It is not allowed to terminate with a period (<tt>.</tt>).
	 * Can be either
	 * <ul>
	 * <li><tt>release(Credential)</tt> or</li>
	 * <li><tt>release(Declaration)</tt> or</li>
	 * <li><tt>do(Action)</tt>.</li>
	 * </ul> 
	 * @return The filtered policy protecting the credential, declaration
	 * or action contained in the parameter request.
	 * @throws Exception
	 */
	public String filter(Policy policy, String[] credentials, Action request)
			throws Exception{
		jlpa = new JLogPrologApi();
		jlpa.init();
		
		jlpa.load(new File(FILTERING_THEORY));
		jlpa.load(policy.toTheory());
		for(int i=0; i<credentials.length; i++) jlpa.execute("add_to_session( 0, received( " + credentials[i] + " ) ).");
		jlpa.execute("start_session( 0, allow( " + request.toGoal() + " ) ).");
		
		String list =
			jlpa.queryOnce("filter_policy1( 0, ActionList ).").getVariableBindings().getResult(0).getBinding(0);
		while(list.matches("\\[\\]")==false){
			//ADD EXECUTION OF ACTION IN list
			String ActionResults="[]"; 
			list =
				jlpa.queryOnce("filter_policy2( 0, "+ ActionResults +", ActionList ).").getVariableBindings().getResult(0).getBinding(0);
		}
		jlpa.execute("filter_policy3(0).");

		StringBuffer sb=new StringBuffer();
		ActionResult ar = jlpa.query("session(0, X), X = rule(_, _, _).");
		for(int i=0;i<ar.getNumberResults();i++) sb.append(ar.getResult(i).getBinding(0)+"\n");
		ar = jlpa.query("session(0, X), X = metarule(_, _, _).");
		for(int i=0;i<ar.getNumberResults();i++) sb.append(ar.getResult(i).getBinding(0)+"\n");
		return sb.toString();
	}
	
	public String[] filter(Policy policy, String[] credentials, Action[] request) throws Exception{
		String[] sa = new String[request.length];
		for(int i=0; i<sa.length; i++) sa[i] = filter(policy, credentials, request[i]);
		return sa;
	}
	
	/**
	 * Tests whether the policy protecting the action is satisfied.<br />
	 * <b>NOTE:</b> Must be invoked after the method {@link #filter(Policy, String[], Action)}}.<br />
	 * <b>OPEN ISSUE:</b> So far the method was implemented just for credentials.
	 * @param action The action.<br />
	 * <b>OPEN ISSUE:</b> So far the method was implemented just for credentials.<br />
	 * <b>NOTE:</b> The action is not allowed to terminate with a period (<tt>.</tt>).
	 * @return <tt>true</tt> if the policy protecting the action is satisfied, <tt>false</tt>
	 * otherwise.
	 * @throws Exception
	 */
	public boolean isUnlocked(Action action) throws Exception{
		return
		   jlpa.query("session( 0, state( received( session( 0, state( received( " + action.toGoal() + " ) ) ) ) ) ).").getExecutionResult();
	}

}
