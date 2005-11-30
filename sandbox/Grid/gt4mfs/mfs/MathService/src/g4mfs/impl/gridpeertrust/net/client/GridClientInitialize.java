/*
 * Created on Jul 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.net.client;

import g4mfs.impl.gridpeertrust.util.LocalPeer;
import g4mfs.impl.gridpeertrust.util.SyncQueue;
import g4mfs.impl.gridpeertrust.wrappers.SendHelper;
import g4mfs.impl.org.peertrust.PTEngine;
import g4mfs.impl.org.peertrust.TrustClient;
import g4mfs.impl.org.peertrust.config.RunTimeOptions;
import g4mfs.impl.org.peertrust.event.PTEventDispatcher;
import g4mfs.impl.org.peertrust.inference.MinervaProlog;
import g4mfs.impl.org.peertrust.meta.MetaInterpreter;
import g4mfs.impl.org.peertrust.meta.MetaInterpreterListener;
import g4mfs.impl.org.peertrust.net.EntitiesTable;
import g4mfs.impl.org.peertrust.strategy.FIFOQueue;
import g4mfs.impl.org.peertrust.tnviz.app.TNVizListener;



/**
 * @author ionut
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GridClientInitialize 
{

	EntitiesTable entitiesTable;
	FIFOQueue fifoQueue;
	LocalPeer localPeer;
	GridClientFactory gridClientFactory;
	PTEventDispatcher ptEventDispatcher;
	RunTimeOptions runTimeOptions;
	MinervaProlog minervaProlog;
	MetaInterpreterListener metaInterpreterListener;
	MetaInterpreter metaInterpreter;
	TNVizListener tnvizListener;
	
	
	
	public void initialize1EntitiesTable(String filename)
	{
		entitiesTable = new EntitiesTable();
		entitiesTable.setEntitiesFile("/home/ionut/PeertrustFiles/demoClient/entities.dat1"); // ?????? set the file
		try
		{
			entitiesTable.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void initialize2fifoQueue()
	{
		fifoQueue = new FIFOQueue();
		try
		{
			fifoQueue.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void initialize3GridClientFactory(GridClientTrustNegotiation gctn, SyncQueue sq,SendHelper sendHelper)
	{
		gridClientFactory = new GridClientFactory();
		gridClientFactory.setGridClientTrustNegotiation(gctn);
		gridClientFactory.setSyncQueue(sq);
		gridClientFactory.setSendHelper(sendHelper);
		gridClientFactory.init();		
	}
	
	public void initialize4PTEventDisparcher()
	{
		ptEventDispatcher = new PTEventDispatcher();
		try
		{
			ptEventDispatcher.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void initialize5RuntimeOptions()
	{
		runTimeOptions = new RunTimeOptions();
		runTimeOptions.setRunningMode("demo");
		
		try
		{
			runTimeOptions.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void initialize6MinervaProlog(String baseFolder, String policyFile,String license, boolean debugMode)
	{
		minervaProlog = new MinervaProlog();
		minervaProlog.setBaseFolder("/home/ionut/PeertrustFiles/demoClient/"); //????????????????????????
		minervaProlog.setPrologFiles("demoPolicies.alice1"); // ??????????????????????
		minervaProlog.setLicense("minervagui.mca");
		minervaProlog.setDebugMode(true);
		try
		{
			minervaProlog.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public void initialize7MetaInterpreterListener()
	{
		metaInterpreterListener = new MetaInterpreterListener();
		metaInterpreterListener.setEntitiesTable(entitiesTable);
		metaInterpreterListener.setInferenceEngine(minervaProlog);
		metaInterpreterListener.setQueue(fifoQueue);
		metaInterpreterListener.setCommunicationChannelFactory(gridClientFactory);
		metaInterpreterListener.setEventDispatcher(ptEventDispatcher);
		metaInterpreterListener.setRunTimeOptions(runTimeOptions);
		
		try
		{
			metaInterpreterListener.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public void initialize8MetaInterpreter(String peerName)
	{
		metaInterpreter = new MetaInterpreter();
		metaInterpreter.setPeerName("alice");
		metaInterpreter.setEntitiesTable(entitiesTable);
		metaInterpreter.setInferenceEngine(minervaProlog);
		metaInterpreter.setQueue(fifoQueue);
		metaInterpreter.setLocalPeer(localPeer);
		metaInterpreter.setCommunicationChannelFactory(gridClientFactory);
		metaInterpreter.setEventDispatcher(ptEventDispatcher);
		metaInterpreter.setRunTimeOptions(runTimeOptions);
		try
		{
			metaInterpreter.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void initialize9TNVizListener()
	{
		tnvizListener = new TNVizListener();
		tnvizListener.setEventDispatcher(ptEventDispatcher);
		try
		{
			tnvizListener.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void initialize10PTEngine()
	{
		PTEngine ptEngine = new PTEngine();
		ptEngine.setMetaInterpreter(metaInterpreter);
		ptEngine.setMetaInterpreterListener(metaInterpreterListener);
		ptEngine.setEventDispatcher(ptEventDispatcher);
		ptEngine.setEventListener(tnvizListener);
		try
		{
			ptEngine.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	
	public void sendQuery(String query)
	{
		TrustClient tc = new TrustClient(ptEventDispatcher) ;
		
		System.out.println("\n\nClientCreate initializePTClient trimit mesaj\n\n");
		//tc.sendQuery("request(add,Session) @ elearn") ;
		tc.sendQuery("request(add,Session) @ hpclinuxcluster") ;
	}
	
	public void initialize0LocalPeer(String alias)
	{
		localPeer = new LocalPeer(alias);
	}

	public LocalPeer getLocalPeer()
	{
		return localPeer;
	}
	
}
