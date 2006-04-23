package org.peertrust.inference.prolog.yprolog;

public class QStack {

	int ptr=0;
	Object [] obj = new Object[10240];

	public QStack () {}

	public Object pop() {
		if (ptr==0) return null;
		return obj[--ptr];
	}

	public void push(Object o) {
		obj[ptr++] = o;
	}

	public boolean empty() { return ptr==0; }

	public int size() { return ptr; }

	public void clear() { ptr=0; }

}
