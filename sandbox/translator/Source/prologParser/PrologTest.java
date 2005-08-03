/**
 * This class is used for testing of Prolog parser and convertor
 *
 * @author Bogdan Vlasenko
 */

import java.util.Vector;

public class PrologTest
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
        String prologRule ="rule( head(X), [], [] ).";
        String convertedRule = "head(X).";
        Vector result = new Vector(0, 1);

        System.out.println("testJustHead running: ");

        result = PrologGrammar.SyntaxCheck(prologRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testJustHead!");
            System.out.println("Sent           : " + prologRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }

        // next Test case
        prologRule = "rule( head(Y)@i$r, [], [] ).";
        convertedRule = "head(Y)@i$r.";

        result = PrologGrammar.SyntaxCheck(prologRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testJustHead!");
            System.out.println("Sent           : " + prologRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }

        // next Test case
        prologRule = "rule( head(Z)@I$R, [], [] ).";
        convertedRule = "head(Z)@I$R.";

        result = PrologGrammar.SyntaxCheck(prologRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testJustHead!");
            System.out.println("Sent           : " + prologRule);
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
        String prologRule = "rule( head(X), [], [lb(),h(Y),n(),s(Y,[1,2,3])] ).";
        String convertedRule = "head(X) <- lb(),h(Y),n(),s(Y,[1,2,3]).";
        Vector result = new Vector(0, 1);

        System.out.println("testHeadAndSimpleBody running: ");

        result = PrologGrammar.SyntaxCheck(prologRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testHeadAndSimpleBody!");
            System.out.println("Sent           : " + prologRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }
        // next Test case
        prologRule = "rule( h()@I$R, [g(),g2(a,[b,c])], [d(f,e)] ).";
        convertedRule = "h()@I$R <- g(),g2(a,[b,c]) | d(f,e).";

        result = PrologGrammar.SyntaxCheck(prologRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testHeadAndSimpleBody!");
            System.out.println("Sent           : " + prologRule);
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
        String prologRule = "signed( rule( h(x), [], [] ), 123, signature( 123 ) ).";
        String convertedRule = "h(x) signedBy[123].";
        Vector result = new Vector(0, 1);

        System.out.println("testSigned running: ");

        result = PrologGrammar.SyntaxCheck(prologRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testSigned!");
            System.out.println("Sent           : " + prologRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }

        // next Test case
        prologRule = "signed( rule( h(x), [], [] ), 321, signature( 321 ) ).";
        convertedRule = "h(x) signedBy[321].";

        result = PrologGrammar.SyntaxCheck(prologRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testSigned!");
            System.out.println("Sent           : " + prologRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }

        // next Test case
        prologRule = "signed( rule( h(z), [], [body()@i$r] ), 3, signature( 3 ) ).";
        convertedRule = "h(z) <- signedBy[3]body()@i$r.";

        result = PrologGrammar.SyntaxCheck(prologRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testSigned!");
            System.out.println("Sent           : " + prologRule);
            System.out.println("Expected result: " + convertedRule);
            System.out.println("Gotten result  : " + (String)result.elementAt(0));
            testError = true;
        }
        // next Test case
        prologRule = "signed( rule( h(Z), [sG()@i$r], [sB()] ), s, signature( s ) ).";
        convertedRule = "h(Z) <- signedBy[s]sG()@i$r | sB().";

        result = PrologGrammar.SyntaxCheck(prologRule);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testSigned!");
            System.out.println("Sent           : " + prologRule);
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
        String prologRule1 = "signed( rule( h(), [sg()], [sb(54)] ), someBody, signature( someBody ) ).";
        String prologRule2 = "policy( h(), [lb(12,fg)] ).";
        String convertedRule = "h() <- lb(12,fg)signedBy[someBody] sg() | sb(54).";
        Vector result = new Vector(0, 1);

        System.out.println("testRulesWithPolicy running: ");

        result = PrologGrammar.SyntaxCheck(prologRule1, prologRule2);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testRulesWithPolicy!");
            System.out.println("Sent1           : " + prologRule1);
            System.out.println("Sent2           : " + prologRule2);
            System.out.println("Expected result : " + convertedRule);
            System.out.println("Gotten result   : " + (String)result.elementAt(0));
            testError = true;
        }

        // next Test case
        prologRule1 = "signed( rule( p(X,Z,a,T), [f(X,T)], [g(X,Z,T)] ), t, signature( t ) ).";
        prologRule2 = "policy( p(X,Z,a,T), [z(X,Z,a),s(Y,[1,2,3])] ).";
        convertedRule = "p(X,Z,a,T) <- z(X,Z,a),s(Y,[1,2,3])signedBy[t] f(X,T) | g(X,Z,T).";
        result = new Vector(0, 1);

        result = PrologGrammar.SyntaxCheck(prologRule1, prologRule2);
        if( convertedRule.equals((String)result.elementAt(0)) )
        {
            System.out.print(".");
        }
        else
        {
            System.out.println("ERROR in testRulesWithPolicy!");
            System.out.println("Sent1           : " + prologRule1);
            System.out.println("Sent2           : " + prologRule2);
            System.out.println("Expected result : " + convertedRule);
            System.out.println("Gotten result   : " + (String)result.elementAt(0));
            testError = true;
        }

        if( !testError ) System.out.println("testRulesWithPolicy ok!");
    }
}
