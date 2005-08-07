package crypto;

import java.io.*;


public class CryptTest {

	public static int MIN_MESSAGE_PRIORITY = 0;

	private static String testFileName = "Test";
	private static String testText = "I am a certificate.\n Trust me.";

	public static void printMessage(String from, String mess, int prio) {
		if (prio < MIN_MESSAGE_PRIORITY)
			System.out.println("[crypto." + from + "] - " + mess);
	}

	public static void writeFile(String text) {
		try {
			File testFile = new File(testFileName);
			FileWriter testWriter = new FileWriter(testFile);
			testWriter.write(text);
			testWriter.close();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}

	public static void main(String[] args) {
		printMessage("CryptTest.main", "invoked.", 1);

		printMessage("CryptTest.main", "Adding Bouncing Castle JCE Provider", 1);
		java.security.Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());

/*		printMessage("CryptTest.main", "I will try to write into a file in this directory.", 1);
		printMessage("CryptTest.main", "Filename = " + testFileName, 1);

		writeFile(testText);
*/
		printMessage("CryptTest.main", "Print available information of installed security providers:", 1);
		CryptTools.printProviders();

/*		printMessage("CryptTest.main", "Print hashvalue of Testfile:", 1);
		try {
			byte[] hv = CryptTools.computeMessageDigestOf(new File(testFileName));
			for (int i = 0; i < hv.length; i++) {
				System.out.print(hv[i]);
			}
			System.out.println();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}

		CryptGui gui = new CryptGui();

		printMessage("CryptTest.main", "Try to store a credential", 1);
		String credential = "is member@visa | signed by ericSign";
		byte[] signature = CryptTools.hashString(credential);
		//CredentialManager cmgr = new CredentialManager();
		//cmgr.addCredential(credential, signature);
*/
	}
}
