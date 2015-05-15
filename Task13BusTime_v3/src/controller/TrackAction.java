/*
 * Author: Qianwen Li
 * Date: 5/12/2015
 * 
 * */

package controller;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import model.Model;
import model.StopDAO;

import org.genericdao.RollbackException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import sun.net.www.protocol.http.HttpURLConnection;
import databeans.Stop;

public class TrackAction extends Action {
	private StopDAO stopDAO;
	private static String apiKey = "w9m3MfMkLEEwU7hqyGM6mU9ut";

	public TrackAction(Model model) {
		stopDAO = model.getStopDAO();
	}

	public String getName() {
		return "track.do";
	}

	public String perform(HttpServletRequest request) {
		
		// get busNumber
		String busNumber = ((String) request.getParameter("busNumber")).trim();
		System.err.println("busNumber = " + busNumber);
		
		// get new location of the busNumber
		// return the Json Object
			System.err.println("Try to get new location here");
		try {
			double[] newLoc = getLocation(busNumber);
			if (newLoc == null || newLoc.length == 0) {
				newLoc = new double[2];
				newLoc[0] = -25.363882;
				newLoc[1] = 131.044922;
			}
			JSONObject locObj = new JSONObject();
			locObj.put("lat", newLoc[0]);
			locObj.put("lon", newLoc[1]);

			System.err.println("locObj: ");
			System.err.println(locObj.toString());
			request.setAttribute("locObj", locObj);
		} catch(IOException ioe) {
			ioe.printStackTrace();
		} 
		

		return "track.ajax";
	}
	
	private double[] getLocation(String bus) throws IOException {
		HttpURLConnection connection = null;
		URL url = new URL("http://truetime.portauthority.org/bustime/"
				+ "api/v2/getvehicles?key=" + apiKey + "&vid="
				+ bus + "&format=json");
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);

		double[] location = new double[2];
		try {
		JSONObject obj = (JSONObject) JSONValue.parse(readResponse(connection));
		JSONObject error = (JSONObject) obj.get("error");
		if (error != null) {
			// error handling
			System.err.println("error: " + error.toString());
			location[0] = 40.45935874149717;
			location[1] = -79.92761098927465;
			return null;
		}

		JSONObject results = (JSONObject) obj.get("bustime-response");
		System.err.println("getLocation = " + results.toString());
		JSONArray vehicles = (JSONArray) results.get("vehicle");
		// TODO: double check here
		if (vehicles == null || vehicles.size() == 0)
			return null;
		JSONObject vehicle = (JSONObject) vehicles.get(0);
			location[0] = Double.parseDouble(((String) vehicle.get("lat"))
					.trim());
			location[1] = Double.parseDouble(((String) vehicle.get("lon"))
					.trim());
		} catch (NumberFormatException nfe) {
			nfe.printStackTrace();
		} 

		return location;
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
