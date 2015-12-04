package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.content.Context;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by bigfire on 12/4/2015.
 */
public class FormMultiCheckBox extends FormWidget    {
    protected TextView _label;

    protected int 			_priority;
    protected CheckBox		_checkbox,_checkbox1;

    public FormMultiCheckBox(Context context, String property,String tag,int size,List<String> stringList) {
        super( context, property ,tag);
        _label = new TextView( context );
        _label.setText(getDisplayText());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        RelativeLayout.LayoutParams newParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);


        params.gravity = Gravity.START;

        LinearLayout buttonLayout=new LinearLayout(context);
        buttonLayout.setBackgroundColor(0x00000000);
        buttonLayout.setOrientation(LinearLayout.VERTICAL);
        // LinearLayout buttonLayoutR=new LinearLayout(this);
        newParams.addRule(RelativeLayout.RIGHT_OF, 1);
        newParams.setMargins(2, 3, 2, 2);

        buttonLayout.setLayoutParams(params);
        for (int i=0;i<size;i++){


            _checkbox = new CheckBox(context);
            _checkbox.setText(stringList.get(i));
            _checkbox.setTag(stringList.get(i));

            buttonLayout.addView(_checkbox);
        }
        buttonLayout.setTag(tag);

        _layout.addView(_label);
        _layout.addView(buttonLayout);
    }

    @Override
    public String getValue() {
        return String.valueOf( _checkbox.isChecked() ? "1" : "0" );
    }

    public void setValue( String value ) {
        _checkbox.setChecked( value.equals("1") );
    }




}
