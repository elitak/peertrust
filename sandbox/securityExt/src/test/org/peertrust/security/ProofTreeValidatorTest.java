package test.org.peertrust.security;

import net.jxta.edutella.util.*;
import org.peertrust.*;
import org.peertrust.meta.*;
import org.peertrust.net.*;
import org.peertrust.security.*;
import org.peertrust.security.credentials.x509.*;
import junit.framework.*;

/**
 * Class that tests the verification of proof tree and credentials (second and third task
 * of the Bachelor-thesis). Credentials of one test are valid until 20 june 2004. This class
 * uses JUnit for testing.
 * @author Sebastian Wittler
 */
public class ProofTreeValidatorTest extends TestCase {
	public ProofTreeValidatorTest(String name) {
		super(name);
	}

	public static Test suite() {
		return new TestSuite(ProofTreeValidatorTest.class);
	}

	/**
	 * Checks if Tree has a valid proof tree.
	 * @param tree The Tree whose proof tree should be checked.
	 * @param proof The proof tree as String, it will be transformed.
	 * @return boolean If the proof tree is valid or not.
	 */
	private boolean checkTree(Tree tree,String proof) throws Throwable {
		Configurator config=new Configurator("trust.properties",new String[0]);
		config.finishConfig();
		tree.appendProofRuleVector(new X509CredentialStore(config.getValue(
			Vocabulary.BASE_FOLDER_TAG)+config.getValue(Vocabulary.
			KEYSTORE_FILE_TAG),config.getValue(Vocabulary.STORE_PASSWORD_TAG)),
			config,proof);
		tree.setLastExpandedGoal(tree.getGoal());
		return ProofTreeValidator.isProofTreeOk(tree,null);
	}

	public void test1() {
		Tree tree=new Tree(1,"request(spanishCourse,session1)@eLearn","",null,
			Tree.READY,new Peer("eLearn","localhost",32000),1,null,"");
		String proof="["+
			"r(request(spanishCourse,V23397626),[policy1(request("+
			"spanishCourse),alice)],[get(spanishCourse,V23397626)])@eLearn"+
			","+
			"r(policy1(request(spanishCourse),alice),[drivingLicense(alice)"+
			"@caState@alice,policeOfficer(alice)@caStatePolice@alice],[])@eLearn"+
			","+
			"proved_by(alice)@eLearn"+
			","+
			"signed(r(drivingLicense(alice)@caState,[],[]),caState,signature("+
			"caState))@alice"+
			","+
			"proved_by(alice)@eLearn"+
			","+
			"signed(r(policeOfficer(alice)@caStatePolice,[],[]),"+
			"caStatePolice,signature(caStatePolice))@alice"+
			","+
			"r(get(spanishCourse,session1),hidden,hidden)@eLearn"+
			"]";
		boolean ret=false;
		try {
			ret=checkTree(tree,proof);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		assertEquals(true,ret);
	}

	public void test2() {
		Tree tree=new Tree(1,"a(ss)","",null,
			Tree.READY,new Peer("eLearn","localhost",32000),1,null,"");
		String proof="["+
			"r(a(A),[b(A)],[])@eLearn"+
			","+
			"r(b(ss),[],[])@eLearn"+
			"]";
		boolean ret=false;
		try {
			ret=checkTree(tree,proof);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		assertEquals(true,ret);
	}

	public void test3() {
		Tree tree=new Tree(1,"a(ss)","",null,
			Tree.READY,new Peer("eLearn","localhost",32000),1,null,"");
		String proof="["+
			"r(a(ss),[],[])@eLearn"+
			"]";
		boolean ret=false;
		try {
			ret=checkTree(tree,proof);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		assertEquals(true,ret);
	}

	public void test4() {
		Tree tree=new Tree(1,"a(ss)@eLearn","",null,
			Tree.READY,new Peer("eLearn","localhost",32000),1,null,"");
		String proof="["+
			"r(a(ss),[],[])@eLearn"+
			"]";
		boolean ret=false;
		try {
			ret=checkTree(tree,proof);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
		assertEquals(true,ret);
	}

	public static void main(String args[]) {
		try {
			junit.textui.TestRunner.run(suite());
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
}