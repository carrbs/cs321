/* Generated By:JavaCC: Do not edit this line. miniLexerConstants.java */
package lexer;


/**
 * Token literal values and constants.
 * Generated by org.javacc.parser.OtherFilesGen#start()
 */
public interface miniLexerConstants {

  /** End of File. */
  int EOF = 0;
  /** RegularExpression Id. */
  int SINGLE_LINE_COMMENT = 7;
  /** RegularExpression Id. */
  int MULTI_LINE_COMMENT = 8;
  /** RegularExpression Id. */
  int BOOLEAN = 10;
  /** RegularExpression Id. */
  int CLASS = 11;
  /** RegularExpression Id. */
  int ELSE = 12;
  /** RegularExpression Id. */
  int EXTENDS = 13;
  /** RegularExpression Id. */
  int FALSE = 14;
  /** RegularExpression Id. */
  int FLOAT = 15;
  /** RegularExpression Id. */
  int IF = 16;
  /** RegularExpression Id. */
  int INT = 17;
  /** RegularExpression Id. */
  int LENGTH = 18;
  /** RegularExpression Id. */
  int MAIN = 19;
  /** RegularExpression Id. */
  int NEW = 20;
  /** RegularExpression Id. */
  int PUBLIC = 21;
  /** RegularExpression Id. */
  int RETURN = 22;
  /** RegularExpression Id. */
  int STATIC = 23;
  /** RegularExpression Id. */
  int THIS = 24;
  /** RegularExpression Id. */
  int TRUE = 25;
  /** RegularExpression Id. */
  int VOID = 26;
  /** RegularExpression Id. */
  int WHILE = 27;
  /** RegularExpression Id. */
  int STRING = 28;
  /** RegularExpression Id. */
  int PRINT_LINE = 29;
  /** RegularExpression Id. */
  int OPERATOR = 30;
  /** RegularExpression Id. */
  int DELIMITER = 31;
  /** RegularExpression Id. */
  int KEYWORD = 32;
  /** RegularExpression Id. */
  int DIGIT = 33;
  /** RegularExpression Id. */
  int LETTER = 34;
  /** RegularExpression Id. */
  int ID = 35;
  /** RegularExpression Id. */
  int NUM = 36;
  /** RegularExpression Id. */
  int REAL = 37;
  /** RegularExpression Id. */
  int REAL_FLOAT = 38;
  /** RegularExpression Id. */
  int STRING_LITERAL = 39;

  /** Lexical state. */
  int DEFAULT = 0;
  /** Lexical state. */
  int IN_SINGLE_LINE_COMMENT = 1;
  /** Lexical state. */
  int IN_MULTI_LINE_COMMENT = 2;

  /** Literal token values. */
  String[] tokenImage = {
    "<EOF>",
    "\" \"",
    "\"\\t\"",
    "\"\\n\"",
    "\"\\r\"",
    "\"//\"",
    "\"/*\"",
    "<SINGLE_LINE_COMMENT>",
    "\"*/\"",
    "<token of kind 9>",
    "\"boolean\"",
    "\"class\"",
    "\"else\"",
    "\"extends\"",
    "\"false\"",
    "\"float\"",
    "\"if\"",
    "\"int\"",
    "\"length\"",
    "\"main\"",
    "\"new\"",
    "\"public\"",
    "\"return\"",
    "\"static\"",
    "\"this\"",
    "\"true\"",
    "\"void\"",
    "\"while\"",
    "\"String\"",
    "\"System.out.println\"",
    "<OPERATOR>",
    "<DELIMITER>",
    "<KEYWORD>",
    "<DIGIT>",
    "<LETTER>",
    "<ID>",
    "<NUM>",
    "<REAL>",
    "<REAL_FLOAT>",
    "<STRING_LITERAL>",
  };

}