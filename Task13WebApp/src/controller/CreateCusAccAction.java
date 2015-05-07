package controller;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import databeans.*;
import employeeFormbeans.*;
import model.*;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

/*
 * Processes the parameters from the form in register.jsp.
 * If successful:
 *   (1) creates a new User bean
 *   (2) sets the "user" session attribute to the new User bean
 *   (3) redirects to view the originally requested photo.
 * If there was no photo originally requested to be viewed
 * (as specified by the "redirect" hidden form value),
 * just redirect to manage.do to allow the user to add some
 * photos.
 */
public class CreateCusAccAction extends Action {
	private FormBeanFactory<CreateCusAccForm> formBeanFactory = FormBeanFactory.getInstance(CreateCusAccForm.class);

	private CustomerDAO customerDAO;
	
	public CreateCusAccAction(Model model) {
		customerDAO = model.getCustomerDAO();
	}

	public String getName() { return "create_customer.do"; }

    public String perform(HttpServletRequest request) {
        List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        String message = null;

        try {
	        CreateCusAccForm form = formBeanFactory.create(request);
	        request.setAttribute("form",form);
	    	//request.setAttribute("userList",userDAO.getUsers());
	        
	        // If no params were passed, return with no errors so that the form will be
	        // presented (we assume for the first time).
	        if (!form.isPresent()) {
	            return "create-customer.jsp";
	        }
	
	        // Any validation errors?
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() != 0) {
	            return "create-customer.jsp";
	        }
	        Customer cus;
	        cus = customerDAO.getCustomer(form.getUsername());
			if (cus != null) {
				errors.add("The username already exists.");
				return "create-customer.jsp";
			}
			cus=new Customer();
            cus.setUsername(form.getUsername());
            cus.setFirstname(form.getFirstName());
            cus.setLastname(form.getLastName());
            cus.setPassword(form.getPassword());
            cus.setAddr_line1(form.getAddr_line1());
            cus.setAddr_line2(form.getAddr_line2());
            cus.setCity(form.getCity());
            cus.setState(form.getState());
            cus.setZip(form.getZip());
            cus.setTotalbalance(form.getCashAsLong());
            cus.setAvailablebalance(form.getCashAsLong());
            
	        customerDAO.createCustomer(cus);
	        message = "You successfully created a new customer: "+cus.getFirstname()+" "+cus.getLastname(); 
	    } catch (RollbackException e) { //RollBackException 
        	errors.add(e.getMessage());
        	return "create-customer.jsp";
        } catch (FormBeanException e) {
        	errors.add(e.getMessage());
        	System.out.println("Error is here"+e.getMessage());
        	return "create-customer.jsp";
        }
		request.setAttribute("form", null);
		 request.setAttribute("messages", message);
			return "create-customer.jsp";
		    

    }
}
