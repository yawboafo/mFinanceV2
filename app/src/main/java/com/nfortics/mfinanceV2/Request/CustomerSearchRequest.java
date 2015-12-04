package com.nfortics.mfinanceV2.Request;

import com.activeandroid.util.Log;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Settings.GeneralSettings;
import com.nfortics.mfinanceV2.Utilities.Utils;

import java.net.URLEncoder;

/**
 * Created by bigfire on 11/19/2015.
 */
public class CustomerSearchRequest implements Request {

    public static final String BASE_URL = Application.serverURL
            + "customers.json?";
    private static String url;

    private GeneralSettings generalSettings;
    public CustomerSearchRequest(Customer client) {

        generalSettings=GeneralSettings.getInstance();
        // setting this value to be used as the instance egof the customer to
        // search for.
        Log.d("oxinbo","customer from search request "+client.getElectronicCardNumber());
        buildUrl(client);

    }
    public CustomerSearchRequest(String client) {


        // setting this value to be used as the instance egof the customer to
        // search for.

        buildUrl(client);

    }
    public void buildUrl(Customer client) {


        Log.d("oxinbo","electronic card is ="+client.getElectronicCardNumber());

     /***   try{
            StringBuffer urlBuilder = new StringBuffer();
            if (client.getBiometricId().trim().length() > 3 || client.getElectronicCardNumber().trim().length() > 3 || client.getBillCode().trim().length() > 3)
                urlBuilder.append(Application.serverURL2 + "customers.json?");
            else
                urlBuilder.append(Application.serverURL + "customers.json?");

            addAgentMsisdn(urlBuilder);
            addAccountCode(urlBuilder);
           // addSurname(client.getFirst_name(), urlBuilder);
          //  addAccountNumber(client.getAccount_number(), urlBuilder);
          //  addMobileNumber(client.getMobile_number(), urlBuilder);
          //  addCustomerId(client.getCustomer_id(), urlBuilder);
          //  addBillCode(client.getBillCode(), urlBuilder);
          //  addBiometricId(client.getBiometricId(), urlBuilder);
            addCardRefferenceNumber(client.getElectronicCardNumber(), urlBuilder);

            url = urlBuilder.toString();
        }catch (Exception e){

            e.printStackTrace();
        }

***/
    }




    public void buildUrl(String  cardnumber) {


        try{
            StringBuffer urlBuilder = new StringBuffer();
            if (cardnumber.length() > 3 )
                urlBuilder.append(Application.serverURL2 + "customers.json?");
            else
                urlBuilder.append(Application.serverURL + "customers.json?");

            addAgentMsisdn(urlBuilder);
            addAccountCode(urlBuilder);
            // addSurname(client.getFirst_name(), urlBuilder);
            //  addAccountNumber(client.getAccount_number(), urlBuilder);
            //  addMobileNumber(client.getMobile_number(), urlBuilder);
            //  addCustomerId(client.getCustomer_id(), urlBuilder);
            //  addBillCode(client.getBillCode(), urlBuilder);
            //  addBiometricId(client.getBiometricId(), urlBuilder);
            addCardRefferenceNumber(cardnumber, urlBuilder);

            url = urlBuilder.toString();
        }catch (Exception e){

            e.printStackTrace();
        }


    }
    private void addAgentMsisdn(StringBuffer urlBuilder) {
        urlBuilder.append("agent_msisdn=");
        urlBuilder.append(Application.getActiveAgent().getMsisdn());
    }

    private void addSurname(String surname, StringBuffer urlBuilder) {
        if (!surname.equals("") && !surname.equals(null)) {

            urlBuilder.append("&name=");// 0240533673
            urlBuilder.append(URLEncoder.encode(surname));
        }

    }

    private void addAccountNumber(String accNumber, StringBuffer urlBuilder) {
        if (!accNumber.equals("") && !accNumber.equals(null)) {

            urlBuilder.append("&customer_account=");
            urlBuilder.append(URLEncoder.encode(accNumber));
        }

    }

    private void addMobileNumber(String msisdn, StringBuffer urlBuilder) {
        if (!msisdn.equals("") && !msisdn.equals(null)) {

            urlBuilder.append("&customer_msisdn=");
            urlBuilder.append(msisdn);
        }
    }

    private void addCustomerId(String customerId, StringBuffer urlBuilder) {
        if (!customerId.equals("") && !customerId.equals(null)) {

            urlBuilder.append("&customer_uid=");
            urlBuilder.append(URLEncoder.encode(customerId));
        }
    }

    private void addBillCode(String billCode, StringBuffer urlBuilder) {
        if (!billCode.equals("") && !billCode.equals(null)) {
            urlBuilder.append("&bill_code=");
            urlBuilder.append(URLEncoder.encode(billCode));
        }
    }

    private void addBiometricId(String biometricId, StringBuffer urlBuilder) {
        if (!biometricId.equals("") && !biometricId.equals(null)) {

            urlBuilder.append("&biometric_id=");
            urlBuilder.append(biometricId);
        }
    }

    private void addCardRefferenceNumber(String cardRefferenceNumber,
                                         StringBuffer urlBuilder) {
        if (!cardRefferenceNumber.equals("") && !cardRefferenceNumber.equals(null)) {


                urlBuilder.append("&card_ref_num=");
                urlBuilder.append(cardRefferenceNumber);

        }
    }

    private void addAccountCode(StringBuffer urlBuilder) {
        urlBuilder.append("&account_code=");
        urlBuilder.append(URLEncoder.encode(Merchant.getActiveMerchant("true").getCode()));
    }

    private void addLocality(StringBuffer urlBuilder) {
        urlBuilder.append("&locality=");
        urlBuilder.append("abelempke");
    }

    @Override
    public String getURL() {
        // TODO Auto-generated method stub
        return url;
    }

}

