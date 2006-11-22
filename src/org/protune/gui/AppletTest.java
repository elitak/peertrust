package org.protune.gui;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;

public class AppletTest extends Applet {
	
	private static final long serialVersionUID = 909678460071555083L;
	Image snapshot;
	String from, to; 

	public void init() {
		from =
			null == getParameter("from") ? "Deutschland" : getParameter("from");
		to =
			null == getParameter("to") ? "England" : getParameter("to");
		
		snapshot = getImage(getCodeBase(), "./katz.jpg");
		System.out.println(getCodeBase());
		MediaTracker tracker = new MediaTracker(this);
		tracker.addImage(snapshot, 0);

		try {
			tracker.waitForID(0);
		}
		catch (Exception e) {
			System.out.println("can not download snapshot.jpg!");
		}
	}

	public void paint(Graphics g) {
		g.drawImage(snapshot, 0, 0, snapshot.getWidth(this), snapshot.getHeight(this), this);
		g.setFont(new Font("XXX", Font.BOLD + Font.ITALIC, 14));
		g.setColor(Color.red);
			
		g.drawString(from, 60, 90);
		g.drawString("spielt mit\n", 100, 105);
		g.drawString(to, 140, 120);
	}
}