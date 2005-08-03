/**
 * This class represents structure of PeerTrust rule
 *
 * @author Bogdan Vlasenko
 */

public class PTRule
{
    public int simpleRule = 1;
    public int signedRule = 0;
    public int ruleWithGuards = 0;
    public String head = new String();
    public String body = new String();
    public String signedBy = new String();
    public String guards = new String();
    public String beforeSigned = new String();
    public String signedBody = new String();
    public String signedGuards = new String();
}
