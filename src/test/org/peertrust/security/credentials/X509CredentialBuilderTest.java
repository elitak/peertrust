package test.org.peertrust.security.credentials;

import java.io.File;
import java.util.Vector;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.peertrust.security.credentials.x509.X509CredentialBuilder;
import org.peertrust.security.credentials.x509.X509CredentialStore;

public class X509CredentialBuilderTest extends TestCase {

	private X509CredentialStore testStore;
	private X509CredentialBuilder testItem;

	public X509CredentialBuilderTest( String name ) {
		super( name );
	}

	public static Test suite() {
		return new TestSuite( X509CredentialBuilderTest.class );
	}

	public void setUp() {
		try {
			testItem = new X509CredentialBuilder("CN=X509CredentialBuilderTest" ,CryptToolsTest.getCAKey() );
			testStore = new X509CredentialStore();
		} catch ( Exception e ) {
			assertTrue( "Exception while setting up test environment.", false );
		}
	}

	public void testStoreCredentials() throws Exception {
		for ( int i = 0; i < 5; i++ ) {
			testItem.setSubjectAlias( "CN=X509CredentialBuilderTestSubject_"+i );
			testItem.setSubjectKey( null ); // Random key will be computed...
			testItem.setCredentialString( X509CredentialStoreTest.getCredentialNumber( i ) );
			testItem.createCredential( testStore );
		}
		testStore.saveAllCredentialsToFile( new File( AllCryptoTests.CREDENTIAL_STORE_FILE ) );
	}

	public void testRetrieveCredentials() throws Exception {
		testStore.loadAllCredentialsFromFile( new File( AllCryptoTests.CREDENTIAL_STORE_FILE ) );
		Vector v = testStore.getAllCredentials();
		// Test, if size matches the number of credentials added in the last test
		assertEquals( "CredentialStoreFile was not overwritten.", 5, v.size() );
		for ( int i = 0; i < v.size(); i++ ) {
			assertEquals("Testing Credential " + i + ":", X509CredentialStoreTest.getCredentialNumber( i ), testStore.getCredentialForStringRepresentation(X509CredentialStoreTest.getCredentialNumber(i)).getStringRepresentation());
//			assertEquals( "Testing Credential "+i+".",X509CredentialStoreTest.getCredentialNumber( i ),( ( Credential )v.get( i ) ).getStringRepresentation() );
		}
	}

}
