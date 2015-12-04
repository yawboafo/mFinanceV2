package com.nfortics.mfinanceV2.FormWidgetGenerator;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.TextView;

import com.nfortics.mfinanceV2.R;

/**
 * Created by bigfire on 11/30/2015.
 */
public class FormEditInflated extends FormWidget {

    protected TextView _label;
    protected EditText _input;

    public FormEditInflated(Context context, String name, String tag) {
        super(context, name, tag);
        LayoutInflater li = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        TextView test = (TextView)li.inflate(R.layout.edit_text_style, null);

    }
}
