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

import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;
import org.peertrust.net.*;

/**
 * <p>
 * 
 * </p><p>
 * $Id: Tree.java,v 1.12 2005/08/07 12:06:53 dolmedilla Exp $
 * <br/>
 * Date: 05-Dec-2003
 * <br/>
 * Last changed: $Date: 2005/08/07 12:06:53 $
 * by $Author: dolmedilla $
 * </p>
 * @author olmedilla 
 */
public class Tree
{
	private static Logger log = Logger.getLogger(Tree.class);
	
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
 	public static final int NULL_ID = new Random().nextInt() ;

 	private long _id = NULL_ID ;
 	private String _originalGoal = null ;
 	private String _goal = null ;
 	private String _resolvent = null ;
 	private Proof _proof = null ;
 	private Trace _trace = null ;
 	private int _status = UNSPECIFIED ;
 	private Peer _requester = null ;
 	private long _reqQueryId = NULL_ID ;

	private Peer _delegator = null ;
	private String _lastExpandedGoal = null ;
	private long _timeStamp = 0 ;
	static private long _currentId = 0 ;
	
 	Tree (long id, String goal, String subqueries, Proof proof, int status, Peer requester, 
 			long reqQueryId, Peer delegator, String lastExpandedGoal, Trace trace)
 	{
 		this._id = id ;
 		this._originalGoal = goal ;
 		this._goal = goal ;
 		this._resolvent = subqueries ;
 		if (proof == null)
 			_proof = new Proof() ;
 		else
 			_proof = proof ;
 		if (trace == null)
 			_trace = new Trace() ;
 		else
 			_trace = trace ;
 		this._status = status ;
 		this._requester = requester ;	
 		this._reqQueryId = reqQueryId ;
 		this._delegator = delegator ;
 		this._lastExpandedGoal = lastExpandedGoal ;
 		_trace = trace ;
 		_timeStamp = System.currentTimeMillis() ;
 		log.debug("Created: " + this.toString()) ;
 	}

 	// Constructor for a completely new query
 	public Tree (String goal, Peer requester, long reqQueryId, Trace trace)
	{
 		this(getNewId(), goal, "[query(" + goal + ",no)]", null, READY, requester, 
 				reqQueryId, null, null, trace) ;
	}
 	
	Tree (String goal, String subqueries, Proof proof, int status, Peer requester, 
			long reqQueryId, Peer delegator, String lastExpandedGoal, Trace trace)
 	{
 		this(getNewId(), goal, subqueries, proof, status, requester, reqQueryId, 
 				delegator, lastExpandedGoal, trace) ;
 	}

	public Tree (String goal, String subqueries, Proof proof, int status, Peer requester, 
			long reqQueryId, Trace trace)
	{
		this(getNewId(), goal, subqueries, proof, status, requester, reqQueryId, 
				null, null, trace) ;
	}
 	
	Tree (long id, Peer requester, long reqQueryId)
	{
		this(id, null, null, null, UNSPECIFIED, requester, reqQueryId, null, null, new Trace()) ;
		log.debug("Created pattern tree. Id: |" + id + "|") ;
	}
	
	// Complete constructor is:
	// Tree (long id, String goal, String subqueries, Proof proof, int status, Peer requester, 
	//	long reqQueryId, Peer delegator, String lastExpandedGoal, Trace trace)
	
	
	// constructor with only tree Id (specially for searching by tree id)
 	public Tree (long id)
 	{
 		this(id, null, null, null, UNSPECIFIED, null, NULL_ID, null, null, new Trace()) ;
 	}
 	
	// constructor with only requester and requester Id (specially for searching by requester and requester query id)
 	public Tree (Peer requester, long reqQueryId)
 	{
 		this(NULL_ID, null, null, null, UNSPECIFIED, requester, reqQueryId, null, null,  new Trace()) ;
 	}

 	// copy a tree but change the id
 	public Tree (Tree tree)
 	{
 		this(getNewId(), tree.getGoal(), tree.getResolvent(), tree.getProof(), tree.getStatus(),
 				tree.getRequester(), tree.getReqQueryId(), tree.getDelegator(), 
				tree.getLastExpandedGoal(), tree.getTrace()) ;
 	}
 	
	static synchronized long getNewId ()
 	{
 		_currentId += 1 ;
 		return _currentId ;
 	}
 	
 	public void update(Tree tree)
 	{
 		long tmpLong ;
 		String tmpString ;
 		Peer tmpPeer ;
 		int tmpByte ;
 		Proof tmpProof ;
 		Trace tmpTrace ;
 		
 		
 		tmpLong = tree.getId() ;
 		if (tmpLong != NULL_ID)
 			this._id = tmpLong ;
 		
 		tmpString = tree.getGoal() ;
 		if (tmpString != null)
 			this._goal = tmpString ;
 		
 		tmpString = tree.getResolvent() ;
 		if (tmpString != null)
 			this._resolvent = tmpString ;
 			
 		tmpProof = tree.getProof() ;
 		if (tmpProof != null)
 			setProof(tmpProof) ;
 		
 		tmpTrace = tree.getTrace() ;
 		if (tmpTrace != null)
 			setTrace(tmpTrace) ;
 		
 		tmpByte = tree.getStatus() ;
 		if (tmpByte != UNSPECIFIED)
 			this._status = tmpByte ;
 			
 		tmpPeer = tree.getRequester() ;
 		if (tmpPeer != null)
 			this._requester = tmpPeer ;
 	}
 	
 	public boolean isProcessable ()
 	{
 		boolean ret = false ;
 		
 		switch (_status)
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
 		if ( (System.currentTimeMillis() - _timeStamp) > MAX_TREE_LIFETIME)
 			return true ;
 		else
 			return false ;
 	}
 	
 	public boolean equals (Object object)
 	{
 		log.debug("COMPARING():\nThis: " + (Tree)object + "\nwith:" + this) ;
 		
 		boolean same = true  ;
 		 
 		Tree tree = (Tree) object ;
 		
 		if ( ( (this.getId() != NULL_ID) && (this._id != tree.getId()) ) ||
			// ( (tree.getGoal() != null) && (this.goal.compareTo(tree.getGoal() != 0))) ||
			// ( (tree.getSubqueries() != null) && (this.subqueries.compareTo(tree.getSubqueries() != 0))) ||
			// ( (tree.getProof() != null) && (this.proof.compareTo(tree.getProof() != 0))) ||
			// ( (tree.getStatus() != UNSPECIFIED) && (this.status != tree.getStatus())) ||
			( (this.getRequester() != null) && (this._requester.equals(tree.getRequester()) == false)) ||
			( (this.getReqQueryId() != NULL_ID) && (this._reqQueryId != tree._reqQueryId)) )
 			same = false ;
 		
 		return same ;	
 	}
 	
//	public void appendProof (Proof proof2)
//	{
//		_proof.append(proof2) ;
//		
//		String proof1 = _stringProof ;
//		
//		String previousProof = proof1.substring(1,proof1.length()-1) ;
//		String newProof =  proof2.substring(1,proof2.length()-1) ;
//		String joinedProof = "[" ;
//		if (previousProof.length() > 2)
//		{
//			joinedProof += previousProof ;
//			if (newProof.length() > 2)
//				joinedProof +="," + newProof ;
//		}
//		else
//			joinedProof += newProof ;
//		joinedProof += "]" ;	
//		
//		this._stringProof = joinedProof ;
//		this._vectorProof.addAll(generateProofVector(proof2)) ;
//	}

 	public long getId () {
 		return _id ; }
 	
 	public void setId(long id) {
 		this._id = id ;
 	}
 	
 	public String getGoal() {
 		return _goal ; }
 		
 	public void setGoal (String goal) {
 		this._goal = goal ;
 	}
 		
 	public String getResolvent() {
 		return _resolvent ; }
 	
 	public void setResolvent(String subqueries) {
 		this._resolvent = subqueries ;
 	}
 		
 	public Proof getProof() {
 		return _proof ; }
 		
 	public void setProof (Proof proof) {
 		_proof = proof ;
 	}
 	
 	public int getStatus() {
 		return _status ; }
 	
 	public void setStatus(int status) {
 		this._status = status ;
 	}
 	public Peer getRequester() {
 		return _requester ; }

	public void setRequester (Peer requester) {
		this._requester = requester ;
	}
	
	public long getReqQueryId () {
		return _reqQueryId ;
	}
	
	void setReqQueryId (long reqQueryId) {
		this._reqQueryId = reqQueryId ;
	}

	public Peer getDelegator() {
		return _delegator ; }
	
	public void setDelegator (Peer delegator) {
		this._delegator = delegator ;
	}
	
	public Trace getTrace ()
	{
		return _trace ;
	}
	
	public void setTrace (Trace trace)
	{
		this._trace = trace ;
	}

	/**
	 * @return Returns the lastExpandedGoal.
	 */
	public String getLastExpandedGoal() {
		return _lastExpandedGoal;
	}
	/**
	 * @param lastExpandedGoal The lastExpandedGoal to set.
	 */
	public void setLastExpandedGoal(String lastExpandedGoal) {
		this._lastExpandedGoal = lastExpandedGoal;
	}
	
	public Vector generateProofVector(String proof) {
		log.debug("Parsing proof: " + proof) ;
		Vector vector = new Vector() ;
		int offsetstart=1,num_brackets=0;
		char c;
		if(proof==null)
			return vector ;
		for(int i=1;i<proof.length()-1;i++) {
			c=proof.charAt(i);
			if((c=='(')||(c=='['))
				num_brackets++;
			else if((c==')')||(c==']'))
				num_brackets--;
			if(((c==',')||(i==proof.length()-2))&&
				(i>offsetstart)&&(num_brackets==0)) {
				vector.addElement(proof.substring(offsetstart,(c==',') 
					? i : i+1));
				offsetstart=i+1;
			}
		}
		return vector ;
	}
	
	public String toString()
	{
		return "Id: |" + _id + "| - originalGoal: |" + _originalGoal +
		"\n\t| - Goal: " + _goal + " | Subgoals: |" + _resolvent + 
		"\n\t| - Proof: " + _proof +
		"\n\t| - Trace: " + _trace +
		"\n\t| - LastExpandedGoal: " + _lastExpandedGoal +
		"\n\t| - Requester: " + _requester +
		"\n\t| - ReqQueryId: " + _reqQueryId +
		"\n\t| - Status: " + getStatusString(_status) ;
	}
	
 	public static String getStatusString (int status)
 	{
 		String result ;
 		switch (status)
		{
 			case UNSPECIFIED:
 				result = "UNSPECIFIED" ;
 				break ;
 			case READY:
 				result = "READY" ;
 				break ;
 			case WAITING:
 				result = "WAITING" ;
 				break ;
 			case ANSWERED_AND_WAITING:
 				result = "ANSWERED_AND_WAITING" ;
 				break ;
 			case ANSWERED:
 				result = "ANSWERED" ;
 				break ;
 			case FAILED:
 				result = "FAILED" ;
 				break ;
 			default:
 				result = "UNKNOWN" ;
		}
 		return result ;
 	}
}
