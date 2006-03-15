/* Generated By:JavaCC: Do not edit this line. ProtuneParserConstants.java */
package de.l3s.ppt.protuneparser.javacc;

public interface ProtuneParserConstants {

  int EOF = 0;
  int DIGIT = 5;
  int INCLUDE = 6;
  int DECLARATION = 7;
  int LOWER_CASE = 8;
  int UPPER_CASE = 9;
  int UNDERSCORE = 10;
  int OPENING_BRACKET = 11;
  int CLOSING_BRACKET = 12;
  int OPENING_SQUARE_BRACKET = 13;
  int CLOSING_SQUARE_BRACKET = 14;
  int NEG_SYMBOL = 15;
  int RULE_SEPARATOR = 16;
  int COMMA = 17;
  int SEMICOLON = 18;
  int DOT = 19;
  int OPERATOR = 20;
  int EQUAL = 21;
  int START_SINGLE_QUOTE = 22;
  int START_DOUBLE_QUOTE = 23;
  int ANY_CHARACTER = 24;
  int END_SINGLE_QUOTE = 25;
  int END_DOUBLE_QUOTE = 26;
  int ANY_CHAR = 27;

  int DEFAULT = 0;
  int IN_SINGLE_QUOTE = 1;
  int IN_DOUBLE_QUOTE = 2;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "<DIGIT>",
    "\"include\"",
    "\"declaration\"",
    "<LOWER_CASE>",
    "<UPPER_CASE>",
    "\"_\"",
    "\"(\"",
    "\")\"",
    "\"[\"",
    "\"]\"",
    "<NEG_SYMBOL>",
    "<RULE_SEPARATOR>",
    "\",\"",
    "\":\"",
    "\".\"",
    "<OPERATOR>",
    "\"=\"",
    "\"\\\'\"",
    "\"\\\"\"",
    "<ANY_CHARACTER>",
    "\"\\\'\"",
    "\"\\\"\"",
    "<ANY_CHAR>",
  };

}
