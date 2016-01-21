package com.nfortics.mfinanceV2.Services;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.activeandroid.ActiveAndroid;

import com.nfortics.mfinanceV2.Models.Account;
import com.nfortics.mfinanceV2.Models.C_Branch;
import com.nfortics.mfinanceV2.Models.C_FingerPrintInfo;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.Models.Msisdn;
import com.nfortics.mfinanceV2.Models.ThirdPartyIntegration;
import com.nfortics.mfinanceV2.Request.CustomersPullRequest;
import com.nfortics.mfinanceV2.Request.HTTPRequest;
import com.nfortics.mfinanceV2.Request.Request;
import com.nfortics.mfinanceV2.Service;
import com.nfortics.mfinanceV2.Settings.GeneralSettings;
import com.nfortics.mfinanceV2.Utilities.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomersDownloadService extends Service {

  Msisdn msisdn ;
  ThirdPartyIntegration tpi ;
  Customer customer = new Customer();
  C_Branch branch = new C_Branch();
  C_FingerPrintInfo fpi ;
  Account ac = new Account();
  List<ThirdPartyIntegration> electronicCards ;
  List<C_FingerPrintInfo> fingerPrints ;
  List<Msisdn> msisdns ;
  private GeneralSettings generalSettings;


  public CustomersDownloadService(Handler handler) {
    super(handler);
  }

  @Override
  public void processRequest(Request request) {
    // ServiceProcessor processor = new ServiceProcessor(request);
    // new Thread(processor).start();
  }

  public void processRequest() {
    ServiceProcessor processor = new ServiceProcessor();
    new Thread(processor).start();
  }

  private class ServiceProcessor implements Runnable {

    public ServiceProcessor() {

      generalSettings = GeneralSettings.getInstance();



    }

    @Override
    public void run() {
      try {


        Message message = new Message();
        // just a random value to initialize statusCode
        int statusCode = 11111111;
        int page = 1;
        boolean loop = true;

        while (loop) {
          HTTPRequest httpRequest = new HTTPRequest();
          InputStream inputStream = httpRequest.processGetRequest(new CustomersPullRequest(new Customer(), page));

          JSONObject jsonObject = new JSONObject(Utils.convertStreamToString(inputStream));

          statusCode = Utils.getAPIStatus(jsonObject);

          if (statusCode == 000) {
            try {

              JSONArray customers = jsonObject.getJSONArray("customers");
              parseJsonObject(jsonObject, customers);


              Log.d("oxinbo",customers.toString());
              if (customers.length() > 0)
                page = page + 1;

            } catch (Exception e) {
              loop = false;
            }

          } else if (statusCode == 140) {

          }
        }
        message.arg1 = statusCode;

        // creating a bundle to be sent to handler so it can determine
        // which Request class sent the handler. This is used within the
        // processStatusCode method so appropriate actions can be taken
        // for specific requests
        Bundle bundle = new Bundle();
        // bundle.putString("request_class_name", request.getClass()
        // .getName());
        message.setData(bundle);

        handler.sendMessage(message);
      } catch (ConnectException e) {
        e.printStackTrace();
        // creating and setting message arg2 to 0 to tell the handler
        // the
        // processing had a Connection Exception error
        Message msg = new Message();
        msg.arg2 = 740;
        handler.sendMessage(msg);

      } catch (UnknownHostException e) {
        e.printStackTrace();
        // creating and setting message arg2 to 0 to tell the handler
        // the
        // processing had a UnknownHost Exception error
        Message msg = new Message();
        msg.arg2 = 740;
        handler.sendMessage(msg);

      } catch (Exception e) {
        e.printStackTrace();
        Message msg = new Message();
        msg.arg2 = 100;
        handler.sendMessage(msg);
        // handler.sendMessage(createMessage(Application.ERROR, e));
      }
    }
  }


  private void parseJsonObject(JSONObject jsonObject, JSONArray customers) {


    Log.d("oxinbo","customers response "+customers.toString());

    for (int i = 0; i < customers.length(); i++) {

      List<Account> aa = new ArrayList<Account>();
      List<C_Branch> branches = new ArrayList<C_Branch>();
      electronicCards = new ArrayList<ThirdPartyIntegration>();
      fingerPrints = new ArrayList<C_FingerPrintInfo>();
      msisdns = new ArrayList<Msisdn>();

      try {
        JSONObject customerObjects = customers.getJSONObject(i);
        JSONObject customerObject = customerObjects.getJSONObject("customer");

        String tmpCustomerID=customerObject.getString("uid");
        customer=Customer.getCustomer(tmpCustomerID);

        if(customer==null){

          customer = new Customer();
        }


        customer.setFullname(customerObject.getString("name"));
        //  customerSegal.setFirst_name(customerObject.getString("name"));

        customer.setCustomer_id(customerObject.getString("uid"));

        customer.setPhotoUrl(customerObject.getString("photo_url"));
        // customerSegal.setPhotoUrl(customerObject.getString("photo_url"));
        try {
          String[] locality = customerObject.getString("locality").split(", ");
          if (locality.length >= 3) {
            if (locality[0] != null)
              customer.setHouseNumber(locality[0]);

            if (locality[1] != null)
              customer.setStreetName(locality[1]);

            if (locality[2] != null)
              customer.setCity(locality[2]);

          } else if (locality.length == 2) {
            if (locality[0] != null)
              customer.setStreetName(locality[0]);

            if (locality[1] != null)
              customer.setCity(locality[1]);

          }
        } catch (Exception e) {
          e.printStackTrace();
        }

        try {
          customer.setGender(customerObject.getString("gender"));
          customer.setTitle(customerObject.getString("title"));
          customer.setIdentificationType(customerObject.getString("id_type"));
          customer.setIdentificationNumber(customerObject.getString("id_number"));
          customer.setDob(customerObject.getString("dob"));
          customer.setHouseNumber(customerObject.getString("address1"));
          customer.setStreetName(customerObject.getString("address2"));
          customer.setCity(customerObject.getString("address3"));

        } catch (Exception e) {
          e.printStackTrace();
        }
        // customer.setBillCode(customerObject.getString("bill_post_or_zip_code"));

        // customer.setElectronicCardNumber(customerObject
        // .getString("card_ref_num"));

        String accountNumbers = customerObject.getString("account_numbers");

        accountNumbers = accountNumbers.replace("[", "");
        accountNumbers = accountNumbers.replace("]", "");
        accountNumbers = accountNumbers.replace("\"", "");
        String[] parts = accountNumbers.split(",");

        customer.setAccount_numbers(Arrays.asList(parts).toString());

        Log.d("oxinbo","splitted accout numebers"+customer.getAccount_numbers());

        JSONArray accs = customerObject.getJSONArray("accounts");

        for (int a = 0; a < accs.length(); a++) {

          JSONObject accObjects = accs.getJSONObject(a);
          JSONObject accObject = accObjects.getJSONObject("account");


          String accnumber=java.net.URLDecoder.decode(accObject.getString("account_number"), "utf-8");


          ac= Account.getAccount(tmpCustomerID,accnumber);
          if(ac==null){

            ac=new Account();
          }




          ac.setAccount_number(accnumber);
          ac.setDescription(accObject.getString("description"));
          ac.setPrimary(accObject.getBoolean("primary"));





          Log.d("oxinbo", "Account number unpurified " + accObject.getString("account_number"));

          try {
            ac.setArrears(accObject.getString(("arrears")));

          } catch (Exception e) {
            Log.i("oxinbo", "No arrears in JSON");
          }

          Utils.LogInfo(getClass().getName(), ">>> INFO customer account number " + ac.getAccount_number() + " description " + ac.getDescription());

          aa.add(ac);
        }

        try {
          JSONArray branchesArray = customerObject.getJSONArray("branches");
          for (int p = 0; p < branchesArray.length(); p++) {

            JSONObject f = branchesArray.getJSONObject(p);
            JSONObject bracnchJSONObject = f.getJSONObject("branch");


            String branchcode=bracnchJSONObject.getString("branch_code");
            //
            branch=C_Branch.getC_Branch(tmpCustomerID,branchcode);
            if(branch==null){
              branch = new C_Branch();


            }


            branch.setName(bracnchJSONObject.getString("name"));
            branch.setBranchCode(branchcode);
            branch.setMain(bracnchJSONObject.getBoolean("main"));

            Utils.LogInfo(getClass().getName(), ">>> INFO customer branch " + branch.getName() + " branch code " + branch.getBranchCode());
            branches.add(branch);
          }
        } catch (Exception e) {
          Log.i("oxinbo", "No branches in JSON");
        }

        // Parsing the customer's msisdn
        JSONArray msisdnsArray = customerObject.getJSONArray("msisdns");

        for (int a = 0; a < msisdnsArray.length(); a++) {

          JSONObject msisdnObjects = msisdnsArray.getJSONObject(a);
          JSONObject msisdnObject = msisdnObjects.getJSONObject("msisdn");



          String number=msisdnObject.getString("number");

          msisdn=Msisdn.getMsisdn(tmpCustomerID,number);
          if(msisdn==null){

            msisdn = new Msisdn();
          }


          msisdn.setNumber(number);
          Utils.LogInfo(getClass().getName(), ">>> INFO customer phone number =  " + msisdn.getNumber());
          msisdns.add(msisdn);
        }

        // Parsing the customer's finger print data
        JSONArray fingerPrintsArray = customerObject.getJSONArray("bio_details");

        for (int a = 0; a < fingerPrintsArray.length(); a++) {

          JSONObject fingerPrintObjects = fingerPrintsArray.getJSONObject(a);
          JSONObject fingerPrintObject = fingerPrintObjects.getJSONObject("bio_detail");


          fpi= C_FingerPrintInfo.getC_FingerPrintInfo(tmpCustomerID);
          if(fpi==null){

            fpi=new C_FingerPrintInfo();
          }


          fpi.setAfisTemplate(fingerPrintObject.getString("bio_id"));
          fpi.setDescription(fingerPrintObject.getString("description"));
          Utils.LogInfo(getClass().getName(), ">>> INFO customer finger print id =  " + fpi.getAfisTemplate() + " DESCRIPTION = " + fpi.getDescription());
          fingerPrints.add(fpi);
        }

        // Parsing the customer's finger print data
        JSONArray electronicCardsArray = customerObject.getJSONArray("card_numbers");

        for (int a = 0; a < electronicCardsArray.length(); a++) {

          JSONObject electronicCardObjects = electronicCardsArray.getJSONObject(a);
          JSONObject electronicCardObject = electronicCardObjects.getJSONObject("card_number");


          String number=electronicCardObject.getString("pan");

          tpi=ThirdPartyIntegration.getThirdPartyIntegration(tmpCustomerID,number);

          if(tpi==null){

            tpi=new ThirdPartyIntegration();
          }

          tpi.setNumber(electronicCardObject.getString("pan"));
          // fpi.setDescription(fingerPrintObject.getString("description"));
          Utils.LogInfo(getClass().getName(), ">>> INFO customer card number =  " + tpi.getNumber());
          electronicCards.add(tpi);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      //Customer customerSegal=new Customer();
      //customerDao.insert();

      ActiveAndroid.beginTransaction();
      try
      {
        Long idd= customer.save();

        Log.d("oxinbo","ID= " + idd.toString()+"  Customer Real id=  "+customer.getCustomer_id());


        for(Account account:aa){

          account.setCustomer_id(customer.getCustomer_id());
          account.save();
          Log.d("oxinbo", "Account ID = " + account.getId()+" customer id "+account.getCustomer_id()   );
        }


        for(Msisdn _m:msisdns){

          _m.setCustomer_id(customer.getCustomer_id());
          _m.save();
          Log.d("oxinbo", "Msisdn ID= " + _m.getId()+"customer id "+_m.getCustomer_id()  );
        }


        for(ThirdPartyIntegration _t:electronicCards){

          _t.setCustomer_id(customer.getCustomer_id());
          _t.save();

          Log.d("oxinbo", "ThirdPartyIntegration ID= " + _t.getId()+"Customer Id "+_t.getCustomer_id());
        }



        for(C_FingerPrintInfo _f:fingerPrints){

          _f.setCustomer_id(customer.getCustomer_id());
          _f.save();
          Log.d("oxinbo", "Finger Print ID = " + _f.getId()+" customer id ="+_f.getCustomer_id());
        }

        for(C_Branch _b:branches){

          _b.setCustomer_id(customer.getCustomer_id());
          _b.save();

          Log.d("oxinbo", "Branch ID= " + _b.getId()+"customer Id"+_b.getCustomer_id());
        }



        ActiveAndroid.setTransactionSuccessful();
      }
      finally {
        ActiveAndroid.endTransaction();
      }


    }

    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
  }

/***
  private void parseJsonObject(JSONObject jsonObject, JSONArray customers) {


    Log.d("oxinbo","customers response "+customers.toString());

    for (int i = 1; i < customers.length(); i++) {

      List<Account> aa = new ArrayList<Account>();
      List<C_Branch> branches = new ArrayList<C_Branch>();
      electronicCards = new ArrayList<ThirdPartyIntegration>();
      fingerPrints = new ArrayList<C_FingerPrintInfo>();
      msisdns = new ArrayList<Msisdn>();

      try {
        JSONObject customerObjects = customers.getJSONObject(i-1);
        JSONObject customerObject = customerObjects.getJSONObject("customer");

      String tmpCustomerID=customerObject.getString("uid");
        customer=Customer.getCustomer(tmpCustomerID);

        if(customer==null){

          customer = new Customer();
        }


        customer.setFirst_name(customerObject.getString("name"));
        //  customerSegal.setFirst_name(customerObject.getString("name"));

        customer.setCustomer_id(customerObject.getString("uid"));

        customer.setPhotoUrl(customerObject.getString("photo_url"));
        // customerSegal.setPhotoUrl(customerObject.getString("photo_url"));
        try {
          String[] locality = customerObject.getString("locality").split(", ");
          if (locality.length >= 3) {
            if (locality[0] != null)
              customer.setHouseNumber(locality[0]);

            if (locality[1] != null)
              customer.setStreetName(locality[1]);

            if (locality[2] != null)
              customer.setCity(locality[2]);

          } else if (locality.length == 2) {
            if (locality[0] != null)
              customer.setStreetName(locality[0]);

            if (locality[1] != null)
              customer.setCity(locality[1]);

          }
        } catch (Exception e) {
          e.printStackTrace();
        }

        try {
          customer.setGender(customerObject.getString("gender"));
          customer.setTitle(customerObject.getString("title"));
          customer.setIdentificationType(customerObject.getString("id_type"));
          customer.setIdentificationNumber(customerObject.getString("id_number"));
          customer.setDob(customerObject.getString("dob"));
          customer.setHouseNumber(customerObject.getString("address1"));
          customer.setStreetName(customerObject.getString("address2"));
          customer.setCity(customerObject.getString("address3"));

        } catch (Exception e) {
          e.printStackTrace();
        }
        // customer.setBillCode(customerObject.getString("bill_post_or_zip_code"));

        // customer.setElectronicCardNumber(customerObject
        // .getString("card_ref_num"));

        String accountNumbers = customerObject.getString("account_numbers");

        accountNumbers = accountNumbers.replace("[", "");
        accountNumbers = accountNumbers.replace("]", "");
        accountNumbers = accountNumbers.replace("\"", "");
        String[] parts = accountNumbers.split(",");

        customer.setAccount_numbers(Arrays.asList(parts).toString());

        Log.d("oxinbo","splitted accout numebers"+customer.getAccount_numbers());

        JSONArray accs = customerObject.getJSONArray("accounts");

        for (int a = 1; a < accs.length(); a++) {

          JSONObject accObjects = accs.getJSONObject(a-1);
          JSONObject accObject = accObjects.getJSONObject("account");


          String accnumber=java.net.URLDecoder.decode(accObject.getString("account_number"), "utf-8");


          ac= Account.getAccount(tmpCustomerID,accnumber);
          if(ac==null){

            ac=new Account();
          }




          ac.setAccount_number(accnumber);
          ac.setDescription(accObject.getString("description"));
          ac.setPrimary(accObject.getBoolean("primary"));





          Log.d("oxinbo", "Account number unpurified " + accObject.getString("account_number"));

          try {
            ac.setArrears(accObject.getString(("arrears")));

          } catch (Exception e) {
            Log.i("oxinbo", "No arrears in JSON");
          }

          Utils.LogInfo(getClass().getName(), ">>> INFO customer account number " + ac.getAccount_number() + " description " + ac.getDescription());

          aa.add(ac);
        }

        try {
          JSONArray branchesArray = customerObject.getJSONArray("branches");
          for (int p = 1; p < branchesArray.length(); p++) {

            JSONObject f = branchesArray.getJSONObject(p-1);
            JSONObject bracnchJSONObject = f.getJSONObject("branch");


            String branchcode=bracnchJSONObject.getString("branch_code");
            //
            branch=C_Branch.getC_Branch(tmpCustomerID,branchcode);
            if(branch==null){
              branch = new C_Branch();


            }


            branch.setName(bracnchJSONObject.getString("name"));
            branch.setBranchCode(branchcode);
            branch.setMain(bracnchJSONObject.getBoolean("main"));

            Utils.LogInfo(getClass().getName(), ">>> INFO customer branch " + branch.getName() + " branch code " + branch.getBranchCode());
            branches.add(branch);
          }
        } catch (Exception e) {
          Log.i("oxinbo", "No branches in JSON");
        }

        // Parsing the customer's msisdn
        JSONArray msisdnsArray = customerObject.getJSONArray("msisdns");

        for (int a = 1; a < msisdnsArray.length(); a++) {

          JSONObject msisdnObjects = msisdnsArray.getJSONObject(a-1);
          JSONObject msisdnObject = msisdnObjects.getJSONObject("msisdn");



          String number=msisdnObject.getString("number");

          msisdn=Msisdn.getMsisdn(tmpCustomerID,number);
          if(msisdn==null){

            msisdn = new Msisdn();
          }


          msisdn.setNumber(number);
          Utils.LogInfo(getClass().getName(), ">>> INFO customer phone number =  " + msisdn.getNumber());
          msisdns.add(msisdn);
        }

        // Parsing the customer's finger print data
        JSONArray fingerPrintsArray = customerObject.getJSONArray("bio_details");

        for (int a = 1; a < fingerPrintsArray.length(); a++) {

          JSONObject fingerPrintObjects = fingerPrintsArray.getJSONObject(a-1);
          JSONObject fingerPrintObject = fingerPrintObjects.getJSONObject("bio_detail");


          fpi= C_FingerPrintInfo.getC_FingerPrintInfo(tmpCustomerID);
          if(fpi==null){

            fpi=new C_FingerPrintInfo();
          }


          fpi.setAfisTemplate(fingerPrintObject.getString("bio_id"));
          fpi.setDescription(fingerPrintObject.getString("description"));
          Utils.LogInfo(getClass().getName(), ">>> INFO customer finger print id =  " + fpi.getAfisTemplate() + " DESCRIPTION = " + fpi.getDescription());
          fingerPrints.add(fpi);
        }

        // Parsing the customer's finger print data
        JSONArray electronicCardsArray = customerObject.getJSONArray("card_numbers");

        for (int a = 1; a < electronicCardsArray.length(); a++) {

          JSONObject electronicCardObjects = electronicCardsArray.getJSONObject(a-1);
          JSONObject electronicCardObject = electronicCardObjects.getJSONObject("card_number");


         String number=electronicCardObject.getString("pan");

          tpi=ThirdPartyIntegration.getThirdPartyIntegration(tmpCustomerID,number);

          if(tpi==null){

            tpi=new ThirdPartyIntegration();
          }

          tpi.setNumber(electronicCardObject.getString("pan"));
          // fpi.setDescription(fingerPrintObject.getString("description"));
          Utils.LogInfo(getClass().getName(), ">>> INFO customer card number =  " + tpi.getNumber());
          electronicCards.add(tpi);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }

      //Customer customerSegal=new Customer();
      //customerDao.insert();

      ActiveAndroid.beginTransaction();
      try
      {
        Long idd= customer.save();

        Log.d("oxinbo","ID= " + idd.toString()+"  Customer Real id=  "+customer.getCustomer_id());


        for(Account account:aa){

          account.setCustomer_id(customer.getCustomer_id());
          account.save();
          Log.d("oxinbo", "Account ID = " + account.getId()+" customer id "+account.getCustomer_id()   );
        }


        for(Msisdn _m:msisdns){

          _m.setCustomer_id(customer.getCustomer_id());
          _m.save();
          Log.d("oxinbo", "Msisdn ID= " + _m.getId()+"customer id "+_m.getCustomer_id()  );
        }


        for(ThirdPartyIntegration _t:electronicCards){

          _t.setCustomer_id(customer.getCustomer_id());
          _t.save();

          Log.d("oxinbo", "ThirdPartyIntegration ID= " + _t.getId()+"Customer Id "+_t.getCustomer_id());
        }



        for(C_FingerPrintInfo _f:fingerPrints){

          _f.setCustomer_id(customer.getCustomer_id());
          _f.save();
          Log.d("oxinbo", "Finger Print ID = " + _f.getId()+" customer id ="+_f.getCustomer_id());
        }

        for(C_Branch _b:branches){

          _b.setCustomer_id(customer.getCustomer_id());
          _b.save();

          Log.d("oxinbo", "Branch ID= " + _b.getId()+"customer Id"+_b.getCustomer_id());
        }



        ActiveAndroid.setTransactionSuccessful();
      }
      finally {
        ActiveAndroid.endTransaction();
      }


    }

    // } catch (JSONException e) {
    // e.printStackTrace();
    // }
  }
***/





}
