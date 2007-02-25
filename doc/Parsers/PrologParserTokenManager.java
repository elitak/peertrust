/* Generated By:JJTree&JavaCC: Do not edit this line. PrologParserTokenManager.java */
package org.peertrust.inference.prolog.parser;
import java.io.* ;

public class PrologParserTokenManager implements PrologParserConstants
{
  public  java.io.PrintStream debugStream = System.out;
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
      debugStream.println("   No more string literal token matches are possible.");
   switch (pos)
   {
      default :
         return -1;
   }
}
private final int jjStartNfa_0(int pos, long active0)
{
   return jjMoveNfa_0(jjStopStringLiteralDfa_0(pos, active0), pos + 1);
}
private final int jjStopAtPos(int pos, int kind)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   debugStream.println("   No more string literal token matches are possible.");
   debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
   return pos + 1;
}
private final int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   debugStream.println("   No more string literal token matches are possible.");
   debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   debugStream.println("Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int)curChar + ") at line " + input_stream.getLine() + " column " + input_stream.getColumn());
   return jjMoveNfa_0(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 13:
         jjmatchedKind = 4;
         return jjMoveStringLiteralDfa1_0(0x20L);
      case 34:
         return jjStopAtPos(0, 21);
      case 39:
         return jjStopAtPos(0, 20);
      case 40:
         return jjStopAtPos(0, 13);
      case 41:
         return jjStopAtPos(0, 14);
      case 44:
         return jjStopAtPos(0, 15);
      case 46:
         return jjStopAtPos(0, 19);
      case 91:
         return jjStopAtPos(0, 16);
      case 92:
         return jjMoveStringLiteralDfa1_0(0x800L);
      case 93:
         return jjStopAtPos(0, 17);
      case 95:
         return jjStopAtPos(0, 18);
      default :
      debugStream.println("   No string literal matches possible.");
         return jjMoveNfa_0(0, 0);
   }
}
private final int jjMoveStringLiteralDfa1_0(long active0)
{
   if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
      debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
   debugStream.println("   Possible string literal matches : { "
 + 
         jjKindsForBitVector(0, active0)  + " } ");
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
         debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
      return 1;
   }
   debugStream.println("Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int)curChar + ") at line " + input_stream.getLine() + " column " + input_stream.getColumn());
   switch(curChar)
   {
      case 10:
         if ((active0 & 0x20L) != 0L)
            return jjStopAtPos(1, 5);
         break;
      case 43:
         if ((active0 & 0x800L) != 0L)
            return jjStopAtPos(1, 11);
         break;
      default :
      debugStream.println("   No string literal matches possible.");
         break;
   }
   return jjStartNfa_0(0, active0);
}
private final void jjCheckNAdd(int state)
{
   if (jjrounds[state] != jjround)
   {
      jjstateSet[jjnewStateCnt++] = state;
      jjrounds[state] = jjround;
   }
}
private final void jjAddStates(int start, int end)
{
   do {
      jjstateSet[jjnewStateCnt++] = jjnextStates[start];
   } while (start++ != end);
}
private final void jjCheckNAddTwoStates(int state1, int state2)
{
   jjCheckNAdd(state1);
   jjCheckNAdd(state2);
}
private final void jjCheckNAddStates(int start, int end)
{
   do {
      jjCheckNAdd(jjnextStates[start]);
   } while (start++ != end);
}
private final void jjCheckNAddStates(int start)
{
   jjCheckNAdd(jjnextStates[start]);
   jjCheckNAdd(jjnextStates[start + 1]);
}
private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 10;
   int i = 1;
   jjstateSet[0] = startState;
      debugStream.println("   Starting NFA to match one of : " + jjKindsForStateVector(curLexState, jjstateSet, 0, 1));
      debugStream.println("Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int)curChar + ") at line " + input_stream.getLine() + " column " + input_stream.getColumn());
   int j, kind = 0x7fffffff;
   for (;;)
   {
      if (++jjround == 0x7fffffff)
         ReInitRounds();
      if (curChar < 64)
      {
         long l = 1L << curChar;
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 8)
                        kind = 8;
                  }
                  else if ((0xac0000000000L & l) != 0L)
                  {
                     if (kind > 12)
                        kind = 12;
                  }
                  else if (curChar == 60)
                     jjCheckNAdd(4);
                  else if (curChar == 58)
                     jjCheckNAdd(4);
                  else if (curChar == 36)
                  {
                     if (kind > 9)
                        kind = 9;
                  }
                  break;
               case 2:
                  if ((0x3ff000000000000L & l) != 0L)
                     kind = 8;
                  break;
               case 3:
                  if (curChar == 36)
                     kind = 9;
                  break;
               case 4:
                  if (curChar == 45 && kind > 10)
                     kind = 10;
                  break;
               case 5:
                  if (curChar == 58)
                     jjCheckNAdd(4);
                  break;
               case 6:
                  if (curChar == 60)
                     jjCheckNAdd(4);
                  break;
               case 7:
                  if ((0xac0000000000L & l) != 0L && kind > 12)
                     kind = 12;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else if (curChar < 128)
      {
         long l = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               case 0:
                  if ((0x7fffffeL & l) != 0L)
                  {
                     if (kind > 7)
                        kind = 7;
                  }
                  else if ((0x7fffffe00000000L & l) != 0L)
                  {
                     if (kind > 6)
                        kind = 6;
                  }
                  else if (curChar == 64)
                  {
                     if (kind > 9)
                        kind = 9;
                  }
                  if (curChar == 105)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 1:
                  if ((0x7fffffeL & l) != 0L)
                     kind = 7;
                  break;
               case 3:
                  if (curChar == 64)
                     kind = 9;
                  break;
               case 8:
                  if (curChar == 115 && kind > 12)
                     kind = 12;
                  break;
               case 9:
                  if (curChar == 105)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               default : break;
            }
         } while(i != startsAt);
      }
      else
      {
         int i2 = (curChar & 0xff) >> 6;
         long l2 = 1L << (curChar & 077);
         MatchLoop: do
         {
            switch(jjstateSet[--i])
            {
               default : break;
            }
         } while(i != startsAt);
      }
      if (kind != 0x7fffffff)
      {
         jjmatchedKind = kind;
         jjmatchedPos = curPos;
         kind = 0x7fffffff;
      }
      ++curPos;
      if (jjmatchedKind != 0 && jjmatchedKind != 0x7fffffff)
         debugStream.println("   Currently matched the first " + (jjmatchedPos + 1) + " characters as a " + tokenImage[jjmatchedKind] + " token.");
      if ((i = jjnewStateCnt) == (startsAt = 10 - (jjnewStateCnt = startsAt)))
         return curPos;
      debugStream.println("   Possible kinds of longer matches : " + jjKindsForStateVector(curLexState, jjstateSet, startsAt, i));
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
      debugStream.println("Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int)curChar + ") at line " + input_stream.getLine() + " column " + input_stream.getColumn());
   }
}
static final int[] jjnextStates = {
};
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, null, null, null, "\134\53", 
null, "\50", "\51", "\54", "\133", "\135", "\137", "\56", "\47", "\42", };
public static final String[] lexStateNames = {
   "DEFAULT", 
};
static final long[] jjtoToken = {
   0x3fffc1L, 
};
static final long[] jjtoSkip = {
   0x3eL, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[10];
private final int[] jjstateSet = new int[20];
protected char curChar;
public PrologParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}
public PrologParserTokenManager(SimpleCharStream stream, int lexState){
   this(stream);
   SwitchTo(lexState);
}
public void ReInit(SimpleCharStream stream)
{
   jjmatchedPos = jjnewStateCnt = 0;
   curLexState = defaultLexState;
   input_stream = stream;
   ReInitRounds();
}
private final void ReInitRounds()
{
   int i;
   jjround = 0x80000001;
   for (i = 10; i-- > 0;)
      jjrounds[i] = 0x80000000;
}
public void ReInit(SimpleCharStream stream, int lexState)
{
   ReInit(stream);
   SwitchTo(lexState);
}
public void SwitchTo(int lexState)
{
   if (lexState >= 1 || lexState < 0)
      throw new TokenMgrError("Error: Ignoring invalid lexical state : " + lexState + ". State unchanged.", TokenMgrError.INVALID_LEXICAL_STATE);
   else
      curLexState = lexState;
}

protected Token jjFillToken()
{
   Token t = Token.newToken(jjmatchedKind);
   t.kind = jjmatchedKind;
   String im = jjstrLiteralImages[jjmatchedKind];
   t.image = (im == null) ? input_stream.GetImage() : im;
   t.beginLine = input_stream.getBeginLine();
   t.beginColumn = input_stream.getBeginColumn();
   t.endLine = input_stream.getEndLine();
   t.endColumn = input_stream.getEndColumn();
   return t;
}

int curLexState = 0;
int defaultLexState = 0;
int jjnewStateCnt;
int jjround;
int jjmatchedPos;
int jjmatchedKind;

public Token getNextToken() 
{
  int kind;
  Token specialToken = null;
  Token matchedToken;
  int curPos = 0;

  EOFLoop :
  for (;;)
  {   
   try   
   {     
      curChar = input_stream.BeginToken();
   }     
   catch(java.io.IOException e)
   {        
      debugStream.println("Returning the <EOF> token.");
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 32 && (0x100000600L & (1L << curChar)) != 0L)
   {
      debugStream.println("Skipping character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int)curChar + ")");
         curChar = input_stream.BeginToken();
   }
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
      debugStream.println("Current character : " + TokenMgrError.addEscapes(String.valueOf(curChar)) + " (" + (int)curChar + ") at line " + input_stream.getLine() + " column " + input_stream.getColumn());
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
      {
         debugStream.println("   Putting back " + (curPos - jjmatchedPos - 1) + " characters into the input stream.");
         input_stream.backup(curPos - jjmatchedPos - 1);
      }
    debugStream.println("****** FOUND A " + tokenImage[jjmatchedKind] + " MATCH (" + TokenMgrError.addEscapes(new String(input_stream.GetSuffix(jjmatchedPos + 1))) + ") ******\n");
      if ((jjtoToken[jjmatchedKind >> 6] & (1L << (jjmatchedKind & 077))) != 0L)
      {
         matchedToken = jjFillToken();
         return matchedToken;
      }
      else
      {
         continue EOFLoop;
      }
   }
   int error_line = input_stream.getEndLine();
   int error_column = input_stream.getEndColumn();
   String error_after = null;
   boolean EOFSeen = false;
   try { input_stream.readChar(); input_stream.backup(1); }
   catch (java.io.IOException e1) {
      EOFSeen = true;
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
      if (curChar == '\n' || curChar == '\r') {
         error_line++;
         error_column = 0;
      }
      else
         error_column++;
   }
   if (!EOFSeen) {
      input_stream.backup(1);
      error_after = curPos <= 1 ? "" : input_stream.GetImage();
   }
   throw new TokenMgrError(EOFSeen, curLexState, error_line, error_column, error_after, curChar, TokenMgrError.LEXICAL_ERROR);
  }
}

protected static final int[][][] statesForState = {
 {
   { 0, 1, 2, 3, 5, 6, 7, 9, },
   { 0, 1, 2, 3, 5, 6, 7, 9, },
   { 0, 1, 2, 3, 5, 6, 7, 9, },
   { 0, 1, 2, 3, 5, 6, 7, 9, },
   { 4 }, 
   { 0, 1, 2, 3, 5, 6, 7, 9, },
   { 0, 1, 2, 3, 5, 6, 7, 9, },
   { 0, 1, 2, 3, 5, 6, 7, 9, },
   { 8 }, 
   { 0, 1, 2, 3, 5, 6, 7, 9, },
 },

};
protected static final int[][] kindForState = {
{ 6, 7, 8, 9, 10, 10, 10, 12, 12, 12}
};
   int kindCnt = 0;
  protected  final String jjKindsForBitVector(int i, long vec)
  {
    String retVal = "";
    if (i == 0)
       kindCnt = 0;
    for (int j = 0; j < 64; j++)
    {
       if ((vec & (1L << j)) != 0L)
       {
          if (kindCnt++ > 0)
             retVal += ", ";
          if (kindCnt % 5 == 0)
             retVal += "\n     ";
          retVal += tokenImage[i * 64 + j];
       }
    }
    return retVal;
  }

  protected  final String jjKindsForStateVector(int lexState, int[] vec, int start, int end)
  {
    boolean[] kindDone = new boolean[22];
    String retVal = "";
    int cnt = 0;
    for (int i = start; i < end; i++)
    {
     if (vec[i] == -1)
       continue;
     int[] stateSet = statesForState[curLexState][vec[i]];
     for (int j = 0; j < stateSet.length; j++)
     {
       int state = stateSet[j];
       if (!kindDone[kindForState[lexState][state]])
       {
          kindDone[kindForState[lexState][state]] = true;
          if (cnt++ > 0)
             retVal += ", ";
          if (cnt % 5 == 0)
             retVal += "\n     ";
          retVal += tokenImage[kindForState[lexState][state]];
       }
     }
    }
    if (cnt == 0)
       return "{  }";
    else
       return "{ " + retVal + " }";
  }

}
