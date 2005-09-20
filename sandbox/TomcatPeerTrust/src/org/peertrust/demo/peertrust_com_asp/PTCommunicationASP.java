package org.peertrust.demo.peertrust_com_asp;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import org.peertrust.config.Configurable;
import org.peertrust.event.NewMessageEvent;
import org.peertrust.event.PTEvent;
import org.peertrust.event.PTEventListener;
import org.peertrust.event.QueryEvent;
import org.peertrust.exception.ConfigurationException;

import org.peertrust.net.NetClient;
import org.peertrust.net.Peer;

public class PTCommunicationASP implements PTEventListener,Configurable {
	
	//private BlockingQueue sendQueue;
	private NetClient netClient;
	private Peer source;
	private Hashtable listenerPool= new Hashtable();
	
	public void event(PTEvent ptEvent) {
		System.out.println("====>ptEvent:"+ptEvent);
		if(ptEvent instanceof NewMessageEvent){
			Object mes=((NewMessageEvent)ptEvent).getMessage();
			if(mes instanceof PTCommunicationASPObject){
				Serializable payload=((PTCommunicationASPObject)mes).getPeggyBackedMessage();
				Peer source=((PTCommunicationASPObject)mes).getSource();
				Peer target=((PTCommunicationASPObject)mes).getTarget();
				System.out.println("\n**********************Payload message received********************");
				System.out.println("message: "+mes);
				System.out.println("\n**************************************************");
				if(mes!=null){
					Vector listeners= 
						(Vector)listenerPool.get(payload.getClass());
					if(listeners==null){
						return;
					}else{
						for(int i=listeners.size()-1;i>=0;i--){
							((PTComASPMessageListener)listeners.elementAt(i)).PTMessageReceived(payload,source,target);
						}
					}
				}
			}else{
				return;
			}
		}else if(ptEvent instanceof QueryEvent){
			Vector listeners= 
				(Vector)listenerPool.get(QueryEvent.class);
			if(listeners!=null){
				for(int i=listeners.size()-1;i>=0;i--){
					((PTEventListener)listeners.elementAt(i)).event(ptEvent);
				}
			}
			return;
		}
	}

	public void registerPTComASPMessageListener(PTComASPMessageListener listener, Class objTypeToListenFor){
		registerListener(listener,objTypeToListenFor);
	}
	
	public void unregisterPTComASPMessageListener(PTComASPMessageListener listener, Class objTypeToListenFor){
		unregisterListener(listener,objTypeToListenFor);
	}
	
	public void unregisterPTEventListener(PTEventListener eventListener){
		unregisterListener(eventListener,QueryEvent.class);
	}
	
	public void registerPTEventListener(PTEventListener eventListener){
		registerListener(eventListener,QueryEvent.class);
	}
	
	private void registerListener(Object listener, Class objTypeToListenFor){
		if(listener==null){
			throw new NullPointerException("Parameter listener must not be null");
		}
		
		if(objTypeToListenFor==null){
			throw new NullPointerException("Parameter objTypeToListenFor must not be null");
		}
		
		Vector listenerVect=(Vector)listenerPool.get(objTypeToListenFor);
		if(listenerVect==null){
			listenerVect= new Vector(8);
			listenerPool.put(objTypeToListenFor,listenerVect);
		}
		listenerVect.add(listener);
		System.out.println("****************RegisterListerner for PTCommunicationASP**********************");
		System.out.println("Listener:"+listener.getClass());
		System.out.println("ListenTo:"+objTypeToListenFor);
		System.out.println("****************RegisterListerner for PTCommunicationASP END**********************");
	}
	
	private void unregisterListener(Object listener, Class objTypeToListenFor){
		if(listener==null){
			throw new NullPointerException("Parameter listener must not be null");
		}
		
		if(objTypeToListenFor==null){
			throw new NullPointerException("Parameter objTypeToListenFor must not be null");
		}
		
		Vector listenerVect=(Vector)listenerPool.get(objTypeToListenFor);
		if(listenerVect==null){
			return;
		}else{
			listenerVect.remove(listener);
			if(listenerVect.size()==0){
				listenerPool.remove(objTypeToListenFor);
			}
		}
	}
	
	public void send(Serializable toSend, Peer destination){
		PTCommunicationASPObject mes=
			new PTCommunicationASPObject(source,destination,toSend);
		netClient.send(mes,destination);
	}
	
	
	public void init() throws ConfigurationException {
		System.out.println("****************init for PTCommunicationASP**********************");
	}

}
