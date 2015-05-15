<%@ page language="java" contentType="text/html; charset=UTF-8"
  pageEncoding="UTF-8"%>
<%@ page import="databeans.*"%>
<%@ page import="java.util.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>

<!DOCTYPE html>
<html>
<head>
<link href="css/bootstrap.min.css" rel="stylesheet">

<!-- Custom CSS -->
<link href="css/modern-business.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet"
  type="text/css">
<meta name="viewport" content="initial-scale=1.0, user-scalable=no" />
<style type="text/css">
html {
  height: 100%
}

body {
  height: 100%;
  margin: 0;
  padding: 0
}

#map_canvas {
  height: 100%
}
</style>
<script type="text/javascript"
  src="http://maps.googleapis.com/maps/api/js?key=AIzaSyB8tJ-LFoxKHQtFuWqiNgRjmQ895kTELvo&sensor=SET_TO_TRUE_OR_FALSE">
    </script>
<script type="text/javascript">
    var polylines = [];
    var map;
    var directionsService = new google.maps.DirectionsService();
    var curPos;
    var directionsDisplay;
    var routeNames = [];
    function getPos(pos)
    {
      curPos = pos;
    }
    function initMap()
    {
      if (map == null)
      {
        var myLatLng;
            if (false && navigator.geolocation) {
                navigator.geolocation.getCurrentPosition(getPos);
                myLatLng = new google.maps.LatLng(curPos.coords.latitute, curPos.coords.longitute);
            } 
            else
                myLatLng = new google.maps.LatLng(40.453970, -79.944623);
         
         var mapOptions = {
                    zoom: 7,
                    center: myLatLng,
                    mapTypeId: google.maps.MapTypeId.ROADMAP
                  };
        map = new google.maps.Map(document.getElementById("map_canvas"),
                  mapOptions);
          directionsDisplay.setMap(map);
      }
    }
    function initialize() {
        
        directionsDisplay = new google.maps.DirectionsRenderer();
        
        //directionsDisplay.setPanel(document.getElementById("directionsPanel"));
        
        <%
        List<CandidateRoute> routes = (List<CandidateRoute>)(session.getAttribute("Routes"));
          for(int i = 0; i < routes.size(); i++) { 
          %>
              routeNames[<%=i%>] = "<%=routes.get(i).getSummary()%>";
          var coordinates = [
            <%for (WayPoint p : routes.get(i).getWayPoints()) {%>
             new google.maps.LatLng(<%=p.getLatitute()%>, <%=p.getLongitute()%>),
          <%}%>
          ];
          var poly = new google.maps.Polyline({
            path: coordinates,
            strokeColor: "#FF0000",
            strokeOpacity: 1.0,
            strokeWeight: 2
          });
          polylines[polylines.length] = poly;
        <%}%>
        
        var start = "<%=session.getAttribute("origin")%>";
        var end = "<%=session.getAttribute("destination")%>";
        var request = {
          origin:start,
          destination:end,
          travelMode: google.maps.TravelMode.TRANSIT,
          provideRouteAlternatives: true,
          transitOptions:
          {
            <%
            Object rdepartureTime = session.getAttribute("departureTime");
            Object rarrivalTime = session.getAttribute("arrivalTime");
            if (session.getAttribute("departureTime") != null) { %>
              departureTime: new Date(<%=rdepartureTime%>)
            <%}else{%>
              arrivalTime: new Date(<%=rarrivalTime%>)
            <%}%>
          }
        };
        directionsService.route(request, function(response, status) {
          if (status == google.maps.DirectionsStatus.OK) {
            directionsDisplay.setDirections(response);
          }
        });
      }
    function showRoute(id)
    {
        initMap();
      directionsDisplay.setRouteIndex(id);
    }
    </script>
</head>

<body onload="initialize()">

<div class="containder">
  <!-- Navigation -->
  <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
    <div class="container">
      <!-- Brand and toggle get grouped for better mobile display -->
      <div class="navbar-header">
        <button type="button" class="navbar-toggle" data-toggle="collapse"
          data-target="#bs-example-navbar-collapse-1">
          <span class="sr-only">Toggle navigation</span> <span
            class="icon-bar"></span> <span class="icon-bar"></span> <span
            class="icon-bar"></span>
        </button>
        <a class="navbar-brand" href="index.jsp">PAAC</a>
      </div>
      <!-- Collect the nav links, forms, and other content for toggling -->
      <div class="collapse navbar-collapse"
        id="bs-example-navbar-collapse-1">
        <ul class="nav navbar-nav navbar-right">
          <li><a href="index.jsp">Waiting Time</a></li>
          <li class="active"><a href="services.html">Trip Planner</a></li>
          <li class="dropdown"><a href="#" class="dropdown-toggle"
            data-toggle="dropdown">More<b class="caret"></b></a>
            <ul class="dropdown-menu">
              <li><a href="download.html">Download App</a></li>
              <li><a href="privacy.html">Privacy Statement</a></li>
            </ul></li>
        </ul>
      </div>
      <!-- /.navbar-collapse -->
    </div>
    <!-- /.container -->
  </nav>

  <br />
  <br />
  <br />
  
  		<!-- Buttons -->
		<div class="row" align="center">
			<div class="col-lg-12">
				<div class="btn-group" role="group" aria-label="...">
					<button type="button" class="btn btn-success"
						onClick="window.location.reload();">Refresh</button>
					<button type="button" class="btn btn-danger"
						onClick="window.location.href='services.html'">Go Back</button>
				</div>
			</div>
		</div>
		<!-- /.row -->
		
  <br />
  
  <!-- Page Heading-->
  <div class="row" style="margin-up: 150px;height:100%">
    <div class="col-lg-8, col-md-offset-2" style="height:100%">
      <table style="height: 100%; width: 100%; margin-left: 20px">
        <tr style="height: 100%">
          <td width="380px" style="height:100%;vertical-align:top">
          <div class="list-group">
            <% List<RouteEstimation> estimations = (List<RouteEstimation>)session.getAttribute("Estimations"); %>
            <% for (int i = 0; i < routes.size(); i++) { %>
            <h4 class="list-group-item-heading">
              <a href="#" onclick="showRoute(<%=i%>);" class="list-group-item"><%=routes.get(i).getSummary() %></a>
              <br /><p "list-group-item-text">Estimated Arrival:
              <%=routes.get(i).getEstimatedArrivalTime()%>
              <% RouteEstimation est = estimations.get(i);
                           for (int j = 0; j<est.Segments.size(); j++)
                           {
                               GregorianCalendar cal = new GregorianCalendar();
                                cal.setTime(est.Segments.get(j).DepartureTime.getTime());

                                java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("HH:mm");
                                sdf.setTimeZone(TimeZone.getDefault());
                                String departureTime = (sdf.format(cal.getTime()));
                        %>
              <br />Bus
              <%=est.Segments.get(j).RouteName%>
              departures at
              <%=departureTime%>
              <%
                           }
                        %>
            </h4> <%} %>
            </div>
            <div id="map_canvas" style="width: 100%; height: 400px"></div>
          </td>
        </tr>
      </table>

    </div>
    </div>
    <hr>
		<!-- Footer -->
		<footer>
			<div class="row">
				<div align="center">
					<p>
						<font size="-1">Copyright 2015 &copy; Port Authority of
							Allegheny County</font>
					</p>
				</div>
			</div>
		</footer>
  </div>
  <!-- jQuery -->
  <script src="js/jquery.js"></script>

  <!-- Bootstrap Core JavaScript -->
  <script src="js/bootstrap.min.js"></script>

  <!-- Code to clear the text field when user clicks in it. -->
  <script src="js/services.js"></script>
</body>
</html>