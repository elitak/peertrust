<h2>Configuration</h2>
<hr/>
<br/>
<input 	id="usePeerTrust" 
		type="radio" 
		name="radio" 
		checked=""
		style="text-align:left;width:auto"
		onclick=""/>PeerTrust
<br/>
<input 	id="useUserPassword" 
		type="radio" 
		name="radio" 
		style="text-align:left;width:auto"
		 onclick=""/>User Password
<br/>

<input 	type="button" 
		name="comitPeertrustConfig" 
		value="Commit" 
		onclick='setTrustSchemeToUse();'
		style="width: 200px"/>

<br/>
<script>checkTrustSchemeOption();</script>