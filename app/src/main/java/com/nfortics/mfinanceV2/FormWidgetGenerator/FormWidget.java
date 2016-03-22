package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.nfortics.mfinanceV2.Activities.OnBoardCustomerActivity;
import com.nfortics.mfinanceV2.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by bigfire on 10/29/2015.
 */
public abstract class FormWidget  {

    protected View _view;
    protected String _tag;
    protected String 		_property;
    protected String    _step;
    protected String 		_displayText;
    protected int 	 		_priority;

    protected boolean 	 		_required;

    protected LinearLayout _layout;
    LayoutInflater inflator;
    protected HashMap<String, ArrayList<String>> _toggles;
    protected OnBoardCustomerActivity.ImageClickListener imageClickHandler;


    protected OnBoardCustomerActivity.TextWatcherO textwatcher;
    protected OnBoardCustomerActivity.PicturesImageClickListener PicimageClickHandler;
    protected OnBoardCustomerActivity.GenderCheckBoxClicklistener GenderCheckBoxHandler;
    protected  OnBoardCustomerActivity.DatePicker DateListener;
    protected  OnBoardCustomerActivity.SignatureClickListen SignatureClick;
    protected  OnBoardCustomerActivity.floatingButton floatingbutton;
    protected  OnBoardCustomerActivity.FingerPrintRequestClickListener fingerClick;
    protected OnBoardCustomerActivity.PictruesSignatureClickListen PicsSignatureClick;
//protected OnBoardCustomerActivity. supportFragmentManager;
    public static final LinearLayout.LayoutParams defaultLayoutParams =
            new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    // -- data
    protected Map<String, FormWidget> _map;
    private FragmentManager supportFragmentManager;


    public FormWidget( Context context,
                       String name,
                       String tag )
        {

        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        _layout=(LinearLayout)inflator.inflate(R.layout.relative_layout_style, null);
      //  defaultLayoutParams.setMargins(5,30,0,0);
      // _layout.setLayoutParams(defaultLayoutParams);
        //_layout.setOrientation(LinearLayout.VERTICAL);



        _tag              =tag;
        _property 		= name;
        _displayText 	= name.replace( "_", " ");
        _displayText 	= toTitleCase( _displayText );
    }

    public String toTitleCase( String s )
    {
        char[] chars = s.trim().toLowerCase().toCharArray();
        boolean found = false;

        for (int i=0; i<chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i])) {
                found = false;
            }
        }

        return String.valueOf(chars);
    }

    public ArrayList<String> getToggledOn()
    {
        if( _toggles == null ) return new ArrayList<String>();

        if( _toggles.get( getValue() ) != null ) {
            return _toggles.get( getValue() );
        } else {
            return new ArrayList<String>();
        }
    }

    /**
     * return list of widgets to toggle off
     * @param
     * @return
     */
    public ArrayList<String> getToggledOff()
    {
        ArrayList<String> result = new ArrayList<String>();
        if( _toggles == null ) return result;

        Set<String> set = _toggles.keySet();

        for (String key : set)
        {
            if( !key.equals( getValue() ) )
            {
                ArrayList<String> list = _toggles.get(key);
                if( list == null ) return new ArrayList<String>();
                for( int i = 0; i < list.size(); i++ ) {
                    result.add( list.get(i) );
                }
            }
        }

        return result;
    }


    public String getValue() {
        return "";
    }

    /**
     * sets value of this widget, method should be overridden in sub-class
     * @param value
     */
    public void setValue( String value ) {
        // -- override
    }

    // -----------------------------------------------
    //
    // modifiers
    //
    // -----------------------------------------------

    /**
     * sets the hint for the widget, method should be overriden in sub-class
     */
    public void setHint( String value ){
        // -- override
    }



    public void setRequired(boolean value){

        _required=value;
    }


    public boolean Required(){


        return _required;
    }


// -----------------------------------------------
    //
    // view
    //
    // -----------------------------------------------
    /**
     * return LinearLayout containing this widget's view elements
     */
    public View getView() {
        return _layout;
    }

    /**
     * toggles the visibility of this widget
     * @param value
     */
    public void setVisibility( int value ){
        _layout.setVisibility( value );
    }

    // -----------------------------------------------
    //
    // set / get value
    //
    // -----------------------------------------------


    public void setPriority( int value ) {
        _priority = value;
    }

    /**
     * returns visual priority
     * @return
     */
    public int getPriority() {
        return _priority;
    }


    public String getTag(){
        return _tag;
    }
    public String getDisplayText(){
        return _displayText;
    }
    public void setImage(Bitmap map){


    }

    public void setTextWatcher(OnBoardCustomerActivity.ImageClickListener handler){

        imageClickHandler=handler;
    }

    public void setImageClickListener(OnBoardCustomerActivity.ImageClickListener handler){

        imageClickHandler=handler;
    }
    public void setDateButtonClickListener(OnBoardCustomerActivity.DatePicker handler){

        DateListener=handler;
    }

    public void setSignatureButtonClick(OnBoardCustomerActivity.SignatureClickListen handler){

        SignatureClick=handler;
    }
    public void setGenderCheckBoxClicklistener(OnBoardCustomerActivity.GenderCheckBoxClicklistener handler){

        GenderCheckBoxHandler=handler;

    }

    public void setFingerPrinntClickListner(OnBoardCustomerActivity.FingerPrintRequestClickListener handler){

        fingerClick=handler;
    }


    public void setPicturesImageClickListener(OnBoardCustomerActivity.PicturesImageClickListener handler){

        PicimageClickHandler=handler;
    }

    public void setTextWather(OnBoardCustomerActivity.floatingButton hadler){

    floatingbutton=hadler;
}
    public void setPicsSignatureClickListener(OnBoardCustomerActivity.PictruesSignatureClickListen handler){

        PicsSignatureClick=handler;
    }

    //public FragmentManager getSupportFragmentManager() {
       // return supportFragmentManager;
   // }
}
