package com.nfortics.mfinanceV2.Request;



import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.Settings.GeneralSettings;

import java.net.URLEncoder;

public class CustomersPullRequest implements Request {
	User user;
	Merchant merchant;
	GeneralSettings generalSettings=GeneralSettings.getInstance();
	public static final String BASE_URL = Application.serverURL2 + "customers.json?";
	private static String url;


	public CustomersPullRequest(Customer client, int pageNumber) {
		try{

			user=User.load(User.class, 1);
			if(generalSettings.getActivemerchant()==null){

				merchant=Merchant.getAllMerchant(""+user.getId()).get(0);
			}else{
				merchant=generalSettings.getActivemerchant();

			}




		}catch (Exception e){

			e.printStackTrace();
		}

		buildUrl(client, pageNumber);
	}

	public void buildUrl(Customer client, int pageNumber) {
		StringBuffer urlBuilder = new StringBuffer(BASE_URL);

		addAgentMsisdn(urlBuilder);
		addAccountCode(urlBuilder);
		addRetrieveStatus(urlBuilder);
		addPerPage(urlBuilder);
		addPageNumber(pageNumber, urlBuilder);
		// addSurname(client.getFirst_name(), urlBuilder);
		// addAccountNumber(client.getAccount_number(), urlBuilder);
		// addMobileNumber(client.getMobile_number(), urlBuilder);
		// addCustomerId(client.getCustomer_id(), urlBuilder);
		// addBillCode(client.getBilCode(), urlBuilder);
		// addBiometricId(client.getBiometricId(), urlBuilder);
		// addCardRefferenceNumber(client.getElectronicCardNumber(),
		// urlBuilder);

		url = urlBuilder.toString();
	}

	private void addAgentMsisdn(StringBuffer urlBuilder) {
		urlBuilder.append("agent_msisdn=");
		urlBuilder.append(user.getMsisdn());
	}

	private void addAccountCode(StringBuffer urlBuilder) {
		urlBuilder.append("&account_code=");
		urlBuilder.append(URLEncoder.encode(merchant.getCode()));
	}

	private void addRetrieveStatus(StringBuffer urlBuilder) {
		urlBuilder.append("&retrieve=");
		urlBuilder.append("true");
	}

	private void addPerPage(StringBuffer urlBuilder) {
		urlBuilder.append("&per_page=");
		urlBuilder.append("200");
	}

	private void addPageNumber(int pageNum, StringBuffer urlBuilder) {
		// if (!pageNum.equals("") && !pageNum.equals(null)) {
		urlBuilder.append("&page=");
		urlBuilder.append(pageNum);
		// }
	}

	@Override
	public String getURL() {
		// TODO Auto-generated method stub
		return url;
	}

}
