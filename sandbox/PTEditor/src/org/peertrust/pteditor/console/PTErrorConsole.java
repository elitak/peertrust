package org.peertrust.pteditor.console;

import java.util.HashMap;
import java.util.Iterator;

import org.eclipse.jface.text.BadLocationException;

import org.eclipse.ui.console.IHyperlink;
import org.eclipse.ui.console.IOConsole;
import org.peertrust.pteditor.model.MyResource;

public class PTErrorConsole extends IOConsole {
	public PTErrorConsole() {
		super("Policy Errors",null);
	}
	
	public void showErrors(HashMap<String,MyResource> hashmap) {
		StringBuffer strbuf=new StringBuffer();
		Iterator<String> iter=hashmap.keySet().iterator();
		int size=hashmap.size();
		int nOffsets[]=new int[size];
		int nLengths[]=new int[size];
		IHyperlink hyperlinks[]=new IHyperlink[size];
		int i=0;
		while(iter.hasNext()) {
			String strErrorText=iter.next();
			nOffsets[i]=strbuf.length();
			nLengths[i]=strErrorText.length();
			hyperlinks[i]=new MyHyperlink(hashmap.get(strErrorText));
			strbuf.append(strErrorText+"\n");
			i++;
		}
		getDocument().set(strbuf.toString());
		for(i=0;i<size;i++) {
			try {
				addHyperlink(hyperlinks[i],nOffsets[i],nLengths[i]);
			} catch (BadLocationException e) {
			}
		}
	}
	
	class MyHyperlink implements IHyperlink {
		private MyResource res;
		
		public MyHyperlink(MyResource res) {
			this.res=res;
		}

		public void linkActivated() {
		}

		public void linkEntered() {
		}

		public void linkExited() {
		}
	}
}
