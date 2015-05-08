package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;
import databeans.Route;

public class RouteDAO extends GenericDAO<Route> {
	public RouteDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(Route.class, tableName, pool);
	}
	public void create(Route route) throws RollbackException {
		createAutoIncrement(route);
	}
	public Route[] getRouteWithStopAndBound(String stopId, String bound) 
			throws RollbackException {
		MatchArg matchArg1 = MatchArg.equals("stopId", stopId);
		MatchArg matchArg2 = MatchArg.equals("bound", bound);
		Route[] routes = match(MatchArg.and(matchArg1, matchArg2));
		return routes;
	}
}
