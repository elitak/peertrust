/**
 * Copyright 2004
 * 
 * This file is part of Peertrust.
 * 
 * Peertrust is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * Peertrust is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with Peertrust; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
*/

package test.org.peertrust.parser.peertrust;

/**
 * This class is used for testing of PeerTrust parser and convertor
 *
 * @author Bogdan Vlasenko
 */

import java.util.Vector;

import org.peertrust.parser.peertrust.ParseException;
import org.peertrust.parser.peertrust.PeerTrustGrammar;

public class PeerTrustTest
{
    public static void main(String args[])
    throws ParseException, java.io.FileNotFoundException, java.io.IOException
    {
        testJustHead();
        testHeadAndSimpleBody();
        testSigned();
        testRulesWithPolicy();
    }
    private static void testJustHead()
    throws ParseException, java.io.FileNotFoundException, java.io.IOException
    {
        boolean testError = false;
        String peerTrustRule = "head(X).";
        String convertedRule = "rule( head(X), [], [] ).";
        Vector result = new Vector(0, 1);

        System.out.println("testJustHead running: ");

        result = PeerTrustGrammar.SyntaxCheck(peerTrustRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testJustHead!");
            System.out.println("Sent           : " + peerTrustRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }

        // next Test case
        peerTrustRule = "head(Y)  @ i  $r .";
        convertedRule = "rule( head(Y)@i$r, [], [] ).";

        result = PeerTrustGrammar.SyntaxCheck(peerTrustRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testJustHead!");
            System.out.println("Sent           : " + peerTrustRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }

        // next Test case
        peerTrustRule = "head(Z)  @ I  $R <-.";
        convertedRule = "rule( head(Z)@I$R, [], [] ).";

        result = PeerTrustGrammar.SyntaxCheck(peerTrustRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testJustHead!");
            System.out.println("Sent           : " + peerTrustRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }
        if( !testError ) System.out.println("testJustHead ok!");
    }
    private static void testHeadAndSimpleBody()
    throws ParseException, java.io.FileNotFoundException, java.io.IOException
    {
        boolean testError = false;
        String peerTrustRule = "head(X) <-  lb(), h(Y),  n(),      s(Y, [1,2,3]).";
        String convertedRule = "rule( head(X), [], [lb(),h(Y),n(),s(Y,[1,2,3])] ).";
        Vector result = new Vector(0, 1);

        System.out.println("testHeadAndSimpleBody running: ");

        result = PeerTrustGrammar.SyntaxCheck(peerTrustRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testHeadAndSimpleBody!");
            System.out.println("Sent           : " + peerTrustRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }
        // next Test case
        peerTrustRule = "h()@I$R<-g(), g2(a, [b, c])|  d(f, e).";
        convertedRule = "rule( h()@I$R, [g(),g2(a,[b,c])], [d(f,e)] ).";

        result = PeerTrustGrammar.SyntaxCheck(peerTrustRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testHeadAndSimpleBody!");
            System.out.println("Sent           : " + peerTrustRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }
        if( !testError ) System.out.println("testHeadAndSimpleBody ok!");
    }
    private static void testSigned()
    throws ParseException, java.io.FileNotFoundException, java.io.IOException
    {
        boolean testError = false;
        String peerTrustRule = "h(x) signedBy[123].";
        String convertedRule = "signed( rule( h(x), [], [] ), 123, signature( 123 ) ).";
        Vector result = new Vector(0, 1);

        System.out.println("testSigned running: ");

        result = PeerTrustGrammar.SyntaxCheck(peerTrustRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testSigned!");
            System.out.println("Sent           : " + peerTrustRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }

        // next Test case
        peerTrustRule = "h(x)  <- signedBy[321].";
        convertedRule = "signed( rule( h(x), [], [] ), 321, signature( 321 ) ).";

        result = PeerTrustGrammar.SyntaxCheck(peerTrustRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testSigned!");
            System.out.println("Sent           : " + peerTrustRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }
        // next Test case
        peerTrustRule = "h(z)  <- signedBy[3] body()  @i$r.";
        convertedRule = "signed( rule( h(z), [], [body()@i$r] ), 3, signature( 3 ) ).";

        result = PeerTrustGrammar.SyntaxCheck(peerTrustRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testSigned!");
            System.out.println("Sent           : " + peerTrustRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }
        // next Test case
        peerTrustRule = "h(Z)  <- signedBy[s] sG()  @i$r |sB().";
        convertedRule = "signed( rule( h(Z), [sG()@i$r], [sB()] ), s, signature( s ) ).";

        result = PeerTrustGrammar.SyntaxCheck(peerTrustRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testSigned!");
            System.out.println("Sent           : " + peerTrustRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }
        // next Test case
        peerTrustRule = "h(Z_S)  <- signedBy[s_d_w] sG()  @i$r |sB().";
        convertedRule = "signed( rule( h(Z_S), [sG()@i$r], [sB()] ), s_d_w, signature( s_d_w ) ).";

        result = PeerTrustGrammar.SyntaxCheck(peerTrustRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testSigned!");
            System.out.println("Sent           : " + peerTrustRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }
        if( !testError ) System.out.println("testSigned ok!");
    }
    private static void testRulesWithPolicy()
    throws ParseException, java.io.FileNotFoundException, java.io.IOException
    {
        boolean testError = false;
        String peerTrustRule = "h()<-lb(12, fg) signedBy[someBody] sg() | sb(54)  .";
        String convertedRule1 = "signed( rule( h(), [sg()], [sb(54)] ), someBody, signature( someBody ) ).";
        String convertedRule2 = "policy( h(), [lb(12,fg)] ).";
        Vector result = new Vector(0, 1);

        System.out.println("testRulesWithPolicy running: ");

        result = PeerTrustGrammar.SyntaxCheck(peerTrustRule);
        if( convertedRule1.equals((String)result.elementAt(0)) && convertedRule2.equals((String)result.elementAt(1)))
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testRulesWithPolicy!");
            System.out.println("Sent            : " + peerTrustRule);
            System.out.println("Expected result1: " + convertedRule1);
            System.out.println("Gotten result1  : " + (String)result.elementAt(0));
            System.out.println("Expected result2: " + convertedRule1);
            System.out.println("Gotten result2  : " + (String)result.elementAt(1));
            testError = true;
        }

        // next Test case
        peerTrustRule = "h()<-lb(12, fg) | signedBy[someBody] sg() | sb(54)  .";
        convertedRule1 = "signed( rule( h(), [sg()], [sb(54)] ), someBody, signature( someBody ) ).";
        convertedRule2 = "policy( h(), [lb(12,fg)] ).";
        result = new Vector(0, 1);

        result = PeerTrustGrammar.SyntaxCheck(peerTrustRule);
        if( convertedRule1.equals((String)result.elementAt(0)) && convertedRule2.equals((String)result.elementAt(1)))
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testRulesWithPolicy!");
            System.out.println("Sent            : " + peerTrustRule);
            System.out.println("Expected result1: " + convertedRule1);
            System.out.println("Gotten result1  : " + (String)result.elementAt(0));
            System.out.println("Expected result2: " + convertedRule1);
            System.out.println("Gotten result2  : " + (String)result.elementAt(1));
            testError = true;
        }
        // next Test case
        peerTrustRule = "p(X,Z,a,T)<-z(X,Z,a), s(Y,[1,2,3]) signedBy [t] f(X, T) | g(X, Z, T).";
        convertedRule1 = "signed( rule( p(X,Z,a,T), [f(X,T)], [g(X,Z,T)] ), t, signature( t ) ).";
        convertedRule2 = "policy( p(X,Z,a,T), [z(X,Z,a),s(Y,[1,2,3])] ).";
        result = new Vector(0, 1);

        result = PeerTrustGrammar.SyntaxCheck(peerTrustRule);
        if( convertedRule1.equals((String)result.elementAt(0)) && convertedRule2.equals((String)result.elementAt(1)))
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testRulesWithPolicy!");
            System.out.println("Sent            : " + peerTrustRule);
            System.out.println("Expected result1: " + convertedRule1);
            System.out.println("Gotten result1  : " + (String)result.elementAt(0));
            System.out.println("Expected result2: " + convertedRule1);
            System.out.println("Gotten result2  : " + (String)result.elementAt(1));
            testError = true;
        }

        if( !testError ) System.out.println("testRulesWithPolicy ok!");
    }
}
