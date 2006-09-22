package org.protune.net;

import java.io.Serializable;

/**
 * In order to grant that communication among nodes actually takes place, it is required that each node
 * is able to understand the information it receives, which means that the nodes need to share the same
 * information representation. The interface <tt>Message</tt> represents the common format of the
 * information the two nodes exchange. Different applications can implement the interface in different
 * ways.
 * @author jldecoi
 */
interface Message extends Serializable{}
