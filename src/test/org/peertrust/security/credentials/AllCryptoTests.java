package test.org.peertrust.security.credentials;

import junit.framework.*;

public class AllCryptoTests {

	public static final String CREDENTIAL_FILE = "credential.cer";
	public static final String KEY_STORE_FILE = "test_keystore";
	public static final String CREDENTIAL_STORE_FILE = "credStore.cst";
	public static final String KEY_STORE_PASSWORD = "testpwd";
	public static final String SUBJECT_ALIAS = "CN=Alice";
	public static final String ISSUER_DN = "CN=Test_CA";

	public static Test suite() {
		TestSuite result = new TestSuite( CryptToolsTest.class );
		result.addTest( new TestSuite( X509CredentialTests.class ) );
		result.addTest( new TestSuite( X509CredentialStoreTest.class ) );
		result.addTest( X509CredentialBuilderTest.suite() );
		return result;
	}

	public static void main( String args[] ) {
		junit.textui.TestRunner.run( suite() );
	}
}
