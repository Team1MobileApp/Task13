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

public class MessageAction extends Action {
	private FormBeanFactory<CreateFundForm> formBeanFactory = FormBeanFactory
			.getInstance(CreateFundForm.class);
	
	private FundDAO fundDAO;

	public MessageAction(Model model) {
		fundDAO = model.getFundDAO();
	}
	
	public String getName() {
		return "message.do";
	}

	public String perform(HttpServletRequest request) {
		HttpSession session = request.getSession(true);
		List<String> errors;
		User user = (User) session.getAttribute("user");
		errors = (List<String>) request.getAttribute("errors");
		if (errors ==null) {
			errors = new ArrayList<String>();
			request.setAttribute("errors", errors);
		}
		if (errors.size()>0) {
			return "errorPage.jsp";
		}
		return "errorPage.jsp";
	}


}
