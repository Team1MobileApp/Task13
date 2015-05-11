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
        var mapOptions = {
          center: new google.maps.LatLng(-34.397, 150.644),
          zoom: 8,
          mapTypeId: google.maps.MapTypeId.ROADMAP
        };
        var map = new google.maps.Map(document.getElementById("map_canvas"),
            mapOptions);
      }
    </script>
  </head>
  <body onload="initialize()">
    <table style="height:100%; width:100%">
    <tr style="height:100%">
    <td width="20%">
        from addr: ${fuck}
        
    </td>
    <td width="80%"><div id="map_canvas" style="width:100%; height:100%"></div></td>
    </tr>
    </table>
     </body>
</html>