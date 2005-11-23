package org.peertrust.demo.common;

import java.awt.Component;
import java.io.IOException;



import javax.swing.JInternalFrame;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;


import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.peertrust.net.Answer;
import org.peertrust.net.Query;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * A collection of utility methods
 * 
 * @author Patrice Congo (token77)
 */
public class Utils {
	/**
	 * Shows a yes and no dialog with a specified question
	 * 
	 * @param dlgTitle -- the title of the dialog window
	 * @param question -- the question to ask
	 * @param parent -- the parent component
	 * @param buttonTittle -- an string array of length 2 containing
	 * 		  for the button. Its 1st elemtn will replace the ok and 
	 * 			its seond element will replace the cancel				
	 * @return true for a klick on yes otherwise false.
	 */
	public static boolean askYesNoQuestion(
									String dlgTitle,
									String question, 
									Component parent,
									Object[] buttonTittle)
	{
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
	
	/**
	 * Show a text message dialog.
	 * 
	 * @param dlgTitle -- the title of the message window
	 * @param info -- the message to show
	 * @param parent -- the parent component
	 */
	static public void inform(
							String dlgTitle,
							String info, 
							Component parent)
	{
		JOptionPane.showMessageDialog(parent,
			    info,
			    dlgTitle,
			    JOptionPane.INFORMATION_MESSAGE,
			    null);//icon
	}
	
	/**
	 * To get the root element of an xml document.
	 * 
	 * @param path -- the path of the xml document
	 * @param rootElement -- the name of the root element
	 * 
	 * @return the single root element of the xml document
	 * 
	 * @throws NullPointerException
	 * @throws SAXException
	 * @throws IOException
	 * @throws ParserConfigurationException
	 * @throws Error if the document does contains several root element
	 */
	static public Element getRootElement(
								String path, 
								String rootElement) 
								throws 	NullPointerException, 
										SAXException, 
										IOException, 
										ParserConfigurationException
	{
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
	 * Start an input dialog to prompt user for input.
	 *  
	 * @param parent -- the parent component
	 * @param mes -- the promt mesage
	 * @return the user input 
	 */
	static public String getUserInput(
								Component parent,
								String mes)
	{
		if(parent instanceof JInternalFrame){
			return 
				JOptionPane.showInternalInputDialog(
										parent,
										mes,
										"User alias",
										JOptionPane.QUESTION_MESSAGE);
		}else{
			return JOptionPane.showInputDialog(
										parent,
										mes,
										"User alias",
										JOptionPane.QUESTION_MESSAGE);
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		//inform("info","this is not an info!",null);
		System.out.println("input:"+getUserInput(null,"your input"));
	}

	static public Logger getLogger(String key)
	{
	 	Logger logger;
	 	logger = Logger.getLogger(key);
		logger.setLevel(Level.ALL);
		logger.setAdditivity(true);
	    return logger;
    }
	
	/**
	 * Build a text representation of a peertrust 
	 * message
	 * @param 	mes -- the message object to represent as
	 * 			text.
	 * 
	 * @return a string representing the passed message object 
	 */
	static public String getMessageAsString(Object mes)
	{
    	StringBuffer buf= new StringBuffer(128);
		try {
			if(mes instanceof Answer){
				Answer answer= (Answer)mes;
				buf.append("\n************ANSWER**********************");
				buf.append("\nAnswer.Goal:"+answer.getGoal());
				buf.append("\nAnswer.ID:"+answer.getReqQueryId());
				buf.append("\nAnswer.Status:"+answer.getStatus());
				buf.append("\nAnswer.Source:"+answer.getSource().getAlias());
				buf.append("\nAnswer.Target:"+answer.getTarget().getAlias());
				buf.append("\n*************ANSWER END******************\n");
			}else if(mes instanceof Query){
				Query query= (Query)mes;
				buf.append("\n************Query**********************");
				buf.append("\nQuery.Goal:"+query.getGoal());
				buf.append("\nQuery.ID:"+query.getReqQueryId());
				buf.append("\nQuery.Source:"+query.getSource().getAlias());
				buf.append("\nQuery.Target:"+query.getTarget().getAlias());
				buf.append("\n*************Query END******************\n");
			}else{
				buf.append("\n**************NOR ANSWER EITHER QUERY**********\n");
				buf.append(mes);
				buf.append("\n**************NOR ANSWER EITHER QUERY END**********\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
			buf.append("\nEXCEPTION:"+e.getMessage());
		}
		
		return buf.toString();
    }
}
