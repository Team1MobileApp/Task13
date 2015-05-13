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

import databeans.Stop;

public class StopNameAction extends Action {

	private StopDAO stopDAO;

	public StopNameAction(Model model) {
		stopDAO = model.getStopDAO();
	}

	public String getName() {
		return "stopname.do";
	}

	public String perform(HttpServletRequest request) {
		
		// get route
		System.err.println("route = null : " + (request.getParameter("route") == null));
		String[] route = ((String) request.getParameter("route")).trim().split(
				" ");
		String routeId = route[0].trim();
		System.err.println("routeId = " + routeId);

		// get direction
		String direction = ((String) request.getParameter("direction")).trim();
		System.err.println("direction = " + direction);
		
		// get all stop names related to the routeId and direction
		// return the Json Object
		try {
			System.err.println("Try to get all stop names here");
			Stop[] stops = stopDAO.getStops(direction, routeId);
			//List<String> stopList = new ArrayList<String>();
			
			JSONObject stopObj = new JSONObject();
			JSONArray stopArr = new JSONArray();
			if (stops.length != 0) {
				for (Stop stop : stops) {
					JSONObject stopElem = new JSONObject(); 
					stopElem.put("stopName", stop.getStopName());
					stopElem.put("direction", stop.getDirection());
					stopElem.put("stopId", stop.getStopId());
					stopElem.put("routeId", stop.getRouteId());
					stopArr.add(stopElem);
				}
			}
			// TODO: check property of stopObj
			if (stopArr.size()!= 0) {
				System.err.println("Has already get the stopArr");
				stopObj.put("success", true);
			} else {
				stopObj.put("success", false);
			}
			
			stopObj.put("stops", stopArr);
			System.err.println("stopObj: ");
			System.err.println(stopObj.toString());
			
			request.setAttribute("stopObj", stopObj);
			
		} catch (RollbackException re) {
			re.printStackTrace();
		} 
		
		return "stop.ajax";
	}

}
