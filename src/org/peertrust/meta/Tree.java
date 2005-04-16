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

/**
 * $Id: Tree.java,v 1.6 2005/04/16 21:29:42 dolmedilla Exp $
 * @author olmedilla
 * @date 05-Dec-2003
 * Last changed  $Date: 2005/04/16 21:29:42 $
 * by $Author: dolmedilla $
 * @description
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
 	public static final int NULL_ID = -1 ;

 	private long _id = NULL_ID ;
 	private String _originalGoal = null ;
 	private String _goal = null ;
 	private String _resolvent = null ;
 	private String _stringProof = null ;
 	private Vector _vectorProof ;
 	private int _status = UNSPECIFIED ;
 	private Peer _requester = null ;
 	private long _reqQueryId = NULL_ID ;

	private Peer _delegator = null ;
	private String _lastExpandedGoal = null ;
	private long _timeStamp = 0 ;
	static private long _currentId = 0 ;

	// addition to distinguish different negotiation paths
	long _currentNegotiationCounter ;
	Vector _negotiationIdList = new Vector() ;

	public synchronized void addNegotiationId ()
	{
//		resetNegotiationCounter () ;
//		_negotiationIdList.add(new Long (increaseCounter())) ;
		
		log.debug ("---TNVizListener entra2") ;
		_negotiationIdList.add(new Long (1)) ;
		log.debug ("---TNVizListener sale2") ;
	}
	
	public synchronized void setNegotiationIds (long [] array)
	{		
		_negotiationIdList = new Vector () ;
		
		if (array != null)		
			for (int i = 0 ; i < array.length ; i++)
				_negotiationIdList.add(new Long (array[i])) ;
	}
	
	public synchronized long[] getNegotiationIds ()
	{
		long[] array = new long[_negotiationIdList.size()] ;
		for (int i = 0 ; i < _negotiationIdList.size() ; i++)
			array[i] = ( (Long) _negotiationIdList.elementAt(i)).longValue() ;
		return array ;
	}
	
//	private synchronized void resetNegotiationCounter ()
//	{
//		_currentNegotiationCounter = 0 ;
//	}
	
	public String printNegotiationIdList()
	{
		String list = "[" ;
		
		if (_negotiationIdList != null)
		{
			for (int i = 0 ; i < _negotiationIdList.size() ; i++)
			{
				list += ( (Long)_negotiationIdList.elementAt(i)).longValue() ;
				
				if (i != _negotiationIdList.size())
					list += ","  ;
			}
			
		}
		list += "]" ;
		
		return list ;
	}
	
	public synchronized long increaseNegotiationCounter ()
	{
		long id = -1 ;
		
		log.debug ("---TNVizListener entra " + this.printNegotiationIdList()) ;
		if (_negotiationIdList.isEmpty() == false)
		{ log.debug ("---TNVizListener 1") ;
			id = ( (Long) _negotiationIdList.lastElement()).longValue() ;
			id++ ;
			log.debug ("---TNVizListener 2") ;
			_negotiationIdList.setElementAt(new Long(id), _negotiationIdList.size()-1) ;
		}
		
		log.debug ("---TNVizListener sale" + this.printNegotiationIdList()) ;
		
		return id ;
	}
	
/*	synchronized long increaseCounter ()
 	{
 		_currentNegotiationCounter ++ ;
 		return _currentNegotiationCounter ;
 	}*/
	
 	Tree (long id, String goal, String subqueries, String proof, int status, Peer requester, 
 			long reqQueryId, Peer delegator, String lastExpandedGoal, long [] negotiationIdList)
 	{
 		this._id = id ;
 		this._originalGoal = goal ;
 		this._goal = goal ;
 		this._resolvent = subqueries ;
 		setProof(proof) ;
 		this._status = status ;
 		this._requester = requester ;	
 		this._reqQueryId = reqQueryId ;
 		this._delegator = delegator ;
 		this._lastExpandedGoal = lastExpandedGoal ;
 		setNegotiationIds(negotiationIdList) ;
 		_timeStamp = System.currentTimeMillis() ;
 		log.debug("Created: " + this.toString()) ;
 	}
 	
 	// Constructor for a completely new query
 	public Tree (String goal, Peer requester, long reqQueryId, long [] negotiationIdList)
	{
 		this(getNewId(), goal, "[query(" + goal + ",no)]", "[]", READY, requester, 
 				reqQueryId, null, null, negotiationIdList) ;
	}
 	
	Tree (String goal, String subqueries, String proof, int status, Peer requester, 
			long reqQueryId, Peer delegator, String lastExpandedGoal, long [] negotiationIdList)
 	{
 		this(getNewId(), goal, subqueries, proof, status, requester, reqQueryId, 
 				delegator, lastExpandedGoal, negotiationIdList) ;
 	}

	public Tree (String goal, String subqueries, String proof, int status, Peer requester, 
			long reqQueryId, long [] negotiationIdList)
	{
		this(getNewId(), goal, subqueries, proof, status, requester, reqQueryId, 
				null, null, negotiationIdList) ;
	}
 	
	Tree (long id, Peer requester, long reqQueryId)
	{
		this(id, null, null, null, UNSPECIFIED, requester, reqQueryId, null, null, Query.NO_RELATED_QUERY) ;
		log.debug("Created pattern tree. Id: |" + id + "|") ;
	}
	
	// constructor with only tree Id (specially for searching by tree id)
 	public Tree (long id)
 	{
 		this(id, null, null, null, UNSPECIFIED, null, NULL_ID, null, null, Query.NO_RELATED_QUERY) ;
 	}
 	
	// constructor with only requester and requester Id (specially for searching by requester and requester query id)
 	public Tree (Peer requester, long reqQueryId)
 	{
 		this(NULL_ID, null, null, null, UNSPECIFIED, null, NULL_ID, null, null,  Query.NO_RELATED_QUERY) ;
 	}

 	// copy a tree but change the id
 	public Tree (Tree tree)
 	{
 		this(getNewId(), tree.getGoal(), tree.getResolvent(), tree.getProof(), tree.getStatus(),
 				tree.getRequester(), tree.getReqQueryId(), tree.getDelegator(), 
				tree.getLastExpandedGoal(), tree.getNegotiationIds()) ;
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
 		
 		tmpLong = tree.getId() ;
 		if (tmpLong != NULL_ID)
 			this._id = tmpLong ;
 		
 		tmpString = tree.getGoal() ;
 		if (tmpString != null)
 			this._goal = tmpString ;
 		
 		tmpString = tree.getResolvent() ;
 		if (tmpString != null)
 			this._resolvent = tmpString ;
 			
 		tmpString = tree.getProof() ;
 		if (tmpString != null)
 			setProof(tmpString) ;
 		
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
 	
	public void appendProof (String proof2)
	{
		String proof1 = _stringProof ;
		
		String previousProof = proof1.substring(1,proof1.length()-1) ;
		String newProof =  proof2.substring(1,proof2.length()-1) ;
		String joinedProof = "[" ;
		if (previousProof.length() > 2)
		{
			joinedProof += previousProof ;
			if (newProof.length() > 2)
				joinedProof +="," + newProof ;
		}
		else
			joinedProof += newProof ;
		joinedProof += "]" ;	
		
		this._stringProof = joinedProof ;
		this._vectorProof.addAll(generateProofVector(proof2)) ;
	}

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
 		
 	public String getProof() {
 		return _stringProof ; }
 		
 	public void setProof (String proof) {
 		this._stringProof = proof ;
 		this._vectorProof = generateProofVector (proof) ;
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
		return "Id: |" + _id + "| - originalGoal: |" + _originalGoal + "| - Goal: |" + _goal + "| Subgoals: |" +
			_resolvent + "| - Proof: |" + _stringProof + "| LastExpandedGoal: |" + _lastExpandedGoal + "|"  ;
	}
}
