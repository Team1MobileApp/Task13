package model;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.RollbackException;

import databeans.UserBean;

public class Model {
	private UserDAO  userDAO;
	
	public Model(ServletConfig config) throws ServletException {
		try {
			String jdbcDriver = config.getInitParameter("jdbcDriverName");
			String jdbcURL    = config.getInitParameter("jdbcURL");
			
			ConnectionPool pool = new ConnectionPool(jdbcDriver, jdbcURL);
			userDAO  = new UserDAO("user", pool);
		} catch (DAOException e) {
			throw new ServletException(e);
		} catch (RollbackException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public UserDAO  getUserDAO()  { return userDAO;  }
}
