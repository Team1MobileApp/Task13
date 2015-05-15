<%@ page import="java.util.*"%>
<%@ page import="databeans.Prediction"%>
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
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet"
	type="text/css">

<!-- JQuery Mobile -->
<script src="//code.jquery.com/jquery-1.10.2.js"></script>
<script src="//code.jquery.com/ui/1.11.4/jquery-ui.js"></script>

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
					<li class="active"><a href="index.jsp">Waiting Time</a></li>
					<li><a href="services.html">Plan a Trip</a></li>
					<li class="dropdown"><a href="#" class="dropdown-toggle"
						data-toggle="dropdown">More<b class="caret"></b></a>
						<ul class="dropdown-menu">
							<li><a href="download.html">Download App</a></li>
							<li><a href="privacy.html">Privacy</a></li>
						</ul></li>
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
				<h2 class="page-header">
					<%
						String stopName = (String) request.getAttribute("stopName");
					%>
					Waiting Time <br> <small> at Bus Stop <%=stopName%></small>

				</h2>
			</div>
		</div>
		<!-- /.row -->
		<div class="row" align="center">
			<div class="col-lg-12">
				<%
					String routeOne = (String) request.getAttribute("routeId");
							Prediction predictOne = (Prediction) request.getAttribute("predictOne");
											if (predictOne == null) {
				%>
				<h4 style="color: red">
					Sorry, No scheduled service within 30 minutes for
					<%=routeOne%></h4>
				<%
					} else {
				%>
				<h4><%=predictOne.getRouteId()%>
					<%=predictOne.getDirection()%>
					|
					<%=predictOne.getRouteName()%>
					in <b><font color="#FF0000"><%=predictOne.getWaitTime()%></font>
						minutes (ETA <%=predictOne.getPredictTime()%>) </b>
				</h4>
			</div>
		</div>
		<hr>
		
		
		<%
			} 
							
				List<Prediction> predictAll = (List<Prediction>) request.getAttribute("predictAll");
				
				if (predictAll == null) {
					%>
			<div class="row" align="center">
				<div class="col-lg-12">
				<h4 style="color: red">Sorry, No scheduled service within 30 minutes for all routes.</h4>
				</div>
			</div>
					<% 
				} else {
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
						<%
							for (Prediction predict : predictAll) {
								
							double lat = predict.getLat();
							double lon = predict.getLon();
						%>
						<tr>
							<td><%=predict.getRouteId()%></td>
							<td><%=predict.getDirection()%></td>
							<td><font color="#FF0000"><%=predict.getWaitTime()%></font>
								Minutes</td>
							<td><%=predict.getPredictTime()%></td>
						</tr>
						<%
							}
						%>

					</tbody>

				</table>
			</div>
		</div>

		<hr>
		<!-- Google Map (Real Time Tracker) -->

		<div class="panel panel-default">
			<div class="panel-heading">
				<h4 class="panel-title">
					<a class="accordion-toggle" data-toggle="collapse"
						data-parent="#accordion" href="#collapseOne">Show Map</a>
				</h4>
			</div>
			<div id="collapseOne" class="panel-collapse collapse">
				<div id="map-canvas" style="height: 600px; width: 100%"></div>
			</div>
		</div>

		<!-- /.Google Map -->

		<%
			}
		%>

		<br> <br>

		<!-- Buttons -->
		<div class="row" align="center">
			<div class="col-lg-12">
				<div class="btn-group" role="group" aria-label="...">
					<button type="button" class="btn btn-success"
						onClick="window.location.reload();">Refresh</button>
					<button type="button" class="btn btn-danger"
						onClick="window.location.href='index.jsp'">Go Back</button>
				</div>
			</div>
		</div>
		<!-- /.row -->

		<hr>
		<!-- Footer -->
		<footer>
			<div class="row">
				<div align="center">
					<p>
						<font size="-1"> <a href="#">Download Mobile App</a> <br>Copyright
							2015 &copy; Port Authority of Allegheny County
						</font>
					</p>
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
	<script
		src="https://maps.googleapis.com/maps/api/js?v=3.exp&sensor=true&language=en"></script>
	<script>
	
    var map;
   
    function initialize() {
      var mapOptions = {
        zoom: 15,
        mapTypeId: google.maps.MapTypeId.ROADMAP
      };
      map = new google.maps.Map(document.getElementById('map-canvas'),
          mapOptions);

      // Try HTML5 geolocation
      // set current location
      <%Double stopLat = (Double) request.getAttribute("stopLat");
         Double stopLon = (Double) request.getAttribute("stopLon");%>
      if(navigator.geolocation) {
        navigator.geolocation.getCurrentPosition(function(position) {
          var pos = new google.maps.LatLng(position.coords.latitude,
                                           position.coords.longitude);
		  
          var marker = new google.maps.Marker({
              position: pos,
              map: map,
              title: 'My Location',
              draggable:true,
              animation: google.maps.Animation.DROP,
          });

         
          var stopLat = <%=stopLat%>;
          var stopLon = <%=stopLon%>;
          var stopPos = new google.maps.LatLng(stopLat,stopLon);
          var stopIcon = 'img/bus-stop.png'
          var stopMarker = new google.maps.Marker({
              position: stopPos,
              map: map,
              title: 'Stop',
              draggable:true,
              animation: google.maps.Animation.DROP,
              icon: stopIcon
          });
          map.setCenter(stopPos);
          
        });
      } else {
        // Browser doesn't support Geolocation
        handleNoGeolocation(false);
      }
      
      <%
      if (predictAll != null) {
      for(int i = 0; i < predictAll.size(); i++) {
    	String busNumber = predictAll.get(i).getBusNumber();
      	String routeId = predictAll.get(i).getRouteId();
      	double lat = predictAll.get(i).getLat();
      	double lon = predictAll.get(i).getLon();
      	System.err.println("in the page: " + busNumber + ", " + routeId + ", " + lat + ", " + lon);%>

      	var image = 'img/bus.png';
      	var index = <%=i%>;
      	console.log("index = " + index);
      	if (index <= 6) {
      		image = 'img/bus' + index + '.png';
      		
      	}
      	console.log("imaage = " + image);
      	var myBusNumber = '<%=busNumber%>';
      	var myLat = <%=lat%>;
      	var myLon = <%=lon%>;
      	var myId = '<%=routeId%>';

			var myLatlng = new google.maps.LatLng(myLat, myLon);
			console.log("myLatlng = " + myLatlng);
			var marker = new google.maps.Marker({
				position : myLatlng,
				title : myId,
				icon : image
			});
			console.log("marker = " + marker);
			// To add the marker to the map, call setMap();
			marker.setMap(map);

			updateMarker(marker, myBusNumber, myLat, myLon);
	<%}
    }
      
	%>
		}

		function handleNoGeolocation(errorFlag) {
			if (errorFlag) {
				var content = 'Error: The Geolocation service failed.';
			} else {
				var content = 'Error: Your browser doesn\'t support geolocation.';
			}

			var options = {
				map : map,
				position : new google.maps.LatLng(60, 105),
				content : content
			};

			var infowindow = new google.maps.InfoWindow(options);
			map.setCenter(options.position);
		}

	    $('a[href="#collapseOne"]').click(function(){
			console.log("click a href");
			$('#map-canvas').css({"width": "100%", "height": "600px"});
			/* google.maps.event.trigger(map, "resize"); */
			initialize();
		});
	    /* google.maps.event.addDomListener(window, 'load', initialize);  */
		
		// for ajax to get new location of each bus
		function updateMarker() {
			var marker = arguments[0];
			var busNumber = arguments[1];

			setInterval(function() {
				requestAjax(marker, busNumber);
			}, 1000);

		}

		function requestAjax() {
			var loc = new Array();
			var marker = arguments[0];
			var busNumber = arguments[1];
			dataString = "busNumber=" + busNumber;
			console.log("dataString = " + dataString);

			$
					.ajax({
						type : 'POST',
						async : false,
						url : 'track.do',
						data : dataString,
						dataType : "json",

						// If successfully get the response json
						success : function(data) {
							console.log("successfully get response! " + data);

							loc[0] = data.lat;
							console.log("newLat = " + loc[0]);
							loc[1] = data.lon;
							console.log("newLon = " + loc[1]);
						},

						//If there was no response from the server
						error : function(data, status, er) {
							alert("error: " + data + " status: " + status
									+ " er:" + er);
						},

						//capture the request before it was sent to server
						beforeSend : function(jqXHR, settings) {
							//adding some Dummy data to the request
							settings.data += "&dummyData=whatever";
							console.log("before send");
						},

						//this is called after the response or error functions are finished
						//so that we can take some action
						complete : function(jqXHR, textStatus) {
							//enable the button 
							console.log("complete");
						}
					});

			var position = new google.maps.LatLng(loc[0], loc[1]);
			marker.setPosition(position);
		}
	</script>
	<!-- /.Google Map JavaScript -->

</body>
</html>
