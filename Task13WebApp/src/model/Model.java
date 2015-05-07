package model;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;


public class Model {
	private RouteDAO  routeDAO;
	private StopDAO stopDAO;
	private BoundDAO boundDAO;
	
	public Model(ServletConfig config) throws ServletException {
		try {
			String jdbcDriver = config.getInitParameter("jdbcDriverName");
			String jdbcURL    = config.getInitParameter("jdbcURL");
			
			ConnectionPool pool = new ConnectionPool(jdbcDriver, jdbcURL);
			routeDAO = new RouteDAO("route", pool);
			stopDAO = new StopDAO("stop", pool);
			boundDAO = new BoundDAO("bound", pool);
		} catch (DAOException e) {
			throw new ServletException(e);
		}
	}
	
	public RouteDAO getRouteDAO() {
		return routeDAO;
	}
	public StopDAO getStopDAO() {
		return stopDAO;
	}
	public BoundDAO getBoundDAO() {
		return boundDAO;
	}
}
