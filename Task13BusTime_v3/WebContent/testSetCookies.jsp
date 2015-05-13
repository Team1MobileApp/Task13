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

</head>

<body>
<form name="form" method="post" action="testGetCookies.jsp" onsubmit="return storeValues(this);">
<ul>
	<li>stop: <input type="text" name="stop" value=""></li>
	<li>route: <input type="text" name="route" value=""></li>
</ul>
<input type="submit" value="Set Cookies">
</form>
</body>
</html>