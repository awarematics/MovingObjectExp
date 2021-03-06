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
			<br /> <span style="margin-left:170px;">OGC analysis </span> <br />

			<hr size="1" color="#008B8B"></hr>
			<input style="display:none;" id="firstName" type="file" size="35" />

			<input type="button" value="Show MF List!" style="margin-left:25px;"
				class="btn_2k3" onclick="doRequestUsingGET();" />
			<hr size="1" color="#008B8B"></hr>
			<ul id="showUl"></ul>
			<hr size="1" color="#008B8B"></hr>
			<div style=" margin-top:20px;margin-bottom:20px;height:20px;margin-left:30px;">				
				<span> Select function</span> <select id="type5">
					<option value="">--</option>
					<option value="fun1">M_VeolocityAtTime</option>
					<option value="fun2">M_AccelerationAtTime</option>
					<option value="fun3">M_TimeAtDistance</option>
					<option value="fun4">M_TimeToDistance</option>
					<option value="fun5">M_TimeAtCummulativer</option>
					<option value="fun6">M_SnapToGrid</option>	
					<option value="fun7">M_BBox</option>				
					<option value="fun8">M_BTime</option>								
				</select>
				<p>
				<div id="delta" style="display:none">
					Please input parameter: <input type="text" id="deltavalue"
						value="" />
				</div>
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
				<th>Function</th>
				<th>Name</th>
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
			layer = document.forms[0].layer;
			var txt = '';
			var test = xmlHttp.responseText;
			var strs = new Array();
			var strs_result = new Array();
			strs = test.split(" ");
			for (i = 0; i < strs.length; i++) {
				if (strs[i] != "") {
					strs_result.push(strs[i]);
				}
			}
			var options = $("#type5 option:selected");
			var parameter1 = document.getElementById("deltavalue");
			//alert(options.text());
			var times = layer.length;
			for (i = 0; i < layer.length; i++) {
				if (layer[i].checked) {
					txt = txt + strs_result[i] + " "
				}
			}
			document.getElementById("order3").value = "Successful Add MF: " + JSON.stringify(txt) + " " + options.text() + " " + parameter1.value + "@" + "@";
		}
	
		var option = {
			"fun1" : "delta",
			"fun2" : "delta",
			"fun3" : "delta",
			"fun5" : "delta",
			"fun6" : "delta"
		};
		$("#type5").bind("change", function() {
			var divId = option[this.value];
			$("#" + divId).show().siblings().hide();
		});
	</script>
</body>
</html>

