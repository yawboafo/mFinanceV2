package com.nfortics.mfinanceV2.Activities;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.os.Vibrator;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.activeandroid.ActiveAndroid;
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.grabba.Grabba;
import com.grabba.GrabbaConnectionListener;
import com.grabba.GrabbaEventDispatchException;
import com.grabba.GrabbaMagstripe;
import com.grabba.GrabbaMagstripeListener;
import com.grabba.GrabbaSmartcardListener;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Dialogs.CustomerConfirmDialog;
import com.nfortics.mfinanceV2.Dialogs.CustomerConfirmDialog.CustomerConfirmDialogInteractionListener;
import com.nfortics.mfinanceV2.Dialogs.ShowAccountDialog;
import com.nfortics.mfinanceV2.Dialogs.ShowAccountDialog.ShowAccountDialogInteractionListener;
import com.nfortics.mfinanceV2.Handlers.GeneralRequestHandler;
import com.nfortics.mfinanceV2.Models.Account;
import com.nfortics.mfinanceV2.Models.C_Branch;
import com.nfortics.mfinanceV2.Models.C_FingerPrintInfo;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Models.Msisdn;
import com.nfortics.mfinanceV2.Models.ThirdPartyIntegration;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Request.CustomerSearchRequest;
import com.nfortics.mfinanceV2.Services.CustomerSearchService;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

public class BalancenquiryActivity
        extends
        BaseActivity implements
        ShowAccountDialogInteractionListener,
        CustomerConfirmDialogInteractionListener {


    View view;
    ListView listMenu;
    RelativeLayout relativeLayout;
    Typefacer typefacer;
    TextView checkby,customerDetailsTitle,balanceAccoount;

    Button printButton,smsbutton;

    EditText balanceAccountValue;
    Utils utils=new Utils();
    Spinner checkbySpinner;

    private static final String BASE_URL = Application.serverURL + "collections.json?";
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
    SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
    String formattedDate = df.format(c.getTime());
    String formattedDatetimstamp = sdf.format(c.getTime());
    int statusCode;
    String statusMessage;
    Vibrator vibrate ;

    static String balanceDeliveryType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typefacer=new Typefacer();
        getSupportActionBar().setTitle("");
        relativeLayout=(RelativeLayout)findViewById(R.id.compound);
        relativeLayout.removeAllViews();

        view=generateActivityView();


        SetView(view);
        relativeLayout.addView(view);
        getIntentVariables();
        populateSpinners();
        EventListen();
        vibrate = (Vibrator) BalancenquiryActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
    }

    void EventListen(){

        smsbuttonClickListen();
        printButtonClickListen();
    }

    void smsbuttonClickListen(){
        smsbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sms="SMS";
                balanceDeliveryType=sms;
                prepareBalanceEnquirer(sms);
            }
        });
    }
    void printButtonClickListen(){



        printButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String print="print";
                balanceDeliveryType=print;
                prepareBalanceEnquirer(print);
            }
        });
    }


    String getcheckbySpinnerValue(){



       return checkbySpinner.getSelectedItem().toString();

    }

    void prepareBalanceEnquirer(String type){

        if(balanceAccountValue.getText().toString().length()<=0){

            vibrate.vibrate(500);

            utils.ErrorAlertDialog(BalancenquiryActivity.this,"Fields Can never be Empty","Empty Fields");


            return;
        }

        String collectionType=getcheckbySpinnerValue();
        if(collectionType.equalsIgnoreCase("Account Number") ){
            Account account=Account.getAccountByAccountNumber(balanceAccountValue.getText().toString());
            if(account==null){
                Customer customer=new Customer();
                customer.setAccount_number(balanceAccountValue.getText().toString());
                searchCustomerVolley(customer,false);
            }else {

                showCustomerConfirmDialog(account.getCustomer_id(),confirmCollectionMessage(type));


            }
        }else  if(collectionType.equalsIgnoreCase("Electronic Card") ){

            if(Utils.deviceIsPhone()){

                Toast.makeText(BalancenquiryActivity.this, "This Device does not Support Electronic Card", Toast.LENGTH_LONG);

                // return;
            }else{

                ThirdPartyIntegration thirdPartyIntegration=ThirdPartyIntegration.getThirdPartyIntegrationByNumber(Application.getPan());

                if(thirdPartyIntegration==null){
                    Customer customer=new Customer();
                    customer.setElectronicCardNumber(Application.getPan());
                    searchCustomerVolley(customer,false);

                }else{

                    showCustomerConfirmDialog(thirdPartyIntegration.getCustomer_id(),confirmCollectionMessage(type));
                }
            }



        }else if(collectionType.equalsIgnoreCase("Phone Number") ){

            Msisdn msisdn=Msisdn.getMsisdnByNumber(Utils.formatMsisdn(balanceAccountValue.getText().toString()));
            // Customer customer=Customer.getCustomerByMsisdn(Utils.formatMsisdn(collectionNumber.getText().toString()));
            if(msisdn==null){
                Customer customers=new Customer();
                customers.setMobile_number(Utils.formatMsisdn(balanceAccountValue.getText().toString()));
                searchCustomerVolley(customers,false);

            }else{

                showCustomerConfirmDialog(msisdn.getCustomer_id(),confirmCollectionMessage(type));
            }

        }
    }


    String confirmCollectionMessage(String type){

        String content ="";

        switch (type)
        {

            case "SMS":
               content= "Confirm Balance request"+"\n" +
                        "From : "+balanceAccountValue.getText().toString()+
                        "\n"+"Send Via SMS.";

                break;

            case "print":

                content="Confirm Balance request"+"\n" +
                        "From : "+balanceAccountValue.getText().toString()+
                        "\n"+"Print Receipt. ";
                break;
        }


        return content;

    }
    void showCustomerConfirmDialog(String customer_id,String message){

        CustomerConfirmDialog customerConfirmDialog=CustomerConfirmDialog.newInstance(customer_id, message);
        showDialog(customerConfirmDialog);
    }

    private void populateSpinners(){
        try{




            if (Utils.isCCML()) {
                Utils.populateSpinner(BalancenquiryActivity.this, checkbySpinner, getResources().getStringArray(R.array.ccml_collection_categories));
                balanceAccountValue.setEnabled(false);
                readCard();
            } else {

                Utils.populateSpinner(BalancenquiryActivity.this, checkbySpinner, getResources().getStringArray(R.array.collection_categories));
                balanceAccountValue.setEnabled(true);
                //  Utils.makeListForBillersSpinners(FieldCollect.this,projectSpinner,userInfoSettings.getCurrentMerchant().getProjects());




            }

        }catch (Exception e){


        }

    }

    private void readCard() {
        if (Utils.deviceIsM50()) {
            //  startMagneticCardReading();
        } else if (Utils.deviceIsC180()) {
            // initData();
            //  stripeCard.readStripeCard();
        } else if (Utils.deviceIsGrabba()) {
            // initiate grabba card reading
            startGrabaCardReader();

        }

        // collectionNumber.setEnabled(false);
    }
    private void startGrabaCardReader() {
        try {
            GrabbaMagstripe.getInstance().addEventListener(grabbaMagstripeListener);
            ToastUtil.showToast(this, "Swipe card now");
        } catch (GrabbaEventDispatchException e) {
            e.printStackTrace();
        }
    }

    private void getIntentVariables() {
        Intent i = getIntent();
        String account_number = i.getStringExtra("account_number");
        if(account_number!=null){


            for (int k = 0; k < checkbySpinner.getCount(); k++) {
                if (checkbySpinner.getItemAtPosition(k).toString().equalsIgnoreCase("Account Number")) {
                    checkbySpinner.setSelection(k);
                }
            }

            balanceAccountValue.setText(account_number);
            checkbySpinner.setEnabled(false);
        }



    }

    private View generateActivityView(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_balancenquiry, null, false);

        return activityView;
    }

    private void searchCustomers(String customer) {
        CustomerSearchRequest request = new CustomerSearchRequest(customer);
        GeneralRequestHandler handler = new GeneralRequestHandler(this, getString
                (R.string.string_searching_customer)) {
            @Override
            protected void processMessage(Message message) {

                // checking if there was an internet connectivity issue.
                if (message.arg2 == 740) {
                    // Toast.makeText(FieldCollectionActivity.this, getString(R.string.string_conn_not_established),Toast.LENGTH_LONG).show();
                    utils.ErrorAlertDialog(BalancenquiryActivity.this,getString(R.string.string_conn_not_established), "Failed to Perform Online Search ").show();
                } else if (message.arg2 == 104) {

                    utils.ErrorAlertDialog(BalancenquiryActivity.this, getString(R.string.customer_does_notexist), "Customer Search ").show();
                    // Toast.makeText(FieldCollectionActivity.this, "custommer does not exist",Toast.LENGTH_LONG).show();
                } else if (message.arg2 == 100) {

                    utils.ErrorAlertDialog(BalancenquiryActivity.this,getString(R.string.somethingwentwrong),"Customer Search ").show();
                    //  Toast.makeText(FieldCollectionActivity.this, "message 100",Toast.LENGTH_LONG).show();
                } else  {
                    //  Toast.makeText(FieldCollectionActivity.this, "good good good ",Toast.LENGTH_LONG).show();


                    ShowAccountDialog dialog =  ShowAccountDialog.newInstance("");
                    //dialog.newInstance("","");
                    showDialog(dialog);
                }
            }
        };






        handler.showProgressDialog();
        new CustomerSearchService(handler).processRequest(request);
    }
    private void searchCustomers(Customer customer) {
        CustomerSearchRequest request = new CustomerSearchRequest(customer);
        GeneralRequestHandler handler = new GeneralRequestHandler(this, getString
                (R.string.string_searching_customer)) {
            @Override
            protected void processMessage(Message message) {

                // checking if there was an internet connectivity issue.
                if (message.arg2 == 740) {
                    // Toast.makeText(FieldCollect.this, getString(R.string.string_conn_not_established),Toast.LENGTH_LONG).show();

                } else if (message.arg2 == 104) {

                } else if (message.arg2 == 100) {

                } else {

                }
            }
        };






        handler.showProgressDialog();
        new CustomerSearchService(handler).processRequest(request);
    }



    private String buildURL(Customer customer ){

        StringBuffer urlBuilder = new StringBuffer();


        if ( customer.getBiometricId()!=null || customer.getElectronicCardNumber()!=null || customer.getBillCode()!=null )
            urlBuilder.append(Application.serverURL2 + "customers.json?");
        else
            urlBuilder.append(Application.serverURL + "customers.json?");


        urlBuilder.append("agent_msisdn=");
        urlBuilder.append(Application.getActiveAgent().getMsisdn());


        urlBuilder.append("&account_code=");
        urlBuilder.append(Merchant.getActiveMerchant("true").getCode());

        if(customer!=null){

            if(customer.getFirst_name()!=null &&  ! customer.getFirst_name().isEmpty() ){

                urlBuilder.append("&name=");// 0240533673
                urlBuilder.append(URLEncoder.encode(customer.getFirst_name()));

            }

            if(customer.getAccount_number()!=null &&  ! customer.getAccount_number().isEmpty()  ){

                urlBuilder.append("&customer_account=");
                urlBuilder.append(URLEncoder.encode(customer.getAccount_number()));
            }


            if(customer.getMobile_number()!=null &&   !customer.getMobile_number().isEmpty()){
                urlBuilder.append("&customer_msisdn=");
                urlBuilder.append(customer.getMobile_number());

            }
            if(customer.getCustomer_id()!=null &&   !customer.getCustomer_id().isEmpty()){
                urlBuilder.append("&customer_uid=");
                urlBuilder.append(customer.getCustomer_id());

            }

            if(customer.getBillCode()!=null &&   !customer.getBillCode().isEmpty()){
                urlBuilder.append("&bill_code=");
                urlBuilder.append(URLEncoder.encode(customer.getBillCode()));

            }

            if(customer.getBiometricId()!=null &&   !customer.getBiometricId().isEmpty()){
                urlBuilder.append("&biometric_id=");
                urlBuilder.append(URLEncoder.encode(customer.getBillCode()));

            }


            if(customer.getElectronicCardNumber()!=null &&   !customer.getElectronicCardNumber().isEmpty()){
                urlBuilder.append("&card_ref_num=");
                urlBuilder.append(customer.getElectronicCardNumber());

            }


        }








        return urlBuilder.toString();

    }



    private  void searchCustomerVolley(final Customer customer,final boolean token ){

        final ProgressDialog pDialog  = new ProgressDialog(BalancenquiryActivity.this);
        pDialog.setMessage("Searching Customers...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        pDialog.show();



        Log.d("oxinbo", "url " + buildURL(customer));
        volleySingleton= VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                buildURL(customer),
                new Response.Listener<JSONObject>() {
                    //Log.d("Params",params+"");
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        Log.d("oxinbo", "Server response" + response.toString());
                        statusCode= Utils.getAPIStatus(response);
                        String errorMessage=Utils.getMessageFromAPIStatus(statusCode);

                        try {


                            if(token){



                                if(statusCode==000){
                                    parseJsonObject(response);

                                    ThirdPartyIntegration tps=ThirdPartyIntegration.getThirdPartyIntegration(Application.getPan());

                                    if(tps!=null){
                                        ShowAccountDialog dialog =  ShowAccountDialog.newInstance(tps.getCustomer_id());
                                        //dialog.newInstance("","");
                                        showDialog(dialog);

                                    }else{

                                        Toast.makeText(BalancenquiryActivity.this,"Something Went Wrong Try Again",Toast.LENGTH_LONG);
                                    }

                                }else if(statusCode==104){

                                    utils.ErrorAlertDialog(BalancenquiryActivity.this,"Card is Not Linked to Any Accounts","Search Results").show();

                                }else{

                                    utils.ErrorAlertDialog(BalancenquiryActivity.this,errorMessage,"Search Results").show();
                                }



                            }else{


                                if(statusCode==000){

                                    parseJsonObject(response);
                                    showCustomerConfirmDialog(customer.getCustomer_id(), confirmCollectionMessage(balanceDeliveryType));

                                }else {


                                    utils.ErrorAlertDialog(BalancenquiryActivity.this, errorMessage, "Something Went Wrong").show();


                                }

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                        //  Message.messageShort(MyApplication.getAppContext(),""+tokenValue+"\n"+response.toString()+"\n"+booleaner);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try{


                    pDialog.hide();
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Log.d("oxinbo", "NoConnectionError.....TimeoutError..");


                        utils.ErrorAlertDialog(BalancenquiryActivity.this,"No Internet Connection ,Transaction Timed Out","Connectivity Error").show();



                    } else if (error instanceof AuthFailureError) {
                        Log.d("oxinbo", "AuthFailureError..");
                        int statusCode=9000;
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null) {
                            JSONObject jobj=null;
                            VolleyError verror = new VolleyError(new String(error.networkResponse.data));
                            try {
                                jobj = new JSONObject(verror.getMessage().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            statusCode= Utils.getAPIStatus(jobj);
                            Log.d("oxinbo", "errorMessage code : " + response.statusCode + "  response data " + jobj.toString());
                            String errorMessage=Utils.getMessageFromAPIStatus(statusCode);
                            utils.ErrorAlertDialog(BalancenquiryActivity.this,errorMessage,"AuthFailureError").show();
                        }




                    } else if (error instanceof ServerError) {
                        Log.d("oxinbo", "ServerError..");

                    } else if (error instanceof NetworkError) {
                        Log.d("oxinbo", "NetworkError..");
                        int statusCode=9000;
                        NetworkResponse response = error.networkResponse;
                        if(response != null && response.data != null) {
                            JSONObject jobj=null;
                            VolleyError verror = new VolleyError(new String(error.networkResponse.data));
                            try {
                                jobj = new JSONObject(verror.getMessage().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            statusCode= Utils.getAPIStatus(jobj);

                            String errorMessage=Utils.getMessageFromAPIStatus(statusCode);
                            utils.ErrorAlertDialog(BalancenquiryActivity.this, errorMessage, "NetworkError").show();
                        }

                    } else if (error instanceof ParseError) {
                        Log.d("oxinbo", "ParseError..");
                    }

                }catch (Exception e){

                    //utils.RetryAlertDialog(BalancenquiryActivity.this,"A serve Error Occurred,Report Error ?","Unexpected Error").show();

                    e.printStackTrace();
                }





            }
        }) {

        };
        int socketTimeout = 240000000;//4 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);
        requestQueue.add(request);








    }
    private void parseJsonObject(JSONObject jsonObject) {
        Msisdn msisdn ;
        ThirdPartyIntegration tpi ;
        Customer customer = new Customer();
        C_Branch branch = new C_Branch();
        C_FingerPrintInfo fpi ;
        Account ac = new Account();
        List<ThirdPartyIntegration> electronicCards ;
        List<C_FingerPrintInfo> fingerPrints ;
        List<Msisdn> msisdns ;

        JSONArray customers = null;
        try {
            customers = jsonObject.getJSONArray("customers");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("oxinbo", "customers response " + customers.toString());

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
                customer= Customer.getCustomer(tmpCustomerID);

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

    void SetView(View view){

        typefacer=new Typefacer();


        checkby=(TextView)view.findViewById(R.id.checkby);
        checkby.setTypeface(typefacer.getRoboCondensedLight(getAssets()));

        customerDetailsTitle=(TextView)view.findViewById(R.id.customerDetailsTitle);
        customerDetailsTitle.setTypeface(typefacer.getRoboCondensedLight(getAssets()));
        customerDetailsTitle.setText("Balance Enquiry");



        balanceAccoount
                =(TextView)view.findViewById(R.id.balanceAccoount);
        balanceAccoount.setTypeface(typefacer.getRoboCondensedLight(getAssets()));

        printButton=(Button)view.findViewById(R.id.printButton);
        printButton.setTypeface(typefacer.getRoboCondensedLight(getAssets()));


        smsbutton=(Button)view.findViewById(R.id.smsbutton);
        smsbutton.setTypeface(typefacer.getRoboCondensedLight(getAssets()));


        balanceAccountValue=(EditText)view.findViewById(R.id.balanceAccountValue);


        checkbySpinner=(Spinner)view.findViewById(R.id.checkbySpinner);

        Utils.populateSpinner(BalancenquiryActivity.this, checkbySpinner, getResources().getStringArray(R.array.collection_categories));

    }
    protected void updateViewGrabba(String string) {

        if (string == null) {
            return;
        }
        int index1 = string.indexOf("?");
        if (index1 != -1) {

            Application.setCardData(string.substring(1, index1 - 1));

            balanceAccountValue.post(new Runnable() {
                @Override
                public void run() {


                    ThirdPartyIntegration thirdPartyIntegration = ThirdPartyIntegration.getThirdPartyIntegrationByNumber("" + Application.getPan());

                    if (thirdPartyIntegration == null) {

                        Customer customer = new Customer();
                        customer.setElectronicCardNumber(Application.getPan().toString());

                        searchCustomerVolley(customer, true);
                        //  searchCustomers(Application.getPan().toString());
                    } else {
                        ShowAccountDialog dialog = ShowAccountDialog.newInstance(thirdPartyIntegration.getCustomer_id());
                        //dialog.newInstance("","");
                        showDialog(dialog);
                        //toastOnUiThread("found a customer with this number his id is "+thirdPartyIntegration.getCustomer_id());
                        //  collectionNumber.setText(thirdPartyIntegration.getCustomer_id());
                    }


                }
            });

        }

    }
    public void showDialog(DialogFragment dialogFragment){

        FragmentManager manager = this.getSupportFragmentManager();
        // CashPaymentDialog dialog= new CashPaymentDialog();
        DialogFragment dialog = dialogFragment;
        dialog.show(manager, "");

    }
    @Override
    protected void onResume() {
        super.onResume();
        if (Utils.deviceIsGrabba()) {
            Grabba.getInstance().addConnectionListener(grabbaConnectionListner);
            Grabba.getInstance().acquireGrabba();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (Utils.deviceIsGrabba()) {
            Grabba.getInstance().addConnectionListener(grabbaConnectionListner);
            Grabba.getInstance().acquireGrabba();
        }
    }

    private final GrabbaMagstripeListener grabbaMagstripeListener = new GrabbaMagstripeListener() {

        @Override
        public void magstripeReadEvent(byte[] track1, byte[] track2, byte[] track3) {
            //
            // byte[][] tracks = new byte[][] { track1, track2, track3 };
            // for (int i = 0; i < 3; i++) {
            if (track2 == null) {
                toastOnUiThread("Swipe again");
            } else if (track2.length == 0) {
                toastOnUiThread("Swipe again");
            } else {
                toastOnUiThread("Card Read Succesfully...");
                updateViewGrabba(new String(track2));

            }

        }

        @Override
        public void magstripeRawReadEvent(byte[] track1raw, byte[] track2raw, byte[] track3raw) {
            // TODO Auto-generated method stub

        }
    };
    private GrabbaSmartcardListener grabbaSmartcardListener = new GrabbaSmartcardListener() {

        @Override
        public void smartcardRemovedEvent() {
            // TODO Auto-generated method stub

        }

        @Override
        public void smartcardInsertedEvent() {
            // TODO Auto-generated method stub

        }
    };

    private final GrabbaConnectionListener grabbaConnectionListner = new GrabbaConnectionListener() {

        @Override
        public void grabbaDisconnectedEvent() {
            toastOnUiThread("Device disconnected");

        }

        @Override
        public void grabbaConnectedEvent() {

            toastOnUiThread("Device Connected");
        }

    };

    private void toastOnUiThread(final String msg) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Toast.makeText(BalancenquiryActivity.this, msg, Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void ShowAccountDialogInteraction(String value) {
        balanceAccountValue.setText(value);
    }

    @Override
    public void CustomerConfirmDialogInteraction(String message) {

    }
}
