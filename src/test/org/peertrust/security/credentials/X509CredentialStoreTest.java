package test.org.peertrust.security.credentials;

import junit.framework.*;
import java.security.*;
import java.security.cert.*;
import java.io.*;
import java.util.*;

import org.peertrust.security.credentials.CryptTools;
import org.peertrust.security.credentials.x509.*;

public class X509CredentialStoreTest extends TestCase {

	private X509CredentialStore testStore;
	private String credentialOwner = "test_credential_owner";

	public X509CredentialStoreTest( String name ) {
		super( name );
	}

	public static Test suite() {
		return new TestSuite( X509CredentialStoreTest.class );
	}

	protected static String getCredentialNumber( int i ) {
		return "credential(" + i + ")@test";
	}

	public void setUp() {
		testStore = new X509CredentialStore();
	}

	public void testStoreCredentials() {
		try {
			for ( int i = 0; i < 5; i++ ) {
				X509Certificate cert = CryptTools.createCert( "CN=" + credentialOwner + "_" + i, getCredentialNumber( i ), CryptTools.makeKeyPair().getPublic(), "CN=X509CredentialStoreTest", CryptToolsTest.getCAKey() );
				testStore.addCredential( testStore.buildCredential( cert ) );
			}
			testStore.saveAllCredentialsToFile( new File( AllCryptoTests.CREDENTIAL_STORE_FILE ) );
		} catch ( Exception e ) {
			e.printStackTrace();
			assertTrue( "Exception.", false );
		}
	}

	public void testRetrieveCredentials() {
		try {
			testStore.loadAllCredentialsFromFile( new File( AllCryptoTests.CREDENTIAL_STORE_FILE ) );
			Vector v = testStore.getAllCredentials();
			assertEquals( "CredentialStoreFile was not overwritten.", 5, v.size() );
			for ( int i = 0; i < v.size(); i++ ) {
				// This test shows if the credential exists.
				assertEquals("Testing Credential " + i + ":", getCredentialNumber( i ), testStore.getCredentialForStringRepresentation(getCredentialNumber(i)).getStringRepresentation());
				//assertEquals( "Testing Credential " + i + ":", getCredentialNumber( i ), ((Credential)v.get( i )).getStringRepresentation() );
				
			}
		} catch ( Exception e ) {
			e.printStackTrace();
			assertTrue( "Exception.", false );
		}
	}

	public void testCredentialStoreUsesValidKeystore() throws Exception {
		CryptTools.installBouncyCastle();
		KeyStore ks = KeyStore.getInstance( "JKS" );
		InputStream is = new FileInputStream( AllCryptoTests.CREDENTIAL_STORE_FILE );
		// TODO: Assure that always the same key is used as in the creation of the CredentialStore.
		ks.load( is, "testpwd".toCharArray() );
		assertEquals("Wrong cert count",5,ks.size());
	}

}
