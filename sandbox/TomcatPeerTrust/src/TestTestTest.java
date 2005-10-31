//import java.beans.XMLEncoder;
//import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
//import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
import org.peertrust.parser.prolog.InputToken;
import org.peertrust.parser.prolog.PrologGrammarConstants;
import org.peertrust.parser.prolog.PrologGrammarTokenManager;
import org.peertrust.parser.prolog.PrologRule;
import org.peertrust.parser.prolog.PrologSemanticAnalyzer;
import org.peertrust.parser.prolog.Token;
//import org.apache.commons.httpclient.methods.RequestEntity;
//import org.peertrust.meta.Trace;
//import org.peertrust.net.Peer;
//import org.peertrust.net.Query;

/*
 * Created on 28.04.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class TestTestTest {
	public static void copyFile(String httpSource, String localDestination){
		HttpClient httpClient= new HttpClient();
		GetMethod getMethod= new GetMethod(httpSource);
		try {
			httpClient.executeMethod(getMethod);
			byte[] fileContent=getMethod.getResponseBody();
			System.out.println("=============================\n"+(new String(fileContent)));
			FileOutputStream fStream=
				new FileOutputStream(localDestination);
			fStream.write(fileContent);
		} catch (HttpException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	public static void testPath() throws MalformedURLException, URISyntaxException{
		System.out.println("OS:"+System.getProperty("java.io.tmpdir"));
		File file = new File("file:/C:/Dokumente und Einstellungen/pat_dev/pt/demoPeertrustConfig.client.rdf");
		System.out.println(file.getAbsolutePath()+":"+file.exists());
		file = new File("file:\\\\C:\\Dokumente%20und%20Einstellungen\\pat_dev\\pt\\demoPeertrustConfig.client.rdf");
		System.out.println(file.getAbsolutePath()+":"+file.exists());
		file = new File("C:/Dokumente und Einstellungen/pat_dev/pt/demoPeertrustConfig.client.rdf");
		System.out.println(file.exists());
		file = new File(
				(new URL("file:/C:/Dokumente%20und%20Einstellungen/pat_dev/pt/demoPeertrustConfig.client.rdf")).toURI());
						  	
		System.out.println(file.getAbsolutePath()+":"+file.exists());
	}
	
	static public void testObjectStream() throws IOException{
		ObjectOutputStream oOut=
			new ObjectOutputStream(System.out);
		String str= "dadadadadadadada!\n".toString();
		oOut.writeObject(str);
		System.out.println();
		oOut.writeObject(new Integer(123));		
		System.out.println();
		Vector v= new Vector();
		v.add(new Float(1.0001));
		v.add(new Double(1.0001));
		v.add(new String("didi"));
		oOut.writeObject(v);
	}
	
	public static boolean askYesNoQuestion(String dlgTitle,String question, JFrame parent,Object[] buttonTittle){
		Object[] options = {"Yes","No"};
		if(buttonTittle!=null){
			if(buttonTittle.length==2){
				options[0]=buttonTittle[0];
				options[1]=buttonTittle[1];
			}
		}
		int n = JOptionPane.showOptionDialog(parent,
									question,
									dlgTitle,
									JOptionPane.YES_NO_OPTION,
									JOptionPane.QUESTION_MESSAGE,
									null,     //don't use a custom Icon
									options,  //the titles of buttons
									options[0]); //default button title
		return (n==JOptionPane.YES_OPTION);
	}
	
	static public void testPrologParser() throws Exception{
		String fileName="/home/pat_dev/eclipse_home/workspace_3_1/TomcatPeerTrust/web/PeerTrustConfig/alice.min";
//		PrologGrammar gr= new PrologGrammar(new FileInputStream(fileName));
//		System.out.println("img1:"+gr.getNextToken().image);
		org.peertrust.parser.prolog.SimpleCharStream stream= 
			new org.peertrust.parser.prolog.SimpleCharStream(new FileInputStream(fileName));
		PrologGrammarTokenManager mgr= new PrologGrammarTokenManager(stream);
		//String imgs[]=mgr.tokenImage;
		Token to=mgr.getNextToken();
		Vector tVector= new Vector();
		InputToken token;
		
		for(int i=0;to!=null && i<=10000 && to.kind!=PrologGrammarConstants.EOF;i++){
			System.out.println("h"+i+":"+to.image);
			to=mgr.getNextToken();
			if(to.kind==PrologGrammarConstants.EOF){
				break;
			}
			token=new InputToken();
			token.beginColumn=to.beginColumn;
			token.beginLine=to.beginLine;
			token.endColumn=to.endColumn;
			token.endLine=to.endLine;
			token.image=to.image;
			token.kind=to.kind;			
			tVector.add(token);
		}
		Vector rVector=PrologSemanticAnalyzer.analyze(tVector);
		PrologRule rule;
		for(Iterator it=rVector.iterator();it.hasNext();){
			rule=(PrologRule)it.next();
			System.out.println("body:"+rule.body+" ");
		}
		//System.out.println("h:"+mgr.getNextToken().next.next.image);
		return;
	}
	
	public static void main(String[] args) throws Exception {
		//testObjectStream();
//		String[] ar= new String[0];
//		System.out.println(ar.length);
//		System.out.println("yesnoQ:"+askYesNoQuestion("intallation","question?",null,null));
//		System.out.println("yesnoQ:"+askYesNoQuestion("intallation","<big>question?</big>",null,new String[]{"1","2","3"}));
		
		//testObjectStream();
		testPrologParser();
	}
}
