package test.org.peertrust.security.credentials;

import junit.framework.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

import org.peertrust.security.credentials.CryptTools;
import org.peertrust.security.credentials.x509.*;

public class X509CredentialTests extends TestCase {

	private X509Credential testItem;
	
	public X509CredentialTests( String name ) {
		super( name );
	}

	public static Test suite() {
		return new TestSuite( X509CredentialTests.class );
	}


	public static void main( String[] args ) {
		try {
			junit.textui.TestRunner.run( suite() );
		} catch ( Throwable t ) {
			t.printStackTrace();
		}
	}

	public void setUp() {
		try {
			KeyStore ks = KeyStore.getInstance( "JKS" );
			InputStream is = new FileInputStream( AllCryptoTests.KEY_STORE_FILE );
			ks.load( is,  AllCryptoTests.KEY_STORE_PASSWORD.toCharArray() );
			X509Certificate cert = ( X509Certificate )ks.getCertificate( AllCryptoTests.SUBJECT_ALIAS );
			X509CredentialStore credStore = new X509CredentialStore();
			testItem = ( X509Credential )credStore.buildCredential( cert );
		} catch ( Exception e ) {
			e.printStackTrace();
			assertTrue("Exception while setting up test environment.", false);
		}
	}



	// Important: A Credential has to exist! Run CrypToolsTest.java first!
	public void testExport() {
		try {
			File file = new File( AllCryptoTests.CREDENTIAL_FILE );
			testItem.exportCredential( file );

			// Now a X.509 Certificate should exist in the file system.
			// We should be able to load it with CryptTools and... 

			/*InputStream inStream = new FileInputStream( CREDENTIAL_FILE );
			  CertificateFactory cf = CertificateFactory.getInstance("X.509");
			  X509Certificate cert = (X509Certificate)cf.generateCertificate(inStream);
			  inStream.close();*/

			// It doesn't seem to be possible to export X509Certificates in java. I 
			// think we have to use keytool to export X509Certificates. :-(

			InputStream inStream = new FileInputStream( AllCryptoTests.CREDENTIAL_FILE ); 
			ObjectInputStream ois = new ObjectInputStream( inStream );
			X509Certificate cert = ( X509Certificate )ois.readObject();

			// ...extract a Credential-String.

			assertEquals( testItem.getStringRepresentation(), CryptTools.extractCredential( cert ) );
		} catch ( Exception e ) {
			e.printStackTrace();
			assertTrue( "Exception.", false );
		}	
	}


	// Important: A Credential has to exist! Run CrypToolsTest.java first!
	public void testImport() {
		try {
			File file = new File( AllCryptoTests.CREDENTIAL_FILE );
			String temp = testItem.getStringRepresentation();
			testItem.importCredential( file );
			assertEquals( temp, testItem.getStringRepresentation() );
		} catch ( Exception e ) {
			e.printStackTrace();
			assertTrue( "Exception.", false );
		}
	}

}
