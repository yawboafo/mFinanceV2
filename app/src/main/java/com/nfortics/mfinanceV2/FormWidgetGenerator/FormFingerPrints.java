package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.widget.Button;

import com.nfortics.mfinanceV2.Activities.OnBoardCustomerActivity;
import com.nfortics.mfinanceV2.R;

/**
 * Created by bigfire on 11/10/2015.
 */
public class FormFingerPrints  extends FormWidget {

    protected Button _input;

    public FormFingerPrints(Context context, String property ,String tag) {


        super(context, property, tag);




        Drawable img = context.getResources().getDrawable( R.drawable.calendar );
        img.setBounds(0, 0, 60, 60);





        _input=new Button(context);
        _input.setLayoutParams(defaultLayoutParams);

        _input.setTag(tag);

        _input.setText("Click to capture finger Prints");
        _layout.addView(_input);




    }


    @Override
    public void setFingerPrinntClickListner(OnBoardCustomerActivity.FingerPrintRequestClickListener handler) {
        super.setFingerPrinntClickListner(handler);

        _input.setOnClickListener(handler);
    }
}
