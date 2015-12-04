package com.nfortics.mfinanceV2.Activities;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupMenu;
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
import com.android.volley.toolbox.StringRequest;
import com.melnykov.fab.FloatingActionButton;
import com.nfortics.mfinanceV2.Activities.MenuActivities.FieldCollectionMenuActivity;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Dialogs.CustomerDetailDialog;
import com.nfortics.mfinanceV2.Dialogs.CustomerDetailDialog.CustomerDetailDialogInteractionListener;
import com.nfortics.mfinanceV2.Handlers.GeneralRequestHandler;
import com.nfortics.mfinanceV2.Models.Account;
import com.nfortics.mfinanceV2.Models.C_Branch;
import com.nfortics.mfinanceV2.Models.C_FingerPrintInfo;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Models.Msisdn;
import com.nfortics.mfinanceV2.Models.ThirdPartyIntegration;
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Services.CustomersDownloadService;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.ViewAdapters.CustomerViewAdapter;
import com.nfortics.mfinanceV2.Volley.VolleySingleton;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class CustomerActivity extends
        BaseActivity
        implements
        CustomerDetailDialogInteractionListener{

    View view;
    ListView listMenu;
    RelativeLayout relativeLayout;
    Typefacer typefacer;
    private TextView txtTitleField,txtDiscripFieldC;
    Utils utils=new Utils();
    RecyclerView recyclerView;
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;

    TextView edtSearch;
    Spinner filterTypespinner;
    CustomerViewAdapter customerViewAdapter;


    ImageButton filterButton,searchButton;

    User user;
    Merchant merchant;

    Msisdn msisdn ;
    ThirdPartyIntegration tpi ;
    Customer customer = new Customer();
    C_Branch branch = new C_Branch();
    C_FingerPrintInfo fpi ;
    Account ac = new Account();
    List<ThirdPartyIntegration> electronicCards ;
    List<C_FingerPrintInfo> fingerPrints ;
    List<Msisdn> msisdns ;

    FloatingActionButton fab;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        typefacer=new Typefacer();
        getSupportActionBar().setTitle("");
        relativeLayout=(RelativeLayout)findViewById(R.id.compound);


        view=generateActivityView();

        relativeLayout.removeAllViews();


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
        setLabels(view);
        relativeLayout.addView(view);
       // setFloatingButton();


        if(Customer.getAllCustomers().size()<=0){

            pullAllCustomers();


        }


        new LoadRecycleView(view).execute();
        try{
           // pullAllCustomers();
           // FetchCustmersFromServer(user.getMsisdn(),merchant.getCode(),1);

           // pullAllCustomers();

            //FetchCustmersFromServer(user.getMsisdn(), merchant.getCode(), 1);
        }catch (Exception e)
        {

        }
        EventListners();

    }

     void setLabels(View view ){
         FloatingButton(view);
         SetRecycleView(view);

         edtSearch=(EditText)view.findViewById(R.id.edtSearch);
         edtSearch.setHint("Search By Name");
         edtSearch.setTypeface(typefacer.squareLight());

         edtSearch.addTextChangedListener(FilerTextWatcher);
         filterTypespinner=(Spinner)view.findViewById(R.id.filterTypespinner);


         filterButton=(ImageButton)view.findViewById(R.id.filterButton);
         searchButton=(ImageButton)view.findViewById(R.id.searchButton);


        // filterTypespinner.setTypeface(typefacer.getRoboCondensedLight(this.getAssets()));

     }


    void filterTypespinnerChangeListener(){

        filterTypespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


                String selected = parent.getSelectedItem().toString();
                getFilterSpinner(selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    void FilterImageButtonClickListener(){


        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showMerchantsDialog();
            }
        });
    }

    private void EventListners(){

        filterTypespinnerChangeListener();
        FilterImageButtonClickListener();
    }

    void getFilterSpinner(String itm){

        if(itm.equals("Account Number")){

            edtSearch.setHint("Search By Account Number");
        }else if(itm.equals("Phone Number")){

            edtSearch.setHint("Search By Phone Number");

        }else if(itm.equals("Name")){

            edtSearch.setHint("Search By Name");
        }

    }
    private void pullAllCustomers() {
        GeneralRequestHandler handler = new GeneralRequestHandler(this, "downloading customers") {

            @Override
            protected void processMessage(Message message) {


                new LoadRecycleView(view).execute();

            }
        };
        handler.showProgressDialog();
        new CustomersDownloadService(handler).processRequest();
    }
    public static final String BASE_URL = Application.serverURL2 + "customers.json?";

    private String buildURL(String msisdn,String code,String pageNum ){

        StringBuffer urlBuilder = new StringBuffer(BASE_URL);
        urlBuilder.append("agent_msisdn=");
        urlBuilder.append(msisdn);


        urlBuilder.append("&account_code=");
        urlBuilder.append(URLEncoder.encode(code));


        urlBuilder.append("&retrieve=");
        urlBuilder.append("true");


        urlBuilder.append("&per_page=");
        urlBuilder.append("200");


        urlBuilder.append("&page=");
        urlBuilder.append(pageNum);

        return urlBuilder.toString();

    }

    private  void FetchCustmersFromServer(String agent_msisdn,String account_code,int pageNum){

        final ProgressDialog pDialog  = new ProgressDialog(CustomerActivity.this);
        pDialog.setMessage("Downloading Customers...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        pDialog.show();


        final HashMap<String, String> params = new HashMap<String, String>();


        params.put("agent_msisdn", agent_msisdn);
        params.put("&account_code", URLEncoder.encode(account_code));
        params.put("&retrieve", "true");
        params.put("&per_page", "200");
        params.put("&page", ""+pageNum);

        volleySingleton= VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                buildURL(user.getMsisdn(),merchant.getCode(),""+1),
                new Response.Listener<JSONObject>() {
                    //Log.d("Params",params+"");
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        Log.d("oxinbo", "Server response" + response.toString());


                        //  Message.messageShort(MyApplication.getAppContext(),""+tokenValue+"\n"+response.toString()+"\n"+booleaner);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                try{


                    pDialog.hide();
                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                        Log.d("oxinbo", "NoConnectionError.....TimeoutError..");


                        utils.ErrorAlertDialog(CustomerActivity.this,"No Internet Connection ,Login In Timed Out","Connectivity Error").show();



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
                            utils.ErrorAlertDialog(CustomerActivity.this,errorMessage,"AuthFailureError").show();
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
                            utils.ErrorAlertDialog(CustomerActivity.this, errorMessage, "NetworkError").show();
                        }

                    } else if (error instanceof ParseError) {
                        Log.d("oxinbo", "ParseError..");
                    }

                }catch (Exception e){

                    utils.RetryAlertDialog(CustomerActivity.this,"A serve Error Occurred,Report Error ?","Unexpected Error").show();

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


        Log.d("oxinbo", "Server params" + params.toString());





    }

    private void sendRequest(String agent_msisdn,String code,String pagenum){
        volleySingleton= VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();
        StringRequest stringRequest = new StringRequest(buildURL(agent_msisdn,code,pagenum),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       Log.d("oxinbo","respose = "+response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("oxinbo","respose = "+error.toString());
                    }
                });

        requestQueue.add(stringRequest);
    }

    private void parseJsonObject(JSONObject jsonObject, JSONArray customers) {


        for (int i = 1; i < customers.length(); i++) {

            List<Account> aa = new ArrayList<Account>();
            List<C_Branch> branches = new ArrayList<C_Branch>();
            electronicCards = new ArrayList<ThirdPartyIntegration>();
            fingerPrints = new ArrayList<C_FingerPrintInfo>();
            msisdns = new ArrayList<Msisdn>();

            try {
                JSONObject customerObjects = customers.getJSONObject(i-1);
                JSONObject customerObject = customerObjects.getJSONObject("customer");


                customer=Customer.load(Customer.class,i);

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





                    ac= Account.load(Account.class,a);
                    if(ac==null){

                        ac=new Account();
                    }


                    String accnumber=java.net.URLDecoder.decode(accObject.getString("account_number"), "utf-8");

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

                      //
                        branch=C_Branch.load(C_Branch.class,p);
                        if(branch==null){
                            branch = new C_Branch();


                        }


                        branch.setName(bracnchJSONObject.getString("name"));
                        branch.setBranchCode(bracnchJSONObject.getString("branch_code"));
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


                    msisdn=Msisdn.load(Msisdn.class,a);
                    if(msisdn==null){

                        msisdn = new Msisdn();
                    }


                    msisdn.setNumber(msisdnObject.getString("number"));
                    Utils.LogInfo(getClass().getName(), ">>> INFO customer phone number =  " + msisdn.getNumber());
                    msisdns.add(msisdn);
                }

                // Parsing the customer's finger print data
                JSONArray fingerPrintsArray = customerObject.getJSONArray("bio_details");

                for (int a = 1; a < fingerPrintsArray.length(); a++) {

                    JSONObject fingerPrintObjects = fingerPrintsArray.getJSONObject(a-1);
                    JSONObject fingerPrintObject = fingerPrintObjects.getJSONObject("bio_detail");


                    fpi= C_FingerPrintInfo.load(C_FingerPrintInfo.class,a);
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




                    tpi=ThirdPartyIntegration.load(ThirdPartyIntegration.class,a);

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

            Utils.LogInfo(getClass().getName(), ">>> INFO customer name " + i + " " + customer.getFirst_name());
        }

        // } catch (JSONException e) {
        // e.printStackTrace();
        // }
    }





    private TextWatcher FilerTextWatcher=new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {


            switch(filterTypespinner.getSelectedItem().toString()){

                case "Name":

                    new FilerRecycleView(view,s.toString()).execute();

                    break;

            }

        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private View generateActivityView(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_customer, null, false);

        return activityView;
    }


    void FloatingButton(View view){
         fab = (FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {



                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                   //Toast.makeText(CustomerActivity.this,"i was clicked",Toast.LENGTH_SHORT);
                    Intent intent = new Intent(CustomerActivity.this, OnBoardCustomerActivity.class);
                   // intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

                    overridePendingTransition( R.anim.activity_animation, R.anim.activity_animation2);

                    return true;
                }

              //  Toast.makeText(CustomerActivity.this,"i was clicked",Toast.LENGTH_SHORT);
                return true; // consume the event
            }
        });

    }
    void SetRecycleView(View view){

      //  fab.attachToListView(listView);
        RecyclerView.LayoutManager mlayout=new LinearLayoutManager(CustomerActivity.this,LinearLayoutManager.HORIZONTAL,false);
        recyclerView=(RecyclerView)view.findViewById(R.id.customrDisplay);


       // customerViewAdapter=new CustomerViewAdapter(CustomerActivity.this,Customer.getAllCustomers());
       // recyclerView.setAdapter(customerViewAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(CustomerActivity.this));

        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(CustomerActivity.this, recyclerView, new ClickListner() {
            @Override
            public void onClick(View view, int position) {
                TextView hiddenCustomerId = (TextView) view.findViewById(R.id.txtcustomerid);
                String hiddenId = hiddenCustomerId.getText().toString();
                ////
                // Toast.makeText(CustomerActivity.this, hiddenId, Toast.LENGTH_SHORT).show();
                CustomerDetailDialog dialog = CustomerDetailDialog.newInstance(hiddenId);
                //dialog.newInstance("","");
                showDialog(dialog);


                /**    RelativeLayout popmenu=(RelativeLayout)view.findViewById(R.id.popmenu);
                 popmenu.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(final View v) {
                PopupMenu popupMenu = new PopupMenu(CustomerActivity.this, v);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                case R.id.item_Deposit:

                Intent intent = new Intent(CustomerActivity.this, FieldCollectionActivity.class);
                finish();
                startActivity(intent);

                // Toast.makeText(CustomerActivity.this, "Comedy Clicked"+v.getTag(), Toast.LENGTH_SHORT).show();
                return true;
                case R.id.item_Withdrawal:
                Toast.makeText(CustomerActivity.this, "Movies Clicked", Toast.LENGTH_SHORT).show();
                return true;
                case R.id.item_Balance:
                Toast.makeText(CustomerActivity.this, "Music Clicked", Toast.LENGTH_SHORT).show();
                return true;
                }
                return true;
                }

                ;
                });
                popupMenu.inflate(R.menu.customer_quick_actions);
                popupMenu.show();
                }
                });
                 ***/


            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
        //fab.attachToRecyclerView(recyclerView);
    }

    public void showDialog(DialogFragment dialogFragment){

        FragmentManager manager = this.getSupportFragmentManager();
        // CashPaymentDialog dialog= new CashPaymentDialog();
        DialogFragment dialog = dialogFragment;
        dialog.show(manager, "");

    }

    private void showMerchantsDialog() {

        List<String> listItems = new ArrayList<String>();

        listItems.add("Account Number");
        listItems.add("Name");
        listItems.add("Phone Number");

        final CharSequence[] items =listItems.toArray(new CharSequence[listItems.size()]);
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Search By ");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // userInfoSettings.setCurrentMerchant(userInfoSettings.getMerchants().get(which));

                // String selectedItem= ((AlertDialog)dialog).getListView().getSelectedItem().toString();
                Log.d("oxinbo", "selected merchant " + items[which]);

                try {


                    String selectedMerchant = items[which].toString();
                    getFilterSpinner(selectedMerchant);

                } catch (Exception e) {


                }

                // setMerchant();
                // saveSettingsInstance(userInfoSettings, USerInfoSettings.class);
                builder.create().dismiss();
            }
        });
        AlertDialog createOptions = builder.create();
        createOptions.show();
    }

    private PopupMenu generateActionBarMenu(ImageView triggerImage) {
        PopupMenu popMenu = new PopupMenu(this, triggerImage);
        MenuInflater inflater = popMenu.getMenuInflater();
        inflater.inflate(R.menu.customer_quick_actions, popMenu.getMenu());

        popMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {


                return false;
            }
        });
        return popMenu;
    }

















    @Override
    public void onBackPressed() {
        Intent setIntent = new Intent(CustomerActivity.this, MainActivity.class);
        startActivity(setIntent);
        finish();
    }

    @Override
    public void CustomerDetailDialogInteraction() {

    }

    class LoadRecycleView extends AsyncTask<Void, Void, Void>{
        ProgressDialog pDialog ;
        View view;
        public LoadRecycleView(View view){

            this.view=view;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog  = new ProgressDialog(CustomerActivity.this);

            pDialog.setMessage("Fetching Customers...");
            pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            pDialog.setIndeterminate(true);
            pDialog.setCancelable(false);
            pDialog.show();
        }


        @Override
        protected Void doInBackground(Void... params) {
            customerViewAdapter=new CustomerViewAdapter(CustomerActivity.this,Customer.getAllCustomers());
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            recyclerView=(RecyclerView)view.findViewById(R.id.customrDisplay);
            recyclerView.setAdapter(customerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(CustomerActivity.this));
            pDialog.hide();


        }
    }
    class FilerRecycleView extends AsyncTask<Void, Void, Void>{
        View view;
     CustomerViewAdapter customerViewAdapter;
        String editable;
         public FilerRecycleView( View view,String s){

             this.view=view;
          this .editable=s;
             }
        @Override
        protected Void doInBackground(Void... params) {

            customerViewAdapter=new CustomerViewAdapter(CustomerActivity.this,Customer.getAllCustomersByName(editable));
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            recyclerView=(RecyclerView)view.findViewById(R.id.customrDisplay);
            recyclerView.setAdapter(customerViewAdapter);
            recyclerView.setLayoutManager(new LinearLayoutManager(CustomerActivity.this));
        }
    }
    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private GestureDetector gestureDetector;
        private ClickListner clickListner;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListner clickListner){

            this.clickListner=clickListner;

            gestureDetector= new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){

                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    View child= recyclerView.findChildViewUnder(e.getX(),e.getY());

                    if(child !=null & clickListner !=null){


                        clickListner.onClick(child, recyclerView.getChildPosition(child));

                    }

                    return super.onSingleTapUp(e);
                }

                @Override
                public void onLongPress(MotionEvent e) {



                    View child= recyclerView.findChildViewUnder(e.getX(),e.getY());

                    if(child !=null & clickListner !=null){


                        clickListner.onLongClick(child,recyclerView.getChildPosition(child));

                    }
                    // super.onLongPress(e);
                }
            });



        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child= rv.findChildViewUnder(e.getX(),e.getY());

            if(child !=null & clickListner !=null && gestureDetector.onTouchEvent(e) ){


                clickListner.onClick(child, rv.getChildPosition(child));

            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }
    public static  interface ClickListner{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }
}
