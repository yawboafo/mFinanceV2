package com.nfortics.mfinanceV2.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.activeandroid.ActiveAndroid;
import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.Models.Branch;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Models.Product;
import com.nfortics.mfinanceV2.Models.Projects;
import com.nfortics.mfinanceV2.Models.ServiceProvider;
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Settings.AppInstanceID;
import com.nfortics.mfinanceV2.Settings.GeneralSettings;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.CalculatorBrain;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import roboguice.activity.GuiceActivity;
import roboguice.inject.InjectView;

public class LoginActivity extends GuiceActivity implements View.OnClickListener {

    @InjectView(R.id.buttonSignIn)
    private Button loginButton;
    @InjectView(R.id.butBiometric)
    private Button biometricButton;

    @InjectView(R.id.pinSignButton)
    private Button pinSignButton;

    @InjectView(R.id.edt_username)
    private EditText msisdn;


    @InjectView(R.id.edtPin)
    private EditText edtPin;


    @InjectView(R.id.viewflipper)
    private ViewFlipper viewflipper;


    @InjectView(R.id.rbutCancel)
    private Button rbutCancel;

    @InjectView(R.id.rbutSubmit)
    private Button rbutSubmit;

    @InjectView(R.id.redtSetPin)
    private EditText redtSetPin;

    @InjectView(R.id.txtSetPinTitle)
    private TextView txtSetPinTitle;

    @InjectView(R.id.txtAgentName)
    private TextView txtAgentName;
    @InjectView(R.id.edt_password)
    private EditText password;
    Utils utils=new Utils();
    Typefacer typefacer=new Typefacer();
    private VolleySingleton volleySingleton;
    private RequestQueue requestQueue;
    GeneralSettings generalSettings;



    private Boolean userIsInTheMiddleOfTypingANumber = false;
    private CalculatorBrain mCalculatorBrain;
    private static final String DIGITS = "0123456789.";


    Merchant merchant;
    User user;
    private String username,pin;

    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        generalSettings=GeneralSettings.getInstance();

        setLabels();
        InitializeApp();
        EventListeners();
        padLabels();

    }

    void resetFields(){

       //msisdn.setText("");
        password.setText("");
    }


    private void padLabels(){

        Button but0=(Button)findViewById(R.id.button0);
        but0.setOnClickListener(this);
        but0.setTypeface(typefacer.squareLight());

        Button but1=(Button)  findViewById(R.id.button1);
        but1.setOnClickListener(this);
        but1.setTypeface(typefacer.squareLight());


        Button but2=(Button)findViewById(R.id.activeButton);
        but2.setOnClickListener(this);
        but2.setTypeface(typefacer.squareLight());


        Button but3=(Button)findViewById(R.id.button3);
        but3.setOnClickListener(this);
        but3.setOnClickListener(this);
        but3.setTypeface(typefacer.squareLight());



        Button but4=(Button) findViewById(R.id.button4);
        but4.setOnClickListener(this);
        but4.setOnClickListener(this);
        but4.setTypeface(typefacer.squareLight());





        Button but5=(Button)findViewById(R.id.button5);
        but5.setOnClickListener(this);
        but5.setOnClickListener(this);
        but5.setTypeface(typefacer.squareLight());





        Button but6=(Button)findViewById(R.id.button6);
        but6.setOnClickListener(this);
        but6.setOnClickListener(this);
        but6.setTypeface(typefacer.squareLight());




        Button but7=(Button) findViewById(R.id.button7);
        but7.setOnClickListener(this);
        but7.setOnClickListener(this);
        but7.setTypeface(typefacer.squareLight());


        Button but8=(Button) findViewById(R.id.button8);
        but8.setOnClickListener(this);
        but8.setOnClickListener(this);
        but8.setTypeface(typefacer.squareLight());


        Button but9=(Button)  findViewById(R.id.button9);
        but9.setOnClickListener(this);
        but9.setOnClickListener(this);
        but9.setTypeface(typefacer.squareLight());


        Button forget=(Button)   findViewById(R.id.buttonForget);
        forget.setOnClickListener(this);
        forget.setOnClickListener(this);
        forget.setTypeface(typefacer.squareRegular());



        ImageButton clear=(ImageButton) findViewById(R.id.buttonClear);
        clear.setOnClickListener(this);
        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLastCharacter();
            }
        });
        //clear.setTypeface(typefacer.squareLight());




    }
    private void setLabels(){


       // viewflipper.setDisplayedChild(1);

         user=User.load(User.class,1);

        if(user==null){
            viewflipper.setDisplayedChild(0);

        }else{

            getWindow().setSoftInputMode(
                    WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN
            );

            //disable whatever
            txtAgentName.setText(""+user.getFirstName());
            txtAgentName.setTypeface(typefacer.squareLight());
            edtPin.setTypeface(typefacer.squareLight());
            edtPin.addTextChangedListener(mTextEditorWatcher);
            edtPin.setShowSoftInputOnFocus(false);
            edtPin.setCursorVisible(false);
            edtPin.setFocusableInTouchMode(false);
            edtPin.setFocusable(false);

            viewflipper.setDisplayedChild(2);
           // View view = this.getCurrentFocus();


        }
        coordinatorLayout = (CoordinatorLayout) findViewById(R.id
                .cordinateLayout);

        txtSetPinTitle.setTypeface(typefacer.getRoboCondensedRegular(getAssets()));
    }
    private void InitializeApp(){

        if (!Utils.deviceIsPhone()) {
            biometricButton.setVisibility(View.VISIBLE);
        }
    }


    void deleteLastCharacter(){


        String s = edtPin.getText().toString();
        if(!s.isEmpty()){
            s = s.substring(0, s.length() - 1);
            edtPin.setText(s);

        }


    }

    private void EventListeners(){
        loginButtonClickListener();
        rbutSubmitClickListener();
        rbutCancelClickListener();
        pinSignButtonClickListener();
}
    //Buttons EventListeners
    void loginButtonClickListener(){

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = msisdn.getText().toString();
                pin = password.getText().toString();

                if (username.length() <= 0 || pin.length() <= 0) {

                    utils.ErrorAlertDialog(LoginActivity.this, "Enter Username & Password", "Empty Fields").show();
                } else {
                    PerformLogin(username, pin, AppInstanceID.getDeviceId(LoginActivity.this));

                }


                //startActivity(new Intent(LoginActivity.this, MainActivity.class));
            }
        });
    }
    void rbutSubmitClickListener(){
        rbutSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(username.length()<=0 || redtSetPin.getText().toString().length()<=0){
                    utils.ErrorAlertDialog(LoginActivity.this,"Pin Cannot be Empty", "Empty Fields").show();

                }else{

                    ResetPin(username, redtSetPin.getText().toString());
                }


            }
        });


    }
    void rbutCancelClickListener(){

        rbutCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewflipper.setDisplayedChild(0);
            }
        });

    }
    void pinSignButtonClickListener(){

        pinSignButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String hiddedPin=edtPin.getText().toString();

                if(hiddedPin.length()<=0){



                }else{

                    User newUser=User.aUser(user.getMsisdn(),hiddedPin);

                    if(newUser==null){



                    utils.ErrorAlertDialog(LoginActivity.this,"Pin Does Not Match "+user.getMsisdn()+"' s Pin ","Login Failed ").show();
                    } else {

                        Application.setActiveAgent(newUser);

                        Intent myIntent=new Intent(LoginActivity.this,MainActivity.class);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                        startActivity(myIntent);

                        finish();
                    }
                }



            }
        });
    }

    private void updateUserPin(String pin){

        User user=User.aUser(Application.getActiveAgentMsisdn());
        user.setPin(pin);
        user.save();



        merchant=Merchant.getAllMerchantByAgentMsisdn(user.getMsisdn()).get(0);
        if(generalSettings.getActivemerchant()==null){

            generalSettings.setActivemerchant(merchant);
        }




                Log.d("oxinbo","user ID "+user.getId());

    }
    private static final String BASE_URL = Application.serverURL + "sessions.json?";
    private static final String RESET_BASE_URL = Application.serverURL + "agents/pin.json?";
    private void ResetPin(final String username, final String password){


        final ProgressDialog pDialog  = new ProgressDialog(this);
        pDialog.setMessage("Please hold on ...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        pDialog.show();


        final HashMap<String, String> params = new HashMap<String, String>();


        params.put("agent_msisdn", username);
        params.put("pin", password);



        volleySingleton= VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                RESET_BASE_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    //Log.d("Params",params+"");
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        try {

                            if(response!=null){

                                String status=response.getString("status");
                                Log.d("oxinbo", "response Status =" + status);


                                if(status.equals("000")){

                                    viewflipper.setDisplayedChild(0);

                                    //startActivity(new Intent(LoginActivity.this, MainActivity.class));

                                }else  if(status.equals("202")){


                                    viewflipper.setDisplayedChild(1);

                                }else {
                                    String message=response.getString("message");
                                    Log.d("oxinbo", "response message =" + message);
                                    utils.ErrorAlertDialog(LoginActivity.this,message,"Login Failed ").show();

                                }


                            }

                            Log.d("oxinbo", "response from server =" + response.toString());


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


                        utils.ErrorAlertDialog(LoginActivity.this,"No Internet Connection ,Login In Timed Out","Connectivity Error").show();



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
                            utils.ErrorAlertDialog(LoginActivity.this,errorMessage,"AuthFailureError").show();
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
                            utils.ErrorAlertDialog(LoginActivity.this, errorMessage, "NetworkError").show();
                        }

                    } else if (error instanceof ParseError) {
                        Log.d("oxinbo", "ParseError..");
                    }

                }catch (Exception e){

                    utils.RetryAlertDialog(LoginActivity.this,"A serve Error Occurred,Report Error ?","Unexpected Error").show();

                    e.printStackTrace();
                }









            }
        }) {

        };
        /*** int socketTimeout = 240000000;//4 minutes - change to what you want
         RetryPolicy policy = new DefaultRetryPolicy(
         socketTimeout,
         DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
         DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
         request.setRetryPolicy(policy);***/
        requestQueue.add(request);


        Log.d("oxinbo", "Server params" + params.toString());
    }
    private void PerformLogin(final String username, final String password,String IME){


        final ProgressDialog pDialog  = new ProgressDialog(this);
        pDialog.setMessage("Please hold on ...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        pDialog.show();


        final HashMap<String, String> params = new HashMap<String, String>();


        params.put("agent_msisdn", username);
        params.put("pin", password);
        params.put("device_id", IME);


        volleySingleton= VolleySingleton.getsInstance();
        requestQueue=VolleySingleton.getRequestQueue();

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST,
                BASE_URL,
                new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    //Log.d("Params",params+"");
                    @Override
                    public void onResponse(JSONObject response) {
                        pDialog.hide();
                        try {

                            if(response!=null){

                                String status = response.getString("status");
                                Log.d("oxinbo", "response Status =" + status);


                                if(status.equals("000")){



                                    try{
                                        parseJsonObject(response);


                                    }catch(Exception e){

                                        e.printStackTrace();
                                    }


                                    Intent myIntent=new Intent(LoginActivity.this,MainActivity.class);
                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                                    startActivity(myIntent);
                                    Application.setActiveAgent(User.aUser(username));
                                     Application.setActiveAgentMsisdn(username);
                                    updateUserPin(password);
                                    finish();

                                }else  if(status.equals("202")){

                                    resetFields();
                                    viewflipper.setDisplayedChild(1);

                                }else {
                                    String message=response.getString("message");
                                    Log.d("oxinbo", "response message =" + message);
                                    utils.ErrorAlertDialog(LoginActivity.this,message,"Login Failed ").show();

                                }


                            }

                            Log.d("oxinbo", "response from server =" + response.toString());


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
                    if (error instanceof TimeoutError  ) {
                        Log.d("oxinbo", "NoConnectionError.....TimeoutError..");


                        utils.ErrorAlertDialog(LoginActivity.this,"Login  Timed Out","Connectivity Error").show();



                    } else if(error instanceof NoConnectionError){


                        utils.ErrorAlertDialog(LoginActivity.this,"No Internet Connection ","Connectivity Error").show();
                    }


                    else if (error instanceof AuthFailureError) {
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
                            utils.ErrorAlertDialog(LoginActivity.this,errorMessage,"AuthFailureError").show();
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
                            utils.ErrorAlertDialog(LoginActivity.this, errorMessage, "NetworkError").show();
                        }

                    } else if (error instanceof ParseError) {
                        Log.d("oxinbo", "ParseError..");
                    }

                }catch (Exception e){

                    utils.RetryAlertDialog(LoginActivity.this,"A serve Error Occurred,Report Error ?","Unexpected Error").show();

                    e.printStackTrace();
                }









            }
        }) {

        };
        /*** int socketTimeout = 240000000;//4 minutes - change to what you want
         RetryPolicy policy = new DefaultRetryPolicy(
         socketTimeout,
         DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
         DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
         request.setRetryPolicy(policy);***/
        requestQueue.add(request);


        Log.d("oxinbo", "Server params" + params.toString());
    }
    private void parseJsonObject(JSONObject jsonObject) {

        ArrayList<Branch> branches = new ArrayList<Branch>();
        Merchant  merchant=new Merchant();
        ArrayList<ServiceProvider> serviceProviders = new ArrayList<ServiceProvider>();
        ArrayList<Product> products = new ArrayList<Product>();
        ArrayList<Projects> projectsAvialable = new ArrayList<Projects>();
        User _user=new User();
        Product product;
        ServiceProvider serviceProvider ;
        Projects projects;
        Branch  branch ;

        String Merchantcode,A_msisdn;

        try {


            //User _user =new User();

            A_msisdn=jsonObject.getString("agent_msisdn");

            _user=User.aUser(A_msisdn);

            if(_user==null){

                _user=new User();
            }


            _user.setFirstName(jsonObject.getString("agent_name"));
            _user.setMsisdn(A_msisdn);

            Log.d(getClass().getName(), ">>> INFO agent name = " + _user.getFirstName());
            Log.d(getClass().getName(), ">>> INFO agent msisdn = " + _user.getMsisdn());

            JSONArray newAccountsArray = jsonObject.getJSONArray("new_accounts");

            //userInfoSettings.setMerchants(new ArrayList<Merchant>());



            Log.d("oxinbo","merchant lenght "+newAccountsArray.length());
            for (int i = 1; i <=newAccountsArray.length(); i++) {
                JSONObject accountObject = newAccountsArray.getJSONObject(i-1);
                JSONObject accountJSONObject = accountObject.getJSONObject("new_account");


                String name=accountJSONObject.getString("name");
                Merchantcode=accountJSONObject.getString("code");




                  merchant =Merchant.getMerchant(name,Merchantcode,A_msisdn);
                if(merchant==null){

                    merchant=new Merchant();
                }


                merchant.setUserID(""+1);
                merchant.setName(name);
                merchant.setIsActive("false");
                merchant.setAgent_msisdn(A_msisdn);
                merchant.setCode(Merchantcode);
                merchant.setAmount_limit((Double.parseDouble(accountJSONObject.getString("collection_limit"))));
                merchant.setCurrency((accountJSONObject.getString("currency")));
                merchant.setLogo_url(((accountJSONObject.getString("logo_url"))));
                merchant.setDepositMsgTemplate(((accountJSONObject.getString("deposit_msg_template"))));
//                merchant.setSetReceipTemplate(((accountJSONObject.getString("receipt_template"))));
                JSONArray branchesArray = accountJSONObject.getJSONArray("branches");


                Log.d("oxinbo","merchant name = "+merchant.getName()+"merchant code = "+merchant.getCode());
                Log.d("oxinbo","branches length "+branchesArray.length());



                for (int k = 1; k <=branchesArray.length(); k++) {

                    JSONObject branchObject = branchesArray.getJSONObject(k-1);
                    JSONObject branchJSONObject = branchObject.getJSONObject("branch");

                    String bname=branchJSONObject.getString("name");
                    String bcode=branchJSONObject.getString("branch_code");





                      branch =  Branch.load(Branch.class,k);

                    if(branch==null){

                        branch=new Branch();
                    }


                    branch.setMerchant_code(Merchantcode);
                    branch.setName(bname);
                    branch.setBranchCode(bcode);
                    branch.setMain(branchJSONObject.getBoolean(("main")));

                    branches.add(branch);
                }
                JSONArray productArray = accountJSONObject.getJSONArray("products");


                for (int k = 1; k <=productArray.length(); k++) {

                    JSONObject productObject = productArray.getJSONObject(k-1);
                    JSONObject productJSONObject = productObject.getJSONObject("product");


                    String Pname=productJSONObject.getString("name");
                    String Pcode=productJSONObject.getString("product_code");




                     product =Product.load(Product.class, k);

                    if(product==null){

                        product=new Product();
                    }



                    product.setProductName(Pname);
                    product.setProductCode(Pcode);

                    products.add(product);
                }

                JSONArray serviceProvidersArray = accountJSONObject.getJSONArray("service_providers");


                for (int j = 1; j <=serviceProvidersArray.length(); j++) {

                    JSONObject serviceProvidersObject = serviceProvidersArray.getJSONObject(j-1);
                    JSONObject serviceProvidersJSONObject = serviceProvidersObject.getJSONObject("service_provider");


                    String Sname=serviceProvidersJSONObject.getString("name");
                    String Scode=serviceProvidersJSONObject.getString("code");




                    serviceProvider=ServiceProvider.load(ServiceProvider.class,j);
                    if(serviceProvider==null){

                        serviceProvider=new ServiceProvider();
                    }




                    serviceProvider.setName(Sname);
                    serviceProvider.setCode(Scode);

                    serviceProviders.add(serviceProvider);
                }


                JSONArray projectArray = accountJSONObject.getJSONArray("collection_projects");



                for (int j = 1; j <= projectArray.length(); j++) {

                    JSONObject serviceProvidersObject = projectArray.getJSONObject(j-1);
                    JSONObject serviceProvidersJSONObject = serviceProvidersObject.getJSONObject("collection_project");


                    String Cname= serviceProvidersJSONObject.getString("id");

                            String Ctitle=serviceProvidersJSONObject.getString("title");


                     projects =Projects.load(Projects.class,j);

                    if(projects==null){

                        projects=new Projects();
                    }



                    projects.setPid(Cname);
                    projects.setTitle(Ctitle);
                    projects.setReceipt_template(serviceProvidersJSONObject.getString("receipt_template"));

                    Log.d("oxinbo",projects.getReceipt_template());
                    projectsAvialable.add(projects);
                }

                ActiveAndroid.beginTransaction();

                try {
                    merchant.save();
                    _user.merchant_id=merchant.getId().toString();
                    _user.save();
                    Log.d("oxinbo","User Id "+_user.getId()+"");

                    for (Branch Brch : branches) {
                        Brch.merchant_id=merchant.getId().toString();
                        Brch.save();
                        Log.d("oxinbo","Branch ID = "+Brch.getId().toString());
                    }

                    for (ServiceProvider Serv : serviceProviders) {
                        Serv.merchant_id=merchant.getId().toString();
                        Serv.save();

                        Log.d("oxinbo", "ServiceProvider ID = " + Serv.getId().toString());
                    }

                    for (Product Prod : products) {
                        Prod.merchant_id=merchant.getId().toString();
                        Prod.save();
                        Log.d("oxinbo", "Product ID = " + Prod.getId().toString());
                    }

                    for(Projects Prjs:  projectsAvialable){

                        Prjs.merchant_id=merchant.getId().toString();
                        Prjs.save();
                        Log.d("oxinbo", "Projects ID = " + Prjs.getId().toString());
                    }



                    ActiveAndroid.setTransactionSuccessful();


                }finally {
                    ActiveAndroid.endTransaction();
                }




            }







        } catch (Exception e) {
            e.printStackTrace();
        }







    }



    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            //This sets a textview to the current length
            int i=s.length();

            if(i==4){

                User newUser=User.aUser(user.getMsisdn(),s.toString());

                if(newUser==null){

                    edtPin.setText("");
                  //  ToastUtil.showMessageToast("Pin Does Not Match " + user.getMsisdn() + "' s Pin ", false);
                    Snackbar snackbar = Snackbar
                            .make(coordinatorLayout, "Pin Does Not Match "+user.getMsisdn()+"' s Pin ", Snackbar.LENGTH_SHORT);

                    // Changing action button text color
                    View sbView = snackbar.getView();
                    TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
                    textView.setTextColor(Color.WHITE);
                    snackbar.show();
                   // utils.ErrorAlertDialog(LoginActivity.this,"Pin Does Not Match "+user.getMsisdn()+"' s Pin ","Login Failed ").show();
                } else {

                    Application.setActiveAgent(newUser);

                    Intent myIntent=new Intent(LoginActivity.this,MainActivity.class);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    myIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                    startActivity(myIntent);

                    finish();
                }
            }
        }

        public void afterTextChanged(Editable s) {
        }
    };

    @Override
    public void onClick(View v) {

        String buttonPressed = ((Button) v).getText().toString();


        int number=edtPin.getText().toString().length();


        if (DIGITS.contains(buttonPressed)) {

            // digit was pressed
            if (userIsInTheMiddleOfTypingANumber) {

                if (buttonPressed.equals(".") && edtPin.getText().toString().contains(".")) {
                    // ERROR PREVENTION
                    // Eliminate entering multiple decimals
                } else {
                    edtPin.append(buttonPressed);
                }

            } else {

                if (buttonPressed.equals(".")) {
                    // ERROR PREVENTION
                    // This will avoid error if only the decimal is hit before an operator, by placing a leading zero
                    // before the decimal
                    edtPin.setText(0 + buttonPressed);
                } else {
                    edtPin.setText(buttonPressed);
                }

                userIsInTheMiddleOfTypingANumber = true;
            }

        } else {

           if(buttonPressed.equals("DEL")){


            }
            if(buttonPressed.equals("Forget?")){

                viewflipper.setDisplayedChild(0);
            }


        }

    }
}
