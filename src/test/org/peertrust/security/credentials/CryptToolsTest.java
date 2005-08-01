package test.org.peertrust.security.credentials;

import junit.framework.*;
//import java.util.*;
import java.io.*;
import java.security.*;
import java.security.cert.*;

import org.peertrust.security.credentials.*;

public class CryptToolsTest extends TestCase {

	private	static KeyStore ks;

	public static PrivateKey getCAKey() throws Exception {
		if ( ks == null ) {
			initKeyStore();
		}
		return (PrivateKey) ks.getKey( "test", "testpwd".toCharArray() );
	}
	
	private static KeyStore loadKeyStore(String keystore) {
		try {
			CryptTools.installBouncyCastle();
			ks = KeyStore.getInstance( "JKS" );
			InputStream is = new FileInputStream( AllCryptoTests.KEY_STORE_FILE );
			ks.load( is,  AllCryptoTests.KEY_STORE_PASSWORD.toCharArray() );
		} catch (Exception e) {
			System.out.println( "Error while setting up test environment." );
			e.printStackTrace();
			System.exit(1);
		}
		return ks ;
	}

	public CryptToolsTest ( String name ) {
		super( name );
		initKeyStore();
	}

	public static Test suite() {
		return new TestSuite( CryptToolsTest .class );
	}

	public void setUp() {
	}


	public void testCreateCredential() {
		String credential = "member(alice)@visa";

		try {
			PrivateKey caKey = getCAKey();
			PublicKey pubKey = CryptTools.makeKeyPair( "RSA", 512 ).getPublic();
			X509Certificate cert = CryptTools.createCert(  AllCryptoTests.SUBJECT_ALIAS, credential, pubKey, AllCryptoTests.ISSUER_DN, caKey );
			ks.setCertificateEntry( cert.getSubjectDN().getName(), cert );
			OutputStream os = new FileOutputStream(  AllCryptoTests.KEY_STORE_FILE );
			ks.store( os, AllCryptoTests.KEY_STORE_PASSWORD.toCharArray() );
			assertEquals( "Extraction of Credential.", credential, CryptTools.extractCredential(cert));
		} catch (Exception e) {
			e.printStackTrace();
			assertTrue( "Exception.", false );
		}
	}

	public static void main( String[] args ) {
		try {
			junit.textui.TestRunner.run( suite() );
		} catch ( Throwable t ) {
			t.printStackTrace();
		}
	}
}
