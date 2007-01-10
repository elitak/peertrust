package test.org.protune;

import java.io.IOException;
import org.protune.net.DispatcherPeer;

/**
 * 
 * @author cjin
 */
public class ServerTest{

	public static void main(String[] args){
		
		Runnable runnable = new Runnable(){
			public void run(){
				// Local variables initialisation.
				System.out.println("new Thread...");
				String[] sa = {"org.protune.net.DummyService"};
				DispatcherPeer dp;
				
				try{
					dp = new DispatcherPeer("localhost", 1234, sa);
					dp.init();
				}
				catch(IOException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				catch(ClassNotFoundException e){
					// TODO Auto-generated catch block
					e.printStackTrace();
				}			
			}
		};
		
		Thread thread = new Thread(runnable);
		thread.setDaemon(true);
		thread.start();
		
	}

}
