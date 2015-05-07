package model;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;

import databeans.Bound;

public class BoundDAO extends GenericDAO<Bound>{
	public BoundDAO(String tableName, ConnectionPool pool) throws DAOException{
		super(Bound.class, tableName, pool);
	}
}
