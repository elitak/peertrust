package org.protune.api;

/**
 * Class <tt>SideEffectAction</tt> represents a provisional predicate. Each
 * provisional predicate should extend <tt>SideEffectAction</tt>. Deliverable
 * <tt>I2-D2</tt> lists the following provisional predicates
 * <ol>
 * <li><tt>credential(C, K)</tt></li>
 * <li><tt>declaration(D)</tt></li>
 * <li><tt>do(uri_or_service_request)</tt></li>
 * <li><tt>authenticates_to(K)</tt></li>
 * <li><tt>logged(X, logfile_name)</tt></li>
 * </ol>
 * The intended meaning is as follows: the predicate holds if
 * <ol>
 * <li>a credential
 * <ul>
 * <li>matching <tt>C</tt> and</li>
 * <li>signed by the principal whose public key is <tt>K</tt></li>
 * </ul>
 * is sent</li>
 * <li>a declaration matching <tt>D</tt> is sent</li>
 * <li>the peer connects to <tt>uri</tt> or invokes <tt>service_request</tt></li>
 * <li>the peer proves it is the owner of the private key associated to
 * <tt>K</tt></li>
 * <li><tt>X</tt> is recorded into <tt>logfile_name</tt></li>
 * </ol>
 * These provisional predicates trigger some events (resp. the sending of a
 * credential/desclaration, the connection to a <tt>uri</tt>, the invocation
 * of a service, a challenge procedure) which can eventually modify some
 * resources (e.g. the <tt>log</tt> file) and do not provide any result.<br />
 * <b>OPEN ISSUE:</b> So far only {@link org.protune.api.LogAction} was
 * implemented: for the other subclasses of <tt>SideEffectAction</tt> a
 * discussion is needed.
 * 
 * @author jldecoi
 */
public abstract class SideEffectAction extends Action {
}
