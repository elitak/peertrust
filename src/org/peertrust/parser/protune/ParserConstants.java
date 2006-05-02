/* Generated By:JavaCC: Do not edit this line. ParserConstants.java */
package org.peertrust.parser.protune;

public interface ParserConstants {

  int EOF = 0;
  int COMMENT = 5;
  int OPERATOR = 6;
  int NEGATION = 7;
  int SEPARATOR = 8;
  int INCLUDE = 9;
  int IN = 10;
  int DECLARATION = 11;
  int CREDENTIAL = 12;
  int VARIABLE = 13;
  int IDENTIFIER = 14;
  int STRING = 15;
  int STRING_QUOTED = 16;
  int NUMBER = 17;
  int LOWERCASE = 18;
  int UPPERCASE = 19;
  int DIGIT = 20;
  int UNDERSCORE = 21;

  int DEFAULT = 0;

  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\r\"",
    "\"\\t\"",
    "\"\\n\"",
    "<COMMENT>",
    "<OPERATOR>",
    "<NEGATION>",
    "<SEPARATOR>",
    "\"include\"",
    "\"in\"",
    "\"declaration\"",
    "\"credential\"",
    "<VARIABLE>",
    "<IDENTIFIER>",
    "<STRING>",
    "<STRING_QUOTED>",
    "<NUMBER>",
    "<LOWERCASE>",
    "<UPPERCASE>",
    "<DIGIT>",
    "\"_\"",
    "\".\"",
    "\"[\"",
    "\"]\"",
    "\"(\"",
    "\")\"",
    "\",\"",
    "\":\"",
  };

}
