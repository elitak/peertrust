package test.org.protune;

import java.lang.reflect.InvocationTargetException;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			Object[] oa = {"", ""};
			Class[] ca = new Class[oa.length];
			for(int i=0; i<ca.length; i++) ca[i] = oa[i].getClass();
			Dummy d =
				(Dummy) Class.forName("test.org.protune.DummyAgain").getConstructor(ca).newInstance(oa); 
			System.out.println(d);
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
