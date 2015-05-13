/*
 * Author: Qianwen Li
 * Date: 5/11/2015
 * 
 * */

package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;

import databeans.Stop;

public class StopDAO extends GenericDAO<Stop> {
	public StopDAO(String tableName, ConnectionPool pool) throws DAOException{
		super(Stop.class, tableName, pool);
	}
	public void create(Stop stop) throws RollbackException {
		createAutoIncrement(stop);
	}

	public Stop read(String stopName, String direction) throws RollbackException {
		//TODO: later check upper/lowercase
		Stop[] stops = match(MatchArg.and(MatchArg.equals("stopName", stopName), MatchArg.equals("direction", direction)));
		if (stops.length != 0) return stops[0];
		return null;
	}
	
	public Stop[] getStops(String direction, String routeId) throws RollbackException {
		System.err.println("in the getAllStopName");
		System.err.println("direction = " + direction + ", routeId = " + routeId);
		Stop[] stops = match(MatchArg.and(MatchArg.equals("direction", direction), MatchArg.equals("routeId", routeId)));
		
		System.err.println("stops == null: " + (stops == null));
		if (stops != null) {
			System.err.println("stops == length : " + (stops.length));
			return stops;
		}
		return null;
	}
	
}
