<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8"/>
    <title>Moving Feature System</title>
	<link rel="stylesheet" type="text/css" href="css/ol.css"> 
	<link rel="stylesheet" type="text/css" href="css/common.css"> 
	<script src="js/ol.js"></script>
    <style type="text/css">
    </style>
    <script type="text/javascript"> 
    </script>
</head>
<body>
<div style="margin-top:50px;margin-left:150px"><img src="img/logo.png" />
    <div id="item">
  <ul class="">
    <li>
      MF Layers

      <ul class="second">
        <li><a href="map.jsp">Add Map</a></li>
        <li>Add Data</li>
        <li>Add Layer</li>
      </ul>
    </li>
    <li>
      Moving Feature

      <ul class="second">
        <li>Projection</li>
        <li>
          Temporal/Time-Series

        </li>
        <li>Moving Media Similarity
       
        </li>
      </ul>
    </li>
    <li>
      Analysis Tools

      <ul class="second">
        <li>OGC Analysis</li>
        <li>
          Spatial-Temporal Predict

          <ul class="third">
            <li></li>
            <li></li>
            <li></li>
            <li></li>
          </ul>
        </li>
      </ul>
    </li>
  </ul>
</div>
</div>
<div class ="shine_red" id="map" style="width:80%; margin-left:10%; display:none; "></div>
<div id="popup" class="ol-popup">
      <a href="#" id="popup-closer" class="ol-popup-closer"></a>
      <div id="popup-content"></div>
    </div>
</body>
</html>