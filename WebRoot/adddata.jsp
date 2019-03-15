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
  <div style="margin-left:20%;width:60%;text-align: center; border-style:solid; border-width:1px; border-color:#008B8B">
     <form action ="" method="post">
     <br/><br/>
		<span>Please Upload Moving Feature Data: </span>
		
	 <br/><br/>
  		<input id="firstName" type="file" size="35"/>
  		<hr size="1" color="#008B8B"></hr><p><br>
  		<input type="button" value="Submit" style="width:100px;" onclick="doRequestUsingGET();"/></form> 
    	<div style="margin-top:50px;margin-bottom:100px;height:50px;"> <div id="serverResponse"></div><p><p>
    	<input type="button" onclick="window.opener.location.reload(); self.close();" value="Finished"> </div>
    	
    	

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
            alert('No WebSocket'); 
        } 
        websocket.onopen = function (evt) { onOpen(evt) }; 
        websocket.onclose = function (evt) { onClose(evt) }; 
        websocket.onmessage = function (evt) { onMessage(evt) }; 
        websocket.onerror = function (evt) { onError(evt) }; 
       
    </script>
</body>
</html>

