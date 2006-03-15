package de.l3s.ppt.log;

import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.console.ConsolePlugin;
import org.eclipse.ui.console.IConsole;
import org.eclipse.ui.console.MessageConsole;
import org.eclipse.ui.console.MessageConsoleStream;

public class Logger {
	private static MessageConsole console = null;
	private static MessageConsoleStream debugConsoleStream = null;
	private static MessageConsoleStream infoConsoleStream = null;
	private static MessageConsoleStream warningConsoleStream = null;
	private static MessageConsoleStream errorConsoleStream = null;
	private static MessageConsoleStream fatalConsoleStream = null;
	private Class className;
	// todo: read these variables from settings (from plugin.xml?)
	private static boolean debugLevel = false;
	private static boolean infoLevel = false;
	private static boolean warningLevel = true;
	private static boolean errorLevel = true;
	private static boolean fatalLevel = true;
	public Logger(Class className) {
		this.className = className;
	}
	public void debug(String str) {
		if (debugLevel) {
			getDebugConsoleStream().println(className.getName() + " : debug : " + str);
		}
	}
	public void info(String str) {
		if (infoLevel) {
			getInfoConsoleStream().println(className.getName() + " : info : " + str);
		}
	}
	public void warning(String str) {
		if (warningLevel) {
			getWarningConsoleStream().println(className.getName() + " : warning : " + str);
		}
	}
	public void error(String str) {
		if (errorLevel) {
			getErrorConsoleStream().println(className.getName() + " : error : " + str);
		}
	}
	public void fatal(String str) {
		if (fatalLevel) {
			getFatalConsoleStream().println(className.getName() + " : fatal : " + str);
		}
	}
	private MessageConsole getConsole() {
		if( console == null ) {
			createConsole("Protune / PeerTrust Console");
		}
		return console;
	}
	private MessageConsoleStream getDebugConsoleStream() {
		if( debugConsoleStream == null ) {
			debugConsoleStream = getConsole().newMessageStream();
			debugConsoleStream.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_GRAY));
		}
		return debugConsoleStream;
	}
	private MessageConsoleStream getInfoConsoleStream() {
		if( infoConsoleStream == null ) {
			infoConsoleStream = getConsole().newMessageStream();
			infoConsoleStream.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLACK));
		}
		return infoConsoleStream;
	}
	private MessageConsoleStream getWarningConsoleStream() {
		if( warningConsoleStream == null ) {
			warningConsoleStream = getConsole().newMessageStream();
			warningConsoleStream.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_BLUE));
		}
		return warningConsoleStream;
	}
	private MessageConsoleStream getErrorConsoleStream() {
		if( errorConsoleStream == null ) {
			errorConsoleStream = getConsole().newMessageStream();
			errorConsoleStream.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		}
		return errorConsoleStream;
	}
	private MessageConsoleStream getFatalConsoleStream() {
		if( fatalConsoleStream == null ) {
			fatalConsoleStream = getConsole().newMessageStream();
			fatalConsoleStream.setColor(Display.getCurrent().getSystemColor(SWT.COLOR_RED));
		}
		return fatalConsoleStream;
	}
	private void createConsole(String title) {
		console = new MessageConsole(title, null);
		ConsolePlugin.getDefault().getConsoleManager().addConsoles(new IConsole[]{ console });
	}	
}