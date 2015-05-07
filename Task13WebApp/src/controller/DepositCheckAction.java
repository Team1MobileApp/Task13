/*
 * Edited by Scarlett Chen
 * Thursday Jan 22 2015 4:09 PM
 * customer id --> username
 */
package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.CustomerDAO;
import model.Model;
import model.TransactionDAO;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import databeans.Employee;
import databeans.Transaction;
import databeans.User;
import employeeFormbeans.DepositCheckForm;

public class DepositCheckAction  extends Action  {
	private FormBeanFactory<DepositCheckForm> formBeanFactory = FormBeanFactory
			.getInstance(DepositCheckForm.class);
	
	private TransactionDAO transactionDAO;
	private CustomerDAO customerDAO;
	public DepositCheckAction(Model model) {
		transactionDAO = model.getTransactionDAO();
		customerDAO = model.getCustomerDAO();
	}
	
	public String getName() {
		return "depositCheck.do";
	}
	public String perform(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		List<String> errors = new ArrayList<String>();
		User user = (User) session.getAttribute("user");
		request.setAttribute("errors", errors);
		String message = null;
		try {
			if (user==null) {
				errors.add("Please login first");
				return "login.do";
			}
			else if (!(user instanceof Employee)) {
				errors.add("You don't have permissions to do the operation");
				return "login.do";
			}
			

			DepositCheckForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			// If no params were passed, return with no errors so that the form
			// will be
			// presented (we assume for the first time).
			if (!form.isPresent()) {
				return "depositCheck.jsp";
			}

			// Any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "depositCheck.jsp";
			}
            
			Transaction transaction;
			Customer customer;
			customer = customerDAO.getCustomer(form.getUsername());
			if (customer==null) {
				errors.add("Customer Username \"" +form.getUsername()+"\" does not exist");
				return "depositCheck.jsp";
			}
			// Create the transaction bean
			transaction = new Transaction();
			transaction.setCustomer_id(customer.getCustomer_id());
			transaction.setExecute_date(null);
			transaction.setTransaction_type("DepositCheck");
			transaction.setAmount(form.getDatabaseAmount());
			
			transactionDAO.create(transaction);
            
			customer.setAvailablebalance(customer.getAvailablebalance() + form.getDatabaseAmount());
            customerDAO.update(customer);
            
            message = "The request to deposit $"+form.getRealAmount()+" has been submitted. Please wait for transition processing.";  
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "depositCheck.jsp";
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			return "depositCheck.jsp";
		}
		
		request.setAttribute("form", null);
		 request.setAttribute("messages", message);
		return "depositCheck.jsp";
	}
}
