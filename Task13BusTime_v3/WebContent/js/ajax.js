/**
 * Author: Qianwen Li
 * Date: 5/12/2015
 * 
 */

$(document).ready(function() {
    //Stops the submit request
    $('#form1').submit(function(e){
           e.preventDefault();
    });
	
    // load stop names from server/db
    $('#inbound, #outbound').click(function(e) {
    	console.log("click button");
    	//get the form data and then serialize that
        var route = $('input#route').val();
        var direction = $(this).val();
        dataString = "route=" + route + "&direction=" + direction
        console.log("dataString: " + dataString);
        
      //make the AJAX request, dataType is set to json
        $.ajax({
            type: 'POST',
            async: false,
            url: 'stopname.do',
            data: dataString,
            dataType: "json", 
            
            // If successfully get the response json
            success: function(data) {
            	console.log("successfully get response! " + data);
            	
            	$('#streets').html("");
            	$.each(data.stops, function(index) {
            		console.log("stop: " + stop);
            		$('#streets').append("<option value='" + data.stops[index].stopName + "'>" + data.stops[index].stopName + "</option>");
            		
            	});
            },
            
            //If there was no response from the server
            error: function(data, status, er) {
                 //alert("error: " + data +" status: " + status +" er:" + er);
            },
            
            //capture the request before it was sent to server
            beforeSend: function(jqXHR, settings) {
                //adding some Dummy data to the request
                settings.data += "&dummyData=whatever";
                console.log("before send");
                //disable the button until we get the response
                $(this).attr("disabled", true);
            },
            
            //this is called after the response or error functions are finished
            //so that we can take some action
            complete: function(jqXHR, textStatus) {
                //enable the button 
            	console.log("complete");
                $(this).attr("disabled", false);
            }
        });
        
        return false;
    });
    
    
});
