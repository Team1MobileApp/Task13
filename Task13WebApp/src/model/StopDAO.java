package model;


import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.ConnectionPool;

import databeans.Stop;


public class StopDAO extends GenericDAO<Stop> {
	public StopDAO(String tableName, ConnectionPool pool) throws DAOException{
		super(Stop.class, tableName, pool);
	}

}
