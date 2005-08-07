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

package org.peertrust.parser.peertrust;

/**
 * <p>
 * This class is used for semantic analyze of tokens returned by
 * PeerTrust parser and converting them to Prolog syntax
 * </p><p>
 * $Id: PeerTrustSemanticAnalyzer.java,v 1.2 2005/08/07 12:06:54 dolmedilla Exp $
 * <br/>
 * Date: 15-Aug-2004
 * <br/>
 * Last changed: $Date: 2005/08/07 12:06:54 $
 * by $Author: dolmedilla $
 * </p>
 * @author Bogdan Vlasenko
 */
import java.util.Vector;

public class PeerTrustSemanticAnalyzer
{

// state constants
final static int HEAD = 0;
final static int BODY = 1;
final static int SIGN_AFTER_HEAD = 2;
final static int AFTER_GUARDS = 3;
final static int AFTER_SIGNED_IN_BODY = 4;
final static int SIGNED_BODY = 5;
final static int SIGNED_BODY_AFTER_GUARDS = 6;

/**
 * This function converts sequence of tokens to PTRules
 *
 * @param tokens Vector of tokens from parser
 * @return Vector of PTRules
 */
public static Vector analyze(Vector tokens)
{
    int newRule = 1;
    int state = HEAD;
    PTRule rule = new PTRule();
    Vector rules = new Vector(0, 1);
    Vector convertedRules = new Vector(0, 1);
    for( int i = 0; i < tokens.capacity(); i++ )
    {
        InputToken token = (InputToken) tokens.elementAt(i);
        if( newRule == 1 )
        {
            rule = new PTRule();
            newRule = 0;
            state = HEAD;
        }
        switch(state)
        {
            case HEAD:
            {
                if( token.image.equals("<-") )
                {
                    state = BODY;
                }
                else
                {
                    if( token.image.equals("signedBy") )
                    {
                        state = SIGN_AFTER_HEAD;
                        rule.signedRule = 1;
                        rule.simpleRule = 0;
                    }
                    else
                    {
                        if( token.image.equals(".") )
                        {
                            state = HEAD;
                            newRule = 1;
                            rules.add(rule);
                        }
                        else
                        {
                            rule.head = rule.head + token.image;
                        }
                    }
                }
            }; break;
            case BODY:
            {
                if( token.image.equals("|") )
                {
                    rule.simpleRule = 0;
                    rule.ruleWithGuards = 1;
                    rule.guards = rule.body;
                    rule.body = new String();
                    state = AFTER_GUARDS;
                }
                else
                {
                    if( token.image.equals("signedBy") )
                    {
                        state = AFTER_SIGNED_IN_BODY;
                        rule.simpleRule = 0;
                        rule.signedRule = 1;
                        rule.beforeSigned = rule.body;
                        rule.body = new String();
                    }
                    else
                    {
                        if( token.image.equals(".") )
                        {
                            state = HEAD;
                            newRule = 1;
                            rules.add(rule);
                        }
                        else
                        {
                            rule.body = rule.body + token.image;
                        }
                    }
                }
            }; break;
            case SIGN_AFTER_HEAD:
            {
                if( !token.image.equals("[") )
                {
                    if( token.image.equals(".") ) // can never happen
                    {
                        state = HEAD;
                        newRule = 1;
                        rules.add(rule);
                    }
                    else
                    {
                        if( token.image.equals("]") )
                        {
                            state = SIGNED_BODY;
                        }
                        else
                        {
                            rule.signedBy = rule.signedBy + token.image;
                        }
                    }
                }
            }; break;
            case AFTER_SIGNED_IN_BODY:
            {
                if( token.image.equals("]") )
                {
                    state = SIGNED_BODY;
                }
                else
                {
                    if( !token.image.equals("[") )
                    {
                        rule.signedBy = rule.signedBy + token.image;
                    }
                }
            }; break;
            case AFTER_GUARDS:
            {
                if( token.image.equals(".") )
                {
                    state = HEAD;
                    newRule = 1;
                    rules.add(rule);
                }
                else
                {
                    if( token.image.equals("signedBy") )
                    {
                        rule.simpleRule = 0;
                        rule.signedRule = 1;
                        rule.beforeSigned = rule.guards;
                        rule.guards = new String();
                        state = SIGN_AFTER_HEAD;
                    }
                    else
                    {
                        rule.body = rule.body + token.image;
                    }
                }
            }; break;
            case SIGNED_BODY:
            {
                if( token.image.equals(".") )
                {
                    state = HEAD;
                    newRule = 1;
                    rules.add(rule);
                }
                else
                {
                    if( token.image.equals("|") )
                    {
                        state = SIGNED_BODY_AFTER_GUARDS;
                        rule.signedGuards = rule.signedBody;
                        rule.simpleRule = 0;
                        rule.ruleWithGuards = 1;
                        rule.signedBody = new String();
                    }
                    else
                    {
                        rule.signedBody = rule.signedBody + token.image;
                    }
                }
            }; break;
            case SIGNED_BODY_AFTER_GUARDS:
            {
                if( token.image.equals(".") )
                {
                    state = HEAD;
                    newRule = 1;
                    rules.add(rule);
                }
                else
                {
                    rule.signedBody = rule.signedBody + token.image;
                }
            }; break;
            default: break;
        }
    }
    convertedRules = convertRules(rules);
    return(convertedRules);
}

/**
 * This function converts PTRules to Prolog rules
 *
 * @param rules Vector of PTRules
 * @return Vector of Prolog rules
 */
private static Vector convertRules(Vector rules)
{
    Vector convertedRules = new Vector(0, 1);
    for( int i = 0; i < rules.capacity(); i++ )
    {
        PTRule rule = (PTRule) rules.elementAt(i);
        String convertedRule = new String();
        if( rule.signedRule == 0 )
        {
            convertedRule = "rule( " + rule.head + ", [" + rule.guards + "]" + ", [" + rule.body + "] ).";
        }
        else
        {
            if( (rule.signedRule == 1) && (!rule.beforeSigned.equals("")) )
            {
                convertedRule = "signed( rule( " + rule.head + ", [" + rule.signedGuards + "]" + ", [" + rule.signedBody + "] ), " + rule.signedBy + ", signature( " + rule.signedBy + " ) ).";
                convertedRules.add(convertedRule);
                convertedRule = "policy( " + rule.head + ", [" + rule.beforeSigned + "] ).";
            }
            else
            {
                convertedRule = "signed( rule( " + rule.head + ", [" + rule.signedGuards + "]" + ", [" + rule.signedBody + "] ), " + rule.signedBy + ", signature( " + rule.signedBy + " ) ).";
            }
        }
        convertedRules.add(convertedRule);
    }
    return(convertedRules);
}

}
