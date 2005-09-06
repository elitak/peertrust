
function alertTest(){
	alert("Alert test..................................");
}

function getPTApplet(){
	//var theDocument=parent.document.getElementById('applet_frame').document;
	//return top.applet_frame.document.pt_applet;
	//return top.applet_frame.document.getElementById("pt_applet");
	try{
		var ptApplet=top.applet_frame.document.getElementById("pt_object_applet");	
		ptApplet.isActive();
		return ptApplet;
	}catch(exception){
		return top.applet_frame.document.getElementById("pt_embed_applet");
	}
}

function getResource(resource){
	try{
		//var theDocument=parent.document;//top.document;
		//var theDocument=parent.document.getElementById('applet_frame').document;
		//alert("6applets:"+top.applet_frame.document.pt_applet);
		var ptApplet=getPTApplet();//top.applet_frame.document.pt_applet;//top.applet_frame.pt_applet;//theDocument.getElementById('pt_applet');
		ptApplet.getResource(resource)
	}catch(ex){
		//alert(ex.message+" name:"+ptApplet);
	}
}

function registerSession(sessionID){
	try{
		var ptApplet=getPTApplet();//top.applet_frame.document.pt_applet;//top.applet_frame.pt_applet;//theDocument.getElementById('pt_applet');
		ptApplet.registerSession(sessionID);
	}catch(ex){
		//alert(ex.message+" name:"+ptApplet);
	}
}

function toggleVisualization(){
	try{
		var ptApplet=getPTApplet();
		ptApplet.toggleVisualization();
	}catch(ex){
		//alert(ex.message+" name:"+ptApplet);
	}
}

function uninstallPeerTrust(){
	try{
		var ptApplet=getPTApplet();
		ptApplet.uninstallPeerTrust();
	}catch(ex){
		//alert(ex.message+" name:"+ptApplet);
	}
}
//managePolicies()

function managePolicies(){
	try{
		var ptApplet=getPTApplet();
		ptApplet.managePolicies();
	}catch(ex){
		//alert(ex.message+" name:"+ptApplet);
	}
}

function destroyPTClient(){
	try{
		var theDocument=top.document;
		var ptApplet=theDocument.getElementById('pt_applet');
		ptApplet.stop();
	}catch(ex){
		alert(ex.message);
	}	
}

/////////////////////////////////////////////////////////////
///function to set trust scheme/////////////////////////////
////////////////////////////////////////////////////////////

function getTrustSchemeToUse(){
		var optionE= document.getElementById("usePeerTrust");		
		if(optionE.checked){
			return "PeerTrust";
		}else{
			return "User Password";
		} 
	}
	
	function setTrustSchemeToUse(){
		try{
			var option= getTrustSchemeToUse();//
			var applet=  getPTApplet();
			applet.setTrustScheme(option);	
			checkTrustSchemeOption();	
		}catch(exception){
			alert("could not set Trust Scheme:"+exception);
		}		 
	}
	
	function checkTrustSchemeOption(){
		try{
			//var option= getTrustSchemeToUse();//
			var applet=  getPTApplet();
			var actualScheme = applet.getTrustScheme();		
			if(actualScheme.equals("PeerTrust")){
				document.getElementById("usePeerTrust").checked=true;
				document.getElementById("useUserPassword").checked=false;		
			}else if(actualScheme.equals("User Password")){
				document.getElementById("useUserPassword").checked=true;		
				document.getElementById("usePeerTrust").checked=false;
			}
		}catch(exception){
			alert("could not check Trust Scheme Option:"+exception);
		}		 
	}
	
	function hideDIV(containerID){		
			var container= document.getElementById(containerID);
			container.style.visibility="hidden";
			container.style.height="0px";
			return;
	}
	
	function showDIV(containerID, height){		
			var container= document.getElementById(containerID);
			container.style.visibility="visible";
			container.style.height=height;
			return;
	}
	
/////////////////////////////////////////////////////////////////
	/**
		To load free pages
	*/	
	function loadPage(pageURL){
		location.href=pageURL;
	}
	
//////////////////////////////////////////////////////////////////
////////////////GlobalLayout//////////////////////////////////////
//////////////////////////////////////////////////////////////////

	function toggleApplet(){
		try{
			var appletFrame=top.applet_frame;
			var displayFrame=top.display_frame;
			var mainFrame=top;//.applet_frame.parent;//top.main_frameset;
			var cols= new String(mainFrame.document.body.cols);//appletFrame.width);//mainFrame.cols);
			//alert("cols1:"+cols.indexOf("0%")+" "+cols);
			if(cols.indexOf("0%")!=0){
				mainFrame.document.body.cols="0%,100%";
			}else{
				mainFrame.document.body.cols="40%,60%";				
			}
		}catch(exception){		
			alert("cannot hide or show applet frame:"+exception.message);
		}/**/
	}
		