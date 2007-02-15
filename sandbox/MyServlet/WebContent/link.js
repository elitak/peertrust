function init() {
	var e=document.getElementsByTagName("a");
	for(i=0;i<e.length;i++) {
  		var j=document.getElementsByTagName("a").item(i);
  		if(j.addEventListener)
  			j.addEventListener("click",linkevent,false);
		else if(j.attachEvent)
			j.attachEvent("onclick",linkevent);
	}
}

function linkevent(evt) {
	if(!evt)
		evt=window.event;
//	var waitpopup=window.open('http://localhost:8081/MyServlet/wait.html','','width=600,height=200');
//	waitpopup.document.write('<html><body><img src="images/progress.gif"><br><br><b>Please wait</b></body></html>');
//	top.frames[0].document.applets[0].showWaitPage("http://localhost:8081/MyServlet/wait.html","rightFrame");
//	top.frames[0].document.applets[0].askForURL(evt.target,evt.target.target);
	if(evt.target)
		top.frames[0].document.ptapplet.askForURL(evt.target,"rightFrame","");
	else if(evt.srcElement)
		top.frames[0].document.ptapplet.askForURL(""+evt.srcElement,"rightFrame","");
//	top.frames[0].document.ptapplet.askForURL("http://localhost:8081/MyServlet/download/download.html","rightFrame");
//	waitpopup.close();
//	top.frames[0].document.applets[0].askForURL("http://localhost:8081/MyServlet/wait.html","rightFrame");
//	self.location.replace(evt.target);
	if(evt.preventDefault)
		evt.preventDefault();
	evt.returnValue=false;
}

window.onload=init;
