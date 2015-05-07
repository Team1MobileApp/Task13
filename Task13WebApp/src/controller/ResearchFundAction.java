/*
 * Name: Charlotte Lin
 * Date: 01/17/2015
 */

/* Customer research fund
 * 1. set fundList attribute
 * 2. show all fund list if no action
 * 3. use form to get the id of the clicked fund. 
 * 4. set that fund attribute
 * 5. set that fund_history attribute
 * 6. show details of the one clicked fund. 
 */
package controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.CustomerDAO;
import model.Fund_Price_HistoryDAO;
import model.Model;
import model.FundDAO;
import model.PositionDAO;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;

import databeans.Customer;
import databeans.Fund;
import databeans.Fund_Price_History;
import customerFormbeans.ResearchFundForm;

public class ResearchFundAction extends Action {
	private FormBeanFactory<ResearchFundForm> formBeanFactory = 
			FormBeanFactory.getInstance(ResearchFundForm.class);
	
	private CustomerDAO customerDAO;
	private PositionDAO positionDAO;
	private FundDAO fundDAO;
	private Fund_Price_HistoryDAO fundPriceHistoryDAO;
	
	// constructor
	public ResearchFundAction(Model model) {
		customerDAO = model.getCustomerDAO();
		positionDAO = model.getPositionDAO();
		fundDAO = model.getFundDAO();
		fundPriceHistoryDAO = model.getFund_Price_HistoryDAO();
	}
	
	// get action name
	public String getName() {
		return "researchFundAction.do";
	}
	
	// return next page name
	public String perform(HttpServletRequest request) {
		// get session
		HttpSession session = request.getSession();
		
		// get customer
		Customer customer = (Customer) session.getAttribute("user");
		
		// set error attribute
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);
		
		// get fund list
		try {
			Fund[] fundList = (Fund[]) fundDAO.getFunds();
			if (fundList == null || fundList.length == 0) {
				errors.add("No available fund");
				return "research-fund.jsp";
			}
			request.setAttribute("fundList", fundList);
		} catch (RollbackException e) {
			return "research-fund.jsp";
		}
		
		// get form, set form attribute
		try {
			ResearchFundForm form = formBeanFactory.create(request);
			request.setAttribute("form", form);
					
			// if no parameters passed in
			if (!form.isPresent()) {
				return "research-fund.jsp";
			}
					
			// check error, if has
			errors.addAll(form.getValidationErrors());
			if (errors.size() > 0) {
				return "research-fund.jsp";
			}
					
			// if no error, get the clicked fund
			Fund fund = fundDAO.read(form.getIdAsInt());
					
			if (fund == null) {
				errors.add("Fund item does not exist");
				return "research-fund.jsp";
			}
					
			// set fund attribute
			request.setAttribute("fund", fund);
			
			int fund_id = form.getIdAsInt();
			Fund_Price_History[] fundPriceHistoryOrigin = fundPriceHistoryDAO.getFundPrice(fund_id);
			
			
			if (fundPriceHistoryOrigin == null || fundPriceHistoryOrigin.length == 0) {
				errors.add("Fund item does not have history");
				return "research-fund-detail.jsp";
			}
			Fund_Price_History[] fundPriceHistory = new Fund_Price_History[fundPriceHistoryOrigin.length];
			int j = 0;
			for (int i = fundPriceHistoryOrigin.length - 1; i >= 0; i--) {
				fundPriceHistory[j++] = fundPriceHistoryOrigin[i];
			}
			
			request.setAttribute("fundPriceHistory", fundPriceHistory);
			return "research-fund-detail.jsp";
			
		} catch (FormBeanException e) {
			return "research-fund.jsp";
		} catch (RollbackException e) {
			return "research-fund.jsp";
		} 		
	}
}

