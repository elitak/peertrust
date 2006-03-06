package de.l3s.ppt.rcp.protune;

import java.util.ArrayList;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Region;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.TextPresentation;
import org.eclipse.jface.text.presentation.IPresentationDamager;
import org.eclipse.jface.text.presentation.IPresentationRepairer;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.SWT;

import de.l3s.ppt.log.Logger;
import de.l3s.ppt.protuneparser.*;
import de.l3s.ppt.protuneparser.javacc.ProtuneParser;

public class ProtuneRepairer  implements IPresentationDamager, IPresentationRepairer {
	/** The document this object works on */
	protected IDocument iDocument;
	private ArrayList parsedEntries;
	private static Logger logger = new Logger(ProtuneRepairer.class);
	private TextAttribute defaultTA;
	private TextAttribute constantTA;
	private TextAttribute variableTA;
	private TextAttribute idTA;
	private TextAttribute reservedWordTA;
	private TextAttribute metaDefaultTA;
	private TextAttribute metaConstantTA;
	private TextAttribute metaVariableTA;
	private TextAttribute metaIdTA;
	private TextAttribute metaReservedWordTA;
	private TextAttribute commentTA;
	private TextAttribute errorTA;
	public ProtuneRepairer(ProtuneColorManager colorManager) {
		defaultTA = colorManager.getTextAttribute(ProtuneColorManager.DEFAULT_COLOR);
		constantTA = colorManager.getTextAttribute(ProtuneColorManager.CONSTANT_COLOR);
		variableTA = colorManager.getTextAttribute(ProtuneColorManager.VARIABLE_COLOR);
		idTA = colorManager.getTextAttribute(ProtuneColorManager.ID_COLOR);
		reservedWordTA = colorManager.getTextAttribute(ProtuneColorManager.RESERVED_WORD_COLOR);
		metaDefaultTA = colorManager.getTextAttribute(ProtuneColorManager.META_DEFAULT_COLOR);
		metaConstantTA = colorManager.getTextAttribute(ProtuneColorManager.META_CONSTANT_COLOR);
		metaVariableTA = colorManager.getTextAttribute(ProtuneColorManager.META_VARIABLE_COLOR);
		metaIdTA = colorManager.getTextAttribute(ProtuneColorManager.META_ID_COLOR);
		metaReservedWordTA = colorManager.getTextAttribute(ProtuneColorManager.META_RESERVED_WORD_COLOR);
		commentTA = colorManager.getTextAttribute(ProtuneColorManager.COMMENT_COLOR);
		errorTA = colorManager.getTextAttribute(ProtuneColorManager.ERROR_COLOR);
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
			ProtuneParser parser = ProtuneParser.createParser(toParse, 0);
			parsedEntries = parser.parse();
		} catch (Exception e) {
			logger.error("createPresentation() : exception thrown by parser : " + e.toString());
		}
		logger.info("createPresentation() : entries.size() : " + parsedEntries.size());
		for (int i = 0; i < parsedEntries.size(); i++) {
			Object obj = parsedEntries.get(i);
			if (obj instanceof Integer) {
				logger.info("createPresentation() : Integer entry, offset : " + ((Integer)obj).intValue());
			} else if (obj instanceof Rule) {
				logger.info("createPresentation() : Rule entry, offset : " + ((Rule)obj).offsetInInput);
			} else {
				logger.info("createPresentation() : Rule entry, offset : " + ((Directive)obj).offsetInInput);
			}
		}
		ArrayList styleRanges = new ArrayList();
		ArrayList knownIds = new ArrayList();
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
			} else if (obj instanceof Rule) {
				Rule rule = (Rule)obj;
				offset = rule.offsetInInput;
				if (!rule.isMetaRule()) {
					if (rule.hasId()) {
						if (knownIds.contains(rule.getId().getStr())) {
							// duplicated id, marking the rule as a damaged part
							length = offset - previousOffset;
							addRange(styleRanges, rangeOffset, length, errorTA, totalLength);
						} else {
							knownIds.add(rule.getId().getStr());
							decorateRule(styleRanges, rule, totalLength);
						}
					} else {
						decorateRule(styleRanges, rule, totalLength);
					}
				} else {
					if (!rule.hasId()) {
						//meta rule must have id, otherwise it is marked as a damaged part
						length = offset - previousOffset;
						addRange(styleRanges, rangeOffset, length, errorTA, totalLength);
					} else {
						if (knownIds.contains(rule.getId().getStr())) {
							decorateRule(styleRanges, rule, totalLength);
						} else {
							//unknown id of the meta rule must, marking meta rule as a damaged part
							length = offset - previousOffset;
							addRange(styleRanges, rangeOffset, length, errorTA, totalLength);
						}
					}
				}
			} else {
				Directive directive = (Directive)obj;
				offset = directive.offsetInInput;
				decorateDirective(styleRanges, directive, totalLength);
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
	protected void decorateRule(ArrayList styleRanges, Rule rule, int totalLength) {
		if (rule.isMetaRule()) {
			// meta rule must have id, otherwise it is marked as damaged part above
			addRange(styleRanges, rule.getId().getBeginOffset(), 
					rule.offsetInInput - rule.getId().getBeginOffset() + 1, metaDefaultTA, totalLength);
			addRange(styleRanges, rule.getId().getBeginOffset(), 
					rule.getId().getStr().length(), metaIdTA, totalLength);
			MetaLiteral head = rule.getMetaHead();
			Literal literal = head.getLiteral();
			decorateLiteral(styleRanges, literal, totalLength, true);
			if (head.hasAttributeAndValue()) {
				addRange(styleRanges, head.getAttribute().getBeginOffset(), 
						head.getAttribute().getStr().length(), metaConstantTA, totalLength);
				decorateTerm(styleRanges, head.getValue(), totalLength, true);
			}
		} else {
			if (rule.hasId()) {
				addRange(styleRanges, rule.getId().getBeginOffset(), 
						rule.getId().getStr().length(), idTA, totalLength);
			}
			decorateLiteral(styleRanges, rule.getHead(), totalLength, false);
		}
		for (int i = 0; i < rule.getBody().size(); i++) {
			Object obj = rule.getBody().get(i);
			if (obj instanceof Literal) {
				decorateLiteral(styleRanges, (Literal)obj, totalLength, rule.isMetaRule());
			} else if (obj instanceof MetaLiteral) {
				MetaLiteral metaLiteral = (MetaLiteral)obj;
				decorateLiteral(styleRanges, metaLiteral.getLiteral(), totalLength, rule.isMetaRule());
				if (metaLiteral.hasAttributeAndValue()) {
					TextAttribute ta = rule.isMetaRule() ? metaConstantTA : constantTA;
					addRange(styleRanges, metaLiteral.getAttribute().getBeginOffset(), 
							metaLiteral.getAttribute().getStr().length(), ta, totalLength);
					decorateTerm(styleRanges, metaLiteral.getValue(), totalLength, rule.isMetaRule());
				}
			} else {
				decorateComplexTerm(styleRanges, (ComplexTerm)obj, totalLength, rule.isMetaRule());
			}
		}
	}
	protected void decorateTerm(ArrayList styleRanges, Term term, int totalLength, boolean isMetaRule) {
		if (term.isVariable()) {
			TextAttribute ta = isMetaRule ? metaVariableTA : variableTA;
			addRange(styleRanges, term.getStr().getBeginOffset(), term.getStr().getStr().length(), ta, totalLength);
		} else if (term.isConstant()) {
			TextAttribute ta = isMetaRule ? metaConstantTA : constantTA;
			addRange(styleRanges, term.getStr().getBeginOffset(), term.getStr().getStr().length(), ta, totalLength);
		} else {
			// term is list
			ArrayList list = term.getList();
			for (int i = 0; i < list.size(); i++) {
				Term listTerm = (Term)list.get(i);
				decorateTerm(styleRanges, listTerm, totalLength, isMetaRule);
			}
		}
	}
	protected void decorateComplexTerm(ArrayList styleRanges, ComplexTerm term, int totalLength, boolean isMetaRule) {
		TextAttribute ta = isMetaRule ? metaVariableTA : variableTA;
		addRange(styleRanges, term.getVariable().getBeginOffset(), term.getVariable().getStr().length(), ta, totalLength);
		ta = isMetaRule ? metaConstantTA : constantTA;
		addRange(styleRanges, term.getAttribute().getBeginOffset(), term.getAttribute().getStr().length(), ta, totalLength);
		decorateTerm(styleRanges, term.getTerm(), totalLength, isMetaRule);
	}
	protected void decorateLiteral(ArrayList styleRanges, Literal literal, int totalLength, boolean isMetaRule) {
		if (literal.isTermOperatorTerm()) {
			decorateTerm(styleRanges, literal.getTermBefore(), totalLength, isMetaRule);
			decorateTerm(styleRanges, literal.getTermAfter(), totalLength, isMetaRule);
		} else if (literal.isDeclaration()) {
			ArrayList variableBindingList = literal.getVariableBindingList();
			StringDescription commandWord = literal.getCommandWord();
			TextAttribute ta = isMetaRule ? metaReservedWordTA : reservedWordTA;
			addRange(styleRanges, commandWord.getBeginOffset(), commandWord.getStr().length(), ta, totalLength);
			for (int i = 0; i < variableBindingList.size(); i++) {
				VariableBinding vb = (VariableBinding)variableBindingList.get(i);
				ta = isMetaRule ? metaConstantTA : constantTA;
				addRange(styleRanges, vb.getConstant().getBeginOffset(), vb.getConstant().getStr().length(), ta, totalLength);
				ta = isMetaRule ? metaVariableTA : variableTA;
				addRange(styleRanges, vb.getVariable().getBeginOffset(), vb.getVariable().getStr().length(), ta, totalLength);
			}
		} else {
			decorateArguments(styleRanges, literal.getArguments(), totalLength, isMetaRule);
		}
	}
	protected void decorateArguments(ArrayList styleRanges, Arguments arguments, int totalLength, boolean isMetaRule) {
		if (arguments.isExists()) {
			ArrayList list = arguments.getList();
			for (int i = 0; i < list.size(); i++) {
				Argument argument = (Argument)list.get(i);
				if (argument.isTerm()) {
					decorateTerm(styleRanges, argument.getTerm(), totalLength, isMetaRule);
				} else if (argument.startsWithPredicate()){
					if (!argument.getArguments().isExists()) {
						// highlighting constant
						TextAttribute ta = isMetaRule ? metaConstantTA : constantTA;
						addRange(styleRanges, argument.getPredicate().getBeginOffset(), argument.getPredicate().getStr().length(), ta, totalLength);
					} else {
						decorateArguments(styleRanges, argument.getArguments(), totalLength, isMetaRule);
					}
				} else {
					TextAttribute ta = isMetaRule ? metaConstantTA : constantTA;
					addRange(styleRanges, argument.getPackageName().getBeginOffset(), argument.getPackageName().getStr().length(), ta, totalLength);
					addRange(styleRanges, argument.getFunction().getBeginOffset(), argument.getFunction().getStr().length(), ta, totalLength);
					decorateTerm(styleRanges, argument.getTerm(), totalLength, isMetaRule);
				}
			}
		}
	}
	private void decorateDirective(ArrayList styleRanges, Directive directive, int totalLength) {
		StringDescription commandWord = directive.getCommandWord();
		addRange(styleRanges, commandWord.getBeginOffset(), commandWord.getStr().length(), reservedWordTA, totalLength);
		StringDescription quotedText = directive.getQuotedText();
		addRange(styleRanges, quotedText.getBeginOffset(), quotedText.getStr().length(), constantTA, totalLength);
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
