package org.peertrust.demo.common;

import java.awt.Component;



import javax.swing.JOptionPane;


public class Helpers {
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
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		inform("info","this is not an info!",null);
	}

}
