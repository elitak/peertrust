package org.protune.api;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.text.ParseException;
import java.util.Vector;

import org.protune.core.Action;
import org.protune.core.Check;
import org.protune.core.FilteredPolicy;
import org.protune.core.Goal;
import org.protune.core.Notification;

/**
 * As described in the <a href="./package-summary.html">package</a> document, the <i>Protune</i>
 * system consists of an imperative and a declarative part: the first one was implemented (of course)
 * in Java while the second one in Prolog. The declarative part carries out the reasoning tasks related
 * to the negotiation and is implemented by exploiting third-part Java2Prolog and Prolog2Java inference
 * engines. Class <tt>PrologEngine</tt> provides the interface which these third-part tools should
 * respect in order to conform to the <i>Protune</i> system.
 * @author jldecoi
 */
public abstract class PrologEngine {
	
	protected Mapper mapper;

	public abstract void loadTheory(String theory) throws LoadTheoryException;
	
	public void loadTheory(File file) throws Exception{
		BufferedReader br =
			new BufferedReader(new FileReader(file));
		
		StringBuffer sb = new StringBuffer();
		while(true){
			String line = br.readLine();
			if(line==null) break;
			sb.append(line + "\n");
		}
		
		br.close();
		loadTheory(sb.toString());
	}
	
	/**
	 * Short-hand for boolean queries (or for queries whose (un)successful result we are only interested
	 * in).
	 * @param query
	 * @throws QueryException
	 */
	public abstract boolean isSuccessful(String query) throws QueryException;
	
	/**
	 * Short-hand for one-variable queries (or for queries whose first variable's value we are only
	 * interested in).
	 * @param query
	 * @throws QueryException
	 */
	public abstract String getFirstAnswer(String query) throws QueryException;
	
	/**
	 * Short-hand for one-variable queries (or for queries whose first variable's values we are only
	 * interested in).<br />
	 * <b>NOTE:</b> Each rule must be put on a different line.
	 * @param query
	 * @throws QueryException
	 */
	public abstract String[] getAllAnswers(String query) throws QueryException;

//	 add RECEIVED, negotiationStep and Prolog representation of a policy; retract previous fp and assert the new one
	public void addReceived(FilteredPolicy fp){
		try{//System.out.println(mapper.toPrologRepresentation(fp));
			isSuccessful("retract(currentFilteredPolicy(_)).");
			isSuccessful("assert(currentFilteredPolicy(" + mapper.toPrologRepresentation(fp) + ")).");
		}
		catch(QueryException qe){
			// Should not happen.
		}
	}

//	 add RECEIVED, negotiationStep and Prolog representation of a notification
	public void addReceived(Notification n){
		try{//System.out.println("received(alreadyExecuted(" + mapper.toPrologRepresentation(n) + ")).");
		isSuccessful("assert(alreadyExecuted(" + mapper.toPrologRepresentation(n) + ")).");
		}
		catch(QueryException qe){
			// Should not happen.
		}
	}

//	 add NegotiationElement.SENT, negotiationStep and Prolog representation of a policy;
	public void addSent(FilteredPolicy fp){}

//	 add NegotiationElement.SENT, negotiationStep and Prolog representation of a notification
	public void addSent(Notification n){
		try{//System.out.println("sent(alreadyExecuted(" + mapper.toPrologRepresentation(n) + ")).");
		isSuccessful("assert(alreadyExecuted(" + mapper.toPrologRepresentation(n) + ")).");
		}
		catch(QueryException qe){
			// Should not happen.
		}
	}

	public void addLocal(Notification n){
		try{//System.out.println("local(alreadyExecuted(" + mapper.toPrologRepresentation(n) + ")).");
		isSuccessful("assert(alreadyExecuted(" + mapper.toPrologRepresentation(n) + ")).");
		}
		catch(QueryException qe){
			// Should not happen.
		}
	}

//	 add RECEIVED, negotiationStep and Prolog representation of a check
	public void add(Check c){}
	
	public void increaseNegotiationStepNumber(){}
	
	public Action[] extractLocalActions(Goal g) throws QueryException, ParseException{
		String s = getFirstAnswer(
				"extractLocalActions(" +
				mapper.toPrologRepresentation(g) +
				", X)."
		);
		s = s.substring(1, s.length() - 1);
		
		Vector<String> vs = new Vector<String>();
		String currentString = new String();
		int openParenthesis = 0;
		for(int i=0; i<s.length(); i++){
			if(s.charAt(i)=='('){
				openParenthesis++;
				currentString += s.charAt(i);
			}
			else if(s.charAt(i)==')'){
				openParenthesis--;
				currentString += s.charAt(i);
			}
			else if(s.charAt(i)==',' && openParenthesis==0){
				vs.add(currentString);
				currentString = new String();
			}
			else currentString += s.charAt(i);
		}
		if(!currentString.equals("")) vs.add(currentString);
		
		Action[] aa = new Action[vs.size()];
		for(int i=0; i<aa.length; i++) aa[i] = mapper.parseAction(vs.get(i));
		return aa;
	}

	public boolean isNegotiationSatisfied(Goal g) throws QueryException{
		return isSuccessful("prove(" + mapper.toPrologRepresentation(g) + ").");
	}
	
	public boolean terminate() throws QueryException{
		return isSuccessful("terminationAlgorithm.");
	}
	
	// To be changed: Extract just selected unlocked actions
	public Action[] extractExternalActions(Goal g) throws QueryException, ParseException{
		String s = getFirstAnswer(
				"extractUnlockedExternalActions(" +
				mapper.toPrologRepresentation(g) +
				", X)."
		);
		s = s.substring(1, s.length() - 1);
		
		Vector<String> vs = new Vector<String>();
		String currentString = new String();
		int openParenthesis = 0;
		for(int i=0; i<s.length(); i++){
			if(s.charAt(i)=='('){
				openParenthesis++;
				currentString += s.charAt(i);
			}
			else if(s.charAt(i)==')'){
				openParenthesis--;
				currentString += s.charAt(i);
			}
			else if(s.charAt(i)==',' && openParenthesis==0){
				vs.add(currentString);
				currentString = new String();
			}
			else currentString += s.charAt(i);
		}
		if(!currentString.equals("")) vs.add(currentString);

		Action[] aa = new Action[vs.size()];
		for(int i=0; i<aa.length; i++){
			aa[i] = mapper.parseAction(vs.get(i));
		}
		return aa;
	}
	
	public FilteredPolicy filter(Goal g) throws QueryException, ParseException{
		return mapper.parseFilteredPolicy(getFirstAnswer(
				"filter(" + 
				mapper.toPrologRepresentation(g) + 
				", FilteredPolicy)."
		));
	}
	
}