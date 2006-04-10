/* Generated By:JavaCC: Do not edit this line. Parser.java */
package org.peertrust.parser.protune;

import java.io.*;
import java.util.*;

public class Parser implements ParserConstants {

        public static void main(String args[]) throws ParseException,TokenMgrError{

                Parser parser=null;
                try{

                        parser=new Parser(new FileReader("foo/exemplu.pro"));
                        parser.start(System.out);

                }catch(Exception e){
                        e.printStackTrace();
                        return;
                }

        }

//------------------------------------------------------------------------------------------------------------
  final public void start(PrintStream out) throws ParseException {
        String id=null;
        String head=null;

        String rule=null;
        String metarule=null;
    id = Id();
    jj_consume_token(20);
    jj_consume_token(0);
  }

//------------------------------------------------------------------------------------------------------------
  final public String Id() throws ParseException {
        String s=null;
    jj_consume_token(21);
    s = Constant();
    jj_consume_token(22);
         {if (true) return s;}
    throw new Error("Missing return statement in function");
  }

//------------------------------------------------------------------------------------------------------------
  final public String Constant() throws ParseException {
        Token t=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case IDENTIFIER:
      t = jj_consume_token(IDENTIFIER);
         {if (true) return t.image;}
      break;
    case NUMBER:
      t = jj_consume_token(NUMBER);
         {if (true) return t.image;}
      break;
    case STRING_QUOTED:
      t = jj_consume_token(STRING_QUOTED);
         {if (true) return t.image;}
      break;
    default:
      jj_la1[0] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

//------------------------------------------------------------------------------------------------------------
  final public String Head() throws ParseException {
        Token t=null;
        String s=null;
        boolean flag=false;
    t = jj_consume_token(IDENTIFIER);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 23:
      jj_consume_token(23);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case VARIABLE:
      case IDENTIFIER:
      case STRING_QUOTED:
      case NUMBER:
        s = Arguments();
        break;
      default:
        jj_la1[1] = jj_gen;
        ;
      }
      jj_consume_token(24);
                                                   flag=true;
      break;
    default:
      jj_la1[2] = jj_gen;
      ;
    }
                if(s==null){

                        if(flag==false)
                                {if (true) return t.image;}
                        else
                                {if (true) return t.image+"()";}
                }

                {if (true) return t.image+"("+s+")";}
    throw new Error("Missing return statement in function");
  }

//------------------------------------------------------------------------------------------------------------
  final public String Arguments() throws ParseException {
        String arg=null;
        String args=null;
    arg = Argument();
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 25:
      jj_consume_token(25);
      args = Arguments();
      break;
    default:
      jj_la1[3] = jj_gen;
      ;
    }
                if(args==null)
                        {if (true) return arg;}

                {if (true) return arg+", "+args;}
    throw new Error("Missing return statement in function");
  }

//------------------------------------------------------------------------------------------------------------
  final public String Argument() throws ParseException {
        Token t=null;
        String s=null;
        boolean flag=false;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case VARIABLE:
      t = jj_consume_token(VARIABLE);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 21:
        jj_consume_token(21);
        s = Fields(t.image);
        jj_consume_token(22);
        break;
      default:
        jj_la1[4] = jj_gen;
        ;
      }
                if(s==null)
                        {if (true) return t.image;}

                {if (true) return s;}
      break;
    case IDENTIFIER:
      t = jj_consume_token(IDENTIFIER);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 21:
      case 23:
        switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
        case 21:
          jj_consume_token(21);
          s = Fields(t.image);
          jj_consume_token(22);
          break;
        case 23:
          jj_consume_token(23);
          switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
          case VARIABLE:
          case IDENTIFIER:
          case STRING_QUOTED:
          case NUMBER:
            s = Arguments();
            break;
          default:
            jj_la1[5] = jj_gen;
            ;
          }
          jj_consume_token(24);
                                                                                  flag=true;
          break;
        default:
          jj_la1[6] = jj_gen;
          jj_consume_token(-1);
          throw new ParseException();
        }
        break;
      default:
        jj_la1[7] = jj_gen;
        ;
      }
                if(s==null){

                        if(flag==false)
                                {if (true) return t.image;}
                        else
                                {if (true) return t.image+"()";}
                }

                if(flag==true)
                        {if (true) return t.image+"("+s+")";}

                {if (true) return s;}
      break;
    case NUMBER:
      t = jj_consume_token(NUMBER);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 21:
        jj_consume_token(21);
        s = Fields(t.image);
        jj_consume_token(22);
        break;
      default:
        jj_la1[8] = jj_gen;
        ;
      }
                if(s==null)
                        {if (true) return t.image;}

                {if (true) return s;}
      break;
    case STRING_QUOTED:
      t = jj_consume_token(STRING_QUOTED);
      switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
      case 21:
        jj_consume_token(21);
        s = Fields(t.image);
        jj_consume_token(22);
        break;
      default:
        jj_la1[9] = jj_gen;
        ;
      }
                if(s==null)
                        {if (true) return t.image;}

                {if (true) return s;}
      break;
    default:
      jj_la1[10] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

//------------------------------------------------------------------------------------------------------------
  final public String Fields(String id) throws ParseException {
        String field=null;
        String fields=null;
    field = Field(id);
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case 25:
      jj_consume_token(25);
      fields = Fields(id);
      break;
    default:
      jj_la1[11] = jj_gen;
      ;
    }
                if(fields==null)
                        {if (true) return field;}

                {if (true) return field+", "+fields;}
    throw new Error("Missing return statement in function");
  }

//------------------------------------------------------------------------------------------------------------
  final public String Field(String id) throws ParseException {
        Token atr=null;
        String value=null;
    atr = jj_consume_token(IDENTIFIER);
    jj_consume_token(26);
    value = Term();
         {if (true) return atr.image+"("+id+", "+value+")";}
    throw new Error("Missing return statement in function");
  }

//------------------------------------------------------------------------------------------------------------
  final public String Term() throws ParseException {
        Token t=null;
        String s=null;
    switch ((jj_ntk==-1)?jj_ntk():jj_ntk) {
    case VARIABLE:
      t = jj_consume_token(VARIABLE);
         {if (true) return t.image;}
      break;
    case IDENTIFIER:
    case STRING_QUOTED:
    case NUMBER:
      s = Constant();
         {if (true) return s;}
      break;
    default:
      jj_la1[12] = jj_gen;
      jj_consume_token(-1);
      throw new ParseException();
    }
    throw new Error("Missing return statement in function");
  }

  public ParserTokenManager token_source;
  SimpleCharStream jj_input_stream;
  public Token token, jj_nt;
  private int jj_ntk;
  private int jj_gen;
  final private int[] jj_la1 = new int[13];
  static private int[] jj_la1_0;
  static {
      jj_la1_0();
   }
   private static void jj_la1_0() {
      jj_la1_0 = new int[] {0xd000,0xd800,0x800000,0x2000000,0x200000,0xd800,0xa00000,0xa00000,0x200000,0x200000,0xd800,0x2000000,0xd800,};
   }

  public Parser(java.io.InputStream stream) {
     this(stream, null);
  }
  public Parser(java.io.InputStream stream, String encoding) {
    try { jj_input_stream = new SimpleCharStream(stream, encoding, 1, 1); } catch(java.io.UnsupportedEncodingException e) { throw new RuntimeException(e); }
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
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
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  public Parser(java.io.Reader stream) {
    jj_input_stream = new SimpleCharStream(stream, 1, 1);
    token_source = new ParserTokenManager(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  public void ReInit(java.io.Reader stream) {
    jj_input_stream.ReInit(stream, 1, 1);
    token_source.ReInit(jj_input_stream);
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  public Parser(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
  }

  public void ReInit(ParserTokenManager tm) {
    token_source = tm;
    token = new Token();
    jj_ntk = -1;
    jj_gen = 0;
    for (int i = 0; i < 13; i++) jj_la1[i] = -1;
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

  private java.util.Vector<int[]> jj_expentries = new java.util.Vector<int[]>();
  private int[] jj_expentry;
  private int jj_kind = -1;

  public ParseException generateParseException() {
    jj_expentries.removeAllElements();
    boolean[] la1tokens = new boolean[27];
    for (int i = 0; i < 27; i++) {
      la1tokens[i] = false;
    }
    if (jj_kind >= 0) {
      la1tokens[jj_kind] = true;
      jj_kind = -1;
    }
    for (int i = 0; i < 13; i++) {
      if (jj_la1[i] == jj_gen) {
        for (int j = 0; j < 32; j++) {
          if ((jj_la1_0[i] & (1<<j)) != 0) {
            la1tokens[j] = true;
          }
        }
      }
    }
    for (int i = 0; i < 27; i++) {
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
