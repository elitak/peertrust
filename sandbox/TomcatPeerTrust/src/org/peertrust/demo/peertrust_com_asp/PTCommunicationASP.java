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

import org.peertrust.net.AbstractFactory;
import org.peertrust.net.NetClient;
import org.peertrust.net.Peer;

/**
 * PTCommunicationASP is a peertrust event listener.
 * It provide mechnisms to send and receive arbritary serializable 
 * messages.  
 * 
 * @author Patrice Congo (token77)
 *
 */
public class PTCommunicationASP 
					implements 	PTEventListener,
								Configurable 
{
	
	/** 
	 * the peer trust event dispatcher
	 */
	private EventDispatcher _dispatcher ;
	
//	/**
//	 * The communication channel factory which net client is used 
//	 * to send message.
//	 */
//	private AbstractFactory _CommunicationChannelFactory;
	
	/** the net client of the communication channel*/
	private NetClient netClient;
	
	/** contains the listeners with the class of message 
	 * they want to listen for as key
	 */
	private Hashtable listenerPool= new Hashtable();
	
	/**
	 * Received PTEvents, gets PTCommunicationASPObject message and
	 * notifies listeners.
	 *  
	 * @see org.peertrust.event.PTEventListener#event(org.peertrust.event.PTEvent)
	 */
	public void event(PTEvent ptEvent) 
	{
		System.out.println("*====>ptEvent:"+ptEvent);
		if(ptEvent instanceof NewMessageEvent){
			Object mes=((NewMessageEvent)ptEvent).getMessage();
			if(mes instanceof PTCommunicationASPObject){
				Serializable payload=((PTCommunicationASPObject)mes).getPeggyBackedMessage();
				Peer source=((PTCommunicationASPObject)mes).getSource();
				Peer target=((PTCommunicationASPObject)mes).getTarget();
//				System.out.println("\n**********************Payload message received********************");
//				System.out.println("message: "+mes);
//				System.out.println("\n**************************************************");
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

	/**
	 * Registers a PTComASPMessageListener to receive mesage of a specified
	 * type.
	 * @param listener -- the registering listener
	 * @param objTypeToListenFor -- the class of the objects to listen to.
	 */
	public void registerPTComASPMessageListener(
						PTComASPMessageListener listener, 
						Class objTypeToListenFor)
	{
		registerListener(listener,objTypeToListenFor);
	}
	/**
	 * Unregisters a PTComASPMessageListener as listener of the specified
	 * message type.
	 * @param listener -- the unregistering listener
	 * @param objTypeToListenFor -- the class of the objects to stop listen to.
	 */
	public void unregisterPTComASPMessageListener(
							PTComASPMessageListener listener, 
							Class objTypeToListenFor)
	{
		unregisterListener(listener,objTypeToListenFor);
	}
	
	/**
	 * Registers a listener to listen to a specified class of 
	 * objects.
	 * @param listener -- the listener to register
	 * @param 	objTypeToListenTo -- the class of object the listener wants
	 * 			to listen to.
	 */
	private void registerListener(
						Object listener, 
						Class objTypeToListenTo)
	{
		if(listener==null){
			throw new NullPointerException("Parameter listener must not be null");
		}
		
		if(objTypeToListenTo==null){
			throw new NullPointerException("Parameter objTypeToListenFor must not be null");
		}
		
		Vector listenerVect=(Vector)listenerPool.get(objTypeToListenTo);
		if(listenerVect==null){
			listenerVect= new Vector(8);
			listenerPool.put(objTypeToListenTo,listenerVect);
		}
		listenerVect.add(listener);
//		System.out.println("****************RegisterListerner for PTCommunicationASP**********************");
//		System.out.println("Listener:"+listener.getClass());
//		System.out.println("ListenTo:"+objTypeToListenTo);
//		System.out.println("****************RegisterListerner for PTCommunicationASP END**********************");
	}
	
	/**
	 * Unregisters an listener for a specific kind of events
	 * @param 	listener -- the unregistering listener
	 * @param 	objTypeToListenTo -- the class of objects the listener doe
	 * 			not wants to receive anymore
	 */
	private void unregisterListener(
							Object listener, 
							Class objTypeToListenTo)
	{
		if(listener==null){
			throw new NullPointerException("Parameter listener must not be null");
		}
		
		if(objTypeToListenTo==null){
			throw new NullPointerException("Parameter objTypeToListenFor must not be null");
		}
		
		Vector listenerVect=(Vector)listenerPool.get(objTypeToListenTo);
		if(listenerVect==null){
			return;
		}else{
			listenerVect.remove(listener);
			if(listenerVect.size()==0){
				listenerPool.remove(objTypeToListenTo);
			}
		}
	}
	
	/**
	 * Sends a serializable to a peer.
	 * 
	 * @param toSend -- the serializable to send
	 * @param source -- the sender
	 * @param destination -- the destination
	 */
	public void send(	
				Serializable toSend, 
				Peer source, 
				Peer destination)
	{
		
		PTCommunicationASPObject mes=
			new PTCommunicationASPObject(source,destination,toSend);
		netClient.send(mes,destination);
	}
	
	
	/**
	 * Sets the peertrust event dispatcher.
	 * 
	 * @param _dispatcher -- The new _dispatcher to set.
	 */
	public void setEventDispatcher(EventDispatcher _dispatcher) 
	{
		this._dispatcher = _dispatcher;
	}
	
	
	/**
	 * This methods actualy sets the communication channel.
	 * But because is isnot used directely only the reference
	 * to it net client is cached.
	 * 
	 * @param _CommunicationChannelFactory
	 */
	public void setCommunicationChannelFactory(
							AbstractFactory _CommunicationChannelFactory)
	{
		System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!seintin net client in asp");
		netClient=_CommunicationChannelFactory.createNetClient();
		//this._CommunicationChannelFactory=_CommunicationChannelFactory;
	}
	
	/**
	 * This method inits the PTCommunicationASP. Its basicaly
	 * registers  the PTCommunicationASP at the peertrust 
	 * event dipatcher.
	 * @see org.peertrust.config.Configurable#init()
	 */
	public void init() throws ConfigurationException 
	{
		System.out.println("****************init for PTCommunicationASP**********************");
		
		if (_dispatcher == null)
		{
			String msg = "The event dispatcher is not set for "+PTCommunicationASP.class ;
			throw new ConfigurationException(msg) ;
		}
		
		_dispatcher.register(this,NewMessageEvent.class) ;
		
	}

}
