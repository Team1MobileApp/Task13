/******************************
 * Author: Charlotte Lin
 * Date: 2015/05/07
 ******************************/
package controller;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import sun.net.www.protocol.http.HttpURLConnection;
import model.BoundDAO;
import model.Model;
import model.RouteDAO;


import model.StopDAO;

import org.genericdao.RollbackException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import sun.net.www.protocol.http.HttpURLConnection;

public class ManageAction extends Action {

	private RouteDAO routeDAO;
	private StopDAO  stopDAO;
	private BoundDAO boundDAO;
	private static String apiKey = "ADpCvpyDcupACyuMdk5wrVTVH";
	private static String filePath = "searchResult.xml";

	public ManageAction(Model model) {
	    	RouteDAO routeDAO = model.getRouteDAO();
	    	StopDAO stopDAO = model.getStopDAO();
	    	BoundDAO boundDAO = model.getBoundDAO();
	}

	public String getName() { 
		return "manage.do"; 
	}

	public String perform(HttpServletRequest request) {
		
		// Get all route name from database
		// show on the page if no parameter passed in
		
		
		// Get all stop name from database
		// show on the page if no parameter passed in
		
		
		// Show in bound and out bound choice
		// no need code here
		
		// below are trying to use get all routes api
		
		try {
			getAllRoutes();
			System.out.println("I get here!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		// below are trying to use get all stops of some route in some direction api
		
		
		// get route stop and bound parameter from page
		
		
		// search routeId, stopId, boundName through database
		
		
		// using prediction api to get prediction xml file
		// result base on route and stop
		// need to select ones with specific bound name
		// then sort by time
		// show the first five time
		// how to show other roots result???
		// connect one stop bound with all roots.
		// need to search
		
		
		// parse xml to get result
		
		
		// show result on jsp page
		
		
		
		
        // Set up the errors list
		
		return null;
        
    }
	private static void getAllRoutes() throws IOException {
		System.out.println("I get here???");
		HttpURLConnection connection = null;
		URL url = new URL("http://truetime.portauthority.org/bustime/"
				+ "api/v2/getroutes?key=ADpCvpyDcupACyuMdk5wrVTVH&format=json");
		
		connection =  (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
				
		JSONObject obj = (JSONObject) JSONValue
				.parse(readResponse(connection));
		JSONObject results = (JSONObject) obj.get("bustime-response");
		JSONArray routes = (JSONArray) results.get("routes");
//		System.out.println("results is null: " + (results == null || results.size() == 0));
		
//		JSONObject result = (JSONObject) results.get(0);
//		JSONObject geometry = (JSONObject) result.get("geometry");
//		System.out.println("geometry" + geometry.toString());
//		JSONObject location = (JSONObject) geometry.get("location");
//		System.out.println("location" + location.toString());
		JSONObject route1 = (JSONObject) routes.get(0);
		System.out.println(route1.toString());
		String route1Id = (String) route1.get("rt");
		System.out.println(route1Id);
		String route1Name = (String) route1.get("rtnm");
		System.out.println(route1Name);
	}
	private static String readResponse(HttpURLConnection connection) {
		try {
			StringBuilder str = new StringBuilder();

			BufferedReader br = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String line = "";
			while ((line = br.readLine()) != null) {
				str.append(line + System.getProperty("line.separator"));
			}
			return str.toString();
		} catch (IOException e) {
			return new String();
		}
	}
}
