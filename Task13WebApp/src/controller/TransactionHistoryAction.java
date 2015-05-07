/*
 * Name: Charlotte Lin
 * Date: 01/22/2015
 */
package controller;


import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import model.CustomerDAO;
import model.Fund_Price_HistoryDAO;
import model.Model;
import model.FundDAO;
import model.PositionDAO;
import model.TransactionDAO;

import org.genericdao.RollbackException;
import org.mybeans.form.FormBeanException;
import org.mybeans.form.FormBeanFactory;




import databeans.Customer;
import databeans.Fund_Price_History;
import databeans.Position;
import databeans.Fund;
import databeans.Transaction;
import databeans.TransactionHistory;

public class TransactionHistoryAction extends Action {
	
	private CustomerDAO customerDAO;
	private TransactionDAO transactionDAO;
	private FundDAO fundDAO;
	private Fund_Price_HistoryDAO fundPriceHistoryDAO;
	
	// constructor
	public TransactionHistoryAction(Model model) {
		customerDAO = model.getCustomerDAO();
		transactionDAO = model.getTransactionDAO();
		fundDAO = model.getFundDAO();
		fundPriceHistoryDAO = model.getFund_Price_HistoryDAO();
	}
	
	DecimalFormat priceFormat = new DecimalFormat("#,##0.00");
	DecimalFormat sharesFormat = new DecimalFormat("#,##0.000");
	SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy");
	
	// get action name
	public String getName() {
		return "TransactionHistoryAction.do";
	}
	
	// return next page name
	public String perform(HttpServletRequest request) {
		List<String> errors = new ArrayList<String>();
		request.setAttribute("errors", errors);
		
		// get session
		HttpSession session = request.getSession();
		
		
		

		
		// get customer
		Customer customer;
		// You might get customer attribute from page CustomerList
        customer = (Customer) session.getAttribute("customer");
        session.setAttribute("customer", null);
        try {
        if (customer == null) {
        	 //Get customer id from the session
    		// and refresh customer information from database customer Table
    		// store the new customer information into session
        	customer = (Customer) session.getAttribute("user");
        	int customer_id = customer.getCustomer_id();
    		customer = customerDAO.getCustomer(customer_id);
    		session.setAttribute("user", customer);
        }
        request.setAttribute("customer", customer);
		
		if (customer == null) return "login.jsp";
		
		// get transaction
		
			Transaction[] buyFundTransactions = 
					transactionDAO.getTransactions(customer.getCustomer_id(), "BuyFund");
			Transaction[] sellFundTransactions= 
					transactionDAO.getTransactions(customer.getCustomer_id(), "SellFund");
			Transaction[] requestCheckTransactions= 
					transactionDAO.getTransactions(customer.getCustomer_id(), "RequestCheck");
			Transaction[] depositCheckTransactions= 
					transactionDAO.getTransactions(customer.getCustomer_id(), "DepositCheck");
			
			ArrayList<TransactionHistory> transactionInfo = new ArrayList<TransactionHistory>();
			
			for (Transaction trans : buyFundTransactions) {
				TransactionHistory item = new TransactionHistory();
				
				int fund_id = trans.getFund_id();
				System.out.println("fund id is: " + fund_id);
				Fund fund = fundDAO.getFund(fund_id);
				if (fund == null) {
					System.out.println("fund is null");
				}
				/*
				if (fund == null) {
					errors.add("Invalid fund");
					return "transaction-history.jsp";
				}
				*/
				
				Date dateOrigin = (Date) trans.getExecute_date();
				item.setDateForSort(dateOrigin);
				
				// get share : Long to double 
				long sharesOrigin = trans.getShares();
				if (dateOrigin == null) item.setShares("pending");
				else {
					String shares = sharesFormat.format((double) sharesOrigin / 1000);
					item.setShares(shares);
				}
		
				// amount
				double amountOrigin = trans.getAmount();
				String amount = priceFormat.format(amountOrigin / 100);
				item.setAmount(amount);
				
				// date
				if (dateOrigin == null) {
					item.setDate("pending");
				} else {
					String date = sdf.format(dateOrigin);
					item.setDate(date);
				}
				
				// get price: Long to double
				if (dateOrigin == null) {
					item.setPrice("pending");
				} else {
					long priceOrigin = 0;
					Fund_Price_History fundPrice = fundPriceHistoryDAO.getFundPrice(dateOrigin, fund_id);
					
					if (fundPrice == null) item.setPrice("pending");
					else {
						priceOrigin = fundPrice.getPrice();
						String price = priceFormat.format((double) priceOrigin / 100);
						item.setPrice(price);
					}
				}
				
				//System.out.println("#####################################" + fundPriceHistoryDAO.getFundPrice(dateOrigin, fund_id).getPrice());
				
				// name
				String name = fund.getName();
				item.setName(name);
				
				// symbol
				String symbol = fund.getSymbol();
				item.setSymbol(symbol);
				
				// operation
				String operation = trans.getTransaction_type();
				item.setOperation(operation);	
				
				transactionInfo.add(item);
			}
			
			for (Transaction trans : sellFundTransactions) {
				TransactionHistory item = new TransactionHistory();
				
				int fund_id = trans.getFund_id();
				Fund fund = fundDAO.getFund(fund_id);
				
				if (fund == null) {
					errors.add("Invalid fund");
					return "transaction-history.jsp";
				}
				
				Date dateOrigin = (Date) trans.getExecute_date();
				item.setDateForSort(dateOrigin);
				
				// get share : Long to double 
				long sharesOrigin = trans.getShares();
				String shares = sharesFormat.format((double) sharesOrigin / 1000);
				item.setShares(shares);
				
				// amount
				if (dateOrigin == null) {
					item.setAmount("pending");
				} else {
					double amountOrigin = trans.getAmount();
					String amount = priceFormat.format(amountOrigin / 100);
					item.setAmount(amount);
				}
				
				// date
				if (dateOrigin == null) {
					item.setDate("pending");
				} else {
					String date = sdf.format(dateOrigin);
					item.setDate(date);
				}
				
				// get price: Long to double
				if (dateOrigin == null) {
					item.setPrice("pending");
				} else {
					long priceOrigin = 0;
					Fund_Price_History fundPrice = fundPriceHistoryDAO.getFundPrice(dateOrigin, fund_id);
					if (fundPrice == null) item.setPrice("pending");
					else {
						priceOrigin = fundPrice.getPrice();
						String price = priceFormat.format((double) priceOrigin / 100);
						System.out.println("That day Price:" + price);
						item.setPrice(price);
					}
				}
				System.out.println("#####################################" + dateOrigin);
//				System.out.println("#####################################" + fundPriceHistoryDAO.getFundPrice(dateOrigin, fund_id).getPrice());
				
				// name
				String name = fund.getName();
				item.setName(name);
				
				// symbol
				String symbol = fund.getSymbol();
				item.setSymbol(symbol);
				
				// operation
				String operation = trans.getTransaction_type();
				item.setOperation(operation);	
				
				transactionInfo.add(item);
			}
			
			for (Transaction trans : requestCheckTransactions) {
				TransactionHistory item = new TransactionHistory();
				
				int fund_id = trans.getFund_id();
				Fund fund = fundDAO.getFund(fund_id);
				Fund_Price_History fundPriceHistory = fundPriceHistoryDAO.getLatestFundPrice(fund_id);
				
				// set price 
				item.setPrice("");
				
				// set share
				item.setShares("");
				
				// amount
				double amountOrigin = trans.getAmount();
				String amount = priceFormat.format(amountOrigin / 100);
				item.setAmount(amount);
				
				// date
				Date dateOrigin = (Date) trans.getExecute_date();
				item.setDateForSort(dateOrigin);
			
				if (dateOrigin == null) {
					item.setDate("pending");
				} else {
					String date = sdf.format(dateOrigin);
					item.setDate(date);
				}
	
				// name
				item.setName("");
				
				// symbol
				item.setSymbol("");
				
				// operation
				String operation = trans.getTransaction_type();
				item.setOperation(operation);	
				
				transactionInfo.add(item);
			}
			
			for (Transaction trans : depositCheckTransactions) {
				TransactionHistory item = new TransactionHistory();
				
				int fund_id = trans.getFund_id();
				Fund fund = fundDAO.getFund(fund_id);
				Fund_Price_History fundPriceHistory = fundPriceHistoryDAO.getLatestFundPrice(fund_id);
				
				// set price 
				item.setPrice("");
				
				// set share
				item.setShares("");
				
				// amount
				double amountOrigin = trans.getAmount();
				String amount = priceFormat.format(amountOrigin / 100);
				item.setAmount(amount);
				
				// date
				Date dateOrigin = (Date) trans.getExecute_date();
				item.setDateForSort(dateOrigin);
			
				if (dateOrigin == null) {
					item.setDate("pending");
				} else {
					String date = sdf.format(dateOrigin);
					item.setDate(date);
				}
				
				// name
				item.setName("");
				
				// symbol
				item.setSymbol("");
				
				// operation
				String operation = trans.getTransaction_type();
				item.setOperation(operation);	
				
				transactionInfo.add(item);
			}
			
			Comparator<TransactionHistory> comparator = new Comparator<TransactionHistory>() {
				public int compare(TransactionHistory trans1, TransactionHistory trans2) {
					if (trans1 == null || trans2 == null) return -1;
					if (trans1.getDateForSort() == null) return -1;
					if (trans2.getDateForSort() == null) return 1;
					if (trans1.getDateForSort().after(trans2.getDateForSort())) {
						return -1;
					} else if (trans2.getDateForSort().equals(trans2.getDateForSort())) {
						return 0;
					} else return 1;
				}
			};
			
			Collections.sort(transactionInfo, comparator);
 			request.setAttribute("transactionInfo", transactionInfo);
 			
			return "transaction-history.jsp";
		} catch (RollbackException e) {
			errors.add(e.getMessage());
			return "transaction-history.jsp";
		}
	}
}