/**
 * Copyright 2004
 * 
 * This file is part of Peertrust.
 * 
 * Peertrust is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Peertrust is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Peertrust; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/
package org.peertrust.meta;

import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.net.*;
import org.peertrust.security.*;
import org.peertrust.security.credentials.*;
import net.jxta.edutella.util.*;

/**
 * $Id: Tree.java,v 1.1 2004/12/21 11:09:34 seb0815 Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2004/12/21 11:09:34 $
 * by $Author: seb0815 $
 * @description
 */
public class Tree
{
 	// tree(Id, Goal, Subqueries, Proof, Status, (Requester,QueryId))
 	// -> Status = ready, waiting or failed
 	private final long MAX_TREE_LIFETIME = 600000 ;
 	
	// status valid values for trees in the queue
 	public static final int UNSPECIFIED = -1 ;
 	public static final int READY = 0 ;
 	public static final int WAITING = 1 ;
 	public static final int ANSWERED_AND_WAITING = 2 ;
 	public static final int ANSWERED = 3 ;
 	public static final int FAILED = 4 ;
 	
 	// value for trees without an id associated
 	public static final int NULL_ID = -1 ;

 	private long id = NULL_ID ;
 	private String originalGoal = null ;
 	private String goal = null ;
 	private String subqueries = null ;
	private Vector vectorProofRules;
 	private int status = UNSPECIFIED ;
 	private Peer requester = null ;
 	private long reqQueryId = NULL_ID ;

	private Peer delegator = null ;
	private String lastExpandedGoal = null ;
	private long timeStamp = 0 ;
	static private long currentId = 0 ;
	
	private static Logger log = Logger.getLogger(Tree.class);
	
 	public Tree (long id, String goal, String subqueries, Vector proof, int status, Peer requester, long reqQueryId, Peer delegator, String lastExpandedGoal)
 	{
 		this.id = id ;
 		this.originalGoal = goal ;
 		this.goal = goal ;
 		this.subqueries = subqueries ;
 		setProofRuleVector(proof);
 		this.status = status ;
 		this.requester = requester ;	
 		this.reqQueryId = reqQueryId ;
 		this.delegator = delegator ;
 		this.lastExpandedGoal = lastExpandedGoal ;
 		timeStamp = System.currentTimeMillis() ;
		vectorProofRules=new Vector();
 		log.debug("Created: " + this.toString()) ;
 	}
 	
 	// Constructor for a completely new query
 	public Tree (String goal, Peer requester, long reqQueryId)
	{
 		this(getNewId(), goal, "[query(" + goal + ",no)]", null, READY, requester, reqQueryId, null, null) ;
	}
 	
	public Tree (String goal, String subqueries, Vector proof, int status, Peer requester, long reqQueryId, Peer delegator, String lastExpandedGoal)
 	{
 		this(getNewId(), goal, subqueries, proof, status, requester, reqQueryId, delegator, lastExpandedGoal) ;
 	}

	public Tree (String goal, String subqueries, Vector proof, int status, Peer requester, long reqQueryId)
	{
		this(getNewId(), goal, subqueries, proof, status, requester, reqQueryId, null, null) ;
	}
 	
	public Tree (long id, Peer requester, long reqQueryId)
	{
		this(id, null, null, null, UNSPECIFIED, requester, reqQueryId, null, null) ;
		log.debug("Created pattern tree. Id: |" + id + "|") ;
	}
	
	// constructor with only tree Id (specially for searching by tree id)
 	public Tree (long id)
 	{
 		this(id, null, null, null, UNSPECIFIED, null, NULL_ID, null, null) ;
 	}
 	
	// constructor with only requester and requester Id (specially for searching by requester and requester query id)
 	public Tree (Peer requester, long reqQueryId)
 	{
 		this(NULL_ID, null, null, null, UNSPECIFIED, null, NULL_ID, null, null) ;
 	}

 	// copy a tree but change the id
 	public Tree (Tree tree)
 	{
 		this(getNewId(), tree.getGoal(), tree.getSubqueries(),
				 tree.getProofRuleVector(), tree.getStatus(),
 				tree.getRequester(), tree.getReqQueryId(), tree.getDelegator(), tree.getLastExpandedGoal()) ;
 	}
 	
	static synchronized long getNewId ()
 	{
 		currentId += 1 ;
 		return currentId ;
 	}

 	public void update(Tree tree)
 	{
 		long tmpLong ;
 		String tmpString ;
 		Peer tmpPeer ;
 		int tmpByte ;
 		
 		tmpLong = tree.getId() ;
 		if (tmpLong != NULL_ID)
 			this.id = tmpLong ;
 		
 		tmpString = tree.getGoal() ;
 		if (tmpString != null)
 			this.goal = tmpString ;
 		
 		tmpString = tree.getSubqueries() ;
 		if (tmpString != null)
 			this.subqueries = tmpString ;
 			
 		Vector tmpVector = tree.getProofRuleVector() ;
 		if (tmpVector != null)
 			setProofRuleVector(tmpVector) ;
 		
 		tmpByte = tree.getStatus() ;
 		if (tmpByte != UNSPECIFIED)
 			this.status = tmpByte ;
 			
 		tmpPeer = tree.getRequester() ;
 		if (tmpPeer != null)
 			this.requester = tmpPeer ;
 	}
 	
 	public boolean isProcessable ()
 	{
 		boolean ret = false ;
 		
 		switch (status)
 		{
 			case Tree.READY:
 			case Tree.ANSWERED:
 			case Tree.FAILED:
 				ret = true ;
 				break ;
 			
 			case Tree.ANSWERED_AND_WAITING:
 			case Tree.WAITING:
				ret = false ;
				break ;		
 		}
 		return ret ;
 	}
 	
 	public boolean isOutDated ()
 	{
 		if ( (System.currentTimeMillis() - timeStamp) > MAX_TREE_LIFETIME)
 			return true ;
 		else
 			return false ;
 	}
 	
 	public boolean equals (Object object)
 	{
 		boolean same = true  ;
 		 
 		Tree tree = (Tree) object ;
 		
 		if ( ( (this.getId() != NULL_ID) && (this.id != tree.getId()) ) ||
			// ( (tree.getGoal() != null) && (this.goal.compareTo(tree.getGoal() != 0))) ||
			// ( (tree.getSubqueries() != null) && (this.subqueries.compareTo(tree.getSubqueries() != 0))) ||
			// ( (tree.getProof() != null) && (this.proof.compareTo(tree.getProof() != 0))) ||
			// ( (tree.getStatus() != UNSPECIFIED) && (this.status != tree.getStatus())) ||
			( (this.getRequester() != null) && (this.requester.equals(tree.getRequester()) == false)) ||
			( (this.getReqQueryId() != NULL_ID) && (this.reqQueryId != tree.reqQueryId)) )
 			same = false ;
 		
 		return same ;	
 	}
 	
	public void appendProof(Vector proof) {
		for(int i=0;i<proof.size();i++)
			vectorProofRules.add(proof.elementAt(i));
	}

 	public long getId () {
 		return id ; }
 	
 	public void setId(long id) {
 		this.id = id ;
 	}
 	
 	public String getGoal() {
 		return goal ; }
 		
 	public void setGoal (String goal) {
 		this.goal = goal ;
 	}
 		
 	public String getSubqueries() {
 		return subqueries ; }
 	
 	public void setSubqueries(String subqueries) {
 		this.subqueries = subqueries ;
 	}
 		
 	public int getStatus() {
 		return status ; }
 	
 	public void setStatus(int status) {
 		this.status = status ;
 	}
 	public Peer getRequester() {
 		return requester ; }

	public void setRequester (Peer requester) {
		this.requester = requester ;
	}
	
	public long getReqQueryId () {
		return reqQueryId ;
	}
	
	void setReqQueryId (long reqQueryId) {
		this.reqQueryId = reqQueryId ;
	}

	public Peer getDelegator() {
		return delegator ; }
	
	public void setDelegator (Peer delegator) {
		this.delegator = delegator ;
	}

	/**
	 * @return Returns the lastExpandedGoal.
	 */
	public String getLastExpandedGoal() {
		return lastExpandedGoal;
	}
	/**
	 * @param lastExpandedGoal The lastExpandedGoal to set.
	 */
	public void setLastExpandedGoal(String lastExpandedGoal) {
		this.lastExpandedGoal = lastExpandedGoal;
	}
	
	public String toString()
	{
		return "Id: |" + id + "| - originalGoal: |" + originalGoal + "| - Goal: |" + goal + "| Subgoals: |" +
			subqueries + "| LastExpandedGoal: |" + lastExpandedGoal + "|"  ;
	}

	/**
	 * Returns the proof tree-Vector.
	 * @return Vector The Vector that represents the proof tree.
	 */
	public Vector getProofRuleVector() {
		return vectorProofRules;
	}

	/**
	 * Sets the proof tree-Vector.
	 * @param proof The new proof tree-Vector.
	 */
	public void setProofRuleVector(Vector proof) {
		vectorProofRules=proof;
	}

	/**
	 * New rules can be added to the proof tree with this method. AuthenticatesToProofRule-
	 * objects can't be created with this object, so the authenticatesTo-predicate
	 * shouldn't be used as a parameter.
	 * @param credentialstore The place where the credentials are stored.
	 * @param config A configuration-object.
	 * @param stringProof The (normal or signed) rule in text that should be added. These
	 *	may be also be more than one, in that case, the rules must be separated by
	 *	commas. All rules must be enclosed by brackets, for example:
	 *		[rule1(...),rule2(...),...]
	 */
	public void appendProofRuleVector(CredentialStore credentialstore,
		Configurator config,String stringProof) {
		if(vectorProofRules==null)
			vectorProofRules=new Vector();
		int offsetstart=1,num_brackets=0,index;
		char c;
		String str,str2;
		//Try to extract all rules that are contained in the parameter
		for(int i=1;i<stringProof.length()-1;i++) {
			c=stringProof.charAt(i);
			if((c=='(')||(c=='['))
				num_brackets++;
			else if((c==')')||(c==']'))
				num_brackets--;
			//New rule is parsed
			if(((c==',')||(i==stringProof.length()-2))&&
				(i>offsetstart)&&(num_brackets==0)) {
				str=stringProof.substring(offsetstart,(c==',') ? i : i+1);
				str=str.replaceAll(" ","");
				index=0;
				//All hidden-terms have to be replaced by []
				while((index=str.indexOf("hidden",index))!=-1)
					if(((str.charAt(index-1)=='(')||(str.charAt(index-1)
						==','))&&((str.charAt(index+6)==',')||
						(str.charAt(index+6)==')')))
						str=str.substring(0,index)+"[]"+str.substring(
						index+6);
				//If rule is a signed one
				if(str.startsWith("signed(")) {
					str2=str.replaceAll("\\(r\\(","(rule(");
					//Try to find the corresponding credential
					Credential credential=credentialstore.getCredential(
						str2);
					if((credential==null)&&((index=str2.lastIndexOf("@"))
						!=-1))
						credential=credentialstore.getCredential(
							str2.substring(0,index));
					//add special object to the proof tree
					vectorProofRules.addElement(new SignedProofRule(str,
						credential,config));
				}
				else
					//add special object to the proof tree
					vectorProofRules.addElement(new ProofRule(str));
				offsetstart=i+1;
			}
		}
	}
}
