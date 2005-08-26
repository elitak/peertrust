//import java.beans.XMLEncoder;
//import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
//import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Vector;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.methods.GetMethod;
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
	public static void main(String[] args) throws Exception {
		//testObjectStream();
		String[] ar= new String[0];
		System.out.println(ar.length);
	}
}
