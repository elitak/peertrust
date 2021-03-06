/* Generated By:JavaCC: Do not edit this line. PeerTrustGrammar.java */
   /**
    * This class represents PeerTrust parser. It consists of two methods
    * ConvertRules() and SyntaxCheck() to be used for converting PeerTrust
    * programs to Prolog and automatically generated methods for parsing.
    *
    * @author Bogdan Vlasenko
    */

package org.peertrust.parser.peertrust ;

import java.util.Vector;

public class PeerTrustGrammar implements PeerTrustGrammarConstants {
    public static Vector tokens = new Vector(0, 1);

   /**
    * Converts PeerTrust program saved to a file to prolog syntax and saves
    * result to another file. Throws IOException or FileNotFoundException
    * if load / save operations were unsuccessful or ParseException if
    * syntax of input was wrong.
    *
    * @param  inputPath path to input file
    *         outputPath path to output file
    */

    public static void ConvertRules( String inputPath, String outputPath)
    throws ParseException, java.io.FileNotFoundException, java.io.IOException
    {
        System.out.println("Getting rules from " + inputPath );
        java.io.FileInputStream inStream = new java.io.FileInputStream( inputPath );
        java.io.FileWriter outFile = new java.io.FileWriter( outputPath, false );
        tokens = new Vector(0, 1);
        PeerTrustGrammar parser = new PeerTrustGrammar( inStream );
        parser.Input();
        Vector convertedRules = PeerTrustSemanticAnalyzer.analyze(tokens);
        for( int i = 0; i < convertedRules.capacity(); i++ )
        {
            String str = ((String) convertedRules.elementAt(i)) + "\n";
            outFile.write(str, 0, str.length());
        }
        inStream.close();
        outFile.close();
        System.out.println("Syntax looks to be ok, converted rules saved to " + outputPath);
    }

   /**
    * Check syntax of given PeerTrust rule. Throws ParseException if
    * syntax of input was wrong.
    *
    * @param  stringToCheck PeerTrust rule to be checked
    *
    * @return Vector with one or two (in case of policy rule) strings
    * which are translation of given PeerTrust rule to Prolog
    */

public static Vector SyntaxCheck( String stringToCheck )
throws ParseException, java.io.IOException
{
    //System.out.println("Checking rule: " + stringToCheck );
    java.io.StringBufferInputStream inStream = new java.io.StringBufferInputStream( stringToCheck);
    tokens = new Vector(0, 1);
    PeerTrustGrammar parser = new PeerTrustGrammar( inStream );
    parser.Input();

    Vector convertedRules = PeerTrustSemanticAnalyzer.analyze(tokens);

    //System.out.println("Syntax looks to be ok " + (String)convertedRules.elementAt(0));
    return( convertedRules );
}

  final public void Input() throws ParseException {
    label_1:
    while (true) {
      Rule();
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 7:
      case 16:
      case DIGITS:
      case SMALLLETTERS:
      case BIGLETTERS:
        ;
        break;
      default:
        jj_la1[0] = jj_gen;
        break label_1;
      }
    }
    jj_consume_token(0);
  }

  final public void Rule() throws ParseException {
    Head();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 5:
    case 6:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 5:
        jj_consume_token(5);
        Body();
        break;
      case 6:
        jj_consume_token(6);
        jj_consume_token(7);
        String();
        jj_consume_token(8);
        SignedBody();
        break;
      default:
        jj_la1[1] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_la1[2] = jj_gen;
      ;
    }
    jj_consume_token(9);
  }

  final public void Head() throws ParseException {
    ExtendedLiteral();
  }

  final public void ExtendedLiteral() throws ParseException {
    Literal();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 10:
      jj_consume_token(10);
      Issuer();
      break;
    default:
      jj_la1[3] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 11:
      jj_consume_token(11);
      Requester();
      break;
    default:
      jj_la1[4] = jj_gen;
      ;
    }
  }

  final public void Body() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 7:
    case 16:
    case DIGITS:
    case SMALLLETTERS:
    case BIGLETTERS:
      ExtendedLiteral();
      label_2:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 12:
          ;
          break;
        default:
          jj_la1[5] = jj_gen;
          break label_2;
        }
        jj_consume_token(12);
        ExtendedLiteral();
      }
      break;
    default:
      jj_la1[6] = jj_gen;
      ;
    }
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 6:
    case 13:
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 6:
        jj_consume_token(6);
        jj_consume_token(7);
        String();
        jj_consume_token(8);
        SignedBody();
        break;
      case 13:
        jj_consume_token(13);
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 7:
        case 16:
        case DIGITS:
        case SMALLLETTERS:
        case BIGLETTERS:
          ExtendedLiteral();
          label_3:
          while (true) {
            switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
            case 12:
              ;
              break;
            default:
              jj_la1[7] = jj_gen;
              break label_3;
            }
            jj_consume_token(12);
            ExtendedLiteral();
          }
          break;
        case 6:
          jj_consume_token(6);
          jj_consume_token(7);
          String();
          jj_consume_token(8);
          SignedBody();
          break;
        default:
          jj_la1[8] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      default:
        jj_la1[9] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      break;
    default:
      jj_la1[10] = jj_gen;
      ;
    }
  }

  final public void SignedBody() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 7:
    case 16:
    case DIGITS:
    case SMALLLETTERS:
    case BIGLETTERS:
      ExtendedLiteral();
      label_4:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 12:
          ;
          break;
        default:
          jj_la1[11] = jj_gen;
          break label_4;
        }
        jj_consume_token(12);
        ExtendedLiteral();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 13:
        jj_consume_token(13);
        ExtendedLiteral();
        label_5:
        while (true) {
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case 12:
            ;
            break;
          default:
            jj_la1[12] = jj_gen;
            break label_5;
          }
          jj_consume_token(12);
          ExtendedLiteral();
        }
        break;
      default:
        jj_la1[13] = jj_gen;
        ;
      }
      break;
    default:
      jj_la1[14] = jj_gen;
      ;
    }
  }

  final public void Issuer() throws ParseException {
    String();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 10:
      jj_consume_token(10);
      Issuer();
      break;
    default:
      jj_la1[15] = jj_gen;
      ;
    }
  }

  final public void Requester() throws ParseException {
    String();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 11:
      jj_consume_token(11);
      Requester();
      break;
    default:
      jj_la1[16] = jj_gen;
      ;
    }
  }

  final public void Literal() throws ParseException {
    ListOfStrings();
    jj_consume_token(14);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 7:
    case 16:
    case DIGITS:
    case SMALLLETTERS:
    case BIGLETTERS:
      ListOfStrings();
      break;
    default:
      jj_la1[17] = jj_gen;
      ;
    }
    label_6:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 12:
        ;
        break;
      default:
        jj_la1[18] = jj_gen;
        break label_6;
      }
      jj_consume_token(12);
      ListOfStrings();
    }
    jj_consume_token(15);
  }

  final public void ListOfStrings() throws ParseException {
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 16:
    case DIGITS:
    case SMALLLETTERS:
    case BIGLETTERS:
      String();
      break;
    case 7:
      jj_consume_token(7);
      String();
      jj_consume_token(12);
      ListOfStrings();
      label_7:
      while (true) {
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 12:
          ;
          break;
        default:
          jj_la1[19] = jj_gen;
          break label_7;
        }
        jj_consume_token(12);
        ListOfStrings();
      }
      jj_consume_token(8);
      break;
    default:
      jj_la1[20] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
  }

  final public void String() throws ParseException {
    label_8:
    while (true) {
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case DIGITS:
        jj_consume_token(DIGITS);
        break;
      case SMALLLETTERS:
        jj_consume_token(SMALLLETTERS);
        break;
      case BIGLETTERS:
        jj_consume_token(BIGLETTERS);
        break;
      case 16:
        jj_consume_token(16);
        break;
      default:
        jj_la1[21] = jj_gen;
        jj_consume_token(-1);
        throw new ParseException();
      }
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 16:
      case DIGITS:
      case SMALLLETTERS:
      case BIGLETTERS:
        ;
        break;
      default:
        jj_la1[22] = jj_gen;
        break label_8;
      }
    }
  }

  public PeerTrustGrammarTokenManager token_source;
  SimpleCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[23];
  static private int[] jj_la1_0;
  static {
      jj_la1_0();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0xf0080,0x60,0x60,0x400,0x800,0x1000,0xf0080,0x1000,0xf00c0,0x2040,0x2040,0x1000,0x1000,0x2000,0xf0080,0x400,0x800,0xf0080,0x1000,0x1000,0xf0080,0xf0000,0xf0000,};
   }

  public PeerTrustGrammar(java.io.InputStream stream) {
     this(stream, null);
  }
  public PeerTrustGrammar(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new PeerTrustGrammarTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 23; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.InputStream stream) {
     ReInit(stream, null);
  }
  public void ReInit(java.io.InputStream stream, String encoding) {
    try { jj_input_stream.ReInit(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 23; i++) jj_la1[i] = -1;
  }

  public PeerTrustGrammar(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new PeerTrustGrammarTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 23; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 23; i++) jj_la1[i] = -1;
  }

  public PeerTrustGrammar(PeerTrustGrammarTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 23; i++) jj_la1[i] = -1;
  }

  public void ReInit(PeerTrustGrammarTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 23; i++) jj_la1[i] = -1;
  }

  final private Token jj_consume_token(int kind) throws ParseException {
    Token oldToken;
    if ((oldToken = token).next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    if (token.kind == kind) {
      jj_gen++;
      return token;
    }
    token = oldToken;
    jj_kind = kind;
    throw generateParseException();
  }

  final public Token getNextToken() {
    if (token.next != null) token = token.next;
    else token = token.next = token_source.getNextToken();
    jj_ntk = -1;
    jj_gen++;
    return token;
  }

  final public Token getToken(int index) {
    Token t = token;
    for (int i = 0; i < index; i++) {
      if (t.next != null) t = t.next;
      else t = t.next = token_source.getNextToken();
    }
    return t;
  }

  final private int jj_ntk() {
    if ((jj_nt=token.next) == null)
      return (jj_ntk = (token.next=token_source.getNextToken()).kind);
    else
      return (jj_ntk = jj_nt.kind);
  }

  private java.util.Vector jj_expentries = new java.util.Vector();
  private int[] jj_expentry;
  private int jj_kind = -1;

  public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[20];
    for (int i = 0; i < 20; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 23; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 20; i++) {
      if (la1tokens[i]) {
        jj_expentry = new int[1];
        jj_expentry[0] = i;
        jj_expentries.addElement(jj_expentry);
      }
    }
    int[][] exptokseq = new int[jj_expentries.size()][];
    for (int i = 0; i < jj_expentries.size(); i++) {
      exptokseq[i] = (int[])jj_expentries.elementAt(i);
    }
    return new ParseException(token, exptokseq, tokenImage);
  }

  final public void enable_tracing() {
  }

  final public void disable_tracing() {
  }

}
