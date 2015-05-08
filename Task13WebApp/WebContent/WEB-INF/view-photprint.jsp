<!-- Name: Charlotte Lin -->
<!-- Date: 01/18/2015 -->

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="databeans.User"%>
<%@ page import="databeans.Photo"%>
<%@ page import="databeans.Location"%>

<jsp:include page="template-top.jsp" />

<jsp:include page="error-list.jsp" />

<!DOCTYPE html>
<html>
  <head>
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no">
    <meta charset="utf-8">
    <title>My PhotPrint</title>
    <style>
      html, body, #map-canvas {
        height: 100%;
        margin: 10px;
        padding: 0px
      }
    </style>
    <script src="https://maps.googleapis.com/maps/api/js?v=3.exp&signed_in=true"></script>
    <script>
// The following example creates complex markers to indicate beaches near
// Sydney, NSW, Australia. Note that the anchor is set to
// (0,32) to correspond to the base of the flagpole.

function initialize() {
  var mapOptions = {
    zoom: 5,
    center: new google.maps.LatLng(40, -80)
  }
  var map = new google.maps.Map(document.getElementById('map-canvas'),
                                mapOptions);

  setMarkers(map, beaches);
}

/**
 * Data for the markers consisting of a name, a LatLng and a zIndex for
 * the order in which these markers should display on top of each
 * other.
 */
 var beaches = new Array();
 <c:forEach  var="myLocations" items="${myLocations}"> 
 beaches.push(['${myLocations.location}', ${myLocations.lat}, ${myLocations.lng}]);
 </c:forEach>

function setMarkers(map, locations) {
  // Add markers to the map

  // Marker sizes are expressed as a Size of X,Y
  // where the origin of the image (0,0) is located
  // in the top left of the image.

  // Origins, anchor positions and coordinates of the marker
  // increase in the X direction to the right and in
  // the Y direction down.
  var image = '1.png';
  // Shapes define the clickable region of the icon.
  // The type defines an HTML &lt;area&gt; element 'poly' which
  // traces out a polygon as a series of X,Y points. The final
  // coordinate closes the poly by connecting to the first
  // coordinate.
  for (var i = 0; i < locations.length; i++) {
    var beach = locations[i];
    var myLatLng = new google.maps.LatLng(beach[1], beach[2]);
    var marker = new google.maps.Marker({
        position: myLatLng,
        map: map,
        icon: image,
        title: beach[0],
    });
  }
}

google.maps.event.addDomListener(window, 'load', initialize);

    </script>
  </head>
  <body>

<!-- Page Content -->
<!-- This is a very simple parallax effect achieved by simple CSS 3 multiple backgrounds, made by http://twitter.com/msurguy -->

<div class="container">
<h2 align="center"><strong><font face="arial" color="teal">${user.userName}'s PhotPrint&nbsp&nbsp&nbsp&nbsp</font></strong></h2>
<h3 align="center"><strong><font face="comic sans MS" color="teal"><a href="viewAnalysis.do">Analyze the number places ${user.userName} has been</a></font></strong></h1>
  </br>
  <c:forEach var="myLocations" items="${myLocations}">   
  <form action="viewPhotos.do" method="POST">
  			<p align="center"><strong><font face="comic sans MS" color = "black" size="3"><c:out value = '${myLocations.location}' escapeXml='true' /></font></strong>                		
       		&nbsp&nbsp&nbsp&nbsp<strong><a href= "viewPhotos.do?location=${myLocations.location}">View Photos</a></strong>
  </form> 
  </c:forEach>   	
<div id="map-canvas"></div>  
</div>

<jsp:include page="template-bottom.jsp" />
    
  </body>
</html>