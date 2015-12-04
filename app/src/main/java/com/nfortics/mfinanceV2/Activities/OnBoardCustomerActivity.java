package com.nfortics.mfinanceV2.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
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

import com.nfortics.mfinanceV2.Application.Application;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormDatePicker;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormEditText;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormFingerPrints;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormGender;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormImageCapture;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormMultiCheckBox;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormPictures;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormSignature;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormSpinner;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormWidget;
import com.nfortics.mfinanceV2.FormWidgetGenerator.FormWidgetModel;
import com.nfortics.mfinanceV2.Models.FingerPrintInfo;
import com.nfortics.mfinanceV2.Models.ImageItem;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Settings.GeneralSettings;
import com.nfortics.mfinanceV2.Signature.SignaturePad;
import com.nfortics.mfinanceV2.Utilities.Utils;
import com.nfortics.mfinanceV2.ViewAdapters.GalleryImagesAdapter;
import com.nfortics.mfinanceV2.ViewAdapters.GridViewAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class OnBoardCustomerActivity   extends BaseActivity {
    View view;
    LayoutInflater inflater;
    Button butt;
    ListView listMenu;
    RelativeLayout relativeLayout;
    GeneralSettings generalSettings;


    RecyclerView galleryView;
    GalleryImagesAdapter galleryImagesAdapter;
    private GridView gridView;
    private GridViewAdapter gridAdapter;




    public static final int GALLERY_PICTURE_REQUEST = 1;
    public static final int CAMERA_PICTURE_REQUEST = 2;
    public static final int SIGNATURE_REQUEST = 3;
    public static final int CAPTURE_ID_DOCUMENT_REQUEST = 4;
    public static final int FINGER_PRINT_REQUEST= 5;


    private void multimediaDialogClickListerner(
            int selectedItemIndex,
            boolean isId) {
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


    private View generateActivityView(){

        LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View activityView = layoutInflater.inflate(R.layout.activity_on_board_customer, null, false);


        return activityView;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view=generateActivityView();
        inflator = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        relativeLayout=(RelativeLayout)findViewById(R.id.compound);
        getSupportActionBar().setTitle("");
        generalSettings =GeneralSettings.getInstance();
        relativeLayout.removeAllViews();

        //relativeLayout.addView(generateForm(FormWidgetModel.parseFileToString(OnBoardCustomerActivity.this, "schemas2.json")));
        InitilizeView();


        addButton2layout();

       // Log.d("oxinbo","tag size  is == "+tags.size());

    }

    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode,
                                    Intent data) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(200, 200);
        // imageView2.setLayoutParams(layoutParams);
        Bitmap bitmap=null;
        if (resultCode == RESULT_OK) {
            if (requestCode == CAMERA_PICTURE_REQUEST) {
                try {
                    Bundle extras = data.getExtras();


                    Bitmap imageBitmap = (Bitmap) extras.get("data");




                    ImageView imageView2 =(ImageView)relativeLayout.findViewWithTag(generalSettings.getGetCurrentImageTag());
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);


                    Bitmap scaledPhoto=Bitmap.createScaledBitmap(imageBitmap,100,100,true);
                    imageView2.getLayoutParams().height += 200;
                    imageView2.getLayoutParams().width += 200;
                    imageView2.setImageBitmap(scaledPhoto);

                    Log.d("oxinbo", "onActivity Results was called here somewhere ");
                    Log.d("oxinbo", "Tag of imageview " + imageView2.getTag());
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

                    ImageView imageView2 =(ImageView)relativeLayout.findViewWithTag(generalSettings.getGetCurrentImageTag());
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                    imageView2.getLayoutParams().height += 200;
                    imageView2.getLayoutParams().width += 200;
                    Bitmap scaledPhoto=Bitmap.createScaledBitmap(bitmap,100,100,true);

                    imageView2.setImageBitmap(scaledPhoto);


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
                    RecyclerView.LayoutManager mlyout2=new GridLayoutManager(OnBoardCustomerActivity.this,5,GridLayoutManager.HORIZONTAL,false);
                    galleryImagesAdapter=new GalleryImagesAdapter(OnBoardCustomerActivity.this,Application.getGalleryImageList());
                    galleryView.setAdapter(galleryImagesAdapter);
                    galleryView.setLayoutManager(mlyout2);

                    Log.d("oxinbo","i was calld recycleveiw");
                }
             /**   final String filePath = data.getExtras().get("filePath").toString();
                if (!filePath.equals("exception")) {

                    Uri photoUri = Uri.fromFile(new File(data.getExtras().get("filePath").toString()));
                    try {
                        bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), photoUri);
                        //this.signatureImage.setBackgroundResource(0);
                        // this.signatureImage.setImageBitmap(bitmap);
                        InputStream is;

                        // newCustomer.setSignatureUrl(getBase64Bytes(bitmap));
                        // userSettings.setSignatureBytes(ba1);
                        Bitmap scaledPhoto=Bitmap.createScaledBitmap(bitmap,100,100,true);
                       // ImageView imageView =(ImageView)relativeLayout.findViewWithTag(generalSettings.getGetCurrentImageTag());
                       // imageView.setImageBitmap(scaledPhoto);






                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }***/
            }else if(requestCode == FINGER_PRINT_REQUEST){
                //  Toast.makeText(this, "FINGER_PRINT_REQUEST", Toast.LENGTH_LONG).show();
                Button button=(Button)relativeLayout.findViewWithTag("fpt_fingerPrint");
                button.setText(generalSettings.getFingerPrints().size()+" Finger Prints   captured ");
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
    /***
     protected void onActivityResult(int requestCode, int resultCode, Intent data) {
     super.onActivityResult(requestCode, resultCode, data);

     //save();
     //ImageView imageView=(ImageView)findViewById(R.id.imaviewID);



     //FormImageView imv=new FormImageView(this,"").getInsatance();
     if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
     Bundle extras = data.getExtras();

     Bitmap imageBitmap = (Bitmap) extras.get("data");
     // String viewTag=extras.get("viewTag").toString();
     Log.d("oxinbo", "current image tag " + generalSettings.getGetCurrentImageTag());

     //item.getView().findViewWithTag("image").setImageBitmap(imageBitmap);

     ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
     imageBitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
     byte[] byteArray = byteArrayOutputStream .toByteArray();
     //  String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
     //  Log.d("oxinbo", "encoded " + encoded);



     //	imageView2= ( ImageView )(ImageView)getViewsByTag(viewFlipper,"Img_pics").get(0);

     ImageView imageView2 =(ImageView)relativeLayout.findViewWithTag(generalSettings.getGetCurrentImageTag());

     imageView2.setImageBitmap(imageBitmap);

     generalSettings.setGetCurrentImageTag(new String());

     }
















     //Bitmap bitmap=(Bitmap)data.getExtras().get("data");
     //_imageview.setImageBitmap(bitmap);

     //FormImageView imv=new FormImageView(this,"").getInsatance();
     //	imv._imageview.setImageBitmap(bitmap);

     Log.d("oxinbo", "onActivity Results was called here somewhere ");
     Log.d("oxinbo","onactivityresults");
     }
     ***/



    private ArrayList<ImageItem> getData() {
        final ArrayList<ImageItem> imageItems = new ArrayList<>();
        TypedArray imgs = getResources().obtainTypedArray(R.array.image_ids);
        for (int i = 0; i < imgs.length(); i++) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), imgs.getResourceId(i, -1));
            imageItems.add(new ImageItem(bitmap, "Image#" + i));
        }
        return imageItems;
    }

    //  **********************Form Generator ish here Views*********************** //
    Calendar calendar, c;

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
    public static String SCHEMA_KEY_MULTI	= "mul";
    public static String SCHEMA_KEY_PICTURES	= "pictures";


    //public static String SCHEMA_KEY_TOGGLES		= "toggles";
    public static final LinearLayout.LayoutParams defaultLayoutParams =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


    // -- data
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
    List<LinearLayout> linearLayoutList=new ArrayList<>();


    public FormWidget buildFormWidget(String name){

        Log.d("oxinbo","name from buildFormWidget = "+name);

        FormWidget widget=null;
        try{



            //  name = names.getString( i );

            //if( name.equals( SCHEMA_KEY_META )  ) continue;

            property = schemaInner.getJSONObject( name );
            String type = property.getString("type");
            String tag = property.getString("tag");
            int priority = property.getInt(SCHEMA_KEY_PRIORITY);
            boolean toggles  = hasToggles(property);
            String defaultValue   = getDefault(property);


            Log.d("oxinbo","property build "+property);
            widget = getWidget(name, property);
            widget.setPriority(priority);


            if( widget == null) return null;
            if( property.has(SCHEMA_KEY_HINT)) widget.setHint(property.getString(SCHEMA_KEY_HINT));
            if(type.equals("image")) widget.setImageClickListener(new ImageClickListener());
            if(type.equals("date")) widget.setDateButtonClickListener(new DatePicker(tag));
            if(type.equals("gender")) widget.setGenderCheckBoxClicklistener(new GenderCheckBoxClicklistener());
            if(type.equals("signature")) widget.setSignatureButtonClick(new SignatureClickListen());
            if(type.equals("fingerPrint")) widget.setFingerPrinntClickListner(new FingerPrintRequestClickListener());
            if(type.equals("pictures")){
                widget.setPicturesImageClickListener(new PicturesImageClickListener());
                widget.setSignatureButtonClick(new SignatureClickListen());
                widget.setPicsSignatureClickListener(new PictruesSignatureClickListen());
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

/***
    protected FormWidget getWidget( String name, JSONObject property ) {
        try
        {
            String type = property.getString( SCHEMA_KEY_TYPE );
            String tag = property.getString(SCHEMA_KEY_TAG );



            if( type.equals(SCHEMA_KEY_STRING ) ){
                return new FormEditText(this, name,tag );
            }
            if( type.equals(SCHEMA_KEY_GENDER ) ){
                return new FormGender(this, name,tag );
            }
            if( type.equals(SCHEMA_KEY_IMAGE ) ){
                return new FormImageCapture(this, name,tag );
            } if( type.equals(SCHEMA_KEY_DATE ) ){
            return new FormDatePicker(this, name,tag );
        }
            if( type.equals(SCHEMA_KEY_SIGNATURE ) ){
                return new FormSignature(this, name,tag );
            }
            if( type.equals(SCHEMA_KEY_FP ) ){
                return new FormFingerPrints(this, name,tag );
            }
            if(type.equals(SCHEMA_KEY_DROP)){
                JSONObject options = property.getJSONObject(SCHEMA_KEY_OPTIONS );
                return new FormSpinner(this, name,options,tag );
            }

            if()

        } catch( JSONException e ) {
            // e.printStackTrace();
            return null;
        }
        return null;
    }
***/

          protected FormWidget getWidget( String name, JSONObject property ) {
    try
    {
        String type = property.getString( SCHEMA_KEY_TYPE );
        String tag = property.getString(SCHEMA_KEY_TAG );




        if(type.equals(SCHEMA_KEY_PICTURES)){


            try{

                return new FormPictures(this, name,tag );

            }catch (Exception e){

              e.printStackTrace();

            }
        }


        if( type.equals(SCHEMA_KEY_STRING ) ){

            try{
                return new FormEditText(this, name,tag );

            }catch (Exception e){
                e.printStackTrace();
            }

        }

        if( type.equals(SCHEMA_KEY_GENDER ) ){




            try{
                return new FormGender(this, name,tag );
            }catch (Exception e){

                e.printStackTrace();
            }

        }

        if( type.equals( SCHEMA_KEY_MULTI ) ){

            JSONObject toggleList = property.getJSONObject(SCHEMA_KEY_TOGGLES );
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
                    String b = item.toString().replace("[", "");
                    String c = b.toString().replace("]", "");
                    String e=c.toString().replaceAll("\"", "");
                    stringList.add(e);

                }
            } catch( JSONException e ){
                Log.i("Lykaion", e.getMessage() );
            }












            //for(int i=0;i<toggleNames.length();i++){
            // stringList.add(toggleNames.getString());
            //  Log.d("oxinbo","oxinbo string list "+stringList.get(i));
            //}


            return new FormMultiCheckBox(  this, name, tag,stringList.size(),stringList );
            //  return new RealCheckBox( this, name ,tag,toggleNames.length(),stringList );

            //  Log.d("oxinbo","toggles  "+toggleNames.length());
        }






















        if( type.equals(SCHEMA_KEY_IMAGE ) ){


            try{

                return new FormImageCapture(this, name,tag );
            }catch (Exception e){

                e.printStackTrace();
            }


        } if( type.equals(SCHEMA_KEY_DATE ) ){


        try{
            return new FormDatePicker(this, name,tag );
        }catch (Exception e){

            e.printStackTrace();
        }





    }
        if( type.equals(SCHEMA_KEY_SIGNATURE ) ){

            try{

                return new FormSignature(this, name,tag );
            }catch (Exception e){e.printStackTrace();}

        }
        if( type.equals(SCHEMA_KEY_FP ) ){


            try{ return new FormFingerPrints(this, name,tag );}catch (Exception e){e.printStackTrace();}






        }
        if(type.equals(SCHEMA_KEY_DROP)){

            try{
                JSONObject options = property.getJSONObject(SCHEMA_KEY_OPTIONS );
                return new FormSpinner(this, name,options,tag );
            }catch (Exception e){e.printStackTrace();}

        }

    } catch( Exception e ) {
        // e.printStackTrace();
        return null;
    }
    return null;
}


             Map<String, List<FormWidget>> hashmap = new HashMap<>();

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




            //for (int i = 0; i < names.length(); i++)
            //  jsonValues.add(names.getString(i));
            // Collections.sort(jsonValues);
            // JSONArray sortedJsonArray = new JSONArray(jsonValues);
            // names=sortedJsonArray;
            String nameIn;
            JSONObject propertyInner;
            //  schema1 = new JSONObject( data );
            JSONArray namesInner;


            for (int i=0;i<=names.length();i++){
                view_widgets=new LinkedList<>();
                name = names.getString(i);
                property = schema.getJSONObject(name);

                Log.d("oxinbo", "property =  " + property+"name ="+name);


                schemaInner=new JSONObject(property.toString());
                namesInner=schemaInner.names();




                for(int x=0;x<namesInner.length();x++) {

                    nameIn = namesInner.getString(x);

                    propertyInner = schemaInner.getJSONObject(nameIn);
                    String Nma=propertyInner.getString(SCHEMA_KEY_TAG );
                    tags.add(Nma);



                    widget=buildFormWidget(nameIn);

                    view_widgets.add(widget);


                }

                Collections.sort(view_widgets,new PriorityComparison());
                hashmap.put(name, view_widgets);


            }

        }catch (Exception e){

            e.printStackTrace();

        }






    }

    void InitilizeView(){

        generateForm(FormWidgetModel.parseFileToString(OnBoardCustomerActivity.this, "schemas2.json"));








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


               _layout.addView(val.getView());
           }

           _viewport.addView(_layout);
           _viewportList.add(_viewport);
           linearLayoutList.add(_layout);
           _layout.invalidate();


           viewFlipper.addView(_viewportList.get(counter));
           counter++;
       }
       _container = new LinearLayout( OnBoardCustomerActivity.this);
       _container.setOrientation(LinearLayout.VERTICAL);

       _container.setLayoutParams(defaultLayoutParams);

       _container.addView(viewFlipper);



       relativeLayout.addView(_container);


   }











    class PriorityComparison implements Comparator<FormWidget>
    {
        public int compare( FormWidget item1, FormWidget item2 ) {
            return item1.getPriority() > item2.getPriority() ? 1 : -1;
        }
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

                        Button pbut=(Button)relativeLayout.findViewById(R.id.button_previous);
                        pbut.setTypeface(typefacer.squareLight());
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


                        Button pbut=(Button)relativeLayout.findViewById(R.id.button_previous);
                        pbut.setTypeface(typefacer.squareLight());
                        pbut.setText("Previous");
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
            obj.getJSONObject(SCHEMA_KEY_TOGGLES );
            return true;
        } catch ( JSONException e ){
            return false;
        }
    }
    public static String parseFileToString(Context context, String filename) {
        try
        {
            InputStream stream = context.getAssets().open( filename );
            int size = stream.available();

            byte[] bytes = new byte[size];
            stream.read(bytes);
            stream.close();

            return new String( bytes );

        } catch ( IOException e ) {
            Log.i("MakeMachine", "IOException: " + e.getMessage());
        }
        return null;
    }
    class next implements View.OnClickListener {


        @Override
        public void onClick(View v) {

            Log.d("oxinbo", "saved was called from implemetation ");
            //anything();



            viewFlipper.showNext();
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

        }
    }
    public void collectOnboardingData() {

        JSONObject obj = new JSONObject();
        for (String x:tags){
            String subStringed = x.substring(0, 3);

            //detected EditText
            if (subStringed.equals("edt")) {

                EditText editText = (EditText) relativeLayout.findViewWithTag(x);


                if(editText.getText().toString().length()<=0){

                    editText.setError("This Field Cannot be Empty");

                    // Drawable errorIcon = getResources().getDrawable(R.drawable.ic_error_warning);
                    //  errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
                    // editText.setError(null,errorIcon);
                }else{

                    try {
                        obj.put(x,editText.getText().toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    Log.d("oxinbo", "Edit Text :  " + x + "  value " + editText.getText().toString());
                }


            }

            //detected Spinner
            if (subStringed.equals("spn")) {

                Spinner spinner = (Spinner) relativeLayout.findViewWithTag(x);

                try {
                    obj.put(x,spinner.getSelectedItem().toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Log.d("oxinbo", "Spinner  :  " + x + " Value " + spinner.getSelectedItem().toString());

            }


            //detected Image
            if (subStringed.equals("img")) {

                ImageView imageview = (ImageView) relativeLayout.findViewWithTag(x);
                BitmapDrawable bitmapDrawable = ((BitmapDrawable) imageview.getDrawable());
                Bitmap bitmap = bitmapDrawable.getBitmap();
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
                byte[] imageInByte = stream.toByteArray();

                String encoded = Base64.encodeToString(imageInByte, Base64.DEFAULT);

                try {
                    //   obj.put(x,encoded);
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

            //detected Gender CheckBox
            if (subStringed.equals("pro")) {


                LinearLayout buttonLayout = (LinearLayout) relativeLayout.findViewWithTag(x);


                for (int i = 0; i < ((ViewGroup) buttonLayout).getChildCount(); ++i) {
                    View nextChild = ((ViewGroup) buttonLayout).getChildAt(i);

                    if(nextChild instanceof CheckBox) {
                        CheckBox checkBox = (CheckBox) buttonLayout.findViewWithTag(nextChild.getTag());

                        if(checkBox.isChecked()){


                            try {
                                obj.put(x, checkBox.getTag());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            Log.d("oxinbo", "Child Tags  " + checkBox.getTag());



                        }
                    }


                }




            }

            //detected finger prints
            if (subStringed.equals("fpt")) {

                Log.d("oxinbo","insisde getting finger prints = "+generalSettings.getFingerPrints().size());

                JSONArray list = new JSONArray();

                for(FingerPrintInfo fpi:generalSettings.getFingerPrints()){
                    String base64Value = Utils.getBase64Bytes(fpi.getBitmap());
                    String md5Hash = fpi.getAfisTemplate();
                    Log.d("oxinbo","getting Afis Template ="+ fpi.getAfisTemplate()+"  fpi index "+fpi.getIndex());

                    if (fpi.getIndex() == 0) {

                        JSONObject jsonObj= new JSONObject();
                        try {
                            jsonObj.put("right_thumb", base64Value);
                            jsonObj.put("right_thumb_id", md5Hash);
                            list.put(jsonObj);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }



                }
                try {
                    obj.put(x, list);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
        try {

            File file = new File("/sdcard/mysdfile.txt");
            file.createNewFile();
            FileOutputStream fOut = new FileOutputStream(file);

            OutputStreamWriter myOutWriter =
                    new OutputStreamWriter(fOut);
            myOutWriter.append(obj.toString());
            myOutWriter.close();
            fOut.close();



        } catch (Exception e) {
            e.printStackTrace();
        }

        Log.d("oxinbo", "object requested " + obj.toString());

    }
//********************************Form generator ends here ****************************************************//



    // *************************Private  Class functions Methods*****************************//
    public  void writeStringAsFile(final String fileContents, String fileName) {

        try {
            FileWriter out = new FileWriter(new File(getFilesDir(), fileName));
            out.write(fileContents);
            out.close();
        } catch (IOException e) {
            // Logger.logError(TAG, e);
        }
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
    /**
     * Creates and adds attributes to a ListAdapter with images & text.
     * @param items
     * an array of Items [image/text per Item] to be added to the returned adapter
     * @return a ListAdapter to be used as the adapter in an AlertDialog
     */
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

            Intent intent = new Intent(OnBoardCustomerActivity.this, SignaturePad.class);
            startActivityForResult(intent, SIGNATURE_REQUEST);
        }
    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {


            //  startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
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
}
