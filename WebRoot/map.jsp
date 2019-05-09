<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>Moving Feature System</title>
<link rel="stylesheet" type="text/css" href="css/ol.css">
<link rel="stylesheet" type="text/css" href="css/common.css">
<script type="text/javascript"
	src="https://code.jquery.com/jquery-3.0.0.min.js"></script>
<script src="https://cdn.bootcss.com/jquery/2.2.4/jquery.min.js"></script>
<script src="js/ol.js"></script>
<script src="js/common.js"></script>
<style type="text/css">

table{
	border-collapse: collapse;
	width:1000px;
	margin:0 auto;
	margin-bottom:60px;
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
	text-align:center;
	height:30px;
}
</style>
<script type="text/javascript"> 
    
    </script>
</head>
<body onload="doRequestUsingGET_Result();">
	<div style="margin-top:50px;margin-left:150px">
		<img src="img/logo.png" />
		<div id="item">
			<ul class="">
				<li>MF Layers
					<ul class="second">
						<li>Add Map</li>
						<li><a href="#"
							onclick="javascript:window.open('adddata.jsp','newwindow');">Add
								Data</a></li>
						<li><a href="#"
							onclick="javascript:window.open('addlayer.jsp','newwindow');">Add
								Layer</a></li>
						<form method="post">
							<textarea style="display:none" id="order" cols="55" rows="5"
								style="margin-left:25px; resize: none;"></textarea>
						</form>
					</ul>
				</li>
				<li>Moving Feature

					<ul class="second">
						<li><a href="#" onclick="javascript:window.open('projection.jsp','newwindow');">
								Projection</a></li>
						<li><a href="#" onclick="javascript:window.open('time_series.jsp','newwindow');">
								Temporal/Time-Series</a>
						</li>
						<li><a href="#" onclick="javascript:window.open('similarity.jsp','newwindow');">
								Moving Media Similarity</a>
						</li>
					</ul>
				</li>
				<li>Analysis Tools

					<ul class="second">
						<li><a href="#" onclick="javascript:window.open('OGC.jsp','newwindow');">
								OGC Analysis</a>
						</li>
						<li><a href="#" onclick="javascript:window.open('relationship.jsp');">
								Spatial-Temporal Predict</a>
						</li>
					</ul>
				</li>
				<li style="width:300px;">Compare With Secondo Model

					<ul class="second">
						<li><a href="#" onclick="javascript:window.open('m_intersects.jsp','newwindow');">
								M_Intersects</a>
						</li>
						<li><a href="#" onclick="javascript:window.open('m_distance.jsp','newwindow');">
								M_Distance</a>
						</li>
					</ul>
				</li>
			</ul>
		</div>
	</div>
	<div class="shine_red" id="map"
		style="width:80%; height:700px; margin-left:10%;"></div>
	<div id="popup" class="ol-popup">
		<a href="#" id="popup-closer" class="ol-popup-closer"></a>
		<div id="popup-content"></div>
	</div>
	<div style="display:none">
		<div id="serverResult"></div>
	</div>
	<div id="rightlayer" style="display:none">
		<ul id="mouse-menu">
			<li>function1</li>
			<li>function2</li>
			<li>function3</li>
		</ul>
		<img src="img/000f8d37-d4c09a0f_19.png" width="300px" height="200px;" />
	</div>

	<select id="type" style="margin-left:15%;margin-top:20px;margin-bottom:50px;">
		<option value="">None</option>
		<option value="LineString">LineString</option>
		<option value="Polygon">Polygon</option>
	</select>
	<input type="button" id="draw" name="name" value="draw in the map" />
	<input type="button" id="modify" name="name" value="modify" />
	<input type="button" id="dist" name="name" value="distance" onclick= "showDis()" />
	<input type="button" id="range" name="name" value="range query" onclick="showRang()" />
	<div id="distanceid" style="display:none;">
	<table>
		<tr>
			<th>Type</th>
			<th>First Point</th>
			<th>Last Point</th>
			<th>Value</th>
		</tr>
		<tr>
			<td>Distance</td>		
			<td id="disfrom">New York</td>
			<td id="disto">New York</td>
			<td id="disresult">New York</td>
		</tr>
		
	</table>
	</div>
	<div id="rangeid"  style="display:none;">
	<table id="tb">
		<tr>
			<th style="width:160px">Name</th>
			<th style="width:100px">Type</th>
			<th style="width:130px">From Time</th>
			<th style="width:130px">To Time</th>
			<th>Polygon</th>
		</tr>
	</table>
	</div>
	    	<textarea id="order_range"  cols="55" rows="5" style="margin-left:25px;display:none"></textarea>
	    	<div> <div id="serverRange" style="display:none"></div></div>
</body>
</html>
<script type="text/javascript">
	var center = [ -73.83363056934796, 40.67460574679139 ];
	var markSvgSize = [ 30, 30 ] //
	var pathBackTimer = null; //
	var so = new ol.source.Vector();
	var source = new ol.source.OSM();
	var tile = new ol.layer.Tile({
		source : source
	});
	var vectorLayer = new ol.layer.Vector({
		source : so
	});
	var map = new ol.Map({
		layers : [ //
			tile, vectorLayer
		],
		view : new ol.View({
			center : center,
			minZoom : 3,
			maxZoom : 20,
			zoom : 11
		}),
		target : 'map'
	});

	var container = document.getElementById('popup');
	var content = document.getElementById('popup-content');
	var closer = document.getElementById('popup-closer');
	var popup = new ol.Overlay({
		element : container
	});
	closer.onclick = function() {
		popup.setPosition(undefined);
		closer.blur();
		return false;
	};
	map.addOverlay(popup);
	var geometrydeal = [];
	map.on('singleclick', function(evt) {
		var coordinate = evt.coordinate;
		geometrydeal.push(coordinate);
		var hdms = ol.coordinate.toStringHDMS(ol.proj.toLonLat(coordinate));
		showInfo(coordinate, hdms);
	});
	map.getView().setCenter(ol.proj.transform(center, 'EPSG:4326', 'EPSG:3857'));

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

	$('#map').on('contextmenu', function() {
		// return false;
	})
	$('#rightlayer').on('contextmenu', function() {
		return false;
	})


	var select;
	var modify;
	var draw;
	var typeSelect = $("#type");
	var result = false;
	var featureArray = new ol.Collection();

	function Draw() {
		var value = $("#type option:selected").val();
		draw = new ol.interaction.Draw({
			source : so,
			type : value,
			wrapX : false,
			active : false,
			style : new ol.style.Style({
				fill : new ol.style.Fill({
					color : 'rgba(255, 255, 255, 0.2)'
				}),
				stroke : new ol.style.Stroke({
					color : '#ffcc33',
					width : 2
				}),
				image : new ol.style.Circle({
					radius : 7,
					fill : new ol.style.Fill({
						color : '#ffcc33'
					})
				})
			})
		});
		draw.setActive(result);
		map.addInteraction(draw);

	}
	function init() {
		select = new ol.interaction.Select();
		map.addInteraction(select);
		modify = new ol.interaction.Modify({
			features : select.getFeatures() 
		});
		map.addInteraction(modify);
		setEvents(select);
	};
	function setEvents(select) {
		var selectedFeatures = select.getFeatures();
		select.on('change:active', function() {
			selectedFeatures.forEach(selectedFeatures.remove, selectedFeatures);
		});
	}
	;
	init();
	function setActive(active, select, modify) {
		select.setActive(active); 
		modify.setActive(active); 
	};
	typeSelect.change(function() {
		map.removeInteraction(draw);
		Draw();
	});

	Draw();

	setActive(false, select, modify);
	$("#draw").click(function() {
		result = true;
		Draw();
		setActive(false, select, modify);
	});
	$("#modify").click(function() {
		result = false;
		setActive(true, select, modify); 
		map.removeInteraction(draw);
	});
	$("#dist").click(function() {
		var distance;//ol.proj.transform(points[0], 'EPSG:4326', 'EPSG:3857')
		var fromdis = ol.proj.transform(geometrydeal[0], 'EPSG:3857', 'EPSG:4326');
		var todis = ol.proj.transform(geometrydeal[geometrydeal.length-1], 'EPSG:3857', 'EPSG:4326');
		var line = new ol.geom.LineString(geometrydeal);
		distance = Math.round(line.getLength() * 100) / 100;
		if (distance >= 1000) {
			distance = (Math.round(distance / 1000 * 100) / 100) +
				' ' + 'km';
		} else {
			distance = Math.round(distance) +
				' ' + 'm';
		}
		geometrydeal = [];
		 document.getElementById('disresult').innerHTML= distance;
		  document.getElementById('disfrom').innerHTML= fromdis;
		   document.getElementById('disto').innerHTML= todis;

	});
	
	$("#range").click(function() {
		var polygonstring= ol.proj.transform(geometrydeal[0], 'EPSG:3857', 'EPSG:4326');
		for(i=1;i<geometrydeal.length-1;i++)
		{
			polygonstring = polygonstring+" "+ ol.proj.transform(geometrydeal[i], 'EPSG:3857', 'EPSG:4326');
		}
		polygonstring = polygonstring+" "+ ol.proj.transform(geometrydeal[0], 'EPSG:3857', 'EPSG:4326');	
		document.getElementById('order_range').innerHTML= polygonstring;	
		doRequestUsingGET_Range();
	});
	 function showDis()  
		{  
		 document.getElementById("distanceid").style.display ="block"; 
		 document.getElementById("rangeid").style.display ="none";
		}  
		function showRang()  
		{ 
		 document.getElementById("distanceid").style.display ="none";  
		document.getElementById("rangeid").style.display ="block";
        
		}
</script>
