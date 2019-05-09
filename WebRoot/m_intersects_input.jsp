<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="UTF-8">
<title>Geometry Test</title>

<meta name="viewport"
	content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0" />
<script
	src="https://cdn.polyfill.io/v2/polyfill.min.js?features=requestAnimationFrame,Element.prototype.classList,URL"></script>
<script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.js"></script>
<script src="js/layer.js"></script>
<style type="text/css">
table {
	border-collapse: collapse;
	width: 100%;
	margin: 0 auto;
	margin-top: 30px;
	margin-bottom: 60px;
}

th {
	background-color: rgb(128, 102, 160);
	color: white;
	font-weight: bold;
}

td {
	color: black;
}

tr, th {
	border-width: 1px;
	border-style: solid;
	border-color: rgb(128, 102, 160);
	text-align: center;
	height: 30px;
}
</style>

</head>
<body>

	<div style="margin-top:20px">
		<img src="img/logo.png" />
	</div>
	<div
		style="margin-left:20%;width:60%; border-style:solid; border-width:1px; border-color:#008B8B">
		<form method="post">
			<br /> <span style="margin-left:170px;">M_Intersects_Input </span> <br />

			<hr size="1" color="#008B8B"></hr>
			<input style="display:none;" id="firstName" type="file" size="35" />

			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Input Moving Feature<input type="text" id="name1" style="margin-left:25px; width:500px;"
				class="btn_2k3" />
				
			<hr size="1" color="#008B8B"></hr>
				
			&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Input Moving Feature<input type="text" id = "name2" style="margin-left:25px;width:500px;"
				class="btn_2k3"  />
				
				
			<hr size="1" color="#008B8B"></hr>
			<ul id="showUl"></ul>
			<hr size="1" color="#008B8B"></hr>
			<div
				style=" margin-top:20px;margin-bottom:20px;height:20px;margin-left:30px;">
				<span>Select function</span> <select id="type6">
					<option value="">--</option>
					<option value="fun9">MGeometry_Intersects_Input</option>	
					<option value="fun10">Secondo_Intersects_Input</option>				
				</select>
				<p>
			</div>
			<input type="button" onclick="createTimes();" value="Submit"
				style="margin-top:30px;">
			<p>
		</form>

		<form method="post">
			<textarea id="order3" cols="55" rows="5"
				style="margin-top:10px;margin-left:25px;resize: none;"></textarea>
			<input type="button" style="margin-left:25px;"
				onclick="doRequestUsingGET_Time();" value="Show Result">
		</form>
		<!---->
		<div style="display:none">
			<div id="serverTime"></div>
		</div>
		<div style="display:none">
			<div id="serverResponse"></div>
		</div>
		<div style="display:none">
			<div id="serverResult"></div>
		</div>

		<table id="tb2">
			<tr>
				<th style="width:200px;">Function</th>
				<th style="width:300px;">Name</th>
				<th>Result</th>
			</tr>
		</table>

	</div>
	<script>
		self.resizeTo(800, 600);
		self.focus();
		var websocket;
		var host = "ws://echo.websocket.org/";
		if ('WebSocket' in window) {
			websocket = new WebSocket(host);
		} else {
			alert('No WebSocket');
		}
		websocket.onopen = function(evt) {
			onOpen(evt)
		};
		websocket.onclose = function(evt) {
			onClose(evt)
		};
		websocket.onmessage = function(evt) {
			onMessage(evt)
		};
		websocket.onerror = function(evt) {
			onError(evt)
		};
		function createTimes() {
			var txt = '';
			var name1 = document.getElementById("name1").value;
			var name2 = document.getElementById("name2").value;
			txt = txt+ name1+" "+name2+" ";
			var strs_result = new Array();
			
			var options = $("#type6 option:selected");
	
			document.getElementById("order3").value = "Successful Add Algorithm: " + JSON.stringify(txt) + " " + options.text() + " " + "@@@";
		}
		function createOrderInput(){
			  
			   var test = name1+" "+name2;
	    	   var strs_result =  new Array();
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
		
	</script>
</body>
</html>

