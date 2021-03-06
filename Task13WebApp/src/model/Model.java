package model;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;


public class Model {

	private StopDAO stopDAO;
	
	public Model(ServletConfig config) throws ServletException {
		try {
			String jdbcDriver = config.getInitParameter("jdbcDriverName");
			String jdbcURL    = config.getInitParameter("jdbcURL");
			
			ConnectionPool pool = new ConnectionPool(jdbcDriver, jdbcURL);
			stopDAO = new StopDAO("stop", pool);
		} catch (DAOException e) {
			throw new ServletException(e);
		}
	}
	
	public StopDAO getStopDAO() {
		return stopDAO;
	}

}
