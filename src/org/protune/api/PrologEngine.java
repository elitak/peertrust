package org.protune.api;

import java.io.*;

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
	 * @return
	 * @throws QueryException
	 */
	public abstract boolean isSuccessful(String query) throws QueryException;
	
	/**
	 * Short-hand for one-variable queries (or for queries whose first variable's value we are only
	 * interested in).
	 * @param query
	 * @return
	 * @throws QueryException
	 */
	public abstract String getFirstAnswer(String query) throws QueryException;
	
	/**
	 * Short-hand for one-variable queries (or for queries whose first variable's values we are only
	 * interested in).<br />
	 * <b>NOTE:</b> Each rule must be put on a different line.
	 * @param query
	 * @return
	 * @throws QueryException
	 */
	public abstract String[] getAllAnswers(String query) throws QueryException;
	
}
