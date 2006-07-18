package g4mfs.impl;

import javax.xml.namespace.QName;
/**
 * 
 * @author ionut constandache ionut_con@yahoo.com
 * Qualified Namespaces for the Resource Properties including the TrustNegotiation URI
 */
public interface MathQNames 
{
	public static final String NS = "http://www.globus.org/gt4ide/example/MathService";
	public static final String NS1 = "http://www.globus.org/gt4ide/example/MathFactoryService";
	public static final QName RESOURCE_PROPERTIES = new QName(NS,
			"MathResourceProperties");

	public static final QName RESOURCE_REFERENCE = new QName(NS,
			"MathResourceReference");

	/* Insert ResourceProperty Qnames here. For example:

	public static final QName RP_VALUE = new QName(NS, "Value");

	*/
	public static final QName RP_VALUE = new QName(NS,"Value");
	
	public static final QName RP_LASTOP = new QName(NS,"LastOp");
	
	public static final QName RP_TRUST_NEGOTIATION = new QName(NS,"TrustNegotiation");
	//public static final QName RESOURCE_PROPERTIES_NEGOTIATION = new QName(NS1,"ResPTrustNegotiation");
	public static final QName RP_NEWOP = new QName(NS1,"NewOp");
	
}
