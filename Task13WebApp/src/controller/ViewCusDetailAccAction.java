/*
 * 08-600
 * Homework #9
 * Jiali Chen
 * andrewID: jialic
 * Dec 4, 2014
 */
package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.*;
import model.*;
import employeeFormbeans.*;
import customerFormbeans.*;

public class ViewCusDetailAccAction extends Action {
	//private FormBeanFactory<UserIDForm> formBeanFactory = FormBeanFactory
		//	.getInstance(UserIDForm.class);

	private EmployeeDAO employeeDAO;
	private CustomerDAO customerDAO;

	public ViewCusDetailAccAction(Model model) {
		employeeDAO = model.getEmployeeDAO();
		customerDAO = model.getCustomerDAO();
	}

	public String getName() {
		return "viewcustomerdetails.do";
	}

	public String perform(HttpServletRequest request) {
		// Set up the request attributes (the errors list and the form bean so
		// we can just return to the jsp with the form if the request isn't
		// correct)
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);

		try {
			// Set up user list for nav bar
			request.setAttribute("customerList", customerDAO.getCustomers());

			/*UserIDForm form = formBeanFactory.create(request);

			int userID = form.getUserIDAsInt();

			// Set up photo list
			User user = userDAO.read(userID);
			if (user == null) {
				errors.add("Invalid User: " + userID);
				return "error.jsp";
			}
*/          int customer_id=Integer.parseInt(request.getParameter("customer_id"));
			Customer customerdetails = customerDAO.read(customer_id);
			request.setAttribute("customerdetails", customerdetails);
			return "customerdetails.jsp";
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		} /*catch (FormBeanException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}*/
	}
}
