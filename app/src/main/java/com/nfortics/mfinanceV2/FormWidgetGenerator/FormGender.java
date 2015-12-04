package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.content.Context;
import android.view.Gravity;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Typefacer;

/**
 * Created by bigfire on 10/30/2015.
 */
public class FormGender extends FormWidget {
    protected TextView _label;

    LinearLayout ll2;
    CheckBox ch;
    CheckBox ch2;


    protected int 			_priority;
    protected CheckBox _checkbox,_checkbox1;
    public FormGender(Context context,
                      String name,
                      String tag) {
        super(context, name, tag);
        Typefacer typefacer=new Typefacer();
        _label = new TextView( context );
        _label.setTypeface(typefacer.getRoboBold(context.getAssets()));
        _label.setText("Select Gender");
        defaultLayoutParams.setMargins(2, 2, 2, 2);
        _label.setLayoutParams(defaultLayoutParams);


        ll2 = new LinearLayout(context);
        ll2.setOrientation(LinearLayout.HORIZONTAL);
        ll2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        ll2.setGravity(Gravity.CENTER);
        ll2.setWeightSum(1);
        ll2.setTag(tag);

        _checkbox = new CheckBox( context );
        _checkbox.setText("Male");
        _checkbox.setTag("male");
        _checkbox.setTypeface(typefacer.getRoboCondensedBold(context.getAssets()));

        ll2.addView(_checkbox);


        _checkbox1 = new CheckBox( context );
        _checkbox1.setText("Female");
        _checkbox1.setTag("female");
        _checkbox1.setTypeface(typefacer.getRoboCondensedBold(context.getAssets()));


        _checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (buttonView.isChecked()) {
                    _checkbox1.setChecked(false);

                }
            }
        });


        _checkbox1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(buttonView.isChecked()){
                    _checkbox.setChecked(false);

                }
            }
        });
        ll2.addView(_checkbox1);

      //  _layout.addView(_label);
        _layout.addView(ll2);
    }






}
