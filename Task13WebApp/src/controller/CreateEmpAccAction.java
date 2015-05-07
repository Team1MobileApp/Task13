/*ian
*/

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
public class CreateEmpAccAction extends Action {
	private FormBeanFactory<CreateEmpAccForm> formBeanFactory = FormBeanFactory.getInstance(CreateEmpAccForm.class);

	private EmployeeDAO employeeDAO;
	
	public CreateEmpAccAction(Model model) {
		employeeDAO = model.getEmployeeDAO();
	}

	public String getName() { 
		return "create_employee.do"; 
		}

    public String perform(HttpServletRequest request) {
    	HttpSession session = request.getSession(true);
    	List<String> errors = new ArrayList<String>();
        request.setAttribute("errors",errors);
        String message = null;

        try {
	        CreateEmpAccForm form = formBeanFactory.create(request);
	        request.setAttribute("form",form);
	    	//request.setAttribute("userList",userDAO.getUsers());
	        
	        // If no params were passed, return with no errors so that the form will be
	        // presented (we assume for the first time).
	        if (!form.isPresent()) {
	            return "create-employee.jsp";
	        }
	
	        // Any validation errors?
	        errors.addAll(form.getValidationErrors());
	        if (errors.size() != 0) {
	            return "create-employee.jsp";
	        }
	        Employee emp;
	        
	        emp = employeeDAO.getEmployee(form.getUsername());
			if (emp != null) {
				errors.add("The username already exists.");
				return "create-employee.jsp";
			}
			emp=new Employee();
	        
	        emp.setUsername(form.getUsername());
	        emp.setFirstname(form.getFirstName());
	        emp.setLastname(form.getLastName());
	        emp.setPassword(form.getPassword());
	        employeeDAO.createEmployee(emp);

	        message = "You successfully created a new employee: "+emp.getFirstname()+" "+emp.getLastname(); 
			
        } catch (RollbackException e) { //RollBackException 
        	errors.add(e.getMessage());
        	return "create-employee.jsp";
        } catch (FormBeanException e) {
        	errors.add(e.getMessage());
        	return "create-employee.jsp";
        }
         
		request.setAttribute("form", null);
		 request.setAttribute("messages", message);
        return "create-employee.jsp";
    }
}
