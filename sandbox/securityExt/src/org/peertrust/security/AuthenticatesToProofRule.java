package org.peertrust.security;

/**
 * This class represents the proof of an authenticatesTo-predicate in a proof tree. It
 * contains the certificate chain that should correspond to the parameters of this predicate.
 * @author Sebastian Wittler
 */
public class AuthenticatesToProofRule extends ProofRule {
	/** The certificate chain that should satisfy the authenticatesTo-predicate. */
	private CertificateChain certificatechain;

	/**
	 * Constructor.
	 * @param rule The authenticatesTo-predicate as text.
	 * @param chain The certificate chain that should satisfy the predicate.
	 */
	public AuthenticatesToProofRule(String rule,CertificateChain chain) {
		super(rule);
		certificatechain=chain;
	}

	/**
	 * Returns the certificate chain.
	 * @return CertificateChain The certificate chain that should satisfy the predicate.
	 */
	public CertificateChain getCertificateChain() {
		return certificatechain;
	}
}