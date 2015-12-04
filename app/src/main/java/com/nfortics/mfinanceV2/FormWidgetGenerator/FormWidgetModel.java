package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.ViewFlipper;

import com.nfortics.mfinanceV2.Application.Application;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by bigfire on 10/29/2015.
 */
public  abstract class FormWidgetModel  extends Activity{

    Calendar calendar, c;
    public static String SCHEMA_KEY_META_STEP = "step";
    public static String SCHEMA_KEY_STEP = "step";
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
    public static String SCHEMA_KEY_TAG		= "tag";
    public static String SCHEMA_KEY_MULTI	= "mul";
    public static final LinearLayout.LayoutParams defaultLayoutParams =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);


    // -- data
    protected Map<String, FormWidget> _map;
    protected Map<Integer, FormWidget> StepedMap;
    Map<Integer, List<View>> mapDecused = new HashMap<Integer, List<View>>();
    Map<Integer, LinearLayout> mapLlayout = new HashMap<Integer,LinearLayout>();
    protected ArrayList<FormWidget> _widgets;
    FormWidget Dwidget;
    ViewFlipper viewFlipper;
    // -- widgets
    protected LinearLayout _container;
    protected LinearLayout _layout,_layout1;
    protected ScrollView _viewport;
    protected List<ScrollView>    _viewportList=new ArrayList<>();
    // -----------------------------------------------
    //
    // parse data and build view
    //
    // -----------------------------------------------
    ImageView imageView2;

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

            boolean toggles  = hasToggles(property);
            String defaultValue   = getDefault(property);


            Log.d("oxinbo","property build "+property);
            widget = getWidget(name, property);



            if( widget == null) return null;



            widget.setValue( defaultValue );


            if( property.has(SCHEMA_KEY_HINT)) widget.setHint( property.getString( SCHEMA_KEY_HINT ) );



            _widgets.add(widget);
            _map.put(name, widget);


            Log.d("oxinbo", "widget was created " + widget.getView());


        }catch (Exception e){

            e.printStackTrace();
            return null;}


        return  widget;
    }
    protected FormWidget getWidget( String name, JSONObject property ) {
        try
        {
            String type = property.getString(SCHEMA_KEY_TYPE);
            String tag = property.getString(SCHEMA_KEY_TAG);


            Log.d("oxinbo", "type = " + type);
            if( type.equals(SCHEMA_KEY_STRING ) ){
                return new FormEditText(this, name,tag );
            }



        } catch( JSONException e ) {
            // e.printStackTrace();
            return null;
        }
        return null;
    }
    public  LinearLayout generateForm(String data) {
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




            for (int i = 0; i < names.length(); i++)
                jsonValues.add(names.getString(i));
            Collections.sort(jsonValues);
            JSONArray sortedJsonArray = new JSONArray(jsonValues);
            names=sortedJsonArray;
            String nameIn;
            JSONObject propertyInner;
            //  schema1 = new JSONObject( data );
            JSONArray namesInner ;



            for(int i=0;i<=names.length();i++){

                name = names.getString(i);
                property = schema.getJSONObject(name);

                Log.d("oxinbo", "property =  " + property);


                schemaInner=new JSONObject(property.toString());
                namesInner=schemaInner.names();


                _layout = new LinearLayout( this );
                _layout.setOrientation(LinearLayout.VERTICAL);
                _layout.setLayoutParams(defaultLayoutParams);

                _viewport  = new ScrollView( this);
                _viewport.setLayoutParams(defaultLayoutParams);


                for(int x=0;x<namesInner.length();x++) {

                    nameIn = namesInner.getString( x );

                    propertyInner = schemaInner.getJSONObject(nameIn);
                    String Nma=propertyInner.getString(SCHEMA_KEY_TAG );
                    tags.add(Nma);

                    Log.d("oxinbo", "tag =" + Nma);

                    widget=buildFormWidget(nameIn);
                    Log.d("oxinbo", "widget =" + widget);
                    _widgets.add(widget);

                    _layout.addView(buildFormWidget(nameIn).getView());
                }

                _viewport.addView(_layout);

                _viewportList.add(_viewport);
                linearLayoutList.add(_layout);
                _layout.invalidate();

                viewFlipper.addView(_viewportList.get(i));

            }

        }catch (Exception e){

            e.printStackTrace();

        }


        _container = new LinearLayout( this);
        _container.setOrientation(LinearLayout.VERTICAL);

        _container.setLayoutParams(defaultLayoutParams);

        _container.addView(viewFlipper);


        return _container;


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
}
