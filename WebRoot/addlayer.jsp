<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Geometry Test</title>

    <meta name="viewport" content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0"/>
    <script src="https://cdn.polyfill.io/v2/polyfill.min.js?features=requestAnimationFrame,Element.prototype.classList,URL"></script>
    <script src="https://cdn.bootcss.com/jquery/1.12.4/jquery.js"></script>
	<script src="js/layer.js"></script>
    <style type="text/css">
    </style>

</head>
<body>

<div style="margin-top:20px"><img src="img/logo.png" /></div>
  <div style="margin-left:20%;width:60%; border-style:solid; border-width:1px; border-color:#008B8B">
     <form  method="post">
     <br/>
		<span style="margin-left:170px;" >Layer View ON/OFF </span>		
	 <br/>		
  			
  		<hr size="1" color="#008B8B"></hr>
  		<input style="display:none;" id="firstName" type="file" size="35"/>
		
		<input type="button" value="Show Layer List!" style="margin-left:25px;" class="btn_2k3" onclick="doRequestUsingGET();"/>
    	<hr size="1" color="#008B8B"></hr>
  		<input style=" margin-left:30px;"type=checkbox name=All onclick="checkAll('layer')">SELECT ALL 	
  		<hr size="1" color="#008B8B"></hr>
   		<ul id="showUl"></ul>  		
    	<div style="margin-top:20px;margin-bottom:20px;height:20px;margin-left:30px;">    	
    	<input type="button" onclick="createOrder();" value="Submit"><p>
    	</div>
    	</form> 
    	
    	<form  method="post">
    	<textarea id="order"  cols="55" rows="5" style="margin-left:25px;resize: none;"></textarea>
    	<input type="button" style="margin-left:25px;"onclick="doRequestUsingGET_Result();   window.opener.location.reload(); setTimeout('self.close()',300);" value="Finished"> 
    	</form> <!---->
    	<div style="display:none"> <div id="serverResponse"></div></div>
    	<div style="display:none"> <div id="serverResult"></div></div>
</div>
        <script>
        self.resizeTo(800,600);   
        self.focus(); 
        var websocket;  
        var host = "ws://echo.websocket.org/";
        if('WebSocket' in window){  
            websocket = new WebSocket(host); 
        }  
        else{  
            alert('No WebSocket'); 
        } 
        websocket.onopen = function (evt) { onOpen(evt) }; 
        websocket.onclose = function (evt) { onClose(evt) }; 
        websocket.onmessage = function (evt) { onMessage(evt) }; 
        websocket.onerror = function (evt) { onError(evt) }; 
    </script>
</body>
</html>

