package test.org.peertrust.security;

import org.apache.log4j.Logger;
import net.jxta.edutella.util.*;
import org.peertrust.*;
import org.peertrust.meta.*;
import org.peertrust.inference.*;
import org.peertrust.strategy.*;
import org.peertrust.net.*;

/**
 * Class for testing the authenticatesTo-predicate. Two tests are made. Some certificates
 * used were only valid until 4 august 2004, from 6 july 2004 on.
 * @author Sebastian Wittler
 */
public class AuthenticatesToTest {
	/** First Party that participates in the trust negotiation. */
	private MetaInterpreter meta1=null;
	/** Second Party that participates in the trust negotiation. */
	private MetaInterpreter meta2=null;
	/** Stores the location of the configuration-folder. */
	private String strBaseFolder;

	/**
	 * Constructor.
	 * Initializes and starts the two parties that test the autheticatesTo-predicate.
	 */
	public AuthenticatesToTest() {
		try {
			//Create first party
			Configurator config=new Configurator("trust.properties",new String[0]);
			config.finishConfig();
			strBaseFolder=config.getValue(Vocabulary.BASE_FOLDER_TAG);
			config=new Configurator(strBaseFolder+"trust.properties.client",
				new String[0]);
			MinervaProlog engine1=new MinervaProlog(config);
			config.register(engine1);
			meta1=new MetaInterpreter(new FIFOQueue(),engine1,config);
			config.register(meta1);
			config.finishConfig();
			meta1.run();
			//Create second party
			config=new Configurator(strBaseFolder+"trust.properties.server",
				new String[0]);
			MinervaProlog engine2=new MinervaProlog(config);
			config.register(engine2);
			meta2=new MetaInterpreter(new FIFOQueue(),engine2,config);
			config.register(meta2);
			config.finishConfig();
			meta2.run();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Starts the negotiation of an authenticatesTo-predicate.
	 * @param goal The authenticatesTo-predicate.
	 * @return String The result of the negotiation.
	 */
	private String negotiation(String goal) throws Throwable {
		Configurator config=new Configurator(strBaseFolder+"trust.properties.client",
			new String[0]);
		TrustClient client=new TrustClient(goal,new Peer("alice","localhost",32000),
			config);
		config.register(client);
		config.finishConfig();
		client.run();
		return client.getSolution();
	}

	public void test1() {
		String str="";
		try {
			if(true==
				negotiation("authenticatesTo(schorsch,alice,adelheid)@elearn").
				length()>0)
				str+="Test 1 successfull\n";
			else
				str+="Test 1 failed\n";
			if(true==
				negotiation("authenticatesTo(alice,alice,alice)@elearn").
				length()>0)
				str+="Test 2 successfull\n";
			else
				str+="Test 2 failed\n";
			System.out.println("\nResult:\n"+str);
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}

	public static void main(String args[]) {
		try {
			new AuthenticatesToTest().test1();
		}
		catch(Throwable t) {
			t.printStackTrace();
		}
	}
}