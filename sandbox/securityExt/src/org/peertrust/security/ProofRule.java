package org.peertrust.security;

import java.io.*;

/**
 * Represents a local rule (not a signed or authenticatesTo one) in a proof tree.
 * @author Sebastian Wittler
 */
public class ProofRule implements Serializable {
	/** The rule as text representation. */
	private String strRule;

	/**
	 * Constructor.
	 * @param rule The local rule as text representation.
	 */
	public ProofRule(String rule) {
		strRule=rule;
	}

	/**
	 * Returns the rule as text.
	 * @return String The rule as text.
	 */
	public String getRule() {
		return strRule;
	}
}