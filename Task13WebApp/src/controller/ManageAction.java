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
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.BoundDAO;
import model.Model;
import model.RouteDAO;


import model.StopDAO;

import org.genericdao.RollbackException;

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
		/*
		try {
			getAllRoutes();
			System.out.println("I get here!");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		*/
		
		
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
	private static void getAllRoutes() throws MalformedURLException, IOException {
		System.out.println("I get here???");
		
		// cannot get data using this url???
		String url = "http://localhost:8080/bustime/api/v1/getroutes?key=" + apiKey;
		URLConnection uc = new URL(url).openConnection();
		BufferedReader br = new BufferedReader(new InputStreamReader(
				uc.getInputStream()));
		BufferedWriter bw = new BufferedWriter(new FileWriter(
				new File(filePath)));
		String nextLine;
		while ((nextLine = br.readLine()) != null) {

			bw.write(nextLine);// fastest the way to read and write
		}

		br.close();
		bw.close();
	}
}
