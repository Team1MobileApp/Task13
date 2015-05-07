package databeans;

import org.genericdao.PrimaryKey;

@PrimaryKey("userId")
public class UserBean {
	private int userId;
	private String  email = null;
	private String  password = "*";
	private String  firstName = null;
	private String  lastName = null;
	
	public UserBean() {}
	
	public UserBean(String email, String password, String firstName, String lastName) {
		this.email = email;
		this.password = password;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public boolean checkPassword(String password) {
		return this.password.equals(password);
	}

	public boolean equals(Object obj) {
		if (obj instanceof UserBean) {
			UserBean other = (UserBean) obj;
			return email.equals(other.email);
		}
		return false;
	}

	public String  getPassword() 		{ return password; }
	public int     getUserId()       	{ return userId; }
	public String  getEmail()       	{ return email; }
	public String  getFirstName()      	{ return firstName; }
	public String  getLastName()       	{ return lastName; }

	public void setPassword(String s)  	{ password = s; }
	public void setUserId(int n)  		{ userId = n; }
	public void setFirstName(String s)  { firstName = s; }
	public void setLastName(String s)   { lastName = s; }
	public void setEmail(String s)      { email = s; }

	public String toString() {
		return "User("+getEmail()+")";
	}

}
