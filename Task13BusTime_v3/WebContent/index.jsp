<!DOCTYPE html>
<html lang="en">
<head>
<meta charset="utf-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1">
<meta name="description" content="">
<meta name="author" content="">
<title>PAAC | Waiting Time For Buses</title>

<!-- Bootstrap Core CSS -->
<link href="css/bootstrap.min.css" rel="stylesheet">
 
<!-- Custom CSS -->
<link href="css/modern-business.css" rel="stylesheet">

<!-- Custom Fonts -->
<link href="font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

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
      <h1 class="page-header">Waiting Time <small> at Bus Stop</small> </h1>
    </div>
  </div>
  <!-- /.row --> 

  <!-- Wait Time Form --> 
  <div class="row" align="center">
	<div class="col-lg-12">
     <form method="GET" name="waitingTime" id="waitingForm" action="manage.do">
        <div class="control-group form-group">
          <div class="controls">
            <input name="route" type="select" list="routeIds" class="form-control" id="route" placeholder="Please enter Bus Route Number, ex: 71A" required data-validation-required-message="Please enter a bus route number" >
            <datalist id="routeIds">
            <option value="1 Freeport Road">
            <option value="19L Emsworth Limited">
            <option value="20 Kennedy">
            <option value="21 Coraopolis">
            <option value="22 McCoy">
            <option value="24 West Park">
            <option value="26 Chartiers">
            <option value="27 Fairywood">
            <option value="28X Airport Flyer">
            <option value="29 Robinson">
            <option value="31 Bridgeville">
            <option value="36 Banksville">
            <option value="38 Green Tree">
            <option value="39 Brookline">
            <option value="40 Mount Washington">
            <option value="41 Bower Hill">
            <option value="43 Bailey">
            <option value="44 Knoxville">
            <option value="48 Arlington">
            <option value="51 Carrick">
            <option value="51L Carrick Limited">
            <option value="52L Homeville Limited">
            <option value="53 Homestead Park">
            <option value="53L Homestead Park Limited">
            <option value="54 North Side-Oakland">
            <option value="55 Glassport">
            <option value="56 Lincoln Place">
            <option value="57 Hazelwood">
            <option value="58 Greenfield">
            <option value="59 Mon Valley">
            <option value="60 Walnut-Crawford Village">
            <option value="61A North Braddock">
            <option value="61B Braddock-Swissvale">
            <option value="61C McKeesport-Homestead">
            <option value="61D Murray">
            <option value="64 Lawrenceville-Waterfront">
            <option value="65 Squirrel Hill">
            <option value="67 Monroeville">
            <option value="68 Braddock Hills">
            <option value="69 Trafford">
            <option value="71 Edgewood Town Center">
            <option value="71A Negley">
            <option value="71B Highland Park">
            <option value="71C Point Breeze">
            <option value="71D Hamilton">
            <option value="74 Homewood-Squirrel Hill">
            <option value="75 Ellsworth">
            <option value="77 Penn Hills">
            <option value="78 Oakmont">
            <option value="79 East Hills">
            <option value="81 Oak Hill">
            <option value="82 Lincoln">
            <option value="83 Bedford Hill">
            <option value="86 Liberty">
            <option value="87 Friendship">
            <option value="88 Penn">
            <option value="89 Garfield Commons">
            <option value="91 Butler Street">
            <option value="93 Lawrenceville-Hazelwood">
            <option value="G2 West Busway">
            <option value="G3 Moon Flyer">
            <option value="G31 Bridgeville Flyer">
            <option value="O1 Ross Flyer">
            <option value="O5 Thompson Run Flyer">
            <option value="O12 McKnight Flyer">
            <option value="P1 East Busway-All Stops">
            <option value="P2 East Busway Short">
            <option value="P3 East Busway-Oakland">
            <option value="P7 McKeesport Flyer">
            <option value="P10 Allegheny Valley Flyer">
            <option value="P12 Holiday Park Flyer">
            <option value="P13 Mount Royal Flyer">
            <option value="P16 Penn Hills Flyer">
            <option value="P17 Lincoln Park Flyer">
            <option value="P67 Monroeville Flyer">
            <option value="P68 Braddock Hills Flyer">
            <option value="P69 Trafford Flyer">
            <option value="P71 Swissvale Flyer">
            <option value="P76 Lincoln Highway Flyer">
            <option value="P78 Oakmont Flyer">
            <option value="Y1 Large Flyer">
            <option value="Y45 Baldwin Manor Flyer">
            <option value="Y46 Elizabeth Flyer">
            <option value="Y47 Curry Flyer">
            <option value="Y49 Prospect Flyer">
            </datalist> 
            
          <p class="help-block"></p>

	   <div class="btn-group" role="group" aria-label="...">
	  <input name="direction" type="hidden" value="">
	  <button type="button" id="inbound" class="btn btn-default" onclick="inbound()">Inbound</button>
	  <button type="button" id="outbound" class="btn btn-default" onclick="outbound()">Outbound</button>
	  </div>  
            
            <p class="help-block"></p>

            <input type="select" list="streets" class="form-control" id="name" name="stop" placeholder="Please select bus stop, ex: Centre Ave and 5th" required data-validation-required-message="Please enter a bus route number" >
            <datalist id="streets">
             <option value="Forbes Ave opp Craig St">
            <option value="5th Ave at Bellefield Ave">
            <option value="5th Ave at Bigelow Blvd">
            <option value="5th Ave at Chesterfield Rd">
            <option value="5th Ave at Robinson St">
            <option value="5th Ave at Thackeray Ave">
            <option value="5th Ave opp Atwood St">
            <option value="Centre Ave at Melwood Ave">
            <option value="Centre Ave at Morewood Ave">
            <option value="31st St Bridge Ramp at Rialto St">
            <option value="4th Ave at 7th">
            <option value="4th Ave at Central City Plaza">
            <option value="7th St opp New Ken Hospital">
            <option value="7th past Penn Ave">
            <option value="9th St at Central City Plaza">
            <option value="Buttler St at Washington St">
            <option value="Buttler St opp Center St">
            <option value="Buttler St opp Isabella St">
            </datalist> 

	  
        </div>
        <br><br>
           <input name="Checkbox1" type="checkbox" id="Checkbox1" value="allbuses" checked />
		   <label for="Checkbox1">Show all buses arriving to stop</label>
         <br><br>
                 
        <div id="success"></div>
        <!-- For success/fail messages -->
<!--       <form method="get" name="waitingTime" id="waitingForm" action="manage.do"> -->
        <input type="submit" class="btn btn-primary" value="Get Times">
      </form>
    </div>
  </div>
  <!-- /.row -->
  
  <hr>
  <!-- Footer -->
  <footer>
    <div class="row">
      <div align="center">
        <p> <font size="-1">Copyright 2015 &copy; Port Authority of Allegheny County</font></p>
      </div>
    </div>
  </footer>
</div>
<!-- /.container --> 

<!-- jQuery --> 
<script src="js/jquery.js"></script> 

<!-- Bootstrap Core JavaScript --> 
<script src="js/bootstrap.min.js"></script>

<!-- Submit additional JavaScript -->
<script type="text/javascript">
	function inbound() {
		document.waitingTime.direction.value = "INBOUND"
	}
	function outbound() {
		document.waitingTime.direction.value = "OUTBOUND"
	}
	$(function() {
		$( "#inbound" ).button();
		$( "#outbound" ).button();
	});
	
</script>
</body>
</html>
