package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

/**
 * Created by bigfire on 10/29/2015.
 */
public class FormEditText extends FormWidget
{
    protected TextView _label;
    protected EditText _input;
    protected LinearLayout linearLayout;
    LayoutInflater inflator;

    public FormEditText( Context context, String property ,String tag)
    {
        super( context, property,tag );

         inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            Typefacer typefacer=new Typefacer();

        linearLayout=(LinearLayout)inflator.inflate(R.layout.linear_layout_style, null);

        _label =(TextView)inflator.inflate(R.layout.textview_style_layout, null);
        _label.setText(getDisplayText());
        _label.setTypeface(typefacer.squareLight());

        defaultLayoutParams.setMargins(5, 5, 5, 5);
       // _label.setLayoutParams(defaultLayoutParams);

        _input = (EditText)inflator.inflate(R.layout.edit_text_style, null);
        _input.setTypeface(typefacer.squareLight());

        _input.setTag(tag);
       // _layout.addView(_label);
      //  _layout.addView( _input );
        linearLayout.addView(_label);
        linearLayout.addView(_input);
        _layout.addView( linearLayout );

       // _layout.setLayoutParams(defaultLayoutParams);
    }

    @Override
    public String getValue(){
        return _input.getText().toString();
    }

    @Override
    public void setValue( String value ) {
        _input.setText(value );
    }

    @Override
    public void setHint( String value ){
        _input.setHint(value.toLowerCase());
    }


}

