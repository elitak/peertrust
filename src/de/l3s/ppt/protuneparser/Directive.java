package de.l3s.ppt.protuneparser;

public class Directive{
	private StringDescription quotedText;
	private StringDescription commandWord;
	public int offsetInInput = -1;
	
	public Directive(StringDescription commandWord, StringDescription quotedText) {
		this.commandWord = commandWord;
		this.quotedText = quotedText;
		this.offsetInInput = quotedText.getEndOffset();
	}

	public StringDescription getQuotedText() {
		return quotedText;
	}
	public String getImage() {
		return Constants.INCLUDE + quotedText.getStr();
	}

	public int getOffsetInInput() {
		return offsetInInput;
	}

	public StringDescription getCommandWord() {
		return commandWord;
	}
}
