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

import sun.net.www.protocol.http.HttpURLConnection;
import model.Model;
import model.StopDAO;

import org.genericdao.RollbackException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;

import databeans.Prediction;
import databeans.Stop;

public class InitAction extends Action {

	private StopDAO stopDAO;
	private static String apiKey = "w9m3MfMkLEEwU7hqyGM6mU9ut";

	public InitAction(Model model) {
		stopDAO = model.getStopDAO();
	}

	public String getName() {
		return "init.do";
	}

	public String perform(HttpServletRequest request) {
		// if database is empty, initial stop table
		try {
			if (stopDAO.getCount() == 0) {
				setData();
			}
		} catch (IOException ioe) {
			// TODO Auto-generated catch block
			ioe.printStackTrace();
		} catch (RollbackException re) {
			re.printStackTrace();
		}

		return "index.jsp";
	}

	private void setData() throws IOException, RollbackException {
		JSONArray routes = getAllRoutes();
		Map<String, Stop> map = new HashMap<String, Stop>();
		for (int i = 0; i < routes.size(); i++) {
			JSONObject route = (JSONObject) routes.get(i);
			String routeId = (String) route.get("rt");
			String bound = "INBOUND";
			JSONArray stops = getStops(routeId, bound);
			for (Object stop : stops) {
				String stopId = ((String) ((JSONObject) stop).get("stpid"))
						.trim();
				String stopName = ((String) ((JSONObject) stop).get("stpnm"))
						.trim();
				
				System.err.println("stopId = " + stopId
						+ ", map.containsKey(stopId): "
						+ map.containsKey(stopId));
				System.err.println("routeId = " + routeId + ", stopName = "
						+ stopName + ", bound = " + bound);
				if (map.containsKey(stopId)
						&& stopName.equals(map.get(stopId).getStopName())
						&& bound.equals(map.get(stopId).getDirection())
						&& routeId.equals(map.get(stopId).getRouteId())) {
					continue;
				}
				double stopLat = (double) ((JSONObject) stop).get("lat");
				double stopLon = (double) ((JSONObject) stop).get("lon");
				
				Stop newStop = new Stop();
				//stopName, bound, stopId, routeId
				newStop.setStopName(stopName);
				newStop.setDirection(bound);
				newStop.setStopId(stopId);
				newStop.setRouteId(routeId);
				newStop.setStopLat(stopLat);
				newStop.setStopLon(stopLon);
				map.put(stopId, newStop);
				stopDAO.create(newStop);

			}
			bound = "OUTBOUND";
			stops = getStops(routeId, bound);
			for (Object stop : stops) {
				String stopId = ((String) ((JSONObject) stop).get("stpid"))
						.trim();
				String stopName = ((String) ((JSONObject) stop).get("stpnm"))
						.trim();
				System.err
						.println("stopId = " + stopId
								+ "map.containsKey(stopId): "
								+ map.containsKey(stopId));
				System.err.println("routeId = " + routeId + ", stopName = " + stopName + ", bound = "
						+ bound);
				if (map.containsKey(stopId)
						&& stopName.equals(map.get(stopId).getStopName())
						&& bound.equals(map.get(stopId).getDirection())
						&& routeId.equals(map.get(stopId).getRouteId())) {
					continue;
				}
				double stopLat = (double) ((JSONObject) stop).get("lat");
				double stopLon = (double) ((JSONObject) stop).get("lon");
				
				Stop newStop = new Stop();
				//stopName, bound, stopId, routeId
				newStop.setStopName(stopName);
				newStop.setDirection(bound);
				newStop.setStopId(stopId);
				newStop.setRouteId(routeId);
				newStop.setStopLat(stopLat);
				newStop.setStopLon(stopLon);
				map.put(stopId, newStop);
				stopDAO.create(newStop);
			}
		}
	}

	private static JSONArray getAllRoutes() throws IOException,
			RollbackException {
		HttpURLConnection connection = null;
		URL url = new URL("http://truetime.portauthority.org/bustime/"
				+ "api/v2/getroutes?key=" + apiKey + "&format=json");

		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);

		JSONObject obj = (JSONObject) JSONValue.parse(readResponse(connection));
		JSONObject results = (JSONObject) obj.get("bustime-response");
		JSONArray routes = (JSONArray) results.get("routes");
		return routes;
	}

	private static JSONArray getStops(String routeId, String bound)
			throws IOException {
		HttpURLConnection connection = null;
		URL url = new URL("http://truetime.portauthority.org/bustime/"
				+ "api/v2/getstops?key=" + apiKey + "&rt=" + routeId + "&dir="
				+ bound + "&format=json");
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);

		JSONObject obj = (JSONObject) JSONValue.parse(readResponse(connection));
		JSONObject results = (JSONObject) obj.get("bustime-response");
		JSONArray stops = (JSONArray) results.get("stops");
		return stops;
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
