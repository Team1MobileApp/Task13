package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;

import databeans.Route;

public class RouteDAO extends GenericDAO<Route> {
	public RouteDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(Route.class, tableName, pool);
	}
}
