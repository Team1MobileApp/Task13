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
import javax.servlet.http.HttpSession;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.*;
import model.*;
import employeeFormbeans.*;
import employeeFormbeans.ChangePwdForm;
import customerFormbeans.*;

public class ViewCusAccAction extends Action {
	private FormBeanFactory<CustomerListForm> formBeanFactory = FormBeanFactory
			.getInstance(CustomerListForm.class);

	private EmployeeDAO employeeDAO;
	private CustomerDAO customerDAO;

	public ViewCusAccAction(Model model) {
		employeeDAO = model.getEmployeeDAO();
		customerDAO = model.getCustomerDAO();
	}

	public String getName() {
		return "viewCustomerList.do";
	}

	public String perform(HttpServletRequest request) {
		// Set up the request attributes (the errors list and the form bean so
		// we can just return to the jsp with the form if the request isn't
		// correct)
		HttpSession session = request.getSession();
		List<String> errors;
		errors = (List<String>) request.getAttribute("errors");
		if (errors ==null) {
		errors = new ArrayList<String>();
		request.setAttribute("errors", errors);
		}
		Customer[] customerList;
		
		
		
		// If no params were passed, return with no errors so that the form
		// will be
		// presented (we assume for the first time).
		try {
			customerList = customerDAO.getCustomers();
			
			request.setAttribute("customerList",customerList);
			 
			CustomerListForm form = formBeanFactory.create(request);
			
			if (!form.isPresent()) {
				return "customerList.jsp";
			}
			

			// Check for any validation errors
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "customerList.jsp";
			}
			String operation = form.getOperation();
			int customer_id = form.getCustomer_id();
			request.setAttribute("customer", customerDAO.getCustomer(customer_id));
				if (operation.equals("View Account")) {
					session.setAttribute("customer",customerDAO.getCustomer(customer_id));
					return "viewAccAction.do";
				}
				else if (operation.equals("Deposit Check")) {
					return "depositCheck.jsp";
					
				}
				else if (operation.equals("Reset Password")) {
				    return "resetCusPwd.jsp";
				}
				else if (operation.equals("View Transaction History")) {
					session.setAttribute("customer",customerDAO.getCustomer(customer_id));
					return "TransactionHistoryAction.do";
					
				}

            return "customerList.jsp";
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		} /*catch (FormBeanException e) {
			errors.add(e.getMessage());
			return "error.jsp";
		}*/ catch (FormBeanException e) {
			// TODO Auto-generated catch block
			errors.add(e.getMessage());
			return "error.jsp";
		}
	}
}
