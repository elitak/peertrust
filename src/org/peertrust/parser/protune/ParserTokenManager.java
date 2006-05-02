/* Generated By:JavaCC: Do not edit this line. ParserTokenManager.java */
package org.peertrust.parser.protune;
import java.io.*;
import java.util.*;

public class ParserTokenManager implements ParserConstants
{
  public  java.io.PrintStream debugStream = System.out;
  public  void setDebugStream(java.io.PrintStream ds) { debugStream = ds; }
private final int jjStopStringLiteralDfa_0(int pos, long active0)
{
   switch (pos)
   {
      case 0:
         if ((active0 & 0x10000000L) != 0L)
            return 16;
         if ((active0 & 0x2L) != 0L)
            return 8;
         if ((active0 & 0x1e00L) != 0L)
         {
            jjmatchedKind = 14;
            return 39;
         }
         return -1;
      case 1:
         if ((active0 & 0x600L) != 0L)
            return 39;
         if ((active0 & 0x1800L) != 0L)
         {
            if (jjmatchedPos != 1)
            {
               jjmatchedKind = 14;
               jjmatchedPos = 1;
            }
            return 39;
         }
         return -1;
      case 2:
         if ((active0 & 0x1a00L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 2;
            return 39;
         }
         return -1;
      case 3:
         if ((active0 & 0x1a00L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 3;
            return 39;
         }
         return -1;
      case 4:
         if ((active0 & 0x1a00L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 4;
            return 39;
         }
         return -1;
      case 5:
         if ((active0 & 0x1a00L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 5;
            return 39;
         }
         return -1;
      case 6:
         if ((active0 & 0x200L) != 0L)
            return 39;
         if ((active0 & 0x1800L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 6;
            return 39;
         }
         return -1;
      case 7:
         if ((active0 & 0x1800L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 7;
            return 39;
         }
         return -1;
      case 8:
         if ((active0 & 0x1800L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 8;
            return 39;
         }
         return -1;
      case 9:
         if ((active0 & 0x1000L) != 0L)
            return 39;
         if ((active0 & 0x800L) != 0L)
         {
            jjmatchedKind = 14;
            jjmatchedPos = 9;
            return 39;
         }
         return -1;
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
   return pos + 1;
}
private final int jjStartNfaWithStates_0(int pos, int kind, int state)
{
   jjmatchedKind = kind;
   jjmatchedPos = pos;
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) { return pos + 1; }
   return jjMoveNfa_0(state, pos + 1);
}
private final int jjMoveStringLiteralDfa0_0()
{
   switch(curChar)
   {
      case 32:
         return jjStartNfaWithStates_0(0, 1, 8);
      case 40:
         return jjStopAtPos(0, 25);
      case 41:
         return jjStopAtPos(0, 26);
      case 44:
         return jjStopAtPos(0, 27);
      case 46:
         return jjStopAtPos(0, 22);
      case 58:
         return jjStartNfaWithStates_0(0, 28, 16);
      case 91:
         return jjStopAtPos(0, 23);
      case 93:
         return jjStopAtPos(0, 24);
      case 99:
         return jjMoveStringLiteralDfa1_0(0x1000L);
      case 100:
         return jjMoveStringLiteralDfa1_0(0x800L);
      case 105:
         return jjMoveStringLiteralDfa1_0(0x600L);
      default :
         return jjMoveNfa_0(0, 0);
   }
}
private final int jjMoveStringLiteralDfa1_0(long active0)
{
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(0, active0);
      return 1;
   }
   switch(curChar)
   {
      case 101:
         return jjMoveStringLiteralDfa2_0(active0, 0x800L);
      case 110:
         if ((active0 & 0x400L) != 0L)
         {
            jjmatchedKind = 10;
            jjmatchedPos = 1;
         }
         return jjMoveStringLiteralDfa2_0(active0, 0x200L);
      case 114:
         return jjMoveStringLiteralDfa2_0(active0, 0x1000L);
      default :
         break;
   }
   return jjStartNfa_0(0, active0);
}
private final int jjMoveStringLiteralDfa2_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(0, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(1, active0);
      return 2;
   }
   switch(curChar)
   {
      case 99:
         return jjMoveStringLiteralDfa3_0(active0, 0xa00L);
      case 101:
         return jjMoveStringLiteralDfa3_0(active0, 0x1000L);
      default :
         break;
   }
   return jjStartNfa_0(1, active0);
}
private final int jjMoveStringLiteralDfa3_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(1, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(2, active0);
      return 3;
   }
   switch(curChar)
   {
      case 100:
         return jjMoveStringLiteralDfa4_0(active0, 0x1000L);
      case 108:
         return jjMoveStringLiteralDfa4_0(active0, 0xa00L);
      default :
         break;
   }
   return jjStartNfa_0(2, active0);
}
private final int jjMoveStringLiteralDfa4_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(2, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(3, active0);
      return 4;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa5_0(active0, 0x800L);
      case 101:
         return jjMoveStringLiteralDfa5_0(active0, 0x1000L);
      case 117:
         return jjMoveStringLiteralDfa5_0(active0, 0x200L);
      default :
         break;
   }
   return jjStartNfa_0(3, active0);
}
private final int jjMoveStringLiteralDfa5_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(3, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(4, active0);
      return 5;
   }
   switch(curChar)
   {
      case 100:
         return jjMoveStringLiteralDfa6_0(active0, 0x200L);
      case 110:
         return jjMoveStringLiteralDfa6_0(active0, 0x1000L);
      case 114:
         return jjMoveStringLiteralDfa6_0(active0, 0x800L);
      default :
         break;
   }
   return jjStartNfa_0(4, active0);
}
private final int jjMoveStringLiteralDfa6_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(4, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(5, active0);
      return 6;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa7_0(active0, 0x800L);
      case 101:
         if ((active0 & 0x200L) != 0L)
            return jjStartNfaWithStates_0(6, 9, 39);
         break;
      case 116:
         return jjMoveStringLiteralDfa7_0(active0, 0x1000L);
      default :
         break;
   }
   return jjStartNfa_0(5, active0);
}
private final int jjMoveStringLiteralDfa7_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(5, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(6, active0);
      return 7;
   }
   switch(curChar)
   {
      case 105:
         return jjMoveStringLiteralDfa8_0(active0, 0x1000L);
      case 116:
         return jjMoveStringLiteralDfa8_0(active0, 0x800L);
      default :
         break;
   }
   return jjStartNfa_0(6, active0);
}
private final int jjMoveStringLiteralDfa8_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(6, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(7, active0);
      return 8;
   }
   switch(curChar)
   {
      case 97:
         return jjMoveStringLiteralDfa9_0(active0, 0x1000L);
      case 105:
         return jjMoveStringLiteralDfa9_0(active0, 0x800L);
      default :
         break;
   }
   return jjStartNfa_0(7, active0);
}
private final int jjMoveStringLiteralDfa9_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(7, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(8, active0);
      return 9;
   }
   switch(curChar)
   {
      case 108:
         if ((active0 & 0x1000L) != 0L)
            return jjStartNfaWithStates_0(9, 12, 39);
         break;
      case 111:
         return jjMoveStringLiteralDfa10_0(active0, 0x800L);
      default :
         break;
   }
   return jjStartNfa_0(8, active0);
}
private final int jjMoveStringLiteralDfa10_0(long old0, long active0)
{
   if (((active0 &= old0)) == 0L)
      return jjStartNfa_0(8, old0); 
   try { curChar = input_stream.readChar(); }
   catch(java.io.IOException e) {
      jjStopStringLiteralDfa_0(9, active0);
      return 10;
   }
   switch(curChar)
   {
      case 110:
         if ((active0 & 0x800L) != 0L)
            return jjStartNfaWithStates_0(10, 11, 39);
         break;
      default :
         break;
   }
   return jjStartNfa_0(9, active0);
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
static final long[] jjbitVec0 = {
   0x0L, 0x0L, 0xffffffffffffffffL, 0xffffffffffffffffL
};
private final int jjMoveNfa_0(int startState, int curPos)
{
   int[] nextStates;
   int startsAt = 0;
   jjnewStateCnt = 39;
   int i = 1;
   jjstateSet[0] = startState;
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
               case 39:
               case 23:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 14)
                     kind = 14;
                  jjCheckNAdd(23);
                  break;
               case 0:
                  if ((0x3ff000000000000L & l) != 0L)
                  {
                     if (kind > 17)
                        kind = 17;
                     jjCheckNAdd(30);
                  }
                  else if ((0x7000000000000000L & l) != 0L)
                  {
                     if (kind > 6)
                        kind = 6;
                  }
                  else if (curChar == 47)
                     jjAddStates(0, 1);
                  else if (curChar == 34)
                     jjCheckNAddTwoStates(28, 29);
                  else if (curChar == 39)
                     jjCheckNAddTwoStates(25, 26);
                  else if (curChar == 58)
                     jjCheckNAdd(16);
                  else if (curChar == 32)
                     jjstateSet[jjnewStateCnt++] = 8;
                  else if (curChar == 33)
                     jjCheckNAdd(3);
                  else if (curChar == 37)
                  {
                     if (kind > 5)
                        kind = 5;
                     jjCheckNAdd(1);
                  }
                  if (curChar == 60)
                     jjCheckNAddTwoStates(3, 16);
                  else if (curChar == 62)
                     jjCheckNAdd(3);
                  break;
               case 1:
                  if ((0xffffffffffffdbffL & l) == 0L)
                     break;
                  if (kind > 5)
                     kind = 5;
                  jjCheckNAdd(1);
                  break;
               case 2:
                  if ((0x7000000000000000L & l) != 0L && kind > 6)
                     kind = 6;
                  break;
               case 3:
                  if (curChar == 61 && kind > 6)
                     kind = 6;
                  break;
               case 4:
                  if (curChar == 62)
                     jjCheckNAdd(3);
                  break;
               case 5:
                  if (curChar == 33)
                     jjCheckNAdd(3);
                  break;
               case 6:
                  if (curChar == 32 && kind > 6)
                     kind = 6;
                  break;
               case 9:
                  if (curChar == 32)
                     jjstateSet[jjnewStateCnt++] = 8;
                  break;
               case 10:
                  if (curChar == 32 && kind > 7)
                     kind = 7;
                  break;
               case 14:
                  if (curChar == 43 && kind > 7)
                     kind = 7;
                  break;
               case 16:
                  if (curChar == 45 && kind > 8)
                     kind = 8;
                  break;
               case 17:
                  if (curChar == 58)
                     jjCheckNAdd(16);
                  break;
               case 20:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 13)
                     kind = 13;
                  jjstateSet[jjnewStateCnt++] = 20;
                  break;
               case 24:
                  if (curChar == 39)
                     jjCheckNAddTwoStates(25, 26);
                  break;
               case 25:
                  if ((0xffffff7fffffdbffL & l) != 0L)
                     jjCheckNAddTwoStates(25, 26);
                  break;
               case 26:
                  if (curChar == 39 && kind > 16)
                     kind = 16;
                  break;
               case 27:
                  if (curChar == 34)
                     jjCheckNAddTwoStates(28, 29);
                  break;
               case 28:
                  if ((0xfffffffbffffdbffL & l) != 0L)
                     jjCheckNAddTwoStates(28, 29);
                  break;
               case 29:
                  if (curChar == 34 && kind > 16)
                     kind = 16;
                  break;
               case 30:
                  if ((0x3ff000000000000L & l) == 0L)
                     break;
                  if (kind > 17)
                     kind = 17;
                  jjCheckNAdd(30);
                  break;
               case 31:
                  if (curChar == 47)
                     jjAddStates(0, 1);
                  break;
               case 32:
                  if (curChar != 47)
                     break;
                  if (kind > 5)
                     kind = 5;
                  jjCheckNAdd(33);
                  break;
               case 33:
                  if ((0xffffffffffffdbffL & l) == 0L)
                     break;
                  if (kind > 5)
                     kind = 5;
                  jjCheckNAdd(33);
                  break;
               case 34:
                  if (curChar == 42)
                     jjCheckNAddTwoStates(35, 37);
                  break;
               case 35:
                  jjCheckNAddTwoStates(35, 37);
                  break;
               case 36:
                  if (curChar == 47 && kind > 5)
                     kind = 5;
                  break;
               case 37:
                  if (curChar == 42)
                     jjstateSet[jjnewStateCnt++] = 36;
                  break;
               case 38:
                  if (curChar == 60)
                     jjCheckNAddTwoStates(3, 16);
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
               case 39:
               case 22:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 14)
                     kind = 14;
                  jjCheckNAddTwoStates(22, 23);
                  break;
               case 0:
                  if ((0x87fffffeL & l) != 0L)
                  {
                     if (kind > 13)
                        kind = 13;
                     jjCheckNAddTwoStates(19, 20);
                  }
                  else if ((0x7fffffe00000000L & l) != 0L)
                  {
                     if (kind > 14)
                        kind = 14;
                     jjCheckNAddTwoStates(22, 23);
                  }
                  else if (curChar == 92)
                     jjstateSet[jjnewStateCnt++] = 14;
                  if (curChar == 110)
                     jjstateSet[jjnewStateCnt++] = 12;
                  break;
               case 1:
                  if (kind > 5)
                     kind = 5;
                  jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 7:
                  if (curChar == 115)
                     jjstateSet[jjnewStateCnt++] = 6;
                  break;
               case 8:
                  if (curChar == 105)
                     jjstateSet[jjnewStateCnt++] = 7;
                  break;
               case 11:
                  if (curChar == 116)
                     jjstateSet[jjnewStateCnt++] = 10;
                  break;
               case 12:
                  if (curChar == 111)
                     jjstateSet[jjnewStateCnt++] = 11;
                  break;
               case 13:
                  if (curChar == 110)
                     jjstateSet[jjnewStateCnt++] = 12;
                  break;
               case 15:
                  if (curChar == 92)
                     jjstateSet[jjnewStateCnt++] = 14;
                  break;
               case 18:
                  if ((0x87fffffeL & l) == 0L)
                     break;
                  if (kind > 13)
                     kind = 13;
                  jjCheckNAddTwoStates(19, 20);
                  break;
               case 19:
                  if ((0x7fffffe87fffffeL & l) == 0L)
                     break;
                  if (kind > 13)
                     kind = 13;
                  jjCheckNAddTwoStates(19, 20);
                  break;
               case 21:
                  if ((0x7fffffe00000000L & l) == 0L)
                     break;
                  if (kind > 14)
                     kind = 14;
                  jjCheckNAddTwoStates(22, 23);
                  break;
               case 25:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjAddStates(2, 3);
                  break;
               case 28:
                  if ((0xffffffffefffffffL & l) != 0L)
                     jjAddStates(4, 5);
                  break;
               case 33:
                  if (kind > 5)
                     kind = 5;
                  jjstateSet[jjnewStateCnt++] = 33;
                  break;
               case 35:
                  jjAddStates(6, 7);
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
               case 1:
                  if ((jjbitVec0[i2] & l2) == 0L)
                     break;
                  if (kind > 5)
                     kind = 5;
                  jjstateSet[jjnewStateCnt++] = 1;
                  break;
               case 25:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(2, 3);
                  break;
               case 28:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(4, 5);
                  break;
               case 33:
                  if ((jjbitVec0[i2] & l2) == 0L)
                     break;
                  if (kind > 5)
                     kind = 5;
                  jjstateSet[jjnewStateCnt++] = 33;
                  break;
               case 35:
                  if ((jjbitVec0[i2] & l2) != 0L)
                     jjAddStates(6, 7);
                  break;
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
      if ((i = jjnewStateCnt) == (startsAt = 39 - (jjnewStateCnt = startsAt)))
         return curPos;
      try { curChar = input_stream.readChar(); }
      catch(java.io.IOException e) { return curPos; }
   }
}
static final int[] jjnextStates = {
   32, 34, 25, 26, 28, 29, 35, 37, 
};
public static final String[] jjstrLiteralImages = {
"", null, null, null, null, null, null, null, null, 
"\151\156\143\154\165\144\145", "\151\156", "\144\145\143\154\141\162\141\164\151\157\156", 
"\143\162\145\144\145\156\164\151\141\154", null, null, null, null, null, null, null, null, null, "\56", "\133", "\135", 
"\50", "\51", "\54", "\72", };
public static final String[] lexStateNames = {
   "DEFAULT", 
};
static final long[] jjtoToken = {
   0x1fc37fc1L, 
};
static final long[] jjtoSkip = {
   0x3eL, 
};
protected SimpleCharStream input_stream;
private final int[] jjrounds = new int[39];
private final int[] jjstateSet = new int[78];
protected char curChar;
public ParserTokenManager(SimpleCharStream stream){
   if (SimpleCharStream.staticFlag)
      throw new Error("ERROR: Cannot use a static CharStream class with a non-static lexical analyzer.");
   input_stream = stream;
}
public ParserTokenManager(SimpleCharStream stream, int lexState){
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
   for (i = 39; i-- > 0;)
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
      jjmatchedKind = 0;
      matchedToken = jjFillToken();
      return matchedToken;
   }

   try { input_stream.backup(0);
      while (curChar <= 13 && (0x2600L & (1L << curChar)) != 0L)
         curChar = input_stream.BeginToken();
   }
   catch (java.io.IOException e1) { continue EOFLoop; }
   jjmatchedKind = 0x7fffffff;
   jjmatchedPos = 0;
   curPos = jjMoveStringLiteralDfa0_0();
   if (jjmatchedKind != 0x7fffffff)
   {
      if (jjmatchedPos + 1 < curPos)
         input_stream.backup(curPos - jjmatchedPos - 1);
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

}
