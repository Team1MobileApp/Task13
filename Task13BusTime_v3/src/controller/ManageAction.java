/******************************
 * Author: Charlotte Lin
 * Date: 2015/05/07
 ******************************/
package controller;

import java.io.*;
import java.net.*;
import java.util.*;

import javax.servlet.http.HttpServletRequest;

import sun.net.www.protocol.http.HttpURLConnection;
import model.Model;
import model.StopDAO;

import org.genericdao.RollbackException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import databeans.Prediction;
import databeans.Stop;

public class ManageAction extends Action {

	private StopDAO stopDAO;
	private static String apiKey = "ADpCvpyDcupACyuMdk5wrVTVH";

	public ManageAction(Model model) {
		stopDAO = model.getStopDAO();
	}

	public String getName() {
		return "manage.do";
	}

	public String perform(HttpServletRequest request) {
		// if database is empty, initial stop table
		try {
			if (stopDAO.getStopCount() == 0) {
				setData();
			}
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		} catch (RollbackException re) {
			re.printStackTrace();
		}
		
		// get route
		String[] route = ((String) request.getParameter("route")).trim().split(
				" ");
		String routeIdToUse = route[0];
		System.err.println("routeId = " + routeIdToUse);

		// get direction
		String dir = (String) request.getParameter("direction");
		System.err.println("dir = " + dir);

		// get stop
		String stopIdToUse = "36";
		// TODO: fix this bug
//		String stopName =  ((String) request.getParameter("stop")).trim();
//		System.err.println("stopName = " + stopName);
//		try {
//			Stop[] stops = stopDAO.getStopId(dir, stopName);
//			if (stops != null) {
//				stopIdToUse = stops[0].getStopId();
//			}
//			System.err.println("stopIdToUse = " + stopIdToUse);
//		} catch (RollbackException re) {
//			re.printStackTrace();
//		}
		// get the check box checked or not (all routes or single route)
		boolean checked = "allbuses".equals((String) request.getParameter("Checkbox1")) ? true
				: false;
		System.err.println("checked = " + checked);

		// get result
		try {
			// getVehicle(routeIdToUse);
			request.setAttribute("stopIdToUse", stopIdToUse);
			
			// get single route prediction time
			// call getTime()
			Prediction predictOne = getTime(routeIdToUse, stopIdToUse, dir);
			request.setAttribute("predictOne", predictOne);
			
			// If all buses checked, get all time
			// call getAllTime()
			List<Prediction> predictAll = new ArrayList<Prediction>();
			if (checked) {
				predictAll = getAllTime(stopIdToUse, dir);
			}
			request.setAttribute("predictAll", predictAll);

		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "index2.jsp";
	}
	
	private void setData() throws IOException, RollbackException {
		JSONArray routes = getAllRoutes();
		Set<String> stopSet = new HashSet<String>();
		for (int i = 0; i < routes.size(); i++) {
			JSONObject route = (JSONObject) routes.get(i);
			String routeId = (String) route.get("rt");
			String bound = "INBOUND";
			JSONArray stops = getStops(routeId, bound);
			for (Object stop : stops) {
				String stopId = (String) ((JSONObject) stop).get("stpid");
				//if (stopSet.contains(stopId)) continue;
				//stopSet.add(stopId);
				String stopName = (String) ((JSONObject) stop).get("stpnm");
				Stop newStop = new Stop(bound, stopName, stopId);
				stopDAO.createStop(newStop);
			}
			bound = "OUTBOUND";
			stops = getStops(routeId, bound);
			for (Object stop : stops) {
				String stopId = (String) ((JSONObject) stop).get("stpid");
				//if (stopSet.contains(stopId)) continue;
				//stopSet.add(stopId);
				String stopName = (String) ((JSONObject) stop).get("stpnm");
				Stop newStop = new Stop(bound, stopName, stopId);
				stopDAO.createStop(newStop);
			}
		}
	}
	
	private static JSONArray getAllRoutes() throws IOException, RollbackException {
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
		return routes;
	}
	
	private static JSONArray getStops(String routeId, String bound) throws IOException {
		HttpURLConnection connection = null;
		URL url = new URL("http://truetime.portauthority.org/bustime/"
				+ "api/v2/getstops?key=ADpCvpyDcupACyuMdk5wrVTVH"
				+ "&rt=" + routeId + "&dir=" + bound 
				+ "&format=json");
		connection =  (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
				
		JSONObject obj = (JSONObject) JSONValue
				.parse(readResponse(connection));
		JSONObject results = (JSONObject) obj.get("bustime-response");
		JSONArray stops = (JSONArray) results.get("stops");
		return stops;
	}

	private List<Prediction> getAllTime(String stopId, String dir)
			throws IOException {
		HttpURLConnection connection = null;
		URL url = new URL("http://truetime.portauthority.org/bustime/"
				+ "api/v2/getpredictions?key=" + apiKey + "&stpid=" + stopId
				+ "&format=json");
		System.out.println(url.toString());
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);

		JSONObject obj = (JSONObject) JSONValue.parse(readResponse(connection));
		JSONObject error = (JSONObject) obj.get("error");
		if (error != null) {
			System.err.println("error: " + error.toString());
			return null;
		}
		JSONObject results = (JSONObject) obj.get("bustime-response");
		System.out.println(results.toString());

		JSONArray times = (JSONArray) results.get("prd");
		System.out.println("Times is null: " + (times == null));
		JSONObject[] allTime = new JSONObject[times.size()];
		for (int i = 0; i < times.size(); i++) {
			allTime[i] = (JSONObject) times.get(i);
		}
		List<Prediction> predictList = new ArrayList<Prediction>();
		for (JSONObject jo : allTime) {
			// direction
			String direction = ((String) jo.get("rtdir")).trim();
			System.err.println("getDir = dir? " + dir.equals(direction));
			if (!dir.equals(direction))
				continue;

			String[] prediction = new String[2];
			prediction[0] = ((String) jo.get("tmstmp")).trim();
			prediction[1] = ((String) jo.get("prdtm")).trim();

			String predictTime = "";
			String waitTime = "";
			int gapTime = 0;
			try {
				gapTime = (Integer
						.parseInt(prediction[1].substring(
								prediction[1].length() - 5,
								prediction[1].length() - 3)) * 60 + Integer
						.parseInt(prediction[1].substring(prediction[1]
								.length() - 2)))
						- (Integer.parseInt(prediction[0].substring(
								prediction[0].length() - 5,
								prediction[0].length() - 3)) * 60 + Integer
								.parseInt(prediction[0].substring(prediction[0]
										.length() - 2)));
				System.err.println(gapTime);
				// predictionTime[0] is the waiting time in minute form
				waitTime = String.valueOf(gapTime);
				// predictionTime[1] is the prediction time in time form
				predictTime = prediction[1]
						.substring(prediction[1].length() - 5);
			} catch (NumberFormatException nfe) {
				nfe.printStackTrace();
			}

			// routeId
			String routeId = (String) jo.get("rt");
			// routeName
			String routeName = (String) jo.get("des");
			// stopName
			String stopName = (String) jo.get("stpnm");
			// busNumber
			String busNumber = (String) jo.get("vid");
			double[] location = getLocation(busNumber);
			predictList.add(new Prediction(routeId, routeName, stopName,
					predictTime, waitTime, direction, busNumber, location[0],
					location[1]));
		}
		
		return predictList;

	
	}

	private Prediction getTime(String routeId, String stopId, String dir)
			throws IOException {
		HttpURLConnection connection = null;
		URL url = new URL("http://truetime.portauthority.org/bustime/"
				+ "api/v2/getpredictions?key=" + apiKey + "&rt=" + routeId
				+ "&stpid=" + stopId + "&format=json");
		System.out.println(url.toString());
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);

		JSONObject obj = (JSONObject) JSONValue.parse(readResponse(connection));
		JSONObject error =  (JSONObject) obj.get("error");
		if (error != null) {
			System.err.println("error: " + error);
			return null;
		}
		JSONObject results = (JSONObject) obj.get("bustime-response");
		
		System.out.println(results.toString());	
			JSONArray times = (JSONArray) results.get("prd");
			System.out.println("Times is null: " + (times == null));
			JSONObject time = (JSONObject) times.get(0);
			String[] prediction = new String[2];
			String direction = ((String) time.get("rtdir")).trim();
			String predictTime = "";
			String waitTime = "";
			// TODO: if there's no match here, modify the error message
			if (!dir.equals(direction)) {
				waitTime = "0";
				predictTime = "No available";
			} else {
				prediction[0] = ((String) time.get("tmstmp")).trim();
				prediction[1] = ((String) time.get("prdtm")).trim();
				int gapTime = 0;
				try {
					gapTime = (Integer.parseInt(prediction[1].substring(
							prediction[1].length() - 5,
							prediction[1].length() - 3)) * 60 + Integer
							.parseInt(prediction[1].substring(prediction[1]
									.length() - 2)))
							- (Integer.parseInt(prediction[0].substring(
									prediction[0].length() - 5,
									prediction[0].length() - 3)) * 60 + Integer
									.parseInt(prediction[0]
											.substring(prediction[0].length() - 2)));
					System.err.println(gapTime);
					// waiting time in minute form
					waitTime = String.valueOf(gapTime);
					// the prediction time in time form
					predictTime = prediction[1].substring(prediction[1]
							.length() - 5);
				} catch (NumberFormatException nfe) {
					nfe.printStackTrace();
				}
			}

			// routeName
			String routeName = (String) time.get("des");
			// stopName
			String stopName = (String) time.get("stpnm");
			// busNumber
			String busNumber = (String) time.get("vid");
			double[] location = getLocation(busNumber);
			Prediction rtSingle = new Prediction(routeId, routeName, stopName,
					predictTime, waitTime, dir, busNumber, location[0],
					location[1]);
			return rtSingle;

	}

	private double[] getLocation(String bus) throws IOException {
		HttpURLConnection connection = null;
		URL url = new URL("http://truetime.portauthority.org/bustime/"
				+ "api/v2/getvehicles?key=ADpCvpyDcupACyuMdk5wrVTVH" + "&vid="
				+ bus + "&format=json");
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);

		JSONObject obj = (JSONObject) JSONValue.parse(readResponse(connection));
		double[] location = new double[2];
		JSONObject error = (JSONObject) obj.get("error"); 
		if (error != null) {
			// error handling
			System.err.println("error: " + error.toString());
			location[0] = 40.45935874149717;
			location[1] = -79.92761098927465;
			return location;
		}
		
		JSONObject results = (JSONObject) obj.get("bustime-response");
		System.err.println("getLocation = " + results.toString());
		JSONArray vehicles = (JSONArray) results.get("vehicle");
		// TODO: double check here
		if (vehicles == null || vehicles.size() == 0)
			return null;
		JSONObject vehicle = (JSONObject) vehicles.get(0);
		
		try {
			location[0] = Double.parseDouble(((String) vehicle.get("lat")).trim());
			location[1] = Double.parseDouble(((String) vehicle.get("lon")).trim());
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
