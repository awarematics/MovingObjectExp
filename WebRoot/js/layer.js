function onOpen(evt) { 
            console.log("Connected to WebSocket server."); 
        } 
        function onClose(evt) { 
            console.log("Disconnected"); 
        } 
        function onMessage(evt) { 
            console.log('Retrieved data from server: ' + evt.data); 
        } 
        function onError(evt) { 
            console.log('Error occured: ' + evt.data); 
        }
        window.onbeforeunload = function(){  
            onClose(evt);  
        }        
        var xmlHttp;
        function createXMLHttpRequest() {
            if (window.ActiveXObject) {
                xmlHttp = new ActiveXObject("Microsoft.XMLHTTP");
            }
            else if (window.XMLHttpRequest) {
                xmlHttp = new XMLHttpRequest();
            }
        }

       function createQueryString() {
            var firstName = document.getElementById("firstName").value;
            index = firstName.lastIndexOf("\\");
            firstName = firstName.substring(index+1);
            var queryString = "firstName=" + firstName ;
            return queryString;
        }
       function createQueryData() {
           var order = document.getElementById("order").value;
           index = order.lastIndexOf("\\");
           order = order.substring(index+1);
           var queryString = "order=" + order ;
           return queryString;
       }
        function doRequestUsingGET() {
            createXMLHttpRequest();
            var queryString = "GetAndPostExample?";
            queryString = queryString + createQueryString()
            + "&timeStamp=" + new Date().getTime();
           // alert(queryString);
            xmlHttp.open("GET", queryString, true);
            xmlHttp.onreadystatechange = handleStateChange;   
            xmlHttp.send();
        }
        function doRequestUsingGET_Result() {
            createXMLHttpRequest();
            var queryString = "GetAndPostData?";
            queryString = queryString + createQueryData()
            + "&timeStamp=" + new Date().getTime();
           // alert(queryString);
            xmlHttp.open("GET", queryString, true);
            xmlHttp.onreadystatechange = handleStateChange_Result;   
            xmlHttp.send();
        }

        function doRequestUsingPOST() {
            createXMLHttpRequest();
            var url = "GetAndPostExample?timeStamp=" + new Date().getTime();
            var queryString = createQueryString();
            xmlHttp.open("POST", url, true);
            xmlHttp.onreadystatechange = handleStateChange;
            xmlHttp.setRequestHeader("Content-Type",
            "application/x-www-form-urlencoded;");
            xmlHttp.send(queryString);
        }
        function doRequestUsingPOST_Result() {
            createXMLHttpRequest();
            var url = "GetAndPostData?timeStamp=" + new Date().getTime();
            var queryString = createQueryData();
            xmlHttp.open("POST", url, true);
            xmlHttp.onreadystatechange = handleStateChange_Result;
            xmlHttp.setRequestHeader("Content-Type",
            "application/x-www-form-urlencoded;");
            xmlHttp.send(queryString);
        }
        function handleStateChange() {
            if(xmlHttp.readyState == 4) {
                if(xmlHttp.status == 200) {
                parseResults();
                }
            }
        }
        function handleStateChange_Result() {
            if(xmlHttp.readyState == 4) {
                if(xmlHttp.status == 200) {
                parseData();
                }
            }
        }
       function parseResults() {
    	   document.getElementById('serverResponse').innerHTML = xmlHttp.responseText;
    	   var test = xmlHttp.responseText;
    	  
    	   var strs = new Array();
    	   var strs_result = new Array();
    	   strs = test.split(" ");
    	   var uls = document.getElementById('showUl');
    	   for (i = 0; i < strs.length-1; i++) {
              if(strs[i]!="")
            	  {
            	  		strs_result.push(strs[i]);
            	  } 
           }  	  
    	   show(strs_result);
            var responseDiv = document.getElementById("serverResponse");
            if(responseDiv.hasChildNodes()) {
            responseDiv.removeChild(responseDiv.childNodes[0]);
        }
        var responseText = document.createTextNode(xmlHttp.responseText);
        responseDiv.appendChild(responseText);
        }
       
       function parseData() {
    	   document.getElementById('serverResult').innerHTML = xmlHttp.responseText;
            var responseDiv = document.getElementById("serverResult");
            if(responseDiv.hasChildNodes()) {
            responseDiv.removeChild(responseDiv.childNodes[0]);
        }
        var responseText = document.createTextNode(xmlHttp.responseText);
        responseDiv.appendChild(responseText);
        }
       function show(dataList) {
           var uls = document.getElementById('showUl');
           var text = '';
           for (var i = 0; i < dataList.length; i++) {
               text += '<input type="checkbox" name="layer">' + dataList[i] + '</input><p>';
           }
           uls.innerHTML = text;
       }
       function createOrder(){
    	   layer=document.forms[0].layer;
           var txt = '';
           var test = xmlHttp.responseText;   	  
    	   var strs = new Array();
    	   var strs_result = new Array();
    	   strs = test.split(" ");
    	   for (i = 0; i < strs.length; i++) {
              if(strs[i]!="")
            	  {
            	  		strs_result.push(strs[i]);
            	  } 
           }
    	   var times = layer.length;
    	   if (layer.length == undefined)
    	   {
    	       alert("undefined! at least 2 trajectory");
    	   }
    	   else{
           for (i = 0; i< layer.length; i++){
               if (layer[i].checked){
              	 txt = txt +strs_result[i] + " "
               }
           }
           document.getElementById("order").value="Successful Add MF: "+ JSON.stringify(txt);
           }
       }
      
       function checkAll(str)   
       {   
           var a = document.getElementsByName(str);   
           var n = a.length;   
           for (var i=0; i<n; i++)   
           a[i].checked = window.event.srcElement.checked;   
       }   
     // _________________________________________________
       function createQueryProjection() {
           var order = document.getElementById("order2").value;
           index = order.lastIndexOf("\\");
           order = order.substring(index+1);
           var queryString = "order2=" + order ;
           return queryString;
       }
       function doRequestUsingGET_Projection() {
           createXMLHttpRequest();
           var queryString = "GetAndPostProjection?";
           queryString = queryString + createQueryProjection()
           + "&timeStamp=" + new Date().getTime();
          // alert(queryString);
           xmlHttp.open("GET", queryString, true);
           xmlHttp.onreadystatechange = handleStateChange_Projection;   
           xmlHttp.send();
       }
       function doRequestUsingPOST_Result() {
           createXMLHttpRequest();
           var url = "GetAndPostProjection?timeStamp=" + new Date().getTime();
           var queryString = createQueryProjection();
           xmlHttp.open("POST", url, true);
           xmlHttp.onreadystatechange = handleStateChange_Projection;
           xmlHttp.setRequestHeader("Content-Type",
           "application/x-www-form-urlencoded;");
           xmlHttp.send(queryString);
       }
       function handleStateChange_Projection() {
           if(xmlHttp.readyState == 4) {
               if(xmlHttp.status == 200) {
               parseProjection();
               }
           }
       }
       function parseProjection() {
    	   document.getElementById('serverProjection').innerHTML = xmlHttp.responseText;
    	   var test = xmlHttp.responseText;
    	  // var kkk1 = xmlHttp.responseText;
    	   projShow(test);
            var responseDiv = document.getElementById("serverProjection");
            if(responseDiv.hasChildNodes()) {
            responseDiv.removeChild(responseDiv.childNodes[0]);
        }
        var responseText = document.createTextNode(xmlHttp.responseText);
        responseDiv.appendChild(responseText);
        }
       function projShow(responseProj)
       {
    	   var rangeSplit = new Array();
    		rangeSplit = responseProj.split("#");
    		for (i = 0; i < rangeSplit.length-1; i++) {
    			var obj = new Array();
    			obj = rangeSplit[i].split("@");
    			var table = document.getElementById("tb2");
    			var newRow = table.insertRow();

    			var newCell1 = newRow.insertCell(0);
    			newCell1.innerHTML = "<td>" + obj[0] + "</td>";
    			var newCell2 = newRow.insertCell(1);
    			newCell2.innerHTML = "<td>" + obj[1] + "</td>";
    			var newCell3 = newRow.insertCell(2);
    			newCell3.innerHTML = "<td>" + obj[3] + "</td>";
    		}
       }
       // _________________________________________________
       function createQueryTime() {
           var order = document.getElementById("order3").value;
           index = order.lastIndexOf("\\");
           order = order.substring(index+1);
           var queryString = "order3=" + order ;
           return queryString;
       }
       function doRequestUsingGET_Time() {
           createXMLHttpRequest();
           var queryString = "GetAndPostTime?";
           queryString = queryString + createQueryTime()
           + "&timeStamp=" + new Date().getTime();
         // alert(queryString);
           xmlHttp.open("GET", queryString, true);
           xmlHttp.onreadystatechange = handleStateChange_Time;   
           xmlHttp.send();
       }
       function doRequestUsingPOST_Time() {
           createXMLHttpRequest();
           var url = "GetAndPostTime?timeStamp=" + new Date().getTime();
           var queryString = createQueryTime();
           xmlHttp.open("POST", url, true);
           xmlHttp.onreadystatechange = handleStateChange_Time;
           xmlHttp.setRequestHeader("Content-Type",
           "application/x-www-form-urlencoded;");
           xmlHttp.send(queryString);
       }
       function handleStateChange_Time() {
           if(xmlHttp.readyState == 4) {
               if(xmlHttp.status == 200) {
               parseTime();
               }
           }
       }
       function parseTime() {
    	   document.getElementById('serverTime').innerHTML = xmlHttp.responseText;
    	   var test = xmlHttp.responseText;
    	  // var kkk1 = xmlHttp.responseText;
    	   serverTimeShow(test);
            var responseDiv = document.getElementById("serverTime");
            if(responseDiv.hasChildNodes()) {
            responseDiv.removeChild(responseDiv.childNodes[0]);
        }
        var responseText = document.createTextNode(xmlHttp.responseText);
        responseDiv.appendChild(responseText);
        }
       function serverTimeShow(responseProj)
       {
    	   var rangeSplit = new Array();
    		rangeSplit = responseProj.split("#");
    		for (i = 0; i < rangeSplit.length-1; i++) {
    			var obj = new Array();
    			obj = rangeSplit[i].split("@");
    			var table = document.getElementById("tb2");
    			var newRow = table.insertRow();

    			var newCell1 = newRow.insertCell(0);
    			newCell1.innerHTML = "<td>" + obj[0] + "</td>";
    			var newCell2 = newRow.insertCell(1);
    			newCell2.innerHTML = "<td>" + obj[1] + "</td>";
    			var newCell3 = newRow.insertCell(2);
    			newCell3.innerHTML = "<td>" + obj[3] + "</td>";
    		}
       }