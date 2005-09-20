package org.peertrust.demo.common;

import java.awt.Component;
import java.io.IOException;



import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class Utils {
	public static boolean askYesNoQuestion(String dlgTitle,String question, Component parent,Object[] buttonTittle){
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
	
//	public static boolean askYesNoQuestion(	final String dlgTitle, 
//											final String question, 
//											final Component parent,
//											final Object[] buttonTittle){
//		final Object[] options = {"Yes","No"};
//		if(buttonTittle!=null){
//			if(buttonTittle.length==2){
//				options[0]=buttonTittle[0];
//				options[1]=buttonTittle[1];
//			}
//		}
//		final int[] n = {0};
//		Runnable dialogRunnable = new Runnable() {
//	        public void run() {
//	        	n[0]=JOptionPane.showOptionDialog(parent,
//									question,
//									dlgTitle,
//									JOptionPane.YES_NO_OPTION,
//									JOptionPane.QUESTION_MESSAGE,
//									null,     //don't use a custom Icon
//									options,  //the titles of buttons
//									options[0]); //default button title
//	        }
//	    };
//	    SwingUtilities.invokeLater(dialogRunnable);
//		return (n[0]==JOptionPane.YES_OPTION);
//	}
	
	public static void inform(String dlgTitle,String info, Component parent){
		JOptionPane.showMessageDialog(parent,
			    info,
			    dlgTitle,
			    JOptionPane.INFORMATION_MESSAGE,
			    null);//icon
	}
	
	static public Element getRootElement(String path, String rootElement) throws NullPointerException, SAXException, IOException, ParserConfigurationException{
		if(path==null){
			new NullPointerException("Parameter path mustnot be null");
		}
		if(rootElement==null){
			new NullPointerException("Parameter rootElement must not be null");
		}
	
		DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		Document dom = builder.parse(path);
		
		NodeList rootNodeList=
			dom.getElementsByTagName(rootElement);
		Element mRootNode=null;
		if(rootNodeList.getLength()!=1){//
			throw new Error(	
					"Illegal xml config file. It must contain exactelly one "+
					"<"+rootElement+"> but contains "+
					rootNodeList.getLength());
		}else{
			mRootNode=(Element)rootNodeList.item(0);
		}
		
		return mRootNode;		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		inform("info","this is not an info!",null);
	}

}
