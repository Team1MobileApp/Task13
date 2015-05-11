package model;

import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.ConnectionPool;
import org.genericdao.MatchArg;
import org.genericdao.RollbackException;

import databeans.Stop;

public class StopDAO extends GenericDAO<Stop> {
	public StopDAO(String tableName, ConnectionPool pool) throws DAOException{
		super(Stop.class, tableName, pool);
	}
	public void createStop(Stop stop) throws RollbackException {
		create(stop);
	}
	public int getStopCount() throws RollbackException {
		return getCount();
	}
	public Stop[] getStopId(String bound, String stopName) throws RollbackException {
		Stop[] stop = match(MatchArg.and(MatchArg.equals("bound", bound), MatchArg.equals("stopName", stopName)));
		System.err.println("stop == null ? " + stop ==  null);
		if (stop == null) return null;
		return stop;
	}
	
}
