package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.CustomerDAO;
import model.EmployeeDAO;
import model.Model;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import customerFormbeans.LoginForm;
import databeans.Customer;
import databeans.Employee;
import databeans.User;

public class LoginAction extends Action {
	private FormBeanFactory<LoginForm> formBeanFactory = FormBeanFactory
			.getInstance(LoginForm.class);

	private CustomerDAO custDAO;
	private EmployeeDAO empDAO;
	Customer customer;
	Employee employee;

	public LoginAction(Model model) {
		custDAO = model.getCustomerDAO();
		empDAO = model.getEmployeeDAO();
	}

	public String getName() {
		return "login.do";
	}

	public String perform(HttpServletRequest request) {
		HttpSession session = request.getSession();
        
		List<String> errors;
		errors = (List<String>) request.getAttribute("errors");
		if (errors!=null && errors.size()>0) {
			return "login.jsp";
		}
		errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
			// If a user has already logged in, he/she will not be redirect to
			// login page.
			User user = (User) session.getAttribute("user");
			if (user == null) {

				LoginForm form = formBeanFactory.create(request);
				request.setAttribute("form", form);

				// If no params were passed, return with no errors so that the
				// form will be
				// presented (we assume for the first time).
				if (!form.isPresent()) {
					return "login.jsp";
				}

				// Any validation errors?
				errors.addAll(form.getValidationErrors());
				String userType = form.getUserType();
				if (userType == null) {
					errors.add("Please choose one user type.");
					return "login.jsp";
				}
				System.out.println(userType);

				String username = form.getUsername();

				if (userType.equals("Customer")) {
					user = custDAO.getCustomer(username);
				} else if (userType.equals("Employee")) {
					user = empDAO.getEmployee(username);
				}
				if (user == null) {
					errors.add("The " + userType + " " + username
							+ " does not exist");
					return "login.jsp";
				}
				if (!user.checkPassword(form.getPassword())) {
					errors.add("Incorrect password");
					return "login.jsp";
				}
				// Attach (this copy of) the user bean to the session
				session.setAttribute("user", user);
			}

			if (user instanceof Customer) {
				return "viewAccAction.do";
			} else if (user instanceof Employee) {
				return "viewCustomerList.do";
			} else {
				errors.add("No such a user type");
				return "login.jsp";
			}

		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "login.jsp";
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			return "login.jsp";
		}
	}
}
