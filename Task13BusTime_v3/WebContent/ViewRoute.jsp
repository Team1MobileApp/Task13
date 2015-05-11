<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%
	String fromAddr = request.getParameter("fromAddr");
	String toAddr = request.getParameter("toAddr");
	System.out.println(fromAddr);
	pageContext.setAttribute("fromAddr", fromAddr);
%>
    
<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
    <style type="text/css">
      html { height: 100% }
      body { height: 100%; margin: 0; padding: 0 }
      #map_canvas { height: 100% }
    </style>
    <script type="text/javascript"
      src="http://maps.googleapis.com/maps/api/js?key=AIzaSyB8tJ-LFoxKHQtFuWqiNgRjmQ895kTELvo&sensor=SET_TO_TRUE_OR_FALSE">
    </script>
    <script type="text/javascript">
    function initialize() {
    	  var myLatLng = new google.maps.LatLng(0, -180);
    	  var mapOptions = {
    	    zoom: 3,
    	    center: myLatLng,
    	    mapTypeId: google.maps.MapTypeId.TERRAIN
    	  };

    	  var map = new google.maps.Map(document.getElementById("map_canvas"),
    	      mapOptions);
    	  var flightPlanCoordinates = [
    	    new google.maps.LatLng(37.772323, -122.214897),
    	    new google.maps.LatLng(21.291982, -157.821856),
    	    new google.maps.LatLng(-18.142599, 178.431),
    	    new google.maps.LatLng(-27.46758, 153.027892)
    	  ];
    	  var flightPath = new google.maps.Polyline({
    	    path: flightPlanCoordinates,
    	    strokeColor: "#FF0000",
    	    strokeOpacity: 1.0,
    	    strokeWeight: 2
    	  });

    	  flightPath.setMap(map);
    	}
      
    </script>
  </head>
  <body onload="initialize()">
    <table style="height:100%; width:100%">
    <tr style="height:100%">
    <td width="20%">
        <c:forEach var="route" items="${Routes}">
        <p><c:out value="${route.summary}"/><br/>Estimated Arrival: ${route.estimatedArrivalTime}</p>
        </c:forEach>
        
    </td>
    <td width="80%"><div id="map_canvas" style="width:100%; height:100%"></div></td>
    </tr>
    </table>
     </body>
</html>