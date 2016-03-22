package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by bigfire on 11/11/2015.
 */
public class FormSpinner extends FormWidget{
    protected JSONObject _options;
    protected TextView _label;
    protected Spinner _spinner;
    protected Map<String, String> _propmap;
    protected ArrayAdapter<String> _adapter;
    LayoutInflater inflator;
    protected LinearLayout linearLayout;
    Typefacer typefacer=new Typefacer();
    public FormSpinner( Context context, String property, JSONObject options,String tag ) {
        super( context, property,tag );
        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout=(LinearLayout)inflator.inflate(R.layout.linear_layout_style, null);
        _options = options;

        _label = new TextView( context );
        _label.setText(getDisplayText());
        _label.setTypeface(typefacer.squareLight());
        _label.setLayoutParams(defaultLayoutParams );

        _spinner = (Spinner)inflator.inflate(R.layout.spinner_style_layout, null);

        _spinner.setLayoutParams(defaultLayoutParams );

        String p;
        String name;
        JSONArray propertyNames = options.names();

        _propmap = new HashMap<String, String>();
        _adapter = new ArrayAdapter<String>( context, android.R.layout.simple_spinner_item );
        _adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        _spinner.setAdapter(_adapter);
        _spinner.setSelection(0);
        _spinner.setTag(tag);
        linearLayout.setTag("hld"+tag);

        try{
            for( int i = 0; i < options.length(); i++ )
            {
                name =  propertyNames.getString(i);
                //Log.d("oxinbo","propertyNames.getString(i) "+propertyNames.getString(i));
                p = options.getString( name );
                //	Log.d("oxinbo","p == > "+p);
                _adapter.add( p );
                _propmap.put( p, name );
            }
        } catch( JSONException e){

        }

        linearLayout.addView(_label);
        linearLayout.addView( _spinner );
        _layout.addView( linearLayout );
        //_layout.addView( _spinner );
    }
    @Override
    public String getValue() {
        return _propmap.get( _adapter.getItem( _spinner.getSelectedItemPosition() ) );
    }

    @Override
    public void setValue(String value)
    {
        try{
            String name;
            JSONArray names = _options.names();
            for( int i = 0; i < names.length(); i++ )
            {
                name = names.getString(i);

                if( name.equals(value) )
                {
                    String item = _options.getString(name);
                    _spinner.setSelection( _adapter.getPosition(item) );
                }
            }
        } catch( JSONException e ){
            Log.i("Lykaion", e.getMessage());
        }
    }




}
