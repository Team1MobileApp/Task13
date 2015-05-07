package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import model.CustomerDAO;
import model.Model;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import employeeFormbeans.ResetCusPwdForm;

public class ResetCusPwdAction extends Action {
	private FormBeanFactory<ResetCusPwdForm> formBeanFactory = FormBeanFactory
			.getInstance(ResetCusPwdForm.class);
	
	private CustomerDAO customerDAO;

	public ResetCusPwdAction(Model model) {
		customerDAO = model.getCustomerDAO();
	}
	
	public String getName() {
		return "resetCustomerPassword.do";
	}

	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);
		System.out.println("ResetCustomerPassword Action get performed");

		try {
			

			ResetCusPwdForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			// If no params were passed, return with no errors so that the form
			// will be
			// presented (we assume for the first time).
			if (!form.isPresent()) {
				return "resetCusPwd.jsp";
			}

			// Any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "resetCusPwd.jsp";
			}

			Customer customer;
			// Create the fund bean
			customer = customerDAO.getCustomer(form.getUsername());
			if (customer ==null) {
				errors.add("Customer username " + form.getUsername()+ " does not exist");
				return "resetCusPwd.jsp";
			}
			//customer.setPassword(form.getNewPassword());
			//customerDAO.setPassword(customer);
			customerDAO.setPassword(customer.getUsername(),form.getNewPassword());
 				
			
			return "transitionDay.do";
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "createFund.jsp";
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			return "createFund.jsp";
		}
	}


}
