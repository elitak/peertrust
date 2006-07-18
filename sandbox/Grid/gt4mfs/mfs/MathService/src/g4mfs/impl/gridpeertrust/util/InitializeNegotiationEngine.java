/*
 * Created on Jul 17, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package g4mfs.impl.gridpeertrust.util;

import g4mfs.impl.gridpeertrust.net.server.GridFactory;
import g4mfs.impl.gridpeertrust.wrappers.SendHelper;
import g4mfs.impl.org.peertrust.PTEngine;
import g4mfs.impl.org.peertrust.TrustClient;
import g4mfs.impl.org.peertrust.config.RunTimeOptions;
import g4mfs.impl.org.peertrust.event.PTEventDispatcher;
import g4mfs.impl.org.peertrust.event.PTEventListener;
import g4mfs.impl.org.peertrust.event.SimplePeer;
import g4mfs.impl.org.peertrust.inference.MinervaProlog;
import g4mfs.impl.org.peertrust.meta.MetaInterpreter;
import g4mfs.impl.org.peertrust.meta.MetaInterpreterListener;
import g4mfs.impl.org.peertrust.net.EntitiesTable;
import g4mfs.impl.org.peertrust.strategy.FIFOQueue;
import g4mfs.impl.org.peertrust.tnviz.app.TNVizListener;

/**
 * @author ionut constandache ionut_con@yahoo.com
 * Class used for initializing the reasoning engine
 */
public class InitializeNegotiationEngine 
{
	
	EntitiesTable entitiesTable;
	FIFOQueue fifoQueue;
	LocalPeer localPeer;
	GridFactory gridFactory;
	PTEventDispatcher ptEventDispatcher;
	RunTimeOptions runTimeOptions;
	MinervaProlog minervaProlog;
	MetaInterpreterListener metaInterpreterListener;
	MetaInterpreter metaInterpreter;
	TNVizListener tnvizListener;
	SimplePeer simplePeer;
	PTEngine ptEngine;
	
	
	public InitializeNegotiationEngine(String localPeerAlias, String entitiesFilename,SyncQueue gridFactoryQueue, SendHelper gridFactorySendHelper, 
			String runtimeOptionRunningMode, String minervaPrologBaseFolder, String minervaPrologPolicyFile,String minervaPrologLicense, 
			boolean minervaPrologDebugMode,boolean ptEngineVizListener)
	{
		
		initialize0LocalPeer(localPeerAlias);
		initialize1EntitiesTable(entitiesFilename);
		initialize2fifoQueue();
		initialize3GridFactory(gridFactoryQueue,gridFactorySendHelper);
		initialize4PTEventDisparcher();
		initialize5RuntimeOptions(runtimeOptionRunningMode);
		initialize6MinervaProlog(minervaPrologBaseFolder,minervaPrologPolicyFile,minervaPrologLicense,minervaPrologDebugMode);
		initialize7MetaInterpreterListener();
		initialize8MetaInterpreter(localPeerAlias);
		
		if(ptEngineVizListener)
			initialize9TNVizListener();
		else
			initialize9SimplePeer();
		initialize10PTEngine(ptEngineVizListener);
	}
	
	
	public void initialize0LocalPeer(String alias)
	{
		localPeer = new LocalPeer(alias);
	}

	public LocalPeer getLocalPeer()
	{
		return localPeer;
	}
		
	public void initialize1EntitiesTable(String filename)
	{
		entitiesTable = new EntitiesTable();
		entitiesTable.setEntitiesFile(filename); 
		try
		{
			entitiesTable.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public EntitiesTable getEntitiesTable()
	{
		return entitiesTable;
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
	
	public FIFOQueue getFifoQueue()
	{
		return fifoQueue;
	}
	
	
	public void initialize3GridFactory(SyncQueue sq,SendHelper sendHelper)
	{
		gridFactory = new GridFactory();
		gridFactory.setSyncQueue(sq);
		gridFactory.setSendHelper(sendHelper);
		gridFactory.init();		
	}
	
	public GridFactory getGridFactory()
	{
		return gridFactory;
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
	
	public PTEventDispatcher getPTEventDispatcher()
	{
		return ptEventDispatcher;
	}
	
	public void initialize5RuntimeOptions(String runningMode)
	{
		runTimeOptions = new RunTimeOptions();
		runTimeOptions.setRunningMode(runningMode);
		
		try
		{
			runTimeOptions.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public RunTimeOptions getRuntimeOptions()
	{
		return runTimeOptions;
	}
	
	
	public void initialize6MinervaProlog(String baseFolder, String policyFile,String license, boolean debugMode)
	{
		minervaProlog = new MinervaProlog();
		minervaProlog.setBaseFolder(baseFolder); 
		minervaProlog.setPrologFiles(policyFile); 
		minervaProlog.setLicense(license);
		minervaProlog.setDebugMode(debugMode);
		try
		{
			minervaProlog.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public MinervaProlog getMinervaProlog()
	{
		return minervaProlog;	
	}
	
	public void initialize7MetaInterpreterListener()
	{
		metaInterpreterListener = new MetaInterpreterListener();
		metaInterpreterListener.setEntitiesTable(entitiesTable);
		metaInterpreterListener.setInferenceEngine(minervaProlog);
		metaInterpreterListener.setQueue(fifoQueue);
		metaInterpreterListener.setCommunicationChannelFactory(gridFactory);
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
	
	public MetaInterpreterListener getMetaInterpreterListener()
	{
		return metaInterpreterListener;
	}
	
	public void initialize8MetaInterpreter(String peerName)
	{
		metaInterpreter = new MetaInterpreter();
		metaInterpreter.setPeerName(peerName);
		metaInterpreter.setEntitiesTable(entitiesTable);
		metaInterpreter.setInferenceEngine(minervaProlog);
		metaInterpreter.setQueue(fifoQueue);
		metaInterpreter.setLocalPeer(localPeer);
		metaInterpreter.setCommunicationChannelFactory(gridFactory);
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
	
	public MetaInterpreter getMetaInterpreter()
	{
		return metaInterpreter;
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
		
	public TNVizListener getTNVizListener()
	{
		return tnvizListener;
	}
	
	public void initialize9SimplePeer()
	{
		simplePeer = new SimplePeer();
		SimplePeer simplePeer = new SimplePeer();
		simplePeer.setEventDispatcher(ptEventDispatcher);
		try
		{
			simplePeer.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public SimplePeer getSimplePeer()
	{
		return simplePeer;
	}
	
	public void initialize10PTEngine(boolean viz)
	{
		ptEngine = new PTEngine();
		ptEngine.setMetaInterpreter(metaInterpreter);
		ptEngine.setMetaInterpreterListener(metaInterpreterListener);
		ptEngine.setEventDispatcher(ptEventDispatcher);
		
		if(viz)
			ptEngine.setEventListener(tnvizListener);
		else
			ptEngine.setEventListener(simplePeer);
		
		try
		{
			ptEngine.init();
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	public PTEngine getPTEngine()
	{
		return ptEngine;
	}
	
	public void sendQuery(String query)
	{
		TrustClient tc = new TrustClient(ptEventDispatcher) ;
		tc.sendQuery(query);
		
		//tc.sendQuery("request(add,Session) @ hpclinuxcluster") ;
	}	
	

}
