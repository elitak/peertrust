<SCRIPT LANGUAGE="Javascript">
      var javawsInstalled = 0;
      isIE = "false";

      if (navigator.mimeTypes && navigator.mimeTypes.length) {
         x = navigator.mimeTypes['application/x-java-jnlp-file'];
         if (x) javawsInstalled = 1;
      } else {
         isIE = "true";
      }
	  
      function insertLink(url, name) {
       if (javawsInstalled) {
            document.write("<a href=" + url + ">"  + name + "</a>");
         } else {
            document.write("Need to install Java Web Start");
         }
      }
</SCRIPT> 