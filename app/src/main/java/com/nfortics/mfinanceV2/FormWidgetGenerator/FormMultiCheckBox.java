package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

import java.util.List;

/**
 * Created by bigfire on 12/4/2015.
 */
public class FormMultiCheckBox extends FormWidget    {
    protected TextView _label;

    protected int 			_priority;
    protected CheckBox		_checkbox,_checkbox1;
    LayoutInflater inflator;
    protected LinearLayout linearLayout;
    Typefacer typefacer;



    public FormMultiCheckBox(Context context, String property,String tag,int size,List<String> stringList) {
        super( context, property ,tag);
        typefacer=new Typefacer();
        _label = new TextView( context );
        _label.setText(getDisplayText());

        _label.setTypeface(typefacer.squareLight());

        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        linearLayout=(LinearLayout)inflator.inflate(R.layout.linear_layout_style, null);



        LinearLayout buttonLayout=new LinearLayout(context);
        buttonLayout.setBackgroundColor(0x00000000);
        buttonLayout.setOrientation(LinearLayout.VERTICAL);



        for (int i=0;i<size;i++){


            _checkbox = new CheckBox(context);
            _checkbox.setText(stringList.get(i));
            _checkbox.setTag(stringList.get(i));

            buttonLayout.addView(_checkbox);
        }
        buttonLayout.setTag(tag);

        linearLayout.addView(_label);
        linearLayout.addView(buttonLayout);
        _layout.addView(linearLayout);

    }

    @Override
    public String getValue() {
        return String.valueOf( _checkbox.isChecked() ? "1" : "0" );
    }

    public void setValue( String value ) {
        _checkbox.setChecked( value.equals("1") );
    }




}
