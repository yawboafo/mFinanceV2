package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nfortics.mfinanceV2.Activities.OnBoardCustomerActivity;
import com.nfortics.mfinanceV2.R;
import com.nfortics.mfinanceV2.Typefacer;

/**
 * Created by bigfire on 11/9/2015.
 */
public class FormDatePicker extends FormWidget {

    protected TextView _label,_label2;
    protected Button _input;
    protected EditText _editDate;
    LayoutInflater inflator;
    protected LinearLayout linearLayout;
    public FormDatePicker(Context context, String property ,String tag) {


        super(context, property, tag);

        inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Typefacer typefacer=new Typefacer();



        linearLayout=(LinearLayout)inflator.inflate(R.layout.linear_layout_style, null);

        _editDate=(EditText)inflator.inflate(R.layout.date_picker_layout, null);
        _editDate.setTypeface(typefacer.squareLight());

        _editDate.setTag(tag);


        linearLayout.addView(_editDate);
        _layout.addView(linearLayout);




    }

    @Override
    public void setDateButtonClickListener(OnBoardCustomerActivity.DatePicker handler) {
        super.setDateButtonClickListener(handler);
        _editDate.setOnClickListener(handler);
    }
}
