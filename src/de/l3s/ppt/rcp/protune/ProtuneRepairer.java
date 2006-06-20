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
			String toParse = iDocument.get().substring(region.getOffset(),
				region.getOffset() + region.getLength());
		logger.debug("createPresentation() : toParse : " + toParse);
		parsedEntries = new ArrayList();
		try {
			ProtuneParser parser = ProtuneParser.createParser(toParse, 0);
			parsedEntries = parser.parse();
		} catch (Exception e) {
			logger.error("createPresentation() : exception thrown by parser : "
					+ e.toString());
		}
		logger.info("createPresentation() : entries.size() : "
				+ parsedEntries.size());
		for (int i = 0; i < parsedEntries.size(); i++) {
			Object obj = parsedEntries.get(i);
			if (obj instanceof Integer) {
				logger.info("createPresentation() : Error entry, offset : "
						+ ((Integer) obj).intValue());
			} else if (obj instanceof Rule) {
				logger.info("createPresentation() : Rule entry, offset : "
						+ ((Rule) obj).offsetInInput);
			} else if (obj instanceof MetaRule) {
				logger.info("createPresentation() : MetaRule entry, offset : "
						+ ((MetaRule) obj).offsetInInput);
			} else if (obj instanceof StringDescription) {
				logger.info("createPresentation() : Comment entry, offset : "
						+ ((StringDescription) obj).getEndOffset());
			} else {
				logger.info("createPresentation() : Directive entry, offset : "
						+ ((Directive) obj).offsetInInput);
			}
		}
		ArrayList styleRanges = new ArrayList();
		ArrayList knownIds = new ArrayList();
		ArrayList knownHeadLiterals = new ArrayList();
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
				offset = ((Integer) obj).intValue();
				length = offset - previousOffset;
				addRange(styleRanges, rangeOffset, length, errorTA, totalLength);
			} else if (obj instanceof StringDescription) {
				StringDescription comment = (StringDescription)obj;
				offset = comment.getEndOffset();
				length = offset - previousOffset;
				addRange(styleRanges, rangeOffset, length, commentTA, totalLength);
			} else if (obj instanceof MetaRule) {
				MetaRule metaRule = (MetaRule) obj;
				offset = metaRule.offsetInInput;
				if (metaRule.getMetaHeadLiteral().hasId()) {
					if (knownIds.contains(metaRule.getMetaHeadLiteral().getId().getStr())) {
						decorateMetaRule(styleRanges, metaRule, totalLength, rangeOffset);
					} else {
						// meta rule must have the same id or the same
						// literal in head as some rule above,
						// otherwise it is marked as a damaged part
						length = offset - previousOffset;
						addRange(styleRanges, rangeOffset, length, errorTA,
								totalLength);
					}
				} else {
					if (knownHeadLiterals.contains(getHeadLiteralImage(metaRule.getMetaHeadLiteral().getHeadLiteral()))) {
						decorateMetaRule(styleRanges, metaRule, totalLength, rangeOffset);
					} else {
						// meta rule must have the same id or the same
						// literal in head as some rule above,
						// otherwise it is marked as a damaged part
						length = offset - previousOffset;
						addRange(styleRanges, rangeOffset, length, errorTA,
								totalLength);
					}
				}
			} else if (obj instanceof Rule) {
				Rule rule = (Rule) obj;
				offset = rule.offsetInInput;
				if (rule.hasId()) {
					if (knownIds.contains(rule.getId().getStr())) {
						// duplicated id, marking the rule as a damaged part
						length = offset - previousOffset;
						addRange(styleRanges, rangeOffset, length, errorTA,
								totalLength);
					} else {
						knownIds.add(rule.getId().getStr());
						decorateRule(styleRanges, rule, totalLength);
						knownHeadLiterals.add(getHeadLiteralImage(rule.getHeadLiteral()));
					}
				} else {
					decorateRule(styleRanges, rule, totalLength);
					knownHeadLiterals.add(getHeadLiteralImage(rule.getHeadLiteral()));
				}
			} else {
				Directive directive = (Directive) obj;
				offset = directive.offsetInInput;
				decorateDirective(styleRanges, directive, totalLength);
			}
			previousOffset = offset;
		}
		// debug
		for (int i = 0; i < knownIds.size(); i++) {
			logger.debug("konwnId = " + (String)knownIds.get(i));
		}
		for (int i = 0; i < knownHeadLiterals.size(); i++) {
			logger.debug("konwnHeadLiteral = " + (String)knownHeadLiterals.get(i));
		}
		// debug
		StyleRange[] temp = new StyleRange[styleRanges.size()];
		for (int i = 0; i < styleRanges.size(); i++) {
			temp[i] = (StyleRange) styleRanges.get(i);
		}
		logger.info("createPresentation() : number of style ranges : "
				+ temp.length);
		try {
			presentation.replaceStyleRanges(temp);
		} catch (Exception e) {
			logger
					.error("createPresentation() : exception thrown when replacing style ranges : "
							+ e.toString());
		}
		logger.info("createPresentation() : EXIT");
	}
	protected void decorateTerm(ArrayList styleRanges, StringDescription term, int totalLength, boolean isMetaRule) {
		if (term.isVariable()) {
			TextAttribute ta = isMetaRule ? metaVariableTA : variableTA;
			addRange(styleRanges, term.getBeginOffset(), term.getStr().length(), ta, totalLength);
		} else if (term.isConstant() || term.isStringConstant() || term.isQuoted()) {
			TextAttribute ta = isMetaRule ? metaConstantTA : constantTA;
			addRange(styleRanges, term.getBeginOffset(), term.getStr().length(), ta, totalLength);
		}
	}
	protected void decorateField(ArrayList styleRanges, Field field, int totalLength, boolean isMetaRule) {
		decorateTerm(styleRanges, field.getAttribute(), totalLength, isMetaRule);
		decorateTerm(styleRanges, field.getValue(), totalLength, isMetaRule);
	}
	protected void decorateComplexTerm(ArrayList styleRanges, ComplexTerm complexTerm, int totalLength, boolean isMetaRule) {
		decorateTerm(styleRanges, complexTerm.getVariableOrConstant(), totalLength, isMetaRule);
		ArrayList fieldList = complexTerm.getFieldList();
		if (fieldList != null) {
			for (int i = 0; i < fieldList.size(); i++) {
				decorateField(styleRanges, (Field)fieldList.get(i), totalLength, isMetaRule);
			}
		}
	}
	protected void decorateAnyTerm(ArrayList styleRanges, AnyTerm anyTerm, int totalLength, boolean isMetaRule) {
		if (anyTerm.isComplex()) {
			decorateComplexTerm(styleRanges, anyTerm.getComplexTerm(), totalLength, isMetaRule);
		} else {
			decorateTerm(styleRanges, anyTerm.getTerm(), totalLength, isMetaRule);
		}
	}
	protected void decorateFunction(ArrayList styleRanges, Function function, int totalLength, boolean isMetaRule) {
		if(!function.hasBrackets()) {
			decorateTerm(styleRanges, function.getPredicate(), totalLength, isMetaRule);
		} else {
			ArrayList termList = function.getTermList();
			if (termList != null) {
				for (int i = 0; i < termList.size(); i++) {
					decorateTerm(styleRanges, (StringDescription)termList.get(i), totalLength, isMetaRule);
				}
			}
		}
	}
	protected void decoratePackageCall(ArrayList styleRanges, PackageCall packageCall, int totalLength, boolean isMetaRule) {
		decorateTerm(styleRanges, packageCall.getPackageName(), totalLength, isMetaRule);
		decorateFunction(styleRanges, packageCall.getFunction(), totalLength, isMetaRule);
	}
	protected void decoratePredicateLiteral(ArrayList styleRanges, PredicateLiteral predicateLiteral, int totalLength, boolean isMetaRule) {
		ArrayList argumentList = predicateLiteral.getArguments();
		if (argumentList != null) {
			for (int i = 0; i < argumentList.size(); i++) {
				Argument argument = (Argument)argumentList.get(i);
				if (argument.hasPredicateLiteral()) {
					decoratePredicateLiteral(styleRanges, argument.getPredicateLiteral(), totalLength, isMetaRule);
				} else {
					decorateAnyTerm(styleRanges, argument.getAnyTerm(), totalLength, isMetaRule);
				}
			}
		}
	}
	protected void decorateSpecialLiteral(ArrayList styleRanges, SpecialLiteral specialLiteral, int totalLength, boolean isMetaRule) {
		TextAttribute ta = isMetaRule ? metaReservedWordTA : reservedWordTA;
		addRange(styleRanges, specialLiteral.getCommandWord().getBeginOffset(), specialLiteral.getCommandWord().getStr().length(), ta, totalLength);
		if (specialLiteral.isIn()) {
			decorateFunction(styleRanges, specialLiteral.getFunction(), totalLength, isMetaRule);
			decoratePackageCall(styleRanges, specialLiteral.getPackageCall(), totalLength, isMetaRule);
		} else {
			decorateTerm(styleRanges, specialLiteral.getId(), totalLength, isMetaRule);
			decorateAnyTerm(styleRanges, specialLiteral.getAnyTerm(), totalLength, isMetaRule);
		}
	}
	protected void decorateHeadLiteral(ArrayList styleRanges, HeadLiteral headLiteral, int totalLength, boolean isMetaRule) {
		if (headLiteral.isPredicateLiteral()) {
			decoratePredicateLiteral(styleRanges, headLiteral.getPredicateLiteral(), totalLength, isMetaRule);
		} else if (headLiteral.isComplexTerm()) {
			decorateComplexTerm(styleRanges, headLiteral.getComplexTerm(), totalLength, isMetaRule);
		}
	}
	protected void decorateLiteral(ArrayList styleRanges, Literal literal, int totalLength, boolean isMetaRule) {
		if (literal.isSpecialLiteral()) {
			decorateSpecialLiteral(styleRanges, literal.getSpecialLiteral(), totalLength, isMetaRule);
		} else if (literal.isHeadLiteral()) {
			decorateHeadLiteral(styleRanges, literal.getHeadLiteral(), totalLength, isMetaRule);
		} else {
			// term operator term
			decorateTerm(styleRanges, literal.getTermBefore(), totalLength, isMetaRule);
			decorateTerm(styleRanges, literal.getTermAfter(), totalLength, isMetaRule);
		}
	}
	protected void decorateMetaHeadLiteral(ArrayList styleRanges, MetaHeadLiteral metaHeadLiteral, int totalLength) {
		if (metaHeadLiteral.hasId()) {
			addRange(styleRanges, metaHeadLiteral.getId().getBeginOffset(), metaHeadLiteral.getId().getStr().length(), metaIdTA, totalLength);
		} else {
			decorateHeadLiteral(styleRanges, metaHeadLiteral.getHeadLiteral(), totalLength, true);
		}
		decorateField(styleRanges, metaHeadLiteral.getField(), totalLength, true);
	}
	protected void decorateMetaLiteral(ArrayList styleRanges, MetaLiteral metaLiteral, int totalLength) {
		if (metaLiteral.isLiteral()) {
			decorateLiteral(styleRanges, metaLiteral.getLiteral(), totalLength, true);
		} else {
			decorateMetaHeadLiteral(styleRanges, metaLiteral.getMetaHeadLiteral(), totalLength);
		}
	}
	protected void decorateRule(ArrayList styleRanges, Rule rule, int totalLength) {
		if (rule.hasId()) {
			addRange(styleRanges, rule.getId().getBeginOffset(), rule.getId().getStr().length(), idTA, totalLength);
		}
		decorateHeadLiteral(styleRanges, rule.getHeadLiteral(), totalLength, false);
		ArrayList body = rule.getBody();
		if (body != null) {
			for (int i = 0; i < body.size(); i++) {
				decorateLiteral(styleRanges, (Literal)body.get(i), totalLength, false);
			}
		}
	}
	protected void decorateMetaRule(ArrayList styleRanges, MetaRule metaRule, int totalLength, int beginOffset) {
		addRange(styleRanges, beginOffset, metaRule.offsetInInput - beginOffset + 1, metaDefaultTA, totalLength);
		decorateMetaHeadLiteral(styleRanges, metaRule.getMetaHeadLiteral(), totalLength);
		ArrayList body = metaRule.getMetaBody();
		if (body != null) {
			for (int i = 0; i < body.size(); i++) {
				decorateMetaLiteral(styleRanges, (MetaLiteral)body.get(i), totalLength);
			}
		}
	}
	private void decorateDirective(ArrayList styleRanges, Directive directive, int totalLength) {
		StringDescription commandWord = directive.getCommandWord();
		addRange(styleRanges, commandWord.getBeginOffset(), commandWord.getStr().length(), reservedWordTA, totalLength);
		StringDescription quotedText = directive.getQuotedText();
		addRange(styleRanges, quotedText.getBeginOffset(), quotedText.getStr().length(), constantTA, totalLength);
	}
	public IRegion getDamageRegion(ITypedRegion partition, DocumentEvent event,
			boolean documentPartitioningChanged) {
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
	private String getHeadLiteralImage(HeadLiteral headLiteral) {
		StringBuffer image = new StringBuffer();
		if (headLiteral.isStringConstant()) {
			image.append(headLiteral.getStringConstant().getStr());
		} else if (headLiteral.isComplexTerm()) {
			ComplexTerm complexTerm = headLiteral.getComplexTerm();
			image.append(complexTerm.getVariableOrConstant().getStr());
			image.append(Constants.OPENING_SQUARE_BRACKET);
			ArrayList fieldList = complexTerm.getFieldList();
			if (fieldList != null) {
				for (int i = 0; i < fieldList.size(); i++) {
					Field field = (Field)fieldList.get(i);
					image.append(field.getAttribute().getStr());
					image.append(Constants.SEMICOLON);
					image.append(field.getValue().getStr());
					if (i != fieldList.size() - 1) {
						image.append(Constants.COMMA);
					}
				}
			}
			image.append(Constants.CLOSING_SQUARE_BRACKET);
		} else {
			image.append(getPredicateLiteralImage(headLiteral.getPredicateLiteral()));
		}
		return image.toString();
	}
	private String getPredicateLiteralImage(PredicateLiteral predicateLiteral) {
		StringBuffer image = new StringBuffer();
		image.append(predicateLiteral.getPredicate().getStr());
		image.append(Constants.OPENING_BRACKET);
		ArrayList argumentList = predicateLiteral.getArguments();
		if (argumentList != null) {
			for (int i = 0; i < argumentList.size(); i++) {
				Argument argument = (Argument)argumentList.get(i);
				if (!argument.hasPredicateLiteral()) {
					AnyTerm anyTerm = argument.getAnyTerm();
					if (anyTerm.isComplex()) {
						ComplexTerm complexTerm = anyTerm.getComplexTerm();
						image.append(complexTerm.getVariableOrConstant().getStr());
						image.append(Constants.OPENING_SQUARE_BRACKET);
						ArrayList fieldList = complexTerm.getFieldList();
						if (fieldList != null) {
							for (int j = 0; j < fieldList.size(); j++) {
								Field field = (Field)fieldList.get(j);
								image.append(field.getAttribute().getStr());
								image.append(Constants.SEMICOLON);
								image.append(field.getValue().getStr());
								if (j != fieldList.size() - 1) {
									image.append(Constants.COMMA);
								}
							}
						}
						image.append(Constants.CLOSING_SQUARE_BRACKET);
					} else {
						image.append(anyTerm.getTerm().getStr());
					}
				} else {
					image.append(getPredicateLiteralImage(argument.getPredicateLiteral()));
				}
				if (i != argumentList.size() - 1) {
					image.append(Constants.COMMA);
				}
			}
		}
		image.append(Constants.CLOSING_BRACKET);
		return image.toString();
	}
}
