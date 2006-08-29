package test.org.peertrust.parser.protune;

import org.peertrust.parser.protune.*;
import junit.framework.*;

public class PrologProtuneParserTest extends TestCase {

	public PrologProtuneParserTest( String name ) {
		super( name ) ;
	}

	public static Test suite() {
		return new TestSuite( PrologProtuneParserTest.class );
	}

	public void setUp(){
	}
	
	public void testQuery1()throws Exception{
	
		String s=ProtunePrologParser.translate("rule(r1,fact(X),[]).");
		int result=s.compareTo("[r1]fact(X).\n");
		assertTrue(result==0);
	}
	
	public void testQuery2()throws Exception{
		
		String s=ProtunePrologParser.translate("rule(r2,allow(access(Resource)),[authenticate(U),has_subscription(U,Resource)]).");
		int result=s.compareTo("[r2]allow(access(Resource)):-authenticate(U),has_subscription(U,Resource).\n");
		assertTrue(result==0);
	}
	
	public void testQuery3()throws Exception{
		
		String s=ProtunePrologParser.translate("rule(f3,valid(user1),[]).\nrule(f3,complex_term(user1,name,mirela),[]).\nrule(f3,complex_term(user1,passwd,alerim),[]).");
		int result=s.compareTo("[f3]valid(user1[name:mirela,passwd:alerim]).\n");
		assertTrue(result==0);
	}

	public void testQuery4()throws Exception{
		
		String s=ProtunePrologParser.translate("rule(r1,allow(access(books)),[credential(sa,_c4d04d),complex_term(_c4d04d,type,student),complex_term(_c4d04d,issuer,_147917a),complex_term(_c4d04d,public_key,_19fe451),predicat7(_c4d04d,_147917a),predicat8(_147917a),blurred(predicat3(_19fe451))]).");
		int result=s.compareTo("[r1]allow(access(books)):-credential(sa,_c4d04d[type:student,issuer:_147917a,public_key:_19fe451]),predicat7(_c4d04d,_147917a),predicat8(_147917a),blurred(predicat3(_19fe451)).\n");
		assertTrue(result==0);
	}
	
	public void testQuery5()throws Exception{
		
		String s=ProtunePrologParser.translate("metarule(pred,evaluation(transfer_money(C,P),immediate),[ground(C),ground(P)]).");
		int result=s.compareTo("transfer_money(C,P).evaluation:immediate:-ground(C),ground(P).\n");
		assertTrue(result==0);
	}
	
	public void testQuery6()throws Exception{
		
		String s=ProtunePrologParser.translate("metarule(pred,action(challenge(K),\"challenge peer to prove that he has the private key coresponding to public key ${K}\"),[]).");
		int result=s.compareTo("challenge(K).action:\"challenge peer to prove that he has the private key coresponding to public key ${K}\".\n");
		assertTrue(result==0);
	}
	
	public void testQuery7()throws Exception{
		
		String s=ProtunePrologParser.translate("rule(r5,european_citizen(X),[credential(ea,European_id),complex_term(European_id,owner,X),complex_term(European_id,type,european_citizen),complex_term(European_id,issuer,I),complex_term(European_id,public_key,K),valid_credential(European_id,I),trusted_organization(I),challenge(K)]).");
		int result=s.compareTo("[r5]european_citizen(X):-credential(ea,European_id[owner:X,type:european_citizen,issuer:I,public_key:K]),valid_credential(European_id,I),trusted_organization(I),challenge(K).\n");
		assertTrue(result==0);
	}
	
	public static void main( String[] args ){
		
		try {
			junit.textui.TestRunner.run( suite() );
		} catch ( Throwable t ) {
			t.printStackTrace();
		}
		
	}
}
