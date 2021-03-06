package test.org.protune.core;

import org.protune.api.*;
import java.io.File;

/**
 * <tt>consultSource(String theory)</tt> rewrites previous theories.
 * <tt>query(String query)</tt> returns the only first result and rewrites previous query results.
 * <tt>retry()</tt> returns the following result to the previous query.
 * @author jldecoi
 */
public class TestPrologEngine {
/*
	public static void main(String[] args){
		JLogWrapper jlw = new JLogWrapper();
		TuPrologWrapper tpw = new TuPrologWrapper();

		String theory = "a(a).\na(b).";
		
		try{
			jlw.loadTheory(theory);
			tpw.loadTheory(theory);
		}
		catch(LoadTheoryException lte){}
		
		String query = "a(X).";
		
		try{
			System.out.println(jlw.isSuccessful(query));
			System.out.println(tpw.isSuccessful(query));
			System.out.println(jlw.getFirstAnswer(query));
			System.out.println(tpw.getFirstAnswer(query));
			
			String[] sa;
			sa = jlw.getAllAnswers(query);
			for(int i=0;i<sa.length;i++) System.out.println(sa[i]);
			sa = tpw.getAllAnswers(query);
			for(int i=0;i<sa.length;i++) System.out.println(sa[i]);
		}
		catch(QueryException qe){}
	}
*/
	
	public static void main(String[] args){
		TuPrologWrapper tpw = new TuPrologWrapper();
		
		File f;
		
		try {
			f = new File("C:/Dokumente und Einstellungen/jldecoi/Desktop/Test/Program.txt");
			tpw.loadTheory(f);
			f = new File("C:/Dokumente und Einstellungen/jldecoi/Desktop/Test/1-Server.txt");
			tpw.loadTheory(f);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
