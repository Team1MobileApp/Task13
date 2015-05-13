package controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;



import org.json.simple.JSONObject;

import model.Model;

@SuppressWarnings("serial")
public class Controller extends HttpServlet {

    public void init() throws ServletException {
        Model model = new Model(getServletConfig());
        Action.add(new InitAction(model));
        Action.add(new StopNameAction(model));
        Action.add(new ManageAction(model));
        Action.add(new TrackAction(model));
        Action.add(new RouteAction(model));
    }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request,response);
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nextPage = performTheAction(request);
        System.out.println("nextPage: " + nextPage);
        sendToNextPage(nextPage, request, response);
    }
    
    /*
     * Extracts the requested action and (depending on whether the user is logged in)
     * perform it (or make the user login).
     * @param request
     * @return the next page (the view)
     */
    private String performTheAction(HttpServletRequest request) {
        HttpSession session     = request.getSession(true);
        String      servletPath = request.getServletPath();
        String      action = getActionName(servletPath);
        System.out.println("servletPath: " + servletPath);
        System.out.println("action: " + action);

        // System.out.println("servletPath="+servletPath+" requestURI="+request.getRequestURI()+"  user="+user);
        
      	// Let the logged in user run his chosen action
		return Action.perform(action,request);
    }

    /*
     * If nextPage is null, send back 404
     * If nextPage ends with ".do", redirect to this page.
     * If nextPage ends with ".jsp", dispatch (forward) to the page (the view)
     *    This is the common case
     */
    private void sendToNextPage(String nextPage, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
    	if (nextPage == null) {
    		response.sendError(HttpServletResponse.SC_NOT_FOUND,request.getServletPath());
    		return;
    	}
    	
    	if (nextPage.endsWith(".do")) {
			response.sendRedirect(nextPage);
			return;
    	}
    	
    	if (nextPage.contains("vote.do?userid=")) {
			response.sendRedirect(nextPage);
			return;
		}
    	
    	if (nextPage.endsWith(".jsp")) {
	   		RequestDispatcher d = request.getRequestDispatcher(nextPage);
	   		d.forward(request,response);
	   		return;
    	}
    	
    	if (nextPage.equals("image")) {
	   		RequestDispatcher d = request.getRequestDispatcher(nextPage);
	   		d.forward(request,response);
	   		return;
    	}
    	
    	if (nextPage.equals("stop.ajax")) {
    		System.err.println("nextPage = stop.ajax");
    		JSONObject stopObj = (JSONObject) request.getAttribute("stopObj");
    		System.err.println("stopObj == null: " + (stopObj == null));
    		setResponse(response, stopObj);
    		return;
    	}
    	
    	if (nextPage.equals("track.ajax")) {
    		System.err.println("nextPage = track.ajax");
    		JSONObject locObj = (JSONObject) request.getAttribute("locObj");
    		System.err.println("locObj == null: " + (locObj == null));
    		setResponse(response, locObj);
    		return;
    	}
    	
    	
    	throw new ServletException(Controller.class.getName()+".sendToNextPage(\"" + nextPage + "\"): invalid extension.");
    }

	/*
	 * Returns the path component after the last slash removing any "extension"
	 * if present.
	 */
    private String getActionName(String path) {
    	// We're guaranteed that the path will start with a slash
        int slash = path.lastIndexOf('/');
        return path.substring(slash+1);
    }
    
    private void setResponse(HttpServletResponse response, JSONObject jsonObj) throws IOException, ServletException {
    	PrintWriter out = response.getWriter();
    	response.setContentType("application/json; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "POST");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
        response.setHeader("Access-Control-Max-Age", "86400");
        System.err.println("In setResponse:");
        System.err.println(jsonObj.toString());
        out.print(jsonObj);
        out.close();
    }
    
    
}
