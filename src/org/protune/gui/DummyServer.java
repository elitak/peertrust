package org.protune.gui;

import org.protune.net.DispatcherPeer;

/**
 * 
 * @author cjin, swittler
 */
public class DummyServer  {
	
	private ClientGui clientgui;
	
	public DummyServer(ClientGui gui) {
		clientgui = gui;
	}
	
	public void startServer(final String strAdresse,final int nPort,final String[] strService) {
		Runnable runnable = new Runnable() {
			
			public void run() {
				// Local variables initialisation.
				System.out.println("new Thread...");
				System.out.println("server run...");
				
				String[] sa = strService;
				DispatcherPeer dp;
				
				try {
					dp = new DispatcherPeer(strAdresse, nPort, sa);
					dp.init();
				}
				catch (Exception e) {
					clientgui.showException(e);
				}
			}
			
		};
		
		Thread thread=new Thread(runnable);
		thread.setDaemon(true);
		thread.start();
	}
}
