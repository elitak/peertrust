/*
 * Created on Jun 18, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.util;

import g4mfs.impl.org.peertrust.PTEngine;
import g4mfs.impl.org.peertrust.TrustClient;
import g4mfs.impl.org.peertrust.config.RunTimeOptions;
import g4mfs.impl.org.peertrust.config.Vocabulary;
import g4mfs.impl.org.peertrust.event.PTEventDispatcher;
import g4mfs.impl.org.peertrust.event.SimplePeer;
import g4mfs.impl.org.peertrust.inference.MinervaProlog;
import g4mfs.impl.org.peertrust.meta.MetaInterpreter;
import g4mfs.impl.org.peertrust.meta.MetaInterpreterListener;
import g4mfs.impl.org.peertrust.net.EntitiesTable;
import g4mfs.impl.org.peertrust.net.socket.SimpleSocketFactory;
import g4mfs.impl.org.peertrust.tnviz.app.TNVizListener;

import org.apache.log4j.PropertyConfigurator;
import g4mfs.impl.org.peertrust.strategy.FIFOQueue;


/**
 * @author ionut constandache ionut_con@yahoo.com
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class StartPTApp 
{

	
	private static final String LOG_CONFIG_FILE = "/home/ionut/PeertrustFiles/demoClient/.logconfig" ;
	
	public void initializePTClient()
	{
		
		final String PREFIX = TrustClient.PREFIX ;
		
		int TIMEOUT = 15000 ;
		int SLEEP_INTERVAL = 500 ;
		
		try
		{
			PropertyConfigurator.configure(LOG_CONFIG_FILE) ;
			EntitiesTable entitiesTable = new EntitiesTable();
			entitiesTable.setEntitiesFile("/home/ionut/PeertrustFiles/demoClient/entities.dat"); // ?????? set the file
			entitiesTable.init();
			
			FIFOQueue fifoQueue = new FIFOQueue();
			fifoQueue.init();
			
			SimpleSocketFactory simpleSocketFactory = new SimpleSocketFactory();
			simpleSocketFactory.setHost("localhost");
			simpleSocketFactory.setPort(32000);
			simpleSocketFactory.init();
			
			PTEventDispatcher ptEventDispatcher = new PTEventDispatcher();
			ptEventDispatcher.init();
			
			RunTimeOptions runTimeOptions = new RunTimeOptions();
			runTimeOptions.setRunningMode("demo");
			runTimeOptions.init();
			
			MinervaProlog minervaProlog = new MinervaProlog();
			minervaProlog.setBaseFolder("/home/ionut/PeertrustFiles/demoClient/"); //????????????????????????
			minervaProlog.setPrologFiles("demoPolicies.alice"); // ??????????????????????
			minervaProlog.setLicense("minervagui.mca");
			minervaProlog.setDebugMode(true);
			minervaProlog.init();
			
			MetaInterpreterListener metaInterpreterListener = new MetaInterpreterListener();
			metaInterpreterListener.setEntitiesTable(entitiesTable);
			metaInterpreterListener.setInferenceEngine(minervaProlog);
			metaInterpreterListener.setQueue(fifoQueue);
			metaInterpreterListener.setCommunicationChannelFactory(simpleSocketFactory);
			metaInterpreterListener.setEventDispatcher(ptEventDispatcher);
			metaInterpreterListener.setRunTimeOptions(runTimeOptions);
			metaInterpreterListener.init();
			
			MetaInterpreter metaInterpreter = new MetaInterpreter();
			metaInterpreter.setPeerName("Alice");
			metaInterpreter.setEntitiesTable(entitiesTable);
			metaInterpreter.setInferenceEngine(minervaProlog);
			metaInterpreter.setQueue(fifoQueue);
			metaInterpreter.setCommunicationChannelFactory(simpleSocketFactory);
			metaInterpreter.setEventDispatcher(ptEventDispatcher);
			metaInterpreter.setRunTimeOptions(runTimeOptions);
			metaInterpreter.init();
			
			
			
			TNVizListener tnvizListener = new TNVizListener();
			tnvizListener.setEventDispatcher(ptEventDispatcher);
			tnvizListener.init();
		
			PTEngine ptEngine = new PTEngine();
			ptEngine.setMetaInterpreter(metaInterpreter);
			ptEngine.setMetaInterpreterListener(metaInterpreterListener);
			ptEngine.setEventDispatcher(ptEventDispatcher);
			ptEngine.setEventListener(tnvizListener);
			ptEngine.init();
			
			
			TrustClient tc = new TrustClient(ptEventDispatcher) ;
			
			tc.sendQuery("request(spanishCourse,Session) @ elearn") ;
			
			long time = System.currentTimeMillis() ;
			
			while (System.currentTimeMillis() - time < TIMEOUT )
				try {
					Thread.sleep(SLEEP_INTERVAL) ;
				} catch (InterruptedException e) {
					// ignore
				}

			System.out.println (PREFIX + "Stopping") ;
			ptEngine.stop() ;
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		
		}
		
	}
	
	public void initializePTServer()
	{
		try
		{
			PropertyConfigurator.configure(LOG_CONFIG_FILE) ;
			EntitiesTable entitiesTable = new EntitiesTable();
			entitiesTable.setEntitiesFile("/home/ionut/PeertrustFiles/demoServer/entities.dat"); // ?????? set the file
			entitiesTable.init();
			
			FIFOQueue fifoQueue = new FIFOQueue();
			fifoQueue.init();
			
			SimpleSocketFactory simpleSocketFactory = new SimpleSocketFactory();
			simpleSocketFactory.setHost("localhost");
			simpleSocketFactory.setPort(37000);
			simpleSocketFactory.init();
			
			PTEventDispatcher ptEventDispatcher = new PTEventDispatcher();
			ptEventDispatcher.init();
			
			RunTimeOptions runTimeOptions = new RunTimeOptions();
			runTimeOptions.setRunningMode("demo");
			runTimeOptions.init();
			
			MinervaProlog minervaProlog = new MinervaProlog();
			minervaProlog.setBaseFolder("/home/ionut/PeertrustFiles/demoServer/"); //????????????????????????
			minervaProlog.setPrologFiles("demoPolicies.eLearn"); // ??????????????????????
			minervaProlog.setLicense("minervagui.mca");
			minervaProlog.setDebugMode(true);
			minervaProlog.init();
			
			MetaInterpreterListener metaInterpreterListener = new MetaInterpreterListener();
			metaInterpreterListener.setEntitiesTable(entitiesTable);
			metaInterpreterListener.setInferenceEngine(minervaProlog);
			metaInterpreterListener.setQueue(fifoQueue);
			metaInterpreterListener.setCommunicationChannelFactory(simpleSocketFactory);
			metaInterpreterListener.setEventDispatcher(ptEventDispatcher);
			metaInterpreterListener.setRunTimeOptions(runTimeOptions);
			metaInterpreterListener.init();
			
			MetaInterpreter metaInterpreter = new MetaInterpreter();
			metaInterpreter.setPeerName("ELearn");
			metaInterpreter.setEntitiesTable(entitiesTable);
			metaInterpreter.setInferenceEngine(minervaProlog);
			metaInterpreter.setQueue(fifoQueue);
			metaInterpreter.setCommunicationChannelFactory(simpleSocketFactory);
			metaInterpreter.setEventDispatcher(ptEventDispatcher);
			metaInterpreter.setRunTimeOptions(runTimeOptions);
			metaInterpreter.init();
			
			
			
			SimplePeer simplePeer = new SimplePeer();
			simplePeer.setEventDispatcher(ptEventDispatcher);
			simplePeer.init();
			
			PTEngine ptEngine = new PTEngine();
			ptEngine.setMetaInterpreter(metaInterpreter);
			ptEngine.setMetaInterpreterListener(metaInterpreterListener);
			ptEngine.setEventDispatcher(ptEventDispatcher);
			ptEngine.setEventListener(simplePeer);
			ptEngine.init();
		
		}
		catch(Exception e)
		{
			e.printStackTrace();
		
		}		
		
	}
	
	public static void main(String[] args)
	{
		if(args.length < 1)
		{
			System.out.println("s for server c for client");
			System.exit(1);
		}
	
		if(args[0].compareTo("s") == 0)
		{
			StartPTApp server = new StartPTApp();
			server.initializePTServer();
		}
		else
		{
			if(args[0].compareTo("c") == 0)
			{
				StartPTApp client = new StartPTApp();
				client.initializePTClient();
			}
			else
			{
				System.out.println("Unknown options");
				System.exit(1);
			}
		}
	
	}

}
