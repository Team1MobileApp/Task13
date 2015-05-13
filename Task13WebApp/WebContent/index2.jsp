<%@ page import="java.util.*" %>
<%@ page import="databeans.Prediction" %>
<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content="">
<meta name="author" content="">
<title>PAAC | Waiting Time For Buses</title>

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">
 
<!-- Custom CSS -->
<link href="css/modern-business.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">
<!--autocomplete -->
<!-- HTML5 Shim and Respond.js IE8 support of HTML5 elements and media queries -->
<!-- WARNING: Respond.js doesn't work if you view the page via file:// -->
<!--[if lt IE 9]>
        <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
        <script src="https://oss.maxcdn.com/libs/respond.js/1.4.2/respond.min.js"></script>
    <![endif]-->   
</head>

<body>
<!-- Navigation -->
<nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
  <div class="container"> 
    <!-- Brand and toggle get grouped for better mobile display -->
    <div class="navbar-header">
      <button type="button" class="navbar-toggle" data-toggle="collapse" data-target="#bs-example-navbar-collapse-1"> <span class="sr-only">Toggle navigation</span> <span class="icon-bar"></span> <span class="icon-bar"></span> <span class="icon-bar"></span> </button>
      <a class="navbar-brand" href="index.html">PortAuthority | Allegheny County</a> </div>
    <!-- Collect the nav links, forms, and other content for toggling -->
    <div class="collapse navbar-collapse" id="bs-example-navbar-collapse-1">
      <ul class="nav navbar-nav navbar-right">
        <li class="active"> <a href="index.html">Waiting Time</a> </li>
        <li> <a href="services.html">Plan a Trip</a> </li>
        <li class="dropdown"> <a href="#" class="dropdown-toggle" data-toggle="dropdown">More<b class="caret"></b></a>
          <ul class="dropdown-menu">
            <li> <a href="about.html">About Us</a> </li>
            <li> <a href="download.html">Download App</a> </li>
            <li> <a href="download.html">Privacy</a> </li>
          </ul>
        </li>
      </ul>
    </div>
    <!-- /.navbar-collapse --> 
  </div>
  <!-- /.container --> 
</nav>

<!-- Page Content -->
<div class="container"> 
  <!-- Page Heading-->
  <div class="row">
    <div class="col-lg-12">
    <h2 class="page-header">Waiting Time <small> at Bus Stop 
<%
String stopIdToUse = (String) request.getAttribute("stopIdToUse");
if (request.getAttribute("predictOne") == null) {
%>
	Not available
<%
} else {
	Prediction predictOne = (Prediction) request.getAttribute("predictOne");
%>
    <%=predictOne.getStopName()%></small> </h2>
    </div>
  </div>
  <!-- /.row --> 
  <div class="row" align="center">
	<div class="col-lg-12">
	    <h5><%=predictOne.getRouteId()%> <%=predictOne.getDirection()%> | <%=predictOne.getRouteName()%> in <b><font color="#FF0000"><%=predictOne.getWaitTime()%></font> minutes (ETA <%=predictOne.getPredictTime()%>) </b></h5>
    </div>
</div>
<hr>
<%
}
List<Prediction> predictAll = new ArrayList<Prediction>();
if (request.getAttribute("predictAll") != null) {
predictAll = (List<Prediction>) request.getAttribute("predictAll");
%>

<div class="row" align="center">
	<div class="col-lg-12">
<table class="table table-striped">
      <thead>    
          <th>ROUTE</th>
          <th>DIRECTION</th>
          <th>WAITING TIME</th>
          <th>ETA</th>
      </thead>
      <tbody>
    <% for (Prediction predict : predictAll) {
		double lat = predict.getLat();
		double lon = predict.getLon();
	%>
	<tr>
          <td><%= predict.getRouteId() %></td>
          <td><%= predict.getDirection() %></td>
          <td><font color="#FF0000"><%=predict.getWaitTime()%></font> Minutes</td>
          <td>(<%=predict.getPredictTime()%>)</td>
     </tr>
<%
   }
}
%>

    </tbody>

</table>
    </div>
</div>

<hr>
  <!-- Google Map (Real Time Tracker) -->
<div class="row" align="center">
 <div class="col-lg-12">
 	<div id="map-canvas" style="height:500px; width:100%; margin:10px"></div>
 </div>
</div> 
  <!-- /.Google Map -->

  <!-- Buttons --> 
  <div class="row" align="center">
	<div class="col-lg-12">
     <div class="btn-group" role="group" aria-label="...">
  		<button type="button" class="btn btn-success" onClick="window.location.reload();">Refresh</button>
    	<button type="button" class="btn btn-primary" onClick="window.location.href='index.jsp'">Go Back</button>
    </div>
  </div>
  </div>
  <!-- /.row -->
  
  <hr>
  <!-- Footer -->
  <footer>
    <div class="row">
      <div align="center">
        <p> <font size="-1">
        <a href="#">Download Mobile App</a>
        <br>Copyright 2015 &copy; Port Authority of Allegheny County</font></p>
      </div>
    </div>
  </footer>
</div>
<!-- /.container --> 

<!-- jQuery --> 
<script src="js/jquery.js"></script> 

<!-- Bootstrap Core JavaScript --> 
<script src="js/bootstrap.min.js"></script>

    
<!-- Google Map JavaScript -->
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=true&language=en"></script>
    <script>
    
    var map;
    function initialize() {
      var mapOptions = {
        zoom: 14,
        mapTypeId: google.maps.MapTypeId.ROADMAP
      };
      map = new google.maps.Map(document.getElementById('map-canvas'),
          mapOptions);

      // Try HTML5 geolocation
      // set current location
      if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
          var pos = new google.maps.LatLng(position.coords.latitude,
                                           position.coords.longitude);
		  var posicon = 'img/curlocation.png';
          var infowindow = new google.maps.InfoWindow({
            map: map,
            position: pos,
            icon: posicon,
            content: 'My location'
          });

          map.setCenter(pos);
        }, function() {
          handleNoGeolocation(true);
        });
      } else {
        // Browser doesn't support Geolocation
        handleNoGeolocation(false);
      }
      
      <% for(Prediction route : predictAll) {
      	String routeId = route.getRouteId();
      	double lat = route.getLat();
      	double lon = route.getLon();
      	System.err.println("in the page: " + routeId + ", " + lat + ", " + lon);
      %>
      	var image = 'img/bus.png';
      	var myLat = <%=lat%>;
      	var myLon = <%=lon%>;
      	var myId = '<%=routeId%>';
      				
      	var myLatlng = new google.maps.LatLng(myLat, myLon);
      	console.log("myLatlng = " + myLatlng);
      	var marker = new google.maps.Marker({
            position: myLatlng,
            title: myId,
            icon: image
        });
      	console.log("marker = " + marker);
     	// To add the marker to the map, call setMap();
      	marker.setMap(map);
      <%
      }
      %>
    }
    
    function setMarkers(map, routes) {
    	for (var i = 0; i < routes.length; i++) {
    		var location = routes[i];
    		console.log(i + " = " + location);
    		var myLatlng = new google.maps.LatLng(location[1], location[2]);
    		var marker = new google.maps.Marker({
    	        position: myLatLng,
    	        map: map,
    	        title: location[0],
    	    });
    	}
    }

    function handleNoGeolocation(errorFlag) {
      if (errorFlag) {
        var content = 'Error: The Geolocation service failed.';
      } else {
        var content = 'Error: Your browser doesn\'t support geolocation.';
      }

      var options = {
        map: map,
        position: new google.maps.LatLng(60, 105),
        content: content
      };

      var infowindow = new google.maps.InfoWindow(options);
      map.setCenter(options.position);
    }

    google.maps.event.addDomListener(window, 'load', initialize);

    </script>
<!-- /.Google Map JavaScript -->  

</body>
</html>
