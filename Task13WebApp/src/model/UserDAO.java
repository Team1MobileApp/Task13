package model;

import java.util.Arrays;
import java.util.Comparator;

import org.genericdao.ConnectionPool;
import org.genericdao.DAOException;
import org.genericdao.GenericDAO;
import org.genericdao.RollbackException;
import org.genericdao.Transaction;

import databeans.UserBean;


public class UserDAO extends GenericDAO<UserBean> {
	
	public UserDAO(String tableName, ConnectionPool pool) throws DAOException {
		super(UserBean.class, tableName, pool);
	}

	public UserBean[] getUsers() throws RollbackException {
		UserBean[] users = match();
		Arrays.sort(users, new Comparator<UserBean>(){

			@Override
			public int compare(UserBean o1, UserBean o2) {
				// TODO Auto-generated method stub
				if(o1.getFirstName().compareTo(o2.getLastName()) != 0)
					return o1.getFirstName().compareTo(o2.getLastName());
				return o1.getLastName().compareTo(o2.getLastName());
			}
			
		});  // We want them sorted by last and first names (as per User.compareTo());
		return users;
	}
	
	public void setPassword(String userName, String password) throws RollbackException {
        try {
        	Transaction.begin();
        	UserBean dbUser = read(userName);
			
			if (dbUser == null) {
				throw new RollbackException("User "+userName+" no longer exists");
			}
			
			dbUser.setPassword(password);
			
			update(dbUser);
			Transaction.commit();
		} finally {
			if (Transaction.isActive()) Transaction.rollback();
		}
	}
}
