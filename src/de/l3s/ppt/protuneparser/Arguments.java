package de.l3s.ppt.protuneparser;

import java.util.ArrayList;

public class Arguments {
	private boolean exists = false;
	private ArrayList list = null;
	
	public Arguments() {
	}
	
	public Arguments(ArrayList list) {
		exists = true;
		if (list == null) {
			this.list = new ArrayList();
		} else {
			this.list = list;
		}
	}
	public String getImage() {
		StringBuffer buff = new StringBuffer();
		if (exists) {
			buff.append(Constants.OPENING_BRACKET);
			for (int i = 0; i < list.size(); i++) {
				Argument argument = (Argument)list.get(i);
				buff.append(argument.getImage());
				if (i != list.size() - 1) {
					buff.append(Constants.COMMA);
				}
			}
			buff.append(Constants.CLOSING_BRACKET);
		}
		return buff.toString();
	}

	public boolean isExists() {
		return exists;
	}

	public ArrayList getList() {
		return list;
	}
}
