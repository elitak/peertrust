/*
 * Created on 13.06.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.peertrust.demo.common;



import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.peertrust.net.Answer;
import org.peertrust.net.Query;

/**
 * @author pat_dev
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ConfigurationOption {
 	
 static public String getPTServerConfigFilePath(){
 	String configFilePath=null;
 	String os=System.getenv("os");
 	getLogger("ConfigurationOption").info("OS:"+os);
 	if(os==null){
		configFilePath = 
			"file:/home/congo/tomcat/TomcatPeerTrust/web/PeerTrustConfig/demoPeertrustConfig.server.rdf" ;
	}else if(os.equals("Windows_NT")){
		configFilePath = 
			"file:/C:/dev_root/TomcatPeerTrust/web/PeerTrustConfig/demoPeertrustConfig.server.rdf" ;
	}else{
		configFilePath = 
			"file:/home/congo/tomcat/TomcatPeerTrust/web/PeerTrustConfig/demoPeertrustConfig.server.rdf" ;
	}
 	return configFilePath;
 }

	 static public String getPTClientConfigFilePath(){
	 	String configFilePath=null;
	 	String os=System.getProperty("os.name");
//	 	String home=System.getProperty("user.home");
//	 	File instDir=new File(home,"pt");
//	 	configFilePath=instDir.toString();
	 	if(os==null){
			configFilePath = 
				"file:/home/congo/tomcat/TomcatPeerTrust/web/PeerTrustConfig/demoPeertrustConfig.client.rdf" ;
		}else if(os.equals("Windows_NT")){
			configFilePath = 
				"file:/C:/dev_root/TomcatPeerTrust/web/PeerTrustConfig/demoPeertrustConfig.client.rdf" ;
		}else{
			configFilePath = 
				"file:/home/congo/tomcat/TomcatPeerTrust/web/PeerTrustConfig/demoPeertrustConfig.client.rdf" ;
		}
	 	return configFilePath;
	 }
 
	 static public Logger getLogger(String key){
	 	Logger logger;
	 	logger = Logger.getLogger(key);
		logger.setLevel(Level.ALL);
		logger.setAdditivity(true);
		
//		try {
//			logger.addAppender(new SocketAppender("130.75.183.105",4445));
//		} catch (Exception e) {
//			logger.debug("Could not find socket appender",e);
//		}
        return logger;
      }
	 
	 static public String getMessageAsString(Object mes){
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
