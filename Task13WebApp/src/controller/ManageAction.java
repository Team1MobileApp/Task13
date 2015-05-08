/******************************
 * Author: Charlotte Lin
 * Date: 2015/05/07
 ******************************/
package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import javax.servlet.http.HttpServletRequest;
import sun.net.www.protocol.http.HttpURLConnection;
import model.Model;
import model.RouteDAO;
import org.genericdao.RollbackException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import databeans.Prediction;
import databeans.Route;

public class ManageAction extends Action {
	private RouteDAO routeDAO;

	public ManageAction(Model model) {
	    	routeDAO = model.getRouteDAO();
	}

	public String getName() { 
		return "manage.do"; 
	}

	public String perform(HttpServletRequest request) {
		
		/*
		// get route (if type in route id directly, then dont need database at all)
		// below hard code one route name, to test
		String routeNameToSearch = "FREEPORT ROAD";
		// route id need to take from database according to route name.
		String routeIdToUse = "1";
		
		// get stop (api need route id and direction, which we will input)
		// below hard code one stop name, to test
		String stopNameToSearch = "31st St Bridge Ramp at Rialto St";
		// route id need to take from database according to stop name.
		String stopIdToUse = "20781";
		// size is 2, time[0] is time stmp, time[1] is predicted time
		String time[];
		try {
			time = getTime(routeIdToUse, stopIdToUse);
			String timeStamp = time[0];
			String timePredicted = time[1];
			System.out.println(timeStamp);
			System.out.println(timePredicted);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		*/
		
		
		// get all routes base on stop and bound
		// here need get parameters from web page
		
		String stopId = "20781";
		String bound = "INBOUND";
		
		try {
			Route[] routesWithStopAndBound = routeDAO.getRouteWithStopAndBound(stopId, bound);
			ArrayList<Route> routesNoDup = new ArrayList<Route>();
			HashSet<String> routeIds = new HashSet<String>();
			for (Route route : routesWithStopAndBound) {				
				String routeId = route.getRouteId();
				
				if (!routeIds.contains(routeId)) {
					routesNoDup.add(route);
					routeIds.add(routeId);
				}
			}

			ArrayList<Prediction> predictRes = new ArrayList<Prediction>();
			for (Route route : routesNoDup) {
				String routeId = route.getRouteId();
				String[] predictions = getTime(routeId, stopId);
				if (predictions == null) continue;
				String time = predictions[1];
				System.out.println(time);
				predictRes.add(new Prediction(routeId, route.getRouteName(), time));

			}
			System.out.println(predictRes);
		} catch (RollbackException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		// only first time need to do the following things
		try {
			setData();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// need to change according to frondend
		return null;       
    }
	private void setData() throws IOException, RollbackException {
		JSONArray routes = getAllRoutes();
		for (int i = 0; i < routes.size(); i++) {
			JSONObject route = (JSONObject) routes.get(i);
			String routeId = (String) route.get("rt");
			String routeName = (String) route.get("rtnm");
			String bound = "INBOUND";
			JSONArray stops = getStops(routeId, bound);
			for (int j = 0; j < stops.size(); j++) {
				JSONObject stop = (JSONObject) stops.get(j);
				String stopId = (String) stop.get("stpid");
				String stopName = (String) stop.get("stpnm");
				Route newRoute = new Route(routeId, routeName, bound, stopId, stopName);
				routeDAO.create(newRoute);
			}
			bound = "OUTBOUND";
			stops = getStops(routeId, bound);
			for (Object stop : stops) {
				String stopId = (String) ((JSONObject) stop).get("stpid");
				String stopName = (String) ((JSONObject) stop).get("stpnm");
				Route newRoute = new Route(routeId, routeName, bound, stopId, stopName);			
				routeDAO.create(newRoute);
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
	
	private static String[] getTime(String routeId, String stopId) throws IOException {
		HttpURLConnection connection = null;
		URL url = new URL("http://truetime.portauthority.org/bustime/"
				+ "api/v2/getpredictions?key=ADpCvpyDcupACyuMdk5wrVTVH"
				+ "&rt=" + routeId + "&stpid=" + stopId 
				+ "&format=json");
		connection =  (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
				
		JSONObject obj = (JSONObject) JSONValue
				.parse(readResponse(connection));
		JSONObject results = (JSONObject) obj.get("bustime-response");
		JSONArray times = (JSONArray) results.get("prd");
		if (times == null || times.size() == 0) return null;
		JSONObject time = (JSONObject) times.get(0);
		String[] prediction = new String[2];
		prediction[0] = (String) time.get("tmstmp");
		prediction[1] = (String) time.get("prdtm");
		return prediction;
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
