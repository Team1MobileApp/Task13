<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Test Cookies</title>



<script type="text/javascript">
function setCookie(cname, cvalue, exdays) {
    var d = new Date();
    d.setTime(d.getTime() + (exdays*24*60*60*1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = cname + "=" + cvalue + "; " + expires;
}
</script>

<script type="text/javascript">
function getCookie(cname) {
    var name = cname + "=";   
    var ca = document.cookie.split(';');
    for(var i=0; i<ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0)==' ') c = c.substring(1);
        if (c.indexOf(name) == 0) return c.substring(name.length,c.length);
    }
    return "";
}
</script>

<script type="text/javascript">
function storeValues(form) {
    setCookie("stop",form.stop.value);
    setCookie("route",form.route.value);
    return true;
}
</script>

<script type="text/javascript">
var expired = new Date(today.getTime() - 24 * 3600 * 1000);
function deleteCookie(name){
	document.cookie=name + "=null; path=/; expires=" + expired.toGMTString();
}
</script>

<script type="text/javascript">
function clearCookies(){
	deleteCookie("stop");
	deleteCookie("route");
}
</script>

</head>

<body>
<form name="form" method="post" action="testSetCookies.jsp" onsubmit="return clearCookies();">
<ul>
	<li>stop: <input type="text" name="stop" id="stop"></li>
	<li>route: <input type="text" name="route" id="route"></li>	
</ul>
<script type="text/javascript">
	var myStop=getCookie("stop");
	var myRoute=getCookie("route")
	document.getElementById("stop").setAttribute('value',myStop);
	document.getElementById("route").setAttribute('value',myRoute);
</script>
<input type="submit" value="Clear Cookies">
</form>
</body>
</html>