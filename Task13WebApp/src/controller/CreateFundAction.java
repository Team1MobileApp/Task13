package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.FundDAO;
import model.Model;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Employee;
import databeans.Fund;
import databeans.User;
import employeeFormbeans.CreateFundForm;

public class CreateFundAction extends Action {
	private FormBeanFactory<CreateFundForm> formBeanFactory = FormBeanFactory
			.getInstance(CreateFundForm.class);
	
	private FundDAO fundDAO;

	public CreateFundAction(Model model) {
		fundDAO = model.getFundDAO();
	}
	
	public String getName() {
		return "createFund.do";
	}

	public String perform(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);
		User user = (User) session.getAttribute("user");
		try {
			if (user==null) {
				errors.add("Please login first");
				return "login.do";
			}
			else if (!(user instanceof Employee)) {
				errors.add("You don't have permission to do the operation");
				return "login.do";
			}

			CreateFundForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);

			// If no params were passed, return with no errors so that the form
			// will be
			// presented (we assume for the first time).
			if (!form.isPresent()) {
				return "createFund.jsp";
			}

			// Any validation errors?
			errors.addAll(form.getValidationErrors());
			if (errors.size() != 0) {
				return "createFund.jsp";
			}

			Fund fund = null;
			// Create the fund bean
			fund = fundDAO.getFundFromName(form.getName());
			if (fund !=null) {
				errors.add("The fund name "+form.getName()+" already exists. Please use another fund name.");
				return "createFund.jsp";
			}
			fund = fundDAO.getFund(form.getSymbol());
			if (fund !=null) {
				errors.add("The fund symbol "+form.getSymbol()+" already exists. Please use another fund symbol.");
				return "createFund.jsp";
			}
			fund = new Fund();
			fund.setName(form.getName());
			fund.setSymbol(form.getSymbol());
            fundDAO.create(fund);

            String message = "You successfully create a new fund \""+fund.getName()+"\".";
            request.setAttribute("messages", message);
			return "successMessage.jsp";
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "createFund.jsp";
		} catch (FormBeanException e) {
			errors.add(e.getMessage());
			return "createFund.jsp";
		}
	}


}
