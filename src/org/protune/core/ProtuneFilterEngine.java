package org.protune.core;

import org.policy.action.ActionResult;
import org.policy.filtering.*;
import org.protune.api.Action;
import org.protune.api.FilteredPolicy;
import org.protune.api.Mapper;
import org.protune.api.Notification;
import org.protune.api.Policy;

import java.io.*;

/**
 * <b>NOTE:</b> This class is still work in progress.<br />
 * The current implementation of the interface {@link org.protune.core.FilterEngine}. As inference engine
 * an istance of {@link org.policy.filtering.JLogPrologApi} is used.<br />
 * In order to avoid duplication, for each negotiation only a copy of the negotiation state should be
 * present in the system. Since at each negotiation step a copy is held in the inference engine
 * contained in the <tt>ProtuneFilterEngine</tt>, it is pointless implementing another copy somewhere
 * else in the system, as the inference engine itself can act as a(n implementation of the interface)
 * {@link org.protune.core.Status}. Actually this interface is not directly implemented by the inference
 * engine but by the <tt>ProtuneFilterEngine</tt> class. Finally the <tt>ProtuneFilterEngine</tt>
 * implements therefore both the interface {@link org.protune.core.FilterEngine} and {@link
 * org.protune.core.Status}. 
 * @author jldecoi
 */
public class ProtuneFilterEngine implements FilterEngine, Status{

	JLogPrologApi jlpa;
	Mapper mapper;
	
	/*
	 * Debugging method to be deleted.
	 * @param args
	 * @throws ConfigurationException
	 * @throws FileNotFoundException
	 * @throws IOException
	 * @throws Exception
	 *
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
		
		ProtuneFilterEngine t = new ProtuneFilterEngine();
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
	}*/
	
	/**
	 * Creates an inference engine {@link org.policy.filtering.JLogPrologApi} and loads the policy into
	 * it.
	 * @param m A mapper.
	 * @param p The policy available on the peer.
	 */
	ProtuneFilterEngine(Mapper m, Policy p) throws Exception{
		mapper = m;
		
		jlpa = new JLogPrologApi();
		jlpa.init();
		
		jlpa.load(new File(Mapper.FILTERING_THEORY));
		jlpa.load(mapper.toPrologRepresentation(p));
	}
	
	public int getCurrentNegotiationStepNumber(){
		try{
			return Integer.parseInt(jlpa.queryOnce(mapper.getCurrentNegotiationStepNumberStatement()).getResult(0).getBinding(0));
		}
		catch(Exception e){
			return 0;
		}
	}
	
	public void increaseNegotiationStepNumber(){
		try{
			jlpa.execute(mapper.increaseNegotiationStepNumberStatement());
		}
		catch(Exception e){}
	}
	
	public void addNegotiationElement(NegotiationElement ne){
		try{
			jlpa.execute(mapper.addNegotiationElementStatement(mapper.toPrologRepresentation(ne)));
		}
		catch(PrologEngineException pee){}
	}
	
	/**
	 * Checks whether the overall goal of the negotiation is satisfied (i.e. whether it can be proved
	 * according to the current state of the peer).
	 * @param status In this implementation of the interface {@link org.protune.core.FilterEngine} it is
	 * not used (the policy available on the peer is loaded into the inference engine at construction
	 * time - see {@link #ProtuneFilterEngine(Mapper, Policy)}}).
	 * @return <tt>true</tt> if the overall goal of the negotiation is satisfied (i.e. if it can be
	 * proved according to the current state of the peer), <tt>false</tt> otherwise.
	 */
	public boolean isNegotiationSatisfied(Status status){
		try{
			return jlpa.execute(mapper.isNegotiationSatisfiedStatement());
		}
		catch(PrologEngineException pee){
			return false;
		}
	}
	
	public Action[] extractActions(FilteredPolicy fp){
		try{
			ActionResult ar =
				jlpa.query(mapper.extractActionsStatement(mapper.toPrologRepresentation(fp)));
			int length = ar.getNumberResults();
			Action[] aa = new Action[length];
			for(int i=0; i<length; i++) aa[i] = mapper.parseLogAction(ar.getResult(i).getBinding(0));
			return aa;
		}
		catch(Exception e){
			return null;
		}
	}

	/**
	 * Returns the filtered policy protecting the action, whose execution was asked for by the other
	 * peer. The filtering is performed according to the policy available on the peer and according to
	 * its current state.
	 * @param policy In this implementation of the interface {@link org.protune.core.FilterEngine} it is
	 * not used (a <tt>ProtuneFilterEngine</tt> is an {@link org.protune.core.Status} as itself).
	 * @param status In this implementation of the interface {@link org.protune.core.FilterEngine} it is
	 * not used (a <tt>ProtuneFilterEngine</tt> is an {@link org.protune.core.Status} as itself).
	 * @param request An action, whose execution was asked for by the other peer.
	 * @return The filtered policy protecting the action, whose execution was asked for by the other
	 * peer. The filtered policy is computed according to the current state of the peer and according
	 * to its local policy.
	 * @throws Exception
	 */
	public FilteredPolicy filter(Policy policy, Status status, Action request) throws Exception{
		jlpa.execute(mapper.startFilteringStatement(request.accept(mapper))); 
		
		String list =
			jlpa.queryOnce(mapper.firstFilteringStepStatement()).getVariableBindings().getResult(0).getBinding(0);
		while(!mapper.isEmptyListStatement(list)){
			Object[] oa = mapper.parseArray(list);
			Notification[] na = new Notification[oa.length];
			for(int i=0; i<na.length; i++) na[i] = ((Action)oa[i]).perform();
			list =
				jlpa.queryOnce(mapper.secondFilteringStepStatement(mapper.toPrologRepresentation(na))).getVariableBindings().getResult(0).getBinding(0);
		}
		jlpa.execute(mapper.thirdFilteringStepStatement());

		StringBuffer sb = new StringBuffer();
		ActionResult ar;
		ar = jlpa.query(mapper.getRulesStatement());
		for(int i=0; i<ar.getNumberResults(); i++) sb.append(ar.getResult(i).getBinding(0) + "\n");
		ar = jlpa.query(mapper.getMetarulesStatement());
		for(int i=0; i<ar.getNumberResults(); i++) sb.append(ar.getResult(i).getBinding(0) + "\n");
		return mapper.parseFilteredPolicy(sb.toString());
	}
	
	public boolean isUnlocked(Action action) throws Exception{
		return
		   jlpa.query(mapper.isUnlockedStatement(action.accept(mapper))).getExecutionResult();
	}

}
