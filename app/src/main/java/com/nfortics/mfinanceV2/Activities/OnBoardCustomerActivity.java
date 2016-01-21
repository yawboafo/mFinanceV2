package com.nfortics.mfinanceV2.Activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

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
import com.google.gson.Gson;
import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.DataBase.DaoAccess;
import com.nfortics.mfinanceV2.Dialogs.ListDialog;
import com.nfortics.mfinanceV2.Dialogs.ListDialog.ListDialogOnFragmentInteractionListener;
import com.nfortics.mfinanceV2.Dialogs.MaximumImageDialog;
import com.nfortics.mfinanceV2.Dialogs.ShowAccountDialog;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormDatePicker;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormEditNumber;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormEditPhone;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormEditText;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormEditTextInputLayout;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormFingerPrints;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormGender;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormImageCapture;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormMasterPictures;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormMultiCheckBox;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormPictures;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormSignature;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormSpinner;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormWidget;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormWidgetModel;
import com.nfortics.mfinanceV2.Handlers.GeneralRequestHandler;
import com.nfortics.mfinanceV2.MessageAlert;
import com.nfortics.mfinanceV2.Models.Account;
import com.nfortics.mfinanceV2.Models.Branch;
import com.nfortics.mfinanceV2.Models.C_Branch;
import com.nfortics.mfinanceV2.Models.C_FingerPrintInfo;
import com.nfortics.mfinanceV2.Models.Customer;
import com.nfortics.mfinanceV2.Models.FingerPrintInfo;
import com.nfortics.mfinanceV2.Models.ImageItem;
import com.nfortics.mfinanceV2.Models.Merchant;
import com.nfortics.mfinanceV2.Models.Msisdn;
import com.nfortics.mfinanceV2.Models.OnBoard;
import com.nfortics.mfinanceV2.Models.OnBoardModel;
import com.nfortics.mfinanceV2.Models.Product;
import com.nfortics.mfinanceV2.Models.Projects;
import com.nfortics.mfinanceV2.Models.ServiceProvider;
import com.nfortics.mfinanceV2.Models.ThirdPartyIntegration;
import com.nfortics.mfinanceV2.Models.User;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Request.FingerPrintTemplateRequest;
import com.nfortics.mfinanceV2.Request.HTTPRequest;
import com.nfortics.mfinanceV2.Request.UpdateCustomerRequest;
import com.nfortics.mfinanceV2.Services.CustomerSaveService;
import com.nfortics.mfinanceV2.Services.VolleyServices;
import com.nfortics.mfinanceV2.Settings.GeneralSettings;
import com.nfortics.mfinanceV2.Signature.SignaturePad;
import com.nfortics.mfinanceV2.Typefacer;
import com.nfortics.mfinanceV2.Utilities.ToastUtil;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.ViewAdapters.GalleryImagesAdapter;
import com.nfortics.mfinanceV2.ViewAdapters.GalleryImagesAdapterNew;
import com.nfortics.mfinanceV2.ViewAdapters.GridViewAdapter;
import com.nfortics.mfinanceV2.ViewAdapters.ListdialogAdapter;
import com.nfortics.mfinanceV2.Volley.MultipartRequest;
import com.nfortics.mfinanceV2.Volley.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ConnectException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import static com.nfortics.mfinanceV2.Dialogs.MaximumImageDialog.*;

public class OnBoardCustomerActivity
        extends BaseActivity implements
        ListDialogOnFragmentInteractionListener,
        MaximumDialogInteractionListener{


    Calendar calendar, c;

    public static String SCHEMA_KEY_PHONE = "phone";
    public static String SCHEMA_KEY_NUMBER   = "number";
    public static String SCHEMA_KEY_FP   =  "fingerPrint";
    public static String SCHEMA_KEY_META_STEP   = "step";
    public static String SCHEMA_KEY_GENDER      =    "gender";
    public static String SCHEMA_KEY_TYPE		= "type";
    public static String SCHEMA_KEY_BOOL 		= "boolean";
    public static String SCHEMA_KEY_INT  		= "integer";
    public static String SCHEMA_KEY_IMAGE  		= "image";
    public static String SCHEMA_KEY_DATE 		= "date";
    public static String SCHEMA_KEY_STRING 		= "string";
    public static String SCHEMA_KEY_PRIORITY	= "priority";
    public static String SCHEMA_KEY_TOGGLES		= "toggles";
    public static String SCHEMA_KEY_DEFAULT		= "default";
    public static String SCHEMA_KEY_MODIFIERS	= "modifiers";
    public static String SCHEMA_KEY_OPTIONS		= "options";
    public static String SCHEMA_KEY_META		= "meta";
    public static String SCHEMA_KEY_HINT		= "hint";
    public static String SCHEMA_KEY_SIGNATURE		= "signature";
    public static String SCHEMA_KEY_TAG		= "tag";
    public static String SCHEMA_KEY_DROP	= "dropdown";
    public static String SCHEMA_KEY_MULTI	= "multioptions";
    public static String SCHEMA_KEY_PICTURES	= "pictures";
    public static String SCHEMA_KEY_MASTERPICS ="masterPics";




    private static final String BASE_URL = Application.serverURL2 + "customers.json";
    Utils utils=new Utils();
    Typefacer typefacer=new Typefacer();

    public static final String FINGER_PRINT_URL = "http://197.159.131.154/crypto/biometric/enroll";



    public static final LinearLayout.LayoutParams defaultLayoutParams =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


    // -- data
    Map<String, List<FormWidget>> hashmap = new TreeMap<>();
    protected Map<String, FormWidget> _map;
    protected Map<Integer, FormWidget> StepedMap;
    Map<Integer, List<View>> mapDecused = new HashMap<Integer, List<View>>();
    Map<Integer, LinearLayout> mapLlayout = new HashMap<Integer,LinearLayout>();
    protected ArrayList<FormWidget> _widgets;
    List <FormWidget> view_widgets;
    FormWidget Dwidget;
    ViewFlipper viewFlipper;
    // -- widgets
    protected LinearLayout _container;
    protected LinearLayout _layout,_layout1;
    protected ScrollView _viewport;
    protected List<ScrollView>    _viewportList=new ArrayList<>();
    LayoutInflater inflator;
    // -----------------------------------------------
    //
    // parse data and build view
    //
    // -----------------------------------------------


    JSONObject property;
    JSONObject schema ;
    String JsonData;


    JSONObject propertyInner;
    JSONObject schemaInner ;
    String JsonDataInner;

    ArrayList<String >tags=new ArrayList<>();
    ArrayList<String>requiredWidget=new ArrayList<>();
    List<LinearLayout> linearLayoutList=new ArrayList<>();


    View view;
    LayoutInflater inflater;
    Button butt;
    ListView listMenu;
    RelativeLayout relativeLayout;
    GeneralSettings generalSettings;

    Customer customer = new Customer();
    RecyclerView galleryView;
    GalleryImagesAdapter galleryImagesAdapter;
    GalleryImagesAdapterNew galleryImagesAdapterNew;
    private GridView gridView;
    private GridViewAdapter gridAdapter;
    private ListdialogAdapter listdialogAdapter;

    public static FragmentManager fragmentManager;


    public static final int GALLERY_PICTURE_REQUEST = 1;
    public static final int CAMERA_PICTURE_REQUEST = 2;
    public static final int SIGNATURE_REQUEST = 3;
    public static final int CAPTURE_ID_DOCUMENT_REQUEST = 4;
    public static final int FINGER_PRINT_REQUEST= 5;

    public  int dipHeight;
    DisplayMetrics displaymetrics;
    int screenHeight;
    int screenWeight;
    int valueInDp;
    int counter;

    static Map<String,String>map=new TreeMap<>();
    Map<String,Map<String,String>> unscyncedMap=new TreeMap<>();

    String uniqueID = UUID.randomUUID().toString();

    String value;

    private List<String> errorMessages=new ArrayList<>();
    private int validateCounter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle intent=getIntent().getExtras();
        if(intent!=null){

             value=intent.getString("type");
        }

        displaymetrics=new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight=displaymetrics.heightPixels;
        screenWeight=displaymetrics.widthPixels;

         counter=  Application.getPicturesImageMap().size();
         view=generateActivityView();
        inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        relativeLayout=(RelativeLayout)findViewById(R.id.compound);

         dipHeight=relativeLayout.getHeight();

        Application.fragmentManager=this.getSupportFragmentManager();
         valueInDp= (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 440, getResources().getDisplayMetrics());


        getSupportActionBar().setTitle("");
        generalSettings =GeneralSettings.getInstance();
        relativeLayout.removeAllViews();

        //relativeLayout.addView(generateForm(FormWidgetModel.parseFileToString(OnBoardCustomerActivity.this, "schemas2.json")));
        BuildFormView(value);
        fragmentManager=OnBoardCustomerActivity.this.getSupportFragmentManager();

        addButton2layout();
        //relativeLayout.requestFocus();
       // Log.d("oxinbo","tag size  is == "+tags.size());

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
        // imageView2.setLayoutParams(layoutParams);
        Bitmap bitmap=null;
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PICTURE_REQUEST) {
                try {
                    Bundle extras = data.getExtras();


                    Bitmap imageBitmap = (Bitmap) extras.get("data");





                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();



                    Application.getGalleryImages().put(Application.activePictureLabel, imageBitmap);
                    Application. basse64Images.put(Application.activePictureLabel, getBase64Bytes(imageBitmap));

                    Application.listOfKeysChecked.add(Application.activePictureLabel);

                    galleryView =(RecyclerView)relativeLayout.findViewById(R.id.viewRecycle);

                    if(galleryView!=null){
                        RecyclerView.LayoutManager mlyout2=new GridLayoutManager(OnBoardCustomerActivity.this,3,GridLayoutManager.VERTICAL,false);
                        galleryImagesAdapterNew=new GalleryImagesAdapterNew(OnBoardCustomerActivity.this,Application.getGalleryImages());
                        galleryView.setAdapter(galleryImagesAdapterNew);
                        galleryView.setLayoutManager(mlyout2);

                        Log.d("oxinbo","i was calld recycleveiw");
                    }




                    Log.d("oxinbo", "onActivity Results was called here somewhere ");

                    // this.photo.setBackgroundResource(0);
                    // this.photo.setImageBitmap(bitmap);
                    // newCustomer.setPhotoUrl(getBase64Bytes(bitmap));

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // captureIdCard = false;
            } else if (requestCode == GALLERY_PICTURE_REQUEST) {
                try {
                    final Uri photoUri = data.getData();
                    bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);

                   // ImageView imageView2 =(ImageView)relativeLayout.findViewWithTag(generalSettings.getGetCurrentImageTag());
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();



                    Application.getGalleryImages().put(Application.activePictureLabel, bitmap);
                    Application. basse64Images.put(Application.activePictureLabel, getBase64Bytes(bitmap));
                    Application.listOfKeysChecked.add(Application.activePictureLabel);

                    galleryView =(RecyclerView)relativeLayout.findViewById(R.id.viewRecycle);

                    if(galleryView!=null){
                        RecyclerView.LayoutManager mlyout2=new GridLayoutManager(OnBoardCustomerActivity.this,3,GridLayoutManager.VERTICAL,false);
                        galleryImagesAdapterNew=new GalleryImagesAdapterNew(OnBoardCustomerActivity.this,Application.getGalleryImages());
                        galleryView.setAdapter(galleryImagesAdapterNew);
                        galleryView.setLayoutManager(mlyout2);

                        Log.d("oxinbo","i was calld recycleveiw");
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                // captureIdCard = false;
            } else if (requestCode == CAPTURE_ID_DOCUMENT_REQUEST) {
                try {
                    // final Uri photoUri = data.getData();
                    // bitmap = MediaStore.Images.Media.getBitmap(
                    // this.getContentResolver(), photoUri);
                    bitmap = (Bitmap) data.getExtras().get("data");
                    galleryView =(RecyclerView)relativeLayout.findViewById(R.id.viewRecycle);

                    if(galleryView!=null){
                        RecyclerView.LayoutManager mlyout2=new GridLayoutManager(OnBoardCustomerActivity.this,3,GridLayoutManager.VERTICAL,false);
                        galleryImagesAdapterNew=new GalleryImagesAdapterNew(OnBoardCustomerActivity.this,Application.getGalleryImages());
                        galleryView.setAdapter(galleryImagesAdapterNew);
                        galleryView.setLayoutManager(mlyout2);



                        Log.d("oxinbo","i was calld recycleveiw");
                    }
                    // if (captureIdCard) {
                    // this.captureIDCardImage.setBackgroundResource(0);
                    // this.captureIDCardImage.setImageBitmap(bitmap);
                    // } else {
                    // this.photo.setBackgroundResource(0);
                    // this.photo.setImageBitmap(bitmap);
                    // }
                    // newCustomer.setIdentificationPhotoUrl(getBase64Bytes(bitmap));
                } catch (Exception e) {
                    e.printStackTrace();
                }

                // captureIdCard = false;
            } else if (requestCode == SIGNATURE_REQUEST) {



                galleryView =(RecyclerView)relativeLayout.findViewById(R.id.viewRecycle);

                if(galleryView!=null){
                    RecyclerView.LayoutManager mlyout2=new GridLayoutManager(OnBoardCustomerActivity.this,3,GridLayoutManager.VERTICAL,false);
                    galleryImagesAdapterNew=new GalleryImagesAdapterNew(OnBoardCustomerActivity.this,Application.getGalleryImages());
                    galleryView.setAdapter(galleryImagesAdapterNew);
                    galleryView.setLayoutManager(mlyout2);



                    Log.d("oxinbo","i was calld recycleview");
                }

            }else if(requestCode == FINGER_PRINT_REQUEST){

                galleryView =(RecyclerView)relativeLayout.findViewById(R.id.viewRecycle);

                if(galleryView!=null){
                    RecyclerView.LayoutManager mlyout2=new GridLayoutManager(OnBoardCustomerActivity.this,3,GridLayoutManager.VERTICAL,false);
                    galleryImagesAdapterNew=new GalleryImagesAdapterNew(OnBoardCustomerActivity.this,Application.getGalleryImages());
                    galleryView.setAdapter(galleryImagesAdapterNew);
                    galleryView.setLayoutManager(mlyout2);


                }
            }


            else {
                Toast.makeText(this, "Sorry, your signature was not please try again", Toast.LENGTH_LONG).show();
            }

        } else if (resultCode == RESULT_CANCELED) {
            //  captureIdCard = false;
        } else {
            Toast.makeText(this, "Picture was not taken", Toast.LENGTH_SHORT);
        }
    }

    private View generateActivityView(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_on_board_customer, null, false);


        return activityView;
    }
    public  String generateTag(String type,String tag){

        String value="";
        switch (type){

            case "string":
                value= "edt_"+tag;
                break;
            case "date":
                value="dte_"+tag;
                break;
            case "dropdown":
                value="drp_"+tag;
                break;
            case "multioptions":
                value="mul_"+tag;
             break;
            case "gender":
                value="gen_"+tag;
                break;
            case "masterPics":
                value="mst_"+tag;
                break;

            case "number":
                value="num_"+tag;

                break;
            case "phone":
                value="phn_"+tag;
                break;
        }


        return value;

    }
    protected FormWidget getWidget( String name, JSONObject property ) {


                  try
                {



        String type = property.getString(SCHEMA_KEY_TYPE);
        String tag = property.getString(SCHEMA_KEY_TAG );
       // tags.add(generateTag(type,tag));


        if(type.equals(SCHEMA_KEY_MASTERPICS)){
            try{
                return new FormMasterPictures(this, name,property,generateTag(type,tag) );
            }catch (Exception e){
                e.printStackTrace();}
        }




        if( type.equals(SCHEMA_KEY_STRING ) ){
            try{

                return new FormEditTextInputLayout(this, name,generateTag(type,tag) );
            }catch (Exception e){
                e.printStackTrace();}
        }
                    if( type.equals(SCHEMA_KEY_NUMBER ) ){

                        try{
                            //return new FormEditTextInputLayout(this, name,tag );

                            return new FormEditNumber(this, name,generateTag(type,tag) );

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }


                    if( type.equals(SCHEMA_KEY_PHONE ) ){

                        try{
                            //return new FormEditTextInputLayout(this, name,tag );

                            return new FormEditPhone(this, name,generateTag(type,tag) );

                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }

        if( type.equals(SCHEMA_KEY_GENDER ) ){
            try{
                return new FormGender(this, name,generateTag(type,tag) );
            }catch (Exception e){

                e.printStackTrace();}

        }

        if( type.equals( SCHEMA_KEY_MULTI ) ){

            JSONObject toggleList = property.getJSONObject(SCHEMA_KEY_OPTIONS );
            JSONArray toggleNames = toggleList.names();

            List<String> stringList=new ArrayList<>();

            try{
                String tname;
                toggleNames = toggleList.names();
                for( int i = 0; i < toggleNames.length(); i++ )
                {
                    tname = toggleNames.getString(i);


                    String item = toggleList.getString(tname).toString();
                    //  item.toString().replaceAll("\"", "");
                 //   String b = item.toString().replace("[", "");
                  //  String c = b.toString().replace("]", "");
                   // String e=c.toString().replaceAll("\"", "");
                    stringList.add(item);

                }
            } catch( JSONException e ){
                Log.i("Lykaion", e.getMessage());
            }
            return new FormMultiCheckBox(  this, name, generateTag(type,tag),stringList.size(),stringList );
        }


                    if(type.equals(SCHEMA_KEY_DROP)){

                        try{
                            JSONObject options = property.getJSONObject(SCHEMA_KEY_OPTIONS );


                            Log.d("oxanbo","property names = "+name+"options = "+options);

                            return new FormSpinner(this, name,options,generateTag(type,tag) );


                        }catch (Exception e){e.printStackTrace();}

                    }

        if( type.equals(SCHEMA_KEY_DATE ) ){
            try{
            return new FormDatePicker(this, name,generateTag(type,tag) );
        }catch (Exception e){

            e.printStackTrace();
        }





    }



    } catch( Exception e ) {
        // e.printStackTrace();
        return null;
    }
    return null;
}
    public FormWidget buildFormWidget(String name){

        Log.d("oxinbo", "name from buildFormWidget = " + name);

        FormWidget widget=null;
        try{



            //  name = names.getString( i );

            //if( name.equals( SCHEMA_KEY_META )  ) continue;

            property = schemaInner.getJSONObject( name );
            String type = property.getString("type");
            String tag = property.getString("tag");
            String required = property.getString("required");
            int priority = property.getInt(SCHEMA_KEY_PRIORITY);
            boolean toggles  = hasToggles(property);
            String defaultValue   = getDefault(property);


            Log.d("oxinbo","property build "+property);
            widget = getWidget(name, property);
            widget.setPriority(priority);


            if( widget == null) return null;
            if( property.has(SCHEMA_KEY_HINT)) widget.setHint(property.getString(SCHEMA_KEY_HINT));


            // if(type.equals("string"))

            //  EditText editText=(EditText)widget.getView().findViewWithTag(tag);

            // widget.setTextWather(new floatingButton(widget.getView().findViewWithTag(generateTag(type,tag)),widget.getView().findViewWithTag(tag));
            if(type.equals("image")) widget.setImageClickListener(new ImageClickListener());
            if(type.equals("date")) widget.setDateButtonClickListener(new DatePicker(generateTag(type,tag)));
            if(type.equals("gender")) widget.setGenderCheckBoxClicklistener(new GenderCheckBoxClicklistener());
            //   if(type.equals("signature")) widget.setSignatureButtonClick(new SignatureClickListen());
            // if(type.equals("fingerPrint")) widget.setFingerPrinntClickListner(new FingerPrintRequestClickListener());
            if(type.equals(SCHEMA_KEY_MASTERPICS)){
                widget.setPicturesImageClickListener(new PicturesImageClickListener());
                if(!Application.deviceType.equalsIgnoreCase("phone")){
                    widget.setFingerPrinntClickListner(new FingerPrintRequestClickListener());}
                //   widget.setSignatureButtonClick(new SignatureClickListen());
                widget.setPicsSignatureClickListener(new PictruesSignatureClickListen());
            }

            if(property.has("required")){

                boolean req=false;

                if(required.isEmpty() )
                    req=false;
                else

                try{ req=Boolean.valueOf(required);}catch (Exception e){}


                widget.setRequired(req);

            }

            widget.setValue( defaultValue );



            _widgets.add(widget);
            _map.put( name, widget );


            Log.d("oxinbo", "widget was created " + widget.getView());


        }catch (Exception e){

            e.printStackTrace();
            return null;}


        return  widget;
    }
    public  void generateForm(String data) {
        String fv = "";
        int dvt = 0;
        _widgets = new ArrayList<FormWidget>();
        _map = new HashMap<String, FormWidget>();
        viewFlipper = new ViewFlipper(this);





        try
        {
            String name;
            FormWidget widget;
            JSONObject property;
            schema = new JSONObject( data );
            JSONArray names = schema.names();
            List<String> jsonValues = new ArrayList<String>();


            Log.d("oxinbo", "not sorted =" + names);
          for (int i = 0; i < names.length(); i++){
              jsonValues.add(names.getString(i));
          }
            Collections.sort(jsonValues);
           JSONArray sortedJsonArray = new JSONArray(jsonValues);
            names=sortedJsonArray;
            Log.d("oxinbo","not sortedJsonArray ="+names);


            //for (int i = 0; i < names.length(); i++)
            //  jsonValues.add(names.getString(i));
            // Collections.sort(jsonValues);
            // JSONArray sortedJsonArray = new JSONArray(jsonValues);
            // names=sortedJsonArray;
            String nameIn;
            JSONObject propertyInner;
            //  schema1 = new JSONObject( data );
            JSONArray namesInner;


            for (int i=0;i<=sortedJsonArray.length();i++){
                view_widgets=new LinkedList<>();
                name = sortedJsonArray.getString(i);
                property = schema.getJSONObject(name);

                Log.d("oxinbo", "property =  " + property+"name ="+name);


                schemaInner=new JSONObject(property.toString());
                namesInner=schemaInner.names();




                for(int x=0;x<namesInner.length();x++) {

                    nameIn = namesInner.getString(x);

                    propertyInner = schemaInner.getJSONObject(nameIn);
                   // String tagg=propertyInner.getString(SCHEMA_KEY_TAG );
                    //String type=propertyInner.getString(SCHEMA_KEY_TYPE );
                   // tags.add(Nma);



                    widget=buildFormWidget(nameIn);
                    tags.add(widget.getTag());
                    if(widget.Required())
                        requiredWidget.add(widget.getTag());
                    view_widgets.add(widget);

                }

                Collections.sort(view_widgets,new PriorityComparison());
                hashmap.put(name, view_widgets);


            }

        }catch (Exception e){

            e.printStackTrace();

        }






    }
    public void BuildFormView(String value){

        String data="";
        try{
            JSONObject schema1 = new JSONObject( FormWidgetModel.parseFileToString(OnBoardCustomerActivity.this, "schemas2.json") );
            JSONObject    pop = schema1.getJSONObject(value);
            data=pop.toString();
          //  Utils.log("Deadly Account ="+pop);
        }catch (Exception ee){


        }




        generateForm(data);
        inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);


        int counter=0;
       for(Map.Entry<String, List<FormWidget>> entry : hashmap.entrySet()) {
           String key = entry.getKey();
           _layout =(LinearLayout)inflator.inflate(R.layout.linear_layout_wrapper, null);
           TextView heading=(TextView)_layout.findViewById(R.id.heading);
           heading.setText(key);
           heading.setTypeface(typefacer.squareRegular());


           _viewport  = new ScrollView( this);
           _viewport.setLayoutParams(defaultLayoutParams);

           for(FormWidget val : entry.getValue()) {

          Log.d("oxanbo", "found one  tag" + val.getTag());

               if(val.getTag().equalsIgnoreCase("mst_masterPics")){


                   //TODO get screen size and set to layout height...
                   //So therefore instead of 1000,make it screen size -100dp
                 LinearLayout.LayoutParams par=new LinearLayout.LayoutParams(screenWeight,valueInDp);
                  // par.setMargins(30,10,10,30);
                   par.gravity=Gravity.CENTER;
                   _layout.addView(val.getView(),par);
               }else{
                   _layout.addView(val.getView());
               }

           }

           _viewport.addView(_layout);
           _viewportList.add(_viewport);
           linearLayoutList.add(_layout);
          // _layout.invalidate();


           viewFlipper.addView(_viewportList.get(counter));
           counter++;
       }
       _container = new LinearLayout( OnBoardCustomerActivity.this);
        _container.setOrientation(LinearLayout.VERTICAL);

        _container.setLayoutParams(defaultLayoutParams);

       _container.addView(viewFlipper);


        relativeLayout.addView(_container);
      //  relativeLayout.invalidate();

   }
    private void addButton2layout(){
        inflator = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Log.d("oxinbo","size of linear list "+linearLayoutList.size());

        try{
            if(linearLayoutList.size()<=1){
                RelativeLayout relativeLayout=(RelativeLayout)inflator.inflate(R.layout.bottom_button_single_layout, null);

                Button sbut=(Button)relativeLayout.findViewById(R.id.nextButton);
                sbut.setText("Save");
                sbut.setTypeface(typefacer.squareLight());
                sbut.setOnClickListener(new save());



                linearLayoutList.get(0).addView(relativeLayout);
            }else{





                for(int i=0;i<linearLayoutList.size();i++){

                    if(i==linearLayoutList.size()-1){



                        RelativeLayout relativeLayout=(RelativeLayout)inflator.inflate(R.layout.bottom_button_layout, null);

                        Button sbut=(Button)relativeLayout.findViewById(R.id.button_Save);
                        sbut.setTypeface(typefacer.squareLight());
                        sbut.setOnClickListener(new save());

                        ImageButton pbut=(ImageButton)relativeLayout.findViewById(R.id.button_previous);
                        // pbut.setTypeface(typefacer.squareLight());
                        pbut.setOnClickListener(new previous());



                        try{

                            linearLayoutList.get(linearLayoutList.size()-1).addView(relativeLayout);
                        }catch (Exception e){
                            e.printStackTrace();
                        }

                    }else if(i==0){


                        RelativeLayout relativeLayout=(RelativeLayout)inflator.inflate(R.layout.bottom_button_single_layout, null);

                        Button sbut=(Button)relativeLayout.findViewById(R.id.nextButton);
                        sbut.setTypeface(typefacer.squareLight());
                        int ii=Integer.valueOf(i);
                        int iii=ii+1;
                        sbut.setText("Next");
                        sbut.setOnClickListener(new next());



                        linearLayoutList.get(i).addView(relativeLayout);

                        // linearLayoutList.get(0).addView(relativeLayout);



                        // Button but =new Button(this);
                        // but.setText("Next  ( "+i+" Of "+linearLayoutList.size()+" )");
                        // but.setOnClickListener(new next());
                        // linearLayoutList.get(i).addView(but);

                    } else{




                        RelativeLayout relativeLayout=(RelativeLayout)inflator.inflate(R.layout.bottom_button_layout, null);


                        ImageButton pbut=(ImageButton)relativeLayout.findViewById(R.id.button_previous);
                        // pbut.setTypeface(typefacer.squareLight());
                        //  pbut.setText("Previous");
                        pbut.setOnClickListener(new previous());

                        Button sbut=(Button)relativeLayout.findViewById(R.id.button_Save);

                        int iz=Integer.valueOf(i);
                        int iiz=iz+2;

                        sbut.setText("Next");
                        sbut.setTypeface(typefacer.squareLight());
                        sbut.setOnClickListener(new next());






                        linearLayoutList.get(i).addView(relativeLayout);


                    }

                }

            }

        }catch (Exception e){

            e.printStackTrace();


        }


    }




    protected String getDefault( JSONObject obj ){
        try{
            return obj.getString(SCHEMA_KEY_DEFAULT );
        } catch ( JSONException e ){
            return null;
        }
    }
    protected boolean hasToggles( JSONObject obj ){
        try{
            obj.getJSONObject(SCHEMA_KEY_TOGGLES);
            return true;
        } catch ( JSONException e ){
            return false;
        }
    }
    private void multimediaDialogClickListerner(int selectedItemIndex, boolean isId) {
        switch (selectedItemIndex) {
            case 0:
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                if (isId)
                    startActivityForResult(cameraIntent, CAPTURE_ID_DOCUMENT_REQUEST);
                else
                    startActivityForResult(cameraIntent, CAMERA_PICTURE_REQUEST);
                break;

            case 1:
                // starting the Gallery Activity to select a picture
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);

                if (isId)
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), CAPTURE_ID_DOCUMENT_REQUEST);
                else
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), GALLERY_PICTURE_REQUEST);
                break;

            default:
                // startActivityForResult(videoCaptureIntent,
                // VIDEO_RECORDER_REQUEST);
                break;
        }
    }
    protected void validate(String tag){


        String subStringed1;

        // ClearMemory();
        validateCounter=0;
        errorMessages=new ArrayList<>();

        String subStringed="";
        try{
            subStringed = tag.substring(0, 3);}catch (Exception e){

        }


        if(subStringed.equals("hld")) {



            String trimmedTag="";
            String selectedValue="";
            try{
                trimmedTag = tag.substring(3, tag.length());
                Spinner spinner = (Spinner) relativeLayout.findViewWithTag(trimmedTag);
                selectedValue=spinner.getSelectedItem()+"";
            } catch (Exception e){

            }

            if(selectedValue.length()<=0){
                //  spinner


                validateCounter++;
                ToastUtil.showToast("spinner is empty",false);
            }else{
                validateCounter--;
                ToastUtil.showToast("not empty",true);
            }



        }


        if(subStringed.equals("num")) {
            EditText editText = (EditText) relativeLayout.findViewWithTag(tag);
            if(editText.getText().toString().length()<=0){
                editText.setError("Required ");

                validateCounter++;

                //   ToastUtil.showToast("Provide all required Fields",false);


            }else{
                validateCounter--;

            }


        }
        if(subStringed.equals("phn")) {
            EditText editText = (EditText) relativeLayout.findViewWithTag(tag);
            if(editText.getText().toString().length()<=0){
                editText.setError("Required ");

                validateCounter++;

            }else{

                validateCounter--;
            }


        }
        if (subStringed.equals("edt")) {
            EditText editText = (EditText) relativeLayout.findViewWithTag(tag);
            if(editText.getText().toString().length()<=0){
                editText.setError("Required ");

                validateCounter++;


                //TODO change errorMessagesType
            }else{

                validateCounter--;
            }
            //editText=null;

        }


        //detected dropdown
        /***  if (subStringed.equals("drp")) {
         Spinner spinner = (Spinner) relativeLayout.findViewWithTag(tag);
         String selectedValue=spinner.getSelectedItem()+"";
         try {

         if(selectedValue.length()<=0){
         //  spinner


         validateCounter++;
         ToastUtil.showToast("spinner is empty",false);
         }else{
         validateCounter--;
         ToastUtil.showToast("not empty",true);
         }

         } catch (Exception e) {
         e.printStackTrace();
         }



         }  ****/



        //detected date
        if(subStringed.equals("dte")){
            EditText tv=(EditText)viewFlipper.findViewWithTag(tag);
            if(tv.getText().toString().length()<=0){
                tv.setError("Required ");
                validateCounter++;


            }else{
                validateCounter--;

            }

        }



        //detected Images Gallery
        if(subStringed.equals("mst")){
            try{
                /***   if( Application.basse64Images!=null || Application.basse64Images.size()>0){
                 try {
                 for (Map.Entry<String, String >entry :Application.basse64Images.entrySet()) {
                 String key = entry.getKey();
                 String value= entry.getValue();

                 Application.getCollectionItems().put(key, value);}
                 } catch (Exception e) {
                 e.printStackTrace();
                 }
                 //if you uncomment below code, it will throw java.util.ConcurrentModificationException
                 //studentGrades.remove("Alan");
                 // }
                 //   Map<String,Bitmap> fingerPrintImages;

                 }****/

            }catch (Exception e){


            }

        }


        if (subStringed.equals("mul")) {
            List<String> check=new ArrayList<>();
            LinearLayout buttonLayout = (LinearLayout) relativeLayout.findViewWithTag(tag);
            for (int i = 0; i < ((ViewGroup) buttonLayout).getChildCount(); ++i) {
                View nextChild = ((ViewGroup) buttonLayout).getChildAt(i);
                if(nextChild instanceof CheckBox) {
                    CheckBox checkBox = (CheckBox) buttonLayout.findViewWithTag(nextChild.getTag());
                    if(checkBox.isChecked()){
                        check.add(checkBox.getTag().toString());
                    }
                }


            }

            try {



                if(check.size()<=0){
                    validateCounter++;

                }else{
                    validateCounter--;
                }



            } catch (Exception e) {
                e.printStackTrace();
            }


        }









    }

    private ArrayList<View> getAllChildren(View v) {

        if (!(v instanceof ViewGroup)) {
            ArrayList<View> viewArrayList = new ArrayList<View>();




            viewArrayList.add(v);
            return viewArrayList;
        }

        ArrayList<View> result = new ArrayList<View>();

        ViewGroup viewGroup = (ViewGroup) v;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {

            View child = viewGroup.getChildAt(i);

            ArrayList<View> viewArrayList = new ArrayList<View>();
            viewArrayList.add(v);
            ////if(v!=null)
               // Utils.log("" + v.getTag());

            viewArrayList.addAll(getAllChildren(child));

            result.addAll(viewArrayList);
        }
        return result;
    }

    class next implements View.OnClickListener {


        @Override
        public void onClick(View v) {

        // viewFlipper.getDisplayedChild();
           int  count=viewFlipper.getDisplayedChild();

          //  getAllChildren(linearLayoutList.get(0));


            for(String x:requiredWidget){

                Utils.log("required tag " +x);
            }

            for(View vi:getAllChildren(linearLayoutList.get(count))){

              if(vi.getTag()!=null){

                  if(requiredWidget.contains(vi.getTag())) validate(vi.getTag().toString());


                  Utils.log("position tag " + vi.getTag());
              }

            }
            Utils.log("validateCounter casted " + validateCounter);


            if(validateCounter>0){


            }else{

                viewFlipper.showNext();
            }

            // save();
        }
    }
    class previous implements View.OnClickListener {


        @Override
        public void onClick(View v) {

            Log.d("oxinbo", "saved was called from implemetation ");
            //anything();



            viewFlipper.showPrevious();
            // save();
        }
    }
    class save implements View.OnClickListener {


        @Override
        public void onClick(View v) {

            collectOnboardingData();


            // viewFlipper.getDisplayedChild();
            int  count=viewFlipper.getDisplayedChild();

            //  getAllChildren(linearLayoutList.get(0));


            for(View vi:getAllChildren(linearLayoutList.get(count))){

                if(vi.getTag()!=null){


                    if(requiredWidget.contains(vi.getTag()))
                        validate(vi.getTag().toString());
                    Utils.log("position tag " + vi.getTag());
                }
            }
            Utils.log("validateCounter casted " + validateCounter);



            if(validateCounter>0){



            }else{
                MultipartVolley();
                //ToastUtil.showToast("Several Erros  and Size"+errorMessages.size(),false);
            }



        }
    }

    public  void showBranchesDialog() {
        final CharSequence[] items = getErrorMessagesArray();
        final AlertDialog.Builder builder = new AlertDialog.Builder(OnBoardCustomerActivity.this);
        builder.setTitle("Empty Fields Detected ");
        builder.setItems(items, new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                // userInfoSettings.setCurrentMerchant(userInfoSettings.getMerchants().get(which));

                // String selectedItem= ((AlertDialog)dialog).getListView().getSelectedItem().toString();
                // Log.d("oxinbo","selected merchant "+items[which]);

                try{


                    String selectedMerchant=items[which].toString();



                    // DaoAccess.setActiveMerchant(selectedMerchant);

                }catch(Exception e){



                }

                // setMerchant();
                // saveSettingsInstance(userInfoSettings, USerInfoSettings.class);
                builder.create().dismiss();
            }
        });
        AlertDialog createOptions = builder.create();
        createOptions.show();
    }
    private CharSequence[] getErrorMessagesArray() {

        final List<String> merchantList =errorMessages;
        String[] object = new String[merchantList.size()];
        for (int i = 0; i < merchantList.size(); i++) {
            object[i] = merchantList.get(i);
            Log.d("oxinbo","object "+ object[i]);
        }

        return object;
    }
    private void ClearMemory(){


        Application. galleryImages=new HashMap<>();
        Application. fingerPrintImages=new HashMap<>();
        Application. signatureImages=new HashMap<>();
        Application.basse64Images=new HashMap<>();

        Application. CollectionItems=new HashMap<>();

        // Application. activeSignatureLabel;
        //  public static String activePictureLabel;

        Application. listOfKeysChecked=new ArrayList<>();
        //Application. SignatureLabelsList=new ArrayList<>();
        // Application. PictureLabelsList=new ArrayList<>();

        // Application. checkedImageLabels=new HashMap<>();
        // Application. SignatureMap=new ArrayList<>();
        //  Application. PicturesImageMap=new ArrayList<>();
        //  private static List<Map<String,Bitmap>> galleryImageList=new ArrayList<>();
        // private static List<Map<String,Bitmap>> fingerPrintList=new ArrayList<>();
        Application.otherImages= new HashMap<>();
        Application.setCollectionItems(new HashMap<String, String>());
    }
    private void CreateCustomer(String cusID,String fullname,Map<String,String> map,String synsts,String uniqueID){



        String first_name="";
        String last_name="";
        String other_names="";
        String gender="";
        String title="";
        String id_type="";
        String id_value="";
        String dob="";
        String address1="";
        String address2="";
        String address3="";
        String phone_number="";





        try{

            first_name= map.get("first_name").toString();
            last_name=map.get("last_name").toString();
            other_names=map.get("other_names").toString();
            gender=map.get("gender").toString();
            title=map.get("title").toString();
            id_type=map.get("id_type").toString();
            id_value=map.get("id_value").toString();
            dob=map.get("dob").toString();
            address1=map.get("address1").toString();
            address2=map.get("address2").toString();
            address3=map.get("address3").toString();
            phone_number=map.get("phone_number").toString();



        }catch (Exception e){}

        customer.setCustomer_id(cusID);


        customer.setFullname(fullname);
        customer.setFirst_name(first_name);
        customer.setSurname(last_name);
        customer.setOther_names(other_names);
        customer.setGender(gender);
        customer.setTitle(title);
        customer.setIdentificationType(id_type);
        customer.setIdentificationNumber(id_value);
        customer.setDob(dob);
        customer.setHouseNumber(address1);
        customer.setStreetName(address2);
        customer.setCity(address3);
        customer.setMobile_number(phone_number);
        customer.setSync_status(synsts);
        customer.setLocal_id(uniqueID);

        ActiveAndroid.beginTransaction();
        try{


          Long ID=  customer.save();

            Log.d("oxinbo","Inserted into DB = "+ID+" with cusID " +cusID);
            ActiveAndroid.setTransactionSuccessful();
        }finally {
            ActiveAndroid.endTransaction();
        }
    }

    private String getBase64Bytes(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        //  bitmap = Bitmap.createScaledBitmap(bitmap, 350, 350, true);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bao);

        byte[] ba = bao.toByteArray();

        return com.nfortics.mfinanceV2.Utilities.Base64.encodeBytes(ba);
    }




    public void collectOnboardingData() {

        String subStringed1;

        // ClearMemory();

        errorMessages=new ArrayList<>();
        for (String x:tags){
            String subStringed="";
            try{
                subStringed = x.substring(0, 3);}catch (Exception e){

            }


            if(subStringed.equals("num")) {
                EditText editText = (EditText) relativeLayout.findViewWithTag(x);
                if(editText.getText().toString().length()<=0){
                    editText.setError(editText.getHint());

                    try {
                        subStringed1 = x.substring(4, x.length());
                        errorMessages.add(subStringed1+"  is empty");
                    }catch (Exception e){


                    }


                    //   ToastUtil.showToast("Provide all required Fields",false);


                }else{
                    try{
                        subStringed1 = x.substring(4, x.length());
                        Application.getCollectionItems().put(subStringed1, editText.getText().toString());
                    }catch (Exception e){
                        e.printStackTrace();

                    }

                }


            }
            if(subStringed.equals("phn")) {
                EditText editText = (EditText) relativeLayout.findViewWithTag(x);
                if(editText.getText().toString().length()<=0){
                    editText.setError(editText.getHint());
                    try {
                        subStringed1 = x.substring(4, x.length());
                        errorMessages.add(subStringed1+"  is empty");
                    }catch (Exception e){


                    }


                }else{
                    try{
                        subStringed1 = x.substring(4, x.length());
                        Application.getCollectionItems().put(subStringed1, editText.getText().toString());
                    }catch (Exception e){
                        e.printStackTrace();

                    }

                }


            }
            if (subStringed.equals("edt")) {
                EditText editText = (EditText) relativeLayout.findViewWithTag(x);
                if(editText.getText().toString().length()<=0){
                    editText.setError(editText.getHint());


                    try{


                        subStringed1 = x.substring(4, x.length());
                        errorMessages.add(subStringed1+" is empty");

                    }catch (Exception e){


                    }


                    //TODO change errorMessagesType
                }else{
                    try{
                        subStringed1 = x.substring(4, x.length());
                        Application.getCollectionItems().put(subStringed1, editText.getText().toString());

                    }catch (Exception e){

                        e.printStackTrace();

                    }

                }
                //editText=null;

            }


            //detected dropdown
            if (subStringed.equals("drp")) {
                Spinner spinner = (Spinner) relativeLayout.findViewWithTag(x);
                String selectedValue=spinner.getSelectedItem()+"";
                try {
                    subStringed1 = x.substring(4, x.length());
                    if(selectedValue.isEmpty()){


                        errorMessages.add(subStringed1+"  not selected");
                    }else{
                        Application.getCollectionItems().put(subStringed1, selectedValue);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("oxinbo", "Spinner  :  " + x + " Value " + selectedValue);

            }



            //detected date
            if(subStringed.equals("dte")){
                EditText tv=(EditText)viewFlipper.findViewWithTag(x);
                if(tv.getText().toString().length()<=0){
                    tv.setError("This Field Cannot be Empty");
                    try{

                        subStringed1 = x.substring(4, x.length());

                        errorMessages.add( subStringed1+"  is empty");
                    }catch (Exception e){}

                }else{

                    try {
                        subStringed1 = x.substring(4, x.length());


                        Application.getCollectionItems().put(subStringed1, tv.getText().toString());

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("oxinbo", "Edit Text :  " + x + "  value " + tv.getText().toString());
                }

            }



            //detected Images Gallery
            if(subStringed.equals("mst")){
                try{
                    /***   if( Application.basse64Images!=null || Application.basse64Images.size()>0){
                     try {
                     for (Map.Entry<String, String >entry :Application.basse64Images.entrySet()) {
                     String key = entry.getKey();
                     String value= entry.getValue();

                     Application.getCollectionItems().put(key, value);}
                     } catch (Exception e) {
                     e.printStackTrace();
                     }
                     //if you uncomment below code, it will throw java.util.ConcurrentModificationException
                     //studentGrades.remove("Alan");
                     // }
                     //   Map<String,Bitmap> fingerPrintImages;

                     }****/

                }catch (Exception e){


                }

            }


            if (subStringed.equals("mul")) {
                List<String> check=new ArrayList<>();
                LinearLayout buttonLayout = (LinearLayout) relativeLayout.findViewWithTag(x);
                for (int i = 0; i < ((ViewGroup) buttonLayout).getChildCount(); ++i) {
                    View nextChild = ((ViewGroup) buttonLayout).getChildAt(i);
                    if(nextChild instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) buttonLayout.findViewWithTag(nextChild.getTag());
                        if(checkBox.isChecked()){
                            check.add(checkBox.getTag().toString());
                        }
                    }


                }

                try {

                    subStringed1 = x.substring(4, x.length());

                    if(check.size()<=0){
                        errorMessages.add( subStringed1+"  not selected ");

                    }else{
                        String json = new Gson().toJson(check );

                        String b = json.toString().replace("[", "");
                        String c = b.toString().replace("]", "");
                        String e=c.toString().replaceAll("\"", "");
                        Application.getCollectionItems().put(subStringed1, e);
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }


            }



        }






    }
    private void DownloadCustomerBackgroundS(String uid){

        StringBuffer urlBuilder = new StringBuffer();

        urlBuilder.append(Application.serverURL + "customers.json?");
        urlBuilder.append("agent_msisdn=");
        urlBuilder.append(User.User().getMsisdn());

        urlBuilder.append("&account_code=");
        urlBuilder.append(Merchant.getActiveMerchant("true").getCode());
        urlBuilder.append("&customer_uid=");
        urlBuilder.append(URLEncoder.encode(uid));

        /**  urlBuilder.append("&per_page=");
         urlBuilder.append("1");
         urlBuilder.append("&page=");
         urlBuilder.append(1);
         urlBuilder.append("&retrieve=");
         urlBuilder.append("true");**/


        Log.d("oxinbo", "" + urlBuilder.toString());
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET,
                urlBuilder.toString(),
                new Response.Listener<JSONObject>() {
                    //Log.d("Params",params+"");
                    @Override
                    public void onResponse(JSONObject response) {


                        try {
                            Log.d("oxinbo", "Responsee from heavyDutyVolley " + response);
                            if(response.getString("status").equals("000")){
                                parseJsonObject(response);

                            }else{


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

                    if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    } else if (error instanceof AuthFailureError) {

                    } else if (error instanceof ServerError) {
                        Log.d("oxinbo", "ServerError..");
                    } else if (error instanceof NetworkError) {

                    } else if (error instanceof ParseError) {
                        Log.d("oxinbo", "ParseError..");}
                }catch (Exception e){
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
        Application.requestQueue.add(request);






    }
    private void MultipartVolley(){

        final ProgressDialog pDialog  = new ProgressDialog(this);
        pDialog.setMessage("Please hold on ...");
        pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        pDialog.setIndeterminate(true);
        pDialog.setCancelable(true);
        pDialog.show();

      //  RequestQueue requestQueue = VolleySingleton.getInstance().getRequestQueue();
      //  volleySingleton= VolleySingleton.getsInstance();
       // requestQueue=VolleySingleton.getRequestQueue();
        Application.getCollectionItems().put("agent_msisdn", User.User().getMsisdn());
        Application.getCollectionItems().put("account_code", Merchant.getActiveMerchant("true").getCode());

        if(Application.basse64Images.size()>0){
            Application.getCollectionItems().put("complete", "false");
        }else{
            Application.getCollectionItems().put("complete", "true");
        }

                        MultipartRequest multipartRequest = new MultipartRequest(BASE_URL, Application.getCollectionItems(), new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       pDialog.dismiss();
                        if(response!=null){
                            Log.d("oxinbo", "response message from  =" + response);
                            String message="";
                            String fName="";
                            JSONObject jsonObject=null;
                            String status="";
                            try{
                                 jsonObject=new JSONObject(response);
                                  status= jsonObject.getString("status");



                                if(status.equals("000")){

                                            OnBoardModel onBoardModel=null;
                                            try {
                                                onBoardModel=Utils.MapsDb();
                                                Utils.log("Application.getCollectionItems().size  = "+Application.getCollectionItems().size());
                                                OnBoard onBoard=new OnBoard(uniqueID,Application.getCollectionItems(),Application.basse64Images);
                                                onBoardModel.getOnBoardMap().put(uniqueID, onBoard);
                                                Utils.insertIn2MapDb(onBoardModel);
                                                Utils.log("Size of map in OnBoardModel = "+onBoardModel.getOnBoardMap().size());
                                                onBoardModel=null;
                                            }catch (Exception e){

                                                e.printStackTrace();
                                            }

                                        fName = jsonObject.getString("fullname");
                                    Application.activeCustomerId=jsonObject.getString("customer_uid");
                                     message= "Customer Id :"+  Application.activeCustomerId+" Created";


                                    if(Application.basse64Images.size()>0){
                                        CreateCustomer(Application.activeCustomerId, fName, Application.getCollectionItems(), "partial",uniqueID);



                                       // combineMap(Application.activeCustomerId);
                                        Intent intent =new Intent(OnBoardCustomerActivity.this, VolleyServices.class);
                                        intent.putExtra("cusID", Application.activeCustomerId);
                                        intent.putExtra("actionType","heavy");
                                        startService(intent);

                                        Log.d("oxinbo","phone number"+ User.User().getMsisdn());


                                    }else{

                                        CreateCustomer(Application.activeCustomerId, fName, Application.getCollectionItems(),"complete",uniqueID);
                                    }

                                    Toast.makeText(OnBoardCustomerActivity.this,message,Toast.LENGTH_LONG).show();
                                    finish();





                                }else{

                                    String  message1="Error Message";
                                    try{
                                        message1 = jsonObject.getString("message");
                                        if(message1.equals("Error Message"))
                                          message1=  Utils.getMessageFromAPIStatus(Integer.valueOf(status));
                                    }catch (Exception e){


                                    }

                                    Log.d("oxinbo", "response message =" + Utils.getMessageFromAPIStatus(Integer.valueOf(status)));




                                  //  message=getMessageFromAPIStatus(status)
                                    utils.ErrorAlertDialog(OnBoardCustomerActivity.this,  Utils.getMessageFromAPIStatus(Integer.valueOf(status)), "New Error Message").show();

                                }


                            }catch (Exception e){





                                e.printStackTrace();
                            }



                        }
                    }
                }, new Response.ErrorListener() {

                    @Override
                    public void onErrorResponse(VolleyError error) {

                        pDialog.dismiss();
                        // Handle your error types accordingly.For Timeout & No connection error, you can show 'retry' button.
                        // For AuthFailure, you can re login with user credentials.
                        // For ClientError, 400 & 401, Errors happening on client side when sending api request.
                        // In this case you can check how client is forming the api and debug accordingly.
                        // For ServerError 5xx, you can do retry or handle accordingly.
                       // utils.RetryAlertDialog(OnBoardCustomerActivity.this, "error", "pls retry");

                      //  utils.ErrorAlertDialog(OnBoardCustomerActivity.this, message, " Failed").show();
                        if (error instanceof NetworkError) {
                            String msg="Network Error Occurred ";
                            RetryAlertDialog(msg);
                        } else if (error instanceof ServerError) {
                            String msg="Server Error Occurred ";
                            RetryAlertDialog(msg);
                        } else if (error instanceof AuthFailureError) {
                            String msg="Application Error Occurred ";
                            RetryAlertDialog(msg);

                        } else if (error instanceof ParseError) {


                            String msg="A Parser Error Occurred ";
                            RetryAlertDialog(msg);
                        } else if (error instanceof NoConnectionError) {


                            String msg="No Internet Connection ";
                            RetryAlertDialog(msg);

                        } else if (error instanceof TimeoutError) {
                            String msg="Syncing Timed Out ";
                            RetryAlertDialog(msg);
                        }
                    }
                });
        int socketTimeout = 999999999;//4 minutes - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(
                socketTimeout,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        multipartRequest.setRetryPolicy(policy);
       Application. requestQueue.add(multipartRequest);



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





    public void showDialog(DialogFragment dialogFragment){

        FragmentManager manager = this.getSupportFragmentManager();
        // CashPaymentDialog dialog= new CashPaymentDialog();
        DialogFragment dialog = dialogFragment;
        dialog.show(manager, "");

    }
    public void showPrompt1(){

        LayoutInflater li = LayoutInflater.from(OnBoardCustomerActivity.this);
        View promptsView = li.inflate(R.layout.simple_input_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OnBoardCustomerActivity.this);


        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        TextView tv=(TextView)promptsView.findViewById(R.id.textView1);
        tv.setText("Enter Picture Name");
        tv.setTypeface(typefacer.squareLight());


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text

                                Application.activePictureLabel=userInput.getText().toString();

                                final Item[] items = { new Item("Camera", R.drawable.cameraa), new Item("Gallery", R.drawable.gallery) };
                                new AlertDialog.Builder(OnBoardCustomerActivity.this).setTitle("Multimedia").setAdapter(prepareAdapter(items),
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int item) {
                                                multimediaDialogClickListerner(item, false);
                                            }
                                        }).show();

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();






    }
    public void showPrompt(){

        LayoutInflater li = LayoutInflater.from(OnBoardCustomerActivity.this);
        View promptsView = li.inflate(R.layout.simple_input_dialog, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(OnBoardCustomerActivity.this);


        // set prompts.xml to alertdialog builder
        alertDialogBuilder.setView(promptsView);

        final EditText userInput = (EditText) promptsView
                .findViewById(R.id.editTextDialogUserInput);
        TextView tv=(TextView)promptsView.findViewById(R.id.textView1);
        tv.setText("Enter Signature Name");
        tv.setTypeface(typefacer.squareLight());


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // get user input and set it to result
                                // edit text

                                Application.activeSignatureLabel=userInput.getText().toString();
                                //   result.setText(userInput.getText());


                                Intent intent = new Intent(OnBoardCustomerActivity.this, SignaturePad.class);
                                startActivityForResult(intent, SIGNATURE_REQUEST);

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();






    }
    private void Notify(String notificationTitle, String notificationMessage){
        NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        @SuppressWarnings("deprecation")

        Notification notification = new Notification(R.drawable.mfinance_logo_small,"mFinance "+Merchant.getActiveMerchant("true").getCode(), System.currentTimeMillis());
        Intent notificationIntent = new Intent(this,CustomerActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        notification.setLatestEventInfo(OnBoardCustomerActivity.this, notificationTitle,notificationMessage, pendingIntent);
        notificationManager.notify(9999, notification);
    }
    private void Notify2(String title,String message){

        Intent intent = new Intent(this, CustomerActivity.class);
// use System.currentTimeMillis() to have a unique ID for the pending intent
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), intent, 0);

        Notification n  = new Notification.Builder(this)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.mfinance_logo_small)
                .setContentIntent(pIntent)
                .setAutoCancel(true)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .addAction(R.drawable.ic_close, "Retry", pIntent)
                .addAction(R.drawable.ic_edit, "Clear", pIntent).build();

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        notificationManager.notify(0, n);



    }
    void RetryAlertDialog(String message){

        AlertDialog.Builder builder = new AlertDialog.Builder(OnBoardCustomerActivity.this);
        builder.setTitle("New Error Message !" );
        builder.setMessage("Message :\n" + message);



        builder.setPositiveButton("Retry", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                dialog.dismiss();
                MultipartVolley();
            }
        });
        builder.setNegativeButton("Sync Later", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

                try{
                    String Str = new String( Merchant.getActiveMerchant("true").getCode()+""+Application.getCollectionItems().get("phone_number"));
                    Application.activeCustomerId= Str.toUpperCase();
                }catch (Exception e){
                    e.printStackTrace();
                }


                OnBoardModel onBoardModel=null;
                try {
                    onBoardModel=Utils.MapsDb();
                }catch (Exception e){
                    e.printStackTrace();
                }


                try{
                    OnBoard onBoard=new OnBoard(uniqueID,Application.getCollectionItems(),Application.basse64Images);
                    onBoardModel.getOnBoardMap().put(uniqueID,onBoard);

                }catch (Exception e){


                }



                //Insert Into OnBoard Model......
                try {
                    Utils.insertIn2MapDb(onBoardModel);
                    onBoardModel=null;
                } catch (IOException e) {
                    e.printStackTrace();
                }

                CreateCustomer(Application.activeCustomerId, Utils.getFullName(Application.getCollectionItems()), Application.getCollectionItems(), "none", uniqueID);

                ClearMemory();
                // Customer.CreateCustomer(Str,);
                // CreateCustomer(Application.activeCustomerId, fName, Application.getCollectionItems(), "complete", uniqueID);
                // Utils.log("Size of map in OnBoardModel = " + onBoardModel.getOnBoardMap().size());
                //onBoardModel=null;



                finish();
                //    if(Application.basse64Images.size()>0){
                // combineMap(Application.activeCustomerId);
                // }








            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }




    public class PriorityComparison implements Comparator<FormWidget> {
        public int compare( FormWidget item1, FormWidget item2 ) {
            return item1.getPriority() > item2.getPriority() ? 1 : -1;
        }
    }
    public class floatingButton implements TextWatcher {


        EditText editText;
        TextView textView;

        private floatingButton(EditText editText,TextView textView) {


            this.editText= editText;
            this.textView= textView;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            editText.post(new Runnable() {
                @Override
                public void run() {
                    // validateName(editText,textView);
                }
            });

        }
    }
    public class DatePicker implements View.OnClickListener{

        private EditText textView;
        String tag;

        DatePicker(String tag){
            this.tag=tag;
            calendar = Calendar.getInstance();
            c = Calendar.getInstance();
            //this.textView=textView;
        }

        @Override
        public void onClick(View v) {
            DatePickerDialog dialog = new DatePickerDialog(OnBoardCustomerActivity.this, new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(android.widget.DatePicker view, int year, int month, int day) {

                    c.set(Calendar.YEAR, c.get(Calendar.YEAR) - 10);
                    c.set(Calendar.MONTH, month);
                    c.set(Calendar.DAY_OF_MONTH, day);
                    c.set(Calendar.YEAR, year);


                    Log.d("oxinbo","Tag from class "+tag);
                    EditText tv=(EditText)viewFlipper.findViewWithTag(tag);
                    tv.setText(Application.dateFormatDateOnlySlash.format(c.getTime()));




                }
            }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
            dialog.show();
        }
    }
    public class SignatureClickListen implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            generalSettings.setGetCurrentImageTag(v.getTag().toString());
            Intent intent = new Intent(OnBoardCustomerActivity.this, SignaturePad.class);
            startActivityForResult(intent, SIGNATURE_REQUEST);
        }
    }
    public class PictruesSignatureClickListen implements View.OnClickListener{

        @Override
        public void onClick(View v) {




            if(Application.SignatureLabelsList.size()<=0){

                showPrompt();
            }else{

                ListDialog dialog=ListDialog.newInstance("Select Signature type","signature");
                showDialog(dialog);
                ///  showsDialog();

            }

        }
    }
    public class ImageClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {


            try {
                generalSettings.setGetCurrentImageTag(v.getTag().toString());
                final Item[] items = { new Item("Camera", R.drawable.cameraa), new Item("Gallery", R.drawable.gallery) };
                new AlertDialog.Builder(OnBoardCustomerActivity.this).setTitle("Multimedia").setAdapter(prepareAdapter(items),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                multimediaDialogClickListerner(item, false);
                            }
                        }).show();





                //  dispatchTakePictureIntent();





            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Couldn't load photo", Toast.LENGTH_LONG).show();
            }


        }
    }
    public class PicturesImageClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {


            try {
               // generalSettings.setGetCurrentImageTag(v.getTag().toString());


                if(Application.PictureLabelsList.size()<=0){

                    showPrompt1();
                }else{


                    ListDialog dialog=ListDialog.newInstance("Select Picture Type","picture");
                    showDialog(dialog);
                  //  showsDialog1();

                }








                //  dispatchTakePictureIntent();





            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Couldn't load photo", Toast.LENGTH_LONG).show();
            }


        }
    }
    public class GenderCheckBoxClicklistener implements CompoundButton.OnCheckedChangeListener{

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {



            LinearLayout buttonLayout= (LinearLayout)relativeLayout.findViewWithTag("gender");
            for (int i = 0; i < ((ViewGroup) buttonLayout).getChildCount(); ++i) {
                View nextChild = ((ViewGroup) buttonLayout).getChildAt(i);

                if(nextChild instanceof CheckBox) {


                    if(buttonView.isChecked()){

                        ((CheckBox) nextChild).setChecked(false);
                    }else {

                        buttonView.setChecked(true);
                        ((CheckBox) nextChild).setChecked(false);
                    }





                }

                // Log.d("oxinbo", "Child Tags  " + nextChild.getTag());
            }


        }
    }
    public class FingerPrintRequestClickListener implements View.OnClickListener{


        @Override
        public void onClick(View v) {

            Intent intent = new Intent(OnBoardCustomerActivity.this, FingerPrintCaptureActivity.class);
            // Intent fingerprintIntent = new Intent(this,FingerPrintCaptureActivity.class);
            startActivityForResult(intent, FINGER_PRINT_REQUEST);
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
          //  recyclerView.addOnItemTouchListener(new recyclerTouchListener());


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
    public class TextWatcherO  implements TextWatcher {
        private void validateName(EditText editText){

            editText.requestFocus();

        }
        private EditText view;

        private TextWatcherO(EditText view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            validateName(view);
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {

            validateName(view);




        }
    }
    public ListAdapter prepareAdapter(final Item[] items) {
        ListAdapter adapter = new ArrayAdapter<Item>(this, android.R.layout.select_dialog_item, android.R.id.text1, items) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // User super class to create the View
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);

                // Put the image on the TextView
                tv.setCompoundDrawablesWithIntrinsicBounds(items[position].icon, 0, 0, 0);

                // Add margin between image and text (support various screen
                // densities)
                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };
        return adapter;
    }
    public static  interface ClickListner{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }
    public static class Item {
        public final String text;
        public final int icon;

        public Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }

        @Override
        public String toString() {
            return text;
        }
    }



    @Override
    public void maximumInteraction(String uri) {







        galleryView =(RecyclerView)relativeLayout.findViewById(R.id.viewRecycle);

        if(galleryView!=null){
            RecyclerView.LayoutManager mlyout2=new GridLayoutManager(OnBoardCustomerActivity.this,3,GridLayoutManager.VERTICAL,false);
            galleryImagesAdapterNew=new GalleryImagesAdapterNew(OnBoardCustomerActivity.this,Application.getGalleryImages());
            galleryView.setAdapter(galleryImagesAdapterNew);
            galleryView.setLayoutManager(mlyout2);

            Log.d("oxinbo", "i was calld recycleveiw");
        }
    }

    @Override
    public void ListDialogonFragmentInteraction(String uri) {


        switch (uri){

            case "signature":
                Intent intent = new Intent(OnBoardCustomerActivity.this, SignaturePad.class);
                startActivityForResult(intent, SIGNATURE_REQUEST);
                break;

            case "picture":

                final Item[] items = { new Item("Camera", R.drawable.cameraa), new Item("Gallery", R.drawable.gallery) };
                new AlertDialog.Builder(OnBoardCustomerActivity.this).setTitle("Multimedia").setAdapter(prepareAdapter(items),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int item) {
                                multimediaDialogClickListerner(item, false);
                            }
                        }).show();

                break;
        }

    }
}
