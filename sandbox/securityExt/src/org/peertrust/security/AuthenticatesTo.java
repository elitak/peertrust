package org.peertrust.security;

import java.net.*;
import java.io.*;
import java.util.*;
import javax.net.ssl.*;
import java.security.cert.*;
import java.security.*;
import org.peertrust.meta.*;
import net.jxta.edutella.util.*;
import org.peertrust.inference.*;
import org.peertrust.net.ssl.*;
import org.peertrust.strategy.*;
import org.peertrust.security.credentials.x509.*;
import org.peertrust.net.*;
import org.peertrust.*;

/**
 * This class contains the complete handling of the authenticatesTo-predicate. Its
 * methods must be called from the MetaInterpreter- and MetaInterpreterListener-classes.
 * Exact syntax for predicate: authenticatesTo(Identity,Party,Authority)@Requester
 * @author Sebastian Wittler
 */
public class AuthenticatesTo {
	/** Name of the algorithm that should be used for encryption. */
	public static final String CRYPT_ALGORITHM="MD5withRSA";
	/** Name of the authenticatesTo-predicate. */
	public static final String AUTH_TO_PREDICATE="authenticatesTo";
	/** Name of the predicate that is used between Requester and Party in the
	 *  authenticatesTo-predicate-handling in order to communicate. */
	public static final String AUTH_PREDICATE="authentication";

	/**
	 * Looks if an authenticatesTo-predicate appears in the Tree's subgoals. If yes,
	 * this predicate is either delegated to the right party or Requester is informed how
	 * he must authenticate himself in order to satisfy the predicate.
	 * This method must be called from the MetaInterpreter.
	 * @param tree The Tree-object that is should be examined.
	 * @param metainterpreter The local MetaInterpreter-object.
	 * @param localPeer The local peer.
	 * @param config The config-object.
	 * @param queue The queue the MetaInterpreter uses.
	 * @return boolean If an authenticatesTo-predicate was contained in the Tree-object's
	 *	subgoals or not.
	 */
	public static boolean authenticatesToPredicate(Tree tree,MetaInterpreter
		metainterpreter,Hashtable entities,Peer localPeer,Configurator config,
		Queue queue) {
		int a=0,b=0;
		String goals=tree.getSubqueries();
		boolean firstbracket=true;
		boolean result=true;
		//If the authenticatesTo-predicate appears in one of the Tree's subgoals
		if(((a=goals.indexOf("("+AUTH_TO_PREDICATE+"("))!=-1)&&(a<8)) {
			//Parse the predicate out of the subgoals-list.
			while(((firstbracket)||(b!=0))&&(a<goals.length())) {
				if(goals.charAt(a)=='(') {
					if(firstbracket)
						firstbracket=false;
					b++;
				}
				else if(goals.charAt(a)==')')
					b--;
				a++;
			}
			String auth=goals.substring(7,a-4);
			String aim=null;
			if((a=auth.lastIndexOf("@"))!=-1)
				aim=auth.substring(a+1);
			//Construct a special authentication-message for the party that must
			//authenticate himself to satisfy the predicate.
			StringBuffer strbuf=new StringBuffer(AUTH_PREDICATE+"(");
			//Add the Identity-parameter of the authenticatesTo-predicate to the
			//message as the first parameter.
			a=auth.indexOf("(");
			b=auth.indexOf(",",a+1);
			String identity=(b!=-1)&&(b-a-1>0) ? auth.substring(a+1,b) : "";
			strbuf.append(identity);
			strbuf.append(",");
			//Parse the Party-parameter out of the authenticatesTo-predicate
			a=b+1;
			b=auth.indexOf(",",a);
			String party=(b!=-1)&&(b-a>0) ? auth.substring(a,b) : "";
			if(!party.equals(config.getValue(Vocabulary.PEERNAME))) {
				tree.setLastExpandedGoal(auth);
				tree.setStatus(Tree.WAITING);
				queue.add(tree);
				metainterpreter.sendMessage(new Query(auth,localPeer,
					tree.getId()),(Peer)entities.get(party));
				return true;
			}
			//Add the Authority-parameter of the authenticatesTo-predicate to the
			//message as the second parameter.
			a=b+1;
			b=auth.indexOf(")",a);
			String authority=(b!=-1)&&(b-a>0) ? auth.substring(a,b) : "";
			strbuf.append(authority);
			strbuf.append(",");
			//Create a random text String and append it to the message as third
			//parameter.
			Random random=new Random();
			byte enc_bytes[]=new byte[Math.abs(random.nextInt())%31+20];
			random.nextBytes(enc_bytes);
			String enc_str=new String(enc_bytes);
			strbuf.append(enc_str);
			strbuf.append(")");
			//Store the authenticatesTo-predicate and the authentication-message,
			//these information will be needed later when verifying the answer.
			tree.setLastExpandedGoal(auth+","+strbuf.toString());
			tree.setStatus(Tree.WAITING);
			queue.add(tree);
			//Send the authentication-message to Requester
			metainterpreter.sendMessage(new Query(strbuf.toString(),localPeer,
				tree.getId()),(Peer)entities.get(aim));
		}
		else
			return false;
		return true;
	}

	/**
	 * If the authentication-message is contained in the Tree-object, the local party
	 * must authenticate himself right in order to satisfy the authenticatesTo-predicate.
	 * This method must be called from the MetaInterpreter.
	 * @param tree The Tree-object that is should be examined.
	 * @param config The config-object.
	 * @param localPeer The local peer.
	 * @param metainterpreter The local MetaInterpreter-object.
	 * @return boolean If an authentication-message was contained in the Tree-object's
	 *	subgoals or not.
	 */
	public static boolean authenticatesPredicate(Tree tree,Configurator config,
		Peer localPeer,MetaInterpreter metainterpreter) {
		//If the authentication-message isn't contained in the Tree, quit.
		String str=tree.getGoal();
		if(!str.startsWith(AUTH_PREDICATE+"("))
			return false;
		//Parse out the three parameters of the message, identity will be null if the
		//corresponding parameter is a variable.
		int a=str.indexOf("("),b=str.indexOf(",");
		String identity=(a!=-1)&&(b!=-1)&&(b-a-1>0) ? str.substring(a+1,b) : "";
		if((identity.length()>0)&&(Character.isUpperCase(identity.charAt(0))))
			identity=null;
		a=b+1;
		b=str.indexOf(",",b+1);
		String authority=(b!=-1)&&(b-a>0) ? str.substring(a,b) : "";
		String enc_str=str.substring(b+1,str.length()-1);
		//Build a certificate chain according to the parameters of the message,
		//the first certificate's subject must be identity, if not null, and
		//the issuer of the last certificate in the chain must be authority.
		CertificateChain certchain=new CertificateChain(config,identity,null,
			authority);
		//Store all private keys of the keystore into a Vector
		Vector vectorPrivateKeys=new Vector();
		try {
			KeyStore ks=KeyStore.getInstance("JKS");
			ks.load(new FileInputStream(config.getValue(
				Vocabulary.BASE_FOLDER_TAG)+config.getValue(
				Vocabulary.KEYSTORE_FILE_TAG)),config.getValue(
				Vocabulary.STORE_PASSWORD_TAG).toCharArray());
			Enumeration enum=ks.aliases();
			PrivateKey privatekey=null;
			String alias;
			while(enum.hasMoreElements()) {
				alias=(String)enum.nextElement();
				if(!ks.isKeyEntry(alias))
					continue;
				try {
					privatekey=(PrivateKey)ks.getKey(alias,config.getValue(
						Vocabulary.KEY_PASSWORD_TAG).
						toCharArray());
					if(privatekey!=null)
						vectorPrivateKeys.addElement(privatekey);
				}
				catch(Exception e) {
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		//Try to decrypt the random text.
		String dec_str=enc_str;
		try {
			//Get the public key of the first certificate of the chain.
			X509Certificate certs[]=certchain.getCertificates();
			if((identity==null)&&(certs.length>0))
				identity=CertificateChain.getSubjectAlias(certs[0]);
			PublicKey publickey=(certs.length>0) ? certs[0].getPublicKey() : null;
			Signature signature=Signature.getInstance(CRYPT_ALGORITHM);
			//Try all private keys in the Vector if the are suitable for the
			//public key.
			for(int i=0;i<vectorPrivateKeys.size();i++) {
				//Decrypt random text with the current private key.
				signature.initSign((PrivateKey)vectorPrivateKeys.elementAt(i));
				signature.update(enc_str.getBytes());
				byte dec[]=signature.sign();
				//If this decryption can be reversed with the public key, the
				//current private key is the right one and the decrypted bytes
				//will be transformed into a String
				signature.initVerify(publickey);
				signature.update(enc_str.getBytes());
				if(signature.verify(dec)) {
					StringBuffer strbuf=new StringBuffer();
					for(int j=0;j<dec.length;j++) {
						strbuf.append(dec[j]);
						if(j<dec.length-1)
							strbuf.append(' ');
					}
					dec_str=strbuf.toString();
					break;
				}
			}
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		//Add the suitable AuthenticatesToProofRule-object to the proof tree of the
		//answer
		Vector vector=new Vector();
		vector.add(new AuthenticatesToProofRule("r("+AUTH_TO_PREDICATE+"("+identity+
			","+tree.getRequester().getAlias()+","+authority+"),[],[])@"+localPeer.
			getAlias(),certchain));
		//Create and send the answer back in order to authenticate and satify the
		//authenticatesTo-predicate
		str=AUTH_PREDICATE+"("+identity+","+authority+","+dec_str+")";
		Answer answer=new Answer(str,vector,Answer.LAST_ANSWER,
			tree.getReqQueryId(),localPeer);
		metainterpreter.sendMessage(answer,tree.getRequester());
		return true;
	}

	/**
	 * Checks, if the current answer contains an authentication-predicate and satisfies
	 * an authenticatesTo-predicate that must be evaluated by the local peer.
	 * This method must be called from the MetaInterpreterListener.
	 * @param tree The Tree-object that matches to the answer.
	 * @param answer The current answer.
	 * @param engine The inference engine.
	 * @return boolean Is false, if this is an authentication-answer, but doesn't satisfy
	 *	the authenticatesTo-predicate, otherwise true.
	 */
	public static boolean authentication_answerPredicate(Tree tree,Answer answer,
		InferenceEngine engine) {
		String goal1=tree.getLastExpandedGoal(),goal2=answer.getGoal(),auth;
		//When the answer contains a authenticatesTo-predicate, unify the Tree with
		//the answer and delete the authenticatesTo-predicate from the subgoal-list.
		if(goal2.startsWith(AUTH_TO_PREDICATE+"(")) {
			String str=tree.getLastExpandedGoal();
			engine.unifyTree(tree,answer.getGoal());
			if(tree.getLastExpandedGoal()==null)
				tree.setLastExpandedGoal(str);
			String sub=tree.getSubqueries();
			String term="query("+goal2+",no)";
			int a;
			if((a=sub.indexOf(term))!=-1) {
				sub=sub.substring(0,a)+sub.substring(a+term.length());
				sub=sub.replaceAll("\\[,","[");
				sub=sub.replaceAll(",\\]","]");
				sub=sub.replaceAll(",,",",");
				tree.setSubqueries(sub);
			}
			return true;
		}
		//If the answer doesn't contain a authentication-message, quit.
		if(!goal2.startsWith(AUTH_PREDICATE+"("))
			return true;
		//Before an authentication-message was sent to Party, the authenticatesTo-
		//predicate and authentication-message were stored. Here, they are parsed.
		int a=0,b=0;
		boolean firstbracket=true;
		while(((firstbracket)||(b!=0))&&(a<goal1.length())) {
			if(goal1.charAt(a)=='(') {
				if(firstbracket)
					firstbracket=false;
				b++;
			}
			else if(goal1.charAt(a)==')')
				b--;
			a++;
		}
		a=goal1.indexOf(",",a);
		if(a==-1)
			return false;
		auth=goal1.substring(0,a);
		goal1=goal1.substring(a+1);
		//Parse identity, authority and the random text out of the authentication-
		//message previously sent.
		a=goal1.indexOf("(");
		b=goal1.indexOf(",");
		String identity1=(a!=-1)&&(b!=-1)&&(b-a-1>0) ? goal1.substring(a+1,b) : "";
		a=b+1;
		b=goal1.indexOf(",",b+1);
		String authority1=(b!=-1)&&(b-a>0) ? goal1.substring(a,b) : "";
		String enc_str=goal1.substring(b+1,goal1.length()-1);
		//Parse identity, authority and the decrypted text out of the authentication-
		//answer of Party.
		a=goal2.indexOf("(");
		b=goal2.indexOf(",");
		String identity2=(a!=-1)&&(b!=-1)&&(b-a-1>0) ? goal2.substring(a+1,b) : "";
		a=b+1;
		b=goal2.indexOf(",",b+1);
		String authority2=(b!=-1)&&(b-a>0) ? goal2.substring(a,b) : "";
		String dec_str=goal2.substring(b+1,goal2.length()-1);
		//If the identity-parameters of the previous sent authentication-answer
		//doesn't match (if it was not specified as a variable), and the authority-
		//parameters of bot aren't equal, the current answer is wrong and the
		//authenticatesTo-predicate fails.
		if(((identity1.length()>0)&&(!Character.isUpperCase(identity1.charAt(0)))&&
			(!identity1.equals(identity2)))||(!authority1.equals(authority2)))
			return false;
		//Transform the decrypted text from Party into a byte-array.
		Vector vector=new Vector();
		a=0;
		try {
			while((b=dec_str.indexOf(" ",a))!=-1) {
				vector.add(new Byte(dec_str.substring(a,b)));
				a=b+1;
			}
			vector.add(new Byte(dec_str.substring(a)));
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		byte dec[]=new byte[vector.size()];
		for(int i=0;i<vector.size();i++)
			dec[i]=((Byte)vector.elementAt(i)).byteValue();
		//The authenticatesTo-predicate also fails if the proof tree is empty or
		//has no AuthenticatesToProofRule-object as the last entry.
		vector=answer.getProofRuleVector();
		if((vector.size()==0)||!(vector.elementAt(vector.size()-1) instanceof
			AuthenticatesToProofRule))
			return false;
		//Get the AuthenticatesToProofRule-object and the included certificate chain,
		//which must be greater than 0.
		AuthenticatesToProofRule authrule=(AuthenticatesToProofRule)
			vector.elementAt(vector.size()-1);
		X509Certificate certs[];
		if((authrule.getCertificateChain()==null)||((certs=authrule.
			getCertificateChain().getCertificates())==null)||(certs.length==0))
			return false;
		try {
			//If the first chain's certificate subject doesn't match to the
			//identity-parameter in the answer and the last chain's certificate
			//issuer isn't equal to the authority-parameter in the answer,
			//the authenticatesTo-predicate fails.
			if((!identity2.equals(CertificateChain.getSubjectAlias(certs[0]))||
				(!authority2.equals(CertificateChain.getIssuerAlias(
				certs[certs.length-1])))))
				return false;
			//Verify if the private-key-proof from Party was right. Encrypt the
			//decryption he sent with the first certificate's public key and
			//compare the result with the random text previouls sent.
			Signature signature=Signature.getInstance(CRYPT_ALGORITHM);
			signature.initVerify(certs[0].getPublicKey());
			signature.update(enc_str.getBytes());
			signature.verify(dec);
		}
		catch(Exception e) {
			e.printStackTrace();
			return false;
		}
		tree.setLastExpandedGoal(auth);
		//Replace the identity-parameter in the authenticatesTo-predicate with the
		//one from the answer (so that Tree can be unified later).
		auth=auth.substring(0,AUTH_TO_PREDICATE.length())+auth.substring(
			AUTH_TO_PREDICATE.length()).replaceFirst(identity1,identity2);
		answer.setGoal(auth);
		//authenticatesTo-predicate succeeded, so it is removed from the subgoals-list.
		firstbracket=true;
		a=b=0;
		String goals=tree.getSubqueries();
		while(((firstbracket)||(b!=0))&&(a<goals.length())) {
			if(goals.charAt(a)=='(') {
				if(firstbracket)
					firstbracket=false;
				b++;
			}
			else if(goals.charAt(a)==')')
				b--;
			a++;
		}
		if(goals.charAt(a)==',')
			a++;
		tree.setSubqueries("["+goals.substring(a));
		//tree.appendProofRuleVector(null,null,auth);
		tree.setStatus(Tree.ANSWERED);
		String str=tree.getLastExpandedGoal();
		//Unification of the query goal with the answer.
		engine.unifyTree(tree,answer.getGoal());
		if(tree.getLastExpandedGoal()==null)
			tree.setLastExpandedGoal(str);
		return true;
	}
}