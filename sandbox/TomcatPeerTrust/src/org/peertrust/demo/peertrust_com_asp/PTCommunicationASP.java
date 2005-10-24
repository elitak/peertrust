package org.peertrust.demo.peertrust_com_asp;

import java.io.Serializable;
import java.util.Hashtable;
import java.util.Vector;

import org.peertrust.config.Configurable;
import org.peertrust.event.AnswerEvent;
import org.peertrust.event.EventDispatcher;
import org.peertrust.event.NewMessageEvent;
import org.peertrust.event.PTEvent;
import org.peertrust.event.PTEventListener;
import org.peertrust.event.QueryEvent;
import org.peertrust.exception.ConfigurationException;

import org.peertrust.net.NetClient;
import org.peertrust.net.Peer;

public class PTCommunicationASP implements PTEventListener,Configurable {
	
	//private BlockingQueue sendQueue;
	private EventDispatcher _dispatcher ;
	private NetClient netClient;
	private Peer source;
	private Hashtable listenerPool= new Hashtable();
	
	public void event(PTEvent ptEvent) {
		System.out.println("*====>ptEvent:"+ptEvent);
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
				System.out.println("\n===================>UnknownNewEvent:"+ptEvent);
				//System.out.println("\n=========>QueryEvent:\n"+ptEvent);
				Vector listeners= 
					(Vector)listenerPool.get(ptEvent.getClass());//QueryEvent.class);
				System.out.println("Listeners:"+listeners);
				if(listeners!=null){
					for(int i=listeners.size()-1;i>=0;i--){
						((PTEventListener)listeners.elementAt(i)).event(ptEvent);
					}
				}
				return;
				
			}
		}else if(ptEvent instanceof QueryEvent){
			System.out.println("\n=========>QueryEvent:\n"+ptEvent);
			Vector listeners= 
				(Vector)listenerPool.get(QueryEvent.class);
			if(listeners!=null){
				for(int i=listeners.size()-1;i>=0;i--){
					((PTEventListener)listeners.elementAt(i)).event(ptEvent);
				}
			}
			return;
		}else if(ptEvent instanceof AnswerEvent){
			System.out.println("\n=======>AnwerEvent:\n"+ptEvent);
			Vector listeners= 
				(Vector)listenerPool.get(AnswerEvent.class);
			if(listeners!=null){
				for(int i=listeners.size()-1;i>=0;i--){
					((PTEventListener)listeners.elementAt(i)).event(ptEvent);
				}
			}
			return;
		}else{
			System.out.println("\n========>Unknown Event:"+ptEvent);
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
		unregisterListener(eventListener,AnswerEvent.class);
	}
	
	public void registerPTEventListener(PTEventListener eventListener){
		registerListener(eventListener,QueryEvent.class);
		registerListener(eventListener,AnswerEvent.class);
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
	
	
	/**
	 * @param _dispatcher The _dispatcher to set.
	 */
	public void setEventDispatcher(EventDispatcher _dispatcher) {
		this._dispatcher = _dispatcher;
	}
	
	public void init() throws ConfigurationException {
		System.out.println("****************init for PTCommunicationASP**********************");
		String msg = null ;
		if (_dispatcher == null)
		{
			msg = "There not exist an event dispatcher" ;
			throw new Error(msg) ;
		}
		
		_dispatcher.register(this,NewMessageEvent.class) ;
		
	}

}
