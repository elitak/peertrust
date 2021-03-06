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

package org.peertrust.parser.prolog;

import java.util.Vector;

/**
 * <p>
 * This class is used for semantic analyze of tokens returned by
 * Prolog parser and converting them to PeerTrust syntax
 * </p><p>
 * $Id: PrologSemanticAnalyzer.java,v 1.2 2005/08/07 12:06:52 dolmedilla Exp $
 * <br/>
 * Date: 15-Aug-2004
 * <br/>
 * Last changed: $Date: 2005/08/07 12:06:52 $
 * by $Author: dolmedilla $
 * </p>
 * @author Bogdan Vlasenko
 */
public class PrologSemanticAnalyzer
{

// state constants
final static int HEAD = 0;
final static int BODY = 1;
final static int GUARDS = 2;
final static int SIGNATURE = 3;
final static int EXTRA_BODY = 4;
final static int END = 5;
final static int BEFORE_HEAD = 6;

/**
 * This function converts sequence of tokens to PrologRules
 *
 * @param tokens Vector of tokens from parser
 * @return Vector of PrologRules
 */
public static Vector analyze(Vector tokens)
{
    int newRule = 1;
    int state = BEFORE_HEAD;
    PrologRule rule = new PrologRule();
    int bracketsDeepness = 0;
    Vector rules = new Vector(0, 1);
    Vector convertedRules = new Vector(0, 1);
    for( int i = 0; i < tokens.capacity(); i++ )
    {
        InputToken token = (InputToken) tokens.elementAt(i);
        if( newRule == 1 )
        {
            rule = new PrologRule();
            newRule = 0;
            state = BEFORE_HEAD;
            bracketsDeepness = 0;
        }
        if( token.image.equals("(") ) bracketsDeepness = bracketsDeepness + 1;
        if( token.image.equals(")") ) bracketsDeepness = bracketsDeepness - 1;
        switch(state)
        {
            case BEFORE_HEAD:
                {
                    if( token.image.equals("signed") )
                    {
                        rule.signedRule = 1;
                        i += 3;
                        state = HEAD;
                    }
                    else
                    {
                        if( token.image.equals("rule") )
                        {
                            state = HEAD;
                            i += 1;
                        }
                        else // policy
                        {
                            rule.policyRule = 1;
                            state = HEAD;
                            i += 1;
                        }
                    }
                }; break;
            case HEAD:
            {
                if( token.image.equals("[") && (bracketsDeepness == 0 ))
                {
                    if( rule.policyRule == 0 ) state = GUARDS;
                    else                       state = EXTRA_BODY;
                }
                else
                {
                    if( !((InputToken) tokens.elementAt(i+1)).image.equals("[") || (bracketsDeepness != 0 ) )
                    {
                        rule.head = rule.head + token.image;
                    }
                }
            }; break;
            case GUARDS:
            {
                if( token.image.equals("]") && (bracketsDeepness == 0 ))
                {
                    state = BODY;
                    i += 2;
                }
                else
                {
                    if( !token.image.equals("[") || (bracketsDeepness != 0 ) )
                    {
                        rule.guards = rule.guards + token.image;
                    }
                }
            }; break;
            case EXTRA_BODY:
            {
                if( token.image.equals("]") && (bracketsDeepness == 0 ))
                {
                    state = END;
                }
                else
                {
                    if( !token.image.equals("[") || (bracketsDeepness != 0 ))
                    {
                        rule.extraBody = rule.extraBody + token.image;
                    }
                }
            }; break;
            case BODY:
            {
                if( token.image.equals("]")  && (bracketsDeepness == 0 ))
                {
                    if( rule.signedRule == 1 )
                    {
                        state = SIGNATURE;
                        i += 2;
                    }
                    else
                    {
                        state = END;
                    }
                }
                else
                {
                    if( !token.image.equals("[") || (bracketsDeepness != 0 ))
                    {
                        rule.body = rule.body + token.image;
                    }
                }
            }; break;
            case END:
            {
                if( token.image.equals(".") )
                {
                    rules.add(rule);
                    newRule = 1;
                }
            }; break;
            case SIGNATURE:
            {
                if( token.image.equals(",") )
                {
                    state = END;
                }
                else
                {
                    rule.signature = rule.signature + token.image;
                }
            }; break;
            default: break;
        }
    }
    convertedRules = convertRules(rules);
    return(convertedRules);
}
/**
 * This function converts PrologRules to Peer Trust rules
 *
 * @param rules Vector of PrologRules
 * @return Vector of PeerTrust rules
 */
private static Vector convertRules(Vector rules)
{
    Vector convertedRules = new Vector(0, 1);
    for( int i = 0; i < rules.capacity(); i++ )
    {
        PrologRule rule = (PrologRule) rules.elementAt(i);
        String convertedRule = new String();
        if( (rule.signedRule == 0) && (rule.policyRule == 0) )
        {
            if( rule.body.equals("") ) convertedRule = rule.head + ".";
            else
            {
                if( rule.guards.equals("") ) convertedRule = rule.head + " <- " + rule.body + ".";
                else convertedRule = rule.head + " <- " + rule.guards + " | " + rule.body + ".";
            }
        }
        else
        {
            if( rule.policyRule == 0 ) // ignore all policy strings
            {
                PrologRule anotherRule = (PrologRule) rules.elementAt(i);
                for( int j = 0; j < rules.capacity(); j++ ) // searching for policy string
                {
                    anotherRule = (PrologRule) rules.elementAt(j);
                    if( anotherRule.head.equals(rule.head) && (anotherRule.policyRule == 1) ) break;
                }
                if( anotherRule.head.equals(rule.head) && (anotherRule.policyRule == 1) )
                {
                    if( rule.guards.equals( "" ) ) convertedRule = rule.
                        head + " <- " + anotherRule.extraBody + "signedBy[" +
                        rule.signature + "] " + rule.body + ".";
                    else convertedRule = rule.head + " <- " + anotherRule.extraBody +
                        "signedBy[" + rule.signature + "] " + rule.guards + " | " + rule.body + ".";
                }
                else // no policy string, just signed rule
                {
                    if( rule.body.equals("") && rule.guards.equals("") )
                    {
                        convertedRule = rule.head + " signedBy[" + rule.signature + "]" + ".";
                    }
                    else
                    {
                        if( rule.guards.equals( "" ) ) convertedRule = rule.
                            head + " <- " + "signedBy[" +
                            rule.signature + "]" + rule.body + ".";
                        else convertedRule = rule.head + " <- " + "signedBy[" + rule.signature
                            + "]" + rule.guards + " | " + rule.body + ".";
                    }
                }
            }
        }
        if( !convertedRule.equals("") ) convertedRules.add(convertedRule);
    }
    return(convertedRules);
}

}
