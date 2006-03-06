package de.l3s.ppt.rcp.peertrust;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;

import de.l3s.ppt.log.Logger;
import de.l3s.ppt.peertrustparser.*;
import de.l3s.ppt.peertrustparser.javacc.PeerTrustParser;

import java.util.ArrayList;

public class PeerTrustRepairer implements IPresentationDamager, IPresentationRepairer {
	/** The document this object works on */
	protected IDocument iDocument;
	private ArrayList parsedEntries;
	private static Logger logger = new Logger(PeerTrustRepairer.class);
	private TextAttribute defaultTA;
	private TextAttribute constantTA;
	private TextAttribute variableTA;
	private TextAttribute issuerTA;
	private TextAttribute requesterTA;
	private TextAttribute signedByTA;
	private TextAttribute reservedWordTA;
	private TextAttribute commentTA;
	private TextAttribute errorTA;
	public PeerTrustRepairer(PeerTrustColorManager colorManager) {
		defaultTA = colorManager.getTextAttribute(PeerTrustColorManager.DEFAULT_COLOR);
		constantTA = colorManager.getTextAttribute(PeerTrustColorManager.CONSTANT_COLOR);
		variableTA = colorManager.getTextAttribute(PeerTrustColorManager.VARIABLE_COLOR);
		issuerTA = colorManager.getTextAttribute(PeerTrustColorManager.ISSUER_COLOR);
		requesterTA = colorManager.getTextAttribute(PeerTrustColorManager.REQUESTER_COLOR);
		signedByTA = colorManager.getTextAttribute(PeerTrustColorManager.SIGNED_BY_COLOR);
		reservedWordTA = colorManager.getTextAttribute(PeerTrustColorManager.RESERVED_WORD_COLOR);
		commentTA = colorManager.getTextAttribute(PeerTrustColorManager.COMMENT_COLOR);
		errorTA = colorManager.getTextAttribute(PeerTrustColorManager.ERROR_COLOR);
	}
	/**
	 * @see IPresentationRepairer#setDocument(IDocument)
	 */
	public void setDocument(IDocument document) {
		iDocument = document;
	}
	/**
	 * @see IPresentationRepairer#createPresentation(TextPresentation, ITypedRegion)
	 */
	public void createPresentation(TextPresentation presentation, ITypedRegion region) {
		logger.info("createPresentation() : ENTER");
		String toParse = iDocument.get().substring(region.getOffset(), region.getOffset() + region.getLength());
		logger.debug("createPresentation() : toParse : " + toParse);
		parsedEntries = new ArrayList();
		try {
			PeerTrustParser parser = PeerTrustParser.createParser(toParse, 0);
			parsedEntries = parser.parse();
		} catch (Exception e) {
			logger.error("createPresentation() : exception thrown by parser : " + e.toString());
		}
		logger.info("createPresentation() : entries.size() : " + parsedEntries.size());
		for (int i = 0; i < parsedEntries.size(); i++) {
			Object obj = parsedEntries.get(i);
			if (obj instanceof Integer) {
				logger.info("createPresentation() : Integer entry, offset : " + ((Integer)obj).intValue());
			} else {
				logger.info("createPresentation() : Rule entry, offset : " + ((Rule)obj).offsetInInput);
			} 
		}
		ArrayList styleRanges = new ArrayList();
		int previousOffset = -1;
		int totalLength = toParse.length();
		int rangeOffset;
		int offset;
		int length;
		// adding default style range
		addRange(styleRanges, 0, totalLength, defaultTA, totalLength);
		for (int i = 0; i < parsedEntries.size(); i++) {
			rangeOffset = previousOffset + 1;
			Object obj = parsedEntries.get(i);
			if (obj instanceof Integer) {
				offset = ((Integer)obj).intValue();
				length = offset - previousOffset;
				addRange(styleRanges, rangeOffset, length, errorTA, totalLength);
			} else {
				Rule rule = (Rule)obj;
				offset = rule.offsetInInput;
				decorateRule(styleRanges, rule, totalLength);
			}
			previousOffset = offset;
		}
		StyleRange[] temp = new StyleRange[styleRanges.size()];
		for (int i = 0; i < styleRanges.size(); i++) {
			temp[i] = (StyleRange)styleRanges.get(i);
		}
		logger.info("createPresentation() : number of style ranges : " + temp.length);
		try {
			presentation.replaceStyleRanges(temp);
		} catch (Exception e) {
			logger.error("createPresentation() : exception thrown when replacing style ranges : " + e.toString());
		}
		logger.info("createPresentation() : EXIT");
	}
	private void decorateRule(ArrayList styleRanges, Rule rule, int totalLength) {
		decorateExtendedLiteral(styleRanges, rule.getHead(), totalLength);
		RuleBody ruleBody = rule.getBody();
		if (ruleBody != null) {
			decorateGuards(styleRanges, ruleBody.getPolicy(), totalLength);
			StringDescription commandWord = ruleBody.getCommandWord();
			if (commandWord != null) {
				addRange(styleRanges, commandWord.getBeginOffset(), commandWord.getStr().length(), reservedWordTA, totalLength);
			}
			StringDescription signedBy = ruleBody.getSignedBy();
			if (signedBy != null) {
				addRange(styleRanges, signedBy.getBeginOffset(), signedBy.getStr().length(), signedByTA, totalLength);
			}
			decorateGuards(styleRanges, ruleBody.getBody(), totalLength);
		}
	}
	private void decorateGuards(ArrayList styleRanges, Guards guards, int totalLength) {
		if (guards != null) {
			ArrayList beforeSlash = guards.getBeforeSlash();
			ArrayList afterSlash = guards.getAfterSlash();
			if (beforeSlash != null) {
				for (int i = 0; i < beforeSlash.size(); i++) {
					ExtendedLiteral extendedLiteral = (ExtendedLiteral)beforeSlash.get(i);
					decorateExtendedLiteral(styleRanges, extendedLiteral, totalLength);
				}
			}
			if (afterSlash != null) {
				for (int i = 0; i < afterSlash.size(); i++) {
					ExtendedLiteral extendedLiteral = (ExtendedLiteral)afterSlash.get(i);
					decorateExtendedLiteral(styleRanges, extendedLiteral, totalLength);
				}
			}
		}
	}
	private void decorateArgument(ArrayList styleRanges, Object argument, int totalLength) {
		if (argument != null) {
			if (argument instanceof StringDescription) {
				StringDescription stringDescription = (StringDescription) argument;
				if (stringDescription.isConstant()) {
					addRange(styleRanges, stringDescription.getBeginOffset(), stringDescription.getStr().length(), constantTA, totalLength);
				} else {
					addRange(styleRanges, stringDescription.getBeginOffset(), stringDescription.getStr().length(), variableTA, totalLength);
				}
			} else {
				ArrayList list = (ArrayList)argument;
				for (int i = 0; i < list.size(); i++) {
					decorateArgument(styleRanges, list.get(i), totalLength);
				}
			}
		}
	}
	private void decorateExtendedLiteral(ArrayList styleRanges, ExtendedLiteral extendedLiteral, int totalLength) {
		ArrayList arguments = extendedLiteral.getLiteral().getArguments();
		decorateArgument(styleRanges, arguments, totalLength);
		decorateIssuer(styleRanges, extendedLiteral.getIssuer(), totalLength);
		decorateRequester(styleRanges, extendedLiteral.getRequester(), totalLength);
	}
	private void decorateIssuer(ArrayList styleRanges, Issuer issuer, int totalLength) {
		if (issuer != null) {
			if (issuer.getAdditionalIssuer() != null) {
				decorateIssuer(styleRanges, issuer.getAdditionalIssuer(), totalLength);
			}
			StringDescription stringDescription = issuer.getIssuer();
			addRange(styleRanges, stringDescription.getBeginOffset(), stringDescription.getStr().length(), issuerTA, totalLength);
		}
	}
	private void decorateRequester(ArrayList styleRanges, Requester requester, int totalLength) {
		if (requester != null) {
			if (requester.getAdditionalRequester() != null) {
				decorateRequester(styleRanges, requester.getAdditionalRequester(), totalLength);
			}
			StringDescription stringDescription = requester.getRequester();
			addRange(styleRanges, stringDescription.getBeginOffset(), stringDescription.getStr().length(), requesterTA, totalLength);
		}
	}
	/**
	 * @see IPresentationDamager#getDamageRegion(ITypedRegion, DocumentEvent, boolean)
	 */
	public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent event,
			boolean documentPartitioningChanged) {
		// todo
		return new Region(0, iDocument.getLength());
	}
	protected void addRange(ArrayList styleRanges, int offset, int length, TextAttribute attr, int totalLength) {
		boolean italic = (attr.getStyle() & SWT.ITALIC) != 0 ? true : false;
		boolean bold = (attr.getStyle() & SWT.BOLD) != 0 ? true : false;
		if (attr != null && offset >= 0 && offset + length <= totalLength && length > 0) {
			logger.debug("addRange() : offset; length; RGB; Bold?; Italic? : " 
					+ offset + "; " + length + "; " + attr.getForeground() + "; " + bold + "; " + italic);
			styleRanges.add(new StyleRange(offset, length, attr.getForeground(), 
					attr.getBackground(), attr.getStyle()));
		} else {
			logger.warning("addRange() : corrupted range was not added : offset; length; RGB; Bold?; Italic? : " 
					+ offset + "; " + length + "; " + attr.getForeground() + "; " + bold + "; " + italic);
		}
	}
}